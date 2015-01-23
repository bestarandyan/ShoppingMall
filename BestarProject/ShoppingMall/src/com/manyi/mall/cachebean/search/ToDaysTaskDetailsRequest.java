package com.manyi.mall.cachebean.search;

import org.androidannotations.annotations.EBean;

@EBean
public class ToDaysTaskDetailsRequest {
	  private int taskId;

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
}
