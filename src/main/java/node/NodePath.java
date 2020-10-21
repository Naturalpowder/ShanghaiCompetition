package node;

import wblut.geom.WB_Point;

public class NodePath extends Node {

    public NodePath(WB_Point pt) {
        this.pt = pt;
    }

    @Override
    protected void setPt() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodePath)) return false;
        NodePath nodePath = (NodePath) o;
        if (pt.equals(nodePath.pt)) return true;
        return pt.getDistance(nodePath.pt) < .1;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
