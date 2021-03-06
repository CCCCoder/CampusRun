package com.n1njac.yiqipao.android.bmobObject;

import com.n1njac.yiqipao.android.bean.LocationBean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by N1njaC on 2017/9/23.
 */

public class RunDataBmob extends BmobObject {


    //1.精确时间 2.公里数 3.点坐标 4.平均配速 5.跑步用时 6.最大最小速度 7.最大最小配速 8.平均配速
    //每一条数据需携带指定用户的objectid,目的是通过此特定的用户id来查询此用户的跑步信息数据。

    private String runStartTime;
    private String runDistance;
    private String avSpeed;
    private List<LocationBean> points;
    private String runDurationTime;
    private String pUserObjectId;

    private String maxSpeed;
    private String minSpeed;

    private String maxPace;
    private String minPace;

    private String avPace;

    public String getAvPace() {
        return avPace;
    }

    public void setAvPace(String avPace) {
        this.avPace = avPace;
    }

    public String getMaxPace() {
        return maxPace;
    }

    public void setMaxPace(String maxPace) {
        this.maxPace = maxPace;
    }

    public String getMinPace() {
        return minPace;
    }

    public void setMinPace(String minPace) {
        this.minPace = minPace;
    }

    public String getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(String maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(String minSpeed) {
        this.minSpeed = minSpeed;
    }

    public String getRunDurationTime() {
        return runDurationTime;
    }

    public void setRunDurationTime(String runDurationTime) {
        this.runDurationTime = runDurationTime;
    }


    public String getpUserObjectId() {
        return pUserObjectId;
    }

    public void setpUserObjectId(String pUserObjectId) {
        this.pUserObjectId = pUserObjectId;
    }

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
