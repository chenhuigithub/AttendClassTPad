package com.example.attendclasstpad.aty;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.fg.AnswerTestPaperQuestionAnalysisFg;
import com.example.attendclasstpad.fg.AnswerTestPaperStudentAnalysisFg;
import com.example.attendclasstpad.view.CustomViewpager;

/**
 * 答卷（答题结果）报告
 */
public class AnswerTestPaperReportAty extends FragmentActivity {
    private int currIndex = 0;// 当前页卡编号
    private int offset = 0;// 动画图片偏移量
    private int position_one;

    private FragmentManager manager;// Fragment工具

    private AnswerTestPaperStudentAnalysisFg sAnalysis;
    private AnswerTestPaperQuestionAnalysisFg qAnalysis;

    private CustomViewpager vpagerTest;//滑动布局
    private TextView tvStudentAnalysis;//学生分析
    private TextView tvQuestionAnalysis;//试题分析
    private LinearLayout llBackUpperLevel;//返回上一级


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_aty_answer_test_paper_report);

        manager = getSupportFragmentManager();//  FragmentManager调用v4包内的

        llBackUpperLevel = (LinearLayout) findViewById(R.id.ll_wrapper_back_upper_level_layout_aty_atp_report);


        sAnalysis = new AnswerTestPaperStudentAnalysisFg();// 学生分析
        qAnalysis = new AnswerTestPaperQuestionAnalysisFg();// 试题分析

        initTab();
        initMenuListeners();

        initFg();

        tvStudentAnalysis.performClick();
    }


    /**
     * 初始化切换布局的头标
     */
    private void initTab() {
        tvStudentAnalysis = (TextView) findViewById(R.id.tv_student_analysis_layout_aty_atp_report);
        tvQuestionAnalysis = (TextView) findViewById(R.id.tv_question_analysis_layout_aty_atp_report);
    }


    private void initFg() {
        manager = getSupportFragmentManager();// FragmentManager调用v4包内的

        showFragment(sAnalysis);
    }

    private void initMenuListeners() {
        llBackUpperLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvStudentAnalysis.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                tvStudentAnalysis.setTextColor(getResources().getColor(R.color.blue));
                tvQuestionAnalysis.setTextColor(getResources().getColor(R.color.color_text_content));

                showFragment(sAnalysis);
            }
        });
        tvQuestionAnalysis.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                tvQuestionAnalysis.setTextColor(getResources().getColor(R.color.blue));
                tvStudentAnalysis.setTextColor(getResources().getColor(R.color.color_text_content));

                showFragment(qAnalysis);
            }
        });
    }


    /**
     * 展示布局
     *
     * @param fg
     */
    private void showFragment(Fragment fg) {
        FragmentTransaction transaction = manager.beginTransaction();// FragmentTransaction调用v4包内的
        if (fg.isAdded()) {// 如果该fragment已添加过，直接显示
            transaction.show(fg).commit();
        } else {
            transaction.replace(R.id.fl_content_layout_aty_atp_report, fg)
                    .commit();// 替换为名称为A的fragment并显示它
        }
    }

}
