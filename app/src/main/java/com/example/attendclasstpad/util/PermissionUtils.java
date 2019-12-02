package com.example.attendclasstpad.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限类
 * 注：API23以上需要动态申请读写内存等权限。本类包括检测权限与获取所有权限的功能
 *
 * @author chenhui 2019.09.03
 */
public class PermissionUtils {
    private String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private List<String> mPermissionList = new ArrayList<>();
    // private ImageView welcomeImg = null;
    public static final int PERMISSION_REQUEST = 1;

    /**
     * 检查权限
     */
    public void checkPermission(Activity context) {
        mPermissionList.clear();

        //判断哪些权限未授予
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        /**
         * 判断是否为空
         */
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(context, permissions, PERMISSION_REQUEST);
        }
    }
}
