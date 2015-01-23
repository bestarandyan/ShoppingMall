/**
 * 
 */
package com.manyi.mall.cachebean.search;

/**
 * @author bestar
 *
 */
public class GetTaskRequest {
	private int subEstateId; 
	private String building;// 楼座编号（例如：22栋 或者  1-1）  
	private String room;// 房号（例如：1304室，A1004） 
	
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
