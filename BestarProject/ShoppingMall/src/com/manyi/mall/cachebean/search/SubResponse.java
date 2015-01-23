/**
 * 
 */
package com.manyi.mall.cachebean.search;

import java.util.List;

import com.huoqiu.framework.rest.Response;

/**
 * @author bestar
 *
 */
public class SubResponse extends Response{

	private int errorCode = 0; 
    private String message;
    List<SubEstateResponse> subEstateList;
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
	public List<SubEstateResponse> getSubEstateList() {
		return subEstateList;
	}
	public void setSubEstateList(List<SubEstateResponse> subEstateList) {
		this.subEstateList = subEstateList;
	}
    
	 
	 
}
