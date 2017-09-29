package com.n1njac.yiqipao.android.utils;

import java.math.BigDecimal;

/**
 * Created by N1njaC on 2017/9/27.
 */

public class CalculateUtil {

    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    //配速
    //   分钟/公里
    public static double getPaceDouble(int count, double distance) {

        double min = (double) count / 60;

        return min / distance;
    }


    public static String getPaceResult(double paceDouble, int count, double distance) {
        String result;
        double min = (double) count / 60;
        int paceInt = (int) (min / distance);
        double secondDouble = paceDouble - paceInt;
        int secondInt = (int) (secondDouble * 60);

        // 形如:10'23''
        result = paceInt + "'" + secondInt + "''";
        return result;
    }


    public static int parseMinute(int count) {

        if (count == 60) count = 0;
        return count < 60 ? count : parseMinute(count / 60);
    }


}
