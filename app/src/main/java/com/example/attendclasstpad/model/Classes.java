package com.example.attendclasstpad.model;

/**
 * 班级模型类
 *
 * @author chenhui 2018.06.13
 */
public class Classes {
    private String id;
    private String name;
    private int ivRes; // 班级头像

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

    public int getIvRes() {
        return ivRes;
    }

    public void setIvRes(int ivRes) {
        this.ivRes = ivRes;
    }

}
