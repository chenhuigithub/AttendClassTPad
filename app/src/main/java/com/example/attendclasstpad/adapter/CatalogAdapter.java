package com.example.attendclasstpad.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


import com.example.attendclasstpad.R;
import com.example.attendclasstpad.model.Catalog;

import java.util.List;

/**
 * 课时目录适配器
 */
public class CatalogAdapter extends BaseListAdapter<Catalog> {
    private int currentPosition = 0;// 当前选中的位置,默认一个值
    private String dataID = "";// 数据ID

    /**
     * @param context
     * @param dataList         数据
     * @param selectedPosition 当前选中的位置
     */
    public CatalogAdapter(Context context, List<Catalog> dataList,
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
     * 返回当前位置
     *
     * @author zhaochenhui_2018.01.24
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * 设置当前数据ID
     *
     * @param dataID
     */
    public void setCurrentID(String dataID) {
        this.dataID = dataID;
    }

    /**
     * 选中状态
     *
     * @param tvName
     */
    @SuppressWarnings("deprecation")
    private void setFocused(TextView tvName) {
        tvName.setBackgroundColor(context.getResources().getColor(
                R.color.light_green03));
        tvName.setTextColor(context.getResources().getColor(R.color.clog));
    }

    /**
     * 正常状态
     *
     * @param tvName
     */
    @SuppressWarnings("deprecation")
    private void setNormal(TextView tvName) {
        tvName.setBackgroundResource(R.color.white);
        tvName.setTextColor(context.getResources().getColor(
                R.color.color_text_title));
    }

    /**
     * 返回当前数据ID
     *
     * @return
     */
    public String getCurrentID() {
        return dataID;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.layout_adapter_item_for_catalog;
    }

    @Override
    protected void doAssignValueForView(int position, View resultView,
                                        Catalog dataObj) {
        TextView tvUnit = (TextView) resultView
                .findViewById(R.id.tv_unit_layout_adapter_item_for_catalog);
        // 单元
        String unit = dataObj.getPname();
        if (!TextUtils.isEmpty(unit)) {
            if ((position + 1) % 3 == 1) {
                tvUnit.setText(unit);
            }
        } else {
            tvUnit.setText("");
        }

        // 名称
        TextView tvName = (TextView) resultView
                .findViewById(R.id.tv_chapter_layout_adapter_item_for_catalog);
        String name = dataObj.getName();
        if (!TextUtils.isEmpty(name)) {
            tvName.setText(name);
        } else {
            tvName.setText("");
        }

        // 先根据数据ID设置是否被选中的状态
        String id = dataObj.getId();
        if (!TextUtils.isEmpty(id)) {
            if (id.equals(getCurrentID())) {
                setFocused(tvName);
            } else {
                setNormal(tvName);
            }
        } else {// 根据位置设置是否被选中的状态
//            if (currentPosition != -1 && currentPosition == position) {
//                setFocused(tvName);
//            } else {
//                setNormal(tvName);
//            }

            if (position == 0) {
                setFocused(tvName);
            } else {
                setNormal(tvName);
            }
        }
    }
}
