package com.manyi.mall.cachebean.mine;

public class MineBonusCollectRequest {
	private int uid;
	private int start;// 分页开始的下标 =【=(页数-1)*每页最大条数】：例如第一页(1-1)*12， 第二页(2-1)*12，第三页(3-1)*12
	private int maxResult;// 每页最大条数，例如：12
	private Long markTime;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}

	public Long getMarkTime() {
		return markTime;
	}

	public void setMarkTime(Long markTime) {
		this.markTime = markTime;
	}

}
