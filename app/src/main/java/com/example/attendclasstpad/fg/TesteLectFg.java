package com.example.attendclasstpad.fg;

import java.util.ArrayList;
import java.util.List;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.adapter.TestChoiceAdapter;
import com.example.attendclasstpad.model.Test;
import com.example.attendclasstpad.util.ConstantsUtils;
import com.example.attendclasstpad.util.ValidateFormatUtils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 测试-----选择题
 */
@SuppressLint("ValidFragment")
public class TesteLectFg extends BaseNotPreLoadFg {

    private boolean isPrepared;// 标志位，标志已经初始化完成
    private boolean hasLoadOnce;// 是否已被加载过一次，第二次就不再去请求数据了

    private List<Test> testList;// 题目
    private int serialNum;// 题目序号
    private int hasChoicedTestNum = 0;// 已选中的题目数

    private Handler handler0;// 调用者的handler

    private TestChoiceAdapter choiceAdapter;

    private View allFgView;// 总布局
    private ViewPager vpagerTest;
    private LinearLayout llChoiceAll;// 选择全部
    private CheckBox cboxChoiceAll;// 选择全部
    private EditText edtJumpNumber;// 跳转的题目序号
    private TextView tvEnsure;// 确定
    private TextView tvLast;// 上一题
    private TextView tvNext;// 下一题
    private TextView tvSerialNum;// 题目序号
    private TextView tvNum;// 题目数

