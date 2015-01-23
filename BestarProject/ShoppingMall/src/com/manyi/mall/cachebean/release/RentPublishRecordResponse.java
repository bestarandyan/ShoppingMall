package com.manyi.mall.cachebean.release;

import java.util.ArrayList;
import java.util.List;

import com.huoqiu.framework.rest.Response;

public class RentPublishRecordResponse extends Response {

	private List<RentReleaseResponse> result = new ArrayList<RentReleaseResponse>();

	public List<RentReleaseResponse> getResult() {
		return result;
	}

	public void setResult(List<RentReleaseResponse> result) {
		this.result = result;
	}
	



}
