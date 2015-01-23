package com.manyi.mall.cachebean.release;

import org.androidannotations.annotations.EBean;

@EBean
public class ReleasedRentInfoListRequest {
	private int cityId;

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	
}
