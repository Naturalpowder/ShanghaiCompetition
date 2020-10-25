package util;

import path.TwoPointPath;
import processing.core.PConstants;
import processing.core.PGraphics;
import sun.plugin2.util.ColorUtil;
import wblut.geom.WB_Point;

import java.awt.*;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-25 10:34
 **/
public class Legend {
    private final static double MIN = 1, SECTION = 8821.;//8821. , 6969.

    public Legend() {
        initial();
    }

    private void initial() {
    }

    public void draw(PGraphics pg) {
        int scale = 2;
        pg.pushStyle();
        pg.pushMatrix();
        pg.scale(1, -1);
        pg.translate(0, -pg.height);
        pg.textSize(10 * scale);
        pg.textAlign(PConstants.LEFT, PConstants.CENTER);
        pg.fill(0);
        int height = pg.height / 6 * scale, width = pg.width / 40 * scale;
        WB_Point origin = new WB_Point(pg.width / 2., pg.height / 2.);
        for (int i = 0; i < height + 1; i++) {
            double amount = SECTION * ((double) i) / height + MIN;
            Color color = Util.getColor(SECTION, MIN, amount);
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
        pg.save("src/main/data/1023_2/FullDay.png");
//        pg.save("src/main/data/1023_2/Morning_Noon_Evening.png");
    }
}