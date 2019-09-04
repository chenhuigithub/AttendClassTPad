package com.example.attendclasstpad.fg;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.adapter.ColorAdapter;
import com.example.attendclasstpad.util.ConstantsUtils;
import com.example.attendclasstpad.view.CustomSurfaceView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 绘制菜单面板
 *
 * @author chenhui 2019.04.16
 */
@SuppressLint("ValidFragment")
public class DrawMenuFg extends Fragment {
    /**
     * 默认布局
     */
    private int layoutResID = R.layout.layout_v_draw_menu02;
    // private int layoutResID =0;

    /**
     * 选色板的颜色资源数组
     */
    private final int[] colorRes = {R.color.red, R.color.blue, R.color.white,
            R.color.green};// 顺序：红、蓝、白、绿

    private int colorCur;// 当前画笔的颜色
    /**
     * 展示方式标签（画笔、白板、全屏等）
     */
    public static String SHOW_STYLE = "showStyle";
    /**
     * 展示方式（画笔、白板、全屏等）
     */
    private int showStyle;

    boolean needFullScreenMenu = false;// 需要显示全屏，默认不显示
    boolean needExitFullScreenMenu = false;// 需要显示退出全屏，默认不显示

    Handler handler;

    private CustomSurfaceView sfvBoard;// 用于绘制的画板
    private LinearLayout llPaint;// 画笔
    private LinearLayout llEraser;// 橡皮
    private LinearLayout llRevoke;// 撤销
    private LinearLayout llRedo;// 恢复
    private LinearLayout llClearScreen;// 清屏
    private LinearLayout llWhiteBoard;// 白板
    private LinearLayout llFullScreen;// 全屏
    private LinearLayout llExitFullScreen;// 退出全屏
    //	private ImageView iv;// 课程图片
    private ImageView ivMenu;// 菜单按钮,点击后关闭菜单栏
    // 退出
    private TextView tvExit;

    // 细线(圆形标志)
    private FrameLayout flSmallPaint;
    private LinearLayout llSmallPaint;
    private ImageView ivSmallPaint;
    // 中等线条(圆形标志)
    private FrameLayout flMiddlePaint;
    private LinearLayout llMiddlePaint;
    private ImageView ivMiddlePaint;
    // 粗线条(圆形标志)
    private FrameLayout flLargePaint;
    private LinearLayout llLargePaint;
    private ImageView ivLargePaint;

    public DrawMenuFg() {
    }

