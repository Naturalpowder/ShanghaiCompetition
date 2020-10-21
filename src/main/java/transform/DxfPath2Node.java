package transform;

import dxf.DXFImporter;
import node.Edge;
import node.Node;
import node.NodePath;
import wblut.geom.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DxfPath2Node extends Path2Node {

    public static void main(String[] args) {
        Path2Node path2Node = new DxfPath2Node("src/main/data/demo.dxf");
    }

    public DxfPath2Node(String filePath) {
        super(filePath);
    }

    @Override
    protected void set(String filePath) {
        String layer = "path";
        DXFImporter importer = new DXFImporter(filePath);
        List<WB_PolyLine> polyLines = importer.getPolyLines(layer);
        List<WB_Polygon> polygons = importer.getPolygons(layer);
        addPolygons(polygons);
        addPolyLines(polyLines);
    }

    private void addPolyLines(List<WB_PolyLine> polyLines) {
        for (WB_PolyLine polyLine : polyLines) {
            List<WB_Coord> coords = polyLine.getPoints().toList();
            for (WB_Coord coord : coords)
                addCoord(coord);
            for (int i = 0; i < coords.size() - 1; i++)
                addEdge(coords, i);
        }
    }

    private void addPolygons(List<WB_Polygon> polygons) {
        for (WB_Polygon polygon : polygons) {
            List<WB_Coord> coords = polygon.getPoints().toList();
            for (WB_Coord coord : coords) {
                addCoord(coord);
            }
            for (int i = 0; i < coords.size(); i++) {
                addEdge(coords, i);
            }
        }
    }

    private void addCoord(WB_Coord coord) {
        Node node = new NodePath(new WB_Point(coord));
        if (!nodes.contains(node))
            nodes.add(node);
    }

    private void addEdge(List<WB_Coord> coords, int i) {
        int start = nodes.indexOf(new NodePath(new WB_Point(coords.get(i))));
        int end = nodes.indexOf(new NodePath(new WB_Point(coords.get((i + 1) % coords.size()))));
        Edge edge = new Edge(start, end);
        if (!edges.contains(edge) && start != end)
            edges.add(edge);
    }
}