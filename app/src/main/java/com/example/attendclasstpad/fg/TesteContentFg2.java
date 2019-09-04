package com.example.attendclasstpad.fg;

import com.example.attendclasstpad.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


//选择题-----内容
public class TesteContentFg2 extends Fragment {


    private View allFgView;// 总布局

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == allFgView) {
            allFgView = View.inflate(getActivity(), R.layout.layout_fg_test_content2, null);

        }
        return allFgView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
