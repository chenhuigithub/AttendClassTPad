package com.example.attendclasstpad.model;

import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.attendclasstpad.util.UrlUtils;

import java.io.Serializable;

/**
 * 图片
 *
 * @author chenhui 2018.08.24
 */
public class Picture implements Serializable {
    /**
     * 随机生成的序列号
     */
    private static final long serialVersionUID = -1046990704625307259L;

    private String ID;
    private String title;
    private String thumbPath;
    private String path;
    private String remark;

    @JSONField(name = "DataID")
    public String getID() {
        if (TextUtils.isEmpty(ID)) {
            return "";
        }
        return ID;
    }

    @JSONField(name = "DataID")
    public void setID(String ID) {
        this.ID = ID;
    }

    @JSONField(name = "DataTitle")
    public String getTitle() {
        if (TextUtils.isEmpty(title)) {
            return "";
        }

        return title;
    }

    @JSONField(name = "DataTitle")
    public void setTitle(String title) {
        this.title = title;
    }

    @JSONField(name = "ThumbPath")
    public String getThumbPath() {
        if (TextUtils.isEmpty(thumbPath)) {
            return "";
        }

        return UrlUtils.PREFIX + thumbPath;
    }

    @JSONField(name = "ThumbPath")
    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    @JSONField(name = "DataPath")
    public String getPath() {
        if (TextUtils.isEmpty(path)) {
            return "";
        }

        return UrlUtils.PREFIX + path;
    }

    @JSONField(name = "DataPath")
    public void setPath(String path) {
        this.path = path;
    }

    @JSONField(name = "Remark")
    public String getRemark() {
        if (TextUtils.isEmpty(remark)) {
            return "";
        }
        return remark;
    }

    @JSONField(name = "Remark")
    public void setRemark(String remark) {
        this.remark = remark;
    }

}
