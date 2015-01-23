package com.manyi.mall.cachebean.search;

import com.huoqiu.framework.rest.Response;


public class UserTaskCountResponse extends Response {

    private int count;
    private int sellPrice; //出售
    private int rentPrice; //出租
    private int updateDisc; //改盘
    private int invitation;//邀请朋友注册
    private int lookHouse;// 看房随手拍
    private int releaseCount;//出租发布审核的条数
    private int auditSellCount;//出售发布审核的条数 1.8 新增
    private int showMonthAward;//是否显示【出租】月度分级奖励1显示，0不显示 1.8 新增
    private int showMonthAwardSell;//是否显示【出售】月度分级奖励1显示，0不显示 【1.9新增】

    public int getShowMonthAward() {
        return showMonthAward;
    }

    public void setShowMonthAward(int showMonthAward) {
        this.showMonthAward = showMonthAward;
    }

    public int getShowMonthAwardSell() {
        return showMonthAwardSell;
    }

    public void setShowMonthAwardSell(int showMonthAwardSell) {
        this.showMonthAwardSell = showMonthAwardSell;
    }

    public int getAuditSellCount() {
        return auditSellCount;
    }

    public void setAuditSellCount(int auditSellCount) {
        this.auditSellCount = auditSellCount;
    }

    public int getReleaseCount() {
        return releaseCount;
    }

    public void setReleaseCount(int releaseCount) {
        this.releaseCount = releaseCount;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(int rentPrice) {
        this.rentPrice = rentPrice;
    }

    public int getUpdateDisc() {
        return updateDisc;
    }

    public void setUpdateDisc(int updateDisc) {
        this.updateDisc = updateDisc;
    }

    public int getInvitation() {
        return invitation;
    }

    public void setInvitation(int invitation) {
        this.invitation = invitation;
    }

    public int getLookHouse() {
        return lookHouse;
    }

    public void setLookHouse(int lookHouse) {
        this.lookHouse = lookHouse;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
