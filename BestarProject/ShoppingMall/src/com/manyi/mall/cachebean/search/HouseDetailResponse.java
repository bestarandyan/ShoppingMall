package com.manyi.mall.cachebean.search;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.huoqiu.framework.rest.Response;

public class HouseDetailResponse extends Response implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	private String note;// 审核失败原因

	private int houseState;// 1出租，2出售，3即租又售，4即不租也不售

	private List<Host> hostList;// 联系人

	private int status;// 状态，1审核通过,2审核中，3审核失败, 4无效/删除

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<Host> getHostList() {
		return hostList;
	}

	public void setHostList(List<Host> hostList) {
		this.hostList = hostList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public String getThumbnailKey_fyb() {
		return thumbnailKey_fyb;
	}

	public void setThumbnailKey_fyb(String thumbnailKey_fyb) {
		this.thumbnailKey_fyb = thumbnailKey_fyb;
	}

	public List<HouseDetailImage> getHouseImage() {
		return houseImage;
	}

	public void setHouseImage(List<HouseDetailImage> houseImage) {
		this.houseImage = houseImage;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getHouseState() {
		return houseState;
	}

	public void setHouseState(int houseState) {
		this.houseState = houseState;
	}

	public static class Host {

		private String hostMobile;// 联系人电话

		private String hostName;// 联系人姓名

		public String getHostMobile() {
			return hostMobile;
		}

		public void setHostMobile(String hostMobile) {
			this.hostMobile = hostMobile;
		}

		public String getHostName() {
			return hostName;
		}

		public void setHostName(String hostName) {
			this.hostName = hostName;
		}

	}

}
