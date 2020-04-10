package com.example.attendclasstpad.aty;

import java.util.ArrayList;
import java.util.List;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.adapter.ClassNameAdapter;
import com.example.attendclasstpad.model.Classes;
import com.example.attendclasstpad.util.ConstantsForPreferencesUtils;
import com.example.attendclasstpad.util.ConstantsUtils;
import com.example.attendclasstpad.util.PreferencesUtils;
import com.example.attendclasstpad.util.ServerRequestUtils;
import com.example.attendclasstpad.util.ValidateFormatUtils;
import com.example.attendclasstpad.util.ViewUtils;
import com.example.attendclasstpad.view.CustomDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 选择班级
 *
 * @author chenhui 2018.06.12
 */
public class ChoiceClassActivity extends Activity {
    private List<Classes> classList;// 班级列表
    private String classesIDCurr = "";//当前选中的班级ID

    private ServerRequestUtils requestUtils;// 网络请求
    //    private ViewUtils vUtils;// 布局工具
    private Handler uiHandler;// 主线程handler
    private ClassNameAdapter classAdapter;// 班级名称列表适配器
    private Dialog dialog;//加载框

    private GridView gdvClass;
    private ImageView ivNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_aty_choice_class);

        // 测试数据，chenhui 2018.06.13
        classList = new ArrayList<Classes>();
        classesIDCurr = PreferencesUtils.acquireInfoFromPreferences(this, ConstantsForPreferencesUtils.CLASS_ID);

        // 初始化服务器请求操作
        requestUtils = new ServerRequestUtils(this);
//        vUtils = new ViewUtils(this);
        uiHandler = new Handler(getMainLooper());

        ivNoData = (ImageView) findViewById(R.id.iv_no_data_layout_aty_choice_class);
        ivNoData.setVisibility(View.GONE);
        ivNoData.setOnClickListener(new Listeners());

        dealWithExtras();

        gdvClass = (GridView) findViewById(R.id.gdv_layout_aty_choice_class);
        gdvClass.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Classes classes = classList.get(position);
                if (classes != null) {
                    classAdapter.setCurrentID(classes.getId());
                    PreferencesUtils.saveInfoToPreferences(ChoiceClassActivity.this, ConstantsForPreferencesUtils.CLASS_ID, classes.getId());
                    classAdapter.notifyDataSetChanged();
                }

                PreferencesUtils.saveInfoToPreferences(ChoiceClassActivity.this, ConstantsForPreferencesUtils.CLASS_ID_CHOICED, classList.get(position).getId());

                Intent intent = new Intent(ChoiceClassActivity.this,
                        MainActivity.class);
                intent.setAction(ConstantsUtils.REFRESH_USER_INFO);

                if (classList.size() > 0 && classList.get(position) != null) {
                    intent.putExtra(ConstantsUtils.CLASS_NAME, classList.get(position).getName());
                    PreferencesUtils.saveInfoToPreferences(ChoiceClassActivity.this, ConstantsForPreferencesUtils.CLASS_NAME, classList.get(position).getName());
                }

                if (classList.size() > 0 && classList.get(position) != null) {
                    intent.putExtra(ConstantsUtils.CLASS_ID, classList.get(position).getId());
                    PreferencesUtils.saveInfoToPreferences(ChoiceClassActivity.this, ConstantsForPreferencesUtils.CLASS_ID, classList.get(position).getId());
                }


                intent.putExtra(ConstantsUtils.HAS_LOGINED, true);
                //跳转至班级分页
                intent.putExtra(ConstantsUtils.INTENT, ConstantsUtils.INTENT01);

                // 发送广播
                LocalBroadcastManager.getInstance(ChoiceClassActivity.this)
                        .sendBroadcast(intent);

                finish();
            }
        });

        acquireClassList();
    }

    private void dealWithExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        String id = bundle.getString(ConstantsUtils.CLASS_ID);
        if (!TextUtils.isEmpty(id)) {
            classesIDCurr = id;
        }

    }

    /**
     * 获取班级列表
     */
    private void acquireClassList() {
        requestUtils.request("getClassList", "", "加载班级数据中", ServerRequestUtils.REQUEST_SHORT_TIME, new ServerRequestUtils.OnServerRequestListener2() {
            @Override
            public void onFailure(final String msg) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(msg)) {
                            Toast.makeText(ChoiceClassActivity.this, msg + "加载失败，请点击图片重试",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ChoiceClassActivity.this, "获取班级列表失败，请点击图片重试",
                                    Toast.LENGTH_SHORT).show();
                        }
//                        vUtils.dismissDialog();
                        dismissDialog();

                        ivNoData.setVisibility(View.VISIBLE);
                        gdvClass.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(String msg, JSONArray data, String count) {
                //暂无数据，用假数据代替，2019.11.20
//                data = new JSONArray();
//                try {
//                    JSONObject obj1 = new JSONObject();
//                    obj1.put("DataID", 1);
//                    obj1.put("DataName", "高一(1)班");
//
//                    JSONObject obj2 = new JSONObject();
//                    obj2.put("DataID", 2);
//                    obj2.put("DataName", "高一(2)班");
//
//                    data.put(obj1);
//                    data.put(obj2);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


                if (data != null) {
                    List<Classes> list = com.alibaba.fastjson.JSON.parseArray(data.toString(), Classes.class);
                    if (list != null) {
                        if (list.size() == 0) {
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ChoiceClassActivity.this, "暂无可选择的班级，可返回切换账号试试", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            if (classList.size() > 0) {
                                classList.clear();
                            }
                            classList.addAll(list);
                        }
                    }

                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ivNoData.setVisibility(View.GONE);
                            gdvClass.setVisibility(View.VISIBLE);

//                            vUtils.dismissDialog();
                            dismissDialog();

                            setGdvClassAdapter(0);
                        }
                    });

                    //把班级jsonArray存入首选项文件
                    PreferencesUtils.saveInfoToPreferences(ChoiceClassActivity.this, ConstantsForPreferencesUtils.CLASS_LIST_JSONARR, data.toString());
                } else {

                }
            }
        });
    }

    /**
     * 班级列表适配器
     *
     * @param pos
     */
    private void setGdvClassAdapter(int pos) {
//        Classes classes = new Classes();
//        classList.add(classes);
//        classList.add(classes);
//        classList.add(classes);
//        classList.add(classes);
//        classList.add(classes);
//        classList.add(classes);
//        classList.add(classes);
//        classList.add(classes);
//        classList.add(classes);

        if (classAdapter == null) {
            classAdapter = new ClassNameAdapter(this, classList);
            classAdapter.setCurrentID(classesIDCurr);
            classAdapter.notifyDataSetChanged();
            gdvClass.setAdapter(classAdapter);
        } else {
            classAdapter.notifyDataSetChanged();
        }
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent();
            intent.setAction(ConstantsUtils.REFRESH_USER_INFO);// 刷新用户信息
            intent.putExtra(ConstantsUtils.HAS_LOGINED, false);
            LocalBroadcastManager.getInstance(ChoiceClassActivity.this).sendBroadcast(intent);


            Intent intent2 = new Intent(ChoiceClassActivity.this, LoginActivity.class);
            startActivity(intent2);

            finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class Listeners implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_no_data_layout_aty_choice_class://刷新班级列表
                    if (classList != null && classList.size() > 0) {
                        classList.clear();
                    }

//                    vUtils.showLoadingDialog("");
                    showLoadingDialog("正在跳转");
                    acquireClassList();

                    break;
            }
        }
    }
}
