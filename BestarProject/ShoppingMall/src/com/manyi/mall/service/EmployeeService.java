package com.manyi.mall.service;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.api.rest.MediaType;

import com.huoqiu.framework.rest.AppAuthInterceptor;
import com.huoqiu.framework.rest.Loading;
import com.huoqiu.framework.rest.RequestMapping;
import com.huoqiu.framework.rest.RestService;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.mine.MineAwardResponse;
import com.manyi.mall.cachebean.mine.MineRecordsRequest;
 
@RequestMapping(value = "/employee", interceptors = AppAuthInterceptor.class)
public interface EmployeeService extends RestService{
	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.pull_refresh_list_container)
	@RequestMapping("/employee.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public MineAwardResponse mineAward(MineRecordsRequest req);

}
