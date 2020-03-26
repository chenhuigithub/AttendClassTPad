package com.example.attendclasstpad.model;


import com.example.attendclasstpad.util.ValidateFormatUtils;

import java.io.Serializable;

/**
 * 音频、视频
 *
 * @author chenhui 2018.08.24
 */
public class VideoAudio extends ICanGetKeyValue implements Serializable {
    /**
     * 随机生成的序列号
     */
    private static final long serialVersionUID = -5438735758043645383L;
    private String id;
    private String title;
    // private String thumbPath;
    private String path;
    private String remark;

    public String getId() {
        if (!ValidateFormatUtils.isEmpty(id)) {
            return id;
        } else {
            return "";
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        if (!ValidateFormatUtils.isEmpty(title)) {
            return title;
        } else {
            return "";
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // public String getThumbPath() {
    // return thumbPath;
    // }

    // public void setThumbPath(String thumbPath) {
    // this.thumbPath = thumbPath;
    // }

    public String getPath() {
        if (!ValidateFormatUtils.isEmpty(path)) {
            return path;
        } else {
            return "";
        }
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRemark() {
        if (!ValidateFormatUtils.isEmpty(remark)) {
            return remark;
        } else {
            return "";
        }
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


}
