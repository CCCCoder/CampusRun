package com.n1njac.yiqipao.android.bean;

/**
 * Created by huanglei on 2017/3/21.
 */

public class PersonInfoBean {

    public int iconId;
    public String name;
    public float distance;
    public boolean sex;//false为男，true为女

    //    指定推送目标的id
    public String installationId;

    // TODO: 2017/4/8 构造函数要改
    public PersonInfoBean(int iconId, String name, float distance, boolean sex, String installationId) {
        this.iconId = iconId;
        this.name = name;
        this.distance = distance;
        this.sex = sex;
        this.installationId = installationId;
    }

    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
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
