package test;

import dxf.DXFImporter;
import gzf.gui.CameraController;
import node.Node;
import path.BuildingCenterPaths;
import path.Manage;
import path.TwoPointPath;
import processing.core.PApplet;
import processing.core.PGraphics;
import util.Container;
import util.Legend;
import util.Util;
import wblut.geom.WB_Circle;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.processing.WB_Render2D;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestColorfulEachBuilding extends PApplet {
    private CameraController camera;
    private WB_Render2D render;
    private WB_Circle building;
    private List<WB_PolyLine> roads;
    private List<TwoPointPath> paths;
    private List<WB_Point> pois;
    private final static String dxfPath = "src/main/data/1023.dxf";
    private final static String poiPath = "src/main/data/poi1022.csv";
    private final float scale = 2;

    public static void main(String[] args) {
        PApplet.main("test.TestColorfulEachBuilding");
    }

    @Override
    public void settings() {
        size(400, 300);//, PDF, "src/main/data/demo.pdf"
    }

    @Override
    public void setup() {
        long start = System.currentTimeMillis();
        DXFImporter importer = new DXFImporter(dxfPath, DXFImporter.GBK);
        roads = importer.getPolyLines("path");
        Manage manage = new Manage(poiPath, dxfPath);
        List<BuildingCenterPaths> centerPaths = manage.getBuildingCenterPath();
        int max = -1;
        for (int i = 0; i < centerPaths.size(); i++) {
            max = setBuildingCenterPaths(manage, centerPaths, max, i);
        }
        System.out.println("Final Max = " + max);
        long end = System.currentTimeMillis();
        System.out.println("Cost Time = " + (end - start) / 1000. + " s");
    }

    private int setBuildingCenterPaths(Manage manage, List<BuildingCenterPaths> centerPaths, int max, int i) {
        BuildingCenterPaths centerPath = centerPaths.get(i);
        PGraphics pGraphics = createGraphics(4000, 3000);
        render = new WB_Render2D(pGraphics);
        paths = manage.getBuildingNRandomShortestPathColorful(centerPath);
        building = new WB_Circle(centerPath.getBuilding().getPt(), 10 * scale);
        pois = centerPath.getPois().stream().map(Node::getPt).collect(Collectors.toList());
        String name = centerPath.getBuilding().getName();
        drawImage(pGraphics, "src/main/data/1025/SameScale/" + i + "_Colorful_" + Container.getName() + "_" + name + ".png", true);
        if (paths.get(0).getMax() > max)
            max = paths.get(0).getMax();
        System.out.println("--------------- " + ((double) (i + 1)) / centerPaths.size() * 100 + " % ------------------");
        return max;
    }

    private void drawImage(PGraphics pg, String filePath, boolean sameScale) {
        pg.beginDraw();
        pg.pushStyle();
        pg.pushMatrix();
        pg.strokeCap(SQUARE);
        pg.scale(1, -1);
        pg.background(255);
        pg.translate(pg.width / 2f, -pg.height / 2f);
        //Road
        pg.strokeWeight(15 * scale);
        pg.stroke(0, 4);
        roads.forEach(e -> render.drawPolyLine2D(e));
        //Poi
        pg.fill(0, 0, 255, 20);
        pg.noStroke();
        render.drawPoint2D(pois, 5 * scale);
        //Building
        pg.stroke(0, 80);
        pg.strokeWeight(4 * scale);
        render.drawCircle2D(building);
        //Path
        pg.strokeWeight(5 * scale);
        for (TwoPointPath path : paths) {
            pg.stroke(path.getColor(sameScale).getRGB());
            render.drawPolyLine2D(path.getPolyLine());
        }
        pg.popMatrix();
        pg.popStyle();
        new Legend().draw(pg, new WB_Point(pg.width * .8, pg.height * .2), .6, Legend.NO_SAVE);
        pg.endDraw();
        pg.save(filePath);
        System.out.println("Save Image " + filePath + " !");
        exit();
    }
}