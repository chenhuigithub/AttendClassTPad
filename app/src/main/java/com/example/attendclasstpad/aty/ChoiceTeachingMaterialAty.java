package com.example.attendclasstpad.aty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.adapter.CatalogAdapter;
import com.example.attendclasstpad.adapter.CourseCatalogLsvAdapter;
import com.example.attendclasstpad.adapter.KeyValueAdapter;
import com.example.attendclasstpad.model.Catalog;
import com.example.attendclasstpad.model.KeyValue;
import com.example.attendclasstpad.model.Model;
import com.example.attendclasstpad.util.ConstantsForPreferencesUtils;
import com.example.attendclasstpad.util.ConstantsForServerUtils;
import com.example.attendclasstpad.util.ConstantsUtils;
import com.example.attendclasstpad.util.PreferencesUtils;
import com.example.attendclasstpad.util.ServerDataAnalyzeUtils;
import com.example.attendclasstpad.util.UrlUtils;
import com.example.attendclasstpad.util.ValidateFormatUtils;
import com.example.attendclasstpad.util.VariableUtils;
import com.example.attendclasstpad.util.ViewUtils;
import com.example.attendclasstpad.view.CustomGridView01;
import com.example.attendclasstpad.view.CustomListView;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    // 学段
    String[] periods;

    String periodIDCurr = "";
    String subjectIDCurr = "";
    String editionIDCurr = "";
    String moduleIDCurr = "";
    private ArrayList<String> ids;// 存放id数据的字符串组合
    private String lastType;// 最后一步点击操作的类型，例如：xd

    KeyValueAdapter pGdvAdapter;
    KeyValueAdapter sGdvAdapter;
    KeyValueAdapter eGdvAdapter;
    KeyValueAdapter mGdvAdapter;

    private GridView gdvPeriod;// 学段
    private GridView gdvSubject;// 学科
    //    private GridView gdvEdition;// 版本
//    private GridView gdvModule;// 模块
    //    private GridView gdvCatalog;// 目录
    private ListView lvCatalog;// 目录

    private CustomGridView01 gdvModule1;// 版本
    private CustomGridView01 gdvEdition1;// 模块

    private List<KeyValue> periodList;//学段
    private List<KeyValue> subjectList;//学科
    private List<KeyValue> editionList;//版本
    private List<KeyValue> moduleList;//模块

    private KeyValue periodSelected;// 已选学段
    //    private KeyValue subjectSelected;// 已选科目
//    private KeyValue editionSelected;// 已选版本
//    private KeyValue moduleSelected;// 已选模块
    private KeyValue catelogSelected;// 已选目录
    private String jsonLastId = "";// 最后一步点击操作的数据id（后台这么设计，木办法。。。）

    private FragmentManager manager;
    private CatalogAdapter cAdapter;

    private TextView tvLast;

    // 课程目录
//    private List<Course> catalogList;
    private List<Catalog> catalogList;

    String unitIDCurr;

    private CourseCatalogLsvAdapter catalogLstvAdapter;// 目录

    private CustomListView lsvCourseCatalog;// 课程目录

    private Handler uiHandler;// ui主线程
    private ViewUtils vUtils;// 布局工具

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ChoiceTeachingMaterialAty.this.setFinishOnTouchOutside(false);

        setContentView(R.layout.layout_fg_attend_class);

        // 初始化数据
        uiHandler = new Handler(getMainLooper());
        ids = new ArrayList<String>();
        vUtils = new ViewUtils(this);
        periodList = new ArrayList<KeyValue>();
        subjectList = new ArrayList<KeyValue>();
        editionList = new ArrayList<KeyValue>();
        moduleList = new ArrayList<KeyValue>();
        catalogList = new ArrayList<Catalog>();
        catelogSelected = new KeyValue();

        initVariableData();

        gdvPeriod = (GridView) findViewById(R.id.gdv_period_layout_fg_attend_class);
        gdvSubject = (GridView) findViewById(R.id.gdv_subject_layout_fg_attend_class);

