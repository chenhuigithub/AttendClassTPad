package com.example.attendclasstpad.adapter;

import android.content.Context;
import android.view.View;
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

    public TestPaperAdapter(Context context, ActivityFgInterface.ICanKnowFgDoSthAboutMenu ican, List<TestPaper> dataList) {
        super(context, dataList);

        this.iCan = ican;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.layout_v_test_paper_item;
    }

    @Override
    protected void doAssignValueForView(final int position, View resultView, TestPaper dataObj) {
        //试卷名称
        TextView tvName = (TextView) resultView.findViewById(R.id.tv_name_layout_test_paper_item);
        tvName.setText(dataObj.getName());

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
