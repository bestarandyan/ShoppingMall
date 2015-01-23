package com.manyi.mall.cachebean.release;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.huoqiu.framework.rest.Response;

public class RentAndSellReleaseRecordInfoResponse extends Response implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int houseId;// 房屋ID
	private String building;// 楼座编号（例如：22栋，22坐，22号）
	private String room;// 房号（例如：1304室，1004－1008室等）
	private BigDecimal spaceArea;// 内空面积
	private int bedroomSum;// 几房
	private int livingRoomSum;// 几厅
	private int wcSum;// 几卫

	private String estateName;// 小区名称
	private String cityName;// 行政区name
	private String townName;// 板块名称

	private int state;// 审核状态
	private String stateStr;// 审核状态对应的文本

	private Date publishDate;// 发布时间
	private String actionTypeStr; // 发布出租/出售
	private BigDecimal price;// 价格
	private String subEstateName;// 子划分名称

	private int subEstateId;// 子划分id

	private String note;// 审核失败原因

	private String buildingNameStr;// 本土化后显示的楼栋名称

	private List<HoustConcat> houstConcatList;

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

	public String getEstateName() {
		return estateName;
	}

	public void setEstateName(String estateName) {
		this.estateName = estateName;
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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateStr() {
		return stateStr;
	}

	public void setStateStr(String stateStr) {
		this.stateStr = stateStr;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getActionTypeStr() {
		return actionTypeStr;
	}

	public void setActionTypeStr(String actionTypeStr) {
		this.actionTypeStr = actionTypeStr;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getSubEstateName() {
		return subEstateName;
	}

	public void setSubEstateName(String subEstateName) {
		this.subEstateName = subEstateName;
	}

	public int getSubEstateId() {
		return subEstateId;
	}

	public void setSubEstateId(int subEstateId) {
		this.subEstateId = subEstateId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getBuildingNameStr() {
		return buildingNameStr;
	}

	public void setBuildingNameStr(String buildingNameStr) {
		this.buildingNameStr = buildingNameStr;
	}

	public List<HoustConcat> getHoustConcatList() {
		return houstConcatList;
	}

	public void setHoustConcatList(List<HoustConcat> houstConcatList) {
		this.houstConcatList = houstConcatList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static class HoustConcat implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String hostName;// 联系人姓名
		private String hostMobile;// 联系人电话

		public String getHostName() {
			return hostName;
		}

		public void setHostName(String hostName) {
			this.hostName = hostName;
		}

		public String getHostMobile() {
			return hostMobile;
		}

		public void setHostMobile(String hostMobile) {
			this.hostMobile = hostMobile;
		}

	}

}
