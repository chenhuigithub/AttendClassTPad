package com.example.attendclasstpad.adapter;

import java.util.List;
import java.util.Map;

import com.example.attendclasstpad.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ClassOutAdpter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> listItem;
    private int clickItem = -1;

    private int[] icon = {R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher};
    //    private String[] iconName = {"a", "b", "c", "d", "e", "f", "g", "h", "i"};

    public ClassOutAdpter(Context context, List<Map<String, Object>> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    public void setSelection(int position) {
        this.clickItem = position;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_item_out, null);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.img_out);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_name_layout_v_student);

        Map<String, Object> map = listItem.get(position);
        imageView.setImageResource((Integer) map.get("ItemImage"));
        textView.setText(map.get("ItemText") + "");

        if (clickItem == position) {
            imageView.setImageResource(icon[position]);
//	            textView.setText(iconName[position]);
        }

        return convertView;

    }

}
