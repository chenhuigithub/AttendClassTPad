package com.example.attendclasstpad.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

/**
 * 网络工具类
 *
 * @author Administrator
 */
public class NetworkUtils {

    /**
     * 检测网络是否连接
     *
     * @return
     */
    public static boolean checkNetworkState(Context context) {
        boolean flag = false;
        // 得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        return flag;
    }

    /**
     * 判断当前是否使用的是 WIFI网络（无sim卡的平板可用）
     *
     * @param context
     * @return
     */
    public static int CheckNetWork(Context context) {
        int state = 0;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // 是否通过wifi连接到网络
        boolean isWifiConnected = cm.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ? true
                : false;
        // 是否通过流量连接到网络
        boolean isGprsConnected = cm.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ? true
                : false;

        if (isWifiConnected) {// wifi连接的情况
            state = 1;
        } else if (isGprsConnected == true) { // 流量连接的情况
            state = 2;
        }
        return state;
    }

    /**
     * 判断当前网络是否可用(6.0以上版本) 实时(无sim卡的平板可用)
     *
     * @param context
     * @return
     */
    public static boolean isNetSystemUsable(Context context) {
        boolean isNetUsable = false;
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            NetworkCapabilities networkCapabilities = manager
                    .getNetworkCapabilities(manager.getActiveNetwork());
            isNetUsable = networkCapabilities
                    .hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        }
        return isNetUsable;
    }
}