    public DrawMenuFg(int layoutResID, CustomSurfaceView sfvBoard,
                      boolean needFullScreenMenu, boolean needExitFullScreenMenu,
                      Handler handler) {
        setLayout(layoutResID);

        this.needFullScreenMenu = needFullScreenMenu;
        this.needExitFullScreenMenu = needExitFullScreenMenu;

        this.handler = handler;
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View allFgView = inflater.inflate(getLayout(), null);

        // 画笔（涂鸦）
        LinearLayout llPaint = (LinearLayout) allFgView
                .findViewById(R.id.ll_paint_layout_v_draw_menu);
        final TextView tvPaint = (TextView) allFgView
                .findViewById(R.id.tv_paint_layout_v_draw_menu);
        llPaint.setOnClickListener(new WidgetsListeners());

        // 细线(圆形标志)
        // flSmallPaint = (FrameLayout) allFgView
        // .findViewById(R.id.fl_small_paint_layout_v_draw_menu);
        // llSmallPaint = (LinearLayout) allFgView
        // .findViewById(R.id.ll_small_paint_layout_v_draw_menu);
        // ivSmallPaint = (ImageView) allFgView
        // .findViewById(R.id.iv_small_paint_layout_v_draw_menu);
        // flSmallPaint.setOnClickListener(new WidgetsListeners());
        //
        // // 中等线条(圆形标志)
        // flMiddlePaint = (FrameLayout) allFgView
        // .findViewById(R.id.fl_middle_paint_layout_v_draw_menu);
        // llMiddlePaint = (LinearLayout) allFgView
        // .findViewById(R.id.ll_middle_paint_layout_v_draw_menu);
        // llMiddlePaint.setVisibility(View.INVISIBLE);
        // ivMiddlePaint = (ImageView) allFgView
        // .findViewById(R.id.iv_middle_paint_layout_v_draw_menu);
        // flMiddlePaint.setOnClickListener(new WidgetsListeners());
        //
        // // 粗线(圆形标志)
        // flLargePaint = (FrameLayout) allFgView
        // .findViewById(R.id.fl_large_paint_layout_v_draw_menu);
        // llLargePaint = (LinearLayout) allFgView
        // .findViewById(R.id.ll_large_paint_layout_v_draw_menu);
        // llLargePaint.setVisibility(View.INVISIBLE);
        // ivLargePaint = (ImageView) allFgView
        // .findViewById(R.id.iv_large_paint_layout_v_draw_menu);
        // flLargePaint.setOnClickListener(new WidgetsListeners());

        // 显示粗细按钮默认为红色
        colorCur = colorRes[0];
        // setThicknessMenu(0);

        // GridView gdvColor = (GridView) drawMenuDialog
        // .findViewById(R.id.gvd_color_layout_v_draw_menu);

        // gdvColor.setAdapter(new ColorAdapter(getActivity(), colorRes));
        // gdvColor.setOnItemClickListener(new OnItemClickListener() {
        // @Override
        // public void onItemClick(AdapterView<?> parent, View v, int pos,
        // long id) {
        // // 赋值
        // colorCur = colorRes[pos];
        // // 设置粗细按钮
        // setThicknessMenu(pos);
        // }
        // });

        // 画笔
        llPaint = (LinearLayout) allFgView
                .findViewById(R.id.ll_paint_layout_v_draw_menu);
        llPaint.setOnClickListener(new WidgetsListeners());

        // 橡皮
        llEraser = (LinearLayout) allFgView
                .findViewById(R.id.ll_eraser_layout_v_draw_menu);
        llEraser.setOnClickListener(new WidgetsListeners());

        // 撤销
        llRevoke = (LinearLayout) allFgView
                .findViewById(R.id.ll_revoke_layout_v_draw_menu);
        llRevoke.setOnClickListener(new WidgetsListeners());

        // 恢复
        llRedo = (LinearLayout) allFgView
                .findViewById(R.id.ll_redo_layout_v_draw_menu);
        llRedo.setOnClickListener(new WidgetsListeners());

        // 清屏
        llClearScreen = (LinearLayout) allFgView
                .findViewById(R.id.ll_clear_screen_layout_v_draw_menu);
        llClearScreen.setOnClickListener(new WidgetsListeners());

        // 白板
        llWhiteBoard = (LinearLayout) allFgView
                .findViewById(R.id.ll_white_board_layout_v_draw_menu);
        llWhiteBoard.setOnClickListener(new WidgetsListeners());

        if (needFullScreenMenu) {
            // 全屏
            llFullScreen = (LinearLayout) allFgView
                    .findViewById(R.id.ll_full_screen_layout_v_draw_menu);
            llFullScreen.setOnClickListener(new WidgetsListeners());
        } else {
            llFullScreen.setVisibility(View.GONE);
        }

        if (needExitFullScreenMenu) {
            // 退出全屏
            llExitFullScreen = (LinearLayout) allFgView
                    .findViewById(R.id.ll_exit_full_screen_layout_v_draw_menu);
            llExitFullScreen.setOnClickListener(new WidgetsListeners());
        } else {
            llExitFullScreen.setVisibility(View.GONE);
        }

        return allFgView;
    }

    private int getLayout() {
        return layoutResID;
    }

    private void setLayout(int resID) {
        this.layoutResID = resID;
    }

    /**
     * 设置粗细按钮（顺序不可打乱，与colorRes数组相对应）
     *
     * @param pos 位置
     */
    @SuppressLint("NewApi")
    private void setThicknessMenu(int pos) {
        switch (pos) {
            case 0:// 红色
                setThicknessMenuColor(R.drawable.selector_for_red_disc);
                break;
            case 1:// 蓝色
                setThicknessMenuColor(R.drawable.selector_for_blue_disc);
                break;
            case 2:// 白色
                setThicknessMenuColor(R.drawable.selector_for_white_disc);
                break;
            case 3:// 绿色
                setThicknessMenuColor(R.drawable.selector_for_green_disc);
                break;
        }
    }

    /**
     * 设置粗细按钮的颜色
     *
     * @param res 样式资源
     */
    private void setThicknessMenuColor(int res) {
        ivSmallPaint.setBackgroundResource(res);
        ivMiddlePaint.setBackgroundResource(res);
        ivLargePaint.setBackgroundResource(res);
    }

    /**
     * 画笔(涂鸦)
     */
    private void graffiti() {
        if (ConstantsUtils.GRAFFITI != showStyle) {
            // 重画
            sfvBoard.resetCanvas();
            // 重置数据
            sfvBoard.resetPathList();

            showStyle = ConstantsUtils.GRAFFITI;
        }

        // 设置画笔颜色
        sfvBoard.setPaintColor(colorCur);

        sfvBoard.setPaintBrush();

//		iv.setVisibility(View.VISIBLE);
//		sfvBoard.setVisibility(View.VISIBLE);
    }

    /**
     * 关闭绘制菜单
     */
    // private void dismissDrawMenuDialog() {
    // if (drawMenuDialog.isShowing()) {
    // drawMenuDialog.dismiss();
    // }
    // }

