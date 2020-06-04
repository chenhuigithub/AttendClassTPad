package com.example.attendclasstpad.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 学生模型类
 */
public class Student {
    private String id;// 唯一ID
    private String name;// 真实姓名
    private String logo;// 头像
    private boolean islock;// 是否被教师解锁

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JSONField(name = "avatar")
    public String getLogo() {
        return logo;
    }

    @JSONField(name = "avatar")
    public void setLogo(String logo) {
        this.logo = logo;
    }


    public boolean isIslock() {
        return islock;
    }

    public void setIslock(boolean islock) {
        this.islock = islock;
    }
}
