package com.example.attendclasstpad.model;

import java.util.List;

/**
 * 目录
 *
 * @author chenhui 2018.07.03
 */
public class Catalog {
    private String id;
    private String name;// 名称
    private String pid;// 父级id
    private String pname;// 父级名称
    private boolean isSelected;// 被选中
    private List<Catalog> catalog;//子级目录列表

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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public List<Catalog> getCatalog() {
        return catalog;
    }

    public void setCatalog(List<Catalog> catalog) {
        this.catalog = catalog;
    }
}
