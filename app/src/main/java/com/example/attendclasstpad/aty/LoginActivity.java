package com.example.attendclasstpad.aty;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.model.AdditionalInfo;
import com.example.attendclasstpad.model.ServiceVersionInfo;
import com.example.attendclasstpad.model.User;
import com.example.attendclasstpad.util.APKVersionCodeUtils;
import com.example.attendclasstpad.util.ConstantsForPreferencesUtils;
import com.example.attendclasstpad.util.ConstantsForServerUtils;
import com.example.attendclasstpad.util.ConstantsUtils;
import com.example.attendclasstpad.util.DeviceUtils;
import com.example.attendclasstpad.util.NetworkUtils;
import com.example.attendclasstpad.util.PermissionUtils;
import com.example.attendclasstpad.util.PicFormatUtils;
import com.example.attendclasstpad.util.PreferencesUtils;
import com.example.attendclasstpad.util.ServerDataAnalyzeUtils;
import com.example.attendclasstpad.util.ServerRequestUtils;
import com.example.attendclasstpad.util.SoftKeyboardStateHelper;
import com.example.attendclasstpad.util.UpdateManager;
import com.example.attendclasstpad.util.UrlUtils;
import com.example.attendclasstpad.util.ValidateFormatUtils;
import com.example.attendclasstpad.util.ViewUtils;
import com.example.attendclasstpad.view.CustomDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author chenhui 2018.06.12
 */
public class LoginActivity extends Activity {
    /**
     * 修改登录信息后的展示状态
     */
    public static String MODIFY_LOGIN_INFO = "MODIFY_LOGIN_INFO";
    private String showType = "";// 展示状态
    private boolean isSwitchLogin = true;// 是否为从主界面过来切换账户的状态
    private String loginName;// 登录名
    private String password;// 登录密码


    private ServerRequestUtils requestUtils;
    private DeviceUtils deviceUtils;// 设备信息工具
    //    private ViewUtils vUtils;// 布局工具
    private Handler uiHandler;// 主线程handler
    private PicFormatUtils pUtils;// 图片工具
    // private NotificationUtils nUtils; // 通知栏工具
    private Dialog dialog;//加载框

    private CheckBox cboxRememberPsd;// 记住密码
    private EditText edtName;// 登录名
    private EditText edtPassword;// 登录密码
    private TextView tvLogin;// 登录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //动态获取权限
        PermissionUtils permissionAty = new PermissionUtils();
        permissionAty.checkPermission(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_aty_login);

        uiHandler = new Handler(getMainLooper());
        requestUtils = new ServerRequestUtils(this);
//        vUtils = new ViewUtils(this);
        deviceUtils = new DeviceUtils(this);
        pUtils = new PicFormatUtils();
        // 初始化通知栏的进度条
        // nUtils = new NotificationUtils(this);

        dealWithExtras();
        initHandler();

        tvLogin = (TextView) findViewById(R.id.tv_login_layout_aty_login);
        tvLogin.setFocusable(true);
        tvLogin.setFocusableInTouchMode(true);
        // 然后获取焦 点
        tvLogin.requestFocus();
        tvLogin.requestFocusFromTouch();
        tvLogin.setOnClickListener(new Listeners());

        // 记住密码
        RelativeLayout rlRememberPsd = (RelativeLayout) findViewById(R.id.rl_remember_layout_aty_login);
        cboxRememberPsd = (CheckBox) findViewById(R.id.cbox_remember_layout_aty_login);
        cboxRememberPsd.setChecked(true);

        // 用户登录名
        edtName = (EditText) findViewById(R.id.edt_user_name_layout_aty_login);
        String loginName = PreferencesUtils.acquireInfoFromPreferences(this,
                ConstantsForPreferencesUtils.LOGIN_NAME);
        edtName.setText(loginName);
        // 将光标设置到内容末尾
        edtName.setSelection(loginName.length());

        // 登录密码
        edtPassword = (EditText) findViewById(R.id.edt_password_layout_aty_login);
        String password = PreferencesUtils.acquireInfoFromPreferences(this,
                ConstantsForPreferencesUtils.LOGIN_PASSWORD);
        edtPassword.setText(password);
        // 将光标移至内容末尾
        edtPassword.setSelection(password.length());

        // 用户名/手机号/邮箱
        LinearLayout llName = (LinearLayout) findViewById(R.id.ll_user_name_layout_aty_login);

        // 软件盘相关
        SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(
                findViewById(R.id.ll_all_layout_aty_login));

        rlRememberPsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = cboxRememberPsd.isChecked();
                if (isChecked) {// 当前是已经选中的状态，需要取消选中
                    cboxRememberPsd.setChecked(false);
                } else {// 当前是没有选中的状态，需要选中
                    cboxRememberPsd.setChecked(true);
                }

