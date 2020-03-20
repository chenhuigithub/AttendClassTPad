package com.example.attendclasstpad.fg;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.adapter.ClassAdpter;
import com.example.attendclasstpad.aty.ChoiceClassActivity;
import com.example.attendclasstpad.aty.ChoiceTeachingMaterialAty;
import com.example.attendclasstpad.callback.InterfacesCallback;
import com.example.attendclasstpad.model.ClassBean;
import com.example.attendclasstpad.model.Classes;
import com.example.attendclasstpad.util.ConstantsForPreferencesUtils;
import com.example.attendclasstpad.util.ConstantsUtils;
import com.example.attendclasstpad.util.PreferencesUtils;
import com.example.attendclasstpad.util.ValidateFormatUtils;
import com.example.attendclasstpad.view.CustomListView;

/**
 * 班级
 */
public class ClassesFg extends BaseNotPreLoadFg implements InterfacesCallback.ICanKnowSth11 {
    private boolean isPrepared;// 标志位，标志已经初始化完成
    private boolean hasLoadOnce = false;// 是否已被加载过一次，第二次就不再去请求数据了

    private List<ClassBean> onlineList = new ArrayList<ClassBean>();
    private List<ClassBean> offlineList = new ArrayList<ClassBean>();
    private String classesIDCurr = "";// 班级ID
    private String classesNameCurr = "";// 班级名称

    private Classes classes;
    private boolean isOpenLock;

    private View allFgView;// 总布局
    private CustomListView gridView, gridView_out;
    private TextView tvClassName;//班级名称
    private ClassAdpter onlineAdapter, offlineAdapter;
    // 切换教材、目录
    private TextView tvSwitchMaterial;


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

            initView(allFgView);
            initData();
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

    private void initData() {
        onlineAdapter = new ClassAdpter(getActivity(), onlineList, "在线");
        gridView.setAdapter(onlineAdapter);
        offlineAdapter = new ClassAdpter(getActivity(), offlineList, "离线");
        gridView_out.setAdapter(offlineAdapter);
        gridView.setOnItemLongClickListener(new OnItemLongClickListener() {// 长按事件
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                for (int i = 0; i < onlineList.size(); i++) {
                    if (!isOpenLock) {
                        onlineList.get(i).setIcon(R.drawable.openlock);
                        ClassAdpter.isopen = true;
                    } else {
                        ClassAdpter.isopen = false;
                        onlineList.get(i).setIcon(R.drawable.lock_ig);
                    }
                }
                onlineAdapter.notifyDataSetChanged();
                isOpenLock = !isOpenLock;
                return false;
            }
        });
    }

    public void open() {
        for (int i = 0; i < onlineList.size(); i++) {
            if (!isOpenLock) {
                onlineList.get(i).setIcon(R.drawable.openlock);
                ClassAdpter.isopen = true;
            } else {
                ClassAdpter.isopen = false;
                onlineList.get(i).setIcon(R.drawable.lock_ig);
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

        gridView = (CustomListView) view.findViewById(R.id.gridview);
        gridView_out = (CustomListView) view.findViewById(R.id.gridview_out);
    }

    @Override
    public void onResume() {
        super.onResume();
        onlineList.clear();
        offlineList.clear();
        for (int i = 0; i < 25; i++) {
            ClassBean bean = new ClassBean();
            bean.setResource(R.drawable.on_line_circle);
            bean.setIconName("小喵" + (i + 1));
            bean.setIcon(R.drawable.lock_ig);
            onlineList.add(bean);
            if (i < 3) {
                offlineList.add(bean);
            }
        }
        onlineAdapter.notifyDataSetChanged();
        offlineAdapter.notifyDataSetChanged();
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
