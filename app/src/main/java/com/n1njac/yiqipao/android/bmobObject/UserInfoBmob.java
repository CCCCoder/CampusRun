package com.n1njac.yiqipao.android.bmobObject;

import cn.bmob.v3.BmobUser;

/**
 * Created by N1njaC on 2017/9/4.
 */

public class UserInfoBmob extends BmobUser {

    private String pNickName;
    private String pSex;
    private String pBirth;
    private String pHeight;
    private String pWeight;
    private String pHobby;

    private String userInfoObjectId;
    private String runDataObjectId;

    public String getUserInfoObjectId() {
        return userInfoObjectId;
    }

    public void setUserInfoObjectId(String userInfoObjectId) {
        this.userInfoObjectId = userInfoObjectId;
    }

    public String getRunDataObjectId() {
        return runDataObjectId;
    }

    public void setRunDataObjectId(String runDataObjectId) {
        this.runDataObjectId = runDataObjectId;
    }

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
}
