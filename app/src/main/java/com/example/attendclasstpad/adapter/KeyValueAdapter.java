package com.example.attendclasstpad.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.model.KeyValue;

import java.util.List;

/**
 * key-value对应适配器
 *
 * @author zhaochenhui_2017.01.05
 */
public class KeyValueAdapter extends BaseListAdapter<KeyValue> {
    private int currentPosition = -1;// 当前选中的位置,默认一个值
    private String dataID = "";//当前选中数据的ID,默认为空

    public KeyValueAdapter(Context context, List<KeyValue> dataList) {
        super(context, dataList);
    }


    /**
     * @param context
     * @param dataList         数据
     * @param selectedPosition 当前选中的位置
     */
    public KeyValueAdapter(Context context, List<KeyValue> dataList,
                           int selectedPosition) {
        super(context, dataList);
        currentPosition = selectedPosition;
    }

    /**
     * 设置当前位置
     *
     * @param pos
     */
    public void setCurrentPosition(int pos) {
        this.currentPosition = pos;
    }

    /**
     * 设置当前数据ID
     *
     * @param dataID
     */
    public void setCurrentID(String dataID) {
        if (!TextUtils.isEmpty(dataID)) {
            this.dataID = dataID;
        }
    }

    public String getCurrentID() {
        return dataID;
    }


    /**
     * 返回当前位置
     *
     * @author zhaochenhui_2018.01.24
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.layout_v_single_line01;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void doAssignValueForView(int position, View resultView,
                                        KeyValue dataObj) {
        TextView tvName = (TextView) resultView
                .findViewById(R.id.tv_layout_v_single_line01);
        String name = dataObj.getName();
        String id = dataObj.getId();

        if (!TextUtils.isEmpty(name)) {
            tvName.setText(name);
        } else {
            tvName.setText("");
        }

        if ("".equals(getCurrentID())) {
            if (currentPosition == position) {
                tvName.setBackgroundResource(R.color.clog);
                tvName.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                tvName.setBackgroundResource(R.color.white);
                tvName.setTextColor(context.getResources().getColor(
                        R.color.color_text_title));
            }
        } else {
            if (getCurrentID().equals(id)) {
                tvName.setBackgroundResource(R.color.clog);
                tvName.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                tvName.setBackgroundResource(R.color.white);
                tvName.setTextColor(context.getResources().getColor(
                        R.color.color_text_title));
            }
        }

    }
}
