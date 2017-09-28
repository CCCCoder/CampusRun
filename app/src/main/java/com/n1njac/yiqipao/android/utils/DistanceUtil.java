package com.n1njac.yiqipao.android.utils;
/*    
 *    Created by N1njaC on 2017/9/28.
 *    email:aiai173cc@gmail.com
 *
 *    根据两点间经纬度坐标（double值），计算两点间距离，
 */

public class DistanceUtil {

    private static final double EARTH_RADIUS = 6378137;

    public static double getDistance(double latitude1, double longitude1, double latitude2, double longitude2) {

        double lat1 = rad(latitude1);
        double lat2 = rad(latitude2);
        double a = lat1 - lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double a) {
        return a * Math.PI / 180.0;
    }

}
