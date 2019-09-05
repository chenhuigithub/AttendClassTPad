package com.example.attendclasstpad.fg;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.attendclasstpad.R;

public class TestPaperListFg extends BaseNotPreLoadFg{
    private boolean isPrepared;// 标志位，标志已经初始化完成
    private boolean hasLoadOnce;// 是否已被加载过一次，第二次就不再去请求数据了

    private View allFgView;// 总布局

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

    }
}
