package com.manyi.mall.common.util;

import com.manyi.mall.cachebean.search.UserTaskCountResponse;

public class TaskCountResponseUtil {
	
	private UserTaskCountResponse response;

	private static TaskCountResponseUtil instance;

	public static TaskCountResponseUtil getInstance() {
		if (instance == null) {
			instance = new TaskCountResponseUtil();
		}
		return instance;
	}

	private TaskCountResponseUtil() {

	}

	public UserTaskCountResponse getResponse() {
		return response;
	}

	public void setResponse(UserTaskCountResponse response) {
		this.response = response;
	}

}
