package com.example.attendclasstpad.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 附加信息：设备信息、应用信息等
 * 
 * @author chenhui 2019.01.04
 * 
 */
public class AdditionalInfo {
	private String deviceType;// 设备类型：1:Android手机，2:Android平板，3:IOS手机，4:IOS平板
	private String deviceName;// 设备名称
	private String deviceModel;// 设备型号
	private String deviceVersion;// 设备系统版本
	private String deviceSerialNumber;// 设备序列号:IMEI，唯一标识

	private String appVersion;// 应用版本

	@JSONField(name = "ctype")
	public String getDeviceType() {
		return deviceType;
	}

	@JSONField(name = "ctype")
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	@JSONField(name = "devname")
	public String getDeviceName() {
		return deviceName;
	}

	@JSONField(name = "devname")
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	@JSONField(name = "devmodel")
	public String getDeviceModel() {
		return deviceModel;
	}

	@JSONField(name = "devmodel")
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	@JSONField(name = "devver")
	public String getDeviceVersion() {
		return deviceVersion;
	}

	@JSONField(name = "devver")
	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}

	@JSONField(name = "number")
	public String getDeviceSerialNumber() {
		return deviceSerialNumber;
	}

	@JSONField(name = "number")
	public void setDeviceSerialNumber(String deviceSerialNumber) {
		this.deviceSerialNumber = deviceSerialNumber;
	}

	@JSONField(name = "appver")
	public String getAppVersion() {
		return appVersion;
	}

	@JSONField(name = "appver")
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
}
