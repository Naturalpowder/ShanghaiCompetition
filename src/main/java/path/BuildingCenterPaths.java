package path;

import node.NodeBuilding;
import node.NodePoi;
import util.Container;
import util.TimeRegion;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-19 21:16
 **/
public class BuildingCenterPaths {
    private final NodeBuilding building;
    private final List<Path> paths;
    private int time;

    public BuildingCenterPaths(NodeBuilding building, List<Path> paths) {
        this.building = building;
        this.paths = paths;
    }

    public void initial(int time) {
        this.time = time;
        setDisProbabilities();
        setScoreProbabilities();
        setPriceProbabilities();
        setTimeProbabilities();
        double sum = 0;
        for (Path path : paths) {
            path.setProbability();
            sum += path.getProbability();
//            System.out.println(path);
        }
        System.out.println("Sum = " + sum);
    }

    private void setTimeProbabilities() {
        double p_a = 0, p_b = 0, p_c = 0;
        switch (time) {
            case Container.MORNING:
                p_a = 1;
                p_b = 0;
                p_c = 0;
                setTime(p_a, 0, 10);
                break;
            case Container.NOON:
                p_a = 0;
                p_b = 1;
                p_c = 0;
                setTime(p_b, 10, 17);
                break;
            case Container.EVENING:
                p_a = 0;
                p_b = 0;
                p_c = 1;
                setTime(p_c, 17, 24.1);
                break;
        }
    }

    private void setTime(double p_x, double start, double end) {
        List<Path> x = getSelectPathOfTime(start, end);
        emptyPrint(x);
        for (Path path : x) {
            path.setTime(p_x / x.size());
        }
    }

    private void emptyPrint(List<Path> x) {
        if (x.isEmpty())
            System.out.println("Empty Properties!");
    }

    private List<Path> getSelectPathOfTime(double start, double end) {
        return paths.stream().filter(e -> intersect(((NodePoi) e.getPath().getEndVertex()).getShop_hours(), start, end)).collect(Collectors.toList());
    }

    private boolean intersect(String shop_hours, double start, double end) {
        TimeRegion timeRegion = new TimeRegion(shop_hours);
        return timeRegion.intersect(start, end);
    }

    private void setScoreProbabilities() {
        double p_a = 0, p_b = 0;
        switch (time) {
            case Container.MORNING:
                p_a = .2;
                p_b = .8;
                break;
            case Container.NOON:
                p_a = .1;
                p_b = .9;
                break;
            case Container.EVENING:
                p_a = .3;
                p_b = .7;
                break;
        }
        setScore(p_a, 0, 4);
        setScore(p_b, 4, 5.1);
    }

    private void setScore(double p_x, double start, double end) {
        List<Path> x = getSelectPathOfScore(start, end);
        emptyPrint(x);
        for (Path path : x) {
            path.setScore(p_x / x.size());
        }
    }

    private List<Path> getSelectPathOfScore(double start, double end) {
        return paths.stream().filter(e -> mid(((NodePoi) e.getPath().getEndVertex()).getOverall_rating(), start, end)).collect(Collectors.toList());
    }

    private void setPriceProbabilities() {
        double p_a = 0, p_b = 0, p_c = 0, p_d = 0;
        switch (time) {
            case Container.MORNING:
                p_a = .6;
                p_b = .3;
                p_c = .1;
                p_d = 0.;
                break;
            case Container.NOON:
                p_a = .3;
                p_b = .5;
                p_c = .15;
                p_d = .05;
                break;
            case Container.EVENING:
                p_a = .25;
                p_b = .45;
                p_c = .2;
                p_d = .1;
                break;
        }
        setPrice(p_a, 0, 20);
        setPrice(p_b, 20, 50);
        setPrice(p_c, 50, 100);
        setPrice(p_d, 100, Double.MAX_VALUE);
    }

    private void setPrice(double p_x, double start, double end) {
        List<Path> x = getSelectPathOfPrice(start, end);
        emptyPrint(x);
        for (Path path : x)
            path.setPrice(p_x / x.size());
    }

    private List<Path> getSelectPathOfPrice(double start, double end) {
        return paths.stream().filter(e -> mid(((NodePoi) e.getPath().getEndVertex()).getPrice(), start, end)).collect(Collectors.toList());
    }

    private boolean mid(double num, double start, double end) {
        return num >= start && num < end;
    }

    private void setDisProbabilities() {
        double sum = 0;
        for (Path path : paths) {
            double weight = path.getPath().getWeight();
            double dis = 1 / weight;
            path.setDis(dis);
            sum += dis;
        }
        for (Path path : paths) {
            path.setDis(path.getDis() / sum);
        }
    }

    public NodeBuilding getBuilding() {
        return building;
    }

    public List<Path> getPaths() {
        return paths;
    }

    private double getMinDistance() {
        return paths.stream().collect(Collectors.summarizingDouble(e -> e.getPath().getWeight())).getMax();
    }
}
