package com.example.attendclasstpad.model;

import com.example.attendclasstpad.util.ValidateFormatUtils;

/**
 * 课时
 *
 * @author zhaochenhui, 2018.05.17
 */
public class Lesson {
    private String key;
    private String pid;// 父级ID
    private String name;

    public String getKey() {
        if (ValidateFormatUtils.isEmpty(key)) {
            return "";
        }
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        if (ValidateFormatUtils.isEmpty(name)) {
            return "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
