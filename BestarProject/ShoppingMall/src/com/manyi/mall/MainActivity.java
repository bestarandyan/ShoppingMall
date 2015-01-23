package com.manyi.mall;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;

import android.os.Bundle;
import android.view.Window;

import com.huoqiu.framework.app.AppConfig;
import com.huoqiu.framework.backstack.AbsOp;
import com.huoqiu.framework.backstack.BackOpFragmentActivity;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.StringUtil;
import com.manyi.mall.cachebean.search.NotificationBean;
import com.manyi.mall.common.Constants;
import com.manyi.mall.common.push.PushServiceUtil;

/**
 * 首页Activity
 */
@WindowFeature({ Window.FEATURE_NO_TITLE })
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            AppConfig.UID = String.format("%d", getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0));
			initFragment();
			// 启动推送服务
			Thread serviceThread = new Thread(new Runnable() {
				@Override
				public void run() {
					PushServiceUtil.startNotificationService(MainActivity.this);
				}
			});
			serviceThread.start();
		}

	}

    @Override
	protected void onResume() {
		super.onResume();
		// 显示推送消息
		showNotificationMessage();
	}

	/**
	 * 显示推送消息
	 */
	private void showNotificationMessage() {
		NotificationBean bean = NotificationBean.getInstance();
		if (!StringUtil.isEmptyOrNull(bean.msgtype)) {
			if ("1".equals(bean.msgtype) && !StringUtil.isEmptyOrNull(bean.message)) {
				DialogBuilder.showSimpleDialog(bean.message, this);
				bean.message = "";
				bean.msgtype = "";
			}

		}
	}

	/**
	 * 加载首页
	 */
	private void initFragment() {
		MainFragment fragment = GeneratedClassUtils.getInstance(MainFragment.class);
		fragment.tag = MainFragment.class.getName();
		fragment.setManager(getSupportFragmentManager());
		fragment.setContainerId(R.id.main_container);
		fragment.setBackOp(new AbsOp(MainFragment.class.getName()) {
			@Override
			public void perform(BackOpFragmentActivity arg0) {
				finish();
			}
		});
		fragment.show();
	}

}
