package path;

import node.Node;
import node.NodeBuilding;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import util.Container;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-19 15:43
 **/
public class PathSelector {
    private final List<Path> paths;
    List<BuildingCenterPaths> centerPaths;

    public PathSelector(List<GraphPath<Node, DefaultWeightedEdge>> paths) {
        this.paths = paths.stream().map(Path::new).collect(Collectors.toList());
        initial();
    }

    private void initial() {
        centerPaths = getBuildingCenterPaths();
    }

    @NotNull
    private List<BuildingCenterPaths> getBuildingCenterPaths() {
        List<BuildingCenterPaths> centerPaths = new ArrayList<>();
        List<Node> buildings = paths.stream().map(e -> e.getPath().getStartVertex()).distinct().collect(Collectors.toList());
        for (Node node : buildings) {
            NodeBuilding building = (NodeBuilding) node;
            List<Path> select = paths.stream().filter(e -> e.getPath().getStartVertex().equals(node)).collect(Collectors.toList());
            BuildingCenterPaths centerPath = new BuildingCenterPaths(building, select);
            centerPath.initial(Container.TIME);
            centerPaths.add(centerPath);
        }
        return centerPaths;
    }

    public List<Path> randomChoose() {
        List<Path> result = new ArrayList<>();
        for (BuildingCenterPaths centerPath : centerPaths) {
            int amount = centerPath.getBuilding().getDining();
            switch (Container.TIME) {
                case Container.MORNING:
                    amount *= .25;
                    break;
                case Container.NOON:
                    amount *= .65;
                    break;
                case Container.EVENING:
                    amount *= .1;
                    break;
            }
            for (int i = 0; i < amount; i++) {
                result.add(randomChooseOne(centerPath.getPaths()));
            }
        }
        return result;
    }

    private Path randomChooseOne(List<Path> select) {
        double p = Container.RANDOM.nextDouble();
//        System.out.println("p=" + p);
        double cumulativeProbability = 0.0;
        Path path = null;
        for (Path value : select) {
            cumulativeProbability += value.getProbability();
            if (p <= cumulativeProbability) {
                path = value;
                break;
            }
        }
        return path;
    }
}