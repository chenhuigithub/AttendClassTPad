package com.example.attendclasstpad.aty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.callback.InterfacesCallback;
import com.example.attendclasstpad.fg.AttendClassDetailFg;
import com.example.attendclasstpad.fg.ClassesFg;
import com.example.attendclasstpad.fg.TestFg;
import com.example.attendclasstpad.callback.ActivityFgInterface.JumpCallback;
import com.example.attendclasstpad.model.Classes;
import com.example.attendclasstpad.util.ActivityUtils;
import com.example.attendclasstpad.util.ConstantsForPreferencesUtils;
import com.example.attendclasstpad.util.ConstantsUtils;
import com.example.attendclasstpad.util.PicFormatUtils;
import com.example.attendclasstpad.util.PreferencesUtils;
import com.example.attendclasstpad.util.ValidateFormatUtils;
import com.example.attendclasstpad.util.VariableUtils;
import com.example.attendclasstpad.util.ViewUtils;
import com.example.attendclasstpad.view.CustomRoundImageView;

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
    private Classes classes;//班级
    private boolean hasLogined = false;//是否已登录，默认为未登录

    private static ClassesFg mClassesFg = null;
    private InterfacesCallback.ICanKnowSth11 callbackForClass;//班级 回调

    private FragmentManager manager;// Fragment工具
    private ClassesFg cFg; // 班级
    private AttendClassDetailFg aFg;// 上课
    private TestFg tFg;// 测试

    private BroadcastReceiver receiver;// 广播
    private LocalBroadcastManager broadcastManager;// 广播接收
    private Handler uiHandler;// 主线程
    private PicFormatUtils pUtils;// 图片工具

    private LinearLayout llClasses;// 班级
    private LinearLayout llAttendClass;// 上课
    private LinearLayout llTest;// 测试
    private LinearLayout llRightTriangle01;// 班级按钮右三角
    private LinearLayout llRightTriangle02;// 上课按钮右三角
    private LinearLayout llRightTriangle03;// 测试按钮右三角
    private LinearLayout llUnlocked;// 全体解锁

    ViewUtils vUtils;

    private Button btnLogin;// 登录
    private CustomRoundImageView ivUserLogo;//用户头像
    private TextView tvName;// 登录名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_main);

        pUtils = new PicFormatUtils();
        res = getResources();
        vUtils = new ViewUtils(this);
        classes = new Classes();

        // 加入栈
        ActivityUtils utils = new ActivityUtils();
        utils.addActivity(this);


        llRightTriangle01 = (LinearLayout) findViewById(R.id.ll_right_triangle01_layout_activity_main);
        llRightTriangle02 = (LinearLayout) findViewById(R.id.ll_right_triangle02_layout_activity_main);
        llRightTriangle03 = (LinearLayout) findViewById(R.id.ll_right_triangle03_layout_activity_main);
        View in01 = (View) findViewById(R.id.in_layout_activity_main);
        //登录
        btnLogin = (Button) in01.findViewById(R.id.btn_login_layout_fg_menu_login);

        //用户头像
        ivUserLogo = (CustomRoundImageView) in01.findViewById(R.id.iv_user_logo_layout_fg_menu_login);
        //登录名
        tvName = (TextView) in01
                .findViewById(R.id.tv_user_name_layout_fg_menu_login);
        btnLogin.setOnClickListener(new Listeners());

        initHandler();
        initBottomBar();

        cFg = new ClassesFg();
        callbackForClass = (InterfacesCallback.ICanKnowSth11) cFg;

        mClassesFg = cFg;
