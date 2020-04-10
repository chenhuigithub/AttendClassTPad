package com.example.attendclasstpad.model;

/**
 * 视频、音频信息模型类
 * 
 * @author zhaochenhui
 * 
 */
public class VideoAndAudioInfoModel {
	private String id;// 唯一ID
	private String uri;// 网络地址
	private String simpleUri;// 资源路径
	private String name;// 资源的文字名称
	private String type;// 类型（视频：0，音频：1)
	private String isFree;// 是否免费(0 ?,1?)

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getSimpleUri() {
		return simpleUri;
	}

	public void setSimpleUri(String simpleUri) {
		this.simpleUri = simpleUri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsFree() {
		return isFree;
	}

	public void setIsFree(String isFree) {
		this.isFree = isFree;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
