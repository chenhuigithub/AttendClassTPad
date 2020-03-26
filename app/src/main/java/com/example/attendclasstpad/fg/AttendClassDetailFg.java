package com.example.attendclasstpad.fg;

import java.util.ArrayList;
import java.util.List;

import com.example.attendclasstpad.adapter.CustomPagerAdapter04;
import com.example.attendclasstpad.aty.ChoiceTeachingMaterialAty;
import com.example.attendclasstpad.R;
import com.example.attendclasstpad.aty.ShareFullScreenActivity;
import com.example.attendclasstpad.adapter.ColorAdapter;
import com.example.attendclasstpad.adapter.CustomPagerAdapter03;
import com.example.attendclasstpad.adapter.GalleryAdapter;
import com.example.attendclasstpad.adapter.FilesListAdapter;
import com.example.attendclasstpad.callback.InterfacesCallback;
import com.example.attendclasstpad.model.Bean;
import com.example.attendclasstpad.model.Courseware;
import com.example.attendclasstpad.model.DataID01;
import com.example.attendclasstpad.model.DataInfo;
import com.example.attendclasstpad.model.File01;
import com.example.attendclasstpad.util.ConstantsForPreferencesUtils;
import com.example.attendclasstpad.util.ConstantsForServerUtils;
import com.example.attendclasstpad.util.ConstantsUtils;
import com.example.attendclasstpad.util.DrawUtils;
import com.example.attendclasstpad.util.PreferencesUtils;
import com.example.attendclasstpad.util.ServerDataAnalyzeUtils;
import com.example.attendclasstpad.util.ServerRequestUtils;
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
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
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
public class AttendClassDetailFg extends BaseNotPreLoadFg implements InterfacesCallback.ICanDoSth {
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

    private String fileType = ConstantsForServerUtils.WORD;// 授课预览内容的文件格式（教案学案等文字类型、课件ppt图片类型），默认为文字类型

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
    private int currentPageNum = 1;// 授课列表当前页页码，默认是首页
    private int currentPageNumForPreview = 1;// 预览授课当前页页码，默认是首页

    private String chapterID = "";//章节ID

    private boolean isPageSelected = false;// 是否允许走viewPager滑动监听中的onPageSelected方法
    private boolean isScroll = false;// 是否正在滑动，默认未滑动
    private int positionMax = -1;// viewPager滑动过的最大位置
    private int lastPosition;// viewPager上一个位置
    private int newPosition;// viewPager当前位置

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
    private List<DataInfo> planList;// 右侧教案学案列表
    private List<Courseware> coursewareList;// 右侧课件列表

    private Handler uiHandler;// ui主线程
    private ViewUtils vUtils;// 布局工具
    private ServerRequestUtils sUtils;// 服务器请求工具
    private InterfacesCallback.ICanDoSth callback;//回调

    private FilesListAdapter filesAdapter;// 文件目录适配器
    private GalleryAdapter glAdapter;

    private View allFgView;// 总布局
    private RelativeLayout rlFile;// 画廊布局：文件（PPT）缩略图展示布局
    private Gallery glFile;// 画廊效果：文件（PPT）缩略图展示布局
    private CustomSurfaceView sfvBoard;// 画布

    private CustomPagerAdapter04 wordVPagerAdapter;// 学案教案滑动布局适配器
    private CustomPagerAdapter03 picVpagerAdapter;// 课件大图滑动布局适配器
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
    private LinearLayout llPreview;//授课预览面板

    //    PullDown vPullDown;
    private PullDownView vPullDown;//授课列表下拉刷新、上拉加载更多

    public AttendClassDetailFg() {
    }

    public AttendClassDetailFg(String catalogID, String catalogName) {
        this.catalogIDCurr = catalogID;
        this.catalogNameCurr = catalogName;
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

            llTitle = (LinearLayout) allFgView
                    .findViewById(R.id.ll_title_layout_fg_attend_class_detail);

            tvTitleName = (TextView) allFgView
                    .findViewById(R.id.tv_title_name_layout_fg_attend_class_detail);

            llPreview = (LinearLayout) allFgView
                    .findViewById(R.id.ll_preview_layout_fg_attend_class_detail);

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

//            setPrlstvFilesListeners();

            initPagerViews();
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

        setVPagerAdapter(0);
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
                currentPageNum = 1;
                // 请求数据
                requestFileListFromServer();
            }

