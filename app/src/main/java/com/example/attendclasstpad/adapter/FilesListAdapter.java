package com.example.attendclasstpad.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.model.Files;

/**
 * 文件目录适配器
 * 
 * @author zhaochenhui,2018.05.21
 * 
 */
public class FilesListAdapter extends BaseListAdapter<Files> {

	public FilesListAdapter(Context context, List<Files> dataList) {
		super(context, dataList);
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.layout_adapter_item_for_files;
	}

	@Override
	protected void doAssignValueForView(int position, View resultView,
			Files dataObj) {
		// 文件名称
		TextView tvName = (TextView) resultView
				.findViewById(R.id.tv_name_layout_adapter_item_for_files);
		tvName.setText(dataObj.getName());

		// 文件大小
		TextView tvSize = (TextView) resultView
				.findViewById(R.id.tv_size_layout_adapter_item_for_files);
		tvSize.setText(dataObj.getSize());

		// 文件标志（头像图片）
		ImageView ivLogo = (ImageView) resultView
				.findViewById(R.id.iv_logo_layout_adapter_item_for_files);
		ivLogo.setBackgroundResource(dataObj.getLogoRes());
	}
}
