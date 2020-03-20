package com.example.attendclasstpad.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.attendclasstpad.R;
import com.example.attendclasstpad.callback.InterfacesCallback;
import com.example.attendclasstpad.model.File01;
import com.example.attendclasstpad.model.Files;
import com.example.attendclasstpad.util.ConstantsUtils;

/**
 * 文件目录适配器
 *
 * @author zhaochenhui, 2018.05.21
 */
public class FilesListAdapter extends BaseListAdapter<File01> {
    private InterfacesCallback.ICanDoSth callback;

    public FilesListAdapter(Context context, List<File01> dataList) {
        super(context, dataList);
    }

    public FilesListAdapter(Context context, List<File01> dataList, InterfacesCallback.ICanDoSth callback) {
        super(context, dataList);
        this.callback = callback;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.layout_adapter_item_for_files;
    }

    @Override
    protected void doAssignValueForView(final int position, View resultView,
                                        final File01 dataObj) {
        RelativeLayout rlAll = (RelativeLayout) resultView
                .findViewById(R.id.rl_all_layout_adapter_item_for_files);
        rlAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.doSth(ConstantsUtils.AFTER_CLICK_ALL, position, dataObj.getDataID());
                }
            }
        });

        // 文件名称
        TextView tvName = (TextView) resultView
                .findViewById(R.id.tv_name_layout_adapter_item_for_files);
        tvName.setText(dataObj.getDataName());

        // 文件大小
        TextView tvSize = (TextView) resultView
                .findViewById(R.id.tv_size_layout_adapter_item_for_files);
        tvSize.setText(dataObj.getSize());
        tvSize.setVisibility(View.GONE);

        // 创建日期
        TextView tvTime = (TextView) resultView
                .findViewById(R.id.tv_time_layout_adapter_item_for_files);
        tvTime.setText(dataObj.getDataTime());


        // 文件标志（头像图片）
        ImageView ivLogo = (ImageView) resultView
                .findViewById(R.id.iv_logo_layout_adapter_item_for_files);
        Glide.with(context).load(dataObj.getLogoUrl()).error(R.drawable.file_unknown).into(ivLogo);
    }
}
