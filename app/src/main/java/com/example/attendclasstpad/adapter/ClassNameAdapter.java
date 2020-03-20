package com.example.attendclasstpad.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.attendclasstpad.R;
import com.example.attendclasstpad.model.Classes;

/**
 * 班级名称列表适配器
 *
 * @author chenhui
 */
public class ClassNameAdapter extends BaseListAdapter<Classes> {
    private Context context;
    private String IDCurr = "";

    public ClassNameAdapter(Context context, List<Classes> dataList) {
        super(context, dataList);
        this.context = context;
    }

    public void setCurrentID(String id) {
        if (!TextUtils.isEmpty(id)) {
            IDCurr = id;
        }
    }

    public String getCurrentID() {
        return IDCurr;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.layout_adapter_item_for_class_name;
    }

    @Override
    protected void doAssignValueForView(int position, View resultView,
                                        Classes dataObj) {
        LinearLayout llAll = (LinearLayout) resultView.findViewById(R.id.ll_all_layout_adapter_item_for_course);
        ImageView iv = (ImageView) resultView.findViewById(R.id.iv_logo_layout_adapter_item_for_class_name);
        Glide.with(context).load(dataObj.getIvUrl()).error(R.drawable.class_logo).into(iv);
        TextView tv = (TextView) resultView.findViewById(R.id.tv_name_layout_adapter_item_for_class_name);
        tv.setText(dataObj.getName());

        if (getCurrentID().equals(dataObj.getId())) {
//            llAll.setSelected(true);
            llAll.setBackgroundResource(R.color.light_blue07);
        } else {
//            llAll.setSelected(false);
            llAll.setBackgroundResource(R.color.white);
        }
    }

}
