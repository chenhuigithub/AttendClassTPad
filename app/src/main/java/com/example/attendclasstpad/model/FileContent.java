package com.example.attendclasstpad.model;

public class FileContent {
    private String pageNumber;// 页码
    private int ivRes; // 单页缩略图资源

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setIvRes(int ivRes) {
        this.ivRes = ivRes;
    }

    public int getIvRes() {
        return ivRes;
    }

}
