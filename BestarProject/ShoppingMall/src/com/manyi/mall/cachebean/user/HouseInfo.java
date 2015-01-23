package com.manyi.mall.cachebean.user;

import java.math.BigDecimal;

import com.huoqiu.framework.rest.Response;

public class HouseInfo extends Response {
	private int houseId;// 房屋ID
	private String building;// 楼座编号（例如：22栋，22坐，22号）
	private BigDecimal spaceArea;// 内空面积
	private int bedroomSum;// 几房

	private String estateName;// 小区名称
	private String subEstateName;// 子划分名称
	private String areaName;// 行政区name
	private String townName;// 小区所属片区name
	private BigDecimal price;// 价格
	private int picNum; // 图片数目

	public int getPicNum() {
		return picNum;
	}

	public void setPicNum(int picNum) {
		this.picNum = picNum;
	}

	public int getHouseId() {
		return houseId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
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

	public String getEstateName() {
		return estateName;
	}

	public void setEstateName(String estateName) {
		this.estateName = estateName;
	}

	public String getSubEstateName() {
		return subEstateName;
	}

	public void setSubEstateName(String subEstateName) {
		this.subEstateName = subEstateName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
