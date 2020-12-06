package util;

import wblut.geom.*;

import java.util.ArrayList;
import java.util.List;

public class Divide {
    private final WB_PolyLine polyLine;
    private double length;

    public Divide(final WB_PolyLine polyLine) {
        if (polyLine instanceof wblut.geom.WB_Polygon) {
            List<WB_Coord> coords = polyLine.getPoints().toList();
            coords.add(coords.get(0));
            this.polyLine = new WB_PolyLine(coords);
        } else
            this.polyLine = polyLine;
        initial();
    }

    private void initial() {
        setLength();
    }

    public List<WB_Point> getDividedPoints(double len) {
        int num = (int) (length / len);
        return getDividedPoints(num);
    }

    public List<WB_Point> getDividedPoints(int num) {
        if (num <= 1)
            return new ArrayList<>();
        List<WB_Point> points = new ArrayList<>();
        if (polyLine.getPoint(0).equals(polyLine.getPoint(polyLine.getNumberOfPoints() - 1)))
            points.add(polyLine.getPoint(0));
        for (int i = 0; i < num - 1; i++)
            points.add(getParametricPoint((i + 1.) / num));
        return points;
    }

    public WB_Point getParametricPoint(final double t) {
        if (t < 0 || t > 1) {
            throw new IllegalArgumentException("T is out of the range!");
        }
        if (t == 0)
            return polyLine.getPoint(0);
        if (t == 1)
            return polyLine.getPoint(polyLine.getNumberOfPoints() - 1);
        double dis = 0;
        double orDis = length * t;
        int i = 0;
        while (dis < orDis)
            dis += polyLine.getSegment(i++).getLength();
        WB_Vector v = new WB_Vector(polyLine.getPoint(i - 1).sub(polyLine.getPoint(i)));
        v.normalizeSelf();
        return new WB_Point(v.mul(dis - orDis).add(polyLine.getPoint(i)));
    }


    private void setLength() {
        length = 0;
        int i = 0;
        polyLine.getNumberSegments();
        while (i < polyLine.getNumberSegments())
            length += polyLine.getSegment(i++).getLength();
    }

    public double getLength() {
        return length;
    }

    public WB_PolyLine getPolyLine() {
        return polyLine;
    }
}
