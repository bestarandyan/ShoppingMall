package com.manyi.mall.cachebean.mine;

public class RecordsMoreRequest {
	int userId;// 用户ID
	int start;// 数据起始下标
	int end;// 数据结束下标
	Long markTime;// 时间戳

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public Long getMarkTime() {
		return markTime;
	}

	public void setMarkTime(Long markTime) {
		this.markTime = markTime;
	}

}
