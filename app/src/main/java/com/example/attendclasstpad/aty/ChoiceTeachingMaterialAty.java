package com.example.attendclasstpad.aty;

import java.util.ArrayList;
import java.util.List;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.adapter.CourseCatalogLsvAdapter;
import com.example.attendclasstpad.adapter.PeriodGridAdapter;
import com.example.attendclasstpad.model.Course;
import com.example.attendclasstpad.model.Lesson;
import com.example.attendclasstpad.util.ConstantsUtils;
import com.example.attendclasstpad.util.PreferencesUtils;
import com.example.attendclasstpad.util.Utils;
import com.example.attendclasstpad.util.ValidateFormatUtils;
import com.example.attendclasstpad.util.VariableUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 选择教材目录
 *
 * @author chenhui 2019.04.25
 */
@SuppressLint("NewApi")
public class ChoiceTeachingMaterialAty extends Activity {
    /*
     * 目录位置
     */
    public static String CATALOG_POS = "CATALOG_POS";
    /*
     * 教材名称
     */
    public static String MATERIAL_NAME;

    private String catalogName = "";// 目录名称
    private String catalogIDCurr;// 目录位置

    String periodIDCurr = "";
    String subjectIDCurr = "";
    String editionIDCurr = "";
    String moduleIDCurr = "";

    TextView tvLast;

    // 课程目录
    private List<Course> catalogList;

    String unitIDCurr;

    private CourseCatalogLsvAdapter catalogLstvAdapter;// 目录

    private GridView gdvPeriod;// 学段
    private GridView gdvSubject;// 学科
    private GridView gdvEdition;// 版本
    private GridView gdvModule;// 模块
    private ListView lsvCourseCatalog;// 课程目录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ChoiceTeachingMaterialAty.this.setFinishOnTouchOutside(false);

        setContentView(R.layout.layout_fg_attend_class);

        gdvPeriod = (GridView) findViewById(R.id.gdv_period_layout_fg_attend_class);
        gdvSubject = (GridView) findViewById(R.id.gdv_subject_layout_fg_attend_class);

        gdvEdition = (GridView) findViewById(R.id.gdv_edition_layout_fg_attend_class);
        gdvModule = (GridView) findViewById(R.id.gdv_module_layout_fg_attend_class);

        dealWithExtras();

        // 学段
        final String[] periods = getResources().getStringArray(
                R.array.arrays_period);
        List<String> periodsList = Utils.getList(periods);
        final PeriodGridAdapter pGdvAdapter = new PeriodGridAdapter(this,
                periodsList);
        gdvPeriod.setAdapter(pGdvAdapter);
        // if (VariableUtils.periodID != null) {
        // pGdvAdapter.setCurrentPosition(-1);
        // } else {
        // pGdvAdapter.setCurrentPosition(0);
        if (ValidateFormatUtils.isEmpty(VariableUtils.modulesID)) {
            VariableUtils.periodID = periods[0];
        }
        pGdvAdapter.setCurrentID(VariableUtils.periodID);
        // }

        gdvPeriod.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                VariableUtils.periodID = periods[position];
                //存入首选项
                PreferencesUtils.saveInfoToPreferences(ChoiceTeachingMaterialAty.this, ConstantsUtils.PERIOD_ID, VariableUtils.periodID);

                // pGdvAdapter.setCurrentPosition(position);
                pGdvAdapter.setCurrentID(VariableUtils.periodID);
                pGdvAdapter.notifyDataSetChanged();

