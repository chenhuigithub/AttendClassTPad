package com.example.attendclasstpad.fg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;


import com.example.attendclasstpad.R;
import com.example.attendclasstpad.adapter.TestExpandableListAdapter;
import com.example.attendclasstpad.callback.InterfacesCallback;
import com.example.attendclasstpad.callback.OnGroupExpandedListener;
import com.example.attendclasstpad.model.Test;
import com.example.attendclasstpad.model.TestData;
import com.example.attendclasstpad.util.ConstantsUtils;
import com.example.attendclasstpad.util.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * 题目预览
 */
@SuppressLint("ValidFragment")
public class PreviewTestFg extends Fragment implements InterfacesCallback.ICanKnowSth5, InterfacesCallback.ICanKnowSth10 {
    private List<TestData> testList;
    private ViewUtils vUtils;// 布局工具
    private InterfacesCallback.ICanKnowSth4 callback;
    private boolean isOnlyRead = true;//是否只读预览

    // map<父级,子集集合>
    private Map<TestData, List<Test>> dataset = new HashMap<TestData, List<Test>>();
    private Test testSelected;//当前被选中的试题

    private Handler uiHandler;
    private TestExpandableListAdapter testAdapter;

    private ImageView ivNoData;
    private ExpandableListView lvContent;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InterfacesCallback.ICanKnowSth4) {
            callback = (InterfacesCallback.ICanKnowSth4) context;
        }
    }

    public PreviewTestFg(List<TestData> testList) {
        this.testList = testList;
    }

    public PreviewTestFg(List<TestData> testList, boolean isOnlyRead) {
        this.testList = testList;
        this.isOnlyRead = isOnlyRead;
    }

    private void setTestList(List<TestData> testList) {
        this.testList = testList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View allFgView = inflater.inflate(R.layout.layout_v_preview_test, null);

        vUtils = new ViewUtils(getActivity());
        uiHandler = new Handler(getActivity().getMainLooper());
        testSelected = new Test();

        ivNoData = (ImageView) allFgView.findViewById(R.id.iv_no_data01_layout_v_preview_test);
        initTestView(allFgView);
        setTestAdapter(0, -1);

        return allFgView;
    }

    /**
     * 给题目设置适配器
     *
     * @param parentPos 父级位置
     * @param childPos  子级位置
     */
    private void setTestAdapter(int parentPos, int childPos) {
        // if (caAdapter == null)
        // {//暂时不加，因getChildrenCount()的groupPosition无法重置的问题
        testAdapter = new TestExpandableListAdapter(getActivity(), this, testList,
                dataset, isOnlyRead);
        lvContent.setAdapter(testAdapter);
        // } else {
        // testAdapter.notifyDataSetChanged();
        // }

        // 遍历所有group,将所有项设置成默认展开
        int groupCount = lvContent.getCount();
        for (int i = 0; i < groupCount; i++) {
            lvContent.expandGroup(i);
        }

        testAdapter.setPosition(parentPos, childPos);
        testAdapter.setOnGroupExpandedListener(new OnGroupExpandedListener() {
            @Override
            public void onGroupExpanded(int groupPosition) {
                // / expandOnlyOne(groupPosition);
            }
        });

//        vUtils.showLoadingDialog("");
        // 延时3秒再次刷新lstvPrev界面，暂时解决listView单项布局内的webView扩展不全的问题_2019.03.31_chenhui
//        uiHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                testAdapter.notifyDataSetChanged();
//                vUtils.dismissDialog();
//                ivNoData.setVisibility(View.GONE);
//            }
//        }, 3000);
    }


    /**
     * 初始化可伸缩目录ListView布局
     */
    private void initTestView(View allFgView) {
        lvContent = (ExpandableListView) allFgView.findViewById(R.id.lstv_layout_v_preview_test);
        lvContent.setGroupIndicator(null);
        lvContent.setDivider(null);
        // 遍历所有group,将所有项设置成默认展开
        int groupCount = lvContent.getCount();
        for (int i = 0; i < groupCount; i++) {
            lvContent.expandGroup(i);
        }

        // 设置分组项的点击监听事件
        lvContent.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                Log.d(TAG, "onGroupClick: groupPosition:" + groupPosition
                        + ", id:" + id);

                // 如果需要收起展开效果就去掉此句，但加上此句可刷新界面
//                setTestAdapter(groupPosition, -1);

                // 请务必返回 false，否则分组不会展开
                return false;
            }
        });

        // 设置子选项点击监听事件
//        lvContent.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v,
//                                        int groupPosition, final int childPosition, long id) {
//                TestData group = testList.get(groupPosition);
//                Test child = dataset.get(group).get(
//                        childPosition);
//                testSelected.setId(child.getId());
//
//                setTestAdapter(groupPosition, childPosition);
//
//                return true;
//            }
//        });
    }

    @Override
    public void doSth(ArrayList<TestData> list) {
        if (list != null && list.size() > 0) {
            setTestList(list);
            setTestAdapter(0, -1);
        }

        return;
    }

    @Override
    public void doSth(String key, HashMap<Object, Object> map) {
        Iterator i = map.entrySet().iterator();
        int gPos = 0;
        int cPos = 0;
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            gPos = (Integer) entry.getKey();
            cPos = (Integer) entry.getValue();
        }

        switch (key) {
            case ConstantsUtils.DELETE://删除
                break;
            case ConstantsUtils.MOVE_UP://上移
                break;
            case ConstantsUtils.MOVE_DOWN://下移
                break;
            case ConstantsUtils.SEE_ANALYSIS://查看解析
                break;
        }

        setTestAdapter(gPos, cPos);

        //从groupPos和childPos获得flatListPosition
//        int flatListPosition = ViewUtils.getFlatListPosition(lvContent, gPos, cPos);
        //lvContent.setSelection(flatListPosition);
//        lvContent.setSelectionFromTop(flatListPosition, 0);
    }
}
