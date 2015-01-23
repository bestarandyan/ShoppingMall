/**
 * 
 */
package com.manyi.mall.cachebean.search;

import com.huoqiu.framework.rest.Response;


/**
 * @author bestar
 *
 */
public class GetTaskResponse extends Response{
	private int houseId;
	private int taskRepulsionCode ;  // (0可以领取；-1 不存在该房源；-2该房源正在审核中，无法进行拍照任务；-3该房源正在进行BD看房任务中，无法进行拍照任务; -4 该房源正在进行中介看房任务中，无法进行拍照任务)
    private String message; //描述信息
    
    
    
	public int getHouseId() {
		return houseId;
	}
	public void setHouseId(int houseId) {
		this.houseId = houseId;
	}
	
	
	public int getTaskRepulsionCode() {
		return taskRepulsionCode;
	}
	public void setTaskRepulsionCode(int taskRepulsionCode) {
		this.taskRepulsionCode = taskRepulsionCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
    
    
    
}
