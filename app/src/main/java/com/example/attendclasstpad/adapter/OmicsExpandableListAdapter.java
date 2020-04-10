package com.example.attendclasstpad.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.callback.InterfacesCallback;
import com.example.attendclasstpad.model.Test;
import com.example.attendclasstpad.model.TestData;
import com.example.attendclasstpad.util.ConstantsUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学案 可扩展ListView适配器
 */
public class OmicsExpandableListAdapter extends BaseExpandableListAdapter<TestData, Test> {
    private Context context;

    private String id = "";// 当前位置的数据ID，默认是首项
    private boolean isOnlyRead = true;// 仅供预览，无其它操作
    private InterfacesCallback.ICanKnowSth10 callback10;
    private View vAllLastFocus;//上次点击过的单项布局
    private View vMenuLastFocus;//上次点击过的菜单栏

    private Resources res;

    /**
     * 构造方法
     *
     * @param context
     * @param groupList 父级（一级）目录集合
     * @param dataset   map<父级,子集集合>
     */
    public OmicsExpandableListAdapter(Context context, InterfacesCallback.ICanKnowSth10 callback, List<TestData> groupList, Map<TestData, List<Test>> dataset, boolean isOnlyRead) {
        super(context, groupList, dataset);

        this.context = context;
        callback10 = callback;
        res = context.getResources();

        this.isOnlyRead = isOnlyRead;

        for (TestData test : groupList) {
            List<Test> list = test.getTestList();
            if (list != null) {
                dataset.put(test, list);
            }
        }
    }

