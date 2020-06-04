package com.example.attendclasstpad.fg;

import java.util.ArrayList;
import java.util.List;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.adapter.PeriodGridAdapter;
import com.example.attendclasstpad.callback.ActivityFgInterface.JumpCallback;
import com.example.attendclasstpad.model.Course;
import com.example.attendclasstpad.model.KeyValue;
import com.example.attendclasstpad.model.Lesson;
import com.example.attendclasstpad.util.Utils;
import com.example.attendclasstpad.util.ValidateFormatUtils;
import com.example.attendclasstpad.util.VariableUtils;
import com.example.attendclasstpad.util.ViewUtils;
import com.example.attendclasstpad.view.CustomGridView01;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 上课
 *
 * @author chenhui
 */
@Deprecated
@SuppressLint("ValidFragment")
public class AttendClassFg extends BaseNotPreLoadFg {
    private JumpCallback callback;

    private boolean isPrepared;// 标志位，标志已经初始化完成
    private boolean hasLoadOnce;// 是否已被加载过一次，第二次就不再去请求数据了

    private KeyValue periodSelected;// 已选学段
    private KeyValue subjectSelected;// 已选科目
    private KeyValue editionSelected;// 已选版本
    private KeyValue moduleSelected;// 已选模块
    private ArrayList<String> ids;// 存放id数据的字符串组合
    private String lastType;// 最后一步点击操作的类型，例如：xd
    private String jsonLastId = "";// 最后一步点击操作的数据id（后台这么设计，木办法。。。）

    private ViewUtils vUtils;// 布局工具

    private View allFgView;// 总布局
    private GridView gdvPeriod;// 学段
    private GridView gdvSubject;// 学科
    //    private GridView gdvEdition;// 版本
//    private GridView gdvModule;// 模块
    private ListView lsvCourseCatalog;// 课程目录

    private CustomGridView01 gdvModule1;// 版本
    private CustomGridView01 gdvEdition1;// 模块

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof JumpCallback) {
            callback = (JumpCallback) activity;
        }
    }


    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (null == allFgView) {
            allFgView = inflater.inflate(R.layout.layout_fg_attend_class, null);

            periodSelected = new KeyValue();
            subjectSelected = new KeyValue();
            editionSelected = new KeyValue();
            moduleSelected = new KeyValue();

            ids = new ArrayList<String>();
            vUtils = new ViewUtils(getActivity());

            gdvPeriod = (GridView) allFgView
                    .findViewById(R.id.gdv_period_layout_fg_attend_class);
            gdvSubject = (GridView) allFgView
                    .findViewById(R.id.gdv_subject_layout_fg_attend_class);

//            gdvEdition = (GridView) allFgView
//                    .findViewById(R.id.gdv_edition_layout_fg_attend_class);
//            gdvModule = (GridView) allFgView
//                    .findViewById(R.id.gdv_module_layout_fg_attend_class);
            gdvModule1 = (CustomGridView01) allFgView.findViewById(R.id.gdv_module1_layout_fg_attend_class);
            gdvEdition1 = (CustomGridView01) allFgView.findViewById(R.id.gdv_edition1_layout_fg_attend_class);

            lsvCourseCatalog = (ListView) allFgView
                    .findViewById(R.id.lsv_catalog_layout_fg_attend_class);
            lsvCourseCatalog.setEnabled(false);
            lsvCourseCatalog.setClickable(false);
            lsvCourseCatalog.setFocusable(false);

            // 学段
            String[] periods = getResources().getStringArray(
                    R.array.arrays_period);
            List<String> periodsList = Utils.getList(periods);
            final PeriodGridAdapter pGdvAdapter = new PeriodGridAdapter(
                    getActivity(), periodsList);
            gdvPeriod.setAdapter(pGdvAdapter);
            pGdvAdapter.setCurrentPosition(0);

            gdvPeriod.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    pGdvAdapter.setCurrentPosition(position);
                    pGdvAdapter.notifyDataSetChanged();
                }
            });

            // 学科
            String[] subjects = getResources().getStringArray(
                    R.array.arrays_junior_middle_school_subject);
            List<String> subjectsList = Utils.getList(subjects);
            final PeriodGridAdapter sGdvAdapter = new PeriodGridAdapter(
                    getActivity(), subjectsList);
            gdvSubject.setAdapter(sGdvAdapter);
            sGdvAdapter.setCurrentPosition(0);

            gdvSubject.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    sGdvAdapter.setCurrentPosition(position);
                    sGdvAdapter.notifyDataSetChanged();
                }
            });

            // 版本
