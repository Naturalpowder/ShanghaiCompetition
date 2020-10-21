package test;

import osm.GeoMath;

import java.util.Arrays;

public class testGeoMath {
    public static void main(String[] args) {
        GeoMath geoMath = new GeoMath((31.230918 + 31.248808) / 2, (121.492236 + 121.518305) / 2);
        double[] xy = geoMath.latLngToXY(31.230918, 121.518305);
        System.out.println(Arrays.toString(xy));
    }
}
