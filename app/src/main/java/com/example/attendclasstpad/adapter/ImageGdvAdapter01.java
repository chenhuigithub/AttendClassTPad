package com.example.attendclasstpad.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.attendclasstpad.R;
import com.example.attendclasstpad.model.Picture;

import java.util.List;

/**
 * 图片网格适配器
 *
 * @author chenhui 2019.03.27
 */
public class ImageGdvAdapter01 extends BaseListAdapter<Picture> {
    List<Picture> dataList;
    private Context context;
    private boolean isShowChoiceMenu = false;//是否显示选择按钮（复选框），默认不显示
    private boolean isChoiceAll;//是否全选（复选框）

    public ImageGdvAdapter01(Context context, List<Picture> dataList) {
        super(context, dataList);

        this.context = context;
        this.dataList = dataList;
    }


    public boolean isShowChoiceMenu() {
        return isShowChoiceMenu;
    }

    public void setShowChoiceMenu(boolean showChoiceMenu) {
        isShowChoiceMenu = showChoiceMenu;
    }

    public void setChoiceAll(boolean choiceAll) {
        isChoiceAll = choiceAll;
    }

    public boolean isChoiceAll() {
        return isChoiceAll;
    }


    @Override
    protected int getLayoutResID() {
        return R.layout.layout_v_image;
    }

    @Override
    protected void doAssignValueForView(int position, View resultView,
                                        Picture pic) {
        TextView tvChoice = (TextView) resultView.findViewById(R.id.tv_choice_layout_v_image);
        ImageView iv = (ImageView) resultView
                .findViewById(R.id.iv_layout_v_image);

        // 取出图片url
        String id = "";//图片ID
        String path = "";//图片路径
        if (pic != null) {
            id = pic.getID();
            path = pic.getPath();
            if (TextUtils.isEmpty(path)) {
                path = pic.getThumbPath();
            }
        }

        if (isShowChoiceMenu && !TextUtils.isEmpty(id) && !TextUtils.isEmpty(path)) {
            tvChoice.setVisibility(View.VISIBLE);

            if (isChoiceAll) {
                tvChoice.setSelected(true);
            } else {
                tvChoice.setSelected(false);
            }
        } else {
            tvChoice.setVisibility(View.GONE);
        }
//        设置图片显示
        Glide.with(context).load(path).into(iv);
    }
}
