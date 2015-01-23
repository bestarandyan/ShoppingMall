package com.manyi.mall.cachebean.release;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.huoqiu.framework.rest.Response;

public class RentRecordInfoResponse extends Response implements Serializable {

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

	private int estateId;// 小区ID
	 private String estateName;// 小区名称
	private int areaId;// 行政区ID
	 private String areaName;// 行政区name
	 private String townName;// 板块名称
	private List<String> address;// 小区地址

	private int sourceState;// 审核状态
	private String sourceStateStr;// 审核状态对应的文本

	private Date publishDate;// 发布时间
	private int sourceLogTypeId; // 发布状态
	private String sourceLogTypeStr; // 发布状态对应的文本
	private BigDecimal price;// 价格
	private List<RecordSourceHost> hosts;// 保存联系人的方式
	private String remark;// 备注
	private int stateReason;// 导致状态的可能原因,默认值0，1已租，2不租（不想租）,3已售，4，不售（不想售）,
	 // 5，不售不租，6，不售已租，我们提供选择，若以后需要新增理由直接增加。这样需要检索由某种原因导致的房源不租不售信息很方便
	private String stateReasonStr;// 导致状态的可能原因,默认值0，1已租，2不租（不想租）,3已售，4，不售（不想售）,
	 // 5，不售不租，6，不售已租，我们提供选择，若以后需要新增理由直接增加。这样需要检索由某种原因导致的房源不租不售信息很方便
	private int changeHouseType;// 该盘类型(1出租改盘，2出售改盘，3租售改盘)
	private String reportRemark;// 举报备注
	private String subEstateName;//子划分名称

	private int subEstateId;//子划分id

	private String note;//审核失败原因
	private String buildingNameStr;//本土化后显示的楼栋名称
	


	public String getBuildingNameStr() {
		return buildingNameStr;
	}

	public void setBuildingNameStr(String buildingNameStr) {
		this.buildingNameStr = buildingNameStr;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public String getSubEstateName() {
		return subEstateName;
	}

	public void setSubEstateName(String subEstateName) {
		this.subEstateName = subEstateName;
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

	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
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

	public List<String> getAddress() {
		return address;
	}

	public void setAddress(List<String> address) {
		this.address = address;
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

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public int getSourceLogTypeId() {
		return sourceLogTypeId;
	}

	public void setSourceLogTypeId(int sourceLogTypeId) {
		this.sourceLogTypeId = sourceLogTypeId;
	}

	public String getSourceLogTypeStr() {
		return sourceLogTypeStr;
	}

	public void setSourceLogTypeStr(String sourceLogTypeStr) {
		this.sourceLogTypeStr = sourceLogTypeStr;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public List<RecordSourceHost> getHosts() {
		return hosts;
	}

	public void setHosts(List<RecordSourceHost> hosts) {
		this.hosts = hosts;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStateReason() {
		return stateReason;
	}

	public void setStateReason(int stateReason) {
		this.stateReason = stateReason;
	}

	public String getStateReasonStr() {
		return stateReasonStr;
	}

	public void setStateReasonStr(String stateReasonStr) {
		this.stateReasonStr = stateReasonStr;
	}

	public int getChangeHouseType() {
		return changeHouseType;
	}

	public void setChangeHouseType(int changeHouseType) {
		this.changeHouseType = changeHouseType;
	}

	public String getReportRemark() {
		return reportRemark;
	}

	public void setReportRemark(String reportRemark) {
		this.reportRemark = reportRemark;
	}

	public static class RecordSourceHost {
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
