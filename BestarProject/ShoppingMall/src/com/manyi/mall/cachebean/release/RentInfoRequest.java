package com.manyi.mall.cachebean.release;

import org.androidannotations.annotations.EBean;

@EBean
public class RentInfoRequest {
	private int houseId;

	public int getHouseId() {
		return houseId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}
	
}
