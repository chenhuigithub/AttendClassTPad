package com.example.attendclasstpad.aty;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.adapter.TestQuestionAdapter;
import com.example.attendclasstpad.model.Test;
import com.example.attendclasstpad.model.TestPaper;
import com.example.attendclasstpad.util.ConstantsUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生个人答卷情况
 *
 * @author chenhui 2019.09.18
 */
public class StudentAnswerPaperSituationAty extends FragmentActivity {
    private TestPaper paper;//试卷(包含试题)
    private List<Test> questionList;//试题
    private TestQuestionAdapter questionAdapter;//题目适配器

    private ListView lvTestQuestion;//试题
    private TextView tvPaperName;//试卷名称
    private RelativeLayout rlColose;//关闭按钮

    public StudentAnswerPaperSituationAty() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        StudentAnswerPaperSituationAty.this.setFinishOnTouchOutside(false);

        setContentView(R.layout.layout_aty_student_answer_paper_situation);

        questionList = new ArrayList<Test>();

        lvTestQuestion = (ListView) findViewById(R.id.lv_test_question_layout_saps);
        tvPaperName = (TextView) findViewById(R.id.tv_paper_name_paper_name_layout);
        //关闭
        rlColose = (RelativeLayout) findViewById(R.id.rl_wrapper_close_layout_aty_saps);
        rlColose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dealWithExtras();

        setQuestionAdapter();
    }

    private void dealWithExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        //试卷信息
        Serializable infoPaperSer = bundle.getSerializable(ConstantsUtils.PAPER_INFO);
        if (infoPaperSer != null) {
            //试卷
            paper = (TestPaper) infoPaperSer;
            tvPaperName.setText(paper.getName());

            //试题
            questionList = paper.getQuestionList();
        }
    }

    /**
     * 题目适配器
     */
    private void setQuestionAdapter() {
        if (questionAdapter == null) {
            questionAdapter = new TestQuestionAdapter(this, questionList, 2, this);
            lvTestQuestion.setAdapter(questionAdapter);
        } else {
            questionAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
