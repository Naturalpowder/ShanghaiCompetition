package path;

import dxf.DXFImporter;
import node.Node;
import node.NodePoi;
import transform.CsvPoi2Node;
import transform.DxfBuilding2Node;
import transform.DxfPath2Node;
import transform.Path2Node;
import util.Container;
import util.TimeRegion;
import util.Util;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;

import java.util.*;
import java.util.stream.Collectors;

public class Manage {
    private final String poiPath, roadPath;
    private List<Node> nodes, pois, buildings, path;
    private List<int[]> edges, pathEdges, shortEdges, cutEdges;
    private PathSelector selector;

    public static void main(String[] args) {
        Manage manage = new Manage("src/main/data/poi1021.csv", "src/main/data/1021.dxf");
        List<Segment> list = manage.getRandomShortestPathColorful();
        System.out.println(list.size());
    }

    public Manage(String poiPath, String roadPath) {
        this.poiPath = poiPath;
        this.roadPath = roadPath;
        Container.RANDOM = new Random(10);
        set();
    }

    private void set() {
        poiSet();
        pathSet();
        buildingSet();
        addNodesEdges();
        setShortestPath();
    }

    public List<WB_PolyLine> getRandomShortestPath() {
        List<Path> randomPaths = selector.randomChoose();
        return randomPaths.stream().map(e -> Util.toPolyLineWithoutEnds(e.getPath())).collect(Collectors.toList());
    }

    public List<BuildingCenterPaths> getBuildingCenterPath() {
        return selector.getCenterPaths();
    }

    public List<Segment> getBuildingNRandomShortestPathColorful(BuildingCenterPaths centerPaths) {
        List<Segment> paths = new ArrayList<>();
        List<Path> randomPaths = selector.randomChooseOneBuilding(centerPaths);
        for (Path randomPath : randomPaths) {
            addTwoPointPath(paths, randomPath);
        }
        setTwoPointPaths(paths);
        return paths;
    }

    public List<Segment> animatePath(List<Path> paths) {
        List<Segment> list = new ArrayList<>();
        for (Path path : paths)
            addTwoPointPath(list, path);
        setTwoPointPaths(list);
        return list;
    }

    public List<Path> getRandomPaths() {
        return selector.randomChoose();
    }

    public List<Segment> getRandomShortestPathColorful() {
        List<Path> randomPaths = selector.randomChoose();
        List<Segment> list = new ArrayList<>();
        for (Path randomPath : randomPaths)
            addTwoPointPath(list, randomPath);
        setTwoPointPaths(list);
        return list;
    }

    private void setTwoPointPaths(List<Segment> list) {
        list.sort(Comparator.comparingInt(Segment::getSum));
        for (Segment path : list) {
            path.setMax(list.get(list.size() - 1).getSum());
            path.setMin(list.get(0).getSum());
        }
        if (!list.isEmpty()) {
            System.out.println("Max = " + list.get(0).getMax());
            System.out.println("Min = " + list.get(0).getMin());
            System.out.println("-------------------------------");
        }
    }

    private void addTwoPointPath(List<Segment> list, Path randomPath) {
        List<Node> nodes = randomPath.getPath().getVertexList();
        for (int i = 1; i < nodes.size() - 2; i++) {
            Segment path = new Segment(nodes.get(i), nodes.get(i + 1));
            if (list.contains(path))
                list.get(list.indexOf(path)).add(1);
            else
                list.add(path);
        }
    }

    private void buildingSet() {
        DxfBuilding2Node node = new DxfBuilding2Node(roadPath);
        buildings = node.getNodes();
        System.out.println("Buildings.Size() = " + buildings.size());
    }

    private void pathSet() {
        Path2Node node = new DxfPath2Node(roadPath);
        path = node.getNodes();
        pathEdges = node.getEdges();
        System.out.println("PathEdges.Size() = " + pathEdges.size());
    }

