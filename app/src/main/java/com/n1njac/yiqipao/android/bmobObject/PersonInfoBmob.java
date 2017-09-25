package com.n1njac.yiqipao.android.bmobObject;

import java.util.SimpleTimeZone;

import cn.bmob.v3.BmobObject;

/**
 * Created by huanglei on 2017/3/11.
 * 此方法已废弃。不再使用
 */


@Deprecated
public class PersonInfoBmob extends BmobObject {


    private int pId;
    private String pNickName;
    private String pSex;
    private String pBirth;
    private String pHeight;
    private String pWeight;
    private String pHobby;
    //    此字段只作为用来初始化该表，拿到objectId。
    private int initNum;

    private String pPhone;
    private String pInstallationId;

    public double getDistance() {
        return pDistance;
    }

    public void setDistance(double pDistance) {
        this.pDistance = pDistance;
    }

    //附近的人距离
    private double pDistance;

    public String getPhone() {
        return pPhone;
    }

    public void setPhone(String pPhone) {
        this.pPhone = pPhone;
    }

    public String getInstallationId() {
        return pInstallationId;
    }

    public void setInstallationId(String pInstallationId) {
        this.pInstallationId = pInstallationId;
    }


    public void setInitNum(int initNum) {
        this.initNum = initNum;
    }

    public int getId() {
        return pId;
    }

    public void setId(int pId) {
        this.pId = pId;
    }

    public String getNickName() {
        return pNickName;
    }

    public void setNickName(String pNickName) {
        this.pNickName = pNickName;
    }

    public String getSex() {
        return pSex;
    }

    public void setSex(String pSex) {
        this.pSex = pSex;
    }

    public String getBirth() {
        return pBirth;
    }

    public void setBirth(String pBirth) {
        this.pBirth = pBirth;
    }

    public String getHeight() {
        return pHeight;
    }

    public void setHeight(String pHeight) {
        this.pHeight = pHeight;
    }

    public String getWeight() {
        return pWeight;
    }

    public void setWeight(String pWeight) {
        this.pWeight = pWeight;
    }

    public String getHobby() {
        return pHobby;
    }

    public void setHobby(String pHobby) {
        this.pHobby = pHobby;
    }
}
