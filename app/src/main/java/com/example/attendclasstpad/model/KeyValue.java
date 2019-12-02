package com.example.attendclasstpad.model;


import android.text.TextUtils;

import java.io.Serializable;

/**
 * key-value对应模型
 *
 * @author zhaochenhui_2018.01.05
 */
//@Table(name = "KeyValue")
public class KeyValue implements Serializable {

    //	@Column
    private String id;// id
    //	@Column
    private String name;// 名称
    //	@Column
    private String type;// 类型

    public String getId() {
        if (TextUtils.isEmpty(id)) {
            return "";
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        if (TextUtils.isEmpty(type)) {
            return "";
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
