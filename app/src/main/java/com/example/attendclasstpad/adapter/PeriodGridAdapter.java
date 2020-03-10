package com.example.attendclasstpad.adapter;

import java.util.List;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.model.KeyValue;
import com.example.attendclasstpad.util.ValidateFormatUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 自定义学段适配器（单个gridView显示）
 *
 * @author chenhui
 */
public class PeriodGridAdapter extends BaseAdapter {
    public static interface IOnItemSelectListener {
        public void onItemClick(int pos);
    }

    private Context context;
    private int currentPosition = -1;// 上次选中的位置，首次为空，默认为-1
    private String currentID = "";// 当前选中的ID
    private List<String> mObjects;

    private LayoutInflater mInflater;

    public PeriodGridAdapter(Context context, List<String> mObjects) {
        this.context = context;
        this.mObjects = mObjects;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    /**
     * 设置位置
     */
    public void setCurrentPosition(int pos) {
        this.currentPosition = pos;
    }

    /**
     * 设置ID
     */
    public void setCurrentID(String id) {
        this.currentID = id;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.layout_adapter_item_for_period, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView
                    .findViewById(R.id.tv_layout_adapter_item_for_period);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String id = mObjects.get(position);
        if (!ValidateFormatUtils.isEmpty(currentID) && currentID.equals(id)) {
            viewHolder.mTextView.setBackgroundColor(context.getResources()
                    .getColor(R.color.blue6));
            viewHolder.mTextView.setTextColor(context.getResources().getColor(
                    R.color.white));
        } else {
            viewHolder.mTextView.setBackgroundColor(context.getResources()
                    .getColor(R.color.white));
            viewHolder.mTextView.setTextColor(context.getResources().getColor(
                    R.color.color_text_content));
        }

        viewHolder.mTextView.setText(mObjects.get(position));

        return convertView;
    }

    public static class ViewHolder {
        public TextView mTextView;
    }

}
