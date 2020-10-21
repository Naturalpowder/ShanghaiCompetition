package dxf;

import org.kabeja.dxf.*;
import org.kabeja.dxf.helpers.Point;
import org.kabeja.parser.DXFParser;
import org.kabeja.parser.ParseException;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;
import wblut.geom.WB_Circle;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: ShanghaiCompetition
 * @description: dxfImport
 * @author: Naturalpowder
 * @create: 2020-10-15 15:00
 **/
public class DXFImporter {

    /**
     * Example
     *
     * @param args
     */
    public static void main(String[] args) {
        DXFImporter importer = new DXFImporter("src/main/data/LineRead.dxf");
        List<WB_PolyLine> lines = importer.getLines("0");
        List<WB_Point> points = importer.getPoints("0");
    }

    private final String filePath;
    private DXFDocument doc;

    public DXFImporter(String filePath) {
        this.filePath = filePath;
        read();
    }

    public List<String> getLayers() {
        List<String> layers = new ArrayList<>();
        Iterator iterator = doc.getDXFLayerIterator();
        while (iterator.hasNext()) {
            layers.add(((DXFLayer) iterator.next()).getName());
        }
        return layers;
    }

    public List<WB_Circle> getCircles(String layerId) {
        DXFLayer layer = doc.getDXFLayer(layerId.toUpperCase());
        List<WB_Circle> circles = new ArrayList<>();
        List list = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_CIRCLE);
        if (list != null) {
            for (Object o : list) {
                DXFCircle c = (DXFCircle) o;
                Point center = c.getCenterPoint();
                WB_Circle circle = new WB_Circle(new WB_Point(center.getX(), center.getY(), center.getZ()), c.getRadius());
                circles.add(circle);
            }
        }
        return circles;
    }

    public List<DXFText> getTexts(String layerId) {
        DXFLayer layer = doc.getDXFLayer(layerId.toUpperCase());
        List texts = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_TEXT);
        return texts == null ? new ArrayList<>() : texts;
    }

    public List<WB_PolyLine> getLines(String layerId) {
        DXFLayer layer = doc.getDXFLayer(layerId.toUpperCase());
        List lines = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_LINE);
        List<WB_PolyLine> polyLines = new ArrayList<>();
        if (lines != null) {
            for (Object o : lines) {
                DXFLine line = (DXFLine) o;
                polyLines.add(new WB_PolyLine(to_WB_Point(line.getStartPoint()), to_WB_Point(line.getEndPoint())));
            }
        }
        return polyLines;
    }

    private WB_Point to_WB_Point(Point p) {
        return new WB_Point(p.getX(), p.getY(), p.getZ());
    }

    public List<WB_Point> getPoints(String layerId) {
        DXFPoint_Reader reader = new DXFPoint_Reader(filePath);
        return reader.getPts(layerId.toUpperCase());
    }

    public List<WB_Polygon> getPolygons(String layerId) {
        List<Z_PolyLine> lines = getDXFPolyLines(layerId);
        return lines.stream().filter(e -> e.getPolygon() != null).map(Z_PolyLine::getPolygon).collect(Collectors.toList());
    }

    public List<WB_PolyLine> getPolyLines(String layerId) {
        List<Z_PolyLine> lines = getDXFPolyLines(layerId);
        return lines.stream().filter(e -> e.getPolyLine() != null).map(Z_PolyLine::getPolyLine).collect(Collectors.toList());
    }

    static class Z_PolyLine {
        private final WB_PolyLine polyLine;
        private final WB_Polygon polygon;

        public Z_PolyLine(WB_PolyLine polyLine, WB_Polygon polygon) {
            this.polyLine = polyLine;
            this.polygon = polygon;
        }

        public WB_PolyLine getPolyLine() {
            return polyLine;
        }

        public WB_Polygon getPolygon() {
            return polygon;
        }
    }

    private List<Z_PolyLine> getDXFPolyLines(String layerId) {
        DXFLayer layer = doc.getDXFLayer(layerId.toUpperCase());
        List pLines = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_POLYLINE);
        List<Z_PolyLine> lines = new ArrayList<>();
        if (pLines != null) {
            for (Object o : pLines) {
                DXFPolyline pLine = (DXFPolyline) o;
                List<WB_Point> pts = new ArrayList<>();
                for (int i = 0; i < pLine.getVertexCount(); i++) {
                    DXFVertex vertex = pLine.getVertex(i);
                    pts.add(new WB_Point(vertex.getX(), vertex.getY(), vertex.getZ()));
                }
                if (pLine.isClosed()) {
                    lines.add(new Z_PolyLine(null, new WB_Polygon(pts)));
//                    System.out.println("Polygon [ Nodes = " + pts.size() + " , Closed = " + pLine.isClosed() + " ]");
                } else {
                    lines.add(new Z_PolyLine(new WB_PolyLine(pts), null));
//                    System.out.println("PolyLine [ Nodes = " + pts.size() + " , Closed = " + pLine.isClosed() + " ]");
                }
            }
        }
        return lines;
    }

    private void read() {
        InputStream in = getInputStream(filePath);
        Parser parser = ParserBuilder.createDefaultParser();
        try {
            parser.parse(in, DXFParser.DEFAULT_ENCODING);
            doc = parser.getDocument();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @org.jetbrains.annotations.Nullable
    private InputStream getInputStream(String filePath) {
        File file = new File(filePath);
        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return in;
    }
}
