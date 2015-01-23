package com.manyi.mall.cachebean.release;

import org.androidannotations.annotations.EBean;

@EBean
public class CommonReleasedFirstRequest{

	private int subEstateId;
	private String building;
	private String room;
	private int houseType;
	
	public int getSubEstateId() {
		return subEstateId;
	}
	
	public void setSubEstateId(int subEstateId) {
		this.subEstateId = subEstateId;
	}


	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public int getHouseType() {
		return houseType;
	}

	public void setHouseType(int houseType) {
		this.houseType = houseType;
	}

}
