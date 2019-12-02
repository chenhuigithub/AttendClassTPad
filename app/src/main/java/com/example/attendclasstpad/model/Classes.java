package com.example.attendclasstpad.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 班级模型类
 *
 * @author chenhui 2018.06.13
 */
public class Classes implements Serializable {
    private String id;
    private String name;
    private String ivUrl;//班级头像网络路径
    private int ivRes; // 班级头像
    private String type;
    private int posFocus;

    @JSONField(name = "DataID")
    public String getId() {
        return id;
    }

    @JSONField(name = "DataID")
    public void setId(String id) {
        this.id = id;
    }

    @JSONField(name = "DataName")
    public String getName() {
        return name;
    }

    @JSONField(name = "DataName")
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPosFocus() {
        return posFocus;
    }

    public void setPosFocus(int posFocus) {
        this.posFocus = posFocus;
    }

    public String getIvUrl() {
        return ivUrl;
    }

    public void setIvUrl(String ivUrl) {
        this.ivUrl = ivUrl;
    }

    public int getIvRes() {
        return ivRes;
    }

    public void setIvRes(int ivRes) {
        this.ivRes = ivRes;
    }

}
