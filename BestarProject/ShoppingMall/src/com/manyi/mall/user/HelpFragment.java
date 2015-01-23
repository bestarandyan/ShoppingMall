package com.manyi.mall.user;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.webkit.WebView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.rest.Configuration;
import com.manyi.mall.R;

@EFragment(R.layout.fragment_help)
public class HelpFragment extends SuperFragment<Object> {

	@ViewById(R.id.help)
	WebView serverAgreement;

	private String url = Configuration.DEFAULT.getDomain() + "/imageExplain.html";

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onResume() {
		super.onResume();
		serverAgreement.getSettings().setJavaScriptEnabled(true);
		serverAgreement.loadUrl(url);
	}

	@Click(R.id.help_back)
	void server_back() {
		remove();
	}
}
