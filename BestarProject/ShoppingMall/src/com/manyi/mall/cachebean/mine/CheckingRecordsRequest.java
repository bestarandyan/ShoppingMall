/**
 * 
 */
package com.manyi.mall.cachebean.mine;

/**
 * @author bestar
 * 
 */
public class CheckingRecordsRequest {
	private int uid;

	private int start;// 分页开始的下标

	private int maxResult;// 每页最大条数，例如：12

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

}
