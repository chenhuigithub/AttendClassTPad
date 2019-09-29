package com.example.attendclasstpad.fg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.attendclasstpad.adapter.TestPaperAdapter;
import com.example.attendclasstpad.adapter.TestQuestionAdapter;
import com.example.attendclasstpad.aty.AnswerStatisticsActivity;
import com.example.attendclasstpad.aty.AnswerTestPaperReportAty;
import com.example.attendclasstpad.aty.ChoiceTeachingMaterialAty;
import com.example.attendclasstpad.R;
import com.example.attendclasstpad.adapter.FragmentVPagerAdapter;
import com.example.attendclasstpad.adapter.SpinnerImitateAdapter;
import com.example.attendclasstpad.callback.ActivityFgInterface;
import com.example.attendclasstpad.model.ICanGetKeyValue;
import com.example.attendclasstpad.model.Test;
import com.example.attendclasstpad.application.CustomApplication;
import com.example.attendclasstpad.model.TestPaper;
import com.example.attendclasstpad.util.ConstantsUtils;
import com.example.attendclasstpad.util.ValidateFormatUtils;
import com.example.attendclasstpad.util.VariableUtils;
import com.example.attendclasstpad.view.BgDarkPopupWindow;
import com.example.attendclasstpad.view.CustomViewpager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

/**
 * 测试
 */
@SuppressLint("HandlerLeak")
public class TestFg extends BaseNotPreLoadFg implements ActivityFgInterface.ICanKnowFgDoSthAboutMenu {
    private boolean isPrepared;// 标志位，标志已经初始化完成
    private boolean hasLoadOnce = false;// 是否已被加载过一次，第二次就不再去请求数据了
    private WindowManager.LayoutParams lp;

    private int currIndex = 0;// 当前页卡编号
    private int offset = 0;// 动画图片偏移量
    private LinearLayout llCursor;// 滑动条
    private ImageView imgv_line;
    private int bottomLineWidth;
    private int position_one;
    private List<String> courseList;// 设置数据
    private List<Test> questionList;// 题目数据
    private List<TestPaper> paperList;//试卷数据
    private TestPaper paperFocus;//当前选中的试卷

    private String catalogIDCurr = "";// 目录ID
    private String catalogNameCurr = "";// 目录名称

    private List<String> conditionList;// 筛选条件列表
    private String tabNameIsFocus = "";// tab是否被选中

    private int hasChoicedTestNum = 0;// 已选中的题目数
    private Animation arrowRotateAnim;// 箭头的旋转动画
    private Animation arrowRotateAnim02;// 箭头的旋转动画

    private View allFgView;// 总布局
    private TextView tvElect;// 选择题
    private TextView tvPictureAnswer;// 拍照答题
    private CustomViewpager vpagerTest;//滑动布局
    private TextView tvTitleName;// 标题栏文字
    private TextView tvSwitch;// 切换
    private BgDarkPopupWindow window;// 弹框
    private LinearLayout llSetting;// 设置
    private ImageView ivArrow;// tab箭头
    private ListView lstv;

    private TextView tvClicked;// 当前点击的tab文字
    // private ImageView imgvArrowUp;

    private SpinnerImitateAdapter mAdapter;

    private TestPaperAdapter paperAdapter;//试卷适配器
    private TestQuestionAdapter questionAdapter;//题目适配器

    private ImageView ivCursor01;// 滑动条
    private ImageView ivCursor02;// 滑动条
    private TextView tvHasChoicedNum;// 选中的题目个数
    private RelativeLayout rl01;//题目相关布局
    private RelativeLayout rl02;
    private LinearLayout llSubmit;// 提交进度
    private LinearLayout llAnswerStatistics;// 答题统计
    private LinearLayout llHasChoiceCount;// 选题情况，已选多少题
    private TextView tvBeginAnswer;//开始答题/查看报告
    private ListView lvTestPaper;//试卷
    private ListView lvTestQuestion;//试题
    private TextView tvDelete;//删除
    private TextView tvCancel;//取消（退出选择状态）
    private TextView tvChoiceAll;//全选
    private ActivityFgInterface.ICanKnowFgDoSthAboutMenu iCan;

