package com.manyi.mall;

import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.rest.RestProxyActivity;

public abstract class BaseActivity extends RestProxyActivity {

	@Override
	protected void onPause() {
		super.onPause();
		ManyiAnalysis.onPageEnd(this.getClass().getSimpleName());// 保证onPageEnd在onPause之前调用,因为onPause中会保存信息
		ManyiAnalysis.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ManyiAnalysis.onPageStart(this.getClass().getSimpleName());
		ManyiAnalysis.onResume(this);
	}

}
