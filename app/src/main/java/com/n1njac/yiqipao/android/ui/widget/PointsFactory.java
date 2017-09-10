package com.n1njac.yiqipao.android.ui.widget;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;
import static com.n1njac.yiqipao.android.ui.widget.BezierView.STYLE_BEACH;
import static com.n1njac.yiqipao.android.ui.widget.BezierView.STYLE_CLOUD;
import static com.n1njac.yiqipao.android.ui.widget.BezierView.STYLE_RIPPLE;
import static com.n1njac.yiqipao.android.ui.widget.BezierView.STYLE_SAND;
import static com.n1njac.yiqipao.android.ui.widget.BezierView.STYLE_SHELL;

/*
 *   *     *    *  *     *    *    * * *
 *   * *   *  * *  * *   *    *   *
 *   *   * *    *  *   * *    *   *
 *   *     *    *  *     *  * *    * * *
 *
 *    Created by N1njaC on 2017/9/10.
 *    email:aiai173cc@gmail.com
 */

public class PointsFactory {


    public static List<Point[]> getPoints(@BezierView.Style int style, int width, int height) {
        switch (style) {
            case STYLE_SAND:
                return getStyleSandPoints(width, height);

            case STYLE_CLOUD:
                return getStyleCloudsPoints(width, height);

            case STYLE_RIPPLE:
                return getStyleRipplesPoints(width, height);

            case STYLE_BEACH:
                return getStyleBeachPoints(width, height);

            case STYLE_SHELL:
                return getStyleShellPoints(width, height);

            default:
                return null;
        }
    }

    public static List<Point[]> getStyleSandPoints(int width, int height) {
        List<Point[]> points = new ArrayList<>();
        Point[] pts1 = new Point[4];
        Point[] pts2 = new Point[4];
        pts1[0] = new Point(0, height / 6);
        pts1[1] = new Point((int) (width * 0.9), height / 5);
        pts1[2] = new Point(width / 2, (int) (height * (5.0 / 6)));
        pts1[3] = new Point(0, (int) (height * 0.75));

        pts2[0] = new Point(width / 3, 0);
        pts2[1] = new Point(width / 2, height / 3);
        pts2[2] = new Point(width, (int) (height * 0.4));
        pts2[3] = new Point(width, 0);

        points.add(pts1);
        points.add(pts2);

        return points;
    }

    public static List<Point[]> getStyleBeachPoints(int width, int height) {
        List<Point[]> points = new ArrayList<>(2);
        if (width > height) {
            Point[] pts1 = new Point[7];
            Point[] pts2 = new Point[7];
            pts1[0] = new Point((int) (width * 0.25), 0);
            pts1[1] = new Point((int) (width * 0.166666667), (int) (height * 0.25));
            pts1[2] = new Point(width / 3, (int) (height * 0.375));
            pts1[3] = new Point((int) (width * 0.416666667), (int) (height * 0.625));
            pts1[4] = new Point((int) (width * 0.666666667), (int) (height * 0.875));
            pts1[5] = new Point(width, (int) (height * 0.75));
            pts1[6] = new Point(width, 0);

            int distance = (int) (width * 0.05);
            for (int i = 0; i < pts1.length; i++) {
                if (i == 0 || i == pts1.length - 2) {
                    pts2[i] = new Point(pts1[i].x + (i == 0 ? distance : 0), pts1[i].y - (i == pts1.length - 2 ? distance : 0));
                } else {
                    pts2[i] = new Point(pts1[i].x + distance, pts1[i].y - distance);
                }
            }
            points.add(pts1);
            points.add(pts2);

            return points;
        } else {
            Point[] pts1 = new Point[8];
            Point[] pts2 = new Point[8];
            pts1[0] = new Point(0, (int) (height * 0.75));
            pts1[1] = new Point((int) (width * 0.25), (int) (height * 0.83333333));
            pts1[2] = new Point((int) (width * 0.375), (int) (height * 0.666666667));
            pts1[3] = new Point((int) (width * 0.625), (int) (height * 0.583333333));
            pts1[4] = new Point((int) (width * 0.875), (int) (height * 0.333333333));
            pts1[5] = new Point(width, (int) (height * 0.0833333333));
            pts1[6] = new Point(width, 0);
            pts1[7] = new Point(0, 0);

            int distance = (int) (width * 0.05);
            for (int i = 0; i < pts1.length; i++) {
                if (i == pts1.length - 2 || i == pts1.length - 1) {
                    pts2[i] = pts1[i];
                } else {
                    pts2[i] = new Point(pts1[i].x + (i == 0 ? 0 : distance), pts1[i].y + distance);
                }
            }
            points.add(pts2);
            points.add(pts1);

            return points;
        }
    }

