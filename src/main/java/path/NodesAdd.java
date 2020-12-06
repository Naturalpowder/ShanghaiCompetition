package path;

import node.Node;
import node.NodePath;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-19 09:18
 **/
public class NodesAdd {
    private final List<Node> pois, buildings, path;
    private final List<int[]> pathEdges;
    private List<Node> nodes;
    private List<int[]> edges, shortEdges;

    public NodesAdd(List<Node> pois, List<Node> buildings, List<Node> path, List<int[]> pathEdges) {
        this.pois = pois;
        this.buildings = buildings;
        this.path = path;
        this.pathEdges = pathEdges;
    }

    public void initial() {
        nodes = new ArrayList<>(path);
        nodes.addAll(pois);
        nodes.addAll(buildings);
        edges = new ArrayList<>();
        addNodes(pois);
        addNodes(buildings);
        shortEdges = new ArrayList<>(edges);
        edges.addAll(pathEdges);
//        adapt();
    }

    private void adapt() {
        List<Node> copy = new ArrayList<>(nodes);
        copy.removeAll(pois);
        copy.removeAll(buildings);
        path.addAll(copy);
    }

    private void addNodes(List<Node> extra) {
        for (Node node : extra) {
            addNode(node);
        }
    }

    private void addNode(Node node) {
        int[] edge = getMinDistanceEdge(node);
        WB_Point p1 = nodes.get(edge[0]).getPt();
        WB_Point p2 = nodes.get(edge[1]).getPt();
        WB_PolyLine pl = new WB_PolyLine(p1, p2);
        WB_Point p = WB_GeometryOp.getClosestPoint3D(node.getPt(), pl);
        if (new NodePath(p).equals(new NodePath(p1))) {
            edges.add(new int[]{edge[0], nodes.indexOf(node)});
        } else if (new NodePath(p).equals(new NodePath(p2))) {
            edges.add(new int[]{edge[1], nodes.indexOf(node)});
        } else {
            Node middle = new NodePath(p);
            nodes.add(middle);
            pathEdges.remove(edge);
            pathEdges.add(new int[]{edge[0], nodes.indexOf(middle)});
            pathEdges.add(new int[]{edge[1], nodes.indexOf(middle)});
            edges.add(new int[]{nodes.indexOf(node), nodes.indexOf(middle)});
        }
    }

    private int[] getMinDistanceEdge(Node node) {
        return Collections.min(pathEdges, (o1, o2) -> {
            Node a = nodes.get(o1[0]);
            Node b = nodes.get(o1[1]);
            Node c = nodes.get(o2[0]);
            Node d = nodes.get(o2[1]);
            WB_PolyLine pl1 = new WB_PolyLine(a.getPt(), b.getPt());
            WB_PolyLine pl2 = new WB_PolyLine(c.getPt(), d.getPt());
            double dis1 = WB_GeometryOp.getDistance3D(node.getPt(), pl1);
            double dis2 = WB_GeometryOp.getDistance3D(node.getPt(), pl2);
            return (int) ((dis1 - dis2) * 10000);
        });
    }

    public List<int[]> getPathEdges() {
        return pathEdges;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<int[]> getEdges() {
        return edges;
    }

    public List<int[]> getShortEdges() {
        return shortEdges;
    }
}
