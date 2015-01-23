package com.manyi.mall.cachebean.search;

import com.huoqiu.framework.rest.Response;


public class UserTaskDetailResponse extends Response {
	private UserTaskDetail userTaskDetail;
	


	public UserTaskDetail getUserTaskDetail() {
		return userTaskDetail;
	}


	public void setUserTaskDetail(UserTaskDetail userTaskDetail) {
		this.userTaskDetail = userTaskDetail;
	}
}
