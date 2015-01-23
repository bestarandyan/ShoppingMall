package com.manyi.mall.cachebean.user;

import org.androidannotations.annotations.EBean;

@EBean
public class LoginRequest {
	private String loginName; 
	private String password;

	public String getLoginName() {
		return loginName;
	}

	
	
	
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
