package com.example.attendclasstpad.model;

import android.graphics.Picture;
import android.view.animation.Animation;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 信息
 *
 * @author chenhui 2018.08.17
 */
public class DataInfo implements Serializable {
    private String type;// 类型（word/images/...）
    private String body;
    private String page;

    private Picture pic;// 图片
    private VideoAudio video;// 音频、视频
    private Animation anim;// 动画

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JSONField(name = "DataBody")
    public String getBody() {
        return body;
    }

    @JSONField(name = "DataBody")
    public void setBody(String body) {
        this.body = body;
    }

    @JSONField(name = "DataPage")
    public String getPage() {
        return page;
    }

    @JSONField(name = "DataPage")
    public void setPage(String page) {
        this.page = page;
    }

    public Picture getPic() {
        if (pic != null) {
            return pic;
        } else {
            return new Picture();
        }
    }

    public void setPic(Picture pic) {
        this.pic = pic;
    }

    public VideoAudio getVideo() {
        return video;
    }

    public void setVideo(VideoAudio video) {
        this.video = video;
    }

    public Animation getAnim() {
        return anim;
    }

    public void setAnim(Animation anim) {
        this.anim = anim;
    }

}
