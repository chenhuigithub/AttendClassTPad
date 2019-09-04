package com.example.attendclasstpad.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.model.ClassBean;

/**
 * 班级列表适配器
 */
public class ClassAdpter extends BaseAdapter {

    private Context context;
    private List<ClassBean> list;
    private int clickItem = -1;
    private String tag;
    public static boolean isopen = false;// 判断是开启还是关闭

    public ClassAdpter(Context context, List<ClassBean> list, String tag) {
        this.context = context;
        this.list = list;
        this.tag = tag;
    }

    public void setSelection(int position) {
        this.clickItem = position;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClassBean bean = list.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.gridview_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if ("在线".equals(tag)) {
            viewHolder.imageView.setImageResource(bean.getResource());
            if (isopen) {
                viewHolder.imageLock.setVisibility(View.GONE);
            } else {
                viewHolder.imageLock.setVisibility(View.VISIBLE);
                viewHolder.imageLock.setImageResource(bean.getIcon());
            }

            viewHolder.textView.setText(bean.getIconName());
        } else if ("离线".equals(tag)) {
            viewHolder.imageLock.setVisibility(View.GONE);
            viewHolder.imageView.setImageResource(bean.getResource());
            viewHolder.textView.setText(bean.getIconName());
        }
        return convertView;
    }

    class ViewHolder {
        private TextView textView;
        private ImageView imageView;
        private ImageView imageLock;

        public ViewHolder(View view) {
            textView = (TextView) view.findViewById(R.id.text);
            imageView = (ImageView) view.findViewById(R.id.on_ln);
            imageLock = (ImageView) view.findViewById(R.id.img_lv);
        }
    }
}
