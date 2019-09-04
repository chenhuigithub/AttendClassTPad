package com.example.attendclasstpad.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.model.FileContent;

/**
 * 画廊效果适配器:文件内容
 *
 * @author zhaochenhui, 2018.05.23
 */
public class GalleryAdapter extends BaseListAdapter<FileContent> {
    private int currentPos;// 当前选中的位置(全都未选中：-1，全部选中：-2)
    private Context context;

    public GalleryAdapter(Context context, List<FileContent> dataList) {
        super(context, dataList);
        this.context = context;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.layout_adapter_item_for_file_content;
    }

    /**
     * 设置当前位置
     */
    public void setCurrentPos(int pos) {
        this.currentPos = pos;
    }

    /**
     * 获取当前位置
     */
    public int getCurrentPos() {
        return this.currentPos;
    }

    @Override
    protected void doAssignValueForView(int position, View resultView,
                                        FileContent dataObj) {

        LinearLayout ll01 = (LinearLayout) resultView
                .findViewById(R.id.ll_01_layout_adapter_item_for_file_content);

        TextView tv = (TextView) resultView
                .findViewById(R.id.tv_layout_adapter_item_for_file_content);
        ImageView iv = (ImageView) resultView
                .findViewById(R.id.iv_layout_adapter_item_for_file_content);

        tv.setText(dataObj.getPageNumber());
        iv.setBackgroundResource(dataObj.getIvRes());
        tv.setTextColor(context.getResources().getColor(
                R.color.color_text_content));

        if ((position == this.currentPos) || (-2 == this.currentPos)) {// 加载的位置正好为当前选中位置或者全部已选中
            ll01.setBackgroundResource(R.drawable.selector_for_green_stroke_rectangle);
        } else {// 包括全部未选中（currentPos=-1）的情况
        }

    }

}
