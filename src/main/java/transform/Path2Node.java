package transform;

import node.Edge;
import node.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Path2Node {
    protected final List<Node> nodes;
    protected List<Edge> edges;

    public Path2Node(String filePath) {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
        set(filePath);
        initial();
    }

    private void initial() {
        nodes.forEach(Node::initial);
    }

    protected abstract void set(String filePath);

    public List<Node> getNodes() {
        return nodes;
    }

    public List<int[]> getEdges() {
        return edges.stream().map(Edge::getEdge).collect(Collectors.toList());
    }
}
