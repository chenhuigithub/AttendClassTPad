package com.example.attendclasstpad.callback;

import com.example.attendclasstpad.model.VideoAndAudioInfoModel;

import java.util.List;

/**
 * 播放视频的Fragment接收其所属Activity类的信息的接口（视频Fragment实现此接口）
 */
public interface OnListenerForPlayVideoCallback {
    /**
     * 当前播放的视频的课时信息
     *
     * @param info
     */
    public void ICanGetInfoSelected(VideoAndAudioInfoModel info);

    /**
     * 剧集信息列表
     *
     * @param infoList
     */
    public void ICanGetInfoList(List<VideoAndAudioInfoModel> infoList);

    /**
     * 停止播放
     */
    public void doOnStopVideo();
}