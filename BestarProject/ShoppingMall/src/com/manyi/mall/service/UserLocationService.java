/**
 * 
 */
package com.manyi.mall.service;

import com.huoqiu.framework.rest.AppAuthInterceptor;
import com.huoqiu.framework.rest.RequestMapping;
import com.huoqiu.framework.rest.RestService;
import com.manyi.mall.cachebean.user.UserLocationRequest;
import com.manyi.mall.cachebean.user.UserLocationResponse;

/**
 * @author bestar
 *
 */
@RequestMapping(value = "/userLocation", interceptors = AppAuthInterceptor.class)
public interface UserLocationService extends RestService{
	@RequestMapping("/addUserLocation.rest")
	public abstract UserLocationResponse sendUserLocation(UserLocationRequest request);
}
