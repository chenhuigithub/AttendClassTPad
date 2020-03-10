package com.example.attendclasstpad.model;

/**
 * ID数据模型类
 * 
 * @author chenhui 2018.11.21
 * 
 */
public class DataID01 {
	private String chid;// 章节ID
	private String cid;// 当前的栏目ID
	private String index;// 预览资源的第几页，默认第1页

	private String dataid;// guid,资源ID

	public String getChid() {
		return chid;
	}

	public void setChid(String chid) {
		this.chid = chid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getDataid() {
		return dataid;
	}

	public void setDataid(String dataid) {
		this.dataid = dataid;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

}