    /**
     * 橡皮擦
     */
    private void eraser() {
        showStyle = ConstantsUtils.ERASER;

        sfvBoard.setEraser();
//		sfvBoard.setVisibility(View.VISIBLE);
    }

    /**
     * 白板
     */
    private void whiteBoard() {
        if (ConstantsUtils.WHITE_BOARD != showStyle) {
            // 重画
            sfvBoard.resetCanvas();
            // 重置数据
            sfvBoard.resetPathList();

            showStyle = ConstantsUtils.WHITE_BOARD;
        }

        sfvBoard.setPaintBrush();

//		iv.setVisibility(View.GONE);
//		sfvBoard.setVisibility(View.VISIBLE);
    }

    /**
     * 更改画笔大小之后的操作
     */
    private void doSthAfterChangePaintSize(int vi01, int vi02, int vi03) {
        llSmallPaint.setVisibility(vi01);
        llMiddlePaint.setVisibility(vi02);
        llLargePaint.setVisibility(vi03);
    }

    /**
     * 退出界面需要的操作
     */
    private void exit() {
        sfvBoard.resetCanvas();
        sfvBoard.resetPathList();

        // 通知调用者，关闭此菜单
    }

    /**
     * 监听
     *
     * @author chenhui
     */
    private class WidgetsListeners implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {

                case R.id.ll_paint_layout_v_draw_menu:// 画笔
                    graffiti();

                    // dismissDrawMenuDialog();
                    // ivMenu.setVisibility(View.VISIBLE);
                    // tvExit.setVisibility(View.VISIBLE);
                    //
                    // // 按钮显示效果
                    // llPaint.setBackgroundResource(R.drawable.bg_menu_item_p);
                    // llEraser.setBackgroundResource(R.drawable.bg_menu_item);
                    // llRevoke.setBackgroundResource(R.drawable.bg_menu_item);
                    // llRedo.setBackgroundResource(R.drawable.bg_menu_item);
                    // llClearScreen.setBackgroundResource(R.drawable.bg_menu_item);
                    // llWhiteBoard.setBackgroundResource(R.drawable.bg_menu_item);
                    // if (needFullScreenMenu) {
                    // llExitFullScreen
                    // .setBackgroundResource(R.drawable.bg_menu_item);
                    // }

                    break;

                case R.id.ll_eraser_layout_v_draw_menu:// 橡皮擦
                    eraser();

                    // dismissDrawMenuDialog();
                    // ivMenu.setVisibility(View.VISIBLE);
                    // tvExit.setVisibility(View.VISIBLE);

                    // 按钮显示效果
                    // llPaint.setBackgroundResource(R.drawable.bg_menu_item);
                    // llEraser.setBackgroundResource(R.drawable.bg_menu_item_p);
                    // llRevoke.setBackgroundResource(R.drawable.bg_menu_item);
                    // llRedo.setBackgroundResource(R.drawable.bg_menu_item);
                    // llClearScreen.setBackgroundResource(R.drawable.bg_menu_item);
                    // llWhiteBoard.setBackgroundResource(R.drawable.bg_menu_item);
                    // if (needExitFullScreenMenu) {
                    // llExitFullScreen
                    // .setBackgroundResource(R.drawable.bg_menu_item);
                    // }

                    break;

                case R.id.ll_revoke_layout_v_draw_menu:// 撤销
                    // dismissDrawMenuDialog();
                    ivMenu.setVisibility(View.VISIBLE);
                    tvExit.setVisibility(View.VISIBLE);

                    sfvBoard.undo();

                    // 按钮显示效果
                    llPaint.setBackgroundResource(R.drawable.bg_menu_item);
                    llEraser.setBackgroundResource(R.drawable.bg_menu_item);
                    llRevoke.setBackgroundResource(R.drawable.bg_menu_item_p);
                    llRedo.setBackgroundResource(R.drawable.bg_menu_item);
                    llClearScreen.setBackgroundResource(R.drawable.bg_menu_item);
                    llWhiteBoard.setBackgroundResource(R.drawable.bg_menu_item);
                    if (needExitFullScreenMenu) {
                        llExitFullScreen
                                .setBackgroundResource(R.drawable.bg_menu_item);
                    }

                    break;

                case R.id.ll_recovery_layout_v_draw_menu:// 恢复
                    // dismissDrawMenuDialog();
                    ivMenu.setVisibility(View.VISIBLE);
                    tvExit.setVisibility(View.VISIBLE);

                    sfvBoard.redo();

