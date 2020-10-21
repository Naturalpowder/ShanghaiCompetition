package path;

import node.Node;
import node.NodePoi;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import util.Container;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-19 20:45
 **/
public class Path {
    private final GraphPath<Node, DefaultWeightedEdge> path;
    private double probability, dis, price, score, dining;
    private double disScale = .15, priceScale = .1, scoreScale = .05, diningScale = 0.7;//.25,.2,.45,.1

    public Path(GraphPath<Node, DefaultWeightedEdge> path) {
        this.path = path;
        setParameters();
    }

    private void setParameters() {
        if (Container.TIME == Container.MORNING) {
            disScale = .5;
            priceScale = .4;
            scoreScale = .1;
            diningScale = .0;
        }
    }

    public GraphPath<Node, DefaultWeightedEdge> getPath() {
        return path;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability() {
        this.probability = disScale * dis + priceScale * price + scoreScale * score + diningScale * dining;
    }

    public void setDis(double dis) {
        this.dis = dis;
    }

    public double getDis() {
        return dis;
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
                ", price=" + price +
                ", score=" + score +
                '}';
    }
}
