package com.example.pullrefreshlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PullRefreshListView extends ListView implements OnScrollListener {

    private final int REFRESHING = 1;                            // 刷新中
    private final int RELEASE_TO_REFRESH = 2;                    // 释放刷新
    private final int PULL_TO_REFRESH = 3;                        // 下拉刷新
    private final int REFRESH_NORMAL = 4;                        // 正
    // 常状态
    private final int LOADMORE_MORMAL = 5;                        // 未加载更多
    private final int LOADMORE_LOADING = 6;                    // 加载中

    private int mScrollState;                                    // 当前滚动位置
    private int mRefreshState;                                    // 当前刷新状态
    private int mLoadSate;                                        // 当前加载状态

    private OnRefreshListener mRefreshListener;
    private OnScrollListener mOnScrollListener;
    private LayoutInflater mInflater;                            //布局加载器
    // "下拉刷新"布局的控件
    private RelativeLayout headerView;
    private ProgressBar pgsHeaderRefresh;
    private ImageView imgHeaderArrow;
    private TextView txtHeaderPullRefresh;
    private LinearLayout layoutHeaderLoading;
    private TextView txtHeaderTime;
    // "加载更多"布局的控件
    private RelativeLayout footerView;
    private ProgressBar pgsFooterRefresh;
    private TextView txtFooterLoadMore;

    private RotateAnimation mFlipAnimation;                    // 下拉动画
    private RotateAnimation mReverseFlipAnimation;                // 恢复动画

    private int lastTouchY;                                    // 最后点击位置的Y坐标
    private int headerHeight;                                    // 顶部高度
    private int headerPaddingTop;                                // 顶部布局的内边距
    private int headerPaddingBottom;                            // 顶部布局的内边距
    private int headerPaddingLeft;                                // 顶部布局的内边距
    private int headerPaddingRight;                            // 顶部布局的内边距

    private boolean isExitsListener;                            //是否设置了刷新事件
    private boolean refreshMode;                                //上拉刷新模式
    private boolean loadMoreMode;                                //下拉刷新模式

    private final String TAG = "PullRefreshListView";
    private final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm",
            Locale.CHINA);

    public PullRefreshListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public PullRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        /**
         * 定义旋转动画 参数：1.旋转开始的角度 2.旋转结束的角度 3. X轴伸缩模式 4.X坐标的伸缩值 5.Y轴的伸缩模式 6.Y坐标的伸缩值
         */
        mFlipAnimation = new RotateAnimation(0, 180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mFlipAnimation.setDuration(250); // 设置持续时间
        mFlipAnimation.setFillAfter(true); // 动画执行完是否停留在执行完的状态
        mReverseFlipAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setDuration(250);
        mReverseFlipAnimation.setFillAfter(true);

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headerView = (RelativeLayout) mInflater.inflate(
                R.layout.pull_refresh_header, this, false);
        pgsHeaderRefresh = (ProgressBar) headerView
                .findViewById(R.id.pull_refresh_header_pgs_refresh);
        imgHeaderArrow = (ImageView) headerView
                .findViewById(R.id.pull_refresh_header_img_arrow);
        txtHeaderPullRefresh = (TextView) headerView
                .findViewById(R.id.pull_refresh_header_txt_pull_refresh);
        layoutHeaderLoading = (LinearLayout) headerView
                .findViewById(R.id.pull_refresh_header_lot_loading);
        txtHeaderTime = (TextView) headerView
                .findViewById(R.id.pull_refresh_header_txt_time);

        footerView = (RelativeLayout) mInflater.inflate(
                R.layout.pull_loadmore_footer, this, false);
        pgsFooterRefresh = (ProgressBar) footerView
                .findViewById(R.id.pull_refresh_footer_pgs_loadmore);
        txtFooterLoadMore = (TextView) footerView
                .findViewById(R.id.pull_refresh_footer_txt_loadmore);

        // 为列表添加"下拉刷新"和"加载更多"布局
        addHeaderView(headerView);
        addFooterView(footerView);

        refreshMode = true;
        loadMoreMode = true;

        measureView(headerView); // 测量"下拉刷新"视图
        headerHeight = headerView.getMeasuredHeight(); // 获取"下拉刷新"布局的高度
        // 初始化"下拉刷新"内边距
        headerPaddingTop = headerView.getPaddingTop();
        headerPaddingBottom = headerView.getPaddingBottom();
        headerPaddingLeft = headerView.getPaddingLeft();
        headerPaddingRight = headerView.getPaddingRight();
        // 初始化刷新状态和加载状态
        mRefreshState = REFRESH_NORMAL;
        mLoadSate = LOADMORE_MORMAL;

        // 设置点击"加载更多"时，加载更多
        footerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mLoadSate != LOADMORE_LOADING) {
                    prepareForLoad();
                    loadMore();
                    mLoadSate = LOADMORE_LOADING;
                }
            }
        });

        super.setOnScrollListener(this);
    }

    /**
     * 设置无"下拉刷新"模式
     */
    public void setNoRefreshMode() {
        refreshMode = false;
        this.removeHeaderView(headerView);
    }

    /**
     * 设置无"加载更多"模式
     */
    public void setNoLoadMoreMode() {
        loadMoreMode = false;
        this.removeFooterView(footerView);
    }

    /**
     * 测量View的高度
     *
     * @param child
     */
    private void measureView(View child) {
        // TODO Auto-generated method stub
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        // TODO Auto-generated method stub
        mOnScrollListener = listener;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        // 判断是否是下拉刷新模式
        if (refreshMode) {
            setSelection(1);
        } else {
            setSelection(0);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub
        if (refreshMode) {
            if (mScrollState == SCROLL_STATE_TOUCH_SCROLL
                    && mRefreshState != REFRESHING) {
                if (firstVisibleItem == 0) {
                    // 如果下拉了listiview,则显示上拉刷新动画
                    if (mRefreshState != RELEASE_TO_REFRESH
                            && (headerView.getTop() >= 0 || headerView
                            .getBottom() >= headerHeight)) {
                        txtHeaderPullRefresh.setText("释放刷新");
                        imgHeaderArrow.clearAnimation();
                        imgHeaderArrow.startAnimation(mFlipAnimation);
                        mRefreshState = RELEASE_TO_REFRESH;
                        // 如果下拉距离不够，则回归原来的状态
                    } else if (mRefreshState != PULL_TO_REFRESH
                            && headerView.getBottom() < headerHeight) {
                        txtHeaderPullRefresh.setText("下拉刷新");
                        if (mRefreshState != REFRESH_NORMAL) {
                            imgHeaderArrow.clearAnimation();
                            imgHeaderArrow
                                    .startAnimation(mReverseFlipAnimation);
                        }
                        mRefreshState = PULL_TO_REFRESH;
                    }
                }
            } else if (mScrollState == SCROLL_STATE_FLING
                    && firstVisibleItem == 0 && mRefreshState != REFRESHING) {
                // 当列表处于飞滑状态时，不能让他滑到表头
                setSelection(1);
            }
        }

        /**
         * 判断是否滑到底部
         */
        if (loadMoreMode && mLoadSate != LOADMORE_LOADING && isExitsListener
                && getBottom() == footerView.getBottom()) {
            prepareForLoad();
            loadMore();
            mLoadSate = LOADMORE_LOADING;
        }

        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem,
                    visibleItemCount, totalItemCount);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        final int touchY = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                // 判断是否有表头
                if (refreshMode) {
                    // 如果滚动条不可用，设置为可用状态
                    if (!isVerticalScrollBarEnabled()) {
                        setVerticalScrollBarEnabled(true);
                    }
                    if (getFirstVisiblePosition() == 0
                            && mRefreshState != REFRESHING) {
                        // 判断下拉距离是否符合刷新条件
                        if (headerView.getBottom() >= headerHeight
                                && mRefreshState == RELEASE_TO_REFRESH) {
                            // 准备刷新
                            prepareForRefresh();
                            // 刷新
                            refresh();
                        } else if (headerView.getBottom() < headerHeight
                                && mRefreshState == PULL_TO_REFRESH) {
                            // 重置列表头控件
                            resetHeader();
                            // 弹回第一项
                            setSelection(1);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                calHeaderPadding(touchY);
                break;
            case MotionEvent.ACTION_DOWN:
                lastTouchY = touchY;
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 计算并设置表头的顶部内边距
     *
     * @param ev
     */
    public void calHeaderPadding(int y) {
        // 如果列表处于"释放刷新"状态的话，扩大表头的顶部内边距
        if (mRefreshState == RELEASE_TO_REFRESH && mRefreshState != REFRESHING) {
            //感觉2.5倍的差值滑起来挺不错的，自己可以设置
            int topPadding = (int) ((y - lastTouchY) / 2.5) - headerPaddingTop;
            // 设置上、下、左、右四个内边距
            headerView.setPadding(headerPaddingLeft, topPadding,
                    headerPaddingRight, headerPaddingBottom);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
        mScrollState = scrollState;
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    /**
     * 为刷新前做准备，调整控件是否显示
     */
    private void prepareForRefresh() {
        // 让列表滚动到表头,距离headerHeight的位置，在500ms内
        smoothScrollToPosition(0);
        setSelectionFromTop(0, headerHeight);
        resetHeaderPadding();
        // 设置表头控件的显示
        pgsHeaderRefresh.setVisibility(View.VISIBLE);
        txtHeaderPullRefresh.setVisibility(View.GONE);
        imgHeaderArrow.clearAnimation();
        imgHeaderArrow.setVisibility(View.GONE);
        layoutHeaderLoading.setVisibility(View.VISIBLE);
        // 设置刷新状态为"刷新中"
        mRefreshState = REFRESHING;
    }

    private void prepareForLoad() {
        txtFooterLoadMore.setText("加载中");
        pgsFooterRefresh.setVisibility(View.VISIBLE);
    }

    /**
     * 重置顶部控件
     */
    private void resetHeader() {
        // TODO Auto-generated method stub
        if (mRefreshState != REFRESH_NORMAL) {
            resetHeaderPadding();
            mRefreshState = REFRESH_NORMAL;
            txtHeaderPullRefresh.setText("下拉刷新");
            imgHeaderArrow.clearAnimation();
            pgsHeaderRefresh.setVisibility(View.GONE);
            layoutHeaderLoading.setVisibility(View.GONE);
            imgHeaderArrow.setVisibility(View.VISIBLE);
            txtHeaderPullRefresh.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 恢复顶部的内边距
     */
    private void resetHeaderPadding() {
        headerView.setPadding(headerPaddingLeft, headerPaddingTop,
                headerPaddingRight, headerPaddingBottom);
    }

    /**
     * 下拉刷新
     */
    private void refresh() {
        if (mRefreshListener != null) {
            mRefreshListener.onRefresh();
        }
    }

    /**
     * 下拉刷新完成时
     */
    public void refreshComplete() {
        resetHeader();
        txtHeaderTime.setText("更新于 " + sdf.format(new Date()));
        if (headerView.getBottom() > 0) {
            setSelection(1);
        }
    }

    /**
     * 加载更多
     */
    private void loadMore() {
        if (mRefreshListener != null) {
            mRefreshListener.onLoadMore();
        }
    }

    /**
     * 加载更多完成时
     */
    public void loadMoreComplete() {
        Log.i(TAG, "loadMoreComplete");
        mLoadSate = LOADMORE_MORMAL;
        txtFooterLoadMore.setText("加载更多");
        footerView.setClickable(true);
        pgsFooterRefresh.setVisibility(View.GONE);
    }

    /**
     * 加载没有更多完成时
     */
//    public void loadNoMoreComplete() {
//        Log.i(TAG, "loadNoMoreComplete");
////        mLoadSate = LOADMORE_MORMAL;
////        txtFooterLoadMore.setText("没有更多数据了");
//        footerView.setClickable(false);
//        pgsFooterRefresh.setVisibility(View.GONE);
//    }


    /**
     * 设置刷新事件
     *
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        isExitsListener = true;
        mRefreshListener = listener;
    }

    /**
     * 接口定义一个回调方法当列表被刷新
     */
    public interface OnRefreshListener {
        /**
         * 当列表下拉刷新时，调用这个方法
         */
        public void onRefresh();

        /**
         * 当列表上拉加载更多是，调用这个方法
         */
        public void onLoadMore();
    }

}