                    // 按钮显示效果
                    llPaint.setBackgroundResource(R.drawable.bg_menu_item);
                    llEraser.setBackgroundResource(R.drawable.bg_menu_item);
                    llRevoke.setBackgroundResource(R.drawable.bg_menu_item);
                    llRedo.setBackgroundResource(R.drawable.bg_menu_item_p);
                    llClearScreen.setBackgroundResource(R.drawable.bg_menu_item);
                    llWhiteBoard.setBackgroundResource(R.drawable.bg_menu_item);
                    llExitFullScreen.setBackgroundResource(R.drawable.bg_menu_item);
                    break;

                case R.id.ll_clear_screen_layout_v_draw_menu:// 清屏
                    // dismissDrawMenuDialog();

                    sfvBoard.resetCanvas();
                    sfvBoard.resetPathList();

                    ivMenu.setVisibility(View.VISIBLE);
                    tvExit.setVisibility(View.VISIBLE);

                    // 按钮显示效果
                    llPaint.setBackgroundResource(R.drawable.bg_menu_item);
                    llEraser.setBackgroundResource(R.drawable.bg_menu_item);
                    llRevoke.setBackgroundResource(R.drawable.bg_menu_item);
                    llRedo.setBackgroundResource(R.drawable.bg_menu_item);
                    llClearScreen.setBackgroundResource(R.drawable.bg_menu_item_p);
                    llWhiteBoard.setBackgroundResource(R.drawable.bg_menu_item);
                    llExitFullScreen.setBackgroundResource(R.drawable.bg_menu_item);
                    break;

                case R.id.ll_white_board_layout_v_draw_menu:// 白板
                    // dismissDrawMenuDialog();
                    ivMenu.setVisibility(View.VISIBLE);
                    tvExit.setVisibility(View.VISIBLE);

                    whiteBoard();

                    // 按钮显示效果
                    llPaint.setBackgroundResource(R.drawable.bg_menu_item);
                    llEraser.setBackgroundResource(R.drawable.bg_menu_item);
                    llRevoke.setBackgroundResource(R.drawable.bg_menu_item);
                    llRedo.setBackgroundResource(R.drawable.bg_menu_item);
                    llClearScreen.setBackgroundResource(R.drawable.bg_menu_item);
                    llWhiteBoard.setBackgroundResource(R.drawable.bg_menu_item_p);
                    llExitFullScreen.setBackgroundResource(R.drawable.bg_menu_item);
                    break;
                // case R.id.ll_exit_layout_v_draw_menu:// 退出全屏
                // // 按钮显示效果
                // llPaint.setBackgroundResource(R.drawable.bg_menu_item);
                // llEraser.setBackgroundResource(R.drawable.bg_menu_item);
                // llRevoke.setBackgroundResource(R.drawable.bg_menu_item);
                // llRecovery.setBackgroundResource(R.drawable.bg_menu_item);
                // llClearScreen.setBackgroundResource(R.drawable.bg_menu_item);
                // llWhiteBoard.setBackgroundResource(R.drawable.bg_menu_item);
                // llExit.setBackgroundResource(R.drawable.bg_menu_item_p);
                //
                // exit();
                //
                // break;

                // case R.id.tv_exit_layout_activity_share_full_screen:// 退出全屏
                // exit();
                //
                // break;

                // case R.id.fl_small_paint_layout_v_draw_menu:// 细线
                // doSthAfterChangePaintSize(View.VISIBLE, View.INVISIBLE,
                // View.INVISIBLE);
                //
                // ivMenu.setVisibility(View.VISIBLE);
                // tvExit.setVisibility(View.VISIBLE);
                //
                // iv.setVisibility(View.VISIBLE);
                // sfvBoard.setVisibility(View.VISIBLE);
                //
                // sfvBoard.setPaintSize(5);
                // break;
                // case R.id.fl_middle_paint_layout_v_draw_menu:// 中等线条
                // doSthAfterChangePaintSize(View.INVISIBLE, View.VISIBLE,
                // View.INVISIBLE);
                //
                // ivMenu.setVisibility(View.VISIBLE);
                // tvExit.setVisibility(View.VISIBLE);
                //
                // iv.setVisibility(View.VISIBLE);
                // sfvBoard.setVisibility(View.VISIBLE);
                //
                // sfvBoard.setPaintSize(15);
                // break;

                // case R.id.fl_large_paint_layout_v_draw_menu:// 粗线
                // doSthAfterChangePaintSize(View.INVISIBLE, View.INVISIBLE,
                // View.VISIBLE);
                //
                // ivMenu.setVisibility(View.VISIBLE);
                // tvExit.setVisibility(View.VISIBLE);
                //
                // iv.setVisibility(View.VISIBLE);
                // sfvBoard.setVisibility(View.VISIBLE);
                //
                // sfvBoard.setPaintSize(30);
                // break;
            }
        }
    }

}
