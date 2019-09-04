package com.example.attendclasstpad.adapter;

import com.example.attendclasstpad.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * 颜色列表适配器
 *
 * @author chenhui 2018.07.23
 */
public class ColorAdapter extends BaseAdapter {
    private int[] res;// 颜色资源数组
    private LayoutInflater inflater;

    /**
     * 颜色资源数组
     *
     * @param res
     */
    public ColorAdapter(Context context, int[] res) {
        this.res = res;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return res.length;
    }

    @Override
    public Object getItem(int pos) {
        return res[pos];
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View resultView, ViewGroup parent) {
        if (resultView == null) {
            resultView = inflater.inflate(
                    R.layout.layout_adapter_item_for_color, null);
        }
        ImageView iv = (ImageView) resultView.findViewById(R.id.iv_layout_adapter_item_for_color);
        iv.setBackgroundResource(res[pos]);

        return resultView;
    }
}
