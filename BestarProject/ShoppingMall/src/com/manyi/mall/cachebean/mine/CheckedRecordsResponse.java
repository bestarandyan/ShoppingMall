/**
 * 
 */
package com.manyi.mall.cachebean.mine;

import java.util.List;

import com.huoqiu.framework.rest.Response;

/**
 * @author bestar
 * 
 */
public class CheckedRecordsResponse extends Response {

	public List<CheckedListResponse> result;

	

	public List<CheckedListResponse> getResult() {
		return result;
	}

	public void setResult(List<CheckedListResponse> result) {
		this.result = result;
	}

	public static class CheckedListResponse {

		private int recordCount;// 当天审核成功数量

		private String resultDateStr; // 审核完成时间

		List<CheckedResponse> examineRecodList;

		public int getRecordCount() {
			return recordCount;
		}

		public void setRecordCount(int recordCount) {
			this.recordCount = recordCount;
		}

		public String getResultDateStr() {
			return resultDateStr;
		}

		public void setResultDateStr(String resultDateStr) {
			this.resultDateStr = resultDateStr;
		}

		public List<CheckedResponse> getExamineRecodList() {
			return examineRecodList;
		}

		public void setExamineRecodList(List<CheckedResponse> examineRecodList) {
			this.examineRecodList = examineRecodList;
		}

	}

	public static class CheckedResponse {

		private String estateName;// 小区名称
		private String subEstateName;// 子划分名称
		private int status;// 状态，1审核通过,2审核中，3审核失败, 4删除
		private String statusStr;// 审核状态对应的文本
		private int houseState;// 1出租，2出售，3即租又售，4即不租也不售
		private String houseStateStr;// house状态对应的文本
		private String publishDate;// 发布时间，此房源被发布，改盘，轮询的时间
		private String building;// 楼栋号
		private int historyId;
		private int houseId;
		private int typeId;// 记录 类型ID
		private String typeName;// 记录 类型(1.发布出售2.发布出租3.改盘4.举报5.新增小区)
		private String buildingNameStr;// 本土化后显示的楼栋名称
		private int hot;// 热点： 1 是，0 不是

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

		public int getHouseId() {
			return houseId;
		}

		public void setHouseId(int houseId) {
			this.houseId = houseId;
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

		public String getBuilding() {
			return building;
		}

		public void setBuilding(String building) {
			this.building = building;
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

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getStatusStr() {
			return statusStr;
		}

		public void setStatusStr(String statusStr) {
			this.statusStr = statusStr;
		}

		public int getHouseState() {
			return houseState;
		}

		public void setHouseState(int houseState) {
			this.houseState = houseState;
		}

		public String getHouseStateStr() {
			return houseStateStr;
		}

		public void setHouseStateStr(String houseStateStr) {
			this.houseStateStr = houseStateStr;
		}

		public String getPublishDate() {
			return publishDate;
		}

		public void setPublishDate(String publishDate) {
			this.publishDate = publishDate;
		}

		public int getHistoryId() {
			return historyId;
		}

		public void setHistoryId(int historyId) {
			this.historyId = historyId;
		}

	}

}
