package path;

import node.Node;
import node.NodePoi;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-19 20:45
 **/
public class Path {
    private final GraphPath<Node, DefaultWeightedEdge> path;
    private double probability, dis, time, price, score, dining;
    private final static double disScale = .25, timeScale = .2, priceScale = .45, scoreScale = .1, diningScale = 0;//.25,.2,.45,.1

    public Path(GraphPath<Node, DefaultWeightedEdge> path) {
        this.path = path;
    }

    public GraphPath<Node, DefaultWeightedEdge> getPath() {
        return path;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability() {
        this.probability = disScale * dis + timeScale * time + priceScale * price + scoreScale * score + diningScale * dining;
    }

    public void setDis(double dis) {
        this.dis = dis;
    }

    public double getDis() {
        return dis;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setDining(double dining) {
        this.dining = dining;
    }

    @Override
    public String toString() {
        return "Path{" +
                "end=" + ((NodePoi) (path.getEndVertex())).getName() +
                ", probability=" + probability +
                ", dis=" + dis +
                ", time=" + time +
                ", price=" + price +
                ", score=" + score +
                '}';
    }
}
