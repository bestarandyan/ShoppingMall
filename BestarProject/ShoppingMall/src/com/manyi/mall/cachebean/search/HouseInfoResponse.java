/**
 * 
 */
package com.manyi.mall.cachebean.search;

import java.math.BigDecimal;

/**
 *
 */
public class HouseInfoResponse {
	private String subEstateName;// 子划分名称
	private String name;// 小区名称
	private String address;// 地址
	private String building;// 栋座号
	private String room;// 室号
	private BigDecimal spaceArea = new BigDecimal(0);// 内空面积
	private int bedroomSum;// 几房
	private int livingRoomSum;// 几厅
	private int wcSum;// 几卫
	private BigDecimal sellPrice;// 出售价格
	private BigDecimal rentPrice;// 出租价格
	private int houseState;// 1出租，2出售，3即租又售
	private int houseId;
	public String getSubEstateName() {
		return subEstateName;
	}

	public void setSubEstateName(String subEstateName) {
		this.subEstateName = subEstateName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public BigDecimal getSpaceArea() {
		return spaceArea;
	}

	public void setSpaceArea(BigDecimal spaceArea) {
		this.spaceArea = spaceArea;
	}

	public int getBedroomSum() {
		return bedroomSum;
	}

	public void setBedroomSum(int bedroomSum) {
		this.bedroomSum = bedroomSum;
	}

	public int getLivingRoomSum() {
		return livingRoomSum;
	}

	public void setLivingRoomSum(int livingRoomSum) {
		this.livingRoomSum = livingRoomSum;
	}

	public int getWcSum() {
		return wcSum;
	}

	public void setWcSum(int wcSum) {
		this.wcSum = wcSum;
	}

	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	public BigDecimal getRentPrice() {
		return rentPrice;
	}

	public void setRentPrice(BigDecimal rentPrice) {
		this.rentPrice = rentPrice;
	}

	public int getHouseState() {
		return houseState;
	}

	public void setHouseState(int houseState) {
		this.houseState = houseState;
	}

	public int getHouseId() {
		return houseId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}
}
