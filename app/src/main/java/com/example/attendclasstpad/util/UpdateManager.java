package com.example.attendclasstpad.util;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.attendclasstpad.R;

import java.io.File;
import java.util.HashMap;


/**
 * 下载新版本更新
 */
@SuppressWarnings("deprecation")
public class UpdateManager {
    // 下载进度，默认为零
    private int downloadProgress = 0;

    private int lastForce;// 强制升级标识（0否，1是）

    /* 保存解析的XML信息 */
    HashMap<String, String> mHashMap;

    // Apk文件名称
    private String apkName = "TeacherResource_2.apk";
    /* 下载保存路径 */
    private String mSavePath = ConstantsForDownloadUtils.PREFIX_DIR
            + "NewVersion/" + apkName;

    private Context mContext;
    private Resources res;// 资源工具
    private ViewUtils vUtils;

    private Handler uiHandler;// 主线程

    // 下载的路径
    private String appUri;

    // 服务器的apk版本号
    private int serviceCode;
    // 客户端要求的最低版本，低于最低版本时需要强制升级
    private int serviceLowCode;

    /* 更新进度条 */
    @SuppressWarnings("deprecation")
    private ProgressDialog progressDialog;

    public UpdateManager(Context context, String serviceCode,
                         String serviceLowCode, String appUri, int lastForce) {
        this.mContext = context;
        this.appUri = appUri;

        this.lastForce = lastForce;

        if (!ValidateFormatUtils.isEmpty(serviceCode)) {
            this.serviceCode = Integer.valueOf(serviceCode);
        }

        if (!ValidateFormatUtils.isEmpty(serviceLowCode)) {
            this.serviceLowCode = Integer.valueOf(serviceLowCode);
        }

        uiHandler = new Handler(context.getMainLooper());
        vUtils = new ViewUtils(context);

        res = context.getResources();

        // 构造软件下载对话框
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("正在更新");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);

        progressDialog.setProgress(0);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "取消",
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate() {
        // 获取当前软件版本
        int versionCode = getVersionCode(mContext);

        // 强制更新的情况
        if (versionCode < serviceLowCode) {
            if (lastForce == 1) {// 强制执行
                // 下载文件
                downloadApk();
                return;
            }
        }

        if (isUpdate(versionCode)) {// 符合更新的条件
            // 显示提示对话框
            showNoticeDialog();
        } else {
            Toast.makeText(mContext, "已经是最新版，欢迎使用！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 检查软件是否有更新版本
     *
     * @return
     */
    private boolean isUpdate(int versionCode) {
        // 版本判断
        if (serviceCode > versionCode) {// 服务器apk版本大于本机apk版本
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    private int getVersionCode(Context context) {
        @SuppressWarnings("deprecation")
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(
                    "com.xzb.teachingresources", 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog() {
        // 构造对话框
        Builder builder = new Builder(mContext);
        builder.setTitle("软件更新");
        builder.setMessage("检测到最新版，立即更新吗？");
        builder.setCancelable(false);

        // 更新
        builder.setPositiveButton("立即更新", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                // 下载文件
                downloadApk();
            }
        });
        // 稍后更新
        builder.setNegativeButton("以后再说", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog(int progress) {
        progressDialog.setProgress(progress);
        progressDialog.show();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 下载项目唯一ID累计加1，区别每个下载任务
        DownloadUtil.get().download(appUri, mSavePath, apkName,
                new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess() {
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(
                                        mContext,
                                        res.getString(R.string.download_finish),
                                        Toast.LENGTH_SHORT).show();

                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                installApk();
                            }
                        });
                    }

                    @Override
                    public void onDownloading(final int progress) {
                        // 顶部下载进度提示
                        if (progress % 10 == 0 && downloadProgress != progress) {// 判断downloadProgress!=progress是避免重复调用getNotication方法，显示缓慢
                            Log.i("进度", String.valueOf(progress));

                            // 赋值，避免重复调用getNotication方法，显示缓慢
                            downloadProgress = progress;

                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // 显示下载对话框
                                    showDownloadDialog(progress);
                                }
                            });
                        }
                    }

                    @Override
                    public void onDownloadFailed() {
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(
                                        mContext,
                                        res.getString(R.string.download_fail)
                                                + ",请重试", Toast.LENGTH_SHORT)
                                        .show();

                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }
                        });
                    }
                });
    }

    /**
     * 安装APK文件
     */
    private void installApk() {
        File apkfile = new File(mSavePath, apkName);
        if (!apkfile.exists()) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 通过Intent安装APK文件
        intent.setDataAndType(Uri.fromFile(apkfile),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);
        System.exit(0);
    }
}
