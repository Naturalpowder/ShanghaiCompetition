package transform;

import dxf.DXFImporter;
import node.Node;
import node.NodeBuilding;
import org.kabeja.dxf.DXFText;
import org.kabeja.dxf.helpers.Point;
import wblut.geom.WB_Circle;
import wblut.geom.WB_Point;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class DxfBuilding2Node extends Path2Node {

    public DxfBuilding2Node(String filePath) {
        super(filePath);
    }

    @Override
    protected void set(String filePath) {
        String building_layer = "building", dining_layer = "dining", name_layer = "name";
        DXFImporter importer = new DXFImporter(filePath, DXFImporter.GBK);
        List<DXFText> texts = importer.getTexts(building_layer);
        List<DXFText> dining = importer.getTexts(dining_layer);
        List<DXFText> names = importer.getTexts(name_layer);
        List<WB_Circle> circles = importer.getCircles(building_layer);
        addNodesofCircle(texts, circles, dining, names);
    }

    private void addNodesofCircle(List<DXFText> texts, List<WB_Circle> circles, List<DXFText> dining, List<DXFText> names) {
        for (WB_Circle circle : circles) {
            WB_Point point = new WB_Point(circle.getCenter());
            addNode(circle.getRadius(), texts, point, dining, names);
        }
    }

    private void addNodesofPoint(double epsilon, List<DXFText> texts, List<WB_Point> points, List<DXFText> dining, List<DXFText> names) {
        for (WB_Point point : points) {
            addNode(epsilon, texts, point, dining, names);
        }
    }

    private void addNode(double epsilon, List<DXFText> texts, WB_Point point, List<DXFText> dining, List<DXFText> names) {
        int amount = (int) getAmount(epsilon, texts, point);
        int di = (int) (getAmount(epsilon, dining, point) * amount);
        String name = getName(epsilon, names, point);
        Node node = new NodeBuilding(amount, point, di, name);
        if (!nodes.contains(node))
            nodes.add(node);
    }

    private String getName(double epsilon, List<DXFText> texts, WB_Point point) {
        String name = "";
        for (DXFText text : texts) {
            Point tempP = text.getInsertPoint();
            WB_Point p = new WB_Point(tempP.getX(), tempP.getY(), tempP.getZ());
            if (p.getDistance(point) < epsilon) {
                name = text.getText();
            }
        }
        return name;
    }

    private double getAmount(double epsilon, List<DXFText> texts, WB_Point point) {
        double amount = -1;
        for (DXFText text : texts) {
            Point tempP = text.getInsertPoint();
            WB_Point p = new WB_Point(tempP.getX(), tempP.getY(), tempP.getZ());
            if (p.getDistance(point) < epsilon) {
                amount = Double.parseDouble(text.getText());
            }
        }
        return amount;
    }
}
