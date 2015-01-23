package com.manyi.mall.cachebean.search;

import com.huoqiu.framework.rest.Response;



public class HisTaskDetailResponse extends Response {

	/*任务明细信息*/ 
	private ToDaysTaskDetailsResponse taskdetail; 
	/*房屋历史基本信息*/ 
	private boolean isSuccess = false; 

	private String taskImgStr; // 任务图片统计字段 
	private String houseTypeStr; // 房型变化字段 
	private String rentPriceStr; // 出租金额变化字段 
	private String sellPriceStr; // 出售金额变化字段 
	private String spaceAreaStr; // 面积变化字段 
	private String houseSupport;/*配套信息*/
	public ToDaysTaskDetailsResponse getTaskdetail() {
		return taskdetail;
	}
	public void setTaskdetail(ToDaysTaskDetailsResponse taskdetail) {
		this.taskdetail = taskdetail;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getTaskImgStr() {
		return taskImgStr;
	}
	public void setTaskImgStr(String taskImgStr) {
		this.taskImgStr = taskImgStr;
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
	public String getHouseSupport() {
		return houseSupport;
	}
	public void setHouseSupport(String houseSupport) {
		this.houseSupport = houseSupport;
	}


	

}