package com.manyi.mall.cachebean.search;

import org.androidannotations.annotations.EBean;

@EBean
public class ChangeHouseRequest {

	private int subEstateId; 
	private String building;
	private String room;


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
}
