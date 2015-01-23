package com.manyi.mall.cachebean.search;

import org.androidannotations.annotations.EBean;

@EBean
public class UserTaskDetailRequest {
	private int userTaskId;

	public int getUserTaskId() {
		return userTaskId;
	}

	public void setUserTaskId(int userTaskId) {
		this.userTaskId = userTaskId;
	}
	
	
}
