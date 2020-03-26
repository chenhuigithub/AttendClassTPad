package com.example.attendclasstpad.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.attendclasstpad.model.DataInfo;

import java.util.List;

/**
 * 自定义ViewPager适配器-文字版
 *
 * @author chenhui
 */
public class CustomPagerAdapter04 extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private Resources res;

    private List<DataInfo> list;
    private int mChildCount = 0;

    public CustomPagerAdapter04(Context context, List<DataInfo> list) {
        this.inflater = LayoutInflater.from(context);
        res = context.getResources();
        this.context = context;

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

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    @SuppressWarnings("deprecation")
    public Object instantiateItem(View container, int position) {
        System.out.println("instantiateItem初始化: " + position);
        // container: 容器: ViewPager
        // position: 当前要显示条目的位置 0 -> 4

        if (list.size() == 0) {
            return new WebView(context);
        }

        if (position > list.size() - 1) {
            return new WebView(context);
        }

        System.out.print("position======" + position);
        DataInfo info = list.get(position);
        String html = info.getBody();

        WebView wb = new WebView(context);

        wb.setWebViewClient(new WebViewClient());
        wb.getSettings().setJavaScriptEnabled(true);
        // wb.requestFocus();
        wb.requestDisallowInterceptTouchEvent(false);

        wb.loadDataWithBaseURL(null, (String) html, "text/html", "utf-8", null);

        // a. 把View对象添加到container中
        ((ViewGroup) container).addView(wb);
        // b. 把View对象返回给框架, 适配器
        return wb; // 必须重写, 否则报异常
    }

}