    public static List<Point[]> getStyleCloudsPoints(int width, int height) {
        List<Point[]> points = new ArrayList<>(2);
        int high = width < height ? width : height;
        Point[] pts1 = new Point[4];
        Point[] pts2 = new Point[4];
        Point[] pts3 = new Point[4];

        pts1[0] = new Point(0, 0);
        pts1[1] = new Point(0, (int) (high * 0.45));
        pts1[2] = new Point((int) (width * 0.916666667), 0);
        pts1[3] = new Point(0, 0);

        pts2[0] = new Point(0, 0);
        pts2[1] = new Point(0, (int) (high * 0.25));
        pts2[2] = new Point((int) (width * 0.75), 0);
        pts2[3] = new Point(0, 0);

        pts3[1] = new Point((int) (width * 0.58333333), 0);
        pts3[2] = new Point(width, (int) (high * 0.333333));
        pts3[0] = new Point(width, 0);
        pts3[3] = new Point(width, 0);

        points.add(pts2);
        points.add(pts3);
        points.add(pts1);

        return points;
    }

    public static List<Point[]> getStyleRipplesPoints(int width, int height) {
        List<Point[]> points = new ArrayList<>(2);
        Point[] pts1 = new Point[4];
        Point[] pts2 = new Point[4];
        Point[] pts3 = new Point[4];
        Point[] pts4 = new Point[4];
        Point[] pts5 = new Point[4];

        int radius = width / 6;
        pts1[0] = new Point(width / 2 - radius * 2, height / 2);
        pts1[1] = new Point(width / 2, height / 2 + radius);
        pts1[2] = new Point(width / 2 + radius * 2, height / 2);
        pts1[3] = new Point(width / 2, height / 2 - radius);

        int radius2 = width / 4;
        pts2[0] = new Point(width / 2 - radius2 * 2, height / 2);
        pts2[1] = new Point(width / 2, height / 2 + radius2);
        pts2[2] = new Point(width / 2 + radius2 * 2, height / 2);
        pts2[3] = new Point(width / 2, height / 2 - radius2);

        int radius3 = width / 3;
        pts3[0] = new Point(width / 2 - radius3 * 2, height / 2);
        pts3[1] = new Point(width / 2, height / 2 + radius3);
        pts3[2] = new Point(width / 2 + radius3 * 2, height / 2);
        pts3[3] = new Point(width / 2, height / 2 - radius3);

        int radius4 = width / 5;
        pts4[0] = new Point(width / 2 - radius4 * 2, height / 2);
        pts4[1] = new Point(width / 2, height / 2 + radius4);
        pts4[2] = new Point(width / 2 + radius4 * 2, height / 2);
        pts4[3] = new Point(width / 2, height / 2 - radius4);

        int radius5 = width / 2;
        pts5[0] = new Point(width / 2 - radius5 * 2, height / 2);
        pts5[1] = new Point(width / 2, height / 2 + radius5);
        pts5[2] = new Point(width / 2 + radius5 * 2, height / 2);
        pts5[3] = new Point(width / 2, height / 2 - radius5);

        points.add(pts5);
        points.add(pts3);
        points.add(pts2);
        points.add(pts4);
        points.add(pts1);
        return points;
    }

    public static List<Point[]> getStyleShellPoints(int width, int height) {
        List<Point[]> points = new ArrayList<>(2);
        Point[] pts1 = new Point[4];
        Point[] pts2 = new Point[4];
        Point[] pts3 = new Point[4];

        int radius = width / 6;
        pts1[0] = new Point(width / 2 - radius * 2, height / 2);
        pts1[1] = new Point(width / 2, height / 2 + radius);
        pts1[2] = new Point(width / 2 + radius * 2, height / 2);
        pts1[3] = new Point(width / 2, height / 2 - radius);

        int radius2 = width / 4;
        pts2[0] = new Point(width / 2 - radius2 * 2, height / 2);
        pts2[1] = new Point(width / 2, height / 2 + radius2);
        pts2[2] = new Point(width / 2 + radius * 2, height / 2);
        pts2[3] = new Point(width / 2, height / 2 - radius2);

        int radius3 = width / 3;
        pts3[0] = new Point(width / 2 - radius3 * 2, height / 2);
        pts3[1] = new Point(width / 2, height / 2 + radius3);
        pts3[2] = new Point(width / 2 + radius * 2, height / 2);
        pts3[3] = new Point(width / 2, height / 2 - radius3);

        points.add(pts3);
        points.add(pts2);
        points.add(pts1);
        return points;
    }

}
