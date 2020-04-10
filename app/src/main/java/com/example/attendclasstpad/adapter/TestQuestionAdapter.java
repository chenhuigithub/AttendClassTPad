package com.example.attendclasstpad.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.model.Test;

import java.util.List;

/**
 * 试题列表适配器
 */
public class TestQuestionAdapter extends BaseListAdapter<Test> {
    private int type = 0;//默认是答题状态（1为查看答案解析状态,2为答题解析状态，显示柱状图）
    private boolean showCbox;//是否选中

//    private FragmentManager manager;//声明成全局性的

    public TestQuestionAdapter(Context context, List<Test> dataList, int type) {
        super(context, dataList);
        this.type = type;

    }


    public TestQuestionAdapter(Context context, List<Test> dataList, int type, FragmentActivity activity) {
        super(context, dataList);
        this.type = type;

//        manager = activity.getSupportFragmentManager();//FragmentManager调用v4包内的
    }

    /**
     * 是否显示复选框
     *
     * @param showCbox
     */
    public void setIfShowCbox(boolean showCbox) {
        this.showCbox = showCbox;
    }


    /**
     * 点击选项后的处理
     *
     * @param str 显示内容
     */
    private void doAfterClickOption(TextView tv, String str) {
        tv.setVisibility(View.VISIBLE);
        tv.setText(str);
    }


    @Override
    protected int getLayoutResID() {
        return R.layout.layout_fg_test_choice;
    }

    @Override
    protected void doAssignValueForView(int position, View resultView, Test dataObj) {
        //题目
        TextView tvQuestion = (TextView) resultView.findViewById(R.id.tv_content_layout_fg_test_choice);
        tvQuestion.setText(dataObj.getQuestionHtml());

        //柱状图
//        HistogramView vGram = (HistogramView) resultView.findViewById(R.id.gramv_layout_fg_test_choice);
//        List<HistogramView.Histogram> gramList = new ArrayList<HistogramView.Histogram>();
//        HistogramView.Histogram gram = new HistogramView.Histogram();
//        gram.setValueName("20");
//        gram.setValue(60);
//        gramList.add(gram);
//        vGram.setData(gramList);
//        vGram.setTextSize(context.getResources().getDimension(R.dimen.dimen_text_008));

        //对错结果
        ImageView ivResult = (ImageView) resultView.findViewById(R.id.iv_result_layout_fg_test_choice);

        //查看答案解析
        LinearLayout llSeeAnswerAnalysis = (LinearLayout) resultView.findViewById(R.id.ll_wrapper_see_answer_analysis_layout_fg_test_choice);

        //柱状图
        View inHistogram = (View) resultView.findViewById(R.id.in_histogram_layout_fg_test_choice);
        //学员名称
        TextView tvStudentName = (TextView) inHistogram.findViewById(R.id.tv_student_name_layout_v_histogram_answer_analysis);
        tvStudentName.setVisibility(View.GONE);

        //选项
        final LinearLayout llOptionA = (LinearLayout) inHistogram.findViewById(R.id.ll_wrapper_a_layout_v_histogram_answer_analysis);
        llOptionA.setTag(tvStudentName);
        llOptionA.setOnClickListener(new Listeners());

        LinearLayout llOptionB = (LinearLayout) inHistogram.findViewById(R.id.ll_wrapper_b_layout_v_histogram_answer_analysis);
        llOptionB.setTag(tvStudentName);
        llOptionB.setOnClickListener(new Listeners());

        LinearLayout llOptionC = (LinearLayout) inHistogram.findViewById(R.id.ll_wrapper_c_layout_v_histogram_answer_analysis);
        llOptionC.setTag(tvStudentName);
        llOptionC.setOnClickListener(new Listeners());

        LinearLayout llOptionD = (LinearLayout) inHistogram.findViewById(R.id.ll_wrapper_d_layout_v_histogram_answer_analysis);
        llOptionD.setTag(tvStudentName);
        llOptionD.setOnClickListener(new Listeners());

        LinearLayout llOptionUnselected = (LinearLayout) inHistogram.findViewById(R.id.ll_wrapper_unselected_layout_v_histogram_answer_analysis);
        llOptionUnselected.setOnClickListener(new Listeners());
        llOptionUnselected.setTag(tvStudentName);

        //答案解析图
        // final ImageView iv01Analysis = (ImageView) resultView.findViewById(R.id.iv_01_layout_fg_test_choice);

        //答案解析
        final TextView tvAnalysis = (TextView) resultView.findViewById(R.id.tv_analysis_layout_fg_test_choice);
        tvAnalysis.setText(dataObj.getAnswerAnalysisHtml());

        if (type == 0) {//答题状态
            llSeeAnswerAnalysis.setVisibility(View.VISIBLE);
            tvAnalysis.setVisibility(View.GONE);
            ivResult.setVisibility(View.GONE);
            inHistogram.setVisibility(View.GONE);
        } else if (type == 1) {//答案解析
            llSeeAnswerAnalysis.setVisibility(View.GONE);
            tvAnalysis.setVisibility(View.VISIBLE);
            ivResult.setVisibility(View.GONE);
            inHistogram.setVisibility(View.VISIBLE);
        } else {//答题情况解析
            llSeeAnswerAnalysis.setVisibility(View.GONE);
            tvAnalysis.setVisibility(View.VISIBLE);
            ivResult.setVisibility(View.VISIBLE);
            inHistogram.setVisibility(View.GONE);
        }

       /* if (manager != null) {
            FragmentTransaction transaction = manager.beginTransaction();//FragmentTransaction调用v4包内的（FragmentTransaction transaction声明成局部的）
            AnswerAnalysisHistogramFg aahFg = new AnswerAnalysisHistogramFg();
            transaction.replace(R.id.rl_wrapper_histogram_layout_fg_test_choice, aahFg).commit();//替换为名称为A的fragment并显示它
        }*/


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

    /**
     * 控件监听
     */
    private class Listeners implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_wrapper_a_layout_v_histogram_answer_analysis://A选项
                    doAfterClickOption((TextView) v.getTag(), "学员：燕小乙、林冲、陆逊、鲁肃、金角大王、吕小布、周公瑾、李师师");

                    break;
                case R.id.ll_wrapper_b_layout_v_histogram_answer_analysis://B选项
                    doAfterClickOption((TextView) v.getTag(), "学员：武松、关胜、扈三娘、王朗、典韦、荀文若");

                    break;
                case R.id.ll_wrapper_c_layout_v_histogram_answer_analysis://C选项
                    doAfterClickOption((TextView) v.getTag(), "学员：宋公明、关胜");

                    break;
                case R.id.ll_wrapper_d_layout_v_histogram_answer_analysis://D选项
                    doAfterClickOption((TextView) v.getTag(), "学员：曹孟德、袁本初、陈公台、郭奉孝");

                    break;
                case R.id.ll_wrapper_unselected_layout_v_histogram_answer_analysis://未选
                    doAfterClickOption((TextView) v.getTag(), "学员：无");

                    break;
            }

        }
    }
}
