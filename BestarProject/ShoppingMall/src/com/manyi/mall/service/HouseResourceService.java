package com.manyi.mall.service;

import com.huoqiu.framework.rest.AppAuthInterceptor;
import com.huoqiu.framework.rest.Loading;
import com.huoqiu.framework.rest.RequestMapping;
import com.huoqiu.framework.rest.RestService;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.user.HouseRequest;
import com.manyi.mall.cachebean.user.HouseResponse;

@RequestMapping(value = "/houseresource", interceptors = AppAuthInterceptor.class)
public interface HouseResourceService extends RestService {

	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.result_loadding)
	@RequestMapping("/findRentByPage.rest")
	public HouseResponse findRentByPage(HouseRequest req);

	@RequestMapping("/findRentByPage.rest")
	public HouseResponse findRentByPage2(HouseRequest req);

	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.result_loadding)
	@RequestMapping("/findSellByPage.rest")
	public HouseResponse findSellByPage(HouseRequest req);

	@RequestMapping("/findSellByPage.rest")
	public HouseResponse findSellByPage2(HouseRequest req);
}
