package com.example.attendclasstpad.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.model.Classes;

/**
 * 班级名称列表适配器
 *
 * @author chenhui
 */
public class ClassNameAdapter extends BaseListAdapter<Classes> {

    public ClassNameAdapter(Context context, List<Classes> dataList) {
        super(context, dataList);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.layout_adapter_item_for_class_name;
    }

    @Override
    protected void doAssignValueForView(int position, View resultView,
                                        Classes dataObj) {

    }

}
