package com.manyi.mall.cachebean.user;

import com.huoqiu.framework.rest.Response;



public class CaptchaCodeResponse extends Response{

	private String msgCode;

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}
	
}
