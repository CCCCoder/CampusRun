package com.n1njac.yiqipao.android.bmobObject;

import cn.bmob.v3.BmobObject;

/**
 * Created by N1njaC on 2017/9/25.
 * 用户个人信息数据表
 */

public class NewUserInfoBmob extends BmobObject {

    private String pNickName;
    private String pSex;
    private String pBirth;
    private String pHeight;
    private String pWeight;
    private String pHobby;

    private String pUserObjectId;

    public String getpNickName() {
        return pNickName;
    }

    public void setpNickName(String pNickName) {
        this.pNickName = pNickName;
    }

    public String getpSex() {
        return pSex;
    }

    public void setpSex(String pSex) {
        this.pSex = pSex;
    }

    public String getpBirth() {
        return pBirth;
    }

    public void setpBirth(String pBirth) {
        this.pBirth = pBirth;
    }

    public String getpHeight() {
        return pHeight;
    }

    public void setpHeight(String pHeight) {
        this.pHeight = pHeight;
    }

    public String getpWeight() {
        return pWeight;
    }

    public void setpWeight(String pWeight) {
        this.pWeight = pWeight;
    }

    public String getpHobby() {
        return pHobby;
    }

    public void setpHobby(String pHobby) {
        this.pHobby = pHobby;
    }

    public String getpUserObjectId() {
        return pUserObjectId;
    }

    public void setpUserObjectId(String pUserObjectId) {
        this.pUserObjectId = pUserObjectId;
    }
}
