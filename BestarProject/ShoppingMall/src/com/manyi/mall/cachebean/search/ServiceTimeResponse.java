/**
 * 
 */
package com.manyi.mall.cachebean.search;

import com.huoqiu.framework.rest.Response;
/**
 * 系统时间服务
 * @author dench
 *
 */
public class ServiceTimeResponse extends Response{
	private Long sysDate;

	public Long getSysDate() {
		return sysDate;
	}

	public void setSysDate(Long sysDate) {
		this.sysDate = sysDate;
	}
}