//            String[] editions = getResources().getStringArray(
//                    R.array.arrays_versions);
//            List<String> editionsList = Utils.getList(editions);
//            final PeriodGridAdapter eGdvAdapter = new PeriodGridAdapter(
//                    getActivity(), editionsList);
//            gdvEdition.setAdapter(eGdvAdapter);
//            eGdvAdapter.setCurrentPosition(0);

//            gdvEdition.setOnItemClickListener(new OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View v,
//                                        int position, long id) {
//                    eGdvAdapter.setCurrentPosition(position);
//                    eGdvAdapter.notifyDataSetChanged();
//                }
//            });

            // 模块
//            String[] modules = getResources().getStringArray(
//                    R.array.arrays_module);
//            List<String> modulesList = Utils.getList(modules);
//            final PeriodGridAdapter mGdvAdapter = new PeriodGridAdapter(
//                    getActivity(), modulesList);
//            gdvModule.setAdapter(mGdvAdapter);
//            mGdvAdapter.setCurrentPosition(0);
//
//            gdvModule.setOnItemClickListener(new OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View v,
//                                        int position, long id) {
//                    mGdvAdapter.setCurrentPosition(position);
//                    mGdvAdapter.notifyDataSetChanged();
//                }
//            });


            // 课程目录
            List<Course> courseList = new ArrayList<Course>();

            Course course01 = new Course();
            course01.setUnitName("第一单元");

            List<Lesson> lessonList01 = new ArrayList<Lesson>();
            Lesson lesson01 = new Lesson();
            lesson01.setName("1.沁园春-长沙");

            Lesson lesson02 = new Lesson();
            lesson02.setName("2.诗两首");

            Lesson lesson03 = new Lesson();
            lesson03.setName("3.大堰河——我的保姆");

            lessonList01.add(lesson01);
            lessonList01.add(lesson02);
            lessonList01.add(lesson03);

            course01.setLessonList(lessonList01);
            courseList.add(course01);

            Course course02 = new Course();
            course02.setUnitName("第二单元");

            List<Lesson> lessonList02 = new ArrayList<Lesson>();
            Lesson lesson04 = new Lesson();
            lesson04.setName("4.烛之武退秦师");

            Lesson lesson05 = new Lesson();
            lesson05.setName("5.荆轲刺秦王");

            Lesson lesson06 = new Lesson();
            lesson06.setName("6.鸿门宴");

            lessonList02.add(lesson04);
            lessonList02.add(lesson05);
            lessonList02.add(lesson06);

            course02.setLessonList(lessonList02);
            courseList.add(course02);

            Course course03 = new Course();
            course03.setUnitName("第三单元");

            List<Lesson> lessonList03 = new ArrayList<Lesson>();
            Lesson lesson07 = new Lesson();
            lesson07.setName("7.纪念刘和珍君");

            Lesson lesson08 = new Lesson();
            lesson08.setName("8.小狗包弟");

            Lesson lesson09 = new Lesson();
            lesson09.setName("9.记梁任公先生的一次演讲");

            lessonList03.add(lesson07);
            lessonList03.add(lesson08);
            lessonList03.add(lesson09);

            course03.setLessonList(lessonList03);
            courseList.add(course03);

            Course course04 = new Course();
            course04.setUnitName("第四单元");

            List<Lesson> lessonList04 = new ArrayList<Lesson>();
            Lesson lesson10 = new Lesson();
            lesson10.setName("10.短新闻两篇");

            Lesson lesson11 = new Lesson();
            lesson11.setName("11.包身工");

            Lesson lesson12 = new Lesson();
            lesson12.setName("12.飞向太空的航程");

            lessonList04.add(lesson10);
            lessonList04.add(lesson11);
            lessonList04.add(lesson12);

            course04.setLessonList(lessonList04);
            courseList.add(course04);

