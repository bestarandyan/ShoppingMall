package com.manyi.mall.cachebean.user;

import org.androidannotations.annotations.EBean;


@EBean
public class InformRequest {	
	
	private int houseId;

	public int getHouseId() {
		return houseId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}
}
