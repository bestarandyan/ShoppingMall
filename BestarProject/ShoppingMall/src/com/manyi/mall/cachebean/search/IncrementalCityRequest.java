package com.manyi.mall.cachebean.search;


/**
 * 
 */

/**
 * @author zxc
 *
 */
public class IncrementalCityRequest {

	private int uid;
	
	private String version;

	public int getUid() {
		return uid;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
}
