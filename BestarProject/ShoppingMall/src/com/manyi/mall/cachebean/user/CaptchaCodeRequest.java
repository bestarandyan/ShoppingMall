package com.manyi.mall.cachebean.user;

import org.androidannotations.annotations.EBean;

@EBean
public class CaptchaCodeRequest {
	
	private String mobile; 
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
