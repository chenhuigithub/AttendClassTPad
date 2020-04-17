package com.example.attendclasstpad.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;
import android.widget.ExpandableListView;

import com.example.attendclasstpad.view.CustomDialog;

/**
 * 布局工具
 *
 * @author chenhui 2019.01.04
 */
public class ViewUtils {
    private Context context;
    private Dialog dialog;
    private boolean canShowDialog = true;//是否可以显示dialog(dialog依赖于Activity，在activity类中对此灵活控制)


    public ViewUtils(Context context) {
        this.context = context;
    }

    public boolean isCanShowDialog() {
        return canShowDialog;
    }

    /**
     * 设置是否可以显示dialog的值(dialog依赖于Activity，在activity类中onPause等对此灵活控制)
     *
     * @param canShowDialog 是否可以显示dialog
     */
    public void setCanShowDialog(boolean canShowDialog) {
        this.canShowDialog = canShowDialog;
    }


    /**
     * 加载框
     *
     * @param tip 提示信息
     */
    public void showLoadingDialog(String tip) {
        // 设置dialog提示框
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        if (ValidateFormatUtils.isEmpty(tip)) {
            tip = "正在加载...";
        }
        builder.setMessage(tip);
        dialog = builder.createForLoading();
        dialog.show();
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    /**
     * 自定义窗口
     *
     * @param width     窗口宽度（int值，例如WindowManager.LayoutParams.WRAP_CONTENT或 CustomApplication.Screen_Width / 5 * 4）
     * @param height    窗口高度（int值，例如WindowManager.LayoutParams.WRAP_CONTENT或 CustomApplication.Screen_Width / 5 * 4 ）
     * @param alpha     透明度（float值，例如1.0f）
     * @param dimAmount 黑暗度（float值，例如0.0f）
     */
    public void setCustomWindow(int width, int height, float alpha, float dimAmount) {
        if (context instanceof Activity) {
            WindowManager.LayoutParams params = ((Activity) context).getWindow().getAttributes(); //获取对话框当前的参数值
            params.width = width;//宽度设置
            params.height = height;//高度设置
            params.alpha = alpha;//设置本身透明度
            params.dimAmount = dimAmount;//设置黑暗度
            ((Activity) context).getWindow().setAttributes(params);
        }
    }

    /**
     * ExpandableListView设置停留位置
     *
     * @param groupPos
     * @param childPos
     * @return
     */
    public static int getFlatListPosition(ExpandableListView lv, int groupPos, int childPos) {
        long packetPosition = ExpandableListView.getPackedPositionForChild(groupPos, childPos);
        return lv.getFlatListPosition(packetPosition);
    }
}
