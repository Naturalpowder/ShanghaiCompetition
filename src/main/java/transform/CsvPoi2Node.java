package transform;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import dxf.DXFImporter;
import node.Node;
import node.NodePoi;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Polygon;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-17 09:46
 **/
public class CsvPoi2Node extends Path2Node {

    public CsvPoi2Node(String filePath) {
        super(filePath);
    }

    public static void main(String[] args) {
        CsvPoi2Node node = new CsvPoi2Node("src/main/data/Poi_WGS84.csv");
        node.getNodes().forEach(System.out::println);
    }

    @Override
    protected void set(String filePath) {
        try {
            readCSV(filePath);
        } catch (IllegalAccessException | IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private void readCSV(String filePath) throws IOException, IllegalAccessException, CsvValidationException {
        String charset = "gbk";
        CSVReader csvReader = new CSVReaderBuilder(new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), charset))).build();
        List<String> titles = Arrays.asList(csvReader.readNext());
        for (String[] str : csvReader) {
            Node poi = new NodePoi();
            Field[] fields = poi.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String type = field.getType().toString();
                String name = field.getName();
                int index = titles.indexOf(name);
                if (index != -1) {
                    String v = str[index];
                    if (type.endsWith("String"))
                        field.set(poi, v);
                    else if (type.endsWith("double")) {
                        double num = -1;
                        if (!v.isEmpty() && !v.contains("-"))
                            num = Double.parseDouble(v);
                        field.set(poi, num);
                    } else if (type.endsWith("int")) {
                        int num = -1;
                        if (!v.isEmpty())
                            num = Integer.parseInt(v);
                        field.set(poi, num);
                    }
                }
            }
            if (!nodes.contains(poi))
                nodes.add(poi);
        }
    }
}
