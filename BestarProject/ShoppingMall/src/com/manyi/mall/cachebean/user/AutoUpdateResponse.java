package com.manyi.mall.cachebean.user;

import org.androidannotations.annotations.EBean;

import com.huoqiu.framework.rest.Response;

@EBean
public class AutoUpdateResponse extends Response{
	/**
	 * 下载地址
	 */
	private String url;
	/**
	 * 消息主体
	 */
//	private String message;
//	private String title;
	/**
	 * 版本号
	 */
	private String version;
	/**
	 * 是否强制执行
	 */
	private boolean ifForced;;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

//	public String getMessage() {
//		return message;
//	}
//	
//	public void setMessage(String message) {
//		this.message = message;
//	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public boolean isIfForced() {
		return ifForced;
	}

	public void setIfForced(boolean ifForced) {
		this.ifForced = ifForced;
	}

	
	
}
