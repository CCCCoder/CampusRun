package com.n1njac.yiqipao.android.utils;
/*    
 *    Created by N1njaC on 2017/9/27.
 *    email:aiai173cc@gmail.com 
 */

import android.graphics.LinearGradient;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.Shader;

import com.n1njac.yiqipao.android.TrackApplication;

import java.util.ArrayList;
import java.util.List;

public class RecordPathUtil {

    private static final long MIN_ANIMATION = 2 * 1000;
    private static final long MAX_ANIMATION = 5 * 1000;

    private int mScreenWidth;

    private long mTotalAnimationTime = MIN_ANIMATION;

    private List<RecordPathBean> mTotalPathList;

    private PathMeasure mPathMeasure;

    private Path mWholePath;

    public RecordPathUtil() {
        mTotalPathList = new ArrayList<>();
        mScreenWidth = SizeUtil.getScreenWidth(TrackApplication.getContext());
    }


    //添加每一段的path
    public void addPath(Point start, Point end, int startColor, int endColor) {

        if (mWholePath == null) {
            mWholePath = new Path();
            mWholePath.moveTo(start.x, start.y);
            mWholePath.lineTo(end.x, end.y);
        }
        //保证最后一个顶点为整个线段的最后一个点
        mWholePath.lineTo(end.x, end.y);
        Path path = new Path();
        path.moveTo(start.x, start.y);
        path.lineTo(end.x, end.y);
        mPathMeasure = new PathMeasure();
        Shader shader = new LinearGradient(start.x, start.y, end.x, end.y, startColor, endColor, Shader.TileMode.CLAMP);
        RecordPathBean recordPathBean = new RecordPathBean();
        recordPathBean.setPath(path);
        recordPathBean.setShader(shader);
        recordPathBean.setPathLength(new PathMeasure(path, false).getLength());
        recordPathBean.setEndPoint(end);
        recordPathBean.setEndColor(endColor);
        mTotalPathList.add(recordPathBean);
        recordPathBean.setIndex(mTotalPathList.size() - 1);

    }

    public float getAllPathLength() {
        float allPathLength = 0;
        if (mTotalPathList != null) {
            for (int i = 0; i < mTotalPathList.size(); i++) {
                allPathLength += mTotalPathList.get(i).getPathLength();
            }
        }

        //根据整个path的长度计算动画持续时间
        calculationAnimDuration(allPathLength);
        return allPathLength;
    }

    private void calculationAnimDuration(float pathLength) {

        float var1 = 2000.0f;
        int var2 = 1080;
        float var3 = mScreenWidth * var1 / var2;
        float durationScale = pathLength / var3;
        if (durationScale <= 1) return;
        long duration = (long) (durationScale * MIN_ANIMATION);
        if (duration >= MAX_ANIMATION) {
            mTotalAnimationTime = MAX_ANIMATION;
            return;
        }
        mTotalAnimationTime = duration;

    }


    public Path getWholePath() {
        return mWholePath;
    }

    public List<RecordPathBean> getTotalPathList() {
        return mTotalPathList;
    }

    public long getTotalAnimationTime() {
        return mTotalAnimationTime;
    }

    //用来存储每一段path的相关属性
    public class RecordPathBean {

        private Path path;
        private Shader shader;
        private Point endPoint;
        private int endColor;
        private int index;
        private float pathLength;

        public Path getPath() {
            return path;
        }

        public void setPath(Path path) {
            this.path = path;
        }

        public Shader getShader() {
            return shader;
        }

        public void setShader(Shader shader) {
            this.shader = shader;
        }

        public Point getEndPoint() {
            return endPoint;
        }

        public void setEndPoint(Point endPoint) {
            this.endPoint = endPoint;
        }

        public int getEndColor() {
            return endColor;
        }

        public void setEndColor(int endColor) {
            this.endColor = endColor;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public float getPathLength() {
            return pathLength;
        }

        public void setPathLength(float pathLength) {
            this.pathLength = pathLength;
        }
    }


}
