package com.example.attendclasstpad.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.model.PaintSize;

public class DrawPaintSizeAdapter extends BaseListAdapter<PaintSize> {
    Context context;

    public DrawPaintSizeAdapter(Context context, List<PaintSize> dataList) {
        super(context, dataList);
        this.context = context;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.layout_v_paint_size;
    }

    @Override
    protected void doAssignValueForView(int position, View resultView,
                                        PaintSize dataObj) {
        ImageView iv = (ImageView) resultView
                .findViewById(R.id.iv_layout_v_paint_size);

        if (position == 0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    50, 50);
            iv.setLayoutParams(params);

            iv.setBackgroundColor(context.getResources().getColor(R.color.red));
        } else if (position == 1) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    80, 80);
            iv.setLayoutParams(params);

            iv.setBackgroundColor(context.getResources().getColor(R.color.red));
        } else if (position == 2) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    100, 100);
            iv.setLayoutParams(params);

            iv.setBackgroundColor(context.getResources().getColor(R.color.red));
        }

    }

}
