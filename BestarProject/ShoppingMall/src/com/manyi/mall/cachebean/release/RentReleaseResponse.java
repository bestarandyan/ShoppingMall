package com.manyi.mall.cachebean.release;

import java.util.Date;

import com.huoqiu.framework.rest.Response;

public class RentReleaseResponse extends Response {

	private int historyId;
	private int houseId;
	private int estateId;// 小区id
	private String estateName;// 小区name
	private String subEstateName;// 子划分name
	private String building;// 楼座编号（例如：22栋，22坐，22号）
	private String room;// 房号（例如：1304室，1004－1008室等）
	private Date publishDate;// 发布时间

	private int sourceState;// 审核状态(1 审核成功 2 审核中 3 审核失败)
	private String sourceStateStr;// 审核状态,文本 (1 审核成功 2 审核中 3 审核失败)

	private int typeId;// 发布记录 类型ID
	private String typeName;// 发布记录 类型(1.发布出售0.发布出租2.改盘3.举报6.新增小区)

	private int hot;// 热点： 1 是，0 不是
	private String buildingNameStr;// 本土化之后的楼栋名称

	public int getHistoryId() {
		return historyId;
	}

	public void setHistoryId(int historyId) {
		this.historyId = historyId;
	}

	public int getHouseId() {
		return houseId;
	}

	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}

	public int getEstateId() {
		return estateId;
	}

	public void setEstateId(int estateId) {
		this.estateId = estateId;
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

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public int getSourceState() {
		return sourceState;
	}

	public void setSourceState(int sourceState) {
		this.sourceState = sourceState;
	}

	public String getSourceStateStr() {
		return sourceStateStr;
	}

	public void setSourceStateStr(String sourceStateStr) {
		this.sourceStateStr = sourceStateStr;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getHot() {
		return hot;
	}

	public void setHot(int hot) {
		this.hot = hot;
	}

	public String getBuildingNameStr() {
		return buildingNameStr;
	}

	public void setBuildingNameStr(String buildingNameStr) {
		this.buildingNameStr = buildingNameStr;
	}

}
