package com.manyi.mall.cachebean.release;

import com.huoqiu.framework.rest.Response;


public class CommonReleasedFirstResponse extends Response{

	private int houseId;
	private String token;
	

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getHouseId() {
		return houseId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}
}
