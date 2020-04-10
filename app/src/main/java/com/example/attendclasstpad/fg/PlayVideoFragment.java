package com.example.attendclasstpad.fg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.adapter.VideoCatalogAdapter;
import com.example.attendclasstpad.application.CustomApplication;
import com.example.attendclasstpad.callback.InterfacesCallback;
import com.example.attendclasstpad.callback.OnListenerForPlayVideoCallback;
import com.example.attendclasstpad.callback.OnListenerForPlayVideoSendOutInfo;
import com.example.attendclasstpad.model.VideoAndAudioInfoModel;
import com.example.attendclasstpad.model.VideoAudio;
import com.example.attendclasstpad.util.ConstantsUtils;
import com.example.attendclasstpad.util.DensityUtil;
import com.example.attendclasstpad.util.FormatUtils;
import com.example.attendclasstpad.util.NetworkUtils;
import com.example.attendclasstpad.util.ValidateFormatUtils;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 视频
 */
@SuppressLint("ValidFragment")
@SuppressWarnings("deprecation")
public class PlayVideoFragment extends Fragment implements
        OnListenerForPlayVideoCallback, Callback {
    /**
     * 资源的网络地址（视频测试地址1_2018.12.20）
     */
    private String movieUri = "http://www.mibeike.com/upload/extend/video/201807/06/201807061457305988.mp4";

    public final static int UPDATE_PROGRESS = 0x11;

    public boolean notFree = true;// 视频不免费(默认免费)
    private int seekProgress = 0;
    private int seekTotalProgress = 0;
    private boolean pauseOrPlay = false; // 是否为播放状态，默认为false
    private String playType;// 播放类型（0:视频，1:音频）
    public Timer myTimer;
    private InterfacesCallback.ICanKnowSth2 callbackForMain;// 回调（主程序用）
    private OnListenerForPlayVideoSendOutInfo callback;// 回调
    private String playName;// 当前播放的剧集名称
    private List<VideoAudio> infoList;// 所有课程列表信息
    private VideoAudio info;// 正在观看的视频信息
    private String name;// 视频/音频名称
    private int startX = 0;
    private int endX = 0;
    private int startY = 0;
    private int endY = 0;
    private int pro = 0; // 快进的进度
    private AudioManager mAudioManager;
    private DisplayMetrics dm;
    private boolean speeding = false;
    private boolean soundChangeing = false;
    public CustomApplication myApplication;
    // 音量控制
    private float mStartY;
    private int startSound;
    private float currentY;
    private int vedioWidth, vedioHeight;
    private boolean isFullScreen = false;// 是否设置为全屏显示，默认为半屏显示
    private int netType;// 网络状态(无网络：0，wifi：1，数据流量：2)
    private int halfOrFullType = 1;// 半屏或全屏状态（半屏:1;全屏:2），默认为半屏

    // 视频
    private SurfaceView sf_vedio;
    private SurfaceHolder holder;
    private MediaPlayer player;
    private SeekBar seek_control;
    private TextView tv_time_current;
    private TextView tv_time_total;
    private LinearLayout ll_playOrPause;
    private Button btn_playOrPause;
    private Button play_btn;
    private ProgressBar pbarLoading;// 正在加载的旋转标志
    private RelativeLayout rl_under;
    private TextView tvType;// 类型显示（视频/音频）
    private LinearLayout ll_back;
    private LinearLayout ll_qp;
    private Button btn_qp;
    private RelativeLayout rl_vedio;
    private RelativeLayout rl_top;
    // private RelativeLayout rl_root;

    private RelativeLayout gesture_progress_layout;// 进度图标布局
    private TextView geture_tv_progress_time;// 播放时间进度
    private ImageView gesture_iv_progress;// 快进或快退标志
    private TextView tv_dyName;
    private LinearLayout ll_moreMovie;
    private LinearLayout llSelectLesson;
    private LinearLayout ll_rightMore;
    private ListView lstvVideo;// 视频、音频列表的容器
    private RelativeLayout rlAudio;// 音频播放背景
    // 音量控制
    private RelativeLayout rl_movie_sound;
    private TextView tv_movie_sound;
    // private ImageView iv_movie_sound;
    // private TextView geture_tv_volume_percentage;// 音量百分比
    // private ImageView gesture_iv_player_volume;// 音量图标
    // 亮度控制
    // private View view_movie_alpha_cover;
    // private AssetFileDescriptor fileDescriptor;
    // private ImageView imgvCollect;// 收藏按钮

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (player != null) {
                switch (msg.what) {
                    case UPDATE_PROGRESS: // 更新进度
                        seek_control.setProgress(player.getCurrentPosition());
                        tv_time_current.setText(FormatUtils
                                .converLongTimeToStr(player.getCurrentPosition()));
                        break;
                }
            } else {
                // 避免退出此界面后视频音频仍播放的问题，liduohong，2016.12.15
                mHandler.removeCallbacksAndMessages(UPDATE_PROGRESS);

            }
        }
    };

    /**
     * 返回视频播放的非主线程handler
     *
     * @return 线程handler
     */
    public Handler getHandler() {
        if (mHandler != null) {
            return mHandler;
        }

        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onAttach(Activity activity) {
        try {
            callbackForMain = (InterfacesCallback.ICanKnowSth2) activity;
            callbackForMain.getInfo(ConstantsUtils.ER);

            // 这里activity只能指MainActivity
            callback = (OnListenerForPlayVideoSendOutInfo) activity;
        } catch (Exception e) {
            // 1.打印异常;
            e.printStackTrace();
            // 2.若activity中未实现此接口，抛出 ClassCastException 异常
            throw new ClassCastException(activity.toString()
                    + " must implement OnListenerForPlayVideoSendOutInfo");
        }

        super.onAttach(activity);
    }

    public PlayVideoFragment() {

    }

    /**
     * 构造方法
     *
     * @param url          路径
     * @param playName     名称
     * @param infoList     课程列表
     * @param isFullScreen 是否全屏
     * @param playType     播放类型（即文件类型）
     */
    public PlayVideoFragment(String url, String playName,
                             List<VideoAudio> infoList, boolean isFullScreen, String playType) {
        if (!ValidateFormatUtils.isEmpty(url)) {
            // 赋值资源简要路径
            this.movieUri = url;
        }

        this.playName = playName;
        this.infoList = infoList;
        this.isFullScreen = isFullScreen;
        this.playType = playType;// 播放类型
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏
        // 应用运行时，保持屏幕高亮，不锁屏
        getActivity().getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        info = new VideoAudio();

        View view = inflater.inflate(R.layout.aty_showmovie, null);
        rlAudio = (RelativeLayout) view.findViewById(R.id.rl_wrapper_audio);// 音频背景


        // AssetManager assetManager = getActivity().getAssets();
        // try {
        // fileDescriptor = assetManager.openFd("02.mp4");
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        myApplication = (CustomApplication) getActivity().getApplication();
        initVideo(view);
        initDataVideo();
        initListener();

        if (!pauseOrPlay) {
            sf_vedio.setBackgroundResource(R.color.black);
        }

        // 播放视频或音频
        loadVideo(movieUri, playType);

        return view;
    }

    private void initVideo(View view) {
        // 禁止休眠
        getActivity().getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        // showmovie = (LinearLayout) findViewById(R.id.vertical_screen);
        sf_vedio = (SurfaceView) view.findViewById(R.id.sf_vedio);
        seek_control = (SeekBar) view.findViewById(R.id.seek_control);
        tv_time_current = (TextView) view.findViewById(R.id.tv_time_current);
        tv_time_total = (TextView) view.findViewById(R.id.tv_time_total);
        ll_playOrPause = (LinearLayout) view
                .findViewById(R.id.ll_play_download_laryout_aty_showmovie);
        btn_playOrPause = (Button) view.findViewById(R.id.btn_playOrPause);
        play_btn = (Button) view
                .findViewById(R.id.btn_play_layout_aty_showmovie);
        pbarLoading = (ProgressBar) view
                .findViewById(R.id.pbar_loading_layout_aty_showmovie);// 正在加载的旋转标志

        rl_under = (RelativeLayout) view.findViewById(R.id.rl_under);
        // 界面一开始隐藏底部菜单
        rl_under.setVisibility(View.GONE);
        // 类型显示
        tvType = (TextView) view.findViewById(R.id.tv_type_layout_aty_shwmovie);

        rl_top = (RelativeLayout) view.findViewById(R.id.rl_top);
        ll_back = (LinearLayout) view
                .findViewById(R.id.ll_back_layout_aty_showmovie);
        /*
         * sf_vedio.setFocusable(true); sf_vedio.setFocusableInTouchMode(true);
         * sf_vedio.requestFocus();
         */
        ll_qp = (LinearLayout) view.findViewById(R.id.ll_qp);
        btn_qp = (Button) view.findViewById(R.id.btn_qp);
        rl_vedio = (RelativeLayout) view.findViewById(R.id.rl_vedio);
        // rl_root = (RelativeLayout) view.findViewById(R.id.rl_root);

        sf_vedio.getHolder().setKeepScreenOn(true); // 保持屏幕高亮
        holder = sf_vedio.getHolder();
        gesture_progress_layout = (RelativeLayout) view
                .findViewById(R.id.gesture_progress_layout);
        geture_tv_progress_time = (TextView) view
                .findViewById(R.id.geture_tv_progress_time);
        // geture_tv_volume_percentage = (TextView)
        // view.findViewById(R.id.geture_tv_volume_percentage);
        gesture_iv_progress = (ImageView) view
                .findViewById(R.id.gesture_iv_progress);
        // gesture_iv_player_volume = (ImageView)
        // view.findViewById(R.id.gesture_iv_player_volume);

        // 视频名字
        tv_dyName = (TextView) view
                .findViewById(R.id.tv_name_layout_aty_showmovie);
        if (this.playName != null) {
            tv_dyName.setText(playName);
        }

        // 右侧弹出列表布局
        ll_moreMovie = (LinearLayout) view.findViewById(R.id.ll_moreMovie);
        ll_moreMovie.setVisibility(View.GONE);

        // 选课
        llSelectLesson = (LinearLayout) view
                .findViewById(R.id.ll_wrapper_select_lesson_layout_aty_showmovie);
        ll_rightMore = (LinearLayout) view.findViewById(R.id.ll_rightMore);
        lstvVideo = (ListView) view
                .findViewById(R.id.lstv_video_list_aty_showmovie);

        // 音量控制
        rl_movie_sound = (RelativeLayout) view
                .findViewById(R.id.rl_movie_sound);
        // iv_movie_sound = (ImageView) view.findViewById(R.id.iv_movie_sound);
        tv_movie_sound = (TextView) view.findViewById(R.id.tv_movie_sound);
    }

    private void initDataVideo() {
        if (isFullScreen) {// 需要展示全屏状态
            // switchFullOrHalfScreen();

            btn_qp.setBackgroundResource(R.drawable.tcqp);
        } else {// 需要展示小屏状态
            btn_qp.setBackgroundResource(R.drawable.qp);
            ll_back.setVisibility(View.GONE);
        }

        // 调节系统音量
        mAudioManager = (AudioManager) getActivity().getSystemService(
                Service.AUDIO_SERVICE);
        // 获取系统最大音量
        float maxVolume = getMaxVolume();
        // 获取当前系统音量
        float currentVolume = getCurrentVolume();
        tv_movie_sound.setText((int) (currentVolume / maxVolume * 100) + "%");
        dm = new DisplayMetrics();
    }

    /**
     * 弹出加载框
     */
    private void showLoadingDialog() {
        // CustomDialog.Builder builder = new
        // CustomDialog.Builder(getActivity());
        // builder.setMessage(R.string.tip_for_loading);
        // dialog = builder.createForLoading();
        // Window dialogWindow = dialog.getWindow();
        // WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        // lp.x = 0; // 新位置X坐标
        // lp.y = 400; // 新位置Y坐标
        // lp.width = 250; // 宽度
        // lp.height = 250; // 高度
        // lp.alpha = 0.75f; // 透明度
        // dialogWindow.setGravity(Gravity.TOP | Gravity.CENTER_VERTICAL);
        // dialogWindow.setAttributes(lp);
        // dialog.show();
        pbarLoading.setVisibility(View.VISIBLE);
    }

    /**
     * 半屏或全屏播放
     */
    @SuppressLint("SourceLockedOrientationActivity")
    private void switchFullOrHalfScreen() {
        RelativeLayout.LayoutParams params;
        if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) { // 当前为横屏状态，切换至竖屏的操作
            getActivity().setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    DensityUtil.dip2px(getActivity(), 240));
            btn_qp.setBackgroundResource(R.drawable.qp);
            // 显示标题栏
            // rl_root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            // rl_root.setFitsSystemWindows(true);
            // rl_root.setClipToPadding(true);
            llSelectLesson.setVisibility(View.GONE);
            tv_dyName.setVisibility(View.GONE);
            // 突出全屏
            // getActivity().getWindow().setFlags(0,
            // WindowManager.LayoutParams.FLAG_FULLSCREEN);

            // 设置半屏状态标志
            isFullScreen = false;

            callback.doSwitchHalfScreen();
        } else { // 当前为竖屏状态，切换至横屏的操作
            getActivity().setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            btn_qp.setBackgroundResource(R.drawable.tcqp);

            // 设置全屏
            getActivity().getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            // rl_root.setFitsSystemWindows(false);
            // rl_root.setClipToPadding(false);
            llSelectLesson.setVisibility(View.VISIBLE);
            tv_dyName.setVisibility(View.VISIBLE);

            // 设置全屏状态标志
            isFullScreen = true;
            callback.doSwitchFullScreen(infoList, info);
        }
        rl_vedio.setLayoutParams(params);
        changeVedioSize();
    }

    /**
     * 切换至全屏
     */
    private void switchFullScreen() {
        callback.doSwitchFullScreen(infoList, info);
    }

    /**
     * 切换至半屏
     */
    private void switchHalfScreen() {
        callback.doSwitchHalfScreen();
    }

    /**
     * 适配视频尺寸
     */
    private void changeVedioSize() {
        if (vedioWidth == 0 || vedioHeight == 0) {
            return;
        }

        int autoWidth = 0;
        int autoheight = 0;
        float scale = vedioWidth / (vedioHeight * 1.0f); // 宽高比

        if (null == getActivity()) {
            return;
        }

        if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) { // 切换至横屏
            // 如果是高大于宽
            if (vedioHeight >= vedioWidth) {
                autoheight = myApplication.Screen_Width;
                autoWidth = (int) (autoheight * scale);
            } else {
                // autoWidth = myApplication.Screen_Height;
                // autoheight = (int) (autoWidth / scale);
                autoWidth = myApplication.Screen_Height;
                autoheight = myApplication.Screen_Width;
            }
        } else { // 切换至竖屏
            if (vedioHeight >= vedioWidth) {
                autoheight = DensityUtil.dip2px(myApplication, 240);
                autoWidth = (int) (autoheight * scale);
            } else {
                // autoWidth = myApplication.Screen_Width;
                // autoheight = (int) (autoWidth / scale);
                autoWidth = myApplication.Screen_Width;
                autoheight = DensityUtil.dip2px(myApplication, 240);
            }
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                autoWidth, autoheight);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        sf_vedio.setLayoutParams(params);
    }

    private void initListener() {
        ll_playOrPause.setOnClickListener(new Listeners());
        play_btn.setOnClickListener(new Listeners());
        ll_back.setOnClickListener(new Listeners());
        ll_qp.setOnClickListener(new Listeners());

        llSelectLesson.setOnClickListener(new Listeners());
        ll_rightMore.setOnClickListener(new Listeners());

        holder.setFormat(PixelFormat.RGBA_8888);
        // 加载视频
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
                // holder.setFixedSize(width, height);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                resetVideo();
            }
        });

        // 拖动进度
        seek_control
                .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        if (player != null) {
                            player.seekTo(seekBar.getProgress());
                        }
                    }
                });

        // 快进和音量大小
        rl_vedio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        getActivity().getWindowManager().getDefaultDisplay()
                                .getMetrics(dm);
                        // 此为横屏
                        if (dm.heightPixels < dm.widthPixels && player != null
                                && player.isPlaying()) {
                            mStartY = event.getY();
                            // 手指按下时的音量
                            startSound = getCurrentVolume();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        endX = (int) event.getRawX();
                        endY = (int) event.getRawY();

                        // 此为横屏
                        if (dm.heightPixels < dm.widthPixels && player != null
                                && player.isPlaying()) {
                            int distanceX = Math.abs(endX - startX);
                            int distanceY = Math.abs(endY - startY);

                            // 快进
                            if ((distanceX - distanceY > 30 || speeding)
                                    && !soundChangeing) {
                                speeding = true;
                                soundChangeing = false;

                                // 加int
                                pro = (player.getCurrentPosition() + (endX - startX)
                                        * (seekTotalProgress / dm.widthPixels));
                                gesture_progress_layout.setVisibility(View.VISIBLE);
                                if (endX - startX > 20) {
                                    gesture_iv_progress
                                            .setImageResource(R.drawable.souhu_player_forward);
                                } else if (endX - startX < -20) {
                                    gesture_iv_progress
                                            .setImageResource(R.drawable.souhu_player_backward);
                                }

                                if (pro == 0) {
                                    pro = 0;
                                }
                                geture_tv_progress_time.setText(FormatUtils
                                        .converLongTimeToStr(pro)
                                        + "/"
                                        + FormatUtils
                                        .converLongTimeToStr(seekTotalProgress));
                                player.seekTo(pro);
                                startX = endX;
                            } else if (distanceY - distanceX > 20) { // 音量调节
                                soundChangeing = true;
                                // if (startY - endY > 30) {
                                // changeVolumeSize(1);
                                // } else if (startY - endY < -30) {
                                // changeVolumeSize(-1);
                                // }
                                // startY = endY;
                                currentY = event.getY();
                                // 手指移动的距离
                                float moveY = mStartY - currentY;
                                // Log.e("手指移动的距离",moveY+"");
                                // 滑动的百分比
                                float halfScreenH = getActivity()
                                        .getWindowManager().getDefaultDisplay()
                                        .getHeight();
                                float movePercent = moveY / halfScreenH;

                                // 修改的音量
                                int changeVolume = (int) (movePercent * getMaxVolume());
                                // 设置的音量
                                float finalVolume = changeVolume + startSound;
                                updateVolume(finalVolume);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!soundChangeing && !soundChangeing) { // 点击事件
                            if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) { // 如果是横屏
                                // 点击弹出功能栏
                                if (rl_under.getVisibility() == View.VISIBLE) {
                                    rl_under.setVisibility(View.GONE);
                                    rl_top.setVisibility(View.GONE);
                                    ll_moreMovie.setVisibility(View.GONE);

                                    llSelectLesson.setVisibility(View.VISIBLE);
                                    // llCollect.setVisibility(View.VISIBLE);
                                    // ll_share.setVisibility(View.VISIBLE);
                                } else {
                                    rl_under.setVisibility(View.VISIBLE);
                                    rl_top.setVisibility(View.VISIBLE);
                                }
                            } else { // 竖屏
                                // 点击弹出功能栏
                                if (rl_top.getVisibility() == View.VISIBLE) {
                                    rl_under.setVisibility(View.GONE);
                                    rl_top.setVisibility(View.GONE);
                                } else {
                                    rl_under.setVisibility(View.VISIBLE);
                                    rl_top.setVisibility(View.VISIBLE);
                                    // llCollect.setVisibility(View.VISIBLE);
                                    // ll_share.setVisibility(View.VISIBLE);

                                    llSelectLesson.setVisibility(View.GONE);
                                }
                            }
                        }
                        gesture_progress_layout.setVisibility(View.GONE);
                        rl_movie_sound.setVisibility(View.GONE);
                        soundChangeing = false;
                        speeding = false;
                        break;
                    default:
                        break;
                }
                return true;
            }

            // private void changeVolumeSize(int volumeSize) {
            // int volume = mAudioManager
            // .getStreamVolume(AudioManager.STREAM_MUSIC)
            // + volumeSize;
            // if (volume > 14) {
            // volume = 14;
            // } else if (volume < 0) {
            // volume = 0;
            // }
            //
            // // 调节音量 7代表音量以7递增，递减
            // mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
            // volume, 7);
            // // if(volume == 14){
            // //
            // // progress_text.setText("当前系统音量为："+ "100%");
            // // }else{
            // //
            // // progress_text.setText("当前系统音量为："+ volume * 7 + "%");
            // // }
            // // progress_text.setVisibility(View.GONE);
            // }
        });

        // 视频目录（右侧弹出框）
        lstvVideo.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos,
                                    long id) {
                info = infoList.get(pos);
                VideoAndAudioInfoModel info1 = new VideoAndAudioInfoModel();
                info1.setId(info.getId());
                info1.setName(info.getTitle());
                info1.setUri(info.getPath());

                // 收起右侧侧边栏
                ll_moreMovie.setVisibility(View.GONE);

                // 通知主界面视频内容已切换
                callback.ICanGetVideoInfoCurrentPlay(info1);
            }
        });
    }

    /**
     * 重新播放视频
     */
    private void restartVideo() {
        sf_vedio.setBackgroundResource(R.color.transparent);

        // Log.e("进度3", seekProgress + "");
        play_btn.setClickable(true);
        ll_playOrPause.setClickable(true);
        // 网络状态
        boolean isNetUsable = NetworkUtils.isNetSystemUsable(getActivity());
        if (isNetUsable) {
            netType = 1;
        } else {
            netType = 0;
        }
        if (netType == 0) {
            Toast.makeText(getActivity(), "当前没有可用的网络", Toast.LENGTH_SHORT)
                    .show();

            play_btn.setVisibility(View.VISIBLE);
        } else if (netType == 1) {
            if (player != null) {
                // Log.e("进度4", seekProgress + "");
                player.seekTo(seekProgress); // 进度
                player.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mp) {
                        pbarLoading.setVisibility(View.GONE);
                        player.start();
                    }
                });
            }
            btn_playOrPause.setBackgroundResource(R.drawable.pausebtn);
            play_btn.setVisibility(View.GONE);
        } else if (netType == 2) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("提示")
                    .setMessage("您现在使用的是运营商网络，继续观看可能产生超额流量费用。土豪请随意")
                    .setPositiveButton(
                            "取消观看",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    play_btn.setVisibility(View.VISIBLE);
                                }
                            })
                    .setNegativeButton(
                            "继续观看",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // 播放
                                    if (player != null) {
                                        player.seekTo(seekProgress); // 进度
                                        player.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                                            @Override
                                            public void onSeekComplete(
                                                    MediaPlayer mp) {
                                                player.start();
                                            }
                                        });
                                    }
                                    btn_playOrPause
                                            .setBackgroundResource(R.drawable.pausebtn);
                                    play_btn.setVisibility(View.GONE);
                                }
                            }).show();
        }
    }

    /**
     * 加载视频资源
     *
     * @param movieUri 视频播放的地址
     * @param playType 播放类型（0:视频，1:音频）
     */
    private void loadVideo(String movieUri, String playType) {
        if (ValidateFormatUtils.isEmpty(movieUri)) {
            Toast.makeText(myApplication, "播放的网址不存在", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if ("1".equals(playType)) {// 播放音频
            sf_vedio.setVisibility(View.GONE);
            rlAudio.setVisibility(View.VISIBLE);
        } else {// 视频类型或其它类型都按照视频播放
            sf_vedio.setVisibility(View.VISIBLE);
            rlAudio.setVisibility(View.GONE);
        }

        if (ValidateFormatUtils.isEmpty(movieUri)) {
            Toast.makeText(myApplication, "播放的网址不存在", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        try {
            if (player == null) {
                player = new MediaPlayer();
            }
            player.reset();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC); // 设置音频
            player.setDataSource(myApplication, Uri.parse(movieUri)); // 加载视频
            player.prepareAsync(); // 准备

            // 播放
            playVideo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新STREAM_MUSIC的音量为volume,并且更新音量控制条
     */
    private void updateVolume(float volume) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) volume,
                0);
        int finalVolume = (int) (volume / getMaxVolume() * 100);
        rl_movie_sound.setVisibility(View.VISIBLE);
        if (finalVolume >= 0 && finalVolume <= 100) {
            tv_movie_sound.setText(finalVolume + "%");
            // Log.e("设置音量", String.valueOf(finalVolume));
        }
    }

    /**
     * 释放资源
     */
    private void resetVideo() {
        if (player != null) { // 加int
            seekProgress = player.getCurrentPosition();
            player.stop();
            player.release();
            player = null;

            // 避免退出此界面后视频音频仍播放的问题，liduohong，2016.12.15
            sf_vedio.setVisibility(View.GONE);
        }

        if (myTimer != null) {
            myTimer = null;
        }
    }

    /**
     * 暂停视频
     */
    private void pauseVideo() {
        // 暂停
        if (player != null && player.isPlaying()) {
            player.pause();
            seekProgress = (int) player.getCurrentPosition();
        }
        btn_playOrPause.setBackgroundResource(R.drawable.playbtn);
        // play_btn.setVisibility(View.VISIBLE);
    }

    /**
     * 停止播放视频
     */
    private void stopVideo() {
        // 停止播放
        if (player != null && player.isPlaying()) {
            player.stop();
            // 如果用户收藏可以通过此方法记录当前位置，以便下次浏览直接从此位置开始播放，2016.12.15
            seekProgress = (int) player.getCurrentPosition();

            btn_playOrPause.setBackgroundResource(R.drawable.playbtn);
        }
    }

    /**
     * 播放音频或视频
     */
    private void playVideo() {
        // 准备完毕，开始播放
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 总播放时间
                if (player != null) {
                    seekTotalProgress = (int) player.getDuration();
                    seek_control.setMax(seekTotalProgress);
                    tv_time_total.setText(FormatUtils
                            .converLongTimeToStr(seekTotalProgress));

                    // 当前播放时间
                    if (myTimer == null) {
                        myTimer = new Timer();
                    }
                    myTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(UPDATE_PROGRESS);
                        }
                    }, 0, 1000);
                    // Log.e("进度2", seekProgress + "");

                    player.setDisplay(holder);

                    if (pauseOrPlay) {
                        // 开始播放
                        restartVideo();
                    }
                }
            }
        });

        // 播放完成
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                pauseVideo();
            }
        });

        // 适配不拉伸
        player.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                vedioWidth = width;
                vedioHeight = height;
                changeVedioSize();
            }
        });
    }

    /**
     * 获取当前系统STREAM_MUSIC的音量的最大值
     */
    private int getMaxVolume() {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * 获取当前系统STREAM_MUSIC的音量
     */
    private int getCurrentVolume() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    private class Listeners implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            if (viewId == R.id.ll_play_download_laryout_aty_showmovie) { // 底部：播放/暂停按钮
                pauseOrPlay = !pauseOrPlay;
                if (pauseOrPlay) {
                    restartVideo();
                } else {
                    pauseVideo();
                }
            } else if (viewId == R.id.btn_play_layout_aty_showmovie) {// 播放/暂停按钮
                pauseOrPlay = !pauseOrPlay;
                restartVideo();
                // playVideo();
            } else if (viewId == R.id.ll_back_layout_aty_showmovie) {// 返回
                // 重写finish()方法
                finished();
            } else if (viewId == R.id.ll_qp) {// 半屏或全屏
                // switchFullOrHalfScreen();

                if (isFullScreen) {// 全屏状态，需要切换为小屏
                    switchHalfScreen();
                } else {//
                    switchFullScreen();
                }
            } else if (viewId == R.id.ll_wrapper_select_lesson_layout_aty_showmovie) {// 打开剧集列表
                llSelectLesson.setVisibility(View.GONE);
                // ll_share.setVisibility(View.GONE);
                // llCollect.setVisibility(View.GONE);

                ll_moreMovie.setVisibility(View.VISIBLE);

                // List<CourseChapterModel> chapterList = new
                // ArrayList<CourseChapterModel>();
                // for (int i = 0; i < chapterList.size(); i++) {
                // CourseChapterModel chapter = new CourseChapterModel();
                // chapter = (CourseChapterModel) infoList.get(i);
                // coursesInfoList.add(coursesInfo);
                // }

                // lstvVideo.initFata(chapterList, "");
                showLstv(infoList);
            } else if (viewId == R.id.ll_rightMore) {// 关闭右侧菜单栏
                ll_moreMovie.setVisibility(View.GONE);
                llSelectLesson.setVisibility(View.VISIBLE);
                // llCollect.setVisibility(View.VISIBLE);
                // ll_share.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 设置listVew展示
     *
     * @param list
     */
    private void showLstv(List<VideoAudio> list) {
        // CustomExpandableHeaderAdapter adapter = new
        // CustomExpandableHeaderAdapter(
        // getActivity(), list, R.color.black, R.color.white);

        VideoCatalogAdapter adapter = new VideoCatalogAdapter(getActivity(),
                list);
        lstvVideo.setAdapter(adapter);
    }

    /**
     * 关闭
     */
    public void finished() {
        if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            switchFullOrHalfScreen();
            // llCollect.setVisibility(View.VISIBLE);
            // ll_share.setVisibility(View.VISIBLE);
        } else {
            player = null;
            callback.doAfterClickBack();
            getActivity().finish();
        }
    }

    /**
     * 从阿里云oss获取视频url
     */
    // private String obtainVideoUrlFromAliyOSS(String name) {
    // AliyOssHelperUtils utils = new AliyOssHelperUtils(getActivity(), name);
    // String url = utils.obtainVideoURL();
    // return url;
    // }

    /**
     * 从阿里云oss获取音频url
     */
    // private String obtainAudioUrlFromAliyOSS(String name) {
    // AliyOssHelperUtils utils = new AliyOssHelperUtils(getActivity(), name);
    // String url = utils.obtainAudioURL();
    // return url;
    // }
    @Override
    public void onPause() {
        super.onPause();

        // 暂停
        pauseVideo();
    }

    @Override
    public void onDestroy() {
        stopVideo();
        resetVideo();

        super.onDestroy();
    }

    @Override
    public void ICanGetInfoSelected(VideoAndAudioInfoModel info) {// 切换视频或音频显示
        if (info == null) {
            return;
        }

        // 进度重置
        seekProgress = 0;
        if (null != movieUri) {
            movieUri = null;
        }

        // 判断当前状态是音频播放还是视频播放
        playType = info.getType();

        // 赋值名称
        if (tv_dyName != null) {
            name = info.getName();
            if (!ValidateFormatUtils.isEmpty(name)) {// 名称不为空值，设置显示
                tv_dyName.setText(info.getName());
            }
        }
        // 取出资源简要路径
        movieUri = info.getSimpleUri();

        // 播放视频或音频
        loadVideo(movieUri, playType);

        // 重置当前收藏状态为未收藏
        // imgvCollect.setBackground(res.getDrawable(R.drawable.collect));
        // hasCollected = false;

        // 当前若为全屏状态，关闭右侧弹出框
        if (isFullScreen) {
            ll_rightMore.performClick();
        }
    }

    @Override
    public void ICanGetInfoList(List<VideoAndAudioInfoModel> infoList) {
        // 将列表信息展示到右侧弹出的抽屉布局里,此处的信息是ClassCircleCoursesModel，包括图文、文档等所有类型数据
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public boolean handleMessage(Message msg) {
        int what = msg.what;
        if (what == 1) {
            Toast.makeText(getActivity(), "分享失败", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    public void doOnStopVideo() {
        onDestroy();
    }

}
