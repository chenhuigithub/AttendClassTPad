package com.example.attendclasstpad.aty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.fg.AttendClassDetailFg;
import com.example.attendclasstpad.fg.ClassesFg;
import com.example.attendclasstpad.fg.TestFg;
import com.example.attendclasstpad.callback.ActivityFgInterface.JumpCallback;
import com.example.attendclasstpad.util.ActivityUtils;
import com.example.attendclasstpad.util.ConstantsUtils;
import com.example.attendclasstpad.util.PreferencesUtils;
import com.example.attendclasstpad.util.ValidateFormatUtils;
import com.example.attendclasstpad.util.VariableUtils;

/**
 * 主界面
 *
 * @author zhaochenhui 2018.05.11
 */
public class MainActivity extends FragmentActivity implements JumpCallback {
    private long exitTime = 0;
    private Resources res;

    private String materialName;// 教材名
    private String catalogID = "";// 目录ID
    private String catalogName = "";// 目录名

    String periodIDCurr = "";
    String subjectIDCurr = "";
    String editionIDCurr = "";
    String moduleIDCurr = "";

    private static ClassesFg mClassesFg = null;

    private FragmentManager manager;// Fragment工具
    private ClassesFg cFg; // 班级
    private AttendClassDetailFg aFg;// 上课
    private TestFg tFg;// 测试

    private BroadcastReceiver receiver;// 广播
    private LocalBroadcastManager broadcastManager;// 广播接收

    private LinearLayout llClasses;// 班级
    private LinearLayout llAttendClass;// 上课
    private LinearLayout llTest;// 测试
    private LinearLayout llRightTriangle01;// 班级按钮右三角
    private LinearLayout llRightTriangle02;// 上课按钮右三角
    private LinearLayout llRightTriangle03;// 测试按钮右三角
    private LinearLayout llUnlocked;// 全体解锁

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_main);
        llRightTriangle01 = (LinearLayout) findViewById(R.id.ll_right_triangle01_layout_activity_main);
        llRightTriangle02 = (LinearLayout) findViewById(R.id.ll_right_triangle02_layout_activity_main);
        llRightTriangle03 = (LinearLayout) findViewById(R.id.ll_right_triangle03_layout_activity_main);
        View in01 = (View) findViewById(R.id.in_layout_activity_main);
        // 登录
        Button btnLogin = (Button) in01
                .findViewById(R.id.btn_login_layout_fg_menu_login);
        btnLogin.setOnClickListener(new Listeners());

        // 加入栈
        ActivityUtils utils = new ActivityUtils();
        utils.addActivity(this);

        res = getResources();

        initBottomBar();

        cFg = new ClassesFg();
        mClassesFg = cFg;

