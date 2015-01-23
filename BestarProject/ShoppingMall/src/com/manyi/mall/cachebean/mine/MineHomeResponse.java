package com.manyi.mall.cachebean.mine;

import com.huoqiu.framework.rest.Response;

public class MineHomeResponse extends Response {
    private double blance;// 账户余额
    private String spreadId;// 注册成功自动生成推广码
    private int spreanCount;// 已推广人数
    private String awardId; // 我的奖金
    private int awardCount; // 我的奖金记录数
    private String paypalAccount; // 我的支付宝账号(如果支付宝账号为空未绑定否则为绑定)
    private String bankCode; // 银行帐号
    private String bank; // 银行
    private String subBank; // 支行银行
    private int createCount;// 发布记录
    private int allowUpdateMobile;//是否可以修改手机号码  0：可以修改 1：不能修改
    private String msg;//不能修改手机号码的提示信息

    public int getAllowUpdateMobile() {
        return allowUpdateMobile;
    }

    public void setAllowUpdateMobile(int allowUpdateMobile) {
        this.allowUpdateMobile = allowUpdateMobile;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public double getBlance() {
        return blance;
    }

    public void setBlance(double blance) {

        this.blance = blance;
    }

    public String getSpreadId() {
        return spreadId;
    }

    public void setSpreadId(String spreadId) {
        this.spreadId = spreadId;
    }

    public int getSpreanCount() {
        return spreanCount;
    }

    public void setSpreanCount(int spreanCount) {
        this.spreanCount = spreanCount;
    }

    public String getAwardId() {
        return awardId;
    }

    public void setAwardId(String awardId) {
        this.awardId = awardId;
    }

    public int getAwardCount() {
        return awardCount;
    }

    public void setAwardCount(int awardCount) {
        this.awardCount = awardCount;
    }

    public String getPaypalAccount() {
        return paypalAccount;
    }

    public void setPaypalAccount(String paypalAccount) {
        this.paypalAccount = paypalAccount;
    }

    public int getCreateCount() {
        return createCount;
    }

    public void setCreateCount(int createCount) {
        this.createCount = createCount;
    }

}