                // 获取焦 点
                tvLogin.requestFocus();
                tvLogin.requestFocusFromTouch();
            }
        });

        tvLogin.setOnClickListener(new Listeners());
        llName.setOnClickListener(new Listeners());

        softKeyboardStateHelper
                .addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
                    @Override
                    public void onSoftKeyboardOpened(int keyboardHeightInPx) { // 键盘打开
                        Log.i("软键盘监听addSoftKeyboard", "键盘打开");
                    }

                    @Override
                    public void onSoftKeyboardClosed() { // 键盘关闭
                        // 登录按钮重新获取焦点
                        tvLogin.requestFocus();
                        tvLogin.requestFocusFromTouch();
                    }
                });

        // 更新版本
//        updateToServer();
    }

    /**
     * 处理接收过来的数据
     */
    private void dealWithExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        showType = bundle.getString(ConstantsUtils.TYPE);
        if (MODIFY_LOGIN_INFO.equals(showType)) {
            edtName.setEnabled(false);
        }

//        isSwitchLogin = bundle.getBoolean(ConstantsUtils.IS_SWITCH_LOGIN, false);
    }

    /**
     * 更新版本
     */
    private void updateToServer() {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        // 测试用
        RequestBody formBody = new FormBody.Builder().add("apptype", "3")
                .build();
        Request request = new Request.Builder()
                .url(UrlUtils.PREFIX_MOBILE + "getUpdate").post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException ex) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (TextUtils.isEmpty(ex.toString())) {
                            Toast.makeText(LoginActivity.this, "获取版本升级信息失败",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, ex.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                final String str = response.body().string();
                Log.i("user", str.toString());
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ServiceVersionInfo versionInfo = new Gson().fromJson(
                                str.toString(), ServiceVersionInfo.class);
                        if (versionInfo != null && versionInfo.data != null
                                && versionInfo.data.serverVersion != null
                                && versionInfo.data.updateurl != null) {

                            UpdateManager update = new UpdateManager(
                                    LoginActivity.this,
                                    versionInfo.data.serverVersion,
                                    versionInfo.data.lowVersion,
                                    versionInfo.data.updateurl,
                                    versionInfo.data.lastForce);
                            update.checkUpdate();

                        }
                    }
                });
            }
        });
    }


    /**
     * 登录
     */
    private void loginToServer() {
        User user = new User();

        // 登录名
        user.setLoginName(loginName);
        // 登录密码
        user.setLoginPassword(password);

        // 附加信息
        AdditionalInfo aInfo = new AdditionalInfo();
        aInfo.setDeviceType(ConstantsForServerUtils.DEVICE_TYPE_VALUE);

        // 设备名称
        aInfo.setDeviceName(deviceUtils.getName());

        // 设备系统版本
        aInfo.setDeviceVersion(deviceUtils.getAndroidVersion());

        // 设备IMEI,唯一标识
        aInfo.setDeviceSerialNumber(deviceUtils.getIMEI());

        // 设备型号
        aInfo.setDeviceModel(deviceUtils.getModel());

        int versionCode = APKVersionCodeUtils
                .getVersionCode(LoginActivity.this);

        // 应用当前版本号
        aInfo.setAppVersion(String.valueOf(versionCode));

        user.setAdditionalInfo(aInfo);

        String jsonStr = com.alibaba.fastjson.JSON.toJSONString(user);
        requestUtils.request("appLogin", jsonStr, "正在登录...",
                new ServerRequestUtils.OnServerRequestListener() {
                    @Override
                    public void onResponse(final String msg,
                                           final JSONObject data, String count) {
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "登录成功",
                                        Toast.LENGTH_SHORT).show();

                                PreferencesUtils.saveInfoToPreferences(LoginActivity.this,
                                        ConstantsUtils.HAS_LOGINED, true);

                                if (data != null) {
                                    dealWithData(data);
                                }
//                                vUtils.dismissDialog();
                                dismissDialog();
                            }
                        });
                    }

                    @Override
                    public void onFailure(final String msg) {
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!TextUtils.isEmpty(msg)) {
                                    Toast.makeText(LoginActivity.this, msg,
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "登录失败",
                                            Toast.LENGTH_SHORT).show();
                                }
