package com.example.attendclasstpad.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 基础VPager滑动布局
 */
public abstract class BaseVPagerAdapter01<T> extends PagerAdapter {
    protected Context context;
    private List<T> dataList;
    private int mChildCount = 0;

    public BaseVPagerAdapter01(Context context, List<T> dataList) {
        this.context = context;
        this.dataList = dataList;
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
    public int getCount() {
        return Integer.MAX_VALUE;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        System.out.println("isViewFromObject: " + (view == object));
        // 当划到新的条目, 又返回来, view是否可以被复用.
        // 返回判断规则
        return view == object;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    @SuppressWarnings("deprecation")
    public Object instantiateItem(View container, int position) {
        System.out.println("instantiateItem初始化: " + position);
        // container: 容器: ViewPager
        // position: 当前要显示条目的位置 0 -> 4

        int newPosition = position % dataList.size();

        T info = dataList.get(newPosition);

        // // a. 把View对象添加到container中
        // ((ViewGroup) container).addView(wb);
        // // b. 把View对象返回给框架, 适配器
        // return wb; // 必须重写, 否则报异常

        // a. 把View对象添加到container中
        View v = getChildView(info);
        ((ViewGroup) container).addView(v);
        // b. 把View对象返回给框架, 适配器
        return v; // 必须重写, 否则报异常
    }

    protected abstract View getChildView(T info);


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // object 要销毁的对象
        System.out.println("destroyItem销毁: " + position);
        container.removeView((View) object);
    }
}
