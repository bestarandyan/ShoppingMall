package com.manyi.mall.cachebean.release;

import com.huoqiu.framework.rest.Response;


public class IntentDetailResponse extends Response{

private int count ;// 剩余查看房源的次数
private int monthCount;//剩余当月查看房源的次数

public int getMonthCount() {
	return monthCount;
}

public void setMonthCount(int monthCount) {
	this.monthCount = monthCount;
}

public int getCount() {
	return count;
}

public void setCount(int count) {
	this.count = count;
}
	
}
