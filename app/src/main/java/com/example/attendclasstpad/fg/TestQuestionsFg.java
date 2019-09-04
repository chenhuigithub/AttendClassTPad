package com.example.attendclasstpad.fg;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.callback.InterfacesCallback.doOnQuestionCallback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 答题界面通用Fragment
 *
 * @author zhaochenhui_2017.05.11
 */
@SuppressLint({"ValidFragment", "NewApi"})
public class TestQuestionsFg extends BaseNotPreLoadFg {
    /**
     * 显示标题栏面板01
     */
    public final static String SHOW_TITLE_BAR_WRAPPER01 = "1";

    private doOnQuestionCallback callback;// 接口回调

    private String showType = SHOW_TITLE_BAR_WRAPPER01;// 显示状态，默认显示第一种

    private boolean isPrepared;// 标志位，标志已经初始化完成
    private boolean hasLoadOnce;// 是否已被加载过一次，第二次就不再去请求数据了
    private BroadcastReceiver receiver;// 广播
    private LocalBroadcastManager broadcastManager;// 广播接收

    private View allFgView;// 总布局

    private RelativeLayout rlTitleBar01; // 标题栏面板01
    private RelativeLayout rlTitleBar02;// 标题栏面板02

    @Override
    public void onAttach(Activity activity) {
        if (activity instanceof doOnQuestionCallback) {
            callback = (doOnQuestionCallback) activity;
        } else {
            Log.i("TestQuestionsFg", activity.toString()
                    + "must implement doOnQuestionCallback");
        }

        super.onAttach(activity);
    }

    /**
     * 构造方法
     */
    public TestQuestionsFg() {
        if (SHOW_TITLE_BAR_WRAPPER01.equals(showType)) {
            showType = SHOW_TITLE_BAR_WRAPPER01;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == allFgView) {
            allFgView = inflater.inflate(R.layout.layout_ppt, null);

//			rlTitleBar01 = (RelativeLayout) allFgView
//					.findViewById(R.id.rl_wrapper_title_bar01_layout_activity_test_question);
//			if (SHOW_TITLE_BAR_WRAPPER01.equals(showType)) {// 显示标题栏面板01
//			} else {
//				rlTitleBar01.setVisibility(View.GONE);
//			}


//			// 选项
//			final LinearLayout llA = (LinearLayout) allFgView
//					.findViewById(R.id.ll_a_layout_fg_test_question);
//			final LinearLayout llB = (LinearLayout) allFgView
//					.findViewById(R.id.ll_b_layout_fg_test_question);
//			final LinearLayout llC = (LinearLayout) allFgView
//					.findViewById(R.id.ll_c_layout_fg_test_question);
//			final LinearLayout llD = (LinearLayout) allFgView
//					.findViewById(R.id.ll_d_layout_fg_test_question);
//
//			final LinearLayout llAnalysis = (LinearLayout) allFgView
//					.findViewById(R.id.ll_wrapper_analysis_layout_fg_test_question);
//			llAnalysis.setVisibility(View.GONE);
            // 答案解析
            // final TextView tvLblAnalysis = (TextView) allFgView
            // .findViewById(R.id.tv_lbl_analysis_layout_fg_test_question);
            // tvLblAnalysis.setVisibility(View.GONE);
            // 答案解析
            // final TextView tvAnalysis = (TextView)
            // allFgView.findViewById(R.id.tv_analysis_layout_fg_test_question);
            // tvAnalysis.setVisibility(View.GONE);

            // 正确答案
            // tvTrueAnswer = (TextView)
            // allFgView.findViewById(R.id.tv_true_answer_layout_fg_test_question);
            // tvTrueAnswer.setVisibility(View.INVISIBLE);
            // 您的选择
            // tvYourChoice = (TextView)
            // allFgView.findViewById(R.id.tv_your_choice_layout_fg_test_question);
            // tvYourChoice.setVisibility(View.INVISIBLE);
//
//			llA.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					// 跳转至答题结果界面
//					// Intent intent = new Intent(getActivity(),
//					// TestQuestionSituationActivity.class);
//					// startActivity(intent);
//				}
//			});

//			llB.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					llA.performClick();
//				}
//			});
//
//			llC.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					llA.performClick();
//				}
//			});
//
//			llD.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					llA.performClick();
//				}
//			});

            // 注册广播接收
//			broadcastManager = LocalBroadcastManager.getInstance(getActivity());
//
//			IntentFilter filter = new IntentFilter();
            // filter.addAction(ConstantsUtils.ACTION_GO_TO_ANSWER_ANALYSIS);
            // filter.addAction(ConstantsUtils.ACTION_CONTINUE_EXTERCISE);
            // filter.addAction(ConstantsUtils.ACTION_FINISH);

            // receiver = new BroadcastReceiver() {
            // @Override
            // public void onReceive(Context context, Intent intent) {
            // String action = intent.getAction();
            // if (ConstantsUtils.ACTION_GO_TO_ANSWER_ANALYSIS.equals(action))
            // {// 答案解析
            // // tvTrueAnswer.setVisibility(View.VISIBLE);
            // // tvYourChoice.setVisibility(View.VISIBLE);
            // // tvLblAnalysis.setVisibility(View.VISIBLE);
            // // tvAnalysis.setVisibility(View.VISIBLE);
            // llAnalysis.setVisibility(View.VISIBLE);
            //
            // llA.setBackgroundResource(R.drawable.selector_for_true_question_answer_item);
            // llB.setBackgroundResource(R.drawable.selector_for_user_question_choice_item);
            //
            // llA.setClickable(false);
            // llB.setClickable(false);
            // llC.setClickable(false);
            // llD.setClickable(false);
            //
            // if (callback != null) {
            // callback.doAfterKonwShowType(false);
            // }
            // } else if
            // (ConstantsUtils.ACTION_CONTINUE_EXTERCISE.equals(action)) {//
            // 继续做题，即重新答题
            //
            // } else if (ConstantsUtils.ACTION_FINISH.equals(action)) {// 关闭界面
            // getActivity().finish();
            // }
            // }
            // };

//			broadcastManager.registerReceiver(receiver, filter);
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

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || hasLoadOnce) {
            return;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (receiver != null) {
            broadcastManager.unregisterReceiver(receiver);
        }
    }
}
