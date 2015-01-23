package com.manyi.mall.cachebean.user;


public class TaskCommitRequest {
	/**
	 * 
	 private int taskId; private int houseId; private int bedroomSum;// 几房 private int livingRoomSum;// 几厅 private int wcSum;// 几卫
	 * 
	 * private int picNum; //图片数目 private String latitude;// 坐标 纬度 private String longitude;// 坐标 经度
	 * 
	 * //装修类型 private int decorateType; //1 毛坯，2 装修 //装修 private HouseSupportingMeasures houseSupportingMeasures;
	 */
	private int taskId;
	private int houseId;
	private int bedroomSum;// 几房
	private int livingRoomSum;// 几厅
	private int wcSum;// 几卫

	private int picNum; // 图片数目
	private String latitude;// 坐标 纬度
	private String longitude;// 坐标 经度

	public int getPicNum() {
		return picNum;
	}

	public void setPicNum(int picNum) {
		this.picNum = picNum;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getHouseId() {
		return houseId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
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

}
