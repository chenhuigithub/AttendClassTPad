package com.example.attendclasstpad.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.model.Test;

import java.util.List;

/**
 * 试题列表适配器
 */
public class TestQuestionAdapter extends BaseListAdapter<Test> {
    private int type = 0;//默认是答题状态（1为查看答案解析状态）

    private boolean showCbox;//是否选中

    public TestQuestionAdapter(Context context, List<Test> dataList, int type) {
        super(context, dataList);
        this.type = type;
    }

    /**
     * 是否显示复选框
     *
     * @param showCbox
     */
    public void setIfShowCbox(boolean showCbox) {
        this.showCbox = showCbox;
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

        //查看答案解析
        LinearLayout llSeeAnswerAnalysis = (LinearLayout) resultView.findViewById(R.id.ll_wrapper_see_answer_analysis_layout_fg_test_choice);

        //答案解析图
        final ImageView iv01Analysis = (ImageView) resultView.findViewById(R.id.iv_01_layout_fg_test_choice);

        //答案解析
        final TextView tvAnalysis = (TextView) resultView.findViewById(R.id.tv_analysis_layout_fg_test_choice);
        tvAnalysis.setText(dataObj.getAnalysis());


        if (type == 0) {//答题状态
            llSeeAnswerAnalysis.setVisibility(View.VISIBLE);
            iv01Analysis.setVisibility(View.GONE);
            tvAnalysis.setVisibility(View.GONE);
        } else {
            llSeeAnswerAnalysis.setVisibility(View.GONE);
            iv01Analysis.setVisibility(View.VISIBLE);
            tvAnalysis.setVisibility(View.VISIBLE);
        }


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
