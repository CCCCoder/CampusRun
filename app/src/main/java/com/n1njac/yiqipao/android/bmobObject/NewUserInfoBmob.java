package com.n1njac.yiqipao.android.bmobObject;

import cn.bmob.v3.BmobObject;

/**
 * Created by N1njaC on 2017/9/25.
 * 用户个人信息数据表
 */

public class NewUserInfoBmob extends BmobObject {

    private String pNickName;
    //0:未设置 1:男 2:女
    private int pSex;
    private int pHeight;
    private int pWeight;
    private String pHobby;
    private String pUserObjectId;

    private String pAvatarUrl;

    public String getpAvatarUrl() {
        return pAvatarUrl;
    }

    public void setpAvatarUrl(String pAvatarUrl) {
        this.pAvatarUrl = pAvatarUrl;
    }

    public String getpNickName() {
        return pNickName;
    }

    public void setpNickName(String pNickName) {
        this.pNickName = pNickName;
    }

    public int getpSex() {
        return pSex;
    }

    public void setpSex(int pSex) {
        this.pSex = pSex;
    }


    public int getpHeight() {
        return pHeight;
    }

    public void setpHeight(int pHeight) {
        this.pHeight = pHeight;
    }

    public int getpWeight() {
        return pWeight;
    }

    public void setpWeight(int pWeight) {
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
