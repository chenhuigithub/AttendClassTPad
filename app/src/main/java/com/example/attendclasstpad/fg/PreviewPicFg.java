package com.example.attendclasstpad.fg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.adapter.ImageGdvAdapter01;
import com.example.attendclasstpad.adapter.TestExpandableListAdapter;
import com.example.attendclasstpad.aty.PicViewActivity;
import com.example.attendclasstpad.callback.InterfacesCallback;
import com.example.attendclasstpad.model.Picture;
import com.example.attendclasstpad.util.ConstantsUtils;
import com.example.attendclasstpad.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片预览
 */
@SuppressLint("ValidFragment")
public class PreviewPicFg extends Fragment implements InterfacesCallback.ICanKnowSth6 {
    private List<Picture> picList;
    private ViewUtils vUtils;// 布局工具
    private InterfacesCallback.ICanKnowSth7 callback;
    private int picShowType = 0;//图片展示状态（0:浏览状态，点击小图查看大图；1:选中状态，点击小图选中图片）
    private List<String> picIDChoicedList;//右侧选中的图片ID列表

    private Resources res;
    private ImageGdvAdapter01 picAdapter;// 图片网格适配器
    private Handler uiHandler;
    private TestExpandableListAdapter testAdapter;

    private ImageView ivNoData;
    private GridView gdvPic;

    public PreviewPicFg() {
    }

    public PreviewPicFg(List<Picture> list) {
        this.picList = list;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InterfacesCallback.ICanKnowSth7) {
            callback = (InterfacesCallback.ICanKnowSth7) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View allFgView = inflater.inflate(R.layout.layout_v_preview_pic, null);

        vUtils = new ViewUtils(getActivity());
        uiHandler = new Handler(getActivity().getMainLooper());
        picIDChoicedList = new ArrayList<String>();

        res = getResources();

        ivNoData = (ImageView) allFgView.findViewById(R.id.iv_no_data01_layout_v_preview_pic);
        // 图片
        gdvPic = (GridView) allFgView
                .findViewById(R.id.gdv_pic_layout_fg_v_preview_pic);
        setGridPicAdapter(false, false);
        setGdvPicListener();
        return allFgView;
    }


    /**
     * 设置右侧图片布局
     */
    private void setGridPicAdapter(boolean isShowChoiceMenu, boolean isChoiceAll) {
        if (picAdapter == null) {
            picAdapter = new ImageGdvAdapter01(getActivity(), picList);
            picAdapter.setShowChoiceMenu(isShowChoiceMenu);
            picAdapter.setChoiceAll(isChoiceAll);
            gdvPic.setAdapter(picAdapter);
        } else {
            picAdapter.setShowChoiceMenu(isShowChoiceMenu);
            picAdapter.setChoiceAll(isChoiceAll);
            picAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 图片网格布局单项监听
     */
    private void setGdvPicListener() {
        gdvPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos,
                                    long id) {
                switch (picShowType) {
                    case 0://点击查看大图
                        if (picList.size() > 0
                        ) {
                            // 大图url
                            String thumbPath = picList.get(pos)
                                    .getPath();

                            Intent intent = new Intent(getActivity(),
                                    PicViewActivity.class);
                            intent.putExtra(ConstantsUtils.PATH, thumbPath);
                            startActivity(intent);
                        }

                        break;
                    case 1://点击选中
                        choicePic(v, pos);

                        break;
                }
            }
        });

        gdvPic.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                return false;
            }
        });
    }

    /**
     * 选图状态下，点击图片事件
     *
     * @param v        点击布局
     * @param position 数据位置
     */
    private void choicePic(View v, final int position) {
        final TextView tvChoice = (TextView) v.findViewById(R.id.tv_choice_layout_v_image);
        tvChoice.setVisibility(View.VISIBLE);
        tvChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvChoice.isSelected()) {
                    tvChoice.setSelected(false);

                    removeFromPicIDChoicedList(position);
                } else {
                    tvChoice.setSelected(true);

                    addPicIDToChoicedList(position);
                }
            }
        });

        tvChoice.performClick();
    }


    /**
     * 将图片ID移出选中图ID数组
     *
     * @param position 需要移出的数据位置
     */
    private void removeFromPicIDChoicedList(int position) {
        Picture pic = picList.get(position);
        if (pic != null) {
            String id = pic.getID();

            List<String> idList = picIDChoicedList;
            for (int i = 1; i <= picIDChoicedList.size(); i++) {
                String idItem = picIDChoicedList.get(i - 1);
                if (id.equals(idItem)) {
                    idList.remove(i - 1);
                }
            }
            picIDChoicedList = idList;
        }

        if (picIDChoicedList.size() < getPicSize()) {
//            llDownload.setVisibility(View.INVISIBLE);
//            tvChoiceAll.setText(res.getString(R.string.choice_all));
        }
    }

    /**
     * 将图片ID加入选中图ID数组
     *
     * @param position 数据位置
     */
    private void addPicIDToChoicedList(int position) {
        Picture pic = picList.get(position);
        if (pic != null) {
            String id = pic.getID();
            if (!TextUtils.isEmpty(id)) {
                picIDChoicedList.add(id);
            }
        }

        if (getPicSize() == picIDChoicedList.size()) {
//            llDownload.setVisibility(View.VISIBLE);
//            tvChoiceAll.setText(res.getString(R.string.cancel_choice_all));
        }
    }

    /**
     * 获取图片个数
     *
     * @return 图片个数
     */
    private int getPicSize() {
        int picCount = 0;
        for (Picture pic : picList) {
            if (pic != null) {
                String id = pic.getID();
                String path = pic.getPath();

                if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(path)) {
                    picCount++;
                }
            }
        }

        return picCount;
    }


    @Override
    public void doSth(Picture pic) {

    }
}
