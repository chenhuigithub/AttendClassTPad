package com.example.attendclasstpad.fg;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.adapter.AnswerTestPaperStudentAnalysisAdapter;
import com.example.attendclasstpad.adapter.TestQuestionAdapter;
import com.example.attendclasstpad.model.Test;
import com.example.attendclasstpad.model.TestPaper;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生（答题结果）分析
 */
public class AnswerTestPaperStudentAnalysisFg extends BaseNotPreLoadFg {
    private boolean isPrepared;// 标志位，标志已经初始化完成
    private boolean hasLoadOnce;// 是否已被加载过一次，第二次就不再去请求数据了

    private List<TestPaper> paperList;

    private View allFgView;// 总布局
    private ListView lvContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == allFgView) {
            allFgView = View.inflate(getActivity(),
                    R.layout.layout_v_answer_test_paper_student_analysis, null);
            paperList = new ArrayList<TestPaper>();

            lvContent = (ListView) allFgView
                    .findViewById(R.id.lv_content_layout_v_atps_analysis);
            lvContent.setAdapter(new AnswerTestPaperStudentAnalysisAdapter(getActivity(), getTestPaperData()));

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

    /**
     * 获取试卷列表（测试用）
     *
     * @return list 试卷列表
     */
    private List<TestPaper> getTestPaperData() {
        for (int i = 1; i <= 10; i++) {
            TestPaper paper = new TestPaper();
            paper.setID("key" + i);
            paper.setName("沁园春·长沙 测试卷" + i);
            paper.setCreateTime("2019/6/8");
            paper.setTestNum("10");
            paper.setType(String.valueOf(i - 1));
            paperList.add(paper);
        }
        return paperList;
    }


    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || hasLoadOnce) {
            return;
        }
    }
}
