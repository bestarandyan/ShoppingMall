package com.manyi.mall.cachebean.mine;

public class BindPaypalRequest {
	private String paypalAccount; // 支付宝账号
	private int uid;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getPaypalAccount() {
		return paypalAccount;
	}

	public void setPaypalAccount(String paypalAccount) {
		this.paypalAccount = paypalAccount;
	}

}
