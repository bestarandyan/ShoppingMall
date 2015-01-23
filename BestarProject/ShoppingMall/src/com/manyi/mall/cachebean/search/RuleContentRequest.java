package com.manyi.mall.cachebean.search;

import org.androidannotations.annotations.EBean;

@EBean
public class RuleContentRequest {
	private int cityId;// cityId
	private int type;// 类型：发布出租 1; 发布出售 2; 我的奖金 3;

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
