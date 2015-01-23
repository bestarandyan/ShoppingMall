package com.manyi.mall.cachebean.release;

import org.androidannotations.annotations.EBean;

@EBean
public class RentRecordInfoRequest {

	
	private int historyId;
	private int houseId;
	private int typeId;//记录 类型ID
	private String typeName;//记录 类型(1.发布出售2.发布出租3.改盘4.举报5.新增小区)
	

	public int getHistoryId() {
		return historyId;
	}

	public void setHistoryId(int historyId) {
		this.historyId = historyId;
	}

	public int getHouseId() {
		return houseId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	
	
	
	

	

}