    /**
     * 设置菜单栏
     *
     * @param llMenu
     */
    private void setMenu(View llMenu) {
        // 上移
        TextView tvMoveUp = (TextView) llMenu
                .findViewById(R.id.tv_move_up_layout_v_omics_expand_child);
        // 下移
        TextView tvMoveDown = (TextView) llMenu
                .findViewById(R.id.tv_move_down_layout_v_omics_expand_child);

        if (getChildrenCount(gPos) == 1) {// 只有一条数据的情况
            tvMoveUp.setVisibility(View.GONE);
            tvMoveDown.setVisibility(View.GONE);
        } else {
            if (cPos == 0) {// 选中首项的时候，隐藏掉上移按钮
                tvMoveUp.setVisibility(View.GONE);
            } else if (cPos == getChildrenCount(gPos) - 1) {//选中末项的时候，隐藏掉下移按钮
                tvMoveDown.setVisibility(View.GONE);
            } else {
                tvMoveUp.setVisibility(View.VISIBLE);
                tvMoveDown.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 选中状态
     *
     * @param tv 文字
     */
    private void setFocused(TextView tv) {
        tv.setBackgroundResource(R.color.blue3);
    }

    /**
     * 正常状态
     *
     * @param tv 文字
     */
    private void setNormal(TextView tv) {
        tv.setBackgroundResource(R.color.white);
    }

    @Override
    protected int getGroupLayoutResID() {
        return R.layout.layout_v_omics_expand_group;
    }

    @Override
    protected int getChildLayoutResID() {
        return R.layout.layout_v_omics_expand_child;
    }

    @Override
    protected void doAssignValueForGroupView(boolean isExpanded, View resultView, Object dataObj) {
        TextView tvTitle = (TextView) resultView
                .findViewById(R.id.tv_title_layout_v_omics_expand_group);
        if (dataObj instanceof TestData) {
            tvTitle.setText(((TestData) dataObj).getTypeName());
        }
    }

    @Override
    protected void doAssignValueForChildView(final int gPos, final int cPos, boolean isLastChild, View resultView, final Object dataObj) {
        final LinearLayout llWrapperAll = (LinearLayout) resultView
                .findViewById(R.id.ll_wrapper_all_layout_v_omics_expand_child);
        WebView wbvQuestion = (WebView) resultView
                .findViewById(R.id.wb_question_layout_v_omics_expand_child);

        wbvQuestion.setWebViewClient(new WebViewClient());
        wbvQuestion.getSettings().setJavaScriptEnabled(true);
        wbvQuestion.requestFocus();
        wbvQuestion.requestDisallowInterceptTouchEvent(true);

        wbvQuestion.getSettings().setDomStorageEnabled(true);
        wbvQuestion.getSettings().setSupportMultipleWindows(true);
        wbvQuestion.getSettings().setLoadWithOverviewMode(true);
        wbvQuestion.getSettings().setUseWideViewPort(true);
        wbvQuestion.setWebChromeClient(new WebChromeClient());

        // 菜单栏
        final LinearLayout llMenu = (LinearLayout) resultView
                .findViewById(R.id.ll_menu_layout_v_omics_expand_child);
        llMenu.setVisibility(View.GONE);

        // 上移
        final TextView tvMoveUp = (TextView) resultView
                .findViewById(R.id.tv_move_up_layout_v_omics_expand_child);
        // 下移
        final TextView tvMoveDown = (TextView) resultView
                .findViewById(R.id.tv_move_down_layout_v_omics_expand_child);
        // 删除
        final TextView tvDelete = (TextView) resultView
                .findViewById(R.id.tv_delete_layout_v_omics_expand_child);
        // 没有题文
        final TextView tvNoQuestion = (TextView) resultView
                .findViewById(R.id.tv_no_question_layout_v_omics_expand_child);

        if (isOnlyRead) {
            llMenu.setVisibility(View.GONE);
        } else {
            llMenu.setVisibility(View.VISIBLE);
        }

        if (dataObj instanceof Test) {
            final Test test = ((Test) dataObj);
            String qHtml = test.getQuestionHtml();
            final String id = test.getId();

            if (TextUtils.isEmpty(qHtml)) {
                tvNoQuestion.setVisibility(View.VISIBLE);
                wbvQuestion.setVisibility(View.GONE);
            } else {
                tvNoQuestion.setVisibility(View.GONE);
                wbvQuestion.setVisibility(View.VISIBLE);

                // 加载代码，并设定编码格式和字符集
                wbvQuestion.loadDataWithBaseURL(null, (String) qHtml, "text/html", "utf-8", null);
            }

            HashMap<Integer, Integer> i = new HashMap<Integer, Integer>();
            i.put(gPos, cPos);
            if (getPosition().equals(i) && !isOnlyRead) {// 加载到的位置刚好是选中的位置
                llWrapperAll.setBackgroundResource(R.drawable.selector_for_green_stroke_rectangle);
                // 显示菜单栏
                llMenu.setVisibility(View.VISIBLE);

                vAllLastFocus = llWrapperAll;
                vMenuLastFocus = llMenu;

                setMenu(llMenu);
            } else {
                llWrapperAll.setBackgroundResource(R.color.white);
                // 隐藏菜单栏
                llMenu.setVisibility(View.GONE);
            }

            if (!isOnlyRead) {
                wbvQuestion.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_UP://抬起
                                setPosition(gPos, cPos);

                                View vAll = (View) v.getParent().getParent().getParent();
                                if (vAll != null) {
                                    vAll.setBackgroundResource(R.drawable.selector_for_green_stroke_rectangle);

                                    View vMenu = ((View) v.getParent().getParent()).findViewById(R.id.ll_menu_layout_v_omics_expand_child);
                                    if (vMenu != null) {
                                        vMenu.setVisibility(View.VISIBLE);
                                        setMenu(llMenu);
                                    }

                                    vMenuLastFocus = vMenu;
                                    vAllLastFocus = vAll;
                                }

                                break;
                            case MotionEvent.ACTION_DOWN://按下
                                View vAll0 = (View) v.getParent().getParent().getParent();
                                if (vAllLastFocus != vAll0) {//本次点击与上次点击项不同
                                    if (vAllLastFocus != null) {
                                        vAllLastFocus.setBackgroundResource(R.color.white);
                                    }
                                    if (vMenuLastFocus != null) {
                                        vMenuLastFocus.setVisibility(View.GONE);
                                    }
                                }

                                break;
                        }

                        return true;
                    }
                });
            }

            tvDelete.setOnClickListener(new View.OnClickListener() {// 删除
                @Override
                public void onClick(View v) {
                    HashMap<Integer, Integer> i = new HashMap<Integer, Integer>();
                    i.put(gPos, getChildrenCount(gPos) - 1);
                    if (getPosition().equals(i) && getChildrenCount(gPos) > 0) {
                        setPosition(gPos, cPos - 1);
                    }
                    removeItem(gPos, cPos);

                    //重置
                    setPosition(0, -1);

                    if (callback10 != null) {
                        callback10.doSth(ConstantsUtils.DELETE, getPosition());
                    }

                    Intent intent = new Intent();
                    intent.setAction(ConstantsUtils.DELETE_OMICS_PREVIEW);// 删除预览项
                    intent.putExtra(ConstantsUtils.ID, id);// 题目ID
                    LocalBroadcastManager.getInstance(context)
                            .sendBroadcast(intent);
                }
            });

            tvMoveUp.setOnClickListener(new View.OnClickListener() {// 上移
                @Override
                public void onClick(View arg0) {
                    indexExChange(gPos, cPos, cPos - 1);
                    setPosition(gPos, cPos - 1);

                    if (callback10 != null) {
                        callback10.doSth(ConstantsUtils.MOVE_UP, getPosition());
                    }
                }
            });

            tvMoveDown.setOnClickListener(new View.OnClickListener() {// 下移
                @Override
                public void onClick(View arg0) {
                    indexExChange(gPos, cPos, cPos + 1);
                    setPosition(gPos, cPos + 1);

                    if (callback10 != null) {
                        callback10.doSth(ConstantsUtils.MOVE_DOWN, getPosition());
                    }
                }
            });
        }
    }
}