//                                vUtils.dismissDialog();
                                dismissDialog();
                            }
                        });
                    }
                });
    }

    /**
     * 处理结果
     */
    private void dealWithData(JSONObject data) {
        // 发送广播，告知主界面需要刷新用户信息
        Intent intent2 = new Intent();
        intent2.setAction(ConstantsUtils.REFRESH_USER_INFO);// 刷新用户信息

        // 登录名
        intent2.putExtra(ConstantsUtils.LOGIN_NAME, loginName);

        //登录状态
        intent2.putExtra(ConstantsUtils.HAS_LOGINED, true);
        PreferencesUtils.saveInfoToPreferences(LoginActivity.this, ConstantsForPreferencesUtils.HAS_LOGINED, true);

        // 昵称
        String nickName = ServerDataAnalyzeUtils.getValue(data,
                ConstantsForServerUtils.DATANAME);
        if (!TextUtils.isEmpty(nickName)) {
            PreferencesUtils.saveInfoToPreferences(this,
                    ConstantsForPreferencesUtils.NICK_NAME, nickName);

            // 广播
            intent2.putExtra(ConstantsForPreferencesUtils.NICK_NAME, nickName);// 昵称
        } else {
            Log.i("LoginActivity", "用户名username获取失败");
        }

        // 头像
        String headPicUrl = ServerDataAnalyzeUtils.getValue(data,
                ConstantsForServerUtils.AVATAR);
        if (!TextUtils.isEmpty(headPicUrl)) {
            // 拼接地址，重新赋值，存入完整地址
            headPicUrl = UrlUtils.PREFIX + headPicUrl;
            PreferencesUtils.saveInfoToPreferences(this,
                    ConstantsForPreferencesUtils.USER_HEAD_PIC_URL, headPicUrl);

            pUtils.getBitmap(headPicUrl, uiHandler);

            // 广播
            intent2.putExtra(ConstantsUtils.USER_HEAD_PIC_URL,
                    headPicUrl);// 头像
        } else {
            Log.i("LoginActivity", "用户名头像获取失败");
        }

        // 用户身份唯一值Token
        String token = ServerDataAnalyzeUtils.getValue(data,
                ConstantsForServerUtils.TOKEN);
        if (!TextUtils.isEmpty(token)) {
            PreferencesUtils.saveInfoToPreferences(this,
                    ConstantsForPreferencesUtils.TOKEN, token);
        }

        // 记下登录名，登录名与昵称不一样，是从EditText中取的值
        PreferencesUtils.saveInfoToPreferences(LoginActivity.this,
                ConstantsForPreferencesUtils.LOGIN_NAME, loginName);

        // 若用户选择了记住密码
        if (cboxRememberPsd.isChecked()) {
            PreferencesUtils.saveInfoToPreferences(LoginActivity.this,
                    ConstantsForPreferencesUtils.LOGIN_PASSWORD, password);
        } else {
            PreferencesUtils.saveInfoToPreferences(LoginActivity.this,
                    ConstantsForPreferencesUtils.LOGIN_PASSWORD, "");
        }

        if (MODIFY_LOGIN_INFO.equals(showType)) {// 修改登录信息后
            // 作用：通知修改密码界面，方便处理跳转等功能
            Intent intent = new Intent();
            setResult(ConstantsUtils.INTENT_01, intent);

            // 发送广播
            LocalBroadcastManager.getInstance(LoginActivity.this)
                    .sendBroadcast(intent2);
        } else {
            Intent intent = new Intent(LoginActivity.this, ChoiceClassActivity.class);
            intent.putExtra(ConstantsUtils.HAS_LOGINED, true);
            startActivity(intent);

            finish();
        }
    }


    /**
     * 判断是否具备登录的条件
     *
     * @return
     */
    private boolean isCanLogin() {
        loginName = edtName.getText().toString().trim();
        if (TextUtils.isEmpty(loginName)) {
            Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        password = edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(loginName)) {
            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        // 检测网络是否连接
        if (!NetworkUtils.checkNetworkState(LoginActivity.this)) {
            Toast.makeText(LoginActivity.this,
                    R.string.check_network_connections, Toast.LENGTH_SHORT)
                    .show();

            return false;
        }
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
                        // 接收老师头像并显示
                        Object obj = msg.obj;
                        if (obj != null && obj instanceof Bitmap) {
                            Bitmap picBm = (Bitmap) obj;
                            PreferencesUtils.saveBase64ToPreferences(LoginActivity.this, ConstantsForPreferencesUtils.USER_HEAD_PIC_BASE64, picBm);
                        }

                        break;
                }
            }
        };
    }


    /**
     * 加载框
     *
     * @param tip 提示信息
     */
    private void showLoadingDialog(String tip) {
        // 设置dialog提示框
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        if (ValidateFormatUtils.isEmpty(tip)) {
            tip = "正在加载...";
        }
        builder.setMessage(tip);
        dialog = builder.createForLoading();
        dialog.show();
    }

    /**
     * 关闭加载框
     */
    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtils.PERMISSION_REQUEST:

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent();
            intent.setAction(ConstantsUtils.REFRESH_USER_INFO);// 刷新用户信息
            intent.setAction(ConstantsUtils.CLOSE_APP);// 关闭应用
            intent.putExtra(ConstantsUtils.HAS_LOGINED, false);
            LocalBroadcastManager.getInstance(LoginActivity.this).sendBroadcast(intent);

            finish();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 监听
     *
     * @author chenhui
     */
    private class Listeners implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.tv_login_layout_aty_login://登录
                    if (isCanLogin()) {
//                        vUtils.showLoadingDialog("");
                        showLoadingDialog("正在登录");

                        loginToServer();
                    }

                    break;
                case R.id.ll_user_name_layout_aty_login:// 用户名
                    // 然后获取焦 点
                    tvLogin.requestFocus();
                    tvLogin.requestFocusFromTouch();
                    break;
            }
        }
    }
}
