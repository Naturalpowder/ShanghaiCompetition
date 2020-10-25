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
    private final String name;

    public NodeBuilding(int amount, WB_Point pt, int dining, String name) {
        this.amount = amount;
        this.pt = pt;
        this.dining = dining;
        this.name = name;
    }

    @Override
    protected void setPt() {

    }

    public String getName() {
        return name;
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