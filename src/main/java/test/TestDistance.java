package test;

import wblut.geom.*;

public class TestDistance {
    public static void main(String[] args) {
        WB_Point p = new WB_Point(7, 4);
        WB_Point a = new WB_Point(0, 0);
        WB_Point b = new WB_Point(4, 0);
        WB_Point m = WB_GeometryOp.getClosestPoint3D(p, new WB_PolyLine(a, b));
        double d = WB_GeometryOp.getDistance3D(p, new WB_PolyLine(a, b));
        System.out.println(m);
        System.out.println(d);
    }
}