            @Override
            public void onMore() {
                currentPageNum = currentPageNum + 1;
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
            chapterID = PreferencesUtils.acquireInfoFromPreferences(getActivity(), ConstantsForPreferencesUtils.CHAPTER_ID_CHOICED);
        }
        dataID01.setChid(chapterID);//章节ID

        dataID01.setCid(classID);//班级ID

        Bean bean = new Bean();
        bean.setData(dataID01);
        bean.setIndex(String.valueOf(currentPageNum));
        bean.setSize(ConstantsForServerUtils.PAGE_SIZE_DEFAULT_VALUE);

        Gson gson = new Gson();
        String json = gson.toJson(bean);

        sUtils.request("getLectureList", json, "", ServerRequestUtils.REQUEST_LONG_TIME, new ServerRequestUtils.OnServerRequestListener2() {
            @Override
            public void onFailure(final String msg) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPageNum == 1) {// 首页数据
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
                if (currentPageNum == 1 && fileList.size() > 0) {
                    fileList.clear();
                }

                List<File01> list = com.alibaba.fastjson.JSON.parseArray(data.toString(), File01.class);

//                fileType = ServerDataAnalyzeUtils.getValue(dataObj, "DataType");// 类型（word/video/...）

                //假数据
                //List<File01> list = getFileData();

                if (list != null) {
                    if (list.size() == 0) {
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (currentPageNum == 1) {// 首页数据
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

                                if (currentPageNum == 1) {
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
                }
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        vUtils.dismissDialog();
                    }
                });

                hasLoadOnce = true;
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


        if (rightPagerType.equals(ConstantsForServerUtils.IMAGES)) {// 图片类
            setPicVPagerAdapter(position);
        } else {// 文字格式（学案、教案）等
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
                setGridViewAdapter(position);

                setVPagerAdapter(position);

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
        if (glAdapter == null) {
            // 实例化ImageAdapter适配器
            glAdapter = new GalleryAdapter(getActivity(), coursewareList);
            // 将适配器的数据存储到Gallery组件中（设置Gallery组件的Adapter对象）
            glFile.setAdapter(glAdapter);
            glFile.setUnselectedAlpha(1f);
        } else {
            glAdapter.notifyDataSetChanged();
        }

        // if (pos == 0) {
        // glFile.setSelection(pos + 2);
        // }
        // if (fileContents.size() > 2 && pos < 4) {
        // // 设置第二个数据在中央位置
        // glFile.setSelection(2);
        // } else {
        // glFile.setSelection(pos);
        // }

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

    private void previewPPT() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                rlFile.setVisibility(View.VISIBLE);
            }
        });
    }

    private void previewNotPPT() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                rlFile.setVisibility(View.GONE);
            }
        });
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

        if (ConstantsForServerUtils.WORD.equals(fileType) || ConstantsForServerUtils.HTML.equals(fileType) || ConstantsForServerUtils.PPT.equals(fileType)) {// 文字格式（学案、教案）、组学案、组试题、 课件ppt格式
            requestFileDetailJsonArrFromServer(jsonStr, ID, String.valueOf(currentPageNumForPreview));
        } else if (ConstantsForServerUtils.TXT.equals(fileType) || ConstantsForServerUtils.IMAGES.equals(fileType) || ConstantsForServerUtils.VIDEO.equals(fileType)
                || ConstantsForServerUtils.AUDIO.equals(fileType)) {//图片
            requestFileDetailJsonObjFromServer(jsonStr, ID, String.valueOf(currentPageNumForPreview));
        } else if (ConstantsForServerUtils.SWF.equals(fileType)) {// flash动画类,只有swf格式
        } else {//其它未知格式
            requestFileDetailJsonArrFromServer(jsonStr, ID, String.valueOf(currentPageNumForPreview));
        }

    }

    private void requestFileDetailJsonArrFromServer(String jsonStr, String id, final String indexCurr) {
        sUtils.request("getLectureView", jsonStr, "", ServerRequestUtils.REQUEST_SHORT_TIME, new ServerRequestUtils.OnServerRequestListener2() {
            @Override
            public void onFailure(String msg) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        vUtils.dismissDialog();
                    }
                });
            }

            @Override
            public void onResponse(String msg, JSONArray data, String count) {
                rightInfoCount = count;
                currentPageNumForPreview = currentPageNumForPreview + 1;

                showRightInfo(data, indexCurr);

                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        vUtils.dismissDialog();
                    }
                });
            }
        });
    }

    private void requestFileDetailJsonObjFromServer(String jsonStr, String id, String indexCurr) {
        sUtils.request("getLectureView", jsonStr, "", ServerRequestUtils.REQUEST_SHORT_TIME, new ServerRequestUtils.OnServerRequestListener() {
            @Override
            public void onFailure(String msg) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        vUtils.dismissDialog();
                    }
                });
            }

            @Override
            public void onResponse(String msg, JSONObject data, String count) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        vUtils.dismissDialog();
                    }
                });
            }
        });
    }

    private void showRightInfo(JSONArray dataArr, final String indexCurr) {
        if (dataArr != null) {
            if (dataArr.length() == 0) {

                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //                ivNoData.setVisibility(View.VISIBLE);
//                tvNum.setVisibility(View.GONE);

                        Toast.makeText(getActivity(), "暂无右侧内容，切换栏目试试？",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                // 重置数据
                resetPlanList();
                resetCoursewareList();
            } else {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //                ivNoData.setVisibility(View.GONE);
//                tvNum.setVisibility(View.VISIBLE);
                    }
                });


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

                    if (ConstantsForServerUtils.WORD.equals(fileType)) {// 文字格式（学案、教案）
                        DataInfo info = com.alibaba.fastjson.JSON.parseObject(
                                jsonObj.toString(), DataInfo.class);

                        if (planList.size() == 0) {
                            planList.add(info);

                            // 造一个假数据放到尾端，便于viewPager滑动监听
                            planList.add(new DataInfo());
                        } else if (planList.size() > 1) {
                            planList.remove(planList.size() - 1);// 先去掉尾端的假数据
                            planList.add(planList.size(), info);// 在尾端加上新数据

                            if (String.valueOf(planList.size()).equals(rightInfoCount)) {

                            } else {
                                // 造一个假数据放到尾端，便于viewPager滑动监听
                                planList.add(new DataInfo());
                            }
                        }


                        // 造一个假数据放到尾端，便于viewPager滑动监听
//                        planList.add(new DataInfo());

                        previewNotPPT();

                    } else if (ConstantsForServerUtils.PPT.equals(fileType)) {// 课件ppt格式
                        String path = ServerDataAnalyzeUtils.getValue(jsonObj,
                                "DataPath");

                        // 测试图片 2018.12.17
                        // path="/upload/Sync/thumbnail/9ea54a7e-cb24-4987-9dad-7e5f141a1eb7/Slide1_768.jpg";

                        Courseware info1 = new Courseware();
                        info1.setBigPath(path);

                        if (coursewareList.size() == 0) {
                            coursewareList.add(info1);

                        } else if (coursewareList.size() > 1) {
                            coursewareList.remove(coursewareList.size() - 1);// 先去掉尾端的假数据
                            coursewareList.add(coursewareList.size(), info1);
                        }

                        // 造一个假数据放到尾端，便于viewPager滑动监听
                        coursewareList.add(new Courseware());

                        previewPPT();
                        setGridViewAdapter(newPosition);
                    }
                }

