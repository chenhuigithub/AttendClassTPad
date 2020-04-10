package com.example.attendclasstpad.adapter;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;


import com.example.attendclasstpad.R;
import com.example.attendclasstpad.util.ValidateFormatUtils;

import java.util.List;

/**
 * 文字适配器
 *
 * @author chenhui
 */
public class WordAdapter01 extends BaseVPagerAdapter01<String> {
    private Context context;
    private int mChildCount = 0;
    private List<String> list;


    public WordAdapter01(Context context, List<String> list) {
        super(context, list);
        this.context = context;
    }

    protected View getChildView(String html) {
        WebView wb = new WebView(context);

        wb.setWebViewClient(new WebViewClient());
        wb.getSettings().setJavaScriptEnabled(true);
        // wb.requestFocus();
        wb.requestDisallowInterceptTouchEvent(false);

        if (!ValidateFormatUtils.isEmpty(html)) {
            wb.loadDataWithBaseURL(null, (String) html, "text/html", "utf-8",
                    null);
            wb.setVisibility(View.VISIBLE);
        } else {
            ImageView ivNoData = new ImageView(context);
            ivNoData.setBackgroundResource(R.drawable.no_data);
            wb.setVisibility(View.GONE);
            return ivNoData;
        }

        return wb;
    }

}