    private Handler uiHandler = new Handler() {// 主线程
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case ConstantsUtils.ADD_CHOICE_TEST:// 选中题目
                    hasChoicedTestNum = hasChoicedTestNum + 1;

                    sendMsg(ConstantsUtils.ADD_CHOICE_TEST, testList);

                    break;

                case ConstantsUtils.REMOVE_CHOICE_TEST:// 移除选中题目
                    hasChoicedTestNum = hasChoicedTestNum - 1;

                    sendMsg(ConstantsUtils.REMOVE_CHOICE_TEST, testList);

                    break;
            }
        }
    };

    public TesteLectFg(Handler handler) {
        this.handler0 = handler;
    }

    private void sendMsg(int what, Object obj) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = obj;
        handler0.sendMessage(msg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == allFgView) {
            allFgView = View.inflate(getActivity(),
                    R.layout.layout_fg_test_elect, null);

            llChoiceAll = (LinearLayout) allFgView
                    .findViewById(R.id.ll_wrapper_choice_all_layout_fg_test_elect);

            cboxChoiceAll = (CheckBox) allFgView
                    .findViewById(R.id.cbox_choice_test_layout_fg_test_elect);

            edtJumpNumber = (EditText) allFgView
                    .findViewById(R.id.edt_jump_num_layout_fg_test_elect);

            tvEnsure = (TextView) allFgView
                    .findViewById(R.id.tv_ensure_layout_fg_test_elect);

            tvLast = (TextView) allFgView
                    .findViewById(R.id.tv_last_layout_fg_test_elect);

            tvNext = (TextView) allFgView
                    .findViewById(R.id.tv_next_layout_fg_test_elect);

            tvSerialNum = (TextView) allFgView
                    .findViewById(R.id.tv_serial_num_layout_fg_test_elect);

            tvNum = (TextView) allFgView
                    .findViewById(R.id.tv_num_layout_fg_test_elect);

            testList = new ArrayList<Test>();

            initVPagerContent();
            setTestVPagerAdapter(0);

            setListeners();
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
     * 设置监听
     */
    private void setListeners() {
        llChoiceAll.setOnClickListener(new OnClickListener() {// 选中全部
            @Override
            public void onClick(View v) {
                if (cboxChoiceAll.isChecked()) {// 此时是选中状态，需要恢复常态
                    cboxChoiceAll
                            .setBackgroundResource(R.drawable.cbox_n);

                    cboxChoiceAll.setChecked(false);

                    for (Test test : testList) {
                        test.setChoiced(false);
                    }

                    sendMsg(ConstantsUtils.REMOVE_CHOICE_ALL_TEST,
                            testList);
                    setTestVPagerAdapter(serialNum - 1);
                } else {
                    cboxChoiceAll
                            .setBackgroundResource(R.drawable.cbox_p);
                    cboxChoiceAll.setChecked(true);

                    sendMsg(ConstantsUtils.CHOICE_ALL_TEST, testList);

                    for (Test test : testList) {
                        test.setChoiced(true);
                    }

                    setTestVPagerAdapter(serialNum - 1);
                }
            }
        });

        tvEnsure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = edtJumpNumber.getText().toString().trim();
                if (ValidateFormatUtils.isEmpty(num)) {
                    Toast.makeText(getActivity(), "请先将需要跳转的题目序号输入到框内",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                int numInt = Integer.valueOf(num);
                if (numInt <= 0) {
                    Toast.makeText(getActivity(), "题目序号不能为0！",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (numInt > testList.size()) {
                    Toast.makeText(getActivity(), "请输入已有的题目序号！",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (numInt == serialNum) {
                    Toast.makeText(getActivity(), "已显示此题！", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                setTestVPagerAdapter(numInt - 1);
            }
        });

        tvLast.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setTestVPagerAdapter(serialNum - 1 - 1);
            }
        });

        tvNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setTestVPagerAdapter(serialNum);
            }
        });

    }

    /**
     * 初始化viewPager内容
     */
    @SuppressWarnings("deprecation")
    private void initVPagerContent() {
        vpagerTest = (ViewPager) allFgView
                .findViewById(R.id.vpager_content_layout_fg_test_elect);
        // 关闭预加载，默认一次只加载一个Fragment
        vpagerTest.setOffscreenPageLimit(2);

        // = new ArrayList<Fragment>();
        // TestChoiceFg electFg = new TestChoiceFg();// 选择题
        // TesteContentFg2 pictureAnswerFg = new TesteContentFg2();
        // TesteContentFg3 pictureAnswerFg3 = new TesteContentFg3();

        // 添加到list中
        // fgList.add(detailFg);
        // fgList.add(electFg);
        // fgList.add(pictureAnswerFg);
        // fgList.add(pictureAnswerFg3);

        Test test = new Test();
        test.setId("1");
        test.setContent("1：以下历史事件中，与关羽无关的是（）：\nＡ：单刀赴会　Ｂ：水淹七军　Ｃ：大意失荆州　Ｄ：七擒七纵\n2：“东风不与周郎便，铜雀春深锁二乔”。这首诗的作者生活的年代与诗中所描述的历史事件发生的年代大约相隔了（）：\nＡ：４００年　Ｂ：５００年　 Ｃ：６００年　Ｄ：８００年\n3、&apos;中秋节&apos;吃月饼最初的兴起是为了：\nA.纪念屈原                 B.推翻元朝统治       C.南宋人民纪念抗金将士     D.由长娥奔月的传说而来");

        Test test2 = new Test();
        test2.setId("2");
        test2.setContent("2：以下历史事件中，与关羽无关的是（）：\nＡ：单刀赴会　Ｂ：水淹七军　Ｃ：大意失荆州　Ｄ：七擒七纵\n2：“东风不与周郎便，铜雀春深锁二乔”。这首诗的作者生活的年代与诗中所描述的历史事件发生的年代大约相隔了（）：\nＡ：４００年　Ｂ：５００年　 Ｃ：６００年　Ｄ：８００年\n3、&apos;中秋节&apos;吃月饼最初的兴起是为了：\nA.纪念屈原                 B.推翻元朝统治       C.南宋人民纪念抗金将士     D.由长娥奔月的传说而来");

        Test test3 = new Test();
        test3.setId("3");
        test3.setContent("3：以下历史事件中，与关羽无关的是（）：\nＡ：单刀赴会　Ｂ：水淹七军　Ｃ：大意失荆州　Ｄ：七擒七纵\n2：“东风不与周郎便，铜雀春深锁二乔”。这首诗的作者生活的年代与诗中所描述的历史事件发生的年代大约相隔了（）：\nＡ：４００年　Ｂ：５００年　 Ｃ：６００年　Ｄ：８００年\n3、&apos;中秋节&apos;吃月饼最初的兴起是为了：\nA.纪念屈原                 B.推翻元朝统治       C.南宋人民纪念抗金将士     D.由长娥奔月的传说而来");

        Test test4 = new Test();
        test4.setId("4");
        test4.setContent("4：以下历史事件中，与关羽无关的是（）：\nＡ：单刀赴会　Ｂ：水淹七军　Ｃ：大意失荆州　Ｄ：七擒七纵\n2：“东风不与周郎便，铜雀春深锁二乔”。这首诗的作者生活的年代与诗中所描述的历史事件发生的年代大约相隔了（）：\nＡ：４００年　Ｂ：５００年　 Ｃ：６００年　Ｄ：８００年\n3、&apos;中秋节&apos;吃月饼最初的兴起是为了：\nA.纪念屈原                 B.推翻元朝统治       C.南宋人民纪念抗金将士     D.由长娥奔月的传说而来");

        Test test5 = new Test();
        test5.setId("5");
        test5.setContent("5：以下历史事件中，与关羽无关的是（）：\nＡ：单刀赴会　Ｂ：水淹七军　Ｃ：大意失荆州　Ｄ：七擒七纵\n2：“东风不与周郎便，铜雀春深锁二乔”。这首诗的作者生活的年代与诗中所描述的历史事件发生的年代大约相隔了（）：\nＡ：４００年　Ｂ：５００年　 Ｃ：６００年　Ｄ：８００年\n3、&apos;中秋节&apos;吃月饼最初的兴起是为了：\nA.纪念屈原                 B.推翻元朝统治       C.南宋人民纪念抗金将士     D.由长娥奔月的传说而来");

        Test test6 = new Test();
        test6.setId("6");
        test6.setContent("6：以下历史事件中，与关羽无关的是（）：\nＡ：单刀赴会　Ｂ：水淹七军　Ｃ：大意失荆州　Ｄ：七擒七纵\n2：“东风不与周郎便，铜雀春深锁二乔”。这首诗的作者生活的年代与诗中所描述的历史事件发生的年代大约相隔了（）：\nＡ：４００年　Ｂ：５００年　 Ｃ：６００年　Ｄ：８００年\n3、&apos;中秋节&apos;吃月饼最初的兴起是为了：\nA.纪念屈原                 B.推翻元朝统治       C.南宋人民纪念抗金将士     D.由长娥奔月的传说而来");

        testList.add(test);
        testList.add(test2);
        testList.add(test3);
        testList.add(test4);
        testList.add(test5);
        testList.add(test6);

        serialNum = 1;
        tvSerialNum.setText("1");
        tvLast.setTextColor(getResources().getColor(
                R.color.color_text_additional));
        tvLast.setClickable(false);

        tvNum.setText(String.valueOf(testList.size()));

        vpagerTest.setOnPageChangeListener(new CustomOnPageChangeListener());
    }

    /**
     * 设置viewPager适配器
     *
     * @param position 某个位置
     */
    private void setTestVPagerAdapter(int position) {
        // 设置适配器
        if (choiceAdapter == null) {
            choiceAdapter = new TestChoiceAdapter(getActivity(), uiHandler,
                    testList);
            vpagerTest.setAdapter(choiceAdapter);

            // 默认设置到中间的某个位置
            // if (rightInfoList.size() > 0) {
            // int pos = Integer.MAX_VALUE / 2
            // - (Integer.MAX_VALUE / 2 % rightInfoList.size());
            // 2147483647 / 2 = 1073741823 - (1073741823 % 5)
            // }
        } else {
            // 刷新布局
            choiceAdapter.notifyDataSetChanged();
        }

        vpagerTest.setCurrentItem(position);
    }

    private void setUnusableTextView(TextView tv) {
        tv.setTextColor(getResources().getColor(R.color.color_text_additional));
        tv.setClickable(false);
    }

    private void setUsableTextView(TextView tv) {
        tv.setTextColor(getResources().getColor(R.color.color_text_content));
        tv.setClickable(true);
    }

    /**
     * 页卡偏移事件监听
     */
    private class CustomOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case 0:// 滑动结束
                    break;
                case 1:// 正在滑动（初次加载是不走此路径的）
                    break;
                case 2:// 滑动完毕(onPageScrolled()方法前)
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arriveIndex) {
            int newPosition = arriveIndex % testList.size();
            // 赋值：题目序号
            serialNum = newPosition + 1;

            tvSerialNum.setText(String.valueOf(serialNum));

            if (serialNum == 1) {
                setUnusableTextView(tvLast);
            } else {
                setUsableTextView(tvLast);
            }

            if (serialNum == testList.size()) {
                setUnusableTextView(tvNext);
            } else {
                setUsableTextView(tvNext);
            }
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

    ;

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || hasLoadOnce) {
            return;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
