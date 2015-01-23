package com.manyi.mall;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;

import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.backstack.BackOpFragmentActivity;
import com.huoqiu.framework.backstack.Op;
import com.huoqiu.framework.rest.Configuration;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.StringUtil;
import com.manyi.mall.cachebean.search.NotificationBean;
import com.manyi.mall.common.push.Constants;
import com.manyi.mall.common.push.Notifier;
import com.manyi.mall.common.util.DBUtil;

@WindowFeature({ Window.FEATURE_NO_TITLE })
@EActivity(R.layout.activity_start)
public class StartActivity extends BaseActivity {
	@ViewById(R.id.start_activity)
	public FrameLayout mLayout;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 通知
		if (getIntent() != null && getIntent().getStringExtra(Constants.TAG) != null) {
			Intent intent = getIntent();
			NotificationBean bean = NotificationBean.getInstance();
			bean.msgtype = intent.getStringExtra(Constants.NOTIFICATION_MSGTYPE);
			bean.message = intent.getStringExtra(Constants.NOTIFICATION_MESSAGE);
			Notifier.sendReadedInfo(intent.getStringExtra(Constants.PACKET_ID), intent.getStringExtra(Constants.NOTIFICATION_FROM));
		}
		if (savedInstanceState == null) {
			initIp();
//			initBaseException();
			initFragment();

			// 初始化友盟
			ManyiAnalysis.init(this);
		}
	}

	/**
	 * 获取测试Ip
	 */
	private void initIp() {
		if (Configuration.DEFAULT == Configuration.TEST) {
			String ip = DBUtil.getInstance().getUserSetting(this, "ip");
			String port = DBUtil.getInstance().getUserSetting(this, "port");
			if (!StringUtil.isEmptyOrNull(ip) && !StringUtil.isEmptyOrNull(port)) {
				Configuration.DEFAULT.hostname = ip;
				Configuration.DEFAULT.port = StringUtil.toInt(port);
			}
		} else {
			Configuration.DEFAULT.hostname = "fyb365.com";
			Configuration.DEFAULT.port = 80;

		}
		Configuration.DEFAULT.protocol = "http";
		Configuration.DEFAULT.path = "/rest";
	}

	/**
	 * 初始化异常处理
	 */
//	private void initBaseException(){
//        Intent targetIntent = new Intent();
//        targetIntent.setClass(AppConfig.application ,GeneratedClassUtils.get(StartActivity.class));
//        PendingIntent intent = PendingIntent.getActivity(this.getBaseContext(), 0x200, targetIntent, getIntent().getFlags());
//        BaseUncaughtExceptionHandler baseException = BaseUncaughtExceptionHandler.getInstance();
//        baseException.setmIntent(intent);
//    }

	private void initFragment() {
		StartFragment startFragment = GeneratedClassUtils.getInstance(StartFragment.class);
		Bundle extra = getIntent().getBundleExtra("bundle");
		startFragment.tag = StartFragment.class.getName();
		Bundle bundle = new Bundle();
		if (extra == null || extra.getBoolean("isEnterFromLauncher", true)) {
			// startFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
			// R.anim.anim_fragment_close_out);

			bundle.putBoolean("NotCheckNewVersion", false);
		} else {
			bundle.putBoolean("NotCheckNewVersion", true);
		}
		startFragment.setManager(getSupportFragmentManager());
		startFragment.setArguments(bundle);
		startFragment.setBackOp(new Op() {

			@Override
			public void setTag(String tag) {
			}

			@Override
			public void perform(BackOpFragmentActivity activity) {
				finish();
			}

			@Override
			public String getTag() {
				return StartFragment.class.getName();
			}
		});
		startFragment.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cancelNotice();
	}

	/** 取消通知栏 */
	public void cancelNotice() {
		NotificationManager notificationMgr = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		try {
			notificationMgr.cancelAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
