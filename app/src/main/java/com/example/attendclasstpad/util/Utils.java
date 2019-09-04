package com.example.attendclasstpad.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具类
 * 
 * @author zhaochenhui，2018.05.16
 * 
 */
public class Utils {

	/**
	 * String[]转为List<String>
	 * 
	 * @param s
	 *            String[]数据
	 * @return
	 */
	public static List<String> getList(String[] s) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < s.length; i++) {
			list.add(s[i]);
		}

		return list;
	}
}
