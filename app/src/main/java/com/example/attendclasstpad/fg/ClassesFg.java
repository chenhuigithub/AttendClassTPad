package com.example.attendclasstpad.fg;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.adapter.ClassAdpter;
import com.example.attendclasstpad.adapter.StudentGdvAdapter;
import com.example.attendclasstpad.aty.ChoiceClassActivity;
import com.example.attendclasstpad.callback.InterfacesCallback;
import com.example.attendclasstpad.model.Classes;
import com.example.attendclasstpad.model.DataID01;
import com.example.attendclasstpad.model.Student;
import com.example.attendclasstpad.util.ConstantsForPreferencesUtils;
import com.example.attendclasstpad.util.ConstantsUtils;
import com.example.attendclasstpad.util.PreferencesUtils;
import com.example.attendclasstpad.util.ServerDataAnalyzeUtils;
import com.example.attendclasstpad.util.ServerRequestUtils;
import com.example.attendclasstpad.util.ValidateFormatUtils;
import com.example.attendclasstpad.util.ViewUtils;
import com.example.attendclasstpad.view.CustomListView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 班级
 */
public class ClassesFg extends BaseNotPreLoadFg implements InterfacesCallback.ICanKnowSth11 {
    private boolean isPrepared;// 标志位，标志已经初始化完成
    private boolean hasLoadOnce = false;// 是否已被加载过一次，第二次就不再去请求数据了

    private List<Student> onlineList = new ArrayList<Student>();
    private List<Student> offlineList = new ArrayList<Student>();
    private String classesIDCurr = "";// 班级ID
    private String classesNameCurr = "";// 班级名称

    private Classes classes;
    private boolean isOpenLock;

    private View allFgView;// 总布局
    private CustomListView gridView, gridView_out;
    private TextView tvClassName;//班级名称
    private StudentGdvAdapter onlineAdapter, offlineAdapter;
    private ViewUtils vUtils;
    private ServerRequestUtils sUtils;//网络请求助手
    private Handler uiHandler;//主线程

    // 切换教材、目录
    private TextView tvSwitchMaterial;
    //在线人数
    private TextView tvOnlineStudentNum;
    //离线人数
    private TextView tvOfflineStudentNum;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (allFgView == null) {
            allFgView = inflater.inflate(R.layout.layout_fg_classes, null);

            classes = new Classes();
            classesIDCurr = PreferencesUtils.acquireInfoFromPreferences(getActivity(), ConstantsForPreferencesUtils.CLASS_ID);
            vUtils = new ViewUtils(getActivity());
            sUtils = new ServerRequestUtils(getActivity());
            uiHandler = new Handler(getActivity().getMainLooper());


            initView(allFgView);
            initListeners();
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
     * 从服务器获取学生数据
     */
    private void requestStudentsFromServer() {
        DataID01 data = new DataID01();
        data.setCid(classesIDCurr);

        String jsonStr = com.alibaba.fastjson.JSON.toJSONString(data);

        sUtils.request("getClassUser", jsonStr, "", ServerRequestUtils.REQUEST_SHORT_TIME, new ServerRequestUtils.OnServerRequestListener() {
            @Override
            public void onFailure(String msg) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (vUtils != null) {
                            vUtils.dismissDialog();
                        }
                    }
                });
            }