//        gdvEdition = (GridView) findViewById(R.id.gdv_edition_layout_fg_attend_class);
//        gdvModule = (GridView) findViewById(R.id.gdv_module_layout_fg_attend_class);
        gdvModule1 = (CustomGridView01) findViewById(R.id.gdv_module1_layout_fg_attend_class);
        gdvEdition1 = (CustomGridView01) findViewById(R.id.gdv_edition1_layout_fg_attend_class);

        initLstvForCatalog();

        dealWithExtras();

        setWidgetListeners();

        // 显示加载框
        vUtils.showLoadingDialog("");

        requestDataFromServer(true);
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

    private void initVariableData() {
        VariableUtils.periodID = "";
        VariableUtils.subjectID = "";
        VariableUtils.editionID = "";
        VariableUtils.modulesID = "";
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

            View vItem = LayoutInflater.from(this).inflate(R.layout.layout_v_single_line, null);
            TextView tvName = vItem.findViewById(R.id.tv_layout_v_single_line);
            tvName.setText(list.get(i).getName());
//            tvName.setText("测试模块名");
            gdv.addChild(vItem);

            if (kv != null) {
                if (VariableUtils.editionID != null) {
                    if (kv.getId().equals(VariableUtils.editionID)) {
                        tvName.setBackgroundResource(R.color.clog);
                        tvName.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        tvName.setBackgroundResource(R.color.white);
                        tvName.setTextColor(getResources().getColor(
                                R.color.color_text_title));
                    }
                }
            }

            vItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (kv != null) {
                        VariableUtils.editionID = kv.getId();
                    }

                    readyRequestParameterForEdition();

                    // 显示加载框
                    vUtils.showLoadingDialog("");
                    //请求数据
                    requestDataFromServer(false);
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

            View vItem = LayoutInflater.from(this).inflate(R.layout.layout_v_single_line, null);
            TextView tvName = vItem.findViewById(R.id.tv_layout_v_single_line);
            tvName.setText(list.get(i).getName());
//            tvName.setText("测试模块名");
            gdv.addChild(vItem);

            if (kv != null) {
                if (VariableUtils.modulesID != null) {
                    if (kv.getId().equals(VariableUtils.modulesID)) {
                        tvName.setBackgroundResource(R.color.clog);
                        tvName.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        tvName.setBackgroundResource(R.color.white);
                        tvName.setTextColor(getResources().getColor(
                                R.color.color_text_title));
                    }
                }
            }

            vItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (kv != null) {
                        VariableUtils.modulesID = kv.getId();
                    }

                    readyRequestParameterForModule();

                    // 显示加载框
                    vUtils.showLoadingDialog("");
                    //请求数据
                    requestDataFromServer(false);
                }
            });
        }
    }

    private void resetGdvLayout(CustomGridView01 gdv) {
        if (gdv != null) {
            gdv.removeAllViews();
        }
    }

    private void setGdvPeriodAdapter(int pos, String currentID) {
//        periods = getResources().getStringArray(
//                R.array.arrays_period);
//        List<String> periodsList = Utils.getList(periods);
//        pGdvAdapter = new PeriodGridAdapter(this,
//                periodList);

//        if (pGdvAdapter == null) {
        pGdvAdapter = new KeyValueAdapter(this, periodList);
        pGdvAdapter.setCurrentPosition(pos);
        pGdvAdapter.setCurrentID(currentID);
        gdvPeriod.setAdapter(pGdvAdapter);

//            pGdvAdapter.setCurrentPosition(0);
//        } else {
//            pGdvAdapter.setCurrentID(VariableUtils.periodID);
//            pGdvAdapter.notifyDataSetChanged();
    }


    private void setGdvSubjectAdapter(int pos, String currentID) {
        // 学科
//        final String[] subjects = getResources().getStringArray(
//                R.array.arrays_junior_middle_school_subject);
//        List<String> subjectsList = Utils.getList(subjects);
//        final PeriodGridAdapter sGdvAdapter = new PeriodGridAdapter(this,
//                subjectsList);
        sGdvAdapter = new KeyValueAdapter(this, subjectList);
        sGdvAdapter.setCurrentPosition(pos);
        sGdvAdapter.setCurrentID(VariableUtils.subjectID);
        gdvSubject.setAdapter(sGdvAdapter);
    }

