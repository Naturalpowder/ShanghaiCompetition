package test;

import com.hamoid.VideoExport;
import dxf.DXFImporter;
import node.Node;
import path.Manage;
import path.Path;
import path.Segment;
import processing.core.PApplet;
import processing.core.PGraphics;
import transform.CsvPoi2Node;
import transform.Path2Node;
import util.Container;
import util.Divide;
import util.Util;
import wblut.geom.*;
import wblut.processing.WB_Render2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Test_Animate extends PApplet {
    private WB_Render2D render;
    private VideoExport videoExport;
    private List<WB_Circle> buildings;
    private List<WB_PolyLine> roads;
    private List<WB_Point> pois;
    private Manage manage;
    private Stack<Path> paths, renderPaths;
    private List<MovePoint> movePoints;
    private final static String dxfPath = "src/main/data/1023.dxf";
    private final static String poiPath = "src/main/data/poi1022.csv";
    private final static boolean sameScale = true;
    private final static float scale = 2, step = 10f;
    private final static int sum = 500, threshold = 2;
    private int index = 0;

    public static void main(String[] args) {
        PApplet.main("test.Test_Animate");
    }

    @Override
    public void settings() {
        size(1920, 1080);
    }

    @Override
    public void setup() {
        long start = System.currentTimeMillis();
        setBuildingsAndRoads();
        setPois();

        Container.TIME = Container.NOON;
        manage = new Manage(poiPath, dxfPath);
        paths = new Stack<>();
        paths.addAll(manage.getRandomPaths());
        renderPaths = new Stack<>();
        movePoints = new ArrayList<>();
        addMovePoints(1);
        long end = System.currentTimeMillis();
        System.out.println("Cost Time = " + (end - start) / 1000. + " s");
        videoExport = new VideoExport(this);
        videoExport.startMovie();
    }

    @Override
    public void draw() {
        background(255);
        drawImage(this.g);
        drawRenderedPaths();
        if (index == threshold)
            addMovePoints(sum - 1);
        videoExport.saveFrame();
    }

    private void drawRenderedPaths() {
        List<Segment> segments = manage.animatePath(renderPaths);
        System.out.println("Paths.Size = " + paths.size());
        drawPath(this.g, segments);
        for (MovePoint mP : movePoints) {
            if (!mP.isEnd()) {
                drawMovePoint(mP);
            } else {
                mP.update(renderPaths);
                if (!paths.empty()) {
                    mP.reset(paths.pop());
                    index++;
                }
            }
        }
        if (paths.empty() && movePoints.stream().allMatch(MovePoint::isEnd))
            exit();
    }

    public void keyPressed() {
        if (keyPressed) {
            if (key == 'b' || key == 'B') {
                videoExport.endMovie();
                exit();
            }
        }
    }

    private void drawMovePoint(MovePoint mP) {
        pushStyle();
        strokeWeight(10 * scale);
        stroke(255, 0, 0, 15);
        if (index < threshold)
            render.drawPolyLine2D(mP.getPolyLine());
        popStyle();
        noStroke();
        if (index < threshold)
            fill(0, 100);
        else
            fill(100, 1);
        render.drawPoint2D(mP.getPt(), 10 * scale);
    }

    private void setBuildingsAndRoads() {
        render = new WB_Render2D(this.g);
        DXFImporter importer = new DXFImporter(dxfPath, DXFImporter.GBK);
        roads = importer.getPolyLines("path");
        buildings = importer.getCircles("building").stream().map(e -> new WB_Circle(e.getCenter(), 5 * scale)).collect(Collectors.toList());
    }

    private void addMovePoints(int sum) {
        for (int i = 0; i < sum; i++) {
            MovePoint movePoint = new MovePoint(paths.pop());
            movePoints.add(movePoint);
            if (paths.empty())
                break;
        }
    }

    private void setPois() {
        //pois
        DXFImporter dxfImporter = new DXFImporter(dxfPath, DXFImporter.GBK);
        WB_Polygon boundary = dxfImporter.getPolygons("boundary").get(0);
        Path2Node nodes = new CsvPoi2Node(poiPath);
        pois = nodes.getNodes().stream().map(Node::getPt).collect(Collectors.toList());
        List<WB_Point> rest = pois.stream().filter(e -> !WB_GeometryOp.contains2D(e, boundary)).collect(Collectors.toList());
        pois.removeAll(rest);
    }

    private void drawPath(PGraphics pg, List<Segment> segments) {
        pg.pushStyle();
        //Path
        pg.strokeWeight(5 * scale);
        for (Segment path : segments) {
            pg.stroke(path.getColor(sameScale).getRGB());
            render.drawPolyLine2D(path.getPolyLine());
        }
        pg.popStyle();
    }

    private void drawImage(PGraphics pg) {
        pg.strokeCap(SQUARE);
        pg.scale(1, -1);
        pg.background(255);
        pg.translate(pg.width / 2f, -pg.height * 5 / 12f);
        pg.scale(.5f, .5f);
        //Road
        pg.strokeWeight(15 * scale);
        pg.stroke(0, 4);
        roads.forEach(e -> render.drawPolyLine2D(e));
        pg.pushStyle();
        //Poi
        pg.fill(0, 0, 255, 10);
        pg.noStroke();
        render.drawPoint2D(pois, 5 * scale);
        //Building
        pg.stroke(0, 80);
        pg.strokeWeight(4 * scale);
        buildings.forEach(e -> render.drawCircle2D(e));
        pg.popStyle();
    }

    static class MovePoint {
        private int index = 0, times = 0;
        private List<WB_Point> pts;
        private WB_PolyLine polyLine;
        private Path path;

        public MovePoint(Path path) {
            initial(path);
        }

        private void initial(Path path) {
            this.path = path;
            polyLine = Util.PathToPolyLine(path);
            Divide divide = new Divide(polyLine);
            pts = divide.getDividedPoints(step);
        }

        public void reset(Path path) {
            initial(path);
            index = 0;
            times = 0;
        }

        public WB_Point getPt() {
            return pts.get(index++);
        }

        public WB_PolyLine getPolyLine() {
            return polyLine;
        }

        public void update(Stack<Path> renderPaths) {
            if (isEnd() && times == 0) {
                renderPaths.push(path);
                times++;
            }
        }

        public boolean isEnd() {
            return index == pts.size();
        }
    }
}