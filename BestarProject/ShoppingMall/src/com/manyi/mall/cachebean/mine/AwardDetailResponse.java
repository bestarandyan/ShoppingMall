package com.manyi.mall.cachebean.mine;

import java.util.ArrayList;
import java.util.List;

import com.huoqiu.framework.rest.Response;

public class AwardDetailResponse extends Response {
	private int errorCode = 0;
	private String message;
	private List<AwardResponse> awardList = new ArrayList<AwardResponse>();

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<AwardResponse> getAwardList() {
		return awardList;
	}

	public void setAwardList(List<AwardResponse> awardList) {
		this.awardList = awardList;
	}

	public static class AwardResponse {

		private String awardNum; // 奖金数额
		private String payTime; // 打款日期
		private String payType; // 发布奖金类型 文本
		private int payStateId; // 打款状态类型 //0,未付款（未打款）;1,付款成功,2,付款失败，3,付款中（已打款）
		private String payState; // 打款状态
		private Long markTime;
		private int hot;// 热点： 1 是，0 不是

		public String getAwardNum() {
			return awardNum;
		}

		public void setAwardNum(String awardNum) {
			this.awardNum = awardNum;
		}

		public String getPayTime() {
			return payTime;
		}

		public void setPayTime(String payTime) {
			this.payTime = payTime;
		}

		public String getPayType() {
			return payType;
		}

		public void setPayType(String payType) {
			this.payType = payType;
		}

		public int getPayStateId() {
			return payStateId;
		}

		public void setPayStateId(int payStateId) {
			this.payStateId = payStateId;
		}

		public String getPayState() {
			return payState;
		}

		public void setPayState(String payState) {
			this.payState = payState;
		}

		public Long getMarkTime() {
			return markTime;
		}

		public void setMarkTime(Long markTime) {
			this.markTime = markTime;
		}

		public int getHot() {
			return hot;
		}

		public void setHot(int hot) {
			this.hot = hot;
		}

	}
}
