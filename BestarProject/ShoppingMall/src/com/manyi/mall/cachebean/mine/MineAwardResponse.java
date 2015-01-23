package com.manyi.mall.cachebean.mine;

import java.util.ArrayList;
import java.util.List;

import com.huoqiu.framework.rest.Response;

 

public class MineAwardResponse extends Response {

	private List<AwardResponse> awardList = new ArrayList<AwardResponse>();

	public List<AwardResponse> getAwardList() {
		return awardList;
	}

	public void setAwardList(List<AwardResponse> awardList) {
		this.awardList = awardList;
	}

	public static class AwardResponse extends Response {
		private String awardNum; // 奖金数额
		private String awardDate; // 发奖日期
		private long markTime;
		private String payType; // 发布奖金类型

		public String getPayType() {
			return payType;
		}

		public void setPayType(String payType) {
			this.payType = payType;
		}

		public long getMarkTime() {
			return markTime;
		}

		public void setMarkTime(long markTime) {
			this.markTime = markTime;
		}

		public String getAwardNum() {
			return awardNum;
		}

		public void setAwardNum(String awardNum) {
			this.awardNum = awardNum;
		}

		public String getAwardDate() {
			return awardDate;
		}

		public void setAwardDate(String awardDate) {
			this.awardDate = awardDate;
		}

	}
}
