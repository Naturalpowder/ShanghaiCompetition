package path;

import node.Node;
import org.jetbrains.annotations.NotNull;
import util.Container;
import util.Util;
import wblut.geom.WB_PolyLine;

import java.awt.*;
import java.util.Objects;
import java.util.Scanner;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-22 16:45
 **/
public class TwoPointPath {
    private final Node start;
    private final Node end;
    private int sum = 0, min = -1, max = -1;

    public TwoPointPath(Node start, Node end) {
        this.start = start;
        this.end = end;
        sum = 1;
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }

    public void add(int add) {
        sum += add;
    }

    public int getSum() {
        return sum;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public Color getColor(boolean sameScale) {
        double section;
        if (sameScale)
            section = Container.SECTION;
        else
            section = max - min;
        return Util.getColor(section, min, sum);
    }

    public WB_PolyLine getPolyLine() {
        return new WB_PolyLine(start.getPt(), end.getPt());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TwoPointPath that = (TwoPointPath) o;
        return (start.getPt().equals(that.getStart().getPt())
                && end.getPt().equals(that.getEnd().getPt())) ||
                (start.getPt().equals(that.getEnd().getPt())
                        && end.getPt().equals(that.getStart().getPt()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(start.getPt(), end.getPt());
    }

    @Override
    public String toString() {
        return "TwoPointPath{" +
                "color=" + getColor(false) +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}