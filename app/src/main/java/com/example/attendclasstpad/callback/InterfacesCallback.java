package com.example.attendclasstpad.callback;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

/**
 * 回调接口的定义类
 *
 * @author Administrator
 */
public class InterfacesCallback {

    /**
     * 主界面与其分页面的交互，能够实现分页面之间相互跳转的功能。使用：分页面中需要调用此方法，将需要跳转到哪个页面的信息作为参数。主界面须实现此接口，
     * 从分页面中获知将要跳转到哪个分页面，进行跳转。
     *
     * @author zhaochenhui, 2017.01.12
     */

    public interface ICanKnowGoToWhichPager {
        /**
         * 获知需要跳转的分页面位置
         */
        public void getPagerPosition(int position);
    }

    /**
     * Fragment与Activity交互的接口：传递intent信息
     *
     * @author zhaochenhui_2017.05.24
     */
    public interface ICanKnowSth {
        /**
         * 获知信息
         */
        public void getInfo(Intent intent);
    }

    /**
     * Fragment与Activity交互的接口：传递string信息
     *
     * @author zhaochenhui_2017.05.24
     */
    public interface ICanKnowSth2 {
        /**
         * 获知信息
         */
        public void getInfo(String str);
    }

    /**
     * fragment界面实现
     *
     * @param <T>
     * @author zhaochenhui, 2016.11.02
     * @deprecated
     */
    public interface ICanShowCustomList<T> {

        /**
         * 处理获取到的列表数据
         *
         * @param infoList
         */
        public void doSthAfterAcquireList(int currentPageNum, List<T> infoList);
    }

    ;

    /**
     * activity实现，实现与所用的fragment交互的操作
     *
     * @author zhaochenhui, 2016.11.02
     * @deprecated
     */
    public interface ICanDoSthWithListSurfaceOperationCallback {
        /**
         * 处理下拉刷新上拉加载更多的事件
         *
         * @param params
         */
        public void doOnPullDownToRefreshOnTask(Void... params);

        /**
         * 处理上拉加载更多的事件
         *
         * @param params
         */
        public void doOnPullUpToLoadMoreOnTask(Void... params);

        /**
         * 处理班级圈类表listView布局单项选择事件
         */
        public void doSthForLstvItemClickListener(AdapterView<?> adapterview,
                                                  View view, int i, long l);
    }

    ;

    /**
     * 答案解析（做题）界面Activity与Fragment之间的信息交互
     *
     * @author zhaochenhui_2017.05.16
     */
    public interface doOnQuestionCallback {
        /**
         * Fragment中确定展示方式（做题/解析）后，调用此方法设置Activity中的布局
         *
         * @param isExercise 当前界面是否展示为做题方式
         */
        public void doAfterKonwShowType(boolean isExercise);

    }
}
