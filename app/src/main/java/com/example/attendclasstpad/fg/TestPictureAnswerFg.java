package com.example.attendclasstpad.fg;

import com.example.attendclasstpad.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 测试-拍照答题
 */
public class TestPictureAnswerFg extends BaseNotPreLoadFg {
    private boolean isPrepared;// 标志位，标志已经初始化完成
    private boolean hasLoadOnce;// 是否已被加载过一次，第二次就不再去请求数据了

    private View allFgView;// 总布局

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == allFgView) {
            allFgView = View.inflate(getActivity(),
                    R.layout.layout_fg_test_picture_answer, null);
        }

        // 因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) allFgView.getParent();
        if (parent != null) {
            parent.removeView(allFgView);
        }
        // 标志当前页面可见
        isPrepared = true;
        lazyLoad();

        return allFgView;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || hasLoadOnce) {
            return;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
