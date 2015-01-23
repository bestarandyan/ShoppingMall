package com.manyi.mall.cachebean.mine;

public class AwardPageRequest {
	private int userId;
	private int start;
	private int end;
	private Long markTime;

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
