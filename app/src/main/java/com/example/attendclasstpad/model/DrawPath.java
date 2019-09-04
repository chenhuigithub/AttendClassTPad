package com.example.attendclasstpad.model;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * 绘制路径模型类
 *
 * @author zhaochenhui, 2018.06.05
 */
public class DrawPath {
    public Path path;// 路径
    public Paint paint;// 画笔

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

}
