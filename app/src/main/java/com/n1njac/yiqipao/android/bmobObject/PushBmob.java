package com.n1njac.yiqipao.android.bmobObject;

import cn.bmob.v3.BmobObject;



public class PushBmob extends BmobObject {
    private String phone;
    private String installationId;
    //    此字段只作为用来初始化该表，拿到objectId。
    private int initNum;

    public int getInitNum() {
        return initNum;
    }

    public void setInitNum(int initNum) {
        this.initNum = initNum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }
}
