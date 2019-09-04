package com.example.attendclasstpad.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.model.Test;
import com.example.attendclasstpad.util.ConstantsUtils;

/**
 * 选择题---内容适配器
 *
 * @author chenhui 2019.04.04
 */
public class TestChoiceAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private Resources res;

    private Handler handler;

    private List<Test> list;
    private int mChildCount = 0;

    public TestChoiceAdapter(Context context, Handler handler, List<Test> list) {
        this.inflater = LayoutInflater.from(context);
        res = context.getResources();
        this.context = context;
        this.handler = handler;

        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        System.out.println("isViewFromObject: " + (view == object));
        // 当划到新的条目, 又返回来, view是否可以被复用.
        // 返回判断规则
        return view == object;
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    /*
     * 如果item的位置如果没有发生变化，则返回POSITION_UNCHANGED。如果返回了POSITION_NONE，表示该位置的item已经不存在了
     * 。 默认的实现是假设item的位置永远不会发生变化，而返回POSITION_UNCHANGED。
     * 在这里修改适配器的写法，覆盖getItemPosition(
     * )方法，当调用notifyDataSetChanged时，让getItemPosition方法人为的返回POSITION_NONE，
     * 从而达到强迫viewpager重绘所有item的目的。
     */
    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // object 要销毁的对象
        System.out.println("destroyItem销毁: " + position);
        container.removeView((View) object);
    }

    /**
     * 发送消息
     *
     * @param what
     * @param obj
     */
    public void sendMessage(int what, Object obj) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = obj;
        handler.sendMessage(msg);
    }

    @Override
    public Object instantiateItem(View container, int position) {
        View allView = inflater.inflate(R.layout.layout_fg_test_choice, null);

        TextView tvContent = (TextView) allView
                .findViewById(R.id.tv_content_layout_fg_test_choice);
        tvContent.setText(list.get(position).getContent());

        final CheckBox cboxChoice = (CheckBox) allView
                .findViewById(R.id.cbox_choice_test_layout_fg_test_choice);

        final Test test = list.get(position);
        if (test.isChoiced()) {
            cboxChoice.setBackgroundResource(R.drawable.cbox_p);
            cboxChoice.setChecked(true);
        } else {
            cboxChoice.setBackgroundResource(R.drawable.cbox_n);
            cboxChoice.setChecked(false);
        }

        cboxChoice.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked) {// 选中！
                    test.setChoiced(true);

                    notifyDataSetChanged();

                    sendMessage(ConstantsUtils.ADD_CHOICE_TEST, null);
                } else {
                    test.setChoiced(false);

                    notifyDataSetChanged();

                    sendMessage(ConstantsUtils.REMOVE_CHOICE_TEST, null);
                }
            }
        });

        // a. 把View对象添加到container中
        ((ViewGroup) container).addView(allView);
        return allView;
    }

}
