package com.example.attendclasstpad.fg;

import java.util.ArrayList;
import java.util.List;

import com.example.attendclasstpad.adapter.CustomPagerAdapter04;
import com.example.attendclasstpad.aty.ChoiceTeachingMaterialAty;
import com.example.attendclasstpad.R;
import com.example.attendclasstpad.aty.MainActivity;
import com.example.attendclasstpad.aty.ShareFullScreenActivity;
import com.example.attendclasstpad.adapter.ColorAdapter;
import com.example.attendclasstpad.adapter.CustomPagerAdapter03;
import com.example.attendclasstpad.adapter.GalleryAdapter;
import com.example.attendclasstpad.adapter.FilesListAdapter;
import com.example.attendclasstpad.callback.InterfacesCallback;
import com.example.attendclasstpad.callback.OnListenerForPlayVideoCallback;
import com.example.attendclasstpad.model.Bean;
import com.example.attendclasstpad.model.Courseware;
import com.example.attendclasstpad.model.DataID01;
import com.example.attendclasstpad.model.DataInfo;
import com.example.attendclasstpad.model.File01;
import com.example.attendclasstpad.model.Picture;
import com.example.attendclasstpad.model.TestData;
import com.example.attendclasstpad.model.VideoAudio;
import com.example.attendclasstpad.util.ConstantsForPreferencesUtils;
import com.example.attendclasstpad.util.ConstantsForServerUtils;
import com.example.attendclasstpad.util.ConstantsUtils;
import com.example.attendclasstpad.util.DrawUtils;
import com.example.attendclasstpad.util.PreferencesUtils;
import com.example.attendclasstpad.util.ServerDataAnalyzeUtils;
import com.example.attendclasstpad.util.ServerRequestUtils;
import com.example.attendclasstpad.util.UrlUtils;
import com.example.attendclasstpad.util.ValidateFormatUtils;
import com.example.attendclasstpad.util.VariableUtils;
import com.example.attendclasstpad.util.ViewUtils;
import com.example.attendclasstpad.view.BgDarkPopupWindow;
import com.example.attendclasstpad.view.CustomSurfaceView;
import com.example.pullrefreshlistview.util.PullDownView;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 上课详情
 *
 * @author zhaochenhui, 2018.05.18
 */
@SuppressLint("ValidFragment")
public class AttendClassDetailFg extends BaseNotPreLoadFg implements InterfacesCallback.ICanDoSth, InterfacesCallback.ICanKnowSth2 {
    // 文件标志
    private static int[] fileLogoRes = {R.drawable.pdf, R.drawable.ppt,
            R.drawable.doc};
    // 将图片存储在int数组中（加载的全部是图片）
    private int[] ivsRes = {R.drawable.ppt01, R.drawable.ppt02,
            R.drawable.ppt03, R.drawable.ppt01, R.drawable.ppt02,
            R.drawable.ppt03};

    private int prePosition;// 幻灯片展示的上一个位置

    private boolean isPrepared;// 标志位，标志已经初始化完成
    private boolean hasLoadOnce;// 是否已被加载过一次，第二次就不再去请求数据了

//    private String fileType = "";// 授课预览内容的文件格式（教案学案等文字类型、课件ppt图片类型）
//    private String fileSource = "";// 授课预览内容的文件来源（配套资源、扩展资源等）

    // private List<PaintSize> paintSizeList;// 画笔粗细
    // private Handler handler;
    // private FragmentManager manager;// 声明成全局性的
    private BgDarkPopupWindow window;// 弹框

    private int colorCur = 0;// 当前画笔的颜色序号
    private int paintSizeCur = 5;// 当前选中的画笔型号,默认为最小型号

    private String rightInfoCount;//  预览授课资源的总个数
    private String rightPagerType = ConstantsForServerUtils.WORD;// 右侧ViewPager展示类型,默认为文档（word/video/...）

    /**
     * 绘制方式（画笔、橡皮擦等）,默认为画笔
     */
    private int drawStyle = ConstantsUtils.GRAFFITI;

//    private List<FileContent> fileContents;

    private boolean isRepaint = true;// 是否重画

    private String catalogIDCurr = "";// 目录ID
    private String catalogNameCurr = "";// 目录名称
    private String materialName = "";// 教材名称
    private int catalogPos;// 目录位置
    private String classID = "";//班级ID
    private int currentPageNumForFile = 1;// 授课列表当前页页码，默认是首页
    private int currentPageNumForPreview = 1;// 预览授课当前页页码，默认是首页

    private String chapterID = "";//章节ID

    private boolean isPageSelected = false;// 是否允许走viewPager滑动监听中的onPageSelected方法
    private boolean isScroll = false;// 是否正在滑动，默认未滑动
    //    private int positionMax = -1;// viewPager滑动过的最大位置
    private int lastPosition;// viewPager上一个位置
    private int newPosition;// viewPager当前位置
    private boolean needMultiPageRequestPreview = true;//授课预览是否需要多页请求,默认需要

    private File01 fileFocus;//正在预览的授课资源

    int itemWidth;// 选中的绘制菜单栏单项按钮长度

    int[] drawMenuItemResID = {R.id.ll_paint_layout_v_draw_menu,
            R.id.ll_eraser_layout_v_draw_menu,
            R.id.ll_revoke_layout_v_draw_menu, R.id.ll_redo_layout_v_draw_menu,
            R.id.ll_clear_screen_layout_v_draw_menu,
            R.id.ll_white_board_layout_v_draw_menu,
            R.id.ll_full_screen_layout_v_draw_menu,
            R.id.ll_exit_full_screen_layout_v_draw_menu};

    private List<File01> fileList;//授课文件
    private ArrayList<String> idFileChoicedList;//被选中的资源文件ID集合
    private ArrayList<String> idList;//ID集合
    private List<DataInfo> planList;// 教案学案列表（来源：配套资源）
    private List<Courseware> coursewareList;// 右侧课件列表（来源：组课件）
    private List<TestData> testList;// 试题列表（来源：组试题）
    private List<TestData> omicsList;// 学案列表（来源：组学案）
    private ArrayList<String> txtList;//txt文档类数据
    private List<VideoAudio> videoList;// 音视频列表
    private VideoAudio videoCurr;//正在观看的视频

    private Handler uiHandler;// ui主线程
    private ViewUtils vUtils;// 布局工具
    private ServerRequestUtils sUtils;// 服务器请求工具
    private InterfacesCallback.ICanDoSth callback;//回调
    //    private InterfacesCallback.ICanKnowSth5 callback5;
    private InterfacesCallback.ICanKnowSth6 picCallback;//图片回调
    private OnListenerForPlayVideoCallback callbackForVideo;// 音视频播放器回调

    private FilesListAdapter filesAdapter;// 文件目录适配器
    private GalleryAdapter glAdapter;

    private View allFgView;// 总布局
    private RelativeLayout rlFile;// 画廊布局：文件（PPT）缩略图展示布局
    private Gallery glFile;// 画廊效果：文件（PPT）缩略图展示布局
    private CustomSurfaceView sfvBoard;// 画布

    private CustomPagerAdapter04 wordVPagerAdapter;// 学案教案滑动布局适配器
    private CustomPagerAdapter03 picVpagerAdapter;// 课件大图滑动布局适配器
    private PlayVideoFragment videoFg;// 音视频

    // 上一个（小图）
    private TextView tvPrevious;
    // 下一个（小图）
    private TextView tvNext;
    // 切换教材、目录
    private TextView tvSwitchMaterial;

    // 绘制菜单的面板
    LinearLayout llDrawMenu;
    LinearLayout llTitle;// 标题栏
    TextView tvTitleName;// 标题名

