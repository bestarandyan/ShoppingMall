package com.huoqiu.framework.rest;

import java.io.Serializable;

public class ReportRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String restUrl; //接口URL（见对应接口）
    private int result; //调用结果 1:正常；2：timeout 3：服务端报错
    private int responseTime; //响应时间（正常调用时）,单位毫秒
    private String errorMessage; //服务端报错信息
	public String getRestUrl() {
		return restUrl;
	}
	public void setRestUrl(String restUrl) {
		this.restUrl = restUrl;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public int getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(int responseTime) {
		this.responseTime = responseTime;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}