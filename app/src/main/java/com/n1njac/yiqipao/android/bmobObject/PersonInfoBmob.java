package com.n1njac.yiqipao.android.bmobObject;

import java.util.SimpleTimeZone;

import cn.bmob.v3.BmobObject;

/**
 * Created by huanglei on 2017/3/11.
 */

public class PersonInfoBmob extends BmobObject {


    private int pId;
    private String pNickName;
    private String pSex;
    private String pBirth;
    private String pHeight;
    private String pWeight;
    private String pHobby;

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