    // 细线(圆形标志)
    private FrameLayout flSmallPaint;
    private LinearLayout llSmallPaint;
    private ImageView ivSmallPaint;
    // 中等线条(圆形标志)
    private FrameLayout flMiddlePaint;
    private LinearLayout llMiddlePaint;
    private ImageView ivMiddlePaint;
    // 粗线条(圆形标志)
    private FrameLayout flLargePaint;
    private LinearLayout llLargePaint;
    private ImageView ivLargePaint;
    // 当前选中的笔画粗细按钮
    private FrameLayout flFocusPaint;

    private ViewPager vpager;// 滑动布局
    //    private PullRefreshListView prlstvFiles;//授课列表(刷新、加载的另一种实现形式)
    private ListView lstvFiles;//授课列表
    private LinearLayout llNoFile;//没有授课
    private LinearLayout llWrapper01;//ppt、viewpager区域
    private LinearLayout llPreviewContent;//预览Html区域
    private RelativeLayout rlVideo;// 音视频
    private ImageView ivNoData;// 无预览授课情况下的展示图片

    //    PullDown vPullDown;
    private PullDownView vPullDown;//授课列表下拉刷新、上拉加载更多

    private LinearLayout ll01Last;

    public AttendClassDetailFg() {
    }

    public AttendClassDetailFg(String catalogID, String catalogName) {
//        this.catalogIDCurr = catalogID;
//        this.catalogNameCurr = catalogName;
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == allFgView) {
            allFgView = inflater.inflate(
                    R.layout.layout_fg_attend_class_detail, null);

            uiHandler = new Handler(getActivity().getMainLooper());
            vUtils = new ViewUtils(getActivity());
            sUtils = new ServerRequestUtils(getActivity());
            fileList = new ArrayList<File01>();
            idFileChoicedList = new ArrayList<String>();
            idList = new ArrayList<String>();
            planList = new ArrayList<DataInfo>();
            coursewareList = new ArrayList<Courseware>();
            testList = new ArrayList<TestData>();
            omicsList = new ArrayList<TestData>();
            txtList = new ArrayList<String>();
            videoList = new ArrayList<VideoAudio>();

            String catalogIDCurr = PreferencesUtils.acquireInfoFromPreferences(getActivity(), ConstantsForPreferencesUtils.CATALOG_ID_CHOICED);
            if (!ValidateFormatUtils.isEmpty(catalogIDCurr)) {
                this.catalogIDCurr = catalogIDCurr;
            }

            String catalogNameCurr = PreferencesUtils.acquireInfoFromPreferences(getActivity(), ConstantsForPreferencesUtils.CATALOG_NAME_CHOICED);
            if (!ValidateFormatUtils.isEmpty(catalogNameCurr)) {
                this.catalogNameCurr = catalogNameCurr;
            }

            llTitle = (LinearLayout) allFgView
                    .findViewById(R.id.ll_title_layout_fg_attend_class_detail);

            tvTitleName = (TextView) allFgView
                    .findViewById(R.id.tv_title_name_layout_fg_attend_class_detail);

            llWrapper01 = (LinearLayout) allFgView
                    .findViewById(R.id.ll_wrapper01_layout_fg_attend_class_detail);

            llPreviewContent = (LinearLayout) allFgView
                    .findViewById(R.id.ll_preview_content_layout_fg_attend_class_detail);

            // 没有数据
            ivNoData = (ImageView) allFgView
                    .findViewById(R.id.iv_no_data_layout_aty_attend_class_detail);
            ivNoData.setVisibility(View.GONE);

            initBoard();

            //绘制面板
            llDrawMenu = (LinearLayout) allFgView
                    .findViewById(R.id.ll_draw_menu_layout_fg_attend_class_detail);
            View inDrawMenu = (View) allFgView
                    .findViewById(R.id.in_draw_menu_layout_fg_attend_class_detail);
            inDrawMenu.setVisibility(View.GONE);

            //本地上传(授课文件)
            TextView tvFileLoad = (TextView) allFgView
                    .findViewById(R.id.tv_file_upload_layout_fg_attend_class_detail);
            tvFileLoad.setOnClickListener(new Listeners());

            int menuLength = 0;
            if (inDrawMenu instanceof ViewGroup) {
                menuLength = ((ViewGroup) inDrawMenu).getChildCount();
            }
            if (drawMenuItemResID.length <= menuLength) {
                for (int i = 0; i < menuLength; i++) {
                    View vItem = ((ViewGroup) inDrawMenu).getChildAt(i);

                    initDrawMenu(allFgView, vItem, drawMenuItemResID[i]);
                }
            }

            initGallery(allFgView);

            vPullDown = (PullDownView) allFgView
                    .findViewById(R.id.v_files_catalog_layout_fg_attend_class_detail);
            initPullDownLstv(vPullDown);
            lstvFiles = vPullDown.getListView();
            // 授课列表
//            prlstvFiles = (PullRefreshListView) allFgView
//                    .findViewById(R.id.lsv_files_catalog_layout_fg_attend_class_detail);

            initPullDownLstv(vPullDown);

            //没有授课数据
            llNoFile = (LinearLayout) allFgView
                    .findViewById(R.id.ll_no_data_layout_fg_attend_class_detail);
            // 音视频
            rlVideo = (RelativeLayout) allFgView.findViewById(R.id.rl_video_content_layout_fg_attend_class_detail);
            rlVideo.setVisibility(View.GONE);

//            setPrlstvFilesListeners();

            initPagerViews();

            boolean hasChoicedMaterial = PreferencesUtils.acquireBooleanInfoFromPreferences(getActivity(), ConstantsUtils.HAS_CHOICED_MATERIAL);
            if (!hasChoicedMaterial) {
                // 跳转至选择教材目录界面
                Intent intent = new Intent(getActivity(),
                        ChoiceTeachingMaterialAty.class);
                intent.putExtra(ChoiceTeachingMaterialAty.CATALOG_POS, 0);
                startActivity(intent);
            }
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
     * 初始化viewPager相关布局
     */
    private void initPagerViews() {
        // 教案内容，滑动布局
        vpager = (ViewPager) allFgView
                .findViewById(R.id.vpager_content_layout_fg_attend_class_detail);
        // 限制加载数量
        vpager.setOffscreenPageLimit(2);
        // 监听
        vpager.setOnPageChangeListener(new VPagerChangeListener());

//        setVPagerAdapter(0);
    }

    /**
     * 获取假设授课数据
     *
     * @return
     */
    private List<File01> getFileData() {
        // 右侧文件
        List<File01> fileList = new ArrayList<File01>();
        File01 file01 = new File01();
        file01.setDataName("数据分析表.doc");
        file01.setSize("1.17M");
        File01 file02 = new File01();
        file02.setDataName("数据分析表.pdf");
        file02.setSize("5.08M");
        File01 file03 = new File01();
        file03.setDataName("数据演示.ppt");
        file03.setSize("3.5M");
        File01 file04 = new File01();
        file04.setDataName("数据分析表.txt");
        file04.setSize("1.45M");
        fileList.add(file01);
        fileList.add(file02);
        fileList.add(file03);
        fileList.add(file04);

        return fileList;
    }

    /**
     * 授课列表适配器
     */
    private void setLstvFileAdapter(boolean isShowChoiceMenu) {
        if (filesAdapter == null) {
            callback = (InterfacesCallback.ICanDoSth) this;
            filesAdapter = new FilesListAdapter(getActivity(), fileList, callback);
            lstvFiles.setAdapter(filesAdapter);
        } else {
            filesAdapter.notifyDataSetChanged();
        }
    }

//    private void setPrlstvFilesListeners() {
//        prlstvFiles.setOnRefreshListener(new PullRefreshListView.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // 重置页码
//                currentPageNum = 1;
//                // 请求数据
//                requestFileListFromServer();
//            }
//
//            @Override
//            public void onLoadMore() {
//                currentPageNum = currentPageNum + 1;
//                requestFileListFromServer();
//            }
//        });
//    }

    /**
     * 初始化上拉刷新下拉加载
     *
     * @param vPullDown
     */
    private void initPullDownLstv(PullDownView vPullDown) {
        vPullDown.setOnPullDownListener(new PullDownView.OnPullDownListener() {
            @Override
            public void onRefresh() {
                // 重置页码
                currentPageNumForFile = 1;
                // 请求数据
                requestFileListFromServer();
            }

            @Override
            public void onMore() {
                currentPageNumForFile = currentPageNumForFile + 1;
                requestFileListFromServer();
            }
        });
    }

    /**
     * 从服务器获取授课文件列表
     */
    private void requestFileListFromServer() {
        DataID01 dataID01 = new DataID01();

        if (!TextUtils.isEmpty(VariableUtils.catalogID)) {
            chapterID = VariableUtils.catalogID;
        } else {
            chapterID = PreferencesUtils.acquireInfoFromPreferences(getActivity(), ConstantsForPreferencesUtils.CATALOG_ID_CHOICED);
        }
        dataID01.setChid(chapterID);//章节ID

        dataID01.setCid(classID);//班级ID

        Bean bean = new Bean();
        bean.setData(dataID01);
        bean.setIndex(String.valueOf(currentPageNumForFile));
        bean.setSize(ConstantsForServerUtils.PAGE_SIZE_DEFAULT_VALUE);

        Gson gson = new Gson();
        String json = gson.toJson(bean);

        sUtils.request("getLectureList", json, "", ServerRequestUtils.REQUEST_LONG_TIME, new ServerRequestUtils.OnServerRequestListener2() {
            @Override
            public void onFailure(final String msg) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPageNumForFile == 1) {// 首页数据
                            llNoFile.setVisibility(View.VISIBLE);
                            lstvFiles.setVisibility(View.GONE);
                        }
                        vUtils.dismissDialog();
                        if (!TextUtils.isEmpty(msg)) {
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "资源获取失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onResponse(String msg, JSONArray data, String count) {
                // 重置数据
                if (currentPageNumForFile == 1 && fileList.size() > 0) {
                    fileList.clear();
                }

                List<File01> list = com.alibaba.fastjson.JSON.parseArray(data.toString(), File01.class);

                //假数据
                //List<File01> list = getFileData();

                if (list != null) {
                    if (list.size() == 0) {
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (currentPageNumForFile == 1) {// 首页数据
                                    llNoFile.setVisibility(View.VISIBLE);
                                    lstvFiles.setVisibility(View.GONE);

                                    // 刷新显示没有更多数据
                                    vPullDown.notifyDidNoMore();
                                    vPullDown.setVisibility(View.GONE);
                                } else {
                                    // 刷新显示没有更多数据
                                    vPullDown.notifyDidNoMore();
                                    // prlstvFiles.loadMoreComplete();
                                    Toast.makeText(getActivity(), "没有更多数据啦", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        fileList.addAll(list);

                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                llNoFile.setVisibility(View.GONE);
                                lstvFiles.setVisibility(View.VISIBLE);
                                vPullDown.setVisibility(View.VISIBLE);

                                setLstvFileAdapter(false);
                                vPullDown.notifyDidLoad();

                                if (currentPageNumForFile == 1) {
                                    lstvFiles.setSelection(0);
                                    // 隐藏刷新模块
                                    vPullDown.notifyDidRefresh();
//                                prlstvFiles.refreshComplete();
                                } else {
                                    final int lastVisiblePostion = lstvFiles
                                            .getLastVisiblePosition();
                                    lstvFiles.setSelection(lastVisiblePostion);

                                    // 隐藏加载更多模块
                                    vPullDown.notifyDidMore();
//                                prlstvFiles.loadMoreComplete();
                                }
                            }
                        });
                    }

                    fileFocus = fileList.get(0);
//                    fileType = fileFocus.getFileType();
                    if (fileFocus != null) {
                        requestFileDetailFromServer(fileFocus.getFileType(), fileFocus.getDataID());
                    }

                    hasLoadOnce = true;
                }
//                uiHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        vUtils.dismissDialog();
//                    }
//                });


            }
        });
    }

    /**
     * 设置授课viewPager适配器
     *
     * @param position 某个位置
     */
    private void setVPagerAdapter(int position) {
        vpager.removeAllViews();

        vpager.setVisibility(View.VISIBLE);


        if (ConstantsForServerUtils.PPT.equals(fileFocus.getFileType()) || ConstantsForServerUtils.IMAGES.equals(fileFocus.getFileType())) {// 图片类
            setPicVPagerAdapter(position);
        } else if (ConstantsForServerUtils.HTML.equals(fileFocus.getFileType())) {//试题、学案Html类型
        } else {// 文字格式（学案、教案、试题、文档）等
            setWordVPagerAdapter(position);
        }

//        // 设置适配器
//        if (picVpagerAdapter == null) {
//            picVpagerAdapter = new CustomPagerAdapter03(getActivity(),
//                    fileContents);
//            vpager.setAdapter(picVpagerAdapter);
//
//            // 默认设置到中间的某个位置
//            // if (coursewareList.size() > 0) {
//            // int pos = Integer.MAX_VALUE / 2
//            // - (Integer.MAX_VALUE / 2 % rightInfoList.size());
//            // 2147483647 / 2 = 1073741823 - (1073741823 % 5)
//            // }
//        } else {
//            // 刷新布局
//            picVpagerAdapter.notifyDataSetChanged();
//        }

        //  设置到某个位置
        vpager.setCurrentItem(position);
    }

    /**
     * 设置图片展示的viewPager适配器
     *
     * @param position 某个位置
     */
    private void setPicVPagerAdapter(int position) {
        // 设置适配器
        if (picVpagerAdapter == null) {
            picVpagerAdapter = new CustomPagerAdapter03(getActivity(),
                    coursewareList);
            vpager.setAdapter(picVpagerAdapter);

            // 默认设置到中间的某个位置
            // if (coursewareList.size() > 0) {
            // int pos = Integer.MAX_VALUE / 2
            // - (Integer.MAX_VALUE / 2 % rightInfoList.size());
            // 2147483647 / 2 = 1073741823 - (1073741823 % 5)
            // }
        } else {
            // 刷新布局
            picVpagerAdapter.notifyDataSetChanged();
        }

        // 重置文字适配器
        wordVPagerAdapter = null;
    }

    /**
     * 设置右侧文字展示的viewPager适配器
     *
     * @param position 某个位置
     */
    private void setWordVPagerAdapter(int position) {
        // 设置适配器
        if (wordVPagerAdapter == null) {
            wordVPagerAdapter = new CustomPagerAdapter04(getActivity(),
                    planList);
            vpager.setAdapter(wordVPagerAdapter);

            // 默认设置到中间的某个位置
            // if (rightInfoList.size() > 0) {
            // int pos = Integer.MAX_VALUE / 2
            // - (Integer.MAX_VALUE / 2 % rightInfoList.size());
            // 2147483647 / 2 = 1073741823 - (1073741823 % 5)
            // }
        } else {
            // 刷新布局
            wordVPagerAdapter.notifyDataSetChanged();
        }

        // 重置图片适配器
        picVpagerAdapter = null;
    }

    /**
     * 设置试题Html类型展示的viewPager适配器
     *
     * @param position 某个位置
     */
    private void setHtmlVPagerAdapter(int position) {
        // 设置适配器
        if (wordVPagerAdapter == null) {
            wordVPagerAdapter = new CustomPagerAdapter04(getActivity(),
                    planList);
            vpager.setAdapter(wordVPagerAdapter);

            // 默认设置到中间的某个位置
            // if (rightInfoList.size() > 0) {
            // int pos = Integer.MAX_VALUE / 2
            // - (Integer.MAX_VALUE / 2 % rightInfoList.size());
            // 2147483647 / 2 = 1073741823 - (1073741823 % 5)
            // }
        } else {
            // 刷新布局
            wordVPagerAdapter.notifyDataSetChanged();
        }

        // 重置图片适配器
        picVpagerAdapter = null;
    }


    /**
     * 初始化画布
     */
    private void initBoard() {
        // 画布
        sfvBoard = (CustomSurfaceView) allFgView
                .findViewById(R.id.sfv_board_layout_fg_attend_class_detail);
        sfvBoard.setZOrderOnTop(true);
        sfvBoard.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        sfvBoard.setVisibility(View.GONE);
        sfvBoard.surfaceDestroyed(sfvBoard.getHolder());
    }

    private void initDrawMenu(View allFgView, View v, int ResID) {
        v = (View) allFgView.findViewById(ResID);
        v.setOnClickListener(new Listeners());
    }

    /**
     * 初始化画笔型号按钮（粗细）
     *
     * @param view
     */
    private void initPaintSizeMenu(View view) {
        // 细线(圆形标志)
        flSmallPaint = (FrameLayout) view
                .findViewById(R.id.fl_small_paint_layout_v_draw_menu);
        llSmallPaint = (LinearLayout) window.getContentView().findViewById(
                R.id.ll_small_paint_layout_v_draw_menu);
        ivSmallPaint = (ImageView) window.getContentView().findViewById(
                R.id.iv_small_paint_layout_v_draw_menu);
        flSmallPaint.setOnClickListener(new Listeners());

        // 中等线条(圆形标志)
        flMiddlePaint = (FrameLayout) view
                .findViewById(R.id.fl_middle_paint_layout_v_draw_menu);
        llMiddlePaint = (LinearLayout) window.getContentView().findViewById(
                R.id.ll_middle_paint_layout_v_draw_menu);
        ivMiddlePaint = (ImageView) window.getContentView().findViewById(
                R.id.iv_middle_paint_layout_v_draw_menu);
        flMiddlePaint.setOnClickListener(new Listeners());

        // 粗线条(圆形标志)
        flLargePaint = (FrameLayout) view
                .findViewById(R.id.fl_large_paint_layout_v_draw_menu);
        llLargePaint = (LinearLayout) window.getContentView().findViewById(
                R.id.ll_large_paint_layout_v_draw_menu);
        ivLargePaint = (ImageView) window.getContentView().findViewById(
                R.id.iv_large_paint_layout_v_draw_menu);
        flLargePaint.setOnClickListener(new Listeners());

        if (flFocusPaint == null) {// 默认情况下选中第一个
            flFocusPaint = flSmallPaint;
        }

        if (ConstantsUtils.GRAFFITI == drawStyle) {
            setPaintSizeMenu(0);
        }
    }

    /**
     * 初始化颜色区域
     *
     * @param view
     */
    private void initColorArea(View view) {
        GridView gdvColor = (GridView) view
                .findViewById(R.id.gdv_color_layout_v_draw_menu);

        gdvColor.setAdapter(new ColorAdapter(getActivity(), DrawUtils.colorRes));
        gdvColor.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos,
                                    long id) {
                isRepaint = false;

                // 赋值
                colorCur = pos;
                // 设置粗细按钮
                setPaintSizeMenu(pos);

                paint(paintSizeCur);
            }
        });
    }

    /**
     * 显示泡泡弹框：绘制时的画笔颜色、粗细的选择器
     */
    @SuppressWarnings("deprecation")
    private void showSpinWindow(int windowViewResID, int w, int h,
                                int distanceW, int distanceH) {
        dismissWindow();

        View view = LayoutInflater.from(getActivity()).inflate(windowViewResID,
                null);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        window = new BgDarkPopupWindow(getActivity(), view,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // window.setDarkStyle(R.style.MyDarkStyle);
        // window.setDarkColor(Color.parseColor("#a0000000"));
        // window.resetDarkPosition();

        // 设置弹框宽度
        window.setWidth(w);
        // 设置弹框高度
        window.setHeight(h);

        window.darkAbove(llDrawMenu);
        window.showAsDropDown(tvTitleName, distanceW, distanceH);
        // 点击空白处弹框消失
        // window.setOutsideTouchable(true);
        // window.setFocusable(true);

        // 这句对于控制弹框消失的功能来说很重要，对背景不会有影响
        window.setBackgroundDrawable(new BitmapDrawable());

        window.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                // setNormalEffect(ivArrow);
            }
        });

        initPaintSizeMenu(view);

        if (ConstantsUtils.GRAFFITI == drawStyle) {
            initColorArea(view);
        }
    }

    // private void setMenuAdapter(View view) {
    // GridView gdvSize = (GridView) view
    // .findViewById(R.id.gdv_size_layout_v_draw_menu);
    // paintSizeList = new ArrayList<PaintSize>();
    //
    // PaintSize size1 = new PaintSize();
    // size1.setId("01");
    // size1.setSize(5);
    //
    // PaintSize size2 = new PaintSize();
    // size2.setId("02");
    // size2.setSize(10);
    //
    // PaintSize size3 = new PaintSize();
    // size3.setId("03");
    // size3.setSize(15);
    //
    // paintSizeList.add(size1);
    // paintSizeList.add(size2);
    // paintSizeList.add(size3);
    // gdvSize.setAdapter(new DrawPaintSizeAdapter(getActivity(),
    // paintSizeList));
    //
    // gdvSize.setOnItemClickListener(new OnItemClickListener() {
    // @Override
    // public void onItemClick(AdapterView<?> parent, View v, int pos,
    // long id) {
    //
    // }
    // });
    // }

    /**
     * 以画廊效果展示文件内容（PPT）
     *
     * @param v
     */
    private void initGallery(View v) {
        rlFile = (RelativeLayout) v
                .findViewById(R.id.rl_wrapper01_layout_fg_attend_class_detail);

        // 实例化控件
        glFile = (Gallery) v
                .findViewById(R.id.gl_file_layout_fg_attend_class_detail);

//        fileContents = new ArrayList<FileContent>();
//        FileContent fileContent01 = new FileContent();
//        fileContent01.setPageNumber("1");
//        fileContent01.setIvRes(ivsRes[0]);
//
//        FileContent fileContent02 = new FileContent();
//        fileContent02.setPageNumber("2");
//        fileContent02.setIvRes(ivsRes[1]);
//
//        FileContent fileContent03 = new FileContent();
//        fileContent03.setPageNumber("3");
//        fileContent03.setIvRes(ivsRes[2]);
//
//        FileContent fileContent04 = new FileContent();
//        fileContent04.setPageNumber("4");
//        fileContent04.setIvRes(ivsRes[3]);
//
//        FileContent fileContent05 = new FileContent();
//        fileContent05.setPageNumber("5");
//        fileContent05.setIvRes(ivsRes[4]);
//
//        FileContent fileContent06 = new FileContent();
//        fileContent06.setPageNumber("6");
//        fileContent06.setIvRes(ivsRes[5]);
//
//        fileContents.add(fileContent01);
//        fileContents.add(fileContent02);
//        fileContents.add(fileContent03);
//        fileContents.add(fileContent04);
//        fileContents.add(fileContent05);
//        fileContents.add(fileContent06);

        setGridViewAdapter(0);

        // 设置左边距为负数，达到默认居左的显示效果
        MarginLayoutParams layoutParams = (MarginLayoutParams) glFile
                .getLayoutParams();
        WindowManager wm = (WindowManager) getActivity().getSystemService(
                Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        layoutParams.setMargins(-width * 6 / 13, 0, 0, 0);

        // 给Gallery添加监听
        glFile.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long arg3) {

                // 重新赋值
                prePosition = position;

//                if (ll01Last != null) {
//                    ll01Last.setBackgroundResource(R.color.transparent);
//                }
//                LinearLayout ll01 = parent.findViewById(R.id.ll_01_layout_adapter_item_for_file_content);
//                ll01.setBackgroundResource(R.drawable.selector_for_green_stroke_rectangle);

//                ll01Last = ll01;
                setVPagerAdapter(position);
//                setGridViewAdapter(position);

                isRepaint = true;

                sfvBoard.resetCanvas();
                sfvBoard.resetPathList();

                sfvBoard.setVisibility(View.GONE);
                sfvBoard.surfaceDestroyed(sfvBoard.getHolder());

                dismissWindow();
            }
        });

        // 上一个
        tvPrevious = (TextView) allFgView
                .findViewById(R.id.ll_previous_layout_fg_attend_class_detail);
        tvPrevious.setOnClickListener(new Listeners());

        // 下一个
        tvNext = (TextView) allFgView
                .findViewById(R.id.ll_next_layout_fg_attend_class_detail);
        tvNext.setOnClickListener(new Listeners());

        // 切换教材、目录
        tvSwitchMaterial = (TextView) allFgView
                .findViewById(R.id.tv_switch_material_layout_fg_attend_class_detail);
        tvSwitchMaterial.setOnClickListener(new Listeners());

    }

    private void setGridViewAdapter(int pos) {
//        if (glAdapter == null) {
        // 实例化ImageAdapter适配器
        glAdapter = new GalleryAdapter(getActivity(), coursewareList);
        // 将适配器的数据存储到Gallery组件中（设置Gallery组件的Adapter对象）
        glFile.setAdapter(glAdapter);
        glFile.setUnselectedAlpha(1f);
//        } else {
//            glAdapter.notifyDataSetChanged();
//        }

        if (pos > 0) {
            glFile.setSelection(pos - 1);
        }
//        if (coursewareList.size() > 2 && pos < 4) {
//            // 设置第二个数据在中央位置
//            glFile.setSelection(2);
//        } else {
//            glFile.setSelection(pos);
//        }

        glAdapter.setCurrentPos(pos);
    }

    /**
     * 绘制
     */
    private void paint(float size) {
        if (isRepaint) {
            // 重画
            sfvBoard.resetCanvas();
            // 重置数据
            sfvBoard.resetPathList();
        }

        // 设置画笔粗细
        sfvBoard.setPaintSize(size);
        // 画笔颜色
        sfvBoard.setPaintColor(DrawUtils.colorRes[colorCur]);
        // 画笔其它属性
        if (ConstantsUtils.GRAFFITI == drawStyle) {
            sfvBoard.setPaintBrush();
        } else if (ConstantsUtils.ERASER == drawStyle) {
            sfvBoard.setEraser();
        }

        sfvBoard.setVisibility(View.VISIBLE);
    }

    /**
     * 设置粗细按钮（顺序不可打乱，与colorRes数组相对应）
     *
     * @param pos 位置
     */
    @SuppressLint("NewApi")
    private void setPaintSizeMenu(int pos) {
        switch (pos) {
            case 0:// 红色
                setPaintSizeMenuColor(R.drawable.selector_for_red_disc);
                break;
            case 1:// 蓝色
                setPaintSizeMenuColor(R.drawable.selector_for_blue_disc);
                break;
            case 2:// 白色
                setPaintSizeMenuColor(R.drawable.selector_for_white_disc);
                break;
            case 3:// 绿色
                setPaintSizeMenuColor(R.drawable.selector_for_green_disc);
                break;
        }
    }

    /**
     * 设置粗细按钮的颜色
     *
     * @param res 样式资源
     */
    private void setPaintSizeMenuColor(int res) {
        ivSmallPaint.setBackgroundResource(res);
        ivMiddlePaint.setBackgroundResource(res);
        ivLargePaint.setBackgroundResource(res);
    }

    /**
     * 更改画笔大小之后的操作
     */
    private void doSthAfterChangePaintSize(int vi01, int vi02, int vi03) {
        llSmallPaint.setVisibility(vi01);
        llMiddlePaint.setVisibility(vi02);
        llLargePaint.setVisibility(vi03);
    }

    /**
     * 关闭弹框
     */
    private void dismissWindow() {
        if (window != null && window.isShowing()) {
            window.dismiss();
        }
    }

    /**
     * 展示PPT课件，以ViewPager滑动+缩略图方式展示
     */
    private void previewPPT() {
        llWrapper01.setVisibility(View.VISIBLE);

        //画廊
        rlFile.setVisibility(View.VISIBLE);
        llDrawMenu.setVisibility(View.VISIBLE);

        llPreviewContent.setVisibility(View.GONE);
    }

    /**
     * 展示word格式文件，以ViewPager滑动方式展示
     */
    private void previewWord() {
        llWrapper01.setVisibility(View.VISIBLE);

        //画廊
        rlFile.setVisibility(View.GONE);
        llDrawMenu.setVisibility(View.GONE);

        llPreviewContent.setVisibility(View.GONE);
    }

    /**
     * 展示Html格式文件（如组学案、组试题），以普通上下滑动方式展示
     */
    private void previewHtml() {
        llWrapper01.setVisibility(View.GONE);

        llPreviewContent.setVisibility(View.VISIBLE);
    }

    /**
     * 展示图片文件，以gridView方式展示
     */
    private void previewPic() {
        llWrapper01.setVisibility(View.GONE);

        llPreviewContent.setVisibility(View.VISIBLE);
    }

    /**
     * 展示音视频
     */
    private void previewAudioVideo(List<VideoAudio> videoList) {
        llWrapper01.setVisibility(View.GONE);

        //画廊
        rlFile.setVisibility(View.GONE);
        llDrawMenu.setVisibility(View.GONE);
        llPreviewContent.setVisibility(View.GONE);

        rlVideo.setVisibility(View.VISIBLE);

        // 初始化视频
        videoCurr = videoList.get(0);
        initVideo(videoCurr);
    }


    /**
     * 从服务器获取文件
     */
    private void requestFileDetailFromServer(String fileType, String ID) {
        DataID01 data = new DataID01();
        data.setDataid(ID);
        data.setIndex(String.valueOf(currentPageNumForPreview));
        Gson gson = new Gson();
        String jsonStr = gson.toJson(data);

        if (ConstantsForServerUtils.WORD.equals(fileType)) {// 文字格式（学案、教案）
            requestFileDetailJsonArrFromServer("getLectureView", jsonStr, ID);
        } else if (ConstantsForServerUtils.HTML.equals(fileType)) {//Html
            requestFileDetailJsonArrFromServer("getLectureInfo", jsonStr, ID);

        } else if (ConstantsForServerUtils.PPT.equals(fileType)) {//组课件ppt格式
            requestFileDetailJsonArrFromServer("getLectureInfo", jsonStr, ID);
        } else if (ConstantsForServerUtils.TXT.equals(fileType) || ConstantsForServerUtils.IMAGES.equals(fileType) || ConstantsForServerUtils.VIDEO.equals(fileType)) {//图片
            requestFileDetailJsonObjFromServer("getLectureInfo", jsonStr, ID);
        } else if (ConstantsForServerUtils.SWF.equals(fileType)) {// flash动画类,只有swf格式
        } else {//其它未知格式
            requestFileDetailJsonArrFromServer("getLectureView", jsonStr, ID);
        }
    }

    private void requestFileDetailJsonArrFromServer(String methodName, String jsonStr, String id) {
        sUtils.request(methodName, jsonStr, "", ServerRequestUtils.REQUEST_SHORT_TIME, new ServerRequestUtils.OnServerRequestListener2() {
            @Override
            public void onFailure(final String msg) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setWordVPagerAdapter(0);
                        vpager.setVisibility(View.GONE);

                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        ivNoData.setVisibility(View.VISIBLE);

                        vUtils.dismissDialog();
                    }
                });
            }


            @Override
            public void onResponse(String msg, JSONArray data, String count) {
                rightInfoCount = count;

                showRightInfo(data);
            }
        });
    }

    private void requestFileDetailJsonObjFromServer(String methodName, String jsonStr, String id) {
        sUtils.request(methodName, jsonStr, "", ServerRequestUtils.REQUEST_SHORT_TIME, new ServerRequestUtils.OnServerRequestListener() {
            @Override
            public void onFailure(final String msg) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setWordVPagerAdapter(0);
                        vpager.setVisibility(View.GONE);

                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        ivNoData.setVisibility(View.VISIBLE);

                        vUtils.dismissDialog();
                    }
                });
            }

            @Override
            public void onResponse(String msg, JSONObject data, String count) {
                dealWithPreviewDataInfoData(data);
            }
        });
    }

    private void showRightInfo(JSONArray dataArr) {
        if (dataArr != null) {
//        if (true) {
//            fileType = ConstantsForServerUtils.VIDEO;

            if (dataArr.length() == 0) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setVPagerAdapter(0);
                        vpager.setVisibility(View.GONE);

                        ivNoData.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "暂无预览，切换别的文件试试",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ivNoData.setVisibility(View.GONE);
                    }
                });

                if (ConstantsForServerUtils.PPT.equals(fileFocus.getFileType())) {// 课件ppt格式
                    coursewareList = com.alibaba.fastjson.JSON.parseArray(dataArr.toString(), Courseware.class);
                    rightInfoCount = String.valueOf(coursewareList.size());
                    needMultiPageRequestPreview = false;

                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            previewPPT();
                            setGridViewAdapter(newPosition);
                            setVPagerAdapter(0);

                            vUtils.dismissDialog();
                        }
                    });
                } else if (ConstantsForServerUtils.HTML.equals(fileFocus.getFileType())) {// Html格式
                    if (getActivity().getResources().getString(R.string.group_test).equals(fileFocus.getSource())) {//试题
                        resetTestList();
                        testList = com.alibaba.fastjson.JSON.parseArray(dataArr.toString(), TestData.class);
                        rightInfoCount = String.valueOf(testList.size());
                        needMultiPageRequestPreview = false;

                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                previewHtml();
                                PreviewTestFg fg = new PreviewTestFg(testList, true);
                                setFgContent(fg);

                                vUtils.dismissDialog();
                            }
                        });
                    } else if (getActivity().getResources().getString(R.string.group_omics).equals(fileFocus.getSource())) {//学案
                        resetOmicsList();
                        omicsList = com.alibaba.fastjson.JSON.parseArray(dataArr.toString(), TestData.class);
                        rightInfoCount = String.valueOf(omicsList.size());
                        needMultiPageRequestPreview = false;

                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                previewHtml();
                                PreviewOmicsFg fg = new PreviewOmicsFg(omicsList, true);
                                setFgContent(fg);

                                vUtils.dismissDialog();
                            }
                        });
                    }
                } else if (ConstantsForServerUtils.TXT.equals(fileFocus.getFileType())) {//txt文档类
                    resetTxtList();
                    txtList = getTxtList(dataArr);

                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            PreviewTxtFg wordFg = new PreviewTxtFg(txtList);
//                            txtCallback = (InterfacesCallback.ICanKnowSth8) wordFg;
                            setFgContent(wordFg);

                            vUtils.dismissDialog();
                        }
                    });
                } else if (ConstantsForServerUtils.IMAGES.equals(fileFocus.getFileType())) {//图片
                    final List<Picture> picList = com.alibaba.fastjson.JSON.parseArray(dataArr.toString(), Picture.class);
                    if (picList != null) {
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                previewPic();

                                PreviewPicFg fg = new PreviewPicFg(picList);
                                picCallback = (InterfacesCallback.ICanKnowSth6) fg;
                                setFgContent(fg);

                                vUtils.dismissDialog();
                            }
                        });
                    }
                } else if (ConstantsForServerUtils.AUDIO.equals(fileFocus.getFileType()) || ConstantsForServerUtils.VIDEO.equals(fileFocus.getFileType())) {//音频、视频
                    String url = "";
                    try {
                        url = dataArr.getString(0);
                    } catch (JSONException e) {
                        e.printStackTrace();

//                        ivNoData.setVisibility(View.VISIBLE);
//                        tvNum.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "数据返回不合规范！", Toast.LENGTH_SHORT).show();
                    }

                    resetVideoList();
                    url = "/upload/extend/video/201807/06/201807061457305988.mp4";

                    if (videoList != null && !TextUtils.isEmpty(url)) {
                        VideoAudio va = new VideoAudio();
                        va.setTitle("视频" + String.valueOf(1));
                        va.setPath(UrlUtils.PREFIX + url);

                        // 添加
                        videoList.add(va);

                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                previewAudioVideo(videoList);

                                vUtils.dismissDialog();
                            }
                        });
                    }
                }

                if (ConstantsForServerUtils.WORD.equals(fileFocus.getFileType())) {// 文字格式（学案、教案）
                    for (int i = 0; i < dataArr.length(); i++) {
                        if (dataArr.isNull(i)) {
                            continue;
                        }

                        JSONObject jsonObj = null;
                        try {
                            jsonObj = dataArr.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        DataInfo info = com.alibaba.fastjson.JSON.parseObject(
                                jsonObj.toString(), DataInfo.class);

                        needMultiPageRequestPreview = true;

                        planList.add(info);
                        if (planList.size() < Integer.parseInt(rightInfoCount)) {//循环请求单页数据
                            currentPageNumForPreview = currentPageNumForPreview + 1;

                            DataID01 data = new DataID01();
                            data.setDataid(fileFocus.getDataID());
                            data.setIndex(String.valueOf(currentPageNumForPreview));
                            Gson gson = new Gson();
                            String jsonStr = gson.toJson(data);

                            requestFileDetailJsonArrFromServer("getLectureView", jsonStr, fileFocus.getDataID());
                        } else {
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    previewWord();
                                    setVPagerAdapter(0);
                                }
                            });
                        }

