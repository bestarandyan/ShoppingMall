package com.manyi.mall.cachebean.release;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.huoqiu.framework.rest.Response;
import com.manyi.mall.cachebean.search.HouseDetailImage;

public class SellInfoResponse extends Response {
	private int houseId;// 房屋ID
	private String building;// 楼座编号（例如：22栋，22坐，22号）
	private String room;// 房号（例如：1304室，1004－1008室等）
	private BigDecimal spaceArea;// 内空面积
	private BigDecimal rentPrice;// 出租价格
	private BigDecimal sellPrice;// 出售价格
	private int bedroomSum;// 几房
	private int livingRoomSum;// 几厅
	private int wcSum;// 几卫
	private int subEstateId;// 子划分ID
	private String estateName;// 小区名称
	private String subEstateName;// 子划分名称
	private String cityName;// 行政区name
	private String townName;// 小区所属片区name
	private Date publishDate;// 发布时间
	private String thumbnailKey_fyb;// 房源宝水印缩略图
	private List<HouseDetailImage> houseImage;// 房源图片
	private String buildingNameStr;//本土化后显示的楼栋名称
	
	

	public String getBuildingNameStr() {
		return buildingNameStr;
	}

	public void setBuildingNameStr(String buildingNameStr) {
		this.buildingNameStr = buildingNameStr;
	}

	public String getThumbnailKey_fyb() {
		return thumbnailKey_fyb;
	}

	public void setThumbnailKey_fyb(String thumbnailKey_fyb) {
		this.thumbnailKey_fyb = thumbnailKey_fyb;
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

	public BigDecimal getRentPrice() {
		return rentPrice;
	}

	public void setRentPrice(BigDecimal rentPrice) {
		this.rentPrice = rentPrice;
	}

	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
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

	public int getSubEstateId() {
		return subEstateId;
	}

	public void setSubEstateId(int subEstateId) {
		this.subEstateId = subEstateId;
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

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public List<HouseDetailImage> getHouseImage() {
		return houseImage;
	}

	public void setHouseImage(List<HouseDetailImage> houseImage) {
		this.houseImage = houseImage;
	}



}
