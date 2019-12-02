package com.example.attendclasstpad.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 题目信息模型类
 * 
 * @author chenhui 2018.10.09
 * 
 */
public class TestData {
	private String typeID;// 题型ID
	private String typeName;// 题目名称

	private List<Test> testList;

	@JSONField(name = "DataID")
	public String getTypeID() {
		return typeID;
	}

	@JSONField(name = "DataID")
	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}

	@JSONField(name = "DataName")
	public String getTypeName() {
		return typeName;
	}

	@JSONField(name = "DataName")
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@JSONField(name = "DataList")
	public List<Test> getTestList() {
		return testList;
	}

	@JSONField(name = "DataList")
	public void setTestList(List<Test> testList) {
		this.testList = testList;
	}

}
