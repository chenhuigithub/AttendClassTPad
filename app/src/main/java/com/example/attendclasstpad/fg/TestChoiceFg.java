package com.example.attendclasstpad.fg;

import com.example.attendclasstpad.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

/**
 * 选择题-内容
 */
public class TestChoiceFg extends BaseNotPreLoadFg {
    private boolean isPrepared;// 标志位，标志已经初始化完成
    private boolean hasLoadOnce;// 是否已被加载过一次，第二次就不再去请求数据了

    private View allFgView;// 总布局
    private CheckBox cboxChoice;// 复选框

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == allFgView) {
            allFgView = View.inflate(getActivity(),
                    R.layout.layout_fg_test_choice, null);
            cboxChoice = (CheckBox) allFgView
                    .findViewById(R.id.cbox_choice_test_layout_fg_test_choice);

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
}
