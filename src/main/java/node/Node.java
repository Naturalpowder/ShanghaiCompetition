package node;

import wblut.geom.WB_Point;

import java.util.Objects;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-17 10:05
 **/
public abstract class Node {
    protected WB_Point pt;

    protected abstract void setPt();

    public void initial() {
        setPt();
    }

    public WB_Point getPt() {
        return pt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        if (!o.getClass().getSimpleName().equals(getClass().getSimpleName())) return false;
        Node node = (Node) o;
        return getPt().equals(node.getPt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPt());
    }

    @Override
    public String toString() {
        return "Node{" +
                "name=" + getClass().getSimpleName() +
                ", pt=" + pt +
                '}';
    }
}
