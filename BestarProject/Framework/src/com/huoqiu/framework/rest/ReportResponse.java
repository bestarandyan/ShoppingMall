package com.huoqiu.framework.rest;

public class ReportResponse extends Response {
	
	private int errorCode = 0; 
    private String message = "";
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}