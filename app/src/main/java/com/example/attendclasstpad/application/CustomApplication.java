package com.example.attendclasstpad.application;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 自定义application,用于管理整个程序
 * 
 * @author
 * 
 */
public class CustomApplication extends Application {

	public static Context applicationContext;
	private static CustomApplication instance;

	public static int Screen_Width;
	public static int Screen_Height;
	public static float Screen_Density;

	private List<Activity> activityStack = new ArrayList<Activity>();
	public static boolean isFirst = true;

	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = this;
		instance = this;
		// x.Ext.init(this);
		// DisplayMetrics dm = new DisplayMetrics();
		// WindowManager manager = (WindowManager)
		// getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		// manager.getDefaultDisplay().getMetrics(dm);
		// Screen_Width=dm.widthPixels;

		Screen_Width = getResources().getDisplayMetrics().widthPixels;
		Screen_Height = getResources().getDisplayMetrics().heightPixels;
		Screen_Density = getResources().getDisplayMetrics().density;

		// 初始化本地数据库、数据表相关
		// dbUtils = XUtilsDBUtils.getDbUtils(getApplicationContext());

		// 取出首选项文件中的值
		// String isFirstRunStr = PreferencesUtils.acquireInfoFromPreferences(
		// getApplicationContext(), ConstantsUtils.IS_FIRST_RUN);
		// if (ConstantsUtils.VALUE_NO.equals(isFirstRunStr)) {// 软件不是首次运行
		// } else {
		// // 软件首次运行时，获取配置信息
		// // AliyOssHelperUtils.obtainAliyOssConfig(getApplicationContext());
		//
		// // 重置首选项中的是否登陆标记，标记未登录状态
		// PreferencesUtils.saveInfoToPreferences(getApplicationContext(),
		// ConstantsUtils.KEY_USER_IDENTITY,
		// ConstantsUtils.VALUE_NOT_LOGIN);
		// }
	}

	public void addAty(Activity aty) {
		activityStack.add(aty);
	}

	public void finishAty() {
		for (Activity aty : activityStack) {
			aty.finish();
		}
	}

	public static CustomApplication getInstance() {
		return instance;
	}

	/**
	 * 返回本程序包名、版本之类的信息
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = new PackageInfo();
		try {
			info = getPackageManager().getPackageInfo(this.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return info;
	}
}