//                if (currentPageNumForPreview > lastPosition) {// 表明当前正向向右滑动
//                    positionMax = currentPageNumForPreview;
//                }

//                lastPosition = currentPageNumForPreview;

                hasLoadOnce = true;
            }
            if (newPosition == 0
                    && lastPosition == Integer.valueOf(fileList.size()) - 1) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 刷新右侧布局
                        setVPagerAdapter(0);
                    }
                });

            } else {
                final int count = Integer.valueOf(indexCurr) - 1;

                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 刷新右侧布局
                        setVPagerAdapter(count);
                    }
                });
            }
        } else {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    //            ivNoData.setVisibility(View.VISIBLE);
//            tvNum.setVisibility(View.GONE);

                    // 刷新右侧布局
                    setVPagerAdapter(0);

                    Toast.makeText(getActivity(), "暂无右侧内容，切换栏目试试？",
                            Toast.LENGTH_SHORT).show();
                }
            });

            // 重置数据
            resetPlanList();
            resetCoursewareList();
        }

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                // 显示右侧资源个数
//        tvNum.setText(indexCurr + " / " + rightInfoCount);
            }
        });
    }


    /**
     * 处理授课文件详情
     *
     * @param dataObj   数据
     * @param jsonKey   json中的key
     * @param indexCurr 当前页页码
     */
    private void dealWithFileDetail(JSONObject dataObj, String jsonKey,
                                    String indexCurr) {
        isPageSelected = false;

        newPosition = 0;
        lastPosition = 0;
        positionMax = -1;

        fileType = ServerDataAnalyzeUtils.getValue(dataObj, "DataType");
        final JSONArray dataArr = ServerDataAnalyzeUtils.getDataAsJSONArray(
                dataObj, jsonKey);
        // 重置数据
        resetPlanList();
        resetCoursewareList();

        showRightInfo(dataArr, indexCurr);
    }

    /**
     * 重置右侧教案学案等文字类数据
     */
    private void resetPlanList() {
        // 右侧课件
        if (planList.size() > 0) {
            planList.clear();
        }
    }

    /**
     * 重置右侧课件ppt等图片类数据
     */
    private void resetCoursewareList() {
        // 右侧课件
        if (coursewareList.size() > 0) {
            coursewareList.clear();
        }
    }


    /**
     * 重置右侧具体数据
     */
    private void resetRightInfoList() {
        // 右侧课件
        if (fileList.size() > 0) {
            fileList.clear();
        }
    }

    @Override
    public void doSth(int i, int pos, String ID) {
        switch (i) {
            case ConstantsUtils.AFTER_CLICK_ALL://点击文件列表单项
                positionMax = -1;
                resetPlanList();

                if (fileList != null && fileList.size() > 0) {
                    fileFocus = fileList.get(pos);
                    if (fileFocus != null) {
                        fileType = fileFocus.getFormat();//文件格式
                        currentPageNumForPreview = 1;
                        requestFileDetailFromServer(fileType, ID);
                    }
                }

                break;
        }
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

//            // 特殊情况： 此时是从最后一页数据向右重新滑动到第一页
//            if (newPosition == 0
//                    && lastPosition == Integer.valueOf(rightInfoCount) - 1) {// 特殊情况：
//                // 此时是从最后一页数据向右重新滑动到第一页
//                if (ConstantsForServerUtils.WORD.equals(fileType)) {// 文字类：教案、学案
//
//                    DataInfo info = planList.get(0);
//                    resetPlanList();
//                    planList.add(info);
//                    planList.add(new DataInfo());
//
//                } else if (ConstantsForServerUtils.PPT.equals(fileType)) {// 课件
//                    resetCoursewareList();
//                }
//
//                // 在数据变动了之后，先调用一下notifyDataSetChanged()，否则接下来进行网络请求，时间延迟了程序也许会崩溃
//                setVPagerAdapter(0);
//
//                positionMax = -1;
//
//            } else
//            {

//                if (newPosition > lastPosition) {
//                    positionMax = currentPageNumForPreview;
//                }

            if (isPageSelected && newPosition > positionMax) {// 表明当前正向向右滑动
                vUtils.showLoadingDialog("");
                if (newPosition == 1) {
                    currentPageNumForPreview = newPosition + 1;
                }

                positionMax = newPosition;

                if (fileFocus != null) {
                    String ID = fileFocus.getDataID();
                    requestFileDetailFromServer(fileType, ID);
                }


            }
//            }

            // 显示当前页页码与资源总页数
//            tvNum.setText(newPosition + 1 + " / " + rightInfoCount);

            lastPosition = newPosition;

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
        catalogNameCurr = PreferencesUtils.acquireInfoFromPreferences(getActivity(), ConstantsForPreferencesUtils.CHAPTER_NAME_CHOICED);
        if (tvTitleName != null && !TextUtils.isEmpty(catalogNameCurr)) {
            tvTitleName.setText(catalogNameCurr);
        }

        String classID = PreferencesUtils.acquireInfoFromPreferences(getActivity(), ConstantsForPreferencesUtils.CLASS_ID_CHOICED);
        if (!TextUtils.isEmpty(classID)) {
            this.classID = classID;
        }

        requestFileListFromServer();
    }
}
