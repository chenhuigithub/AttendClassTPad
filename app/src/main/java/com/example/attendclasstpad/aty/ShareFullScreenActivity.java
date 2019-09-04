package com.example.attendclasstpad.aty;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.adapter.ColorAdapter;
import com.example.attendclasstpad.util.ConstantsUtils;
import com.example.attendclasstpad.util.DrawUtils;
import com.example.attendclasstpad.view.BgDarkPopupWindow;
import com.example.attendclasstpad.view.CustomSurfaceView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 共享全屏界面
 *
 * @author zhaochenhui, 2018.05.24
 */
public class ShareFullScreenActivity extends FragmentActivity {
    private int colorCur = 0;// 当前画笔的颜色序号
    private int paintSizeCur = 5;// 当前选中的画笔型号,默认为最小型号
    /**
     * 展示方式标签（画笔、白板、全屏等）
     */
    public static String SHOW_STYLE = "showStyle";
    /**
     * 展示方式（画笔、白板、全屏等）
     */
    private int drawStyle;

    private boolean isRepaint = true;// 是否重画

    private boolean isShowDrawMenu = false;// 是否显示绘制菜单栏,默认为不显示

    private CustomSurfaceView sfvBoard;// 用于绘制的画板
    private LinearLayout llPaint;// 画笔
    private LinearLayout llEraser;// 橡皮
    private LinearLayout llRevoke;// 撤销
    private LinearLayout llRecovery;// 恢复
    private LinearLayout llClearScreen;// 清屏
    private LinearLayout llWhiteBoard;// 白板
    private TextView tvWhiteBoard;// 白板
    private LinearLayout llExit;// 退出全屏
    private ImageView iv;// 课程图片
    private Dialog drawMenuDialog;// 绘制菜单栏
    private LinearLayout llDrawMenu;// 绘制菜单栏
    private ImageView ivMenu;// 右下角菜单按钮

    // 菜单dialog相关

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
    // 当前选中的笔画粗细按钮
    private FrameLayout flFocusPaint;

    // 退出
    private TextView tvExit;

    private BgDarkPopupWindow window;// 弹框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.layout_activity_share_full_screen);
        // 退出
        tvExit = (TextView) findViewById(R.id.tv_exit_layout_activity_share_full_screen);
        tvExit.setOnClickListener(new WidgetsListeners());

