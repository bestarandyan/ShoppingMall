package com.manyi.mall.cachebean.release;
public class ReportResponse {
	private int houseId;//房子ID
	private boolean sellEnabled;//是否在租
	private boolean rentEnabled;//是否在售
	private String estateName;
	private String building;
	private String room;

	public int getHouseId() {
		return houseId;
	}
	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}
	public boolean isSellEnabled() {
		return sellEnabled;
	}
	public void setSellEnabled(boolean sellEnabled) {
		this.sellEnabled = sellEnabled;
	}
	public boolean isRentEnabled() {
		return rentEnabled;
	}
	public void setRentEnabled(boolean rentEnabled) {
		this.rentEnabled = rentEnabled;
	}
	public String getEstateName() {
		return estateName;
	}
	public void setEstateName(String estateName) {
		this.estateName = estateName;
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
