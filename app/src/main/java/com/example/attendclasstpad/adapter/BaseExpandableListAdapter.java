package com.example.attendclasstpad.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.attendclasstpad.callback.OnGroupExpandedListener;
import com.example.attendclasstpad.model.OmicsBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 可伸缩ListView适配器
 */
public abstract class BaseExpandableListAdapter<T, S> extends android.widget.BaseExpandableListAdapter {
    private static final String TAG = "NormalExpandableListAda";
    private OnGroupExpandedListener mOnGroupExpandedListener;
    // 一级页面
    protected List<T> parentList;
    // 创建child数据的二维数组
    OmicsBean.DataBean.CategoryListBean[][] childArray;

    private Map<T, List<S>> dataset;

    protected int gPos;// 父级位置，即groupPosition
    protected int cPos;// 子级位置，即childPosition

    private HashMap<Object, Object> posCurrMap;//当前位置

    private Resources res;// 资源工具

    @Deprecated
    private Map<Integer, Integer> itemExpandType = null;// 展开状态

    public Integer expandType = 0;// 展开状态（默认状态）
    public Integer putAwayType = 1;// 收回状态

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            notifyDataSetChanged();//更新数据
            super.handleMessage(msg);
        }
    };

    /**
     * 构造方法
     *
     * @param groupList 父级（一级）目录集合
     * @param dataset   map<父级,子集集合>
     */
    public BaseExpandableListAdapter(Context context,
                                     List<T> groupList,
                                     Map<T, List<S>> dataset) {
        this.parentList = groupList;
        this.dataset = dataset;

        itemExpandType = new HashMap<Integer, Integer>();
        posCurrMap = new HashMap<Object, Object>();

        res = context.getResources();
    }

    /**
     * 设置位置
     *
     * @param gPos 父级位置
     * @param cPos 子级位置
     */
    public void setPosition(int gPos, int cPos) {
        this.gPos = gPos;
        this.cPos = cPos;

        if (posCurrMap.size() > 0) {
            posCurrMap.clear();
        }
        this.posCurrMap.put(gPos, cPos);
    }

    /**
     * 获取当前位置
     *
     * @return
     */
    public HashMap<Object, Object> getPosition() {
        return posCurrMap;
    }

    /**
     * 获取父级位置
     */
    public int getGPosition() {
        return this.gPos;
    }

    /**
     * 获取子级位置
     */
    public int getCPosition() {
        return this.cPos;
    }

    public void setOnGroupExpandedListener(
            OnGroupExpandedListener onGroupExpandedListener) {
        mOnGroupExpandedListener = onGroupExpandedListener;
    }

    /**
     * @param pos
     * @param type
     * @deprecated
     */
    public void setExpandType(int pos, int type) {
        itemExpandType.put(pos, type);
    }

    /**
     * 是否展开状态
     *
     * @param pos
     * @return
     * @deprecated
     */
    public boolean isExpandType(int pos) {
        Integer type = itemExpandType.get(pos);
        if (type == null || expandType == type) {
            return true;
        } else {
            return false;
        }
    }

    /*供外界更新数据的方法*/
    public void refresh(ExpandableListView expandableListView, int groupPosition) {
        handler.sendMessage(new Message());
        //必须重新伸缩之后才能更新数据
        expandableListView.collapseGroup(groupPosition);
        expandableListView.expandGroup(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return dataset.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return dataset.get(parentList.get(groupPosition)).size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return dataset.get(parentList.get(groupPosition));
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return dataset.get(parentList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 子类实现该方法返回layout资源ID
     *
     * @return
     */
    protected abstract int getGroupLayoutResID();

    /**
     * 子类实现该方法返回layout资源ID
     *
     * @return
     */
    protected abstract int getChildLayoutResID();

    /**
     * 子类对父级数据进行赋值
     *
     * @param isExpanded 当前此条是否被展开
     * @param resultView 布局
     * @param dataObj    数据
     */
    protected abstract void doAssignValueForGroupView(boolean isExpanded, View resultView, Object dataObj);

    /**
     * 子级位置变换
     */
    protected List<S> indexExChange(int gPos, int cPos1, int cPos2) {
        //先获取子级列表
        List<S> list = (List<S>) dataset.get(parentList.get(gPos));
        S t = list.get(cPos1);
        list.set(cPos1, list.get(cPos2));
        list.set(cPos2, t);
        return list;
    }

    /**
     * 子级中移除某项
     */
    protected List<S> removeItem(int gPos, int cPos) {
        //先获取子级列表
        List<S> list = (List<S>) dataset.get(parentList.get(gPos));
        list.remove(cPos);
        return list;
    }


    /**
     * 子类对子级数据进行赋值
     *
     * @param groupPosition 所属父级位置
     * @param childPosition 当前子级位置
     * @param isLastChild   是否是最后一条数据
     * @param resultView    布局
     * @param dataObj       数据
     */
    protected abstract void doAssignValueForChildView(int groupPosition, int childPosition, boolean isLastChild, View resultView, Object dataObj);

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        View resultView = convertView;
        if (resultView == null) {
            resultView = LayoutInflater.from(parent.getContext()).inflate(
                    getGroupLayoutResID(), parent, false);

            T dataObj = parentList.get(groupPosition);
            if ((resultView != null) && (dataObj != null)) {
                doAssignValueForGroupView(isExpanded, resultView, dataObj);
            }
        }
        return resultView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        View resultView = convertView;
        if (resultView == null) {
            resultView = LayoutInflater.from(parent.getContext()).inflate(
                    getChildLayoutResID(), parent, false);
            List<S> childList = dataset.get(parentList.get(groupPosition));
            S dataObj = childList.get(childPosition);
            if ((resultView != null) && (dataObj != null)) {
                doAssignValueForChildView(groupPosition, childPosition, isLastChild, resultView, dataObj);
            }

        }
        return resultView;
    }

    /**
     * 选中状态
     *
     * @param childViewHolder
     */
//    private void setFocused(ChildViewHolder childViewHolder) {
    // childViewHolder.tvTitle.setBackgroundColor(res
    // .getColor(R.color.green01));
    // childViewHolder.tvTitle.setTextColor(res.getColor(R.color.clog));

//        childViewHolder.ivSign.setSelected(true);
//    }

    /**
     * 正常状态
     */
//    private void setNormal(ChildViewHolder childViewHolder) {
    // childViewHolder.tvTitle.setBackgroundColor(res.getColor(R.color.white));
    // childViewHolder.tvTitle.setTextColor(res
    // .getColor(R.color.color_text_content));

//        childViewHolder.ivSign.setSelected(false);
//    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        Log.d(TAG, "onGroupExpanded() called with: groupPosition = ["
                + groupPosition + "]");
        if (mOnGroupExpandedListener != null) {
            mOnGroupExpandedListener.onGroupExpanded(groupPosition);
        }
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        Log.d(TAG, "onGroupCollapsed() called with: groupPosition = ["
                + groupPosition + "]");
    }


}
