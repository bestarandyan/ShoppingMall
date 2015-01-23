/**
 * 
 */
package com.manyi.mall.cachebean.user;


/**
 * @author zxc
 */
public class FeedbackRequest {

	private int uid;
	private String context;//意见反馈内容
	
	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
}
