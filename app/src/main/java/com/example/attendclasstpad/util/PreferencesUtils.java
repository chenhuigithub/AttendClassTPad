package com.example.attendclasstpad.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 首选项工具类
 */
public class PreferencesUtils {
    /**
     * 首选项文件名称
     */
    public static final String preferencesFiledName = "Say365New";

    /**
     * 存储信息到首选项
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveInfoToPreferences(Context context, String key, String value) {
        SharedPreferences preferences = (context.getApplicationContext()).getSharedPreferences(preferencesFiledName, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 存储信息到首选项
     *
     * @param context
     * @param key
     * @param value
     * @author zhaochenhui, 2016.06.27
     */
    public static void saveInfoToPreferences(Context context, String key, boolean value) {
        SharedPreferences preferences = (context.getApplicationContext()).getSharedPreferences(preferencesFiledName, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 从首选项中读取信息
     *
     * @param context
     * @param key
     * @return 注意：信息为空时默认返回""
     */
    public static String acquireInfoFromPreferences(Context context, String key) {
        SharedPreferences preferences = (context.getApplicationContext()).getSharedPreferences(preferencesFiledName, Context.MODE_PRIVATE);
        String value = preferences.getString(key, "");
        return value;
    }

    /**
     * 从首选项中读取信息
     *
     * @param context
     * @param key
     * @return 注意：信息为空时默认返回""
     */
    public static boolean acquireBooleanInfoFromPreferences(Context context, String key) {
        SharedPreferences preferences = (context.getApplicationContext()).getSharedPreferences(preferencesFiledName, Context.MODE_PRIVATE);
        boolean value = preferences.getBoolean(key, false);
        return value;
    }
}
