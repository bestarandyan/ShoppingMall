package com.manyi.mall.service;

import com.huoqiu.framework.rest.AppAuthInterceptor;
import com.huoqiu.framework.rest.Loading;
import com.huoqiu.framework.rest.RequestMapping;
import com.huoqiu.framework.rest.Response;
import com.huoqiu.framework.rest.RestService;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.EstateFeedbackRequest;
import com.manyi.mall.cachebean.search.SubEstateStateRequest;
import com.manyi.mall.cachebean.search.SubEstateStateResponse;

@RequestMapping(value = "/estate", interceptors = AppAuthInterceptor.class)
public interface EstateService extends RestService {

	@Loading(value = R.id.loading, layout = R.layout.loading,container = R.id.release_sell)
	@RequestMapping("/getSubEstateIsBlock.rest")
	public SubEstateStateResponse checkSellSubEstate(SubEstateStateRequest req);
	
	@Loading(value = R.id.loading, layout = R.layout.loading,container = R.id.release_rent)
	@RequestMapping("/getSubEstateIsBlock.rest")
	public SubEstateStateResponse checkRentSubEstate(SubEstateStateRequest req);

	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.estate_feedback_relative_layout)
	@RequestMapping("/addEstateFeedBack.rest")
	public Response estateFeedback(EstateFeedbackRequest req);

}
