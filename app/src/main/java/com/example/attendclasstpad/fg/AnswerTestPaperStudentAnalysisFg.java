package com.example.attendclasstpad.fg;

import android.content.Intent;
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
import com.example.attendclasstpad.aty.StudentAnswerPaperSituationAty;
import com.example.attendclasstpad.callback.ActivityFgInterface;
import com.example.attendclasstpad.model.Test;
import com.example.attendclasstpad.model.TestPaper;
import com.example.attendclasstpad.util.ConstantsUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生（答题结果）分析
 */
public class AnswerTestPaperStudentAnalysisFg extends BaseNotPreLoadFg implements ActivityFgInterface.ICanKnowFgDoSthAboutMenu {
    private boolean isPrepared;// 标志位，标志已经初始化完成
    private boolean hasLoadOnce;// 是否已被加载过一次，第二次就不再去请求数据了

    private List<TestPaper> paperList;//试卷
    private List<Test> questionList;//题目

    private View allFgView;// 总布局
    private ListView lvContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == allFgView) {
            allFgView = View.inflate(getActivity(),
                    R.layout.layout_v_answer_test_paper_student_analysis, null);
            paperList = new ArrayList<TestPaper>();
            questionList = new ArrayList<Test>();

            lvContent = (ListView) allFgView
                    .findViewById(R.id.lv_content_layout_v_atps_analysis);
            lvContent.setAdapter(new AnswerTestPaperStudentAnalysisAdapter(getActivity(), getTestPaperData(), this));

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
            paper.setQuestionList(getTestQuestionData());

            paperList.add(paper);
        }
        return paperList;
    }

    /**
     * 获取试题列表（测试用）
     *
     * @return list 试卷列表
     */
    private List<Test> getTestQuestionData() {
        questionList = new ArrayList<Test>();
        for (int i = 1; i <= 10; i++) {
            Test test = new Test();
            test.setId("key" + i);
            test.setContent(i + ".以下历史事件中，与关羽无关的是（）：\n Ａ：单刀赴会　Ｂ：水淹七军　Ｃ：大意失荆州　Ｄ：七擒七纵 \n 2：“东风不与周郎便，铜雀春深锁二乔”。这首诗的作者生活的年代与诗中所描述的历史事件发生的年代大约相隔了（）：\n Ａ：４００年　Ｂ：５００年　 Ｃ：６００年　Ｄ：８００年");
            test.setAnalysis("七擒孟获，又称南中平定战，是建兴三年蜀汉丞相诸葛亮对南中发动平定南中的战争。当时朱褒、雍闿、高定等人叛变，南中豪强孟获亦有参与，最后诸葛亮亲率大军南下，平定南中。");
            questionList.add(test);

//            paper.setName("1.沁园春·长沙 测试卷" + i);
//            paper.setCreateTime("2019/6/8");
//            paper.setTestNum("10");
//            paper.setType(String.valueOf(i - 1));
        }
        return questionList;
    }


    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || hasLoadOnce) {
            return;
        }
    }

    @Override
    public void doOnAfterClickMenu(int menuID, int pos) {
        switch (menuID) {
            case 0://查看（个人答题情况）
                Intent intent = new Intent(getActivity(), StudentAnswerPaperSituationAty.class);
                if (paperList.get(pos) != null) {
                    intent.putExtra(ConstantsUtils.PAPER_INFO, (Serializable) paperList.get(pos));
                }

                startActivity(intent);

                break;
        }
    }
}
