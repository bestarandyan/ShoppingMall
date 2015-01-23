package com.manyi.mall.cachebean.release;

import org.androidannotations.annotations.EBean;

@EBean
public class SellPublishRecordRequest {

	private int uid;
	private int start;
	private int end;
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
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	
	

}
