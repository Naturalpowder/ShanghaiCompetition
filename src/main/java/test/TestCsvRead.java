package test;

import gzf.gui.CameraController;
import node.Node;
import processing.core.PApplet;
import transform.CsvPoi2Node;
import wblut.processing.WB_Render;

import java.util.List;

public class TestCsvRead extends PApplet {
    CameraController camera;
    WB_Render render;
    List<Node> nodes;

    public static void main(String[] args) {
        PApplet.main("test.TestCsvRead");
    }

    @Override
    public void settings() {
        size(800, 600, P3D);
    }

    @Override
    public void setup() {
        CsvPoi2Node csvPoi2Node = new CsvPoi2Node("src/main/data/Poi_WGS84.csv");
        nodes = csvPoi2Node.getNodes();
        camera = new CameraController(this);
        render = new WB_Render(this);
    }

    @Override
    public void draw() {
        background(255);
        camera.drawSystem(100);
        camera.top();
        for (Node node : nodes) {
            render.drawPoint(node.getPt(), 10);
        }
    }
}
