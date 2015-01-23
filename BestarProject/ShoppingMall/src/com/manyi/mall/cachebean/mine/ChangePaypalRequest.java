package com.manyi.mall.cachebean.mine;

public class ChangePaypalRequest {
	private int uid;

	private String paypalAccount; // 支付宝账号
	private String password; // 个人账号密码

	public String getPaypalAccount() {
		return paypalAccount;
	}

	public void setPaypalAccount(String paypalAccount) {
		this.paypalAccount = paypalAccount;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

}