            @Override
            public void onResponse(String msg, JSONObject data, String count) {
                //在线学生数据
                JSONArray onLineStudentsArr = ServerDataAnalyzeUtils.getDataAsJSONArray(data, "OnLine");
                if (onLineStudentsArr != null && onLineStudentsArr.length() > 0) {
                    onlineList = com.alibaba.fastjson.JSON.parseArray(onLineStudentsArr.toString(), Student.class);
                }

                //离线学生数据
                JSONArray offLineStudentsArr = ServerDataAnalyzeUtils.getDataAsJSONArray(data, "OffLine");
                if (offLineStudentsArr != null && offLineStudentsArr.length() > 0) {
                    offlineList = com.alibaba.fastjson.JSON.parseArray(onLineStudentsArr.toString(), Student.class);
                }


                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setOnLineStudentGridAdapter();
                        setOffLineStudentGridAdapter();

                        if (onlineList.size() > 0) {
                            tvOnlineStudentNum.setText("( " + String.valueOf(onlineList.size()) + "人 )");
                        }

                        if (offlineList.size() > 0) {
                            tvOfflineStudentNum.setText("( " + String.valueOf(offlineList.size()) + "人 )");
                        }


                        if (vUtils != null) {
                            vUtils.dismissDialog();
                        }
                    }
                });

            }
        });
    }

    /**
     * 在线学生数据适配器
     */
    private void setOnLineStudentGridAdapter() {
        if (onlineAdapter == null) {
            onlineAdapter = new StudentGdvAdapter(getActivity(), onlineList);
            gridView.setAdapter(onlineAdapter);
        } else {
            onlineAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 离线学生数据适配器
     */
    private void setOffLineStudentGridAdapter() {
        if (onlineAdapter == null) {
            offlineAdapter = new StudentGdvAdapter(getActivity(), offlineList);
            gridView_out.setAdapter(offlineAdapter);
        } else {
            onlineAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置监听
     */
    private void initListeners() {
        gridView.setOnItemLongClickListener(new OnItemLongClickListener() {// 长按事件
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Student student = onlineList.get(position);
                if (student != null) {
                    for (int i = 0; i < onlineList.size(); i++) {
                        if (!isOpenLock) {
//                        onlineList.get(i).setIcon(R.drawable.openlock);
//                        ClassAdpter.isopen = true;

                            student.setIslock(false);

                        } else {
//                        ClassAdpter.isopen = false;
//
//                        onlineList.get(i).setIcon(R.drawable.lock_ig);
                            student.setIslock(true);
                        }
                    }
                }

                setOnLineStudentGridAdapter();

                isOpenLock = !isOpenLock;
                return false;
            }
        });
    }

    public void open() {
        for (int i = 0; i < onlineList.size(); i++) {
            if (!isOpenLock) {
//                onlineList.get(i).setIcon(R.drawable.openlock);
//                ClassAdpter.isopen = true;
                onlineList.get(i).setIslock(true);

            } else {
                ClassAdpter.isopen = false;
//                onlineList.get(i).setIcon(R.drawable.lock_ig);
                onlineList.get(i).setIslock(false);
            }
        }
        onlineAdapter.notifyDataSetChanged();
        isOpenLock = !isOpenLock;
    }

    private void initView(View view) {
        //班级名称
        tvClassName = (TextView) view.findViewById(R.id.tv_name_layout_fg_classes);
        String name = PreferencesUtils.acquireInfoFromPreferences(getActivity(), ConstantsForPreferencesUtils.CLASS_NAME);
        if (!TextUtils.isEmpty(name)) {
            classesNameCurr = name;
            tvClassName.setText(classesNameCurr);
        }

        // 切换班级
        tvSwitchMaterial = (TextView) allFgView
                .findViewById(R.id.tv_switch_material_layout_fg_classes);
        tvSwitchMaterial.setOnClickListener(new Listeners());

        tvOnlineStudentNum = (TextView) allFgView
                .findViewById(R.id.tv_online_num_layout_fg_classes);
        tvOnlineStudentNum.setOnClickListener(new Listeners());

        tvOfflineStudentNum = (TextView) allFgView
                .findViewById(R.id.tv_offline_num_layout_fg_classes);
        tvOfflineStudentNum.setOnClickListener(new Listeners());

        gridView = (CustomListView) view.findViewById(R.id.gridview);
        gridView_out = (CustomListView) view.findViewById(R.id.gridview_out);
    }

    /**
     * 测试数据
     *
     * @return
     */
    private List<Student> getStudents() {
        List<Student> list = new ArrayList<Student>();
        list.clear();
        for (int i = 0; i < 25; i++) {
            Student bean = new Student();
            bean.setName("小喵" + (i + 1));
            list.add(bean);
        }
        return list;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void getInfo(Classes classes) {
        if (classes != null) {
            this.classes = classes;

            tvClassName.setText(classes.getName());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantsUtils.REQUEST_CODE02) {// 从选择教材界面回传数据
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                return;
            }

            // 班级ID
            classesIDCurr = bundle.getString(ConstantsUtils.CLASS_ID);
            // 班级名称
            classesNameCurr = bundle.getString(ConstantsUtils.CLASS_NAME);
            if (!ValidateFormatUtils.isEmpty(classesNameCurr)) {
                tvClassName.setText(classesNameCurr);
            }
        }
    }

    @Override
    protected void lazyLoad() {
//        if (!isPrepared || !isVisible || hasLoadOnce) {
//            return;
//        }

        vUtils.showLoadingDialog("");
        requestStudentsFromServer();
    }

    /**
     * 控件监听
     *
     * @author chenhui
     */
    private class Listeners implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_switch_material_layout_fg_classes://切换
                    // 跳转至选择班级界面
                    Intent intent02 = new Intent(getActivity(),
                            ChoiceClassActivity.class);
                    classesIDCurr = PreferencesUtils.acquireInfoFromPreferences(getActivity(), ConstantsForPreferencesUtils.CLASS_ID);
                    intent02.putExtra(ConstantsUtils.CLASS_ID, classesIDCurr);
                    startActivity(intent02);

                    break;

            }

        }
    }
}
