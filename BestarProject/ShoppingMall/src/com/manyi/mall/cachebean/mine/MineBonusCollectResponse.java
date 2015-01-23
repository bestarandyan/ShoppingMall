package com.manyi.mall.cachebean.mine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.huoqiu.framework.rest.Response;

public class MineBonusCollectResponse extends Response {
	private int errorCode = 0;
	private String message;
	private List<MergerAwardResponse> award4MeSumList = new ArrayList<MergerAwardResponse>();

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

	public List<MergerAwardResponse> getAward4MeSumList() {
		return award4MeSumList;
	}

	public void setAward4MeSumList(List<MergerAwardResponse> award4MeSumList) {
		this.award4MeSumList = award4MeSumList;
	}

	public static class MergerAwardResponse implements Serializable {
		/**
		 * 
		 * 
		 */
		private String awardNum; // 金数额
		private int payCount; // 笔数
		private String payTime; // 打款日期
		private int payStateId; // 打款状态类型 //0,未付款（未打款）;1,付款成功,2,付款失败，3,付款中（已打款）
		private String payState; // 打款状态
		private Long markTime;
		private static final long serialVersionUID = 1L;

		public String getAwardNum() {
			return awardNum;
		}

		public void setAwardNum(String awardNum) {
			this.awardNum = awardNum;
		}

		public int getPayCount() {
			return payCount;
		}

		public void setPayCount(int payCount) {
			this.payCount = payCount;
		}

		public String getPayTime() {
			return payTime;
		}

		public void setPayTime(String payTime) {
			this.payTime = payTime;
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

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

	}

}