    // LinearLayout mlay;

    private Handler handler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case ConstantsUtils.ADD_CHOICE_TEST:// 选中题目
                    hasChoicedTestNum = hasChoicedTestNum + 1;
                    tvHasChoicedNum.setText(String.valueOf(hasChoicedTestNum));

                    Object obj = msg.obj;
                    if (obj != null) {
                        questionList = (List<Test>) obj;
                    }

                    break;
                case ConstantsUtils.REMOVE_CHOICE_TEST:// 移除选中题目
                    hasChoicedTestNum = hasChoicedTestNum - 1;
                    tvHasChoicedNum.setText(String.valueOf(hasChoicedTestNum));

                    Object obj1 = msg.obj;
                    if (obj1 != null) {
                        questionList = (List<Test>) obj1;
                    }

                    break;
                case ConstantsUtils.CHOICE_ALL_TEST:// 选中全部题目
                    Object obj2 = msg.obj;
                    if (obj2 != null) {
                        questionList = (List<Test>) obj2;
                    }
                    hasChoicedTestNum = questionList.size();

                    tvHasChoicedNum.setText(String.valueOf(hasChoicedTestNum));

                    break;
                case ConstantsUtils.REMOVE_CHOICE_ALL_TEST:// 移除选中的全部题目
                    hasChoicedTestNum = 0;