        // 课程图片
        iv = (ImageView) findViewById(R.id.iv_layout_adapter_item_for_color);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                drawMenuDialog.show();
            }
        });

        // 画布
        sfvBoard = (CustomSurfaceView) findViewById(R.id.sfv_board_layout_activity_share_full_screen);// 画板
        sfvBoard.setZOrderOnTop(true);
        sfvBoard.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        sfvBoard.setPaintBrush();

        // 右下角菜单按钮
        ivMenu = (ImageView) findViewById(R.id.iv_menu_layout_activity_share_full_screen);
        ivMenu.setOnClickListener(new WidgetsListeners());

        dealWithExtras();
        initDrawMenuDialog();
    }

    /**
     * 处理接收的数据
     */
    public void dealWithExtras() {
        // 获取数据
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        // 展示方式
        drawStyle = bundle.getInt(SHOW_STYLE);
        switch (drawStyle) {
            // case ConstantsUtils.GRAFFITI:// 画笔
            // graffiti();
            // break;
            case ConstantsUtils.WHITE_BOARD:// 白板
                whiteBoard();
                break;
            // case ConstantsUtils.FULL_SCREEN:// 全屏
            // break;
        }
    }

    /**
     * 显示泡泡弹框：绘制时的画笔颜色、粗细的选择器
     */
    @SuppressWarnings("deprecation")
    private void showSpinWindow(int windowViewResID, int w, int h,
                                int distanceW, int distanceH) {
        dismissWindow();

        View view = LayoutInflater.from(this).inflate(windowViewResID, null);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        window = new BgDarkPopupWindow(this, view,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // window.setDarkStyle(R.style.MyDarkStyle);
        // window.setDarkColor(Color.parseColor("#a0000000"));
        // window.resetDarkPosition();

        // 设置弹框宽度
        window.setWidth(w);
        // 设置弹框高度
        window.setHeight(h);

        window.darkAbove(llDrawMenu);
        window.showAsDropDown(llDrawMenu, distanceW, distanceH);
        // 点击空白处弹框消失
        // window.setOutsideTouchable(true);
        // window.setFocusable(true);

        // 这句对于控制弹框消失的功能来说很重要，对背景不会有影响
        window.setBackgroundDrawable(new BitmapDrawable());

        window.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                // setPaint1(paintSizeCur);
            }
        });

        initPaintSizeMenu(view);

        if (ConstantsUtils.GRAFFITI == drawStyle) {
            initColorArea(view);
        }
    }

    /**
     * 初始化底部绘制菜单
     */
    private void initDrawMenuDialog() {
        // final View drawaMenuDialog = LayoutInflater.from(this).inflate(
        // R.layout.layout_v_draw_menu, null);

        drawMenuDialog = new Dialog(this, R.style.style_draw_menu);
        drawMenuDialog.setContentView(R.layout.layout_v_draw_menu);

        llDrawMenu = (LinearLayout) drawMenuDialog
                .findViewById(R.id.ll_menu_layout_v_draw_menu);

        drawMenuDialog.setCanceledOnTouchOutside(false);
        drawMenuDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                // 弹框消失时显示右上角退出全屏按钮
                // tvExit.setVisibility(View.VISIBLE);

                // ivMenu.setVisibility(View.GONE);
            }
        });

        Window window = drawMenuDialog.getWindow();
        window.setGravity(Gravity.BOTTOM | Gravity.RIGHT); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.style_draw_menu);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        // p.width = (int) ((d.getWidth()/4)*3); // 宽度设置为屏幕的宽度
        p.width = LayoutParams.WRAP_CONTENT;
        window.setAttributes(p);

        // 画笔
        llPaint = (LinearLayout) drawMenuDialog
                .findViewById(R.id.ll_paint_brush_layout_v_draw_menu);
        llPaint.setOnClickListener(new WidgetsListeners());

        // 橡皮
        llEraser = (LinearLayout) drawMenuDialog
                .findViewById(R.id.ll_eraser_layout_v_draw_menu);
        llEraser.setOnClickListener(new WidgetsListeners());

        // 撤销
        llRevoke = (LinearLayout) drawMenuDialog
                .findViewById(R.id.ll_revoke_layout_v_draw_menu);
        llRevoke.setOnClickListener(new WidgetsListeners());

        // 恢复
        llRecovery = (LinearLayout) drawMenuDialog
                .findViewById(R.id.ll_recovery_layout_v_draw_menu);
        llRecovery.setOnClickListener(new WidgetsListeners());

        // 清屏
        llClearScreen = (LinearLayout) drawMenuDialog
                .findViewById(R.id.ll_clear_screen_layout_v_draw_menu);
        llClearScreen.setOnClickListener(new WidgetsListeners());

        // 白板
        llWhiteBoard = (LinearLayout) drawMenuDialog
                .findViewById(R.id.ll_white_board_layout_v_draw_menu);
        llWhiteBoard.setOnClickListener(new WidgetsListeners());

        tvWhiteBoard = (TextView) drawMenuDialog
                .findViewById(R.id.tv_white_board_layout_v_draw_menu);

        // 退出全屏
        llExit = (LinearLayout) drawMenuDialog
                .findViewById(R.id.ll_exit_full_screen02_layout_v_draw_menu);
        llExit.setOnClickListener(new WidgetsListeners());

        // 退出菜单栏
        llExit = (LinearLayout) drawMenuDialog
                .findViewById(R.id.ll_exit_layout_v_draw_menu);
        llExit.setOnClickListener(new WidgetsListeners());
    }

    /**
     * 初始化颜色区域
     *
     * @param view
     */
    private void initColorArea(View view) {
        setPaintSizeMenu(0);

        GridView gdvColor = (GridView) view
                .findViewById(R.id.gdv_color_layout_v_draw_menu);

        gdvColor.setAdapter(new ColorAdapter(this, DrawUtils.colorRes));
        gdvColor.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos,
                                    long id) {
                isRepaint = false;

                // 赋值
                colorCur = pos;
                // 设置粗细按钮
                setPaintSizeMenu(pos);

                setPaint1(paintSizeCur);
            }
        });
    }

    /**
     * 初始化画笔型号按钮（粗细）
     *
     * @param view
     */
    private void initPaintSizeMenu(View view) {
        // 细线(圆形标志)
        flSmallPaint = (FrameLayout) view
                .findViewById(R.id.fl_small_paint_layout_v_draw_menu);
        llSmallPaint = (LinearLayout) view
                .findViewById(R.id.ll_small_paint_layout_v_draw_menu);
        ivSmallPaint = (ImageView) view
                .findViewById(R.id.iv_small_paint_layout_v_draw_menu);
        flSmallPaint.setOnClickListener(new WidgetsListeners());

        // 中等线条(圆形标志)
        flMiddlePaint = (FrameLayout) view
                .findViewById(R.id.fl_middle_paint_layout_v_draw_menu);
        llMiddlePaint = (LinearLayout) view
                .findViewById(R.id.ll_middle_paint_layout_v_draw_menu);
        llMiddlePaint.setVisibility(View.INVISIBLE);
        ivMiddlePaint = (ImageView) view
                .findViewById(R.id.iv_middle_paint_layout_v_draw_menu);
        flMiddlePaint.setOnClickListener(new WidgetsListeners());

        // 粗线(圆形标志)
        flLargePaint = (FrameLayout) view
                .findViewById(R.id.fl_large_paint_layout_v_draw_menu);
        llLargePaint = (LinearLayout) view
                .findViewById(R.id.ll_large_paint_layout_v_draw_menu);
        llLargePaint.setVisibility(View.INVISIBLE);
        ivLargePaint = (ImageView) view
                .findViewById(R.id.iv_large_paint_layout_v_draw_menu);
        flLargePaint.setOnClickListener(new WidgetsListeners());

        if (flFocusPaint == null) {// 默认情况下选中第一个
            flFocusPaint = flSmallPaint;
        }

        if (ConstantsUtils.GRAFFITI == drawStyle) {
            // 设置粗细按钮的颜色显示
            setPaintSizeMenu(colorCur);
        }
    }

    /**
     * 画笔(涂鸦)
     */
    private void setPaint1(float size) {
        if (isRepaint) {
            // 重画
            sfvBoard.resetCanvas();
            // 重置数据
            sfvBoard.resetPathList();
        }

        // 设置画笔粗细
        sfvBoard.setPaintSize(size);

        // 设置画笔颜色
        sfvBoard.setPaintColor(DrawUtils.colorRes[colorCur]);

        // 画笔其它属性
        if (ConstantsUtils.GRAFFITI == drawStyle) {
            sfvBoard.setPaintBrush();
        } else if (ConstantsUtils.ERASER == drawStyle) {
            sfvBoard.setEraser();
        }

        sfvBoard.setVisibility(View.VISIBLE);
    }

    /**
     * 白板
     */
    private void whiteBoard() {
        // 重画
        sfvBoard.resetCanvas();
        // 重置数据
        sfvBoard.resetPathList();

        drawStyle = ConstantsUtils.WHITE_BOARD;
        // }

        sfvBoard.setPaintBrush();

        iv.setVisibility(View.GONE);
        sfvBoard.setVisibility(View.VISIBLE);
    }

    /**
     * 设置粗细按钮的颜色
     *
     * @param res 样式资源
     */
    private void setPaintSizeMenuColor(int res) {
        ivSmallPaint.setBackgroundResource(res);
        ivMiddlePaint.setBackgroundResource(res);
        ivLargePaint.setBackgroundResource(res);
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

        ShareFullScreenActivity.this.finish();
    }

    /**
     * 设置粗细按钮（顺序不可打乱，与colorRes数组相对应）
     *
     * @param pos 位置
     */
    @SuppressLint("NewApi")
    private void setPaintSizeMenu(int pos) {
        switch (pos) {
            case 0:// 红色
                setPaintSizeMenuColor(R.drawable.selector_for_red_disc);
                break;
            case 1:// 蓝色
                setPaintSizeMenuColor(R.drawable.selector_for_blue_disc);
                break;
            case 2:// 白色
                setPaintSizeMenuColor(R.drawable.selector_for_white_disc);
                break;
            case 3:// 绿色
                setPaintSizeMenuColor(R.drawable.selector_for_green_disc);
                break;
        }
    }

    /**
     * 关闭弹框
     */
    private void dismissWindow() {
        if (window != null && window.isShowing()) {
            window.dismiss();
        }
    }

    /**
     * 显示菜单栏
     */
    private void showDrawMenuDialog() {
        if (drawMenuDialog != null && !drawMenuDialog.isShowing()) {
            drawMenuDialog.show();
        }
    }

    /**
     * 关闭菜单栏
     */
    private void dismissDrawMenuDialog() {
        if (drawMenuDialog != null && drawMenuDialog.isShowing()) {
            drawMenuDialog.dismiss();
        }
    }

    /**
     * 监听
     *
     * @author chenhui
     */
    private class WidgetsListeners implements OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.iv_menu_layout_activity_share_full_screen:// 右下角按钮
                    showDrawMenuDialog();
                    // tvExit.setVisibility(View.VISIBLE);
                    ivMenu.setVisibility(View.GONE);

                    break;
                case R.id.ll_paint_brush_layout_v_draw_menu:// 画笔
                    isRepaint = false;
                    drawStyle = ConstantsUtils.GRAFFITI;

                    showSpinWindow(R.layout.layout_v_draw_paint_selector_popup001,
                            500, 100, 0, 0);

                    flFocusPaint.performClick();

                    // 按钮显示效果
                    llPaint.setBackgroundResource(R.drawable.bg_menu_item_p);
                    llEraser.setBackgroundResource(R.drawable.bg_menu_item);
                    llRevoke.setBackgroundResource(R.drawable.bg_menu_item);
                    llRecovery.setBackgroundResource(R.drawable.bg_menu_item);
                    llClearScreen.setBackgroundResource(R.drawable.bg_menu_item);
                    llWhiteBoard.setBackgroundResource(R.drawable.bg_menu_item);
                    llExit.setBackgroundResource(R.drawable.bg_menu_item);

                    break;
                case R.id.ll_eraser_layout_v_draw_menu:// 橡皮擦
                    isRepaint = false;
                    drawStyle = ConstantsUtils.ERASER;

                    showSpinWindow(R.layout.layout_v_draw_eraser_selector_popup,
                            200, 100,
                            llDrawMenu.getWidth() / 8 + llDrawMenu.getWidth() / 15,
                            0);

                    flFocusPaint.performClick();

                    // 按钮显示效果
                    llPaint.setBackgroundResource(R.drawable.bg_menu_item);
                    llEraser.setBackgroundResource(R.drawable.bg_menu_item_p);
                    llRevoke.setBackgroundResource(R.drawable.bg_menu_item);
                    llRecovery.setBackgroundResource(R.drawable.bg_menu_item);
                    llClearScreen.setBackgroundResource(R.drawable.bg_menu_item);
                    llWhiteBoard.setBackgroundResource(R.drawable.bg_menu_item);
                    llExit.setBackgroundResource(R.drawable.bg_menu_item);

                    break;
                case R.id.ll_revoke_layout_v_draw_menu:// 撤销
                    isRepaint = true;

                    sfvBoard.undo();

                    dismissWindow();

                    // 按钮显示效果
                    llPaint.setBackgroundResource(R.drawable.bg_menu_item);
                    llEraser.setBackgroundResource(R.drawable.bg_menu_item);
                    llRevoke.setBackgroundResource(R.drawable.bg_menu_item_p);
                    llRecovery.setBackgroundResource(R.drawable.bg_menu_item);
                    llClearScreen.setBackgroundResource(R.drawable.bg_menu_item);
                    llWhiteBoard.setBackgroundResource(R.drawable.bg_menu_item);
                    llExit.setBackgroundResource(R.drawable.bg_menu_item);

                    break;
                case R.id.ll_recovery_layout_v_draw_menu:// 恢复
                    sfvBoard.redo();

                    dismissWindow();

                    // 按钮显示效果
                    llPaint.setBackgroundResource(R.drawable.bg_menu_item);
                    llEraser.setBackgroundResource(R.drawable.bg_menu_item);
                    llRevoke.setBackgroundResource(R.drawable.bg_menu_item);
                    llRecovery.setBackgroundResource(R.drawable.bg_menu_item_p);
                    llClearScreen.setBackgroundResource(R.drawable.bg_menu_item);
                    llWhiteBoard.setBackgroundResource(R.drawable.bg_menu_item);
                    llExit.setBackgroundResource(R.drawable.bg_menu_item);

                    break;
                case R.id.ll_clear_screen_layout_v_draw_menu:// 清屏
                    sfvBoard.resetCanvas();
                    sfvBoard.resetPathList();

                    // 按钮显示效果
                    llPaint.setBackgroundResource(R.drawable.bg_menu_item);
                    llEraser.setBackgroundResource(R.drawable.bg_menu_item);
                    llRevoke.setBackgroundResource(R.drawable.bg_menu_item);
                    llRecovery.setBackgroundResource(R.drawable.bg_menu_item);
                    llClearScreen.setBackgroundResource(R.drawable.bg_menu_item_p);
                    llWhiteBoard.setBackgroundResource(R.drawable.bg_menu_item);
                    llExit.setBackgroundResource(R.drawable.bg_menu_item);

                    break;
                case R.id.ll_white_board_layout_v_draw_menu:// 白板
                    dismissWindow();

                    if (drawStyle != ConstantsUtils.WHITE_BOARD) {
                        whiteBoard();
                        tvWhiteBoard.setText("PPT");
                    } else {
                        drawStyle = ConstantsUtils.GRAFFITI;
                        iv.setVisibility(View.VISIBLE);
                        tvWhiteBoard.setText("白板");
                    }

                    // 按钮显示效果
                    llPaint.setBackgroundResource(R.drawable.bg_menu_item);
                    llEraser.setBackgroundResource(R.drawable.bg_menu_item);
                    llRevoke.setBackgroundResource(R.drawable.bg_menu_item);
                    llRecovery.setBackgroundResource(R.drawable.bg_menu_item);
                    llClearScreen.setBackgroundResource(R.drawable.bg_menu_item);
                    llWhiteBoard.setBackgroundResource(R.drawable.bg_menu_item_p);
                    llExit.setBackgroundResource(R.drawable.bg_menu_item);

                    break;
                case R.id.ll_exit_full_screen02_layout_v_draw_menu:// 退出全屏
                    // 按钮显示效果
                    llPaint.setBackgroundResource(R.drawable.bg_menu_item);
                    llEraser.setBackgroundResource(R.drawable.bg_menu_item);
                    llRevoke.setBackgroundResource(R.drawable.bg_menu_item);
                    llRecovery.setBackgroundResource(R.drawable.bg_menu_item);
                    llClearScreen.setBackgroundResource(R.drawable.bg_menu_item);
                    llWhiteBoard.setBackgroundResource(R.drawable.bg_menu_item);
                    llExit.setBackgroundResource(R.drawable.bg_menu_item_p);

                    exit();

                    break;
                case R.id.tv_exit_layout_activity_share_full_screen:// 退出全屏
                    exit();

                    break;
                case R.id.ll_exit_layout_v_draw_menu:// 退出菜单栏
                    dismissDrawMenuDialog();
                    ivMenu.setVisibility(View.VISIBLE);

                    setPaint1(paintSizeCur);

                    break;
                case R.id.fl_small_paint_layout_v_draw_menu:// 细线
                    isRepaint = false;

                    doSthAfterChangePaintSize(View.VISIBLE, View.INVISIBLE,
                            View.INVISIBLE);

                    paintSizeCur = 10;
                    setPaint1(paintSizeCur);

                    if (ConstantsUtils.GRAFFITI == drawStyle) {
                        setPaintSizeMenu(colorCur);
                    }

                    flFocusPaint = flSmallPaint;

                    break;
                case R.id.fl_middle_paint_layout_v_draw_menu:// 中等线条
                    isRepaint = false;

                    doSthAfterChangePaintSize(View.INVISIBLE, View.VISIBLE,
                            View.INVISIBLE);

                    paintSizeCur = 15;
                    setPaint1(paintSizeCur);

                    if (ConstantsUtils.GRAFFITI == drawStyle) {
                        setPaintSizeMenu(colorCur);
                    }

                    flFocusPaint = flMiddlePaint;

                    break;
                case R.id.fl_large_paint_layout_v_draw_menu:// 粗线
                    isRepaint = false;

                    doSthAfterChangePaintSize(View.INVISIBLE, View.INVISIBLE,
                            View.VISIBLE);

                    paintSizeCur = 30;
                    setPaint1(paintSizeCur);

                    if (ConstantsUtils.GRAFFITI == drawStyle) {
                        setPaintSizeMenu(colorCur);
                    }

                    flFocusPaint = flLargePaint;

                    break;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        dismissWindow();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            exit();
        }
        return super.onKeyDown(keyCode, event);
    }
}
