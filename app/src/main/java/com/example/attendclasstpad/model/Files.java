package com.example.attendclasstpad.model;

import com.example.attendclasstpad.R;

/**
 * 文件目录模型类
 *
 * @author zhaochenhui, 2018.05.21
 */

public class Files {
    private String id;// 唯一id
    private String name;// 文件名称
    private String size;// 文件大小
    private int logoRes;// 临时数据：图片资源

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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getLogoRes() {
        return logoRes;
    }

    public void setLogoRes(int logoRes) {
        this.logoRes = logoRes;
    }

}
