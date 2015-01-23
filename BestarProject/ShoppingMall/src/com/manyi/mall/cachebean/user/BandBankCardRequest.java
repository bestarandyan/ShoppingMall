package com.manyi.mall.cachebean.user;

public class BandBankCardRequest {
    private Integer userId;  //用户Id
    private String bank; //银行
    private String subBank; //支行
    private String bankCode; //银行帐号
    private Integer cityId; //开户城市
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getSubBank() {
		return subBank;
	}
	public void setSubBank(String subBank) {
		this.subBank = subBank;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
    
}
