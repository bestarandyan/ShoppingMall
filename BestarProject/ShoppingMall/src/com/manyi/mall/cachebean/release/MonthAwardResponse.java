package com.manyi.mall.cachebean.release;

import com.huoqiu.framework.rest.Response;

public class MonthAwardResponse extends Response {
    // 当前等级
    private String curLevelName; // 当前等级名称
    private Integer curAward; // 当前等级每套获额外奖励
    private Integer rantHouseNum; // 发布出租成功 套数
    private Integer awardTotal; // 额外奖励共计 金额
    private Integer curMonthConfig; //本月是否已经配置，1配置，0没配置
    // 下一等级
    private Integer haiChaiTaoNum; // 还差 套数
    private String nextLevelName; // 下一等级名称
    private Integer nextAward; // 下一等级每套获额外奖励

    // 上月
    private String lastMonthLevelName; // 等级名称
    private Integer lastMonthAward; // 等级每套获额外奖励
    private Integer lastMonthRantHouseNum; // 发布出租成功 套数
    private Integer lastMonthAwardTotal; // 额外奖励共计 金额

    private Integer lastMonthConfig; //上月是否已经配置，1配置，0没配置

    public String getCurLevelName() {
        return curLevelName;
    }

    public void setCurLevelName(String curLevelName) {
        this.curLevelName = curLevelName;
    }

    public Integer getCurAward() {
        return curAward;
    }

    public void setCurAward(Integer curAward) {
        this.curAward = curAward;
    }

    public Integer getRantHouseNum() {

        return rantHouseNum;
    }

    public void setRantHouseNum(Integer rantHouseNum) {
        this.rantHouseNum = rantHouseNum;
    }

    public Integer getAwardTotal() {
        return awardTotal;
    }

    public void setAwardTotal(Integer awardTotal) {
        this.awardTotal = awardTotal;
    }

    public Integer getHaiChaiTaoNum() {
        return haiChaiTaoNum;
    }

    public void setHaiChaiTaoNum(Integer haiChaiTaoNum) {
        this.haiChaiTaoNum = haiChaiTaoNum;
    }

    public String getNextLevelName() {
        return nextLevelName;
    }

    public void setNextLevelName(String nextLevelName) {
        this.nextLevelName = nextLevelName;
    }

    public Integer getNextAward() {
        return nextAward;
    }

    public void setNextAward(Integer nextAward) {
        this.nextAward = nextAward;
    }

    public String getLastMonthLevelName() {
        return lastMonthLevelName;
    }

    public void setLastMonthLevelName(String lastMonthLevelName) {
        this.lastMonthLevelName = lastMonthLevelName;
    }

    public Integer getLastMonthAward() {
        return lastMonthAward;
    }

    public void setLastMonthAward(Integer lastMonthAward) {
        this.lastMonthAward = lastMonthAward;
    }

    public Integer getLastMonthRantHouseNum() {
        return lastMonthRantHouseNum;
    }

    public void setLastMonthRantHouseNum(Integer lastMonthRantHouseNum) {
        this.lastMonthRantHouseNum = lastMonthRantHouseNum;
    }

    public Integer getLastMonthAwardTotal() {
        return lastMonthAwardTotal;
    }

    public void setLastMonthAwardTotal(Integer lastMonthAwardTotal) {
        this.lastMonthAwardTotal = lastMonthAwardTotal;
    }

    public Integer getCurMonthConfig() {
        return curMonthConfig;
    }

    public void setCurMonthConfig(Integer curMonthConfig) {
        this.curMonthConfig = curMonthConfig;
    }

    public Integer getLastMonthConfig() {
        return lastMonthConfig;
    }

    public void setLastMonthConfig(Integer lastMonthConfig) {
        this.lastMonthConfig = lastMonthConfig;
    }

}
