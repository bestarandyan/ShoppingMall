package com.manyi.mall.cachebean.user;

import com.huoqiu.framework.rest.Response;


public class LoginResponse extends Response {
	private int uid;
	private String userName;// 用户名
	private int state;// 账户状态 2 审核中、 1 已审核、3 审核失败,4账户冻结
	private int sumCount; // 每日限制查看的额的总数量
	private int PublishCount;// 今天已经使用的数量
	private String alipayAccount;
	private int cityId; // 城市id
	private String bankCode; //银行卡
	private String realName; //身份证姓名
	private String cityName; //城市名
	
	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getSumCount() {
		return sumCount;
	}

	public void setSumCount(int sumCount) {
		this.sumCount = sumCount;
	}

	public int getPublishCount() {
		return PublishCount;
	}

	public void setPublishCount(int publishCount) {
		PublishCount = publishCount;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
}
