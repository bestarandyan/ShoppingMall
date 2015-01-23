package com.manyi.mall.cachebean.user;

import org.androidannotations.annotations.EBean;

@EBean
public class UpdateUserPublicNumRequest {
	private int userId; // userId
    private int type; //   1出租，2出售
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

    
}
