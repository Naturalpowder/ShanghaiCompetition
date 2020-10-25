package test;

import path.Manage;
import path.TwoPointPath;
import processing.core.PApplet;
import util.Legend;

import java.util.List;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-25 10:38
 **/
public class TestLegend extends PApplet {
    private Legend legend;

    public static void main(String[] args) {
        PApplet.main("test.TestLegend");
    }

    @Override
    public void settings() {
        size(2000, 1200);
    }

    @Override
    public void setup() {
        legend = new Legend();
    }

    @Override
    public void draw() {
        background(255);
        legend.draw(this.g);
        exit();
    }
}
