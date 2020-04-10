package com.example.attendclasstpad.fg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.adapter.WordAdapter01;
import com.example.attendclasstpad.callback.InterfacesCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * txt文档类预览
 */
@SuppressLint("ValidFragment")
public class PreviewTxtFg extends Fragment implements InterfacesCallback.ICanKnowSth8 {
    private InterfacesCallback.ICanKnowSth2 callback;
    public static boolean isFirstLoad = true;
    private boolean isPageSelected = false;// 是否允许走viewPager滑动监听中的onPageSelected方法
    private boolean isScroll = false;// 是否正在滑动，默认未滑动
    private int positionMax = -1;// viewPager滑动过的最大位置
    private int lastPosition;// viewPager上一个位置
    private int newPosition;// viewPager当前位置
    private List<String> txtList;//txt文档集合
    private String html = "";//文档网络路径
    private String id = "";//授课ID

    private WordAdapter01 vpagerAdapter;
    private Handler uiHandler;

    private WebView wbContent;//文字内容
    private ViewPager vpager;// 具体内容，右侧滑动布局
    private ImageView ivNoData;//没有数据时的图片

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InterfacesCallback.ICanKnowSth2) {
            callback = (InterfacesCallback.ICanKnowSth2) context;
        }
    }

    public PreviewTxtFg(List<String> list) {
        if (list != null && list.size() > 0) {
            txtList = list;
            html = list.get(0);
        } else {
            txtList = new ArrayList<String>();
        }
    }

    public PreviewTxtFg(List<String> list, String id) {
        if (list != null && list.size() > 0) {
            txtList = list;
            html = list.get(0);
        } else {
            txtList = new ArrayList<String>();
        }

        this.id = id;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View allFgView = inflater.inflate(R.layout.layout_v_preview_txt, null);

        uiHandler = new Handler(getActivity().getMainLooper());

        ivNoData = (ImageView) allFgView.findViewById(R.id.iv_no_data01_layout_v_preview_txt);

//        initPagerViews(allFgView);
//        setWordVPager(0);
        initWebview(allFgView);

        return allFgView;
    }


    /**
     * 初始化viewPager相关布局
     */
//    private void initPagerViews(View allFgView) {
//        // 教案内容，滑动布局
//        vpager = (ViewPager) allFgView
//                .findViewById(R.id.vpager_content_layout_v_preview_txt);
//        // 限制加载数量
//        vpager.setOffscreenPageLimit(2);
//        // 监听
//        vpager.setOnPageChangeListener(new VPagerChangeListener());
//    }


//    private void setWordVPager(int position) {
////        if(vpagerAdapter!=null){
////            vpagerAdapter.notifyDataSetChanged();
////        }else {
//        // 设置适配器
//        vpagerAdapter = new WordAdapter01(getActivity(), txtList);
//        vpager.setAdapter(vpagerAdapter);
////        }
//
//        // 设置到某个位置
//        vpager.setCurrentItem(position);
//    }
    private void initWebview(View v) {
        WebView wb = (WebView) v.findViewById(R.id.wb_content_layout_v_preview_txt);

        wb.setWebViewClient(new WebViewClient());
        wb.getSettings().setJavaScriptEnabled(true);
        // wb.requestFocus();
        wb.requestDisallowInterceptTouchEvent(false);

        if (!TextUtils.isEmpty(html)) {
            wb.loadDataWithBaseURL(null, (String) html, "text/html", "utf-8",
                    null);
            ivNoData.setVisibility(View.GONE);
            wb.setVisibility(View.VISIBLE);
        } else {
            ivNoData.setVisibility(View.VISIBLE);
            wb.setVisibility(View.GONE);
        }
    }

    @Override
    public void doSth(ArrayList<String> list) {
//        ArrayList<String> list1 = new ArrayList<String>();
//
//        isPageSelected = false;
//
//        if (newPosition > lastPosition) {// 表明当前正向向后滑动
//            positionMax = newPosition;
//        } else {
//            if (list != null && list.size() > 0) {
//                list1.addAll(list);
//
//                if (txtList.size() > 0) {
//                    txtList.clear();
//                }
//                txtList.addAll(list1);
//            }
//        }
//
//        lastPosition = newPosition;
//
//
////        vpager.setCurrentItem(newPosition + 1);
//        setWordVPager(newPosition);
    }

    /**
     * 重置数据
     */
    private void resetWordList() {
        // 右侧课件
        if (txtList.size() > 0) {
            txtList.clear();
        }
    }


    /**
     * 主页面底部页卡偏移事件监听
     */
//    private class VPagerChangeListener implements ViewPager.OnPageChangeListener {
//        @Override
//        public void onPageScrollStateChanged(int state) {
//            switch (state) {
//                case 0:// 滑动结束
//                    isPageSelected = false;
//
//                    break;
//                case 1:// 正在滑动（初次加载是不走此路径的）
//                    isPageSelected = true;
//                    isScroll = true;
//
//                    break;
//                case 2:// 滑动完毕
//                    if (isScroll) {
//                        isScroll = false;
//                    } else {
//                        isPageSelected = false;
//                    }
//
//                    break;
//            }
//        }
//
//        @Override
//        public void onPageScrolled(int position, float positionOffset,
//                                   int positionOffsetPixels) {
//            // 滚动时调用
//        }
//
//        @Override
//        public void onPageSelected(int arriveIndex) { // 新的条目被选中时调用
//            if (txtList.size() == 0) {
//                Toast.makeText(getActivity(), "总页数为0，无法滑动", Toast.LENGTH_SHORT)
//                        .show();
//                return;
//            }
//
//            newPosition = arriveIndex % Integer.valueOf(txtList.size());
//
//            if (newPosition == 0
//                    && lastPosition == txtList.size() - 1) {// 特殊情况：此时是从最后一页数据向右重新滑动到第一页
//                resetWordList();
//
//                // 在数据变动了之后，先调用一下notifyDataSetChanged()，否则接下来进行网络请求，时间延迟了程序也许会崩溃
////                setPagerAdapter(0);
//
//                positionMax = -1;
//            }
//
//            if (isPageSelected && newPosition > positionMax) {
//                // 显示当前页页码与资源总页数
//                if (callback != null) {
//                    callback.getInfo(String.valueOf(newPosition + 1));
//                }
//            }
//
//            isPageSelected = true;
//        }
//    }

}
