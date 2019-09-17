package com.example.attendclasstpad.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.callback.ActivityFgInterface;
import com.example.attendclasstpad.model.Test;
import com.example.attendclasstpad.model.TestPaper;

import java.util.List;

/**
 * 试卷列表适配器
 *
 * @author chenhui 2019.09.05
 */
public class TestPaperAdapter extends BaseListAdapter<TestPaper> {
    private ActivityFgInterface.ICanKnowFgDoSthAboutMenu iCan;
    private boolean showCbox;//是否选中


    public TestPaperAdapter(Context context, ActivityFgInterface.ICanKnowFgDoSthAboutMenu ican, List<TestPaper> dataList) {
        super(context, dataList);

        this.iCan = ican;
    }

    /**
     * 是否显示复选框
     *
     * @param showCbox
     */
    public void setIfShowCbox(boolean showCbox) {
        this.showCbox = showCbox;
    }


    /**
     * 返回是否显示复选框的boolean值
     */
    public boolean getShowCbox() {
        return showCbox;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.layout_v_test_paper_item;
    }

    @Override
    protected void doAssignValueForView(final int position, View resultView, TestPaper dataObj) {
        RelativeLayout rlAll = (RelativeLayout) resultView.findViewById(R.id.rl_wrapper_all_layout_v_test_paper_item);

        //试卷名称
        TextView tvName = (TextView) resultView.findViewById(R.id.tv_name_layout_test_paper_item);
        tvName.setText(dataObj.getName());

        CheckBox cBox = (CheckBox) resultView.findViewById(R.id.cbox_layout_v_test_paper_item);
        if (showCbox) {
            cBox.setVisibility(View.VISIBLE);

            if (dataObj.isChoiced()) {
                cBox.setChecked(true);
                rlAll.setBackgroundResource(R.color.light_grey);
            } else {
                cBox.setChecked(false);
                rlAll.setBackgroundResource(R.color.white);
            }
        } else {
            cBox.setVisibility(View.GONE);
            rlAll.setBackgroundResource(R.color.white);
        }

        //试卷状态
        TextView tvType = (TextView) resultView.findViewById(R.id.tv_type_layout_v_test_paper_item);

        //查看报告
        TextView tvSeeReport = (TextView) resultView.findViewById(R.id.tv_see_report_layout_v_test_paper_item);
        tvSeeReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iCan != null) {
                    iCan.doOnAfterClickMenu(0, position);
                }
            }
        });

        if ("0".equals(dataObj.getType())) {
            tvType.setText("未布置");
            tvSeeReport.setVisibility(View.INVISIBLE);
        } else if ("1".equals(dataObj.getType())) {
            tvType.setText("答题中");
            tvSeeReport.setVisibility(View.VISIBLE);
        } else if ("2".equals(dataObj.getType())) {
            tvType.setText("待批阅");
            tvSeeReport.setVisibility(View.VISIBLE);
        }

    }
}
