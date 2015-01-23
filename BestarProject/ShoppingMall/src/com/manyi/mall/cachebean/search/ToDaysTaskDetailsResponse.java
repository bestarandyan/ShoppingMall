package com.manyi.mall.cachebean.search;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.huoqiu.framework.rest.Response;

@SuppressWarnings("serial")
public class ToDaysTaskDetailsResponse extends Response implements Serializable {

	private List<HouseResourceContantResponse> hostList = new ArrayList<HouseResourceContantResponse>();
	private String name;// 小区名称
	private String address;// 地址
	private String building;// 栋座号
	private String room;// 室号
	private Date taskDate;// 时间
	private String explainStr; // 对该房屋情况作一个简要说明
	private String remark; // 如果失败，则有相关说明
	private BigDecimal spaceArea = new BigDecimal(0);// 内空面积
	private int bedroomSum;// 几房
	private int livingRoomSum;// 几厅
	private int wcSum;// 几卫
	private BigDecimal sellPrice;// 出售价格
	private BigDecimal rentPrice;// 出租价格
	private int houseState;// 1出租，2出售，3即租又售
	private int houseId;
	private int id;
	private List<String> photoStrArray; // 图片列表

	public List<String> getPhotoStrArray() {
		return photoStrArray;
	}

	public void setPhotoStrArray(List<String> photoStrArray) {
		this.photoStrArray = photoStrArray;
	}

	public List<HouseResourceContantResponse> getHostList() {
		return hostList;
	}

	public void setHostList(List<HouseResourceContantResponse> hostList) {
		this.hostList = hostList;
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

	public Date getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}

	public String getExplainStr() {
		return explainStr;
	}

	public void setExplainStr(String explainStr) {
		this.explainStr = explainStr;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static class HouseResourceContantResponse {
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