                setCatalogAdapter("", "");
            }
        });

        // 学科
        final String[] subjects = getResources().getStringArray(
                R.array.arrays_junior_middle_school_subject);
        List<String> subjectsList = Utils.getList(subjects);
        final PeriodGridAdapter sGdvAdapter = new PeriodGridAdapter(this,
                subjectsList);
        gdvSubject.setAdapter(sGdvAdapter);
        // sGdvAdapter.setCurrentPosition(0);
        if (ValidateFormatUtils.isEmpty(VariableUtils.modulesID)) {
            VariableUtils.subjectID = subjects[0];
        }
        sGdvAdapter.setCurrentID(VariableUtils.subjectID);

        gdvSubject.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                VariableUtils.subjectID = subjects[position];
                //存入首选项
                PreferencesUtils.saveInfoToPreferences(ChoiceTeachingMaterialAty.this, ConstantsUtils.SUBJECT_ID, VariableUtils.subjectID);

                sGdvAdapter.setCurrentID(VariableUtils.subjectID);
                sGdvAdapter.notifyDataSetChanged();

                setCatalogAdapter("", "");
            }
        });

        // 版本
        final String[] editions = getResources().getStringArray(
                R.array.arrays_versions);
        List<String> editionsList = Utils.getList(editions);
        final PeriodGridAdapter eGdvAdapter = new PeriodGridAdapter(this,
                editionsList);
        gdvEdition.setAdapter(eGdvAdapter);
        // if (VariableUtils.editionID != null) {
        // eGdvAdapter.setCurrentPosition(-1);
        // } else {
        // eGdvAdapter.setCurrentPosition(0);
        if (ValidateFormatUtils.isEmpty(VariableUtils.modulesID)) {
            VariableUtils.editionID = editions[0];
        }
        eGdvAdapter.setCurrentID(VariableUtils.editionID);
        // }

        gdvEdition.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                VariableUtils.editionID = editions[position];
                //存入首选项
                PreferencesUtils.saveInfoToPreferences(ChoiceTeachingMaterialAty.this, ConstantsUtils.EDITION_ID, VariableUtils.editionID);


                // eGdvAdapter.setCurrentPosition(position);
                eGdvAdapter.setCurrentID(VariableUtils.editionID);
                eGdvAdapter.notifyDataSetChanged();

                setCatalogAdapter("", "");
            }
        });

        // 模块
        final String[] modules = getResources().getStringArray(
                R.array.arrays_module);
        List<String> modulesList = Utils.getList(modules);
        final PeriodGridAdapter mGdvAdapter = new PeriodGridAdapter(this,
                modulesList);
        gdvModule.setAdapter(mGdvAdapter);
        // if (VariableUtils.modulesID != null) {
        // mGdvAdapter.setCurrentPosition(-1);
        // } else {
        // mGdvAdapter.setCurrentPosition(0);

        if (ValidateFormatUtils.isEmpty(VariableUtils.modulesID)) {
            VariableUtils.modulesID = modules[0];
        }
        mGdvAdapter.setCurrentID(VariableUtils.modulesID);
        // }

        gdvModule.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                VariableUtils.modulesID = modules[position];
                //存入首选项
                PreferencesUtils.saveInfoToPreferences(ChoiceTeachingMaterialAty.this, ConstantsUtils.MODULE_ID, VariableUtils.modulesID);

                // mGdvAdapter.setCurrentPosition(position);
                mGdvAdapter.setCurrentID(VariableUtils.modulesID);
                mGdvAdapter.notifyDataSetChanged();

                setCatalogAdapter("", "");
            }
        });

        initLstvForCatalog();
    }

    /**
     * 处理接收过来的数据
     */
    private void dealWithExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        // 单元ID
        String unitIDCurr = bundle.getString(ConstantsUtils.UNIT_ID);
        if (!ValidateFormatUtils.isEmpty(unitIDCurr)) {
            this.unitIDCurr = unitIDCurr;
        }

        // 目录ID
        String catalogID = bundle.getString(ConstantsUtils.CATALOG_ID);
        if (!ValidateFormatUtils.isEmpty(catalogID)) {
            this.catalogIDCurr = catalogID;
        }
        // 目录名称
        String catalogName = bundle.getString(ConstantsUtils.CATALOG_NAME);
        if (!ValidateFormatUtils.isEmpty(catalogName)) {
            this.catalogName = catalogName;
        }
    }

    private void initLstvForCatalog() {
        lsvCourseCatalog = (ListView) findViewById(R.id.lsv_catalog_layout_fg_attend_class);
        lsvCourseCatalog.setEnabled(false);
        lsvCourseCatalog.setClickable(false);
        lsvCourseCatalog.setFocusable(false);

        initCatalogData();
        setCatalogAdapter(unitIDCurr, catalogIDCurr);
    }

    private void setCatalogAdapter(String unitIDCurr, String catalogIDCurr) {
        // if (catalogLstvAdapter == null) {
        catalogLstvAdapter = new CourseCatalogLsvAdapter(this, catalogList,
                unitIDCurr, catalogIDCurr);
        // } else {
        // catalogLstvAdapter.notifyDataSetChanged();
        // }

        catalogLstvAdapter.setCurrentUnit(unitIDCurr);
        catalogLstvAdapter.setCurrentCatalogID(catalogIDCurr);
        lsvCourseCatalog.setAdapter(catalogLstvAdapter);
    }

    /**
     * 设置目录数据
     */
    private void initCatalogData() {
        catalogList = new ArrayList<Course>();

        Course course01 = new Course();
        course01.setUnitName("第一单元");
        course01.setUnitID("01");

        List<Lesson> lessonList01 = new ArrayList<Lesson>();
        Lesson lesson01 = new Lesson();
        lesson01.setName("1.沁园春-长沙");
        lesson01.setPid("01");
        lesson01.setKey("001");

        Lesson lesson02 = new Lesson();
        lesson02.setName("2.诗两首");
        lesson02.setPid("01");
        lesson02.setKey("002");

        Lesson lesson03 = new Lesson();
        lesson03.setName("3.大堰河——我的保姆");
        lesson03.setPid("01");
        lesson03.setKey("003");

        lessonList01.add(lesson01);
        lessonList01.add(lesson02);
        lessonList01.add(lesson03);

        course01.setLessonList(lessonList01);
        catalogList.add(course01);

        Course course02 = new Course();
        course02.setUnitName("第二单元");
        course02.setUnitID("02");

        List<Lesson> lessonList02 = new ArrayList<Lesson>();
        Lesson lesson04 = new Lesson();
        lesson04.setName("4.烛之武退秦师");
        lesson04.setPid("02");
        lesson04.setKey("004");

        Lesson lesson05 = new Lesson();
        lesson05.setName("5.荆轲刺秦王");
        lesson05.setPid("02");
        lesson05.setKey("005");

        Lesson lesson06 = new Lesson();
        lesson06.setName("6.鸿门宴");
        lesson06.setPid("02");
        lesson06.setKey("006");

        lessonList02.add(lesson04);
        lessonList02.add(lesson05);
        lessonList02.add(lesson06);

        course02.setLessonList(lessonList02);
        catalogList.add(course02);

        Course course03 = new Course();
        course03.setUnitName("第三单元");
        course03.setUnitID("03");

        List<Lesson> lessonList03 = new ArrayList<Lesson>();
        Lesson lesson07 = new Lesson();
        lesson07.setName("7.纪念刘和珍君");
        lesson07.setPid("03");
        lesson07.setKey("007");

        Lesson lesson08 = new Lesson();
        lesson08.setName("8.小狗包弟");
        lesson08.setPid("03");
        lesson08.setKey("008");

        Lesson lesson09 = new Lesson();
        lesson09.setName("9.记梁任公先生的一次演讲");
        lesson09.setPid("03");
        lesson09.setKey("009");

        lessonList03.add(lesson07);
        lessonList03.add(lesson08);
        lessonList03.add(lesson09);

        course03.setLessonList(lessonList03);
        catalogList.add(course03);

        Course course04 = new Course();
        course04.setUnitName("第四单元");
        course04.setUnitID("04");

        List<Lesson> lessonList04 = new ArrayList<Lesson>();
        Lesson lesson10 = new Lesson();
        lesson10.setName("10.短新闻两篇");
        lesson10.setPid("04");
        lesson10.setKey("010");

        Lesson lesson11 = new Lesson();
        lesson11.setName("11.包身工");
        lesson11.setPid("04");
        lesson11.setKey("011");

        Lesson lesson12 = new Lesson();
        lesson12.setName("12.飞向太空的航程");
        lesson12.setPid("04");
        lesson12.setKey("012");

        lessonList04.add(lesson10);
        lessonList04.add(lesson11);
        lessonList04.add(lesson12);

        course04.setLessonList(lessonList04);
        catalogList.add(course04);
    }

    public void onClick(View v) {
        TextView tvCurr = (TextView) v;
        Object obj = tvCurr.getTag();

        Lesson lesson = null;
        if (obj instanceof Lesson) {
            lesson = (Lesson) obj;
        }
        String catalogID = lesson.getKey();
        String name = lesson.getName().trim();
        VariableUtils.catalogName = name;
        catalogName = name;
        VariableUtils.catalogID = catalogID;

        //存放信息，目录
        PreferencesUtils.saveInfoToPreferences(ChoiceTeachingMaterialAty.this, ConstantsUtils.CATALOG_ID, VariableUtils.catalogID);
        //存放信息：是否选择了教材，方便取用
        PreferencesUtils.saveInfoToPreferences(ChoiceTeachingMaterialAty.this, ConstantsUtils.HAS_CHOICED_MATERIAL, true);

        setCatalogAdapter(unitIDCurr, catalogIDCurr);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(ConstantsUtils.CATALOG_ID, catalogID);
        intent.putExtra(ConstantsUtils.CATALOG_NAME, catalogName);
        setResult(RESULT_OK, intent);

        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {// 拦截设备上的返回按钮
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
