package util;

import node.Node;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-19 17:23
 **/
public class Util {

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
}
