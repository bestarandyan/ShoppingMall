package com.manyi.mall.cachebean.user;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;

import com.huoqiu.framework.rest.Response;

@EBean
public class HouseResponse extends Response {
	private List<HouseInfo> houseList = new ArrayList<HouseInfo>();

	public List<HouseInfo> getHouseList() {
		return houseList;
	}

	public void setHouseList(List<HouseInfo> houseList) {
		this.houseList = houseList;
	}
}
