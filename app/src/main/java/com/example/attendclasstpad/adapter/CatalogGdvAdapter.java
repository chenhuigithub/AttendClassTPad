package com.example.attendclasstpad.adapter;

import java.util.List;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.model.Catalog;
import com.example.attendclasstpad.model.Lesson;
import com.example.attendclasstpad.util.ValidateFormatUtils;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

/**
 * 目录适配器
 *
 * @author chenhui
 */
public class CatalogGdvAdapter extends BaseListAdapter<Catalog> {
    protected int currentPosition = -1;// 上次选中的位置，首次为空，默认为-1

    private String calatogCurr = "沁园春-长沙";// 当前目录名称
    private Resources res;// 资源工具

    private int unitPos = 0;// 单元位置

    private CatalogGdvAdapter catalogGdvAdapter;// 目录适配器

    String unitIDCurr = "";
    String catalogIDCurr = "";

    /**
     * 构造方法
     *
     * @param context
     * @param dataList      目录列表
     * @param unitPos       单元ID
     * @param catalogIDCurr 初始化时默认选中的目录ID
     */
    public CatalogGdvAdapter(Context context, List<Catalog> dataList,
                             int unitPos, String unitIDCurr, String catalogIDCurr) {
        super(context, dataList);

        res = context.getResources();
        this.unitPos = unitPos;
        this.unitIDCurr = unitIDCurr;
        this.catalogIDCurr = catalogIDCurr;

    }

    /**
     * 设置位置
     */
    public void setCurrentPosition(int pos) {
        this.currentPosition = pos;
    }

    public void setCatalogIDCurr(String catalog) {
        this.calatogCurr = catalog;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.layout_v_item_for_catalog;
    }

    @Override
    protected void doAssignValueForView(int position, View resultView,
                                        Catalog dataObj) {
        // 目录
        TextView tvCatalogName = (TextView) resultView
                .findViewById(R.id.tv_name_layout_adapter_item_for_course);
        tvCatalogName.setText("\n" + dataObj.getName() + "\n");

        tvCatalogName.setTag(dataObj);

        // 目录ID
        String catalogID = dataObj.getId();
        // if (ValidateFormatUtils.isEmpty(catalogIDCurr) && currentPosition !=
        // -1
        // && unitPos == 0 && currentPosition == position) {
        // tvCatalogName
        // .setBackgroundColor(res.getColor(R.color.light_blue06));
        // } else {
        // if (catalogID.equals(catalogIDCurr)) {
        // tvCatalogName.setBackgroundColor(res
        // .getColor(R.color.light_blue06));
        // } else {
        // tvCatalogName.setBackgroundColor(res.getColor(R.color.white));
        // }
        // }

        if (ValidateFormatUtils.isEmpty(catalogIDCurr)) {
            if (unitPos == 0 && position == 0) {
                tvCatalogName.setBackgroundColor(res
                        .getColor(R.color.light_blue06));
            } else {
                tvCatalogName.setBackgroundColor(res.getColor(R.color.white));
            }
        } else {
            if (!ValidateFormatUtils.isEmpty(catalogID)
                    && catalogID.equals(catalogIDCurr)) {
                tvCatalogName.setBackgroundColor(res
                        .getColor(R.color.light_blue06));
            } else {
                tvCatalogName.setBackgroundColor(res.getColor(R.color.white));
            }
        }

    }
}
