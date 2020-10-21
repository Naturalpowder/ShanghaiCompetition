package node;

import java.util.Objects;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-20 16:58
 **/
public class Edge {
    private final int start;
    private final int end;

    public Edge(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public Edge(int[] edge) {
        start = edge[0];
        end = edge[1];
    }

    public int[] getEdge() {
        return new int[]{start, end};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return (start == edge.start && end == edge.end) ||
                (start == edge.end && end == edge.start);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
