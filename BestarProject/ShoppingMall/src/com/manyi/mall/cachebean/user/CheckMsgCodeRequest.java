package com.manyi.mall.cachebean.user;

import org.androidannotations.annotations.EBean;

@EBean
public class CheckMsgCodeRequest {
	private String checkVerifyCode;

	private String password;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCheckVerifyCode() {
		return checkVerifyCode;
	}

	public void setCheckVerifyCode(String checkVerifyCode) {
		this.checkVerifyCode = checkVerifyCode;
	}
	
}
