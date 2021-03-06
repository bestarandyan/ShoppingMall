package com.manyi.mall.cachebean.release;

import java.math.BigDecimal;

import org.androidannotations.annotations.EBean;
@EBean
public class ReleasedRentRequest {

	private int houseId;//房屋ID
	private int userId;//用户ID
	private BigDecimal spaceArea;// 内空面积
	private int bedroomSum;// 几房
	private int livingRoomSum;// 几厅
	private int wcSum;// 几卫
	private BigDecimal price;//价格
	private String hostName;//联系人姓名   多个就用, 隔开
//	private String houstMobile;//联系人电话  多个就用, 隔开 ; 这里的联系电话 跟 联系人是一一对应的
	private String houstMobile;//联系人电话只允许填一组
	private int source;//来源  1 房源宝、2 爱屋、3 MLS
	private String token; //1.8新增
	


	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public int getHouseId() {
		return houseId;
	}
	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getHoustMobile() {
		return houstMobile;
	}
	public void setHoustMobile(String houstMobile) {
		this.houstMobile = houstMobile;
	}
	
	
	
}
