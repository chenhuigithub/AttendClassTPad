package com.example.attendclasstpad.model;

/**
 * 柱状图模型类
 *
 * @author chenhui 2019.09.25
 */
public class Histogram {
    private String xName;//x轴名称
    private String yName;//y轴名称
    private String name;
    private String value;

    public String getxName() {
        return xName;
    }

    public void setxName(String xName) {
        this.xName = xName;
    }

    public String getyName() {
        return yName;
    }

    public void setyName(String yName) {
        this.yName = yName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
