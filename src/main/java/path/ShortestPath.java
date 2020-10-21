package path;

import node.Node;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.*;
import wblut.geom.WB_PolyLine;

import java.util.ArrayList;
import java.util.List;

public class ShortestPath {
    private final List<Node> nodes, pois, buildings;
    private final List<int[]> edges;

    public ShortestPath(List<Node> pois, List<Node> buildings, List<Node> nodes, List<int[]> edges) {
        this.pois = pois;
        this.buildings = buildings;
        this.edges = edges;
        this.nodes = nodes;
    }

    public List<GraphPath<Node, DefaultWeightedEdge>> getShortestPath() {
        List<WB_PolyLine> polyLines = new ArrayList<>();
        Graph<Node, DefaultWeightedEdge> graph = getWeightedGraph();
        DijkstraShortestPath<Node, DefaultWeightedEdge> dijk = new DijkstraShortestPath<>(graph);
        List<GraphPath<Node, DefaultWeightedEdge>> list = new ArrayList<>();
        for (Node building : buildings) {
            for (Node poi : pois) {
                GraphPath<Node, DefaultWeightedEdge> path = dijk.getPath(building, poi);
                list.add(path);
                if (path == null) {
                    System.out.println("Null Caution!    " + building + " , " + poi);
                }
            }
        }
        return list;
    }

    private Graph<Node, DefaultWeightedEdge> getWeightedGraph() {
        Graph<Node, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        nodes.forEach(graph::addVertex);
        for (int[] edge : edges) {
            Node start = nodes.get(edge[0]);
            Node end = nodes.get(edge[1]);
            double weight = start.getPt().getDistance(end.getPt());
            DefaultWeightedEdge edgeUp = graph.addEdge(start, end);
            graph.setEdgeWeight(edgeUp, weight);
        }
        return graph;
    }
}
