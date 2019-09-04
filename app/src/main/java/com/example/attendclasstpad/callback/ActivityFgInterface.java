package com.example.attendclasstpad.callback;

import android.support.v4.app.Fragment;

public class ActivityFgInterface {
    /**
     * 实现fragment页面之间的跳转
     *
     * @author zhaochenhui, 2018.05.18
     */
    public interface JumpCallback {
        /**
         * 跳转至fg
         *
         * @param fg 将要跳转至的页面
         */
        void jumpTo(Fragment fg);
    }
}
