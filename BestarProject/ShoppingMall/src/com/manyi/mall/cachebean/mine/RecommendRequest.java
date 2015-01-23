package com.manyi.mall.cachebean.mine;

public class RecommendRequest {
	private int uid;
	private String userTel; // 用户电话
	private String recommendTel; // 被推荐人电话 ，如果有多个，手机号码以逗号分隔

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUserTel() {
		return userTel;
	}

	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}

	public String getRecommendTel() {
		return recommendTel;
	}

	public void setRecommendTel(String recommendTel) {
		this.recommendTel = recommendTel;
	}

}
