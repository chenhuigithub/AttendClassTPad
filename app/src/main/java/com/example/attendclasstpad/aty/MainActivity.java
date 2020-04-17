package com.example.attendclasstpad.aty;

import android.app.Dialog;
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
import com.example.attendclasstpad.callback.OnListenerForPlayVideoSendOutInfo;
import com.example.attendclasstpad.fg.AttendClassDetailFg;
import com.example.attendclasstpad.fg.ClassesFg;
import com.example.attendclasstpad.fg.TestFg;
import com.example.attendclasstpad.callback.ActivityFgInterface.JumpCallback;
import com.example.attendclasstpad.model.Classes;
import com.example.attendclasstpad.model.VideoAndAudioInfoModel;
import com.example.attendclasstpad.model.VideoAudio;
import com.example.attendclasstpad.util.ActivityUtils;
import com.example.attendclasstpad.util.ConstantsForPreferencesUtils;
import com.example.attendclasstpad.util.ConstantsUtils;
import com.example.attendclasstpad.util.PicFormatUtils;
import com.example.attendclasstpad.util.PreferencesUtils;
import com.example.attendclasstpad.util.ValidateFormatUtils;
import com.example.attendclasstpad.util.VariableUtils;
import com.example.attendclasstpad.util.ViewUtils;
import com.example.attendclasstpad.view.CustomDialog;
import com.example.attendclasstpad.view.CustomRoundImageView;

import java.io.Serializable;
import java.util.List;

/**
 * 主界面
 *
 * @author zhaochenhui 2018.05.11
 */
public class MainActivity extends FragmentActivity implements JumpCallback, InterfacesCallback.ICanKnowSth2, OnListenerForPlayVideoSendOutInfo {
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

    private ViewUtils vUtils;

    private Button btnLogin;// 登录
    private CustomRoundImageView ivUserLogo;//用户头像
    private TextView tvName;// 登录名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null && this.clearFragmentsTag()) {
            //重建时清除 fragment的状态
            savedInstanceState.remove(ConstantsUtils.BUNDLE_FRAGMENTS_KEY);
        }

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
        aFg = new AttendClassDetailFg();
        tFg = new TestFg();

        initView();
        initFg();

        initBroadcastReceiver();
        manager = getSupportFragmentManager();// FragmentManager调用v4包内的

        checkIfHasLogined();

        initMenuListeners();

        llClasses.performClick();
    }

    /**
     * 检查用户是否登录
     */
    private void checkIfHasLogined() {
        hasLogined = PreferencesUtils.acquireBooleanInfoFromPreferences(this, ConstantsForPreferencesUtils.HAS_LOGINED);
        if (!hasLogined) {//若未登录先去登录
            toLoginPage();
        } else {
            setLogined();
        }
    }

    /**
     * 跳转至登录界面
     */
    private void toLoginPage() {
        Intent intent = new Intent(MainActivity.this,
                LoginActivity.class);
//        intent.putExtra(ConstantsUtils.IS_SWITCH_LOGIN, true);
        startActivity(intent);
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
        filter.addAction(ConstantsUtils.CLOSE_APP);//关闭应用

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    switch (bundle.getInt(ConstantsUtils.INTENT)) {
                        case ConstantsUtils.INTENT01://跳转至班级分页
//                            cFg = new ClassesFg();
//                            callbackForClass = (InterfacesCallback.ICanKnowSth11) cFg;
//                            showFragment(cFg);

                            break;
                        case ConstantsUtils.INTENT02://跳转至上课分页
//                            showFragment(aFg);

                            break;
                        case ConstantsUtils.INTENT03://跳转至测试分页
//                            showFragment(tFg);

                    }
                }

                if (ConstantsUtils.ACQUIRE_MATERIAL_INFO.equals(action)) {// 获取教材信息
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
                    if (bundle == null) {
                        return;
                    }

                    Boolean hasLogined1 = bundle.getBoolean(ConstantsUtils.HAS_LOGINED);//是否登录的标志
                    if (hasLogined1 != null) {
                        hasLogined = hasLogined1;

                        if (hasLogined) {//已登录
                            setLogined();
                        } else {//未登录或退出登录
                            setLogout();

                            PreferencesUtils.saveInfoToPreferences(MainActivity.this, ConstantsUtils.HAS_LOGINED, false);
                        }
                    }


                    if (callbackForClass != null) {
                        callbackForClass.getInfo(classes);
                    }
                } else if (ConstantsUtils.CLOSE_APP.equals(action)) {//关闭应用
                    finish();
                }


            }
        };
        broadcastManager.registerReceiver(receiver, filter);
    }

    /**
     * 设置登录后的状态
     */
    private void setLogined() {
        String loginName = PreferencesUtils.acquireInfoFromPreferences(MainActivity.this, ConstantsForPreferencesUtils.LOGIN_NAME);
        // 设置头像
        String headPicUrl = PreferencesUtils.acquireInfoFromPreferences(MainActivity.this, ConstantsForPreferencesUtils.USER_HEAD_PIC_URL);
        setLogined(loginName, headPicUrl);

        String cName = PreferencesUtils.acquireInfoFromPreferences(MainActivity.this, ConstantsForPreferencesUtils.CLASS_NAME);
        if (!TextUtils.isEmpty(cName)) {
            classes.setName(cName);
        }

        String cID = PreferencesUtils.acquireInfoFromPreferences(MainActivity.this, ConstantsForPreferencesUtils.CLASS_ID);
        if (!TextUtils.isEmpty(cID)) {
            classes.setId(cID);
        }
    }

    private void initView() {
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

                showFragment(aFg);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null && this.clearFragmentsTag()) {
            //销毁时不保存fragment的状态
            outState.remove(ConstantsUtils.BUNDLE_FRAGMENTS_KEY);
        }
    }

    protected boolean clearFragmentsTag() {
        return true;
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
                        vUtils.dismissDialog();

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

//            aFg = new AttendClassDetailFg(VariableUtils.catalogID,
//                    VariableUtils.catalogName);

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

    @Override
    public void getInfo(String str) {

    }

    @Override
    public void ICanGetVideoInfoCurrentPlay(VideoAndAudioInfoModel info) {

    }

    @Override
    public void doAfterClickBack() {

    }

    @Override
    public void doSwitchFullScreen(List<VideoAudio> list, VideoAudio info) {
        Intent intent = new Intent(MainActivity.this, PlayVideoActivity.class);
        intent.putExtra(ConstantsUtils.VIDEO_LIST, (Serializable) list);
        intent.putExtra(ConstantsUtils.VIDEO, (Serializable) info);
        startActivity(intent);
    }

    @Override
    public void doSwitchHalfScreen() {

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (vUtils != null) {
            vUtils.setCanShowDialog(true);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (vUtils != null) {
            vUtils.setCanShowDialog(true);
        }
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
                case R.id.btn_login_layout_fg_menu_login://退出登录
//                    Intent intent = new Intent(MainActivity.this,
//                            LoginActivity.class);
//                    intent.putExtra(ConstantsUtils.IS_SWITCH_LOGIN, true);
//                    startActivity(intent);


                    //重置登录状态
                    PreferencesUtils.saveInfoToPreferences(MainActivity.this, ConstantsForPreferencesUtils.HAS_LOGINED, false);

                    toLoginPage();

                    break;
            }
        }
    }
}
