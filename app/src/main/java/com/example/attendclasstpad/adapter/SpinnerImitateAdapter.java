package com.example.attendclasstpad.adapter;

import java.util.List;

import com.example.attendclasstpad.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * 自定义弹框适配器（用于类似spinner的下拉列表）
 */
public class SpinnerImitateAdapter extends BaseAdapter {
    public static interface IOnItemSelectListener {
        public void onItemClick(int pos);
    }

    ;

    private List<String> mObjects;

    private LayoutInflater mInflater;

    public SpinnerImitateAdapter(Context context, List<String> mObjects) {
        this.mObjects = mObjects;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refreshData(List<String> objects, int selIndex) {
        mObjects = objects;
        if (selIndex < 0) {
            selIndex = 0;
        }
        if (selIndex >= mObjects.size()) {
            selIndex = mObjects.size() - 1;
        }
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return mObjects.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_spinner_imitate_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.tv_content_layout_spiner_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Object item = getItem(pos);
        viewHolder.mTextView.setText(mObjects.get(position));

        return convertView;
    }

    public static class ViewHolder {
        public TextView mTextView;
    }

}
