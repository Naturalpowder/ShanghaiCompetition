package test;

import node.Node;
import node.NodePath;
import node.NodePoi;
import wblut.geom.WB_Point;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-19 10:33
 **/
public class TestInstanceOf {
    public static void main(String[] args) {
        Node node = new NodePath(new WB_Point(0, 0, 0));
        System.out.println(node instanceof NodePoi);
    }
}
