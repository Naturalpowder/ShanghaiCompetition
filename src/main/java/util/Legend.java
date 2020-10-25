package util;

import processing.core.PConstants;
import processing.core.PGraphics;
import wblut.geom.WB_Point;

import java.awt.*;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-25 10:34
 **/
public class Legend {
    public static final String NO_SAVE = "";

    public void draw(PGraphics pg, WB_Point origin, double scale, String filePath) {
        pg.pushStyle();
        pg.pushMatrix();
        pg.scale(1, -1);
        pg.translate(0, -pg.height);
        pg.textSize(pg.width / 80f * (float) scale);
        pg.textAlign(PConstants.LEFT, PConstants.CENTER);
        pg.fill(0);
        int height = (int) (pg.height / 6 * scale), width = (int) (pg.width / 40 * scale);
        for (int i = 0; i < height + 1; i++) {
            double amount = Container.SECTION * ((double) i) / height + Container.MIN;
            Color color = Util.getColor(Container.SECTION, Container.MIN, amount);
            pg.stroke(color.getRGB());
            pg.line(origin.xf(), origin.yf() + i, origin.xf() + width, origin.yf() + i);
            if (i % (height / 5) == 0) {
                pg.pushMatrix();
                pg.scale(1, -1);
                pg.text(String.valueOf((int) amount), origin.xf() + width * 3 / 2f, -origin.yf() - i);
                pg.popMatrix();
            }
        }
        pg.popMatrix();
        pg.popStyle();
        if (!filePath.equals(NO_SAVE))
            pg.save(filePath);
    }
}