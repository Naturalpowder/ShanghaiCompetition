package util;

import node.Node;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import path.Path;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-19 17:23
 **/
public class Util {

    public static WB_PolyLine PathToPolyLine(Path path) {
        return new WB_PolyLine(path.getPath().getVertexList().stream().map(Node::getPt).collect(Collectors.toList()));
    }

    public static WB_PolyLine toPolyLine(GraphPath<Node, DefaultWeightedEdge> p) {
        List<Node> nodes = p.getVertexList();
        List<WB_Point> points = nodes.stream().map(e -> new WB_Point(e.getPt())).collect(Collectors.toList());
        return new WB_PolyLine(points);
    }

    public static WB_PolyLine toPolyLineWithoutEnds(GraphPath<Node, DefaultWeightedEdge> p) {
        List<Node> nodes = p.getVertexList();
        List<WB_Point> points = nodes.stream().map(e -> new WB_Point(e.getPt())).collect(Collectors.toList());
        points.remove(0);
        points.remove(points.size() - 1);
        return new WB_PolyLine(points);
    }

    public static Color getColor(double section, double min, double amount) {
        double v = Math.sqrt((amount - min) / section);
        float hue = (float) (v * .3333 + .6667);
        float brightness = (float) (v * 1 + 0);
        return Color.getHSBColor(hue, brightness, 1);
    }
}
