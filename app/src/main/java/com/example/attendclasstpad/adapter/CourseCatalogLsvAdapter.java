package com.example.attendclasstpad.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.model.Course;

/**
 * 课程列表适配器
 *
 * @author zhaochenhui 2018.05.17
 */

public class CourseCatalogLsvAdapter extends BaseListAdapter<Course> {
    protected int currentPosition = -1;// 上次选中的位置，首次为空，默认为-1

    private String calatogCurr = "沁园春-长沙";// 当前目录名称
    private String catalogIDCurr;// 当前目录ID
    private String unitIDCurr;// 当前单元ID

    private Context context;
    List<Course> dataList;

    private CatalogGdvAdapter catalogGdvAdapter;// 目录适配器

    public CourseCatalogLsvAdapter(Context context, List<Course> dataList,
                                   String unitIDCurr, String catalogIDCurr) {
        super(context, dataList);

        this.context = context;
        this.dataList = dataList;

        this.unitIDCurr = catalogIDCurr;
        this.catalogIDCurr = catalogIDCurr;
    }

    /**
     * 设置位置
     */
    public void setCurrentPosition(int pos) {
        this.currentPosition = pos;
    }

    /**
     * 设置当前单元ID
     */
    public void setCurrentUnit(String id) {
        this.unitIDCurr = id;
    }

    /**
     * 设置当前目录ID
     */
    public void setCurrentCatalogID(String id) {
        this.catalogIDCurr = id;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.layout_adapter_item_for_course;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void doAssignValueForView(int position, View resultView,
                                        Course dataObj) {
        // 单元
        LinearLayout llAll = (LinearLayout) resultView
                .findViewById(R.id.ll_all_layout_adapter_item_for_course);
        llAll.setEnabled(false);
        llAll.setFocusable(false);
        llAll.setClickable(false);

        // 单元
        TextView tvUnit = (TextView) resultView
                .findViewById(R.id.tv_unit_layout_adapter_item_for_course);
        tvUnit.setText(dataObj.getUnitName());

        // 目录
        GridView tvCatalogName = (GridView) resultView
                .findViewById(R.id.gdv_catalog_layout_adapter_item_for_course);

        Course course = dataList.get(position);
        if (course != null) {
            catalogGdvAdapter = new CatalogGdvAdapter(context,
                    course.getLessonList(), position, unitIDCurr, catalogIDCurr);
            tvCatalogName.setAdapter(catalogGdvAdapter);
        }

        catalogGdvAdapter.setCurrentPosition(currentPosition);
    }
}
