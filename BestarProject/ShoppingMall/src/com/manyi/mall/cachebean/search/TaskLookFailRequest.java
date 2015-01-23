package com.manyi.mall.cachebean.search;

import org.androidannotations.annotations.EBean;

@EBean
public class TaskLookFailRequest {
	
	private int taskId; 
    private String remark;
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	} 
}
