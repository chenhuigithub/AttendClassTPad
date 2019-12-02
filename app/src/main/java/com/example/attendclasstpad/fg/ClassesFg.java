package com.example.attendclasstpad.fg;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.adapter.ClassAdpter;
import com.example.attendclasstpad.callback.InterfacesCallback;
import com.example.attendclasstpad.model.ClassBean;
import com.example.attendclasstpad.model.Classes;
import com.example.attendclasstpad.view.CustomListView;

/**
 * 班级
 */
public class ClassesFg extends Fragment implements InterfacesCallback.ICanKnowSth11 {
    private View view;// 总布局
    private List<ClassBean> onlineList = new ArrayList<ClassBean>();
    private List<ClassBean> offlineList = new ArrayList<ClassBean>();

    Classes classes;

    private CustomListView gridView, gridView_out;
    private TextView tvClassName;//班级名称
    private ClassAdpter onlineAdapter, offlineAdapter;
    private boolean isOpenLock;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_fg_classes, null);

        classes = new Classes();

        initView();
        initData();
        return view;
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

    private void initView() {
        tvClassName = (TextView) view.findViewById(R.id.tv_name_layout_fg_classes);

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
}
