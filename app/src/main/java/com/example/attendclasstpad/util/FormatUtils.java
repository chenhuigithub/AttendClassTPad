package com.example.attendclasstpad.util;

import android.text.TextUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class FormatUtils {

	/**
	 * 格式化字符
	 * */
	public static String formatNum(int num) {

		DecimalFormat formater = new DecimalFormat();
		formater.setMaximumFractionDigits(2);
		formater.setGroupingSize(0); // 设置每0个一组
		formater.setRoundingMode(RoundingMode.FLOOR);

		if (num <= 10000) {

			return num + "";
		} else if (num >= 10000 && num < 100000000) {
			return formater.format(num / 10000.0) + "万";
		} else {
			return formater.format(num / 100000000.0) + "亿";
		}
	}

	/**
	 * 字符转数字
	 * */
	public static int StringToInt(String str) {
		int num;
		try {
			num = Integer.parseInt(str);
		} catch (Exception e) {
			num = 0;
		}
		return num;
	}

	/**
	 * 格式化时间
	 * */
	public static String formatData(String data) {

		if (TextUtils.isEmpty(data)) {
			return "暂无";
		}

		if (data.length() >= 16) {
			return data.substring(0, 16);
		}
		return data;
	}

	/**
	 * 转换毫秒数成“分、秒”，如“01:53”。若超过60分钟则显示“时、分、秒”，如“01:01:30
	 * 
	 * @param time
	 *            待转换的毫秒数
	 * */
	public static String converLongTimeToStr(long time) {
		int ss = 1000;
		int mi = ss * 60;
		int hh = mi * 60;

		long hour = (time) / hh;
		long minute = (time - hour * hh) / mi;
		long second = (time - hour * hh - minute * mi) / ss;

		String strHour = hour < 10 ? "0" + hour : "" + hour;
		String strMinute = minute < 10 ? "0" + minute : "" + minute;
		String strSecond = second < 10 ? "0" + second : "" + second;

		if (hour > 0) {

			return strHour + " " + ":" + " " + strMinute + " " + ":" + " " + strSecond + " ";
		} else {
			return " " + strMinute + " " + ":" + " " + strSecond + " ";
		}
	}

	/**
	 * 转换文件大小
	 * */
	public static String FormetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}
}