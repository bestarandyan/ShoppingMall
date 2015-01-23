package com.manyi.mall.cachebean.search;
import org.androidannotations.annotations.EBean;

@EBean
public class SearchRequest {
	private int cityId; 
	private String name;

	



	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