                    tvHasChoicedNum.setText(String.valueOf(hasChoicedTestNum));
            }
        }
    };

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == allFgView) {
            allFgView = inflater.inflate(R.layout.layout_fg_test, null);

            paperList = new ArrayList<TestPaper>();
            questionList = new ArrayList<Test>();

            //设置
            llSetting = (LinearLayout) allFgView
                    .findViewById(R.id.ll_setting_layout_fg_test);
            llSetting.setVisibility(View.GONE);

            ivArrow = (ImageView) allFgView
                    .findViewById(R.id.iv_setting_layout_fg_test);

            // 标题栏文字
            tvTitleName = (TextView) allFgView
                    .findViewById(R.id.tv_title_layout_fg_test);
            // 切换教材、目录
            tvSwitch = (TextView) allFgView
                    .findViewById(R.id.tv_omics_case_tab);
            tvSwitch.setOnClickListener(new Listeners());

            // 滑动条
            ivCursor01 = (ImageView) allFgView
                    .findViewById(R.id.iv_cursor01_layout_fg_test);
            ivCursor02 = (ImageView) allFgView
                    .findViewById(R.id.iv_cursor02_layout_fg_test);

            tvHasChoicedNum = (TextView) allFgView
                    .findViewById(R.id.tv_has_choiced_num_layout_fg_test);
            tvHasChoicedNum.setText(String.valueOf(hasChoicedTestNum));

            // 提交进度
            llSubmit = (LinearLayout) allFgView
                    .findViewById(R.id.ll_submit__wrapper_vpager_tab);
            llSubmit.setVisibility(View.GONE);

            // 答题统计
            llAnswerStatistics = (LinearLayout) allFgView
                    .findViewById(R.id.ll_answer_statistics_layout_fg_test);
            llAnswerStatistics.setVisibility(View.GONE);
            llAnswerStatistics.setOnClickListener(new Listeners());

            // 选题情况，已选多少题
            llHasChoiceCount = (LinearLayout) allFgView
                    .findViewById(R.id.ll_has_choice_count_layout_fg_test);
            llHasChoiceCount.setVisibility(View.GONE);

            // 开始答题/查看报告
            tvBeginAnswer = (TextView) allFgView
                    .findViewById(R.id.tv_begin_answer_layout_fg_test);


            rl01 = (RelativeLayout) allFgView
                    .findViewById(R.id.ll_wrapper01_layout_fg_test);

            rl02 = (RelativeLayout) allFgView
                    .findViewById(R.id.rl02_wrapper_layout_fg_test);

            //返回上一层级
            LinearLayout ivBackUpperLevel = (LinearLayout) allFgView.findViewById(R.id.ll_wrapper_back_upper_level_layout_fg_test);
            ivBackUpperLevel.setOnClickListener(new Listeners());


            lvTestPaper = (ListView) allFgView
                    .findViewById(R.id.lv_test_paper_layout_fg_test);

            iCan = (ActivityFgInterface.ICanKnowFgDoSthAboutMenu) this;
            paperAdapter = new TestPaperAdapter(getActivity(), iCan, getTestPaperData());
            paperAdapter.setIfShowCbox(false);
            lvTestPaper.setAdapter(paperAdapter);

            lvTestQuestion = (ListView) allFgView
                    .findViewById(R.id.lv_test_question_layout_fg_test);
            questionAdapter = new TestQuestionAdapter(getActivity(), paperList.get(0).getQuestionList(), 0);
            lvTestQuestion.setAdapter(questionAdapter);

            tvDelete = (TextView) allFgView.findViewById(R.id.tv_delete_layout_fg_test);
            tvDelete.setOnClickListener(new Listeners());

            tvCancel = (TextView) allFgView.findViewById(R.id.tv_cancel_choice_layout_fg_test);
            tvCancel.setVisibility(View.GONE);
            tvCancel.setOnClickListener(new Listeners());

            tvChoiceAll = (TextView) allFgView.findViewById(R.id.tv_choice_all_layout_fg_test);
            tvChoiceAll.setVisibility(View.GONE);
            tvChoiceAll.setOnClickListener(new Listeners());

            if (paperList.size() > 0) {
                paperFocus = paperList.get(0);

                switchTestShow(0, paperFocus);
            }

            setLstvPaperListeners();

            // 科目的数据
            String[] names1 = getResources().getStringArray(R.array.arrays_set);
            courseList = new ArrayList<String>();
            conditionList = new ArrayList<String>();
            questionList = new ArrayList<Test>();

            for (int i = 0; i < names1.length; i++) {
                courseList.add(names1[i]);
            }

            // 箭头的旋转动画
            arrowRotateAnim = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.arrow_rotate_anim);
            arrowRotateAnim.setFillAfter(true);

            arrowRotateAnim02 = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.anim_rotate_anim_02);
            arrowRotateAnim02.setFillAfter(true);

            View view = LayoutInflater.from(getActivity()).inflate(
                    R.layout.layout_spinner_imitate_window, null);
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (window != null) {
                            window.dismiss();
                        }

                        return true;
                    }

                    // 释放焦点
                    tabNameIsFocus = "";

                    // 还原之前点击过的状态
                    setNormalEffect(ivArrow);// 课程分类

                    return false;
                }
            });
            lstv = (ListView) view
                    .findViewById(R.id.lstv_content_layout_spiner_window);
            lstv.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // 分类
                    if (window.isShowing()) {
                        // 释放焦点
                        tabNameIsFocus = "";

                        // setTabText(position, conditionList, tvClicked);

                        window.dismiss();
                    }
                }
            });
            window = new BgDarkPopupWindow(getActivity(), view,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);

