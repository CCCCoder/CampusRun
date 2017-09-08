package com.n1njac.yiqipao.android.bean;

/**
 * Created by huanglei on 2017/2/7.
 */

public class PersonalItemBean {

    public String description;
    public String content;


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
