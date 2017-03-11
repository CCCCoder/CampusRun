package com.n1njac.yiqipao.android.bmobObject;

import cn.bmob.v3.BmobObject;

/**
 * Created by huanglei on 2017/3/11.
 */

public class HistoryDistanceBmob extends BmobObject {

    private int pId;
    private double pDistance;
    private double pDate;
    private double pStartRunTime;
    private double pEndRunTime;
    private String pStartLoc;
    private String pEndLoc;

    public int getId() {
        return pId;
    }

    public void setId(int pId) {
        this.pId = pId;
    }

    public double getDistance() {
        return pDistance;
    }

    public void setDistance(double pDistance) {
        this.pDistance = pDistance;
    }

    public double getDate() {
        return pDate;
    }

    public void setDate(double pDate) {
        this.pDate = pDate;
    }

    public double getStartRunTime() {
        return pStartRunTime;
    }

    public void setStartRunTime(double pStartRunTime) {
        this.pStartRunTime = pStartRunTime;
    }

    public double getEndRunTime() {
        return pEndRunTime;
    }

    public void setEndRunTime(double pEndRunTime) {
        this.pEndRunTime = pEndRunTime;
    }

    public String getStartLoc() {
        return pStartLoc;
    }

    public void setStartLoc(String pStartLoc) {
        this.pStartLoc = pStartLoc;
    }

    public String getEndLoc() {
        return pEndLoc;
    }

    public void setEndLoc(String pEndLoc) {
        this.pEndLoc = pEndLoc;
    }
}