//    private void setGdvEditionAdapter(int pos, String currentID) {
//        // 版本
////        final String[] editions = getResources().getStringArray(
////                R.array.arrays_versions);
////        List<String> editionsList = Utils.getList(editions);
////        final PeriodGridAdapter eGdvAdapter = new PeriodGridAdapter(this,
////                editionsList);
//
//        eGdvAdapter = new KeyValueAdapter(this, editionList);
//        eGdvAdapter.setCurrentPosition(pos);
//        eGdvAdapter.setCurrentID(currentID);
//        gdvEdition.setAdapter(eGdvAdapter);
//    }

//    private void setGdvModuleAdapter(int pos, String currentID) {
//        // 模块
////        final String[] modules = getResources().getStringArray(
////                R.array.arrays_module);
////        List<String> modulesList = Utils.getList(modules);
////        final PeriodGridAdapter mGdvAdapter = new PeriodGridAdapter(this,
////                modulesList);
//        mGdvAdapter = new KeyValueAdapter(this, moduleList);
//        mGdvAdapter.setCurrentPosition(pos);
//        mGdvAdapter.setCurrentID(currentID);
//        gdvModule.setAdapter(mGdvAdapter);
//    }

    private void setWidgetListeners() {
        gdvPeriod.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
//                VariableUtils.periodID = periods[position];
                if (periodList.get(position) != null) {
                    VariableUtils.periodID = periodList.get(position).getId();
                }
                VariableUtils.subjectID = "";
                VariableUtils.editionID = "";
                VariableUtils.modulesID = "";

                //存入首选项
                PreferencesUtils.saveInfoToPreferences(ChoiceTeachingMaterialAty.this, ConstantsUtils.PERIOD_ID, VariableUtils.periodID);

                readyRequestParameterForPeriod();

                // 显示加载框
                vUtils.showLoadingDialog("");
                // 请求数据
                requestDataFromServer(false);

//                setCatalogAdapter("", "");
            }
        });

        gdvSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (subjectList.get(position) != null) {
                    KeyValue subjectSelected = subjectList.get(position);
                    if (subjectSelected != null) {
                        VariableUtils.subjectID = subjectSelected.getId();
                    }
                }

                VariableUtils.editionID = "";
                VariableUtils.modulesID = "";

                readyRequestParameterSubject();

                // 显示加载框
                vUtils.showLoadingDialog("");
                // 请求数据
                requestDataFromServer(false);
            }
        });
    }

    /**
     * 从服务器获取目录
     */
    private void requestDataFromServer(boolean isFirstRequest) {
        // 获取token
        String token = PreferencesUtils.acquireInfoFromPreferences(this,
                ConstantsForPreferencesUtils.TOKEN);

        RequestBody formBody = null;
        if (isFirstRequest) {
            formBody = new FormBody.Builder().add("0", "0").build();
        } else {
            // 添加参数
            Model model = new Model();
            model.setDataId(jsonLastId);
            model.setListId(ids);
            Gson gson = new Gson();
            String json = gson.toJson(model);
            // MediaType 设置Content-Type 标头中包含的媒体类型值
            formBody = FormBody.create(
                    MediaType.parse("application/json; charset=utf-8"), json);
        }
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(800, TimeUnit.MILLISECONDS)
                .readTimeout(800, TimeUnit.MILLISECONDS).build();
        Request request = new Request.Builder()
                .url(UrlUtils.PREFIX_MOBILE + "getRelaData")
                .addHeader(ConstantsForServerUtils.AUTH_TOKEN, token)
                .post(formBody).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call callback, final IOException ex) {
                if (ids != null && ids.size() > 0) {
                    ids.clear();
                }

                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 关闭加载框
                        vUtils.dismissDialog();

                        Toast.makeText(ChoiceTeachingMaterialAty.this,
                                ex.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call callback, Response response)
                    throws IOException {
                final String result = response.body().string();
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        dealWithData(result);

                        // 关闭加载框
                        vUtils.dismissDialog();
                    }
                });
            }
        });
    }

    /**
     * 获取单个数据，并存入模型
     *
     * @param jsonObj 数据
     * @param type    数据类型（例如：xd）
     */
    private List<KeyValue> saveEveryData(JSONObject jsonObj, String type) {
        if (jsonObj == null) {
            return new ArrayList<KeyValue>();
        }

        List<KeyValue> list = new ArrayList<KeyValue>();

        Iterator<String> sIterator = jsonObj.keys();
        while (sIterator.hasNext()) {
            // 获得key
            String key = sIterator.next();
            // 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可
            String value = "";
            try {
                value = jsonObj.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            KeyValue keyValue = new KeyValue();
            keyValue.setId(key);
            keyValue.setName(value);
            keyValue.setType(type);
            list.add(keyValue);
        }
        return list;
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


    /**
     * 处理接收到数据
     *
     * @param result
     */
    private void dealWithData(String result) {
        JSONObject resultJsonObj = null;
        try {
            resultJsonObj = new JSONObject(result);

            // 获取文字消息
            String msg = ServerDataAnalyzeUtils.getValue(resultJsonObj,
                    ConstantsForServerUtils.MSG);

            if (!ValidateFormatUtils.isEmpty(msg)) {// 数据返回失败，此处解析特殊
                Toast.makeText(ChoiceTeachingMaterialAty.this, msg,
                        Toast.LENGTH_SHORT).show();
            }

            // 学段
            JSONObject periodJsonObj = ServerDataAnalyzeUtils.getDataAsJSONObject(
                    resultJsonObj, ConstantsForServerUtils.XD);
//          List<KeyValue> periods = com.alibaba.fastjson.JSON.parseArray(periodJsonArr.toString(), KeyValue.class);

            if (periodJsonObj != null) {
                periodList.clear();
                periodList.addAll(saveEveryData(periodJsonObj,
                        ConstantsForServerUtils.XD));

                if (periodList.size() > 0) {
                    periodSelected = periodList.get(0);
                    if (periodSelected != null && TextUtils.isEmpty(VariableUtils.periodID)) {
                        VariableUtils.periodID = periodSelected.getId();
                    }
                }
            }


            // 学科
            JSONObject subjectObj = ServerDataAnalyzeUtils.getDataAsJSONObject(
                    resultJsonObj, ConstantsForServerUtils.XK);
            if (subjectObj != null) {
                subjectList.clear();
                subjectList.addAll(saveEveryData(subjectObj,
                        ConstantsForServerUtils.XK));
                if (subjectList.size() > 0) {
                    KeyValue subjectSelected = subjectList.get(0);
                    if (subjectSelected != null && TextUtils.isEmpty(VariableUtils.subjectID)) {
                        VariableUtils.subjectID = subjectSelected.getId();
                    }
                }
            }

            // 版本
            JSONObject editionJsonObj = ServerDataAnalyzeUtils.getDataAsJSONObject(
                    resultJsonObj, ConstantsForServerUtils.BB);
            if (editionJsonObj != null) {
                editionList.clear();
                editionList.addAll(saveEveryData(editionJsonObj,
                        ConstantsForServerUtils.BB));
                if (editionList.size() > 0) {
                    KeyValue editionSelected = editionList.get(0);
                    if (editionSelected != null && TextUtils.isEmpty(VariableUtils.editionID)) {
                        VariableUtils.editionID = editionSelected.getId();
                    }
                }
            }

            // 模块
            JSONObject moduleObj = ServerDataAnalyzeUtils.getDataAsJSONObject(
                    resultJsonObj, ConstantsForServerUtils.MK);
            if (moduleObj != null) {
                moduleList.clear();
                moduleList.addAll(saveEveryData(moduleObj,
                        ConstantsForServerUtils.MK));
                if (moduleList.size() > 0) {
                    KeyValue moduleSelected = moduleList.get(0);
                    if (moduleSelected != null && TextUtils.isEmpty(VariableUtils.modulesID)) {
                        VariableUtils.modulesID = moduleSelected.getId();
                    }
                }
            }


            // 目录
            JSONArray catalogArr = ServerDataAnalyzeUtils.getDataAsJSONArray(
                    resultJsonObj, ConstantsForServerUtils.DATALIST);
            if (catalogArr != null) {
                List<Catalog> list = new ArrayList<Catalog>();
                List<Catalog> childList = new ArrayList<Catalog>();//子级目录
                if (catalogArr == null || catalogArr.length() <= 0) {
                    hideCatalog();

                    Toast.makeText(this, "还没有目录呢，试试别的查询条件吧~",
                            Toast.LENGTH_SHORT).show();
                } else {
                    showCatalog();

                    for (int i = 0; i < catalogArr.length(); i++) {
                        JSONObject jsonObj = catalogArr.getJSONObject(i);
                        String id = jsonObj
                                .getString(ConstantsForServerUtils.DATAID);
                        String name = jsonObj
                                .getString(ConstantsForServerUtils.DATANAME);
                        String pid = jsonObj
                                .getString(ConstantsForServerUtils.DATAPID);

                        Catalog catalog = new Catalog();
                        catalog.setId(id);
                        catalog.setName(name);
                        catalog.setPid(pid);
                        if ("0".equals(pid)) {
                            list.add(catalog);
                        } else {
                            childList.add(catalog);
                        }
                    }

                    List<Catalog> newGroupList = new ArrayList<Catalog>();//父级目录
                    for (Catalog group : list) {
                        List<Catalog> itemChildList = new ArrayList<Catalog>();//子级目录
                        for (Catalog child : childList) {
                            String pid = child.getPid();
                            if (pid.equals(group.getId())) {
                                itemChildList.add(child);
                            }
                        }
                        Catalog newGroup = new Catalog();
                        newGroup = group;
                        newGroup.setCatalog(itemChildList);
                        newGroupList.add(newGroup);
                    }

                    if (list.size() > 0) {
                        list.clear();
                        list.addAll(newGroupList);
                    }
                }
                catalogList.clear();
                catalogList.addAll(list);

                if (catalogList.size() > 0) {
                    Catalog catalog = catalogList.get(0);
                    if (catalog != null) {
                        unitIDCurr = catalog.getId();
                        if (TextUtils.isEmpty(VariableUtils.unitID)) {
                            VariableUtils.unitID = unitIDCurr;
                        }

                        List<Catalog> childs = catalog.getCatalog();
                        if (childs != null && childs.size() > 0) {
                            Catalog child = childs.get(0);
                            if (child != null && TextUtils.isEmpty(VariableUtils.catalogID)) {
                                catelogSelected = new KeyValue();
                                catelogSelected.setId(child.getId());
                                catelogSelected.setName(child.getName());
                                VariableUtils.catalogID = child.getId();
                            }
                        }
                    }
                }
            } else {
                hideCatalog();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // 刷新显示
        setGdvPeriodAdapter(-1, VariableUtils.periodID);
        setGdvSubjectAdapter(-1, VariableUtils.subjectID);
//        setGdvEditionAdapter(-1, VariableUtils.editionID);
//        setGdvModuleAdapter(-1, VariableUtils.modulesID);

        showGdvEdition(editionList, gdvEdition1);
        showGdvModule(moduleList, gdvModule1);

        setCatalogAdapter(unitIDCurr, catelogSelected.getId());

        //存入首选项
        PreferencesUtils.saveInfoToPreferences(this, ConstantsForPreferencesUtils.PERIOD_ID_CHOICED, VariableUtils.periodID);
        PreferencesUtils.saveInfoToPreferences(this, ConstantsForPreferencesUtils.SUBJECT_ID_CHOICED, VariableUtils.subjectID);
        PreferencesUtils.saveInfoToPreferences(this, ConstantsForPreferencesUtils.EDITION_ID_CHOICED, VariableUtils.editionID);
        PreferencesUtils.saveInfoToPreferences(this, ConstantsForPreferencesUtils.MODULE_ID_CHOICED, VariableUtils.modulesID);
        PreferencesUtils.saveInfoToPreferences(this, ConstantsForPreferencesUtils.CATALOG_ID_CHOICED, VariableUtils.catalogID);
        PreferencesUtils.saveInfoToPreferences(this, ConstantsForPreferencesUtils.CATALOG_NAME_CHOICED, VariableUtils.catalogName);

        //默认数据存入首选项，方便下次读取
        if (periodList.size() > 0 && subjectList.size() > 0 && editionList.size() > 0 && moduleList.size() > 0) {
            PreferencesUtils.saveInfoToPreferences(this, ConstantsForPreferencesUtils.ATTEND_COURSE_SCREENING_CRITERIA_NAME, periodList.get(0).getName() + "-" + subjectList.get(0).getName() + "-" + editionList.get(0).getName() + "-" + moduleList.get(0).getName());
        }

        if (ids != null && ids.size() > 0) {
            ids.clear();
        }
    }

    /**
     * 显示目录
     */
    private void showCatalog() {
//        tvLbl02.setVisibility(View.VISIBLE);
//        vLine02.setVisibility(View.VISIBLE);
//        llCatalog.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏目录
     */
    private void hideCatalog() {
//        tvLbl02.setVisibility(View.GONE);
//        vLine02.setVisibility(View.GONE);
//        llCatalog.setVisibility(View.GONE);
    }


    private void initLstvForCatalog() {
        lsvCourseCatalog = (CustomListView) findViewById(R.id.lsv_catalog_layout_fg_attend_class);
        lsvCourseCatalog.setEnabled(true);
        lsvCourseCatalog.setClickable(false);
        lsvCourseCatalog.setFocusable(true);
        lsvCourseCatalog.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

//        initCatalogData();
//        setCatalogAdapter(unitIDCurr, catalogIDCurr);
    }

    /**
     * 课程目录适配器
     *
     * @param unitIDCurr    当前单元ID
     * @param catalogIDCurr 当前目录ID
     */
    private void setCatalogAdapter(String unitIDCurr, String catalogIDCurr) {
//        // if (catalogLstvAdapter == null) {
        catalogLstvAdapter = new CourseCatalogLsvAdapter(this, catalogList,
                unitIDCurr, catalogIDCurr);
        // } else {
        // catalogLstvAdapter.notifyDataSetChanged();
        // }
//
//        catalogLstvAdapter.setCurrentUnit(unitIDCurr);
//        catalogLstvAdapter.setCurrentCatalogID(catalogIDCurr);
        lsvCourseCatalog.setAdapter(catalogLstvAdapter);
    }

    /**
     * 设置目录数据
     */
    private void initCatalogData() {
//        catalogList = new ArrayList<Course>();
//
//        Course course01 = new Course();
//        course01.setUnitName("第一单元");
//        course01.setUnitID("01");
//
//        List<Lesson> lessonList01 = new ArrayList<Lesson>();
//        Lesson lesson01 = new Lesson();
//        lesson01.setName("1.沁园春-长沙");
//        lesson01.setPid("01");
//        lesson01.setKey("001");
//
//        Lesson lesson02 = new Lesson();
//        lesson02.setName("2.诗两首");
//        lesson02.setPid("01");
//        lesson02.setKey("002");
//
//        Lesson lesson03 = new Lesson();
//        lesson03.setName("3.大堰河——我的保姆");
//        lesson03.setPid("01");
//        lesson03.setKey("003");
//
//        lessonList01.add(lesson01);
//        lessonList01.add(lesson02);
//        lessonList01.add(lesson03);
//
//        course01.setLessonList(lessonList01);
//        catalogList.add(course01);
//
//        Course course02 = new Course();
//        course02.setUnitName("第二单元");
//        course02.setUnitID("02");
//
//        List<Lesson> lessonList02 = new ArrayList<Lesson>();
//        Lesson lesson04 = new Lesson();
//        lesson04.setName("4.烛之武退秦师");
//        lesson04.setPid("02");
//        lesson04.setKey("004");
//
//        Lesson lesson05 = new Lesson();
//        lesson05.setName("5.荆轲刺秦王");
//        lesson05.setPid("02");
//        lesson05.setKey("005");
//
//        Lesson lesson06 = new Lesson();
//        lesson06.setName("6.鸿门宴");
//        lesson06.setPid("02");
//        lesson06.setKey("006");
//
//        lessonList02.add(lesson04);
//        lessonList02.add(lesson05);
//        lessonList02.add(lesson06);
//
//        course02.setLessonList(lessonList02);
//        catalogList.add(course02);
//
//        Course course03 = new Course();
//        course03.setUnitName("第三单元");
//        course03.setUnitID("03");
//
//        List<Lesson> lessonList03 = new ArrayList<Lesson>();
//        Lesson lesson07 = new Lesson();
//        lesson07.setName("7.纪念刘和珍君");
//        lesson07.setPid("03");
//        lesson07.setKey("007");
//
//        Lesson lesson08 = new Lesson();
//        lesson08.setName("8.小狗包弟");
//        lesson08.setPid("03");
//        lesson08.setKey("008");
//
//        Lesson lesson09 = new Lesson();
//        lesson09.setName("9.记梁任公先生的一次演讲");
//        lesson09.setPid("03");
//        lesson09.setKey("009");
//
//        lessonList03.add(lesson07);
//        lessonList03.add(lesson08);
//        lessonList03.add(lesson09);
//
//        course03.setLessonList(lessonList03);
//        catalogList.add(course03);
//
//        Course course04 = new Course();
//        course04.setUnitName("第四单元");
//        course04.setUnitID("04");
//
//        List<Lesson> lessonList04 = new ArrayList<Lesson>();
//        Lesson lesson10 = new Lesson();
//        lesson10.setName("10.短新闻两篇");
//        lesson10.setPid("04");
//        lesson10.setKey("010");
//
//        Lesson lesson11 = new Lesson();
//        lesson11.setName("11.包身工");
//        lesson11.setPid("04");
//        lesson11.setKey("011");
//
//        Lesson lesson12 = new Lesson();
//        lesson12.setName("12.飞向太空的航程");
//        lesson12.setPid("04");
//        lesson12.setKey("012");
//
//        lessonList04.add(lesson10);
//        lessonList04.add(lesson11);
//        lessonList04.add(lesson12);
//
//        course04.setLessonList(lessonList04);
//        catalogList.add(course04);
    }

    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);

        TextView tvCurr = (TextView) v;
        Object obj = tvCurr.getTag();

        Catalog ca = null;
        if (obj instanceof Catalog) {
            ca = (Catalog) obj;

            String catalogID = ca.getId();
            String name = ca.getName().trim();
            VariableUtils.catalogName = name;
            catalogName = name;
            VariableUtils.catalogID = catalogID;

            //存放信息，目录ID
            PreferencesUtils.saveInfoToPreferences(ChoiceTeachingMaterialAty.this, ConstantsForPreferencesUtils.CATALOG_ID_CHOICED, VariableUtils.catalogID);
            //存放信息，目录名称
            PreferencesUtils.saveInfoToPreferences(ChoiceTeachingMaterialAty.this, ConstantsForPreferencesUtils.CATALOG_NAME_CHOICED, VariableUtils.catalogName);
            //存放信息：是否选择了教材，方便取用
            PreferencesUtils.saveInfoToPreferences(ChoiceTeachingMaterialAty.this, ConstantsUtils.HAS_CHOICED_MATERIAL, true);

            setCatalogAdapter(unitIDCurr, catalogIDCurr);

            intent.putExtra(ConstantsUtils.CATALOG_ID, catalogID);
            intent.putExtra(ConstantsUtils.CATALOG_NAME, catalogName);
        }
        setResult(RESULT_OK, intent);

        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && event.getAction() == KeyEvent.ACTION_DOWN) {// 拦截设备上的返回按钮
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }

}
