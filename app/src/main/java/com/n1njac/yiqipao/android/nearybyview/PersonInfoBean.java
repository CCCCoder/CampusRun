package com.n1njac.yiqipao.android.nearybyview;

/**
 * Created by huanglei on 2017/3/21.
 */

public class PersonInfoBean {

    private int iconId;
    private String name;
    private float distance;
    private boolean sex;//false为男，true为女

    public PersonInfoBean(int iconId, String name, float distance, boolean sex) {
        this.iconId = iconId;
        this.name = name;
        this.distance = distance;
        this.sex = sex;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }
}
