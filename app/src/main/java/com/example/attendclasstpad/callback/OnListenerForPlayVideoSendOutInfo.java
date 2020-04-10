package com.example.attendclasstpad.callback;

import com.example.attendclasstpad.model.VideoAndAudioInfoModel;
import com.example.attendclasstpad.model.VideoAudio;

import java.util.List;

/**
 * 播放视频的Fragment向其所属Activity类发送信息的接口（引用此视频Fragment的类实现此接口）
 */
public interface OnListenerForPlayVideoSendOutInfo {
    /**
     * 当前播放的视频的课时信息
     *
     * @param info
     */
    public void ICanGetVideoInfoCurrentPlay(VideoAndAudioInfoModel info);

    /**
     * 点击返回按钮后的处理
     */
    public void doAfterClickBack();

    /**
     * 切换至全屏后的处理
     */
    public void doSwitchFullScreen(List<VideoAudio> list,VideoAudio info);

    /**
     * 切换至半屏后的处理
     */
    public void doSwitchHalfScreen();
}
