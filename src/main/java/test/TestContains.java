package test;

import node.Edge;
import node.Node;
import node.NodePath;
import wblut.geom.WB_Point;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-20 16:55
 **/
public class TestContains {
    public static void main(String[] args) {
        Node a = new NodePath(new WB_Point(1, 1, 0));
        Node b = new NodePath(new WB_Point(1.05, 1, 0));
        Node c = new NodePath(new WB_Point(1.05, 2, 0));
        List<Node> nodes = Arrays.asList(c, a);
        System.out.println(nodes.indexOf(b));
    }
}
