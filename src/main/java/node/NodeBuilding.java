package node;

import wblut.geom.WB_Point;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-17 10:07
 **/
public class NodeBuilding extends Node {
    private final int amount, dining;

    public NodeBuilding(int amount, WB_Point pt, int dining) {
        this.amount = amount;
        this.pt = pt;
        this.dining = dining;
    }

    @Override
    protected void setPt() {

    }

    public int getAmount() {
        return amount;
    }

    public int getDining() {
        return dining;
    }

    @Override
    public String toString() {
        return "NodeBuilding{" +
                "amount=" + amount +
                ", dining=" + dining +
                ", pt=" + pt +
                '}';
    }
}