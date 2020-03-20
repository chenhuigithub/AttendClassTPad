package com.example.attendclasstpad.model;

import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 资源文件
 */
public class File01 {
    private String DataID;//ID
    private String DataName;//名称
    private String size;//大小
    private String Source;//来源
    private String DataTime;//创建日期（例如："2018-10-09 13:25"）
    private String logoUrl;//文件类型图标路径
    private String format;//文件格式
    private String Memo;//备注
    private boolean isChoiced = false;//是否被选中

    public String getDataID() {
        if (TextUtils.isEmpty(DataID)) {
            return "";
        }
        return DataID;
    }

    public void setDataID(String dataID) {
        DataID = dataID;
    }

    public String getDataName() {
        if (TextUtils.isEmpty(DataName)) {
            return "";
        }
        return DataName;
    }

    public void setDataName(String dataName) {
        DataName = dataName;
    }

    public String getSize() {
        if (TextUtils.isEmpty(size)) {
            return "";
        }
        return size;
    }


    public void setSize(String size) {
        this.size = size;
    }

    public String getSource() {
        if (TextUtils.isEmpty(Source)) {
            return "";
        }
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getDataTime() {
        if (TextUtils.isEmpty(DataTime)) {
            return "";
        }
        return DataTime;
    }

    public void setDataTime(String dataTime) {
        DataTime = dataTime;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @JSONField(name="DataType")
    public String getFormat() {
        if (TextUtils.isEmpty(format)) {
            return "";
        }
        return format;
    }

    @JSONField(name="DataType")
    public void setFormat(String format) {
        this.format = format;
    }

    public String getMemo() {
        if (TextUtils.isEmpty(Memo)) {
            return "";
        }
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }

    public boolean isChoiced() {
        return isChoiced;
    }

    public void setChoiced(boolean choiced) {
        isChoiced = choiced;
    }
}
