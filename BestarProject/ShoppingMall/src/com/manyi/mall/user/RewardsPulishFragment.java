package com.manyi.mall.user;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.webkit.WebView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.rest.Configuration;
import com.manyi.mall.R;

@SuppressLint("SetJavaScriptEnabled")
@EFragment(R.layout.fragment_rewards_punishments)
public class RewardsPulishFragment extends SuperFragment<Object> {
	@ViewById(R.id.rewards_pulish)
	WebView rewardspulish;
	private String url = Configuration.DEFAULT.getDomain() + "/IncentiveAgreement.html";

	@Override
	public void onResume() {
		super.onResume();
		rewardspulish.getSettings().setJavaScriptEnabled(true);
		rewardspulish.loadUrl(url);
	}

	@Click(R.id.rewards_pulish_back)
	void rewardsPulishBack() {
		remove();
	}
}
