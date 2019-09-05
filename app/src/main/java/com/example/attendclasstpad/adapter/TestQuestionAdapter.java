package com.example.attendclasstpad.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.model.Test;

import java.util.List;

/**
 * 试题列表适配器
 */
public class TestQuestionAdapter extends BaseListAdapter<Test> {
    public TestQuestionAdapter(Context context, List<Test> dataList) {
        super(context, dataList);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.layout_fg_test_choice;
    }

    @Override
    protected void doAssignValueForView(int position, View resultView, Test dataObj) {
        //题目
        TextView tvQuestion = (TextView) resultView.findViewById(R.id.tv_content_layout_fg_test_choice);
        tvQuestion.setText(dataObj.getContent());

        //答案解析
        final TextView tvAnalysis = (TextView) resultView.findViewById(R.id.tv_analysis_layout_fg_test_choice);
        tvAnalysis.setText(dataObj.getAnalysis());
        tvAnalysis.setVisibility(View.GONE);

        //查看解析/收回解析
        final TextView tvSeeAnalysis = (TextView) resultView.findViewById(R.id.tv_see_analysis_layout_fg_test_choice);
        tvSeeAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvAnalysis.getVisibility() == View.GONE) {
                    tvAnalysis.setVisibility(View.VISIBLE);
                    tvSeeAnalysis.setText("收起解析");
                } else {
                    tvAnalysis.setVisibility(View.GONE);
                    tvSeeAnalysis.setText("查看解析");
                }
            }
        });

    }
}
