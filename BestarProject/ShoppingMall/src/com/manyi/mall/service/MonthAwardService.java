package com.manyi.mall.service;

import com.huoqiu.framework.rest.AppAuthInterceptor;
import com.huoqiu.framework.rest.Loading;
import com.huoqiu.framework.rest.RequestMapping;
import com.huoqiu.framework.rest.RestService;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.release.MonthAwardRequest;
import com.manyi.mall.cachebean.release.MonthAwardResponse;

@RequestMapping(value = "/monthaward", interceptors = AppAuthInterceptor.class)
public interface MonthAwardService extends RestService{
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.classification_reward)
	@RequestMapping("/getMonthAward.rest")
	public MonthAwardResponse getMonthAward(MonthAwardRequest request);
}
