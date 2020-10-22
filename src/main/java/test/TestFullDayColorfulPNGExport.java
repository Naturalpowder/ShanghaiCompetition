package test;

import dxf.DXFImporter;
import gzf.gui.CameraController;
import node.Node;
import path.Manage;
import path.TwoPointPath;
import processing.core.PApplet;
import processing.core.PGraphics;
import transform.CsvPoi2Node;
import transform.Path2Node;
import util.Container;
import wblut.geom.*;
import wblut.processing.WB_Render2D;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TestFullDayColorfulPNGExport extends PApplet {
    private CameraController camera;
    private WB_Render2D render;
    private List<WB_Circle> buildings;
    private List<WB_PolyLine> roads;
    private List<TwoPointPath> paths;
    private List<WB_Point> pois;
    private final static String dxfPath = "src/main/data/1022.dxf";
    private final static String poiPath = "src/main/data/poi1022.csv";
    private final float scale = 2;

    public static void main(String[] args) {
        PApplet.main("test.TestFullDayColorfulPNGExport");
    }

    @Override
    public void settings() {
        size(4000, 3000);//, PDF, "src/main/data/demo.pdf"
    }

    @Override
    public void setup() {
        long start = System.currentTimeMillis();
//        pGraphics = createGraphics(4000, 3000);
        PGraphics pGraphics = this.g;
        render = new WB_Render2D(pGraphics);
        DXFImporter importer = new DXFImporter(dxfPath);
        roads = importer.getPolyLines("path");
        buildings = importer.getCircles("building").stream().map(e -> new WB_Circle(e.getCenter(), 5 * scale)).collect(Collectors.toList());

        //pois
        DXFImporter dxfImporter = new DXFImporter(dxfPath);
        WB_Polygon boundary = dxfImporter.getPolygons("boundary").get(0);
        Path2Node nodes = new CsvPoi2Node(poiPath);
        pois = nodes.getNodes().stream().map(Node::getPt).collect(Collectors.toList());
        List<WB_Point> rest = pois.stream().filter(e -> !WB_GeometryOp.contains2D(e, boundary)).collect(Collectors.toList());
        pois.removeAll(rest);

        Container.TIME = 0;
        Manage manage = new Manage(poiPath, dxfPath);
        paths = manage.getRandomShortestPathColorful();
        addPaths(1);
        addPaths(2);
        setMinMax(paths);
        System.out.println("FinalPath = " + paths.size());
        drawImage(pGraphics);
        long end = System.currentTimeMillis();
        System.out.println("Cost Time = " + (end - start) / 1000. + " s");
    }

    private void addPaths(int num) {
        Container.TIME = num;
        Manage manage = new Manage(poiPath, dxfPath);
        List<TwoPointPath> twoPointPaths = manage.getRandomShortestPathColorful();
        for (TwoPointPath path : twoPointPaths) {
            if (paths.contains(path)) {
                paths.get(paths.indexOf(path)).add(path.getSum());
            } else
                paths.add(path);
        }
    }

    private void setMinMax(List<TwoPointPath> list) {
        list.sort(Comparator.comparingInt(TwoPointPath::getSum));
        for (TwoPointPath path : list) {
            path.setMax(list.get(list.size() - 1).getSum());
            path.setMin(list.get(0).getSum());
        }
        System.out.println("Max = " + list.get(0).getMax());
        System.out.println("Min = " + list.get(0).getMin());
    }

    @Override
    public void draw() {
    }

    private void drawImage(PGraphics pg) {
        pg.beginDraw();
        pg.strokeCap(SQUARE);
        pg.scale(1, -1);
        pg.background(255);
        pg.translate(pg.width / 2f, -pg.height / 2f);
        //Road
        pg.strokeWeight(15 * scale);
        pg.stroke(0, 4);
        roads.forEach(e -> render.drawPolyLine2D(e));
        pg.pushStyle();
        //Poi
        pg.fill(0, 0, 255, 20);
        pg.noStroke();
        render.drawPoint2D(pois, 5 * scale);
        //Building
        pg.stroke(0, 80);
        pg.strokeWeight(4 * scale);
        buildings.forEach(e -> render.drawCircle2D(e));
        //Path
        pg.strokeWeight(5 * scale);
        for (TwoPointPath path : paths) {
            pg.stroke(path.getColor(false).getRGB());
            render.drawPolyLine2D(path.getPolyLine());
        }
        pg.popStyle();
        pg.endDraw();
        pg.save("src/main/data/1023/Colorful_FullDay.png");
        System.out.println("Save Image!");
        exit();
    }
}