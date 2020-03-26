package com.example.attendclasstpad.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.attendclasstpad.util.ValidateFormatUtils;

/**
 * 课件
 *
 * @author chenhui
 */
public class Courseware {
    private String id;//ID
    private String thumbPath;//图片路径
    private String bigPath;//大图路径
    private String pageNumber;// 页码

    private boolean hasJoinPreview;// 是否已加入预览
    private int focusType = -1;// 状态（普通状态:-1，未选中:0，被选中:1）

    @JSONField(name = "DataID")
    public String getId() {
        if (ValidateFormatUtils.isEmpty(id)) {
            return "";
        }

        return id;
    }

    @JSONField(name = "DataID")
    public void setId(String id) {
        this.id = id;
    }

    @JSONField(name = "ThumbPath")
    public String getThumbPath() {
        return thumbPath;
    }

    @JSONField(name = "ThumbPath")
    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    @JSONField(name = "BigPath")
    public String getBigPath() {
        return bigPath;
    }

    @JSONField(name = "BigPath")
    public void setBigPath(String bigPath) {
        this.bigPath = bigPath;
    }

    public int getFocusType() {
        return focusType;
    }

    public void setFocusType(int focusType) {
        this.focusType = focusType;
    }

    public boolean isHasJoinPreview() {
        return hasJoinPreview;
    }

    public void setHasJoinPreview(boolean hasJoinPreview) {
        this.hasJoinPreview = hasJoinPreview;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }
}
