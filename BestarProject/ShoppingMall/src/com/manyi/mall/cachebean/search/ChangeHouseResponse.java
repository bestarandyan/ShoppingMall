package com.manyi.mall.cachebean.search;

import com.huoqiu.framework.rest.Response;


public class ChangeHouseResponse extends Response{

	
	private int houseId;//房子ID
	private  boolean sellEnabled;//是否在租
	private  boolean rentEnabled;//是否在售
	public int getHouseId() {
		return houseId;
	}
	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}
	public boolean isSellEnabled() {
		return sellEnabled;
	}
	public void setSellEnabled(boolean sellEnabled) {
		this.sellEnabled = sellEnabled;
	}
	public boolean isRentEnabled() {
		return rentEnabled;
	}
	public void setRentEnabled(boolean rentEnabled) {
		this.rentEnabled = rentEnabled;
	}
}