//                        if (planList.size() == 0) {
//                            planList.add(info);
//
//                            // 造一个假数据放到尾端，便于viewPager滑动监听
//                            planList.add(new DataInfo());
//                        } else if (planList.size() > 1) {
//                            planList.remove(planList.size() - 1);// 先去掉尾端的假数据
//                            planList.add(planList.size(), info);// 在尾端加上新数据
//
//                            if (String.valueOf(planList.size()).equals(rightInfoCount)) {//最后一条数据的情况
//                            } else {
//                                // 造一个假数据放到尾端，便于viewPager滑动监听
//                                planList.add(new DataInfo());
//                            }
//                        }
//
//                    }
                    }

                    hasLoadOnce = true;
                }
            }
        } else {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    setVPagerAdapter(0);
                    vpager.setVisibility(View.GONE);

                    ivNoData.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "暂无预览，切换文件试试？",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                vUtils.dismissDialog();
            }
        });
    }

    /**
     * 添加题目Fragment
     */
    private void setFgContent(Fragment fg) {
        FragmentManager manager = getActivity().getSupportFragmentManager();//FragmentManager调用v4包内的
        FragmentTransaction transaction = manager.beginTransaction();//FragmentTransaction调用v4包内的（FragmentTransaction transaction声明成局部的）

        if (fg.isAdded()) {
            transaction.show(fg).commit();
        } else {
            transaction.replace(R.id.ll_preview_content_layout_fg_attend_class_detail, fg).commit();//替换为名称为A的fragment并显示它
        }
    }


    private void dealWithPreviewDataInfoData(JSONObject data) {
        JSONArray dataInfoArr = ServerDataAnalyzeUtils.getDataAsJSONArray(data, ConstantsForServerUtils.DATAINFO);
        showRightInfo(dataInfoArr);
    }


    /**
     * 设置音视频播放器
     */
    private void initVideo(VideoAudio videoCurr) {
        // 将视频fragment填充到本activity中
        FragmentManager fgManager = getActivity().getSupportFragmentManager();// v4包内提供的方法
        FragmentTransaction fgTransaction = fgManager.beginTransaction();

        if (videoFg != null) {
            fgTransaction.remove(videoFg);
        }

        if (videoCurr != null) {
            videoFg = new PlayVideoFragment(videoCurr.getPath(),
                    videoCurr.getTitle(), videoList, false, "0");
            callbackForVideo = (OnListenerForPlayVideoCallback) videoFg;
        }

        fgTransaction.add(R.id.rl_video_content_layout_fg_attend_class_detail, videoFg);
        fgTransaction.commit();
    }

    /**
     * 重置教案学案等文字类数据
     */
    private void resetPlanList() {
        // 右侧课件
        if (planList.size() > 0) {
            planList.clear();
        }
    }

    /**
     * 重置试题类数据
     */
    private void resetTestList() {
        // 右侧课件
        if (testList.size() > 0) {
            testList.clear();
        }
    }

    /**
     * 重置学案类数据(来源：组学案)
     */
    private void resetOmicsList() {
        // 右侧课件
        if (omicsList.size() > 0) {
            omicsList.clear();
        }
    }

    /**
     * 重置文档txt类数据(来源：扩展资源)
     */
    private void resetTxtList() {
        // 右侧课件
        if (txtList.size() > 0) {
            txtList.clear();
        }
    }


    /**
     * 重置课件ppt数据
     */
    private void resetCoursewareList() {
        // 右侧课件
        if (coursewareList.size() > 0) {
            coursewareList.clear();
        }
    }


