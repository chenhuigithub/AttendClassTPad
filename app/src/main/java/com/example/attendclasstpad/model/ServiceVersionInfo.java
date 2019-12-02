package com.example.attendclasstpad.model;

/**
 * @author wangnan
 * @time 2017/5/25 11:06
 * @des
 */

public class ServiceVersionInfo {

	/**
	 * code : 0 msg : data :
	 * {"appname":"平板","serverVersion":"1.0.2","lowVersion"
	 * :"1.0.2","lastForce":1
	 * ,"updateurl":"http://www.mibeike.com/upload/client/release.apk"
	 * ,"upgradeinfo":"升级信息"} count : 0
	 */

	public int code;
	public String msg;
	public DataBean data;
	public int count;

	public static class DataBean {
		/**
		 * appname : 平板 serverVersion : 1.0.2 lowVersion : 1.0.2 lastForce : 1
		 * updateurl : http://www.mibeike.com/upload/client/release.apk
		 * upgradeinfo : 升级信息
		 */

		public String appname;
		public String serverVersion;
		public String lowVersion;
		public int lastForce; // 是否强制升级，0否，1是
		public String updateurl;
		public String upgradeinfo;
	}
}
