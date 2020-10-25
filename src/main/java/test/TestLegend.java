package test;

import path.Manage;
import path.TwoPointPath;
import processing.core.PApplet;
import util.Legend;
import wblut.geom.WB_Point;

import java.util.List;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-25 10:38
 **/
public class TestLegend extends PApplet {

    public static void main(String[] args) {
        PApplet.main("test.TestLegend");
    }

    @Override
    public void settings() {
        size(800, 600);
    }

    @Override
    public void setup() {
    }

    @Override
    public void draw() {
        background(255);
        new Legend().draw(this.g, new WB_Point(width / 2., height / 2.), 2, "src/main/data/1023_2/FullDay.png");
        exit();
    }
}
