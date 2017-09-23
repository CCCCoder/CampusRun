package com.n1njac.yiqipao.android.bmobObject;

import com.n1njac.yiqipao.android.bean.LocationBean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by N1njaC on 2017/9/23.
 */

public class RunDataBmob extends BmobObject {

    private String runStartTime;
    private String runDistance;
    private String avSpeed;
    private List<LocationBean> points;

    public String getRunStartTime() {
        return runStartTime;
    }

    public void setRunStartTime(String runStartTime) {
        this.runStartTime = runStartTime;
    }

    public String getRunDistance() {
        return runDistance;
    }

    public void setRunDistance(String runDistance) {
        this.runDistance = runDistance;
    }

    public String getAvSpeed() {
        return avSpeed;
    }

    public void setAvSpeed(String avSpeed) {
        this.avSpeed = avSpeed;
    }

    public List<LocationBean> getPoints() {
        return points;
    }

    public void setPoints(List<LocationBean> points) {
        this.points = points;
    }
}
