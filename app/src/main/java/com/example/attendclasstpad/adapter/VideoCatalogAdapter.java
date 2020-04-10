package com.example.attendclasstpad.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.model.VideoAudio;

import java.util.List;

/**
 * 视频目录适配器
 *
 * @author chenhui
 */
public class VideoCatalogAdapter extends BaseListAdapter<VideoAudio> {

    public VideoCatalogAdapter(Context context, List<VideoAudio> dataList) {
        super(context, dataList);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.layout_v_video_catalog;
    }

    @Override
    protected void doAssignValueForView(int position, View resultView,
                                        VideoAudio dataObj) {
        // 名称
        TextView tvName = (TextView) resultView
                .findViewById(R.id.tv_name_layout_v_video_catalog);
        tvName.setText(dataObj.getTitle());
    }

}
