package com.example.attendclasstpad.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.example.attendclasstpad.R;
import com.example.attendclasstpad.model.Courseware;
import com.example.attendclasstpad.model.FileContent;
import com.example.attendclasstpad.util.UrlUtils;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 自定义适配器-图片（课件）版
 *
 * @author chenhui 2018.08.14
 */
public class CustomPagerAdapter03 extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Courseware> list;
    private Resources res;
    private int mChildCount = 0;

    public CustomPagerAdapter03(Context context, List<Courseware> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        res = context.getResources();

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

    @SuppressWarnings("deprecation")
    @Override
    public Object instantiateItem(View container, int position) {
        System.out.println("instantiateItem初始化: " + position);
        // container: 容器: ViewPager
        // position: 当前要显示条目的位置 0 -> 4

        View view = inflater.inflate(R.layout.layout_v_courseware, null);

        ImageView iv = (ImageView) view
                .findViewById(R.id.iv_file_show_layout_v_courseware);
        String picHtml = UrlUtils.PREFIX
                + list.get(position).getThumbPath();
        Glide.with(context).load(picHtml)
                .error(res.getDrawable(R.drawable.no_data)).into(iv);

//        Courseware file = list.get(position);
//        if (file != null) {
//            iv.setBackgroundResource(file.getIvRes());
//        }

        // a. 把View对象添加到container中
        ((ViewGroup) container).addView(view);
        // b. 把View对象返回给框架, 适配器
        return view; // 必须重写, 否则报异常
    }
}
