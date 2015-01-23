package com.manyi.mall.cachebean.search;

import java.io.Serializable;

import com.huoqiu.framework.rest.Response;

/**
 * @author bestar
 *
 */
@SuppressWarnings("serial")
public class SubEstateResponse extends Response implements Serializable{
	 private int subEstateId; 
	 private String subEstateName;
	public int getSubEstateId() {
		return subEstateId;
	}
	public void setSubEstateId(int subEstateId) {
		this.subEstateId = subEstateId;
	}
	public String getSubEstateName() {
		return subEstateName;
	}
	public void setSubEstateName(String subEstateName) {
		this.subEstateName = subEstateName;
	} 
    
}
