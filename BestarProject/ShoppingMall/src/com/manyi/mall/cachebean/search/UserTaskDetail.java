/**
 * 
 */
package com.manyi.mall.cachebean.search;

import java.util.Date;
import java.util.List;

/**
 *
 */
public class UserTaskDetail {
	private HouseInfoResponse houseInfo;
	private Date createTime; // 领取任务时间、创建任务时间
	private Date currenDate = new Date();// 当前时间
	private int taskStatus; // 1已领取(未完成), 2审核中（提交图片完成）, 3审核成功,4审核失败,5过期
	private String note; // 任务失败原因
	private String taskImgStr; // 任务图片统计字段
	private String supportingMeasuresStr; // 配置信息统计字段
	private String houseTypeStr; // 房型变化字段
	private String rentPriceStr; // 出租金额变化字段
	private String sellPriceStr; // 出售金额变化字段
	private String spaceAreaStr; // 面积变化字段
	private int id; // userTaskId
	private List<String> photoStrArray; // 图片列表
	private Date uploadPhotoTime; //上传图片时间 

	public Date getUploadPhotoTime() {
		return uploadPhotoTime;
	}

	public void setUploadPhotoTime(Date uploadPhotoTime) {
		this.uploadPhotoTime = uploadPhotoTime;
	}

	public List<String> getPhotoStrArray() {
		return photoStrArray;
	}

	public void setPhotoStrArray(List<String> photoStrArray) {
		this.photoStrArray = photoStrArray;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public HouseInfoResponse getHouseInfo() {
		return houseInfo;
	}

	public void setHouseInfo(HouseInfoResponse houseInfo) {
		this.houseInfo = houseInfo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCurrenDate() {
		return currenDate;
	}

	public void setCurrenDate(Date currenDate) {
		this.currenDate = currenDate;
	}

	public int getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(int taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getTaskImgStr() {
		return taskImgStr;
	}

	public void setTaskImgStr(String taskImgStr) {
		this.taskImgStr = taskImgStr;
	}

	public String getSupportingMeasuresStr() {
		return supportingMeasuresStr;
	}

	public void setSupportingMeasuresStr(String supportingMeasuresStr) {
		this.supportingMeasuresStr = supportingMeasuresStr;
	}

	public String getHouseTypeStr() {
		return houseTypeStr;
	}

	public void setHouseTypeStr(String houseTypeStr) {
		this.houseTypeStr = houseTypeStr;
	}

	public String getRentPriceStr() {
		return rentPriceStr;
	}

	public void setRentPriceStr(String rentPriceStr) {
		this.rentPriceStr = rentPriceStr;
	}

	public String getSellPriceStr() {
		return sellPriceStr;
	}

	public void setSellPriceStr(String sellPriceStr) {
		this.sellPriceStr = sellPriceStr;
	}

	public String getSpaceAreaStr() {
		return spaceAreaStr;
	}

	public void setSpaceAreaStr(String spaceAreaStr) {
		this.spaceAreaStr = spaceAreaStr;
	}
}
