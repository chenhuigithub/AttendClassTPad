package com.example.attendclasstpad.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.attendclasstpad.R;

/**
 * 自定义dialog
 */
public class CustomDialog extends Dialog {
    private Context context;

    public CustomDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public static class Builder {
        private Context context;
        private String title;
        private String content;
        private String positiveBtnText;
        private String negativeBtnText;
        private View contentView;
        private OnClickListener positiveBtnClickListener;
        private OnClickListener negativeBtnClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String content) {
            this.content = content;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.content = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveBtnText = (String) context.getText(positiveButtonText);
            this.positiveBtnClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveBtnText = positiveButtonText;
            this.positiveBtnClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeBtnText = (String) context.getText(negativeButtonText);
            this.negativeBtnClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeBtnText = negativeButtonText;
            this.negativeBtnClickListener = listener;
            return this;
        }

        /**
         * 对话框
         *
         * @return
         */
        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 初始化自定义dialog
            final CustomDialog dialog = new CustomDialog(context,
                    R.style.Dialog);
            View view = inflater.inflate(R.layout.layout_aty_custom_dialog, null);
            dialog.addContentView(view, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            // 设置标题
            TextView tvTitle = (TextView) view
                    .findViewById(R.id.tv_title_layout_activity_custom_dialog);
            tvTitle.setText(title);

            // 设置positive按钮
            Button btnPositive = (Button) view
                    .findViewById(R.id.btn_positive_layout_activity_custom_dialog);
            if (positiveBtnText != null) {
                btnPositive.setText(positiveBtnText);
                if (positiveBtnClickListener != null) {
                    btnPositive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            positiveBtnClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            } else {// 如果没有设置positive内容，则默认将positive按钮设置为隐藏状态
                btnPositive.setVisibility(View.GONE);
            }

            // 设置negative按钮
            Button btnNegative = (Button) view
                    .findViewById(R.id.btn_negative_layout_activity_custom_dialog);
            if (negativeBtnText != null) {
                btnNegative.setText(negativeBtnText);
                if (negativeBtnClickListener != null) {
                    btnNegative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            negativeBtnClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            } else {// 如果没有设置negative内容，则默认将negative按钮设置为隐藏状态
                btnNegative.setVisibility(View.GONE);
            }

            // 设置内容
            if (content != null) {
                TextView tvContent = (TextView) view
                        .findViewById(R.id.tv_content_layout_activity_custom_dialog);
                tvContent.setText(content);
            } else if (contentView != null) {
                RelativeLayout llWrapperContent = (RelativeLayout) view
                        .findViewById(R.id.rl_wrapper_content_layout_activity_custom_dialog);
                llWrapperContent.removeAllViews();
                llWrapperContent.addView(contentView, new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }

            dialog.setContentView(view);
            // 当点击dialog之外的区域时，弹框不会消失。防误触。
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }

        /**
         * 提示框
         *
         * @return
         */
        public CustomDialog createForLoading() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_dialog_for_loading,
                    null);

            CustomDialog dialog = new CustomDialog(context,
                    R.style.DialogForLoading);
            dialog.addContentView(view, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            // 设置内容
            TextView tvContent = (TextView) view
                    .findViewById(R.id.tv_tip_layout_dialog_for_loading);
            if (content != null) {
                tvContent.setText(content);
            } else {
                tvContent.setVisibility(View.GONE);
            }
            dialog.setContentView(view);
            // 当点击dialog之外的区域时，弹框不会消失。防误触。
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }

    /**
     * 设置底部弹出框
     *
     * @param playOutdirection 弹框位置（例如：Gravity.BOTTOM）
     * @param animStyle        动画效果style文件，传值0时设置默认样式
     * @return
     */
    public AlertDialog createFromBottomEdge(int playOutdirection, int animStyle) {
        if (0 == animStyle) {
            animStyle = R.style.style_custom_dialog;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog mAlertDialog = builder.create();
        Window window = mAlertDialog.getWindow();
        window.setGravity(playOutdirection); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(animStyle); // 添加动画

        WindowManager.LayoutParams params = window.getAttributes();
        // 是否获取焦点，默认的是FLAG_NOT_FOCUSABLE：不能获取输入焦点,
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(params);

        return mAlertDialog;
    }
}
