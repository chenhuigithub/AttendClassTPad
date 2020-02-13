package com.example.attendclasstpad.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 提交服务器所需的总数据模型类
 * 
 * @author chenhui
 * 
 */
public class Bean implements Serializable {

	/**
	 * 随机生成的uid,用于兼容
	 */
	private static final long serialVersionUID = 2533930929202823497L;

	private DataID01 data;// ID数据

	@JSONField(name = "data")
	private TestID dataID;// 题目数据

	private String index;// 当前页
	private String size;// 分页数

	public DataID01 getData() {
		return data;
	}

	public void setData(DataID01 data) {
		this.data = data;
	}

	public TestID getDataID() {
		return dataID;
	}

	public void setDataID(TestID dataID) {
		this.dataID = dataID;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

}
