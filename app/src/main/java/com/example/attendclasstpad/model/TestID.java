package com.example.attendclasstpad.model;

import java.io.Serializable;

/**
 * 题目 ID
 * 
 * @author chenhui 2018.09.27
 * 
 */
public class TestID implements Serializable {
	/**
	 * 随机生成的UID
	 */
	private static final long serialVersionUID = 4501916160968696153L;

	private String chid;// 章节ID
	private String kid;// 知识点ID
	private String tid;// 题型ID
	private String lid;// 难度ID

	public String getChid() {
		return chid;
	}

	public void setChid(String chid) {
		this.chid = chid;
	}

	public String getKid() {
		return kid;
	}

	public void setKid(String kid) {
		this.kid = kid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

}
