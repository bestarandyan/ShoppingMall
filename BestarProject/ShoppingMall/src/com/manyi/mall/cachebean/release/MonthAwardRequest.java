package com.manyi.mall.cachebean.release;

public class MonthAwardRequest {
    private int userId;
    private int type; //1 出租 2 出售 （目前默认1）

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
