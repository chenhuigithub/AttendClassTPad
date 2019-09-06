package com.example.attendclasstpad.adapter;

import android.content.Context;
import android.view.View;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.model.Test;
import com.example.attendclasstpad.model.TestPaper;

import java.util.List;

/**
 * 答题结果 学生分析列表适配器
 *
 * @author chenhui 2019.09.06
 */
public class AnswerTestPaperStudentAnalysisAdapter extends BaseListAdapter<TestPaper> {
    public AnswerTestPaperStudentAnalysisAdapter(Context context, List<TestPaper> dataList) {
        super(context, dataList);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.layout_v_answer_test_paper_student_ananysis_item;
    }

    @Override
    protected void doAssignValueForView(int position, View resultView, TestPaper dataObj) {

    }
}
