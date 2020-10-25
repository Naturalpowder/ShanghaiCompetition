package test;

import dxf.DXFImporter;
import gzf.gui.CameraController;
import node.Node;
import node.NodeBuilding;
import path.BuildingCenterPaths;
import path.Manage;
import path.TwoPointPath;
import processing.core.PApplet;
import processing.core.PGraphics;
import transform.CsvPoi2Node;
import transform.Path2Node;
import util.Container;
import util.Legend;
import wblut.geom.*;
import wblut.processing.WB_Render2D;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TestColorfulEachBuilding extends PApplet {
    private CameraController camera;
    private final static String dxfPath = "src/main/data/1023.dxf";
    private final static String poiPath = "src/main/data/poi1022.csv";
    private PathDrawing pathDrawing;

    public static void main(String[] args) {
        PApplet.main("test.TestColorfulEachBuilding");
    }

    @Override
    public void settings() {
        size(400, 300);//, PDF, "src/main/data/demo.pdf"
    }

    @Override
    public void setup() {
        pathDrawing = new PathDrawing(this, dxfPath, poiPath);
        pathDrawing.set(false);//FullDay
    }

    public void draw() {
        pathDrawing.drawItems("src/main/data/1025/SameScale/", true);
    }

    static class PathDrawing {
        private WB_Render2D render;
        private List<WB_PolyLine> roads;
        private List<WB_Point> pois;
        private final List<Item> items;
        private final float scale = 2;
        private final String dxfPath, poiPath;
        private boolean fullDay;

        private final PApplet app;

        public PathDrawing(PApplet app, String dxfPath, String poiPath) {
            this.app = app;
            this.dxfPath = dxfPath;
            this.poiPath = poiPath;
            items = new ArrayList<>();
        }

        public void set(boolean fullDay) {
            this.fullDay = fullDay;
            long start = System.currentTimeMillis();
            setPoisAndRoads();
            addManages();
            long end = System.currentTimeMillis();
            System.out.println("Manage Cost Time = " + (end - start) / 1000. + " s");
        }

        private void addManages() {
            if (fullDay) {
                int[] times = new int[]{Container.MORNING, Container.NOON, Container.EVENING};
                for (int time : times)
                    addManage(time);
            } else
                addManage(Container.TIME);
            for (Item item : items) {
                setMinMax(item.getPaths());
            }
        }

        private void setPoisAndRoads() {
            DXFImporter importer = new DXFImporter(dxfPath, DXFImporter.GBK);
            roads = importer.getPolyLines("path");
            //pois
            WB_Polygon boundary = importer.getPolygons("boundary").get(0);
            Path2Node nodes = new CsvPoi2Node(poiPath);
            pois = nodes.getNodes().stream().map(Node::getPt).collect(Collectors.toList());
            List<WB_Point> rest = pois.stream().filter(e -> !WB_GeometryOp.contains2D(e, boundary)).collect(Collectors.toList());
            pois.removeAll(rest);
        }

        public void drawItems(String folderPath, boolean sameScale) {
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                PGraphics pGraphics = app.createGraphics(4000, 3000);
                render = new WB_Render2D(pGraphics);
                WB_Circle building = new WB_Circle(item.getBuilding().getPt(), 10 * scale);
                List<TwoPointPath> paths = item.getPaths();
                String name = item.getBuilding().getName();
                if (fullDay)
                    Container.TIME = -1;
                else {
                    pois = item.getPois().stream().map(Node::getPt).collect(Collectors.toList());
                }
                String filePath = folderPath + i + "_Colorful_" + Container.getName() + "_" + name + ".png";
                drawImage(pGraphics, filePath, sameScale, building, paths);
            }
            app.exit();
        }

        private void setMinMax(List<TwoPointPath> list) {
            list.sort(Comparator.comparingInt(TwoPointPath::getSum));
            for (TwoPointPath path : list) {
                path.setMax(list.get(list.size() - 1).getSum());
                path.setMin(list.get(0).getSum());
            }
            System.out.println("Final Max = " + list.get(0).getMax());
            System.out.println("Final Min = " + list.get(0).getMin());
        }

        private void addManage(int num) {
            Container.TIME = num;
            Manage manage = new Manage(poiPath, dxfPath);
            List<BuildingCenterPaths> centerPaths = manage.getBuildingCenterPath();
            for (int i = 0; i < centerPaths.size(); i++) {
                setBuildingCenterPaths(manage, centerPaths, i);
            }
        }

        private void setBuildingCenterPaths(Manage manage, List<BuildingCenterPaths> centerPaths, int i) {
            BuildingCenterPaths centerPath = centerPaths.get(i);
            List<TwoPointPath> paths = manage.getBuildingNRandomShortestPathColorful(centerPath);
            if (centerPaths.size() > items.size()) {
                Item item = new Item(centerPath);
                item.add(paths);
                items.add(item);
            } else {
                List<NodeBuilding> buildings = items.stream().map(Item::getBuilding).collect(Collectors.toList());
                int index = buildings.indexOf(centerPath.getBuilding());
                items.get(index).add(paths);
            }
        }

        private void drawImage(PGraphics pg, String filePath, boolean sameScale, WB_Circle building, List<TwoPointPath> paths) {
            pg.beginDraw();
            pg.background(255);
            drawElements(pg, sameScale, building, paths);
            pg.endDraw();
            pg.save(filePath);
            System.out.println("Save Image " + filePath + " !");
        }

        private void drawElements(PGraphics pg, boolean sameScale, WB_Circle building, List<TwoPointPath> paths) {
            pg.pushStyle();
            pg.pushMatrix();
            pg.strokeCap(SQUARE);
            pg.scale(1, -1);
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
        }

        static class Item {
            private final List<TwoPointPath> paths;
            private final NodeBuilding building;
            private final List<Node> pois;

            Item(BuildingCenterPaths centerPaths) {
                this.paths = new ArrayList<>();
                this.building = centerPaths.getBuilding();
                pois = centerPaths.getPois();
            }

            public void add(List<TwoPointPath> paths) {
                for (TwoPointPath path : paths)
                    if (!this.paths.contains(path))
                        this.paths.add(path);
                    else
                        this.paths.get(this.paths.indexOf(path)).add(path.getSum());
            }

            public List<Node> getPois() {
                return pois;
            }

            public List<TwoPointPath> getPaths() {
                return paths;
            }

            public NodeBuilding getBuilding() {
                return building;
            }
        }
    }
}