package test;

import dxf.DXFImporter;
import gzf.gui.CameraController;
import node.Node;
import path.Manage;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.pdf.*;
import wblut.geom.WB_Circle;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.processing.WB_Render2D;

import java.util.List;
import java.util.stream.Collectors;

public class TestPNGExport extends PApplet {
    private PGraphics pGraphics;
    private CameraController camera;
    private WB_Render2D render;
    private List<WB_Circle> buildings;
    private List<WB_PolyLine> roads;
    private List<WB_PolyLine> paths;
    private List<WB_Point> pois;
    private final static String dxfPath = "src/main/data/1021.dxf";
    private final static String poiPath = "src/main/data/poi1021.csv";
    private final float scale = 2;

    public static void main(String[] args) {
        PApplet.main("test.TestPNGExport");
    }

    @Override
    public void settings() {
        size(4000, 3000);//, PDF, "src/main/data/demo.pdf"
    }

    @Override
    public void setup() {
        long start = System.currentTimeMillis();
//        pGraphics = createGraphics(4000, 3000);
        pGraphics = this.g;
        render = new WB_Render2D(pGraphics);
        DXFImporter importer = new DXFImporter(dxfPath);
        roads = importer.getPolyLines("path");
        buildings = importer.getCircles("building").stream().map(e -> new WB_Circle(e.getCenter(), 5 * scale)).collect(Collectors.toList());
        Manage manage = new Manage(poiPath, dxfPath);
        pois = manage.getPois().stream().map(Node::getPt).collect(Collectors.toList());
        paths = manage.getRandomShortestPath();
        drawImage(pGraphics);
        long end = System.currentTimeMillis();
        System.out.println("Cost Time = " + (end - start) / 1000. + " s");
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
        pg.stroke(255, 0, 0, 1);
        pg.strokeWeight(5 * scale);
        paths.forEach(e -> render.drawPolyLine2D(e));
        pg.popStyle();
        pg.endDraw();
        pg.save("src/main/data/Noon.png");
        System.out.println("Save Image!");
        exit();
    }
}