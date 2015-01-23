package com.manyi.mall.cachebean.mine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.huoqiu.framework.rest.Response;

public class MineRecordsResponse extends Response {

	private List<SourceLogResponse> sourceList = new ArrayList<SourceLogResponse>();

	public List<SourceLogResponse> getSourceList() {
		return sourceList;
	}

	public void setSourceList(List<SourceLogResponse> sourceList) {
		this.sourceList = sourceList;
	}

	public static class SourceLogResponse extends Response {

		private int historyId;

		private String returnMoney;// 返回10元
		private int houseId;
		private int estateId;// 小区id
		private String estateName;// 小区name
		private String building;// 楼座编号（例如：22栋，22坐，22号）
		private String room;// 房号（例如：1304室，1004－1008室等）
		private Date publishDate;// 发布时间
		private String publishStr;
		private String subEstateName;// 子划分名称
		private int sourceState;// 审核状态(0 审核通过 1 审核中 2 审核失败)
		private String sourceStateStr;// 审核状态,文本 (0 审核通过 1 审核中 2 审核失败)
		private int typeId;// 记录 类型ID
		private String typeName;// 记录 类型(1.发布出售2.发布出租3.改盘4.举报5.新增小区)
		private Long markTime;
		private int hot;// 热点： 1 是，0 不是
		private String buildingNameStr;//本土化后显示的楼栋名称
		public String getBuildingNameStr() {
			return buildingNameStr;
		}

		public void setBuildingNameStr(String buildingNameStr) {
			this.buildingNameStr = buildingNameStr;
		}

		public int getHot() {
			return hot;
		}

		public void setHot(int hot) {
			this.hot = hot;
		}

		public String getSubEstateName() {
			return subEstateName;
		}

		public void setSubEstateName(String subEstateName) {
			this.subEstateName = subEstateName;
		}

		public Long getMarkTime() {
			return markTime;
		}

		public void setMarkTime(Long markTime) {
			this.markTime = markTime;
		}

		public String getSourceStateStr() {
			return sourceStateStr;
		}

		public void setSourceStateStr(String sourceStateStr) {
			this.sourceStateStr = sourceStateStr;
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

		public String getPublishStr() {
			return publishStr;
		}

		public void setPublishStr(String publishStr) {
			this.publishStr = publishStr;
		}

		public int getSourceState() {
			return sourceState;
		}

		public void setSourceState(int sourceState) {
			this.sourceState = sourceState;
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

		public int getHouseId() {
			return houseId;
		}

		public void setHouseId(int houseId) {
			this.houseId = houseId;
		}

		public String getReturnMoney() {
			return returnMoney;
		}

		public void setReturnMoney(String returnMoney) {
			this.returnMoney = returnMoney;
		}

		public int getHistoryId() {
			return historyId;
		}

		public void setHistoryId(int historyId) {
			this.historyId = historyId;
		}

	}

}
