package com.example.attendclasstpad.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.attendclasstpad.R;
import com.example.attendclasstpad.model.Student;
import com.example.attendclasstpad.util.ValidateFormatUtils;

import java.util.List;

/**
 * 学生数据GridView适配器
 *
 * @author chenhui 2020.05.12
 */
public class StudentGdvAdapter extends BaseListAdapter<Student> {


    public StudentGdvAdapter(Context context, List<Student> dataList) {
        super(context, dataList);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.layout_v_student;
    }

    @Override
    protected void doAssignValueForView(int position, View resultView, Student dataObj) {
        TextView tvName = (TextView) resultView.findViewById(R.id.tv_name_layout_v_student);
        ImageView ivLogo = (ImageView) resultView.findViewById(R.id.on_ln);
        ImageView ivLock = (ImageView) resultView.findViewById(R.id.img_lv);

        //学生姓名
        String name = dataObj.getName();
        if (!ValidateFormatUtils.isEmpty(name)) {
            tvName.setText(name);
        } else {
            tvName.setText("学生");
        }

        //学生头像
        String logoPath = dataObj.getLogo();
        Glide.with(context).load(logoPath).error(R.drawable.user_logo).into(ivLogo);


        //是否被锁定
        boolean isLocked = dataObj.isIslock();
        if (isLocked) {
            ivLock.setVisibility(View.VISIBLE);
        } else {
            ivLock.setVisibility(View.GONE);
        }

    }
}
