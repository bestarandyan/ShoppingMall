package com.manyi.mall.cachebean.search;

import java.util.List;

import com.huoqiu.framework.rest.Response;


/**
 * 
 */

/**
 * @author zxc
 *
 */
public class IncrementalCityResponce extends Response{

	private String version;

	private List<City> cityList;

	private boolean isUpdate;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isUpdate() {
		return isUpdate;
	}

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public List<City> getCityList() {
		return cityList;
	}

	public void setCityList(List<City> cityList) {
		this.cityList = cityList;
	}
}
