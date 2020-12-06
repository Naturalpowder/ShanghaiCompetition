package test;

import com.hamoid.VideoExport;
import dxf.DXFImporter;
import gzf.gui.CameraController;
import node.Node;
import path.Manage;
import processing.core.PApplet;
import processing.core.PGraphics;
import transform.CsvPoi2Node;
import transform.Path2Node;
import wblut.geom.*;
import wblut.processing.WB_Render2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Test_PointAdd extends PApplet {
    private WB_Render2D render;
    private VideoExport videoExport;
    private List<WB_Circle> buildings;
    private List<WB_PolyLine> roads;
    private List<WB_Point> pois;
    private Manage manage;
    private final static String dxfPath = "src/main/data/1023.dxf";
    private final static String poiPath = "src/main/data/poi1022.csv";
    private final static float scale = 2, screen = .5f;
    private double radius = 0, time = 0;
    private boolean animate2 = false, animate3 = false, animate4 = false, initialColor = false;
    private List<Color> colors;

    public static void main(String[] args) {
        PApplet.main("test.Test_PointAdd");
    }

    @Override
    public void settings() {
        size(1920, 1080);
    }

    @Override
    public void setup() {
        render = new WB_Render2D(this);
        setBuildingsAndRoads();
        setPois();
        manage = new Manage(poiPath, dxfPath);
        videoExport = new VideoExport(this);
        videoExport.startMovie();
    }

    @Override
    public void draw() {
        this.g.background(230);
        controlAnimate();
        drawImage(this.g);
        animate1(this.g);
        animate2(this.g);
        animate3(this.g);
        animate4(this.g);
        videoExport.saveFrame();
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

    private void setBuildingsAndRoads() {
        render = new WB_Render2D(this.g);
        DXFImporter importer = new DXFImporter(dxfPath, DXFImporter.GBK);
        roads = importer.getPolyLines("path");
        buildings = importer.getCircles("building").stream().map(e -> new WB_Circle(e.getCenter(), 5 * scale)).collect(Collectors.toList());
    }

    private void animate1(PGraphics pg) {
        pg.pushStyle();
        pg.fill(0, 0, 255, 50);
        pg.noStroke();
        List<WB_Point> pts = pois.stream().filter(e -> e.getDistance3D(new WB_Point()) < radius).collect(Collectors.toList());
        render.drawPoint2D(pts, 5 * scale);
        pg.popStyle();
        radius += 5;
    }

    public void keyPressed() {
        if (keyPressed) {
            if (key == 'b' || key == 'B') {
                videoExport.endMovie();
                exit();
            }
        }
    }

    private void controlAnimate() {
        if (radius * radius > .3402 * height * height / screen / screen + .25 * width * width / screen / screen)
            animate2 = true;
        if (time > 60)
            animate3 = true;
        if (time > 120) {
            animate2 = false;
            animate3 = false;
            animate4 = true;
        }
        if (time > 180)
            exit();
    }

    private void animate2(PGraphics pg) {
        if (!animate2) return;
        pg.pushStyle();
        List<WB_PolyLine> pls = manage.getShortLines();
        pg.stroke(0);
        pg.strokeWeight(1 * scale);
        pls.forEach(e -> render.drawPolyLine2D(e));
        pg.popStyle();
        time++;
    }

    private void animate3(PGraphics pg) {
        if (!animate3) return;
        pg.pushStyle();
        List<WB_PolyLine> cutLs = manage.getCutLines();
        initialColor(cutLs.size());
        pg.strokeWeight(3 * scale);
        for (int i = 0; i < cutLs.size(); i++) {
            pg.stroke(colors.get(i).getRGB());
            render.drawPolyLine2D(cutLs.get(i));
        }
        pg.popStyle();
    }

    private void animate4(PGraphics pg) {
        if (!animate4) return;
        pg.pushStyle();
        pg.strokeWeight(1 * scale);
        pg.stroke(50);
        pg.fill(0);
        List<WB_PolyLine> mLs = manage.getMergedLines();
        List<WB_Point> pts = manage.getNodes().stream().map(Node::getPt).collect(Collectors.toList());
        mLs.forEach(render::drawPolyLine2D);
        render.drawPoint2D(pts, 2 * scale);
        pg.popStyle();
        time++;
    }

    private void initialColor(int sum) {
        if (!initialColor) {
            colors = new ArrayList<>();
            for (int i = 0; i < sum; i++) {
                Color color = Color.getHSBColor((float) Math.random(), .5f, 1f);
                colors.add(color);
            }
            initialColor = true;
        }
    }

    private void drawMask(PGraphics pg) {
        pg.pushStyle();
        pg.fill(255);
        pg.ellipse(0, 0, (float) radius * 2, (float) radius * 2);
        pg.popStyle();
    }

    private void drawImage(PGraphics pg) {
        pg.strokeCap(SQUARE);
        pg.scale(1, -1);
        pg.translate(pg.width / 2f, -pg.height * 5 / 12f);
        pg.scale(screen, screen);
        drawMask(g);
        //Road
        pg.strokeWeight(15 * scale);
        pg.stroke(0, 4);
        roads.forEach(e -> render.drawPolyLine2D(e));
        pg.pushStyle();
        //Poi
        pg.fill(0, 0, 255, 50);
        pg.noStroke();
//        render.drawPoint2D(pois, 5 * scale);
        //Building
        pg.stroke(0, 80);
        pg.strokeWeight(4 * scale);
        buildings.forEach(e -> render.drawCircle2D(e));
        pg.popStyle();
    }
}