package com.manyi.mall.cachebean.search;

import org.androidannotations.annotations.EBean;

@EBean
public class EstateFeedbackRequest {
	private int userId; // 反馈用户id
	private String estateName; // 小区名称
	private String estateAddr; // 小区地址
	private String content; // 反馈内容 （小于等于100字符）

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getEstateName() {
		return estateName;
	}

	public void setEstateName(String estateName) {
		this.estateName = estateName;
	}

	public String getEstateAddr() {
		return estateAddr;
	}

	public void setEstateAddr(String estateAddr) {
		this.estateAddr = estateAddr;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