//    /**
//     * 重置右侧具体数据
//     */
//    private void resetRightInfoList() {
//        // 右侧课件
//        if (fileList.size() > 0) {
//            fileList.clear();
//        }
//    }


    /**
     * 重置音视频数据
     */
    private void resetVideoList() {
        // 右侧课件
        if (videoList.size() > 0) {
            videoList.clear();
        }
    }


    private ArrayList<String> getTxtList(JSONArray dataArr) {
        String str = "";

//        str = "<p style=\"text-align: justify;\"><span>（</span><span>1</span><span>）</span><span>走进作者</span><span>&nbsp;&nbsp;</span><span>毛泽东，字润之。</span><span>1893</span><span>年</span><span>12</span><span>月</span><span>26</span><span>日生于湖南湘潭韶山冲一个农民家庭。</span><span>1976</span><span>年</span><span>9</span><span>月</span><span>9</span><span>日在北京逝世。中国人民的领袖，马克思主义者，伟大的无产阶级革命家、战略家和理论家，中国共产党、中国人民解放军和中华人民共和国的主要缔造者和领导人，诗人，书法家。</span></p><p style=\"text-align: justify;\"><span>（</span><span>2</span><span>）了解背景</span><span>&nbsp;&nbsp;&nbsp;</span><span>这首词写于</span><span>1925</span><span>年。</span><span>1925</span><span>年是北伐战争开始的前一年。当时中国阶级斗争异常激烈。震惊中外的</span><span>“</span><span>五卅运动</span><span>”</span><span>和省港大罢工已经爆发农民运动也势如破竹，迅猛异常，在 全国十几个省蔓延开来。毛泽东同志当时直接领导了湖南的农村运动先后在韶山等地建了二十多个农民协会，成立雪耻会，并创立了湖南农村第一个党支部</span><span>——</span><span>中共韶山支部。随着革命高潮的到来，各党派对革命领导权进行激烈的争夺。国民党右派势力了想篡夺立功难道权利，而党内，陈独秀又提出了</span><span>“</span><span>一切权力归国民党</span><span>”</span><span>的错误主张。毛泽东等同志同国民党右派势力和党内右倾机会注义进行了针锋相对的斗争。军阀要逮捕毛泽东同志，在韶山人民的掩护下，毛泽东秘密离开了韶山，前往广东创办全国农民运动讲习所。他在途经长沙时重游了橘子洲。面对绚丽的秋景，毛泽东回忆往昔的峥嵘岁月，写下了这首气势磅礴的词。</span></p>";
        if (dataArr != null && dataArr.length() > 0) {
            if (!dataArr.isNull(0)) {
                try {
                    str = dataArr.getString(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSONEx_PRAty", e.toString());
                }

                txtList.add(str);
            }
        }

        return txtList;
    }

    /**
     * 销毁播放
     */
    private void destroyVideo() {
        if (videoFg != null) {
            callbackForVideo.doOnStopVideo();
        }
    }

    @Override
    public void doSth(int i, int pos, String ID) {
        switch (i) {
            case ConstantsUtils.AFTER_CLICK_ALL://点击文件列表单项
//                positionMax = -1;
                resetPlanList();
                resetCoursewareList();
                resetOmicsList();
                resetTestList();
                resetTxtList();
                resetVideoList();
                rightInfoCount = "0";
                currentPageNumForPreview = 1;

                if (fileList != null && fileList.size() > 0) {
                    fileFocus = fileList.get(pos);
                    if (fileFocus != null) {
//                        fileType = fileFocus.getFileType();//文件格式
//                        fileSource = fileFocus.getSource();//文件来源

                        vUtils.showLoadingDialog("");

                        requestFileDetailFromServer(fileFocus.getFileType(), ID);
                    }
                }

                break;
        }
    }

    @Override
    public void getInfo(String str) {

    }


    /**
     * 控件监听
     *
     * @author chenhui
     */
    private class Listeners implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_previous_layout_fg_attend_class_detail:// 上一个
                    isRepaint = true;

                    if (prePosition > 0) {
                        prePosition = prePosition - 1;

                        setGridViewAdapter(prePosition);

                        // 大图
                        // vpager.setCurrentItem(prePosition);

                    } else if (prePosition == 0) {// 当前选中的是第一项
                    }

                    break;
                case R.id.ll_next_layout_fg_attend_class_detail:// 下一个
                    isRepaint = true;

                    if (prePosition < coursewareList.size() - 1) {
                        prePosition = prePosition + 1;

                        setGridViewAdapter(prePosition);

                        // 大图
                        // vpager.setCurrentItem(prePosition);
                    } else if (prePosition == coursewareList.size() - 1) {// 当前选中的是最后一项
                    }

                    break;

                case R.id.ll_paint_layout_v_draw_menu:// 画笔
                    isRepaint = false;
                    drawStyle = ConstantsUtils.GRAFFITI;

                    showSpinWindow(R.layout.layout_v_draw_paint_selector_popup002,
                            500, 100, 0, 0);
                    flFocusPaint.performClick();

                    break;
                case R.id.ll_eraser_layout_v_draw_menu:// 擦除
                    isRepaint = false;
                    drawStyle = ConstantsUtils.ERASER;

                    showSpinWindow(R.layout.layout_v_draw_eraser_selector_popup02,
                            200, 100,
                            llDrawMenu.getWidth() / 7 + llDrawMenu.getWidth() / 14,
                            0);

                    flFocusPaint.performClick();

                    break;
                case R.id.ll_revoke_layout_v_draw_menu:// 撤销
                    isRepaint = true;

                    sfvBoard.undo();

                    dismissWindow();

                    break;
                case R.id.ll_redo_layout_v_draw_menu:// 恢复
                    sfvBoard.redo();

                    dismissWindow();

                    break;
                case R.id.ll_clear_screen_layout_v_draw_menu:// 清屏
                    sfvBoard.resetCanvas();
                    sfvBoard.resetPathList();

                    dismissWindow();

                    break;

                case R.id.ll_white_board_layout_v_draw_menu:// 白板
                    Intent intent = new Intent(getActivity(),
                            ShareFullScreenActivity.class);
                    intent.putExtra(ShareFullScreenActivity.SHOW_STYLE,
                            ConstantsUtils.WHITE_BOARD);
                    startActivity(intent);

                    dismissWindow();

                    break;

                case R.id.ll_full_screen_layout_v_draw_menu:// 全屏
                    Intent intent2 = new Intent(getActivity(),
                            ShareFullScreenActivity.class);
                    intent2.putExtra(ShareFullScreenActivity.SHOW_STYLE,
                            ConstantsUtils.FULL_SCREEN);
                    startActivity(intent2);

                    dismissWindow();

                    break;
                case R.id.fl_small_paint_layout_v_draw_menu:// 细线条
                    isRepaint = false;

                    doSthAfterChangePaintSize(View.VISIBLE, View.INVISIBLE,
                            View.INVISIBLE);

                    flFocusPaint = flSmallPaint;

                    if (ConstantsUtils.GRAFFITI == drawStyle) {
                        setPaintSizeMenu(colorCur);
                    }

                    paintSizeCur = 8;
                    paint(paintSizeCur);

                    break;

                case R.id.fl_middle_paint_layout_v_draw_menu:// 中等线条
                    isRepaint = false;

                    doSthAfterChangePaintSize(View.INVISIBLE, View.VISIBLE,
                            View.INVISIBLE);

                    paintSizeCur = 14;
                    paint(paintSizeCur);

                    if (ConstantsUtils.GRAFFITI == drawStyle) {
                        setPaintSizeMenu(colorCur);
                    }

                    flFocusPaint = flMiddlePaint;

                    break;

                case R.id.fl_large_paint_layout_v_draw_menu:// 粗线条
                    isRepaint = false;

                    doSthAfterChangePaintSize(View.INVISIBLE, View.INVISIBLE,
                            View.VISIBLE);

                    paintSizeCur = 20;
                    paint(paintSizeCur);

                    if (ConstantsUtils.GRAFFITI == drawStyle) {
                        setPaintSizeMenu(colorCur);
                    }

                    flFocusPaint = flLargePaint;

                    break;

                case R.id.tv_switch_material_layout_fg_attend_class_detail:// 切换教材、目录
                    // 跳转至选择教材目录界面
                    Intent intent02 = new Intent(getActivity(),
                            ChoiceTeachingMaterialAty.class);
                    intent02.putExtra(ConstantsUtils.CATALOG_ID, catalogIDCurr);
                    intent02.putExtra(ConstantsUtils.CATALOG_NAME, catalogNameCurr);
                    // intent02.putExtra(ConstantsUtils.PERIOD_ID, periodID);// 学段ID
                    // intent02.putExtra(ConstantsUtils.SUBJECT_ID, subjectID);//
                    // 学科ID
                    // intent02.putExtra(ConstantsUtils.EDITION_ID, editionID);
                    // intent02.putExtra(ConstantsUtils.MODULE_ID, moduleID);
                    intent02.putExtra(ChoiceTeachingMaterialAty.CATALOG_POS, -1);
                    startActivityForResult(intent02, ConstantsUtils.REQUEST_CODE01);

                    break;

                case R.id.tv_file_upload_layout_fg_attend_class_detail://本地上传（授课文件）
                    Toast.makeText(getActivity(), getResources().getText(R.string.no_function), Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    }

    /**
     * 底部页卡偏移事件监听
     */
    private class VPagerChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case 0:// 滑动结束
                    isPageSelected = false;

                    break;
                case 1:// 正在滑动（初次加载是不走此路径的）
                    isPageSelected = true;
                    isScroll = true;

                    break;
                case 2:// 滑动完毕(onPageScrolled()方法前)
                    if (isScroll) {
                        isScroll = false;
                    } else {
                        isPageSelected = false;
                    }

                    break;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            // 滚动时调用
            // int s = position;
            // System.out.print(s);
        }

        @Override
        public void onPageSelected(int arriveIndex) { // 新的条目被选中时调用
            // System.out.println("onPageSelected: " + arriveIndex);

            newPosition = arriveIndex % Integer.valueOf(rightInfoCount);

//            if (isPageSelected && newPosition > positionMax) {// 正向向右滑动的情况
//                if (newPosition == 1) {
//                    currentPageNumForPreview = newPosition + 1;
//                }

//                positionMax = newPosition;

//                if (fileFocus != null && needMultiPageRequestPreview) {
//                    vUtils.showLoadingDialog("");
//
//                    String ID = fileFocus.getDataID();
//                    requestFileDetailFromServer(fileType, ID);
//                }
//            }

            if (ConstantsForServerUtils.PPT.equals(fileFocus.getFileType())) {
                setGridViewAdapter(newPosition);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantsUtils.REQUEST_CODE01) {// 从选择教材界面回传数据
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                return;
            }

            // 目录ID
            catalogIDCurr = bundle.getString(ConstantsUtils.CATALOG_ID);
            // 目录名称
            catalogNameCurr = bundle.getString(ConstantsUtils.CATALOG_NAME);
            if (!ValidateFormatUtils.isEmpty(catalogNameCurr)) {
                tvTitleName.setText(catalogNameCurr);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        dismissWindow();
    }

    @Override
    protected void lazyLoad() {
//        if (!isPrepared || !isVisible || hasLoadOnce) {
//            return;
//        }

        boolean hasChoiced = PreferencesUtils.acquireBooleanInfoFromPreferences(getActivity(), ConstantsUtils.HAS_CHOICED_MATERIAL);
        if (hasChoiced) {

        }
        catalogNameCurr = PreferencesUtils.acquireInfoFromPreferences(getActivity(), ConstantsForPreferencesUtils.CATALOG_NAME_CHOICED);
        if (tvTitleName != null && !TextUtils.isEmpty(catalogNameCurr)) {
            tvTitleName.setText(catalogNameCurr);
        }

        String classID = PreferencesUtils.acquireInfoFromPreferences(getActivity(), ConstantsForPreferencesUtils.CLASS_ID_CHOICED);
        if (!TextUtils.isEmpty(classID)) {
            this.classID = classID;
        }

        requestFileListFromServer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        destroyVideo();
    }
}