//        aFg = new AttendClassDetailFg();
        tFg = new TestFg();

        initView();
        initFg();

        initBroadcastReceiver();
        manager = getSupportFragmentManager();// FragmentManager调用v4包内的

        boolean hasLogined = PreferencesUtils.acquireBooleanInfoFromPreferences(this, ConstantsUtils.HAS_LOGINED);
        if(hasLogined){
            String loginName = PreferencesUtils.acquireInfoFromPreferences(MainActivity.this, ConstantsForPreferencesUtils.LOGIN_NAME);
            // 设置头像
            String headPicUrl = PreferencesUtils.acquireInfoFromPreferences(MainActivity.this, ConstantsForPreferencesUtils.USER_HEAD_PIC_URL);

            setLogined(loginName,headPicUrl);
        }else {
            setLogout();
        }

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
        filter.addAction(ConstantsUtils.REFRESH_USER_INFO);//刷新用户信息

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
                } else if (ConstantsUtils.REFRESH_USER_INFO.equals(action)) {//刷新用户信息
                    Bundle bundle = intent.getExtras();
                    if (bundle == null) {
                        return;
                    }

                    Boolean hasLogined1 = bundle.getBoolean(ConstantsUtils.HAS_LOGINED);//是否登录的标志
                    if (hasLogined1 != null) {
                        hasLogined = hasLogined1;

                        if (hasLogined) {//已登录
                            String loginName = PreferencesUtils.acquireInfoFromPreferences(MainActivity.this, ConstantsForPreferencesUtils.LOGIN_NAME);
                            // 设置头像
                            String headPicUrl = PreferencesUtils.acquireInfoFromPreferences(MainActivity.this, ConstantsForPreferencesUtils.USER_HEAD_PIC_URL);
                            setLogined(loginName, headPicUrl);

                            String cName = bundle.getString(ConstantsUtils.CLASS_NAME);
                            if (!TextUtils.isEmpty(cName)) {
                                classes.setName(cName);
                            }

                            String cID = bundle.getString(ConstantsUtils.CLASS_NAME);
                            if (!TextUtils.isEmpty(cID)) {
                                classes.setId(cID);
                            }
                        } else {//未登录或退出登录
                            setLogout();

                            PreferencesUtils.saveInfoToPreferences(MainActivity.this, ConstantsUtils.HAS_LOGINED, false);
                        }
                    }


                    if (callbackForClass != null) {
                        callbackForClass.getInfo(classes);
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
//                    intent.putExtra(ConstantsUtils.CATALOG_ID, VariableUtils.catalogID);
//                    intent.putExtra(ConstantsUtils.CATALOG_NAME, VariableUtils.catalogName);
                    intent.putExtra(ChoiceTeachingMaterialAty.CATALOG_POS, 0);
//                  startActivityForResult(intent, ConstantsUtils.REQUEST_CODE01);
                    startActivity(intent);
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
     * 设置登录状态下的布局
     *
     * @param loginName
     */
    private void setLogined(String loginName, String headPicUrl) {
        if (!TextUtils.isEmpty(loginName)) {
            tvName.setText(loginName);
        }
        btnLogin.setText("退出登录");

        vUtils.showLoadingDialog("");
        pUtils.getBitmap(headPicUrl, uiHandler);
    }

    /**
     * 设置未登录状态
     */
    private void setLogout() {
        tvName.setText("老师您好");
        btnLogin.setText("点击登录");
        ivUserLogo.setImageDrawable(res.getDrawable(R.drawable.user_logo));

        classes.setId("");
        classes.setName("");
    }

    /**
     * 初始化线程
     */
    private void initHandler() {
        uiHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case PicFormatUtils.SIGN_FOR_BITMAP:
                        // 接收老师头像并显示
                        Object obj = msg.obj;
                        if (obj != null && obj instanceof Bitmap) {
                            Bitmap picBm = (Bitmap) obj;
                            Drawable draw = pUtils.getDrawable(picBm);
                            if (draw != null) {
                                ivUserLogo.setImageDrawable(draw);
                            } else {
                                ivUserLogo.setImageDrawable(getResources()
                                        .getDrawable(R.drawable.ic_launcher_background));
                            }
                        }
                        ivUserLogo.setClickable(true);

                        vUtils.dismissDialog();

                        break;
                }
            }
        };
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
            catalogID = bundle.getString(ConstantsUtils.CATALOG_ID);

            // 目录名称
            catalogName = bundle.getString(ConstantsUtils.CATALOG_NAME);

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

    /**
     * 监听
     *
     * @author chenhui
     */
    private class Listeners implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login_layout_fg_menu_login://登录/退出登录
                    Intent intent = new Intent(MainActivity.this,
                            LoginActivity.class);
                    startActivity(intent);

                    break;
            }
        }
    }
}
