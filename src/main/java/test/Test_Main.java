package test;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-21 20:23
 **/

import dxf.DXFImporter;
import gzf.gui.CameraController;
import node.Node;
import path.Manage;
import processing.core.PApplet;
import wblut.geom.WB_Circle;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.processing.WB_Render;

import java.util.List;
import java.util.stream.Collectors;


public class Test_Main extends PApplet {
    private CameraController camera;
    private WB_Render render;
    private List<WB_Circle> buildings;
    private List<WB_PolyLine> roads;
    private List<WB_PolyLine> paths;
    private List<WB_Point> pois;
    private final static String dxfPath = "src/main/data/1021.dxf";
    private final static String poiPath = "src/main/data/poi1021.csv";

    public static void main(String[] args) {
        PApplet.main("test.Test_Main");
    }

    @Override
    public void settings() {
        size(800, 600, P3D);
    }

    @Override
    public void setup() {
        camera = new CameraController(this);
        camera.top();
        render = new WB_Render(this);
        DXFImporter importer = new DXFImporter(dxfPath);
        roads = importer.getPolyLines("path");
        buildings = importer.getCircles("building").stream().map(e -> new WB_Circle(e.getCenter(), 10)).collect(Collectors.toList());
        Manage manage = new Manage(poiPath, dxfPath);
        pois = manage.getPois().stream().map(Node::getPt).collect(Collectors.toList());
        paths = manage.getRandomShortestPath();
    }

    @Override
    public void draw() {
        background(255);
        camera.drawSystem(100);
        //Road
        strokeWeight(15);
        stroke(0, 3);
        render.drawPolylineEdges(roads);
        pushStyle();
        //Path
        stroke(255, 0, 0, 1);
        strokeWeight(3);
        render.drawPolylineEdges(paths);
        //Poi
        stroke(0, 0, 255, 10);
        strokeWeight(3);
        render.drawPoint(pois, 10);
        //Building
        stroke(0, 100);
        strokeWeight(3);
        render.drawCircle(buildings);
        popStyle();
    }
}

