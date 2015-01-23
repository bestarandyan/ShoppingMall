package com.manyi.mall.cachebean.search;

import java.io.Serializable;

public class TownResponse implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;
	private int townId;
	private String name;
	private boolean flag;// 是否子节点 ，true,是；false否

	public int getTownId() {
		return townId;
	}

	public void setTownId(int townId) {
		this.townId = townId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
