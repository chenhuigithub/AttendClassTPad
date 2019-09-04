package com.example.attendclasstpad.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义viewPager布局，灵活设置是否需要滑动功能
 *
 * @author liduohong, zhaochenhui_2017.04.26
 */
@SuppressLint("NewApi")
public class CustomViewpager extends ViewPager {
    private boolean hasTouchMode = false;// 默认为不需要滑动

    public CustomViewpager(Context context) {
        super(context);
    }

    public CustomViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置是否允许滑动
     *
     * @param hasTouchMode 是否允许滑动
     */
    public void setIfTouchMode(boolean hasTouchMode) {
        this.hasTouchMode = hasTouchMode;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (hasTouchMode)
            return super.onInterceptHoverEvent(ev);
        else
            return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (hasTouchMode)
            return super.onTouchEvent(ev);
        else
            return true;
    }
}
