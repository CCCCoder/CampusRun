package com.n1njac.yiqipao.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by huanglei on 2017/1/17.
 */

public class Distance extends DataSupport {

    private int id;
    private String time;
    private String totalDistance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }
}
