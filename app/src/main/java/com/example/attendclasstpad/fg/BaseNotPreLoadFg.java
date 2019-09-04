package com.example.attendclasstpad.fg;

import android.support.v4.app.Fragment;

/**
 * 去掉ViewPager自带预加载功能的fragment基础类
 *
 * @author zhaochenhui, 2016.12.14
 */
public abstract class BaseNotPreLoadFg extends Fragment {

    protected boolean isVisible;// Fragment当前状态是否可见

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
            lazyLoad();
        }
    }

    /**
     * 延迟加载（子类可在onCreateView方法内初始化控件之后调用，进行不需要预加载的操作，子类必须重写此方法）
     */
    protected abstract void lazyLoad();
}
