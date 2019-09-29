package com.example.attendclasstpad.fg;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.attendclasstpad.R;

/**
 * 学生答题情况柱状图分析
 */
public class AnswerAnalysisHistogramFg extends Fragment {
    private TextView tvStudentName;//学员姓名

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vAll = inflater.inflate(R.layout.layout_v_histogram_answer_analysis, null);
        //选项
        LinearLayout llOptionA = (LinearLayout) vAll.findViewById(R.id.ll_wrapper_a_layout_v_histogram_answer_analysis);
        llOptionA.setOnClickListener(new Listeners());
        LinearLayout llOptionB = (LinearLayout) vAll.findViewById(R.id.ll_wrapper_b_layout_v_histogram_answer_analysis);
        llOptionB.setOnClickListener(new Listeners());
        LinearLayout llOptionC = (LinearLayout) vAll.findViewById(R.id.ll_wrapper_c_layout_v_histogram_answer_analysis);
        llOptionC.setOnClickListener(new Listeners());
        LinearLayout llOptionD = (LinearLayout) vAll.findViewById(R.id.ll_wrapper_d_layout_v_histogram_answer_analysis);
        llOptionD.setOnClickListener(new Listeners());
        LinearLayout llOptionUnselected = (LinearLayout) vAll.findViewById(R.id.ll_wrapper_unselected_layout_v_histogram_answer_analysis);
        llOptionUnselected.setOnClickListener(new Listeners());

        tvStudentName = (TextView) vAll.findViewById(R.id.tv_student_name_layout_v_histogram_answer_analysis);
        tvStudentName.setVisibility(View.GONE);

        return vAll;
    }

    /**
     * 点击选项后的处理
     *
     * @param str 显示内容
     */
    private void doAfterClickOption(String str) {
        tvStudentName.setVisibility(View.VISIBLE);
        tvStudentName.setText(str);
    }

    /**
     * 控件监听
     */
    private class Listeners implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_wrapper_a_layout_v_histogram_answer_analysis://A选项
                    doAfterClickOption("燕小乙、卢俊义、陆逊、鲁肃、金角大王");

                    break;
                case R.id.ll_wrapper_b_layout_v_histogram_answer_analysis://B选项
                    doAfterClickOption("宋押司、关胜、扈三娘、王朗、典韦");


                    break;
                case R.id.ll_wrapper_c_layout_v_histogram_answer_analysis://C选项
                    doAfterClickOption("宋押司、关胜、张翼德、王朗、袁绍");

                    break;
                case R.id.ll_wrapper_d_layout_v_histogram_answer_analysis://D选项
                    doAfterClickOption("曹孟德、刘玄德、孙权、陈公台");

                    break;
                case R.id.ll_wrapper_unselected_layout_v_histogram_answer_analysis://未选
                    tvStudentName.setVisibility(View.GONE);

                    break;
            }

        }
    }
}