//			final CourseCatalogLsvAdapter courseAdapter = new CourseCatalogLsvAdapter(
//					getActivity(), courseList,"",-1);
//			courseAdapter.setCurrentPosition(0);
//			lsvCourseCatalog.setAdapter(courseAdapter);
        }

        // 因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) allFgView.getParent();
        if (parent != null) {
            parent.removeView(allFgView);
        }
        // 标志当前页面可见
        isPrepared = true;
        lazyLoad();

        return allFgView;
    }

    /**
     * 展示版本Gdv布局
     *
     * @param list 数据
     */
    public void showGdvEdition(final List<KeyValue> list, CustomGridView01 gdv) {
        resetGdvLayout(gdv);

        for (int i = 0; i < list.size(); i++) {
            final KeyValue kv = list.get(i);
//            kv.setName("测试模块名");
//            kv.setId("ceshi_edition_id");
//            kv.setChoiced(false);

            View vItem = LayoutInflater.from(getActivity()).inflate(R.layout.layout_v_single_line, null);
            TextView tvName = vItem.findViewById(R.id.tv_layout_v_single_line);
            tvName.setText(list.get(i).getName());
//            tvName.setText("测试模块名");
            gdv.addChild(vItem);

            if (kv != null) {
                if (editionSelected != null) {
                    if (kv.getId().equals(editionSelected.getId())) {
                        tvName.setBackgroundResource(R.color.clog);
                        tvName.setTextColor(getActivity().getResources().getColor(R.color.white));
                    } else {
                        tvName.setBackgroundResource(R.color.white);
                        tvName.setTextColor(getActivity().getResources().getColor(
                                R.color.color_text_title));
                    }
                }
            }

            vItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editionSelected = kv;
                    readyRequestParameterForEdition();

                    // 显示加载框
                    vUtils.showLoadingDialog("");
                    //请求数据
//                    requestDataFromServer(false);
                }
            });
        }
    }

    /**
     * 展示模块Gdv布局
     *
     * @param list 数据
     */
    public void showGdvModule(final List<KeyValue> list, CustomGridView01 gdv) {
        resetGdvLayout(gdv);

        for (int i = 0; i < list.size(); i++) {
            final KeyValue kv = list.get(i);
//            kv.setName("测试模块名");
//            kv.setId("ceshi_module_id");
//            kv.setChoiced(false);

            View vItem = LayoutInflater.from(getActivity()).inflate(R.layout.layout_v_single_line, null);
            TextView tvName = vItem.findViewById(R.id.tv_layout_v_single_line);
            tvName.setText(list.get(i).getName());
//            tvName.setText("测试模块名");
            gdv.addChild(vItem);

            if (kv != null) {
                if (moduleSelected != null) {
                    if (kv.getId().equals(moduleSelected.getId())) {
                        tvName.setBackgroundResource(R.color.clog);
                        tvName.setTextColor(getActivity().getResources().getColor(R.color.white));
                    } else {
                        tvName.setBackgroundResource(R.color.white);
                        tvName.setTextColor(getActivity().getResources().getColor(
                                R.color.color_text_title));
                    }
                }
            }

            vItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moduleSelected = kv;
                    readyRequestParameterForModule();

                    // 显示加载框
                    vUtils.showLoadingDialog("");
                    //请求数据
//                    requestDataFromServer(false);
                }
            });
        }
    }

    private void resetGdvLayout(CustomGridView01 gdv) {
        if (gdv != null) {
            gdv.removeAllViews();
        }
    }

    /**
     * 学段
     */
    private void readyRequestParameterForPeriod() {
        jsonLastId = VariableUtils.periodID;
        lastType = "xd";
    }

    /**
     * 学科
     */
    private void readyRequestParameterSubject() {
        // 学段
        if (!ValidateFormatUtils.isEmpty(VariableUtils.periodID)) {
            ids.add(VariableUtils.periodID);
        }
        // 学科
        jsonLastId = VariableUtils.subjectID;
        lastType = "xk";
    }

    /**
     * 准备根据版本请求服务器时的所需参数
     */
    private void readyRequestParameterForEdition() {
        // 学段
        if (!ValidateFormatUtils.isEmpty(VariableUtils.periodID)) {
            ids.add(VariableUtils.periodID);
        }

        // 学科
        if (!ValidateFormatUtils.isEmpty(VariableUtils.subjectID)) {
            ids.add(VariableUtils.subjectID);
        }

        // 版本
        jsonLastId = VariableUtils.editionID;
        lastType = "bb";
    }

    /**
     * 准备根据模块请求服务器时的所需参数
     */
    private void readyRequestParameterForModule() {
        // 学段
        if (!ValidateFormatUtils.isEmpty(VariableUtils.periodID)) {
            ids.add(VariableUtils.periodID);
        }

        // 学科
        if (!ValidateFormatUtils.isEmpty(VariableUtils.subjectID)) {
            ids.add(VariableUtils.subjectID);
        }

        // 版本
        if (!ValidateFormatUtils.isEmpty(VariableUtils.editionID)) {
            ids.add(VariableUtils.editionID);
        }

        // 模块
        jsonLastId = VariableUtils.modulesID;
        lastType = "mk";
    }


    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || hasLoadOnce) {
            return;
        }
    }

}
