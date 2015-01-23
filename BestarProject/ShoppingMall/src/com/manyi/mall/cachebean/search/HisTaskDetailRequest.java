package com.manyi.mall.cachebean.search;

import org.androidannotations.annotations.EBean;

@EBean
public class HisTaskDetailRequest {
	private int taskId;
	private int taskStatus;

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(int taskStatus) {
		this.taskStatus = taskStatus;
	}

}