package com.manyi.mall.cachebean.user;

import com.huoqiu.framework.rest.Response;


public class TaskWeekResponse extends Response {
	private long employeeId;
	/* 本周任务数 */
	private int weekTaskCount;
	/* 已完成任务数 */
	private int finishCount;
	/* 失败任务数 */
	private int failCount;
	/* 未完成任务数 */
	private int unfinishCount;

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public int getWeekTaskCount() {
		return weekTaskCount;
	}

	public void setWeekTaskCount(int weekTaskCount) {
		this.weekTaskCount = weekTaskCount;
	}

	public int getFinishCount() {
		return finishCount;
	}

	public void setFinishCount(int finishCount) {
		this.finishCount = finishCount;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

	public int getUnfinishCount() {
		return unfinishCount;
	}

	public void setUnfinishCount(int unfinishCount) {
		this.unfinishCount = unfinishCount;
	}

}
