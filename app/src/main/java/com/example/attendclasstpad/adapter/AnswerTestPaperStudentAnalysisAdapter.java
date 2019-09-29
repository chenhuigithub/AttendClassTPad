package com.example.attendclasstpad.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.callback.ActivityFgInterface;
import com.example.attendclasstpad.model.Test;
import com.example.attendclasstpad.model.TestPaper;

import java.util.List;

/**
 * 答题结果 学生分析列表适配器
 *
 * @author chenhui 2019.09.06
 */
public class AnswerTestPaperStudentAnalysisAdapter extends BaseListAdapter<TestPaper> {
    private ActivityFgInterface.ICanKnowFgDoSthAboutMenu callback;

    public AnswerTestPaperStudentAnalysisAdapter(Context context, List<TestPaper> dataList, ActivityFgInterface.ICanKnowFgDoSthAboutMenu callback) {
        super(context, dataList);

        this.callback = callback;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.layout_v_answer_test_paper_student_ananysis_item;
    }

    @Override
    protected void doAssignValueForView(final int position, View resultView, TestPaper dataObj) {
        //查看
        TextView tvSee = (TextView) resultView.findViewById(R.id.tv_see_layout_v_atpsa_item);
        tvSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.doOnAfterClickMenu(0, position);
            }
        });

    }
}
