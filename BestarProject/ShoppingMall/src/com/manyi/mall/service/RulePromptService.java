package com.manyi.mall.service;

import com.huoqiu.framework.rest.AppAuthInterceptor;
import com.huoqiu.framework.rest.Loading;
import com.huoqiu.framework.rest.RequestMapping;
import com.huoqiu.framework.rest.RestService;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.RuleContentRequest;
import com.manyi.mall.cachebean.search.RuleContentResponse;

@RequestMapping(value = "/rulePrompt", interceptors = AppAuthInterceptor.class)
public interface RulePromptService extends RestService {

	@Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.rule_content_layout)
	@RequestMapping("/getRulePrompt.rest")
	public RuleContentResponse getRuleContent(RuleContentRequest req);

}
