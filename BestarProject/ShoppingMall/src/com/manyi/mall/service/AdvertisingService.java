package com.manyi.mall.service;

 

import com.huoqiu.framework.rest.AppAuthInterceptor;
import com.huoqiu.framework.rest.Loading;
import com.huoqiu.framework.rest.RequestMapping;
import com.huoqiu.framework.rest.RestService;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.AdvertDetailRequest;
import com.manyi.mall.cachebean.search.AdvertDetailResponse;
import com.manyi.mall.cachebean.search.AdvertRequest;
import com.manyi.mall.cachebean.search.AdvertResponse;

@RequestMapping(value = "/advertising", interceptors = AppAuthInterceptor.class)
public interface AdvertisingService extends RestService {

//	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.release_fragment)
	@RequestMapping("/getAdTitleList.rest" )
	public AdvertResponse getAdvertList(AdvertRequest req);
	
	@Loading(value = R.id.loading, layout = R.layout.loading,container = R.id.advertDetailLayout)
	@RequestMapping("/getAdById.rest")
	public AdvertDetailResponse getAdvertDetail(AdvertDetailRequest req);

}
