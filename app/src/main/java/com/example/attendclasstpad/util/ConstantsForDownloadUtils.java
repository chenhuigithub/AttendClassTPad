package com.example.attendclasstpad.util;

import android.os.Environment;

public class ConstantsForDownloadUtils {
	/**
	 * 下载路径前缀
	 */
	public static String PREFIX_DIR = Environment.getExternalStorageDirectory()
			+ "/" + "TeachingResource" + "/";

	/**
	 * 配套资源
	 */
	public static String MATCHING_RESOURCE = PREFIX_DIR + "MatchingResource"
			+ "/";

	/**
	 * 组学案
	 */
	public static String OMICS = PREFIX_DIR + "Omics" + "/";

	/**
	 * 组试题
	 */
	public static String TEST = PREFIX_DIR + "Test" + "/";

	/**
	 * 组课件
	 */
	public static String COURSEWARE = PREFIX_DIR + "Courseware" + "/";

	/**
	 * 扩展资源
	 */
	public static String EXTEND_RESOURCE = PREFIX_DIR + "ExtendResource" + "/";
	
	
	/**
	 * 用户信息
	 */
	public static String USER = PREFIX_DIR + "User" + "/";

}
