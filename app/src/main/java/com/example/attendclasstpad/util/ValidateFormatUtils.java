package com.example.attendclasstpad.util;

/**
 * 数据类格式验
 * 
 * 
 */
public class ValidateFormatUtils {
	/**
	 * 验证字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null) {
			return true;
		} else if ("".equals(str.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 符合从阿里云获取数据的条件
	 * 
	 * @author zhaochenhui,2016.09.24
	 * @param str
	 * @return
	 */
	public static boolean canBeObtainFromAliyOss(String str) {
		if (str.contains(".html")) {
			return true;
		}
		return false;
	}
}