//            initVPagerTab();
//            initVPagerContent();
            // InitWidth();

            llSetting.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTabSettingFocusEffect(ivArrow, courseList, "12121212",
                            null, null, null, null);
                }
            });
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
     * 试卷列表点击事件
     */
    private void setLstvPaperListeners() {
        lvTestPaper.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                paperFocus = paperList.get(position);
                if (paperAdapter.getShowCbox()) {//选择状态
                    if (paperFocus != null && paperFocus.isChoiced()) {
                        if (view.getTag() != null && 1 == (int) view.getTag()) {//长按状态
                        } else {//非长按状态
                            paperFocus.setChoiced(false);
                        }
                    } else {
                        paperFocus.setChoiced(true);
                    }

                    //恢复正常状态
                    view.setTag(0);

                    if (paperFocus != null) {
                        questionList = paperFocus.getQuestionList();
                    }

                    if (paperAdapter != null) {
                        paperAdapter.setIfShowCbox(true);
                        paperAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (paperList.size() > 0) {
                        switchTestShow(1, paperList.get(position));
                    }

                    tvDelete.setVisibility(View.GONE);
                }
            }
        });

        lvTestPaper.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < paperList.size(); i++) {
                    TestPaper paper = paperList.get(i);
                    if (paper != null) {
                        if (i == position) {
                            paper.setChoiced(true);
                        } else {
                            paper.setChoiced(false);
                        }
                    }
                }

                if (paperAdapter != null) {
                    paperAdapter.setIfShowCbox(true);
                    paperAdapter.notifyDataSetChanged();
                }

                //长按状态
                view.setTag(1);

                tvCancel.setVisibility(View.VISIBLE);
                tvChoiceAll.setVisibility(View.VISIBLE);

                return false;
            }
        });
    }

    /**
     * 获取试卷列表（测试用）
     *
     * @return list 试卷列表
     */
    private List<TestPaper> getTestPaperData() {
        for (int i = 1; i <= 3; i++) {
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
            questionList.add(test);
        }
        return questionList;
    }

    /**
     * 切换试卷与试题页面的显示
     *
     * @param a     区分标志（0：试卷，1：试题）
     * @param pInfo 点击项的父级数据
     */
    private void switchTestShow(int a, ICanGetKeyValue pInfo) {
        switch (a) {
            case 0://试卷
                lvTestPaper.setVisibility(View.VISIBLE);
                lvTestQuestion.setVisibility(View.GONE);
                rl02.setVisibility(View.GONE);

                break;
            case 1://试题
                lvTestPaper.setVisibility(View.GONE);
                lvTestQuestion.setVisibility(View.VISIBLE);
                rl02.setVisibility(View.VISIBLE);

                if (pInfo instanceof TestPaper) {
                    TestPaper paper = (TestPaper) pInfo;
                    if (paper != null) {
                        //状态
                        String type = paper.getType();
                        if ("0".equals(type)) {//未布置
                            tvBeginAnswer.setText("开始答题");
                            tvBeginAnswer.setBackgroundResource(R.drawable.selector_for_orange_corner_rectangle);
                            tvBeginAnswer.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            });
                        } else {
                            tvBeginAnswer.setText("查看报告");
                            tvBeginAnswer.setBackgroundResource(R.drawable.selector_for_blue_corner_rectangle01);
                            tvBeginAnswer.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    toSeeReport(paperFocus);
                                }
                            });
                        }
                    }
                }


                break;
        }
    }

    private void setTabText(int pos, List<String> list, TextView tv) {
        if (pos >= 0 && pos <= list.size()) {
            String value = list.get(pos);
            tv.setText(value);
        }
    }

    /**
     * 设置tab标题栏点击效果
     */
    private void setTabSettingFocusEffect(ImageView ivClicked,
                                          List<String> tabList, String tabName, TextView tvNotClicked01,
                                          ImageView ivNotClicked01, TextView tvNotClicked02,
                                          ImageView ivNotClicked02) {
        // 先赋值，用于ListView处理
        // this.tvClicked = tvClicked;
        this.ivArrow = ivClicked;
        this.conditionList = tabList;
        // 设置不可点击，防止误触 gc.setEnabled(false);

        // 刷新数据显示
        refreshData(tabList, 0);

        if (this.tabNameIsFocus.equals(tabName)) {// 当前是已被选中的状态，点击关闭弹框
            // 释放焦点
            tabNameIsFocus = "";

            if (window != null) {
                window.dismiss();
            }
        } else {// 点击弹框
            setSettingFocusEffect(ivArrow);

            // 还原之前点击过的状态

            // 设置焦点
            tabNameIsFocus = tabName;

            // if (window != null && window.isShowing()) {
            // window.dismiss();
            // }
            showSpinWindow();
        }
    }

    /**
     * 显示下拉弹框
     */
    @SuppressWarnings("deprecation")
    private void showSpinWindow() {
        // window.setDarkStyle(R.style.MyDarkStyle);
        // window.setDarkColor(Color.parseColor("#a0000000"));
        // window.resetDarkPosition();

        // 设置弹框宽度
        window.setWidth(llSetting.getWidth());
        // 设置弹框高度
        window.setHeight(CustomApplication.Screen_Height / 5);

        window.darkBelow(llSetting);
        window.showAsDropDown(llSetting, 0, 0);
        // 点击空白处弹框消失
        // window.setOutsideTouchable(true);
        // window.setFocusable(true);

        // 这句对于控制弹框消失的功能来说很重要，对背景不会有影响
        window.setBackgroundDrawable(new BitmapDrawable());

        window.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setNormalEffect(ivArrow);
            }
        });
    }

    /**
     * 刷新题目数据和布局
     *
     * @param list
     * @param selIndex
     */
    public void refreshData(List<String> list, int selIndex) {
        if (list != null && selIndex != -1) {
            if (mAdapter != null) {
                mAdapter.refreshData(list, selIndex);
            } else {
                mAdapter = new SpinnerImitateAdapter(getActivity(), list);
                lstv.setAdapter(mAdapter);
            }
        }
    }

    /**
     * 恢复tab标题栏设置按钮（非点击状态）
     *
     * @param iv 箭头
     */
    private void setNormalEffect(ImageView iv) {
        // 旋转动画
        if (arrowRotateAnim02 != null) {
            ivArrow.startAnimation(arrowRotateAnim02);
        } else {
            ivArrow.setAnimation(arrowRotateAnim02);
            ivArrow.startAnimation(arrowRotateAnim02);
        }
    }

    /**
     * 设置tab标题栏设置按钮（点击状态）
     *
     * @param iv 箭头
     */
    private void setSettingFocusEffect(ImageView iv) {
        // 旋转动画
        if (arrowRotateAnim != null) {
            iv.startAnimation(arrowRotateAnim);
        } else {
            iv.setAnimation(arrowRotateAnim);
            iv.startAnimation(arrowRotateAnim);
        }
    }

    /**
     * 初始化viewPager头标
     */
    private void initVPagerTab() {
        tvElect = (TextView) allFgView
                .findViewById(R.id.tv_choice_test_layout_fg_test);// 选择题
        tvPictureAnswer = (TextView) allFgView
                .findViewById(R.id.tv_take_pic_layout_fg_test);// 拍照答题

        tvElect.setOnClickListener(new VPagerTabOnClickListener(0));
        tvPictureAnswer.setOnClickListener(new VPagerTabOnClickListener(1));
    }

    /**
     * 初始化viewPager内容
     */
    @SuppressWarnings("deprecation")
    private void initVPagerContent() {
        vpagerTest = (CustomViewpager) allFgView
                .findViewById(R.id.vpager_content_layout_fg_test);

        // 关闭预加载，默认一次只加载一个Fragment
        vpagerTest.setOffscreenPageLimit(1);

        List<Fragment> fgList = new ArrayList<Fragment>();
        TesteLectFg electFg = new TesteLectFg(handler);// 选择题
        TestPictureAnswerFg pictureAnswerFg = new TestPictureAnswerFg();// 拍照答题
        // 添加到list中
        // fgList.add(detailFg);
        fgList.add(electFg);
        fgList.add(pictureAnswerFg);
        vpagerTest.setAdapter(new FragmentVPagerAdapter(getFragmentManager(),
                fgList));
        vpagerTest.setCurrentItem(0);
        vpagerTest.setOnPageChangeListener(new CustomOnPageChangeListener());
    }

    /**
     * 去查看报告
     *
     * @param paper 试卷信息
     */
    private void toSeeReport(TestPaper paper) {
        if (paper != null) {
            Intent intent = new Intent(getActivity(), AnswerTestPaperReportAty.class);
            intent.putExtra(ConstantsUtils.PAPER_INFO, (Serializable) paper);
            startActivity(intent);
        }
    }

    /**
     * 删除试卷
     */
    private void deletePaper() {
        List<TestPaper> papers = new ArrayList<TestPaper>();
        papers.addAll(paperList);

        for (int i = 0; i < paperList.size(); i++) {
            TestPaper paper = paperList.get(i);
            if (paper != null) {
                if (paper.isChoiced()) {
                    papers.remove(paper);
                }
            }
        }

        paperList.clear();
        paperList.addAll(papers);
    }

    @Override
    public void doOnAfterClickMenu(int menuID, int pos) {
        switch (menuID) {
            case 0://查看报告
                //收集数据
                if (paperList.size() > pos) {
                    TestPaper paper = paperList.get(pos);
                    toSeeReport(paper);
                }

                break;
        }
    }

    // //标题下划线滑动距离
    // private void InitWidth() {
    // imgv_line = (ImageView) allFgView.findViewById(R.id.iv);
    // bottomLineWidth = imgv_line.getLayoutParams().width;
    // llCursor = (LinearLayout)
    // allFgView.findViewById(R.id.ll_wrapper_vpager_cursor_layout_course_detail);//
    // 滑动条
    //
    // DisplayMetrics dm = new DisplayMetrics();
    //
    // getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
    // int screenW = dm.widthPixels;
    // offset = (int) ((screenW / 2.0 - bottomLineWidth) / 2);
    //
    // position_one = (int) (screenW / 420.0 * 42);
    //
    // }

    // public void setHasChoicedTestNum(int num) {
    // this.hasChoicedTestNum = num;
    // }

    /**
     * 页卡偏移事件监听
     */
    private class CustomOnPageChangeListener implements OnPageChangeListener {

        // int three= one*31/10;
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arriveIndex) {
            Resources res = getResources();

            Animation animation = null;
            switch (arriveIndex) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(position_one, 0, 0, 0);
                        tvPictureAnswer.setTextColor(res.getColor(R.color.black));
                        ivCursor02.setVisibility(View.INVISIBLE);
                    }
                    tvElect.setTextColor(Color.parseColor("#027DD3"));
                    ivCursor01.setVisibility(View.VISIBLE);

                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_one, 0,
                                0);
                        tvElect.setTextColor(res.getColor(R.color.black));
                        ivCursor01.setVisibility(View.INVISIBLE);
                    }
                    tvPictureAnswer.setTextColor(Color.parseColor("#027DD3"));

                    ivCursor02.setVisibility(View.VISIBLE);

                    break;
            }

            currIndex = arriveIndex;
            // animation.setFillAfter(true);// True:图片停在动画结束位置
            // animation.setDuration(300);
            // 滑动条滑动到指定位置
            // llCursor.startAnimation(animation);
        }
    }

    /**
     * ViewPager头标点击的事件监听
     */
    public class VPagerTabOnClickListener implements OnClickListener {
        private int index = 0;

        public VPagerTabOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            vpagerTest.setCurrentItem(index);
        }
    }


    /**
     * 控件监听
     *
     * @author chenhui 2019.04.15
     */
    private class Listeners implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_answer_statistics_layout_fg_test:// 答题统计
                    Intent intent = new Intent(getActivity(),
                            AnswerStatisticsActivity.class);
                    startActivity(intent);

                    break;
                case R.id.tv_omics_case_tab:// 切换教材、目录
                    // 跳转至选择教材目录界面
                    Intent intent02 = new Intent(getActivity(),
                            ChoiceTeachingMaterialAty.class);
                    intent02.putExtra(ConstantsUtils.CATALOG_ID, VariableUtils.catalogID);
                    // intent02.putExtra(ConstantsUtils.CATALOG_NAME,
                    // catalogNameCurr);
                    intent02.putExtra(ChoiceTeachingMaterialAty.CATALOG_POS, -1);
                    startActivityForResult(intent02, ConstantsUtils.REQUEST_CODE01);

                    //重新显示试卷列表
                    if (paperList.size() > 0) {
                        switchTestShow(0, paperList.get(0));
                    }

                    break;
                case R.id.ll_wrapper_back_upper_level_layout_fg_test://返回上一层级
                    switchTestShow(0, paperFocus);
                    tvDelete.setVisibility(View.VISIBLE);

                    break;

                case R.id.tv_delete_layout_fg_test://删除
                    boolean hasChoiced = false;
                    for (TestPaper paper : paperList) {
                        if (paper != null) {
                            if (paper.isChoiced()) {
                                hasChoiced = true;
                            }
                        }
                    }

                    if (hasChoiced) {//有选中删除的数据时
                        deletePaper();

                        if (paperAdapter != null) {
                            paperAdapter.setIfShowCbox(false);
                            paperAdapter.notifyDataSetChanged();
                        }

                        tvCancel.setVisibility(View.GONE);
                        tvChoiceAll.setVisibility(View.GONE);
                    } else {//没有要删除的数据时
                        Toast.makeText(getActivity(), "长按内容删除", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.tv_cancel_choice_layout_fg_test://取消（选择状态）
                    for (TestPaper paper : paperList) {
                        if (paper != null) {
                            paper.setChoiced(false);
                        }
                    }

                    if (paperAdapter != null) {
                        paperAdapter.setIfShowCbox(false);
                        paperAdapter.notifyDataSetChanged();
                    }

                    tvCancel.setVisibility(View.GONE);
                    tvChoiceAll.setVisibility(View.GONE);

                    break;
                case R.id.tv_choice_all_layout_fg_test://全选
                    if ("全选".equals(tvChoiceAll.getText().toString().trim())) {
                        for (TestPaper paper : paperList) {
                            if (paper != null) {
                                paper.setChoiced(true);
                            }
                        }
                        //刷新显示
                        if (paperAdapter != null) {
                            paperAdapter.setIfShowCbox(true);
                            paperAdapter.notifyDataSetChanged();
                        }

                        tvChoiceAll.setText("取消全选");
                    } else {
                        for (TestPaper paper : paperList) {
                            if (paper != null) {
                                paper.setChoiced(false);
                            }
                        }
                        //刷新显示
                        if (paperAdapter != null) {
                            paperAdapter.setIfShowCbox(true);
                            paperAdapter.notifyDataSetChanged();
                        }

                        tvChoiceAll.setText("全选");
                    }

                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantsUtils.REQUEST_CODE01) {// 从选择教材界面回传数据
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                return;
            }

            // 目录ID
            // catalogIDCurr = bundle.getString(ConstantsUtils.CATALOG_ID);
            // 目录名称
            // catalogNameCurr = bundle.getString(ConstantsUtils.CATALOG_NAME);
            if (!ValidateFormatUtils.isEmpty(VariableUtils.catalogName)) {
                tvTitleName.setText(VariableUtils.catalogName);
            }
        }
    }

    @Override
    protected void lazyLoad() {
        if (!ValidateFormatUtils.isEmpty(VariableUtils.catalogName)) {
            tvTitleName.setText(VariableUtils.catalogName);
        } else {
            tvTitleName.setText("1.沁园春-长沙");
        }

        if (!isPrepared || !isVisible || hasLoadOnce) {
            return;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (window.isShowing()) {
            window.dismiss();
        }
    }

}