//        aFg = new AttendClassDetailFg();
        tFg = new TestFg();

        initView();
        initFg();

        initBroadcastReceiver();
        manager = getSupportFragmentManager();// FragmentManager调用v4包内的

        initMenuListeners();

        llClasses.performClick();
    }

    /**
     * 监听
     */
    private void initBroadcastReceiver() {
        // 注册广播接收
        broadcastManager = LocalBroadcastManager.getInstance(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantsUtils.ACQUIRE_MATERIAL_INFO);// 获取教材信息

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (ConstantsUtils.ACQUIRE_MATERIAL_INFO.equals(action)) {// 获取教材信息
                    Bundle bundle = intent.getExtras();
                    if (bundle == null) {
                        return;
                    }

                    // 学段ID
                    String periodID = bundle
                            .getString(ConstantsUtils.PERIOD_ID);
                    if (!ValidateFormatUtils.isEmpty(periodID)) {
                        MainActivity.this.periodIDCurr = periodID;
                    }

                    // 学科ID
                    String subjectID = bundle
                            .getString(ConstantsUtils.SUBJECT_ID);
                    if (!ValidateFormatUtils.isEmpty(subjectID)) {
                        MainActivity.this.subjectIDCurr = subjectID;
                    }
                }

            }
        };
        broadcastManager.registerReceiver(receiver, filter);

    }

    private void initView() {
        // TODO Auto-generated method stub
        llUnlocked = (LinearLayout) findViewById(R.id.ll_unlocked_layout_activity_main);
        llUnlocked.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mClassesFg.open();
            }
        });
    }

    private void initFg() {
        manager = getSupportFragmentManager();// FragmentManager调用v4包内的

        showFragment(cFg);
    }

    private void initMenuListeners() {
        llClasses.setOnClickListener(new OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                showLeftMenuColor(res.getColor(R.color.blue6),
                        res.getColor(R.color.blue5),
                        res.getColor(R.color.blue5));

                showRightTraingle(View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                // 显示解锁按钮
                llUnlocked.setVisibility(View.VISIBLE);

                showFragment(cFg);
            }
        });
        llAttendClass.setOnClickListener(new OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                showLeftMenuColor(res.getColor(R.color.blue5),
                        res.getColor(R.color.blue6),
                        res.getColor(R.color.blue5));

                showRightTraingle(View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                // 隐藏掉解锁按钮
                llUnlocked.setVisibility(View.INVISIBLE);


                boolean hasChoicedMaterial = PreferencesUtils.acquireBooleanInfoFromPreferences(MainActivity.this, ConstantsUtils.HAS_CHOICED_MATERIAL);
                if (hasChoicedMaterial) {
                    aFg = new AttendClassDetailFg(VariableUtils.catalogID,
                            VariableUtils.catalogName);
                    showFragment(aFg);
                } else {
                    // 跳转至选择教材目录界面
                    Intent intent = new Intent(MainActivity.this,
                            ChoiceTeachingMaterialAty.class);
                    intent.putExtra(ConstantsUtils.CATALOG_ID, catalogID);
                    intent.putExtra(ConstantsUtils.CATALOG_NAME, catalogName);
                    intent.putExtra(ChoiceTeachingMaterialAty.CATALOG_POS, 0);
                    startActivityForResult(intent, ConstantsUtils.REQUEST_CODE01);
                }
            }
        });
        llTest.setOnClickListener(new OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                showLeftMenuColor(res.getColor(R.color.blue5),
                        res.getColor(R.color.blue5),
                        res.getColor(R.color.blue6));

                showRightTraingle(View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                // 隐藏掉解锁按钮
                llUnlocked.setVisibility(View.INVISIBLE);

                showFragment(tFg);
            }
        });
    }

    /**
     * 显示左侧按钮背景
     */
    private void showLeftMenuColor(int c01, int c02, int c03) {
        llClasses.setBackgroundColor(c01);
        llAttendClass.setBackgroundColor(c02);
        llTest.setBackgroundColor(c03);
    }

    /**
     * 显示左侧按钮的右三角标志
     *
     * @param vi01
     * @param vi02
     * @param vi03
     */
    private void showRightTraingle(int vi01, int vi02, int vi03) {
        llRightTriangle01.setVisibility(vi01);
        llRightTriangle02.setVisibility(vi02);
        llRightTriangle03.setVisibility(vi03);
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
            transaction.replace(R.id.fl_right_content_layout_activity_main, fg)
                    .commit();// 替换为名称为A的fragment并显示它
        }
    }

    /**
     * 初始化左侧菜单栏
     */
    private void initBottomBar() {
        // 班级
        llClasses = (LinearLayout) findViewById(R.id.ll_wrapper_classes_layout_activity_main);
        // 上课
        llAttendClass = (LinearLayout) findViewById(R.id.ll_wrapper_attend_class_layout_activity_main);
        // 测试
        llTest = (LinearLayout) findViewById(R.id.ll_wrapper_test_layout_activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void jumpTo(Fragment fg) {
        showFragment(fg);
    }

    /**
     * 临时方法，绑定layout_adapter_item_for_course.xml内课程列表item按钮事件
     *
     * @author zhaochenhui, 2018.05.18
     */
    public void onClick(View view) {
        showFragment(new AttendClassDetailFg());
    }

    /**
     * 监听
     *
     * @author chenhui
     */
    private class Listeners implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login_layout_fg_menu_login:// 退出登录
                    Intent intent = new Intent(MainActivity.this,
                            LoginActivity.class);
                    startActivity(intent);

                    finish();

                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsUtils.REQUEST_CODE01) {// 从选择教材界面回传数据
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                return;
            }

            // 教材
            materialName = bundle.getString(ConstantsUtils.CATALOG_NAME);

            // 目录ID
            // catalogID = bundle.getString(ConstantsUtils.CATALOG_ID);

            // 目录名称
            // catalogName = bundle.getString(ConstantsUtils.CATALOG_NAME);

            aFg = new AttendClassDetailFg(VariableUtils.catalogID,
                    VariableUtils.catalogName);
            showFragment(aFg);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
