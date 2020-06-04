package com.example.attendclasstpad.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义GridView
 * 1.LinearLayout模拟网格布局，单项内容
 *
 * @author chenhui 2020.05.29
 */
public class CustomGridView01 extends LinearLayout {
    private Context context;
    private List<View> viewList;//GridView中的所有子View
    private int col;//列数
    private int row;//行数

    public CustomGridView01(Context context) {
        super(context);
    }

    public CustomGridView01(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        viewList = new ArrayList<>();
        this.setOrientation(VERTICAL);
        this.setGravity(Gravity.CENTER_VERTICAL);

        col = 8;
    }


    public CustomGridView01(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        this.context = context;
//        viewList = new ArrayList<>();
//        this.setOrientation(VERTICAL);
//        this.setGravity(Gravity.CENTER_VERTICAL);
//
//        //从xml中取出定义的列数赋值给col，默认为1列
////        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.mGridView);
////        col = typedArray.getInt(R.styleable.mGridView_col, 1);
//        col = 3;
    }


    /**
     * 添加子布局
     *
     * @param view 子布局
     */
    public void addChild(View view) {
        if (viewList.size() > 0) {
            viewList.clear();
        }

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(12, 5, 12, 5);
        view.setLayoutParams(params);

        viewList.add(view);
        int size = viewList.size();
        row = (int) Math.ceil(((double) size) / ((double) col));

        //应该画到哪一列
        int nCol = (size - 1) % col;

        //是否需要添加新行
        boolean needAdd = false;
        for (int i = 1; i <= row; i++) {

            //只画最新的一行
            if (i == row) {
                LinearLayout layout = (LinearLayout) this.getChildAt(i - 1);
                if (layout == null) {
                    needAdd = true;
                    layout = new LinearLayout(context);
                    layout.setOrientation(HORIZONTAL);
                    LayoutParams params1 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layout.setLayoutParams(params1);
                }

                for (int j = 0; j < col; j++) {

                    //只画最新的一行中添加到的那一列
                    if (j == nCol) {

                        //添加列
                        layout.addView(view);
                    }
                }

                //添加行
                if (needAdd) {
                    addView(layout);
                }
            }
        }
    }


}
