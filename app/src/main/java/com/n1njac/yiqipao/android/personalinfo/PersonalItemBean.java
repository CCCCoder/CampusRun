package com.n1njac.yiqipao.android.personalinfo;

/**
 * Created by huanglei on 2017/2/7.
 */

public class PersonalItemBean {

    private String description;
    private String content;


    public PersonalItemBean(String description, String content) {
        this.description = description;
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }
}
