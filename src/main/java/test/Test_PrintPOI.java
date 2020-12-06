package test;

import node.Node;
import node.NodePoi;
import processing.core.PApplet;
import transform.CsvPoi2Node;
import transform.Path2Node;

import java.awt.print.Paper;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Test_PrintPOI extends PApplet {
    private final static String poiPath = "src/main/data/poi1022.csv";
    private List<Node> nodes;
    private int index = 0, step = 20;

    public static void main(String[] args) {
        PApplet.main("test.Test_PrintPOI");
    }

    @Override
    public void settings() {
        size(800, 600, P2D);
    }

    @Override
    public void setup() {
        Path2Node path2Node = new CsvPoi2Node(poiPath);
        nodes = path2Node.getNodes();
        frameRate(2);
    }

    @Override
    public void draw() {
        background(255);
        Field[] fields = NodePoi.class.getDeclaredFields();
        String[] strings = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            try {
                strings[i] = fields[i].get(nodes.get(index)).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        index++;

        textSize(10);
        fill(0);
        for (int i = 0; i < strings.length; i++) {
            String str = strings[i];
            text(str, (i + 1) * step, height / 2f);
        }

        if (index == nodes.size())
            exit();
    }
}
