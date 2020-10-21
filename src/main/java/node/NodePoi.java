package node;

import osm.GeoMath;
import util.Container;
import wblut.geom.WB_Point;

import java.util.Objects;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-16 17:43
 **/
public class NodePoi extends Node {
    private String name, shop_hours, tag, content_tag, uid;
    private double lat, lng, price, service_rating, environment_rating, taste_rating, overall_rating, comment_num, image_num;
    private int sharing_or_not;

    @Override
    public String toString() {
        return "NodePoi{" +
                "name=" + getClass().getSimpleName() +
                ", shop=" + name +
                ", pt=" + pt +
                '}';
    }

    public double getOverall_rating() {
        return overall_rating;
    }

    public String getName() {
        return name;
    }

    public int getSharing_or_not() {
        return sharing_or_not;
    }

    public double getPrice() {
        return price;
    }

    public String getShop_hours() {
        return shop_hours;
    }

    @Override
    protected void setPt() {
        GeoMath geoMath = new GeoMath(Container.LAT, Container.LNG);
        double[] xy = geoMath.latLngToXY(lat, lng);
        pt = new WB_Point(xy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodePoi)) return false;
        NodePoi nodePoi = (NodePoi) o;
        return Objects.equals(uid, nodePoi.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), uid);
    }

}