    private void poiSet() {
        Path2Node node = new CsvPoi2Node(poiPath);
        pois = node.getNodes();
//        System.out.println("Before = " + pois.size());
        remainFullInfoNode(pois);
//        System.out.println("After = " + pois.size());
        remainNodeInBoundary(pois);
        System.out.println("Pois.Size() = " + pois.size());
    }

    private int[] getStartEndTime(int time) {
        int[] result = new int[2];
        switch (time) {
            case Container.MORNING:
                result = new int[]{5, 10};
                break;
            case Container.NOON:
                result = new int[]{10, 15};
                break;
            case Container.EVENING:
                result = new int[]{17, 22};
                break;
        }
        return result;
    }

    private void remainFullInfoNode(List<Node> nodes) {
        List<Node> rest = nodes.stream().filter(e -> {
            NodePoi poi = (NodePoi) e;
            boolean remain = poi.getPrice() == -1;
            remain &= poi.getShop_hours().isEmpty();
            remain &= poi.getOverall_rating() == -1;
            return remain;
        }).collect(Collectors.toList());
        nodes.removeAll(rest);
    }

    private void remainCorrectTimeNode(List<Node> nodes) {
        List<Node> rest = nodes.stream().filter(e -> {
            TimeRegion timeRegion = new TimeRegion(((NodePoi) e).getShop_hours());
            int[] time = getStartEndTime(Container.TIME);
            return !timeRegion.intersect(time[0], time[1]);
        }).collect(Collectors.toList());
        nodes.removeAll(rest);
    }

    private void remainNodeInBoundary(List<Node> nodes) {
        DXFImporter importer = new DXFImporter(roadPath, DXFImporter.GBK);
        WB_Polygon boundary = importer.getPolygons("boundary").get(0);
        List<Node> rest = nodes.stream().filter(e -> !WB_GeometryOp.contains2D(e.getPt(), boundary)).collect(Collectors.toList());
        nodes.removeAll(rest);
    }

    public List<Node> getPois() {
        return pois;
    }

    public List<WB_PolyLine> getCutLines() {
        return cutEdges.stream().map(
                e -> new WB_PolyLine(nodes.get(e[0]).getPt(), nodes.get(e[1]).getPt())
        ).collect(Collectors.toList());
    }

    public List<WB_PolyLine> getShortLines() {
        return shortEdges.stream().map(
                e -> new WB_PolyLine(nodes.get(e[0]).getPt(), nodes.get(e[1]).getPt())
        ).collect(Collectors.toList());
    }

    public List<WB_PolyLine> getMergedLines() {
        return edges.stream().map(
                e -> new WB_PolyLine(nodes.get(e[0]).getPt(), nodes.get(e[1]).getPt())
        ).collect(Collectors.toList());
    }

    public List<Node> getNodes() {
        return nodes;
    }

    private void addNodesEdges() {
        NodesAdd nodesAdd = new NodesAdd(pois, buildings, path, pathEdges);
        nodesAdd.initial();
        edges = nodesAdd.getEdges();
        nodes = nodesAdd.getNodes();
        shortEdges = nodesAdd.getShortEdges();
        cutEdges = nodesAdd.getPathEdges();
        remainCorrectTimeNode(pois);
//        printInfo();
        System.out.println("edges = " + edges.size());
        System.out.println("nodes = " + nodes.size());
    }

    private void setShortestPath() {
        ShortestPath shortestPath = new ShortestPath(pois, buildings, nodes, edges);
        selector = new PathSelector(shortestPath.getShortestPath());
    }

    public void printInfo() {
        for (Node node : nodes) {
            System.out.println("Node = " + node);
        }
        for (int[] edge : edges) {
            System.out.println("Index = " + Arrays.toString(edge));
            System.out.println("Edge = [ " + nodes.get(edge[0]) + " , " + nodes.get(edge[1]) + " ]");
        }
    }
}
