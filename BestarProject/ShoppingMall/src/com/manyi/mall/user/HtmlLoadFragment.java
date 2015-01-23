package com.manyi.mall.user;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.webkit.WebView;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.rest.Configuration;
import com.manyi.mall.R;

@EFragment(R.layout.fragment_html_load)
public class HtmlLoadFragment extends SuperFragment<Object> {

	@ViewById(R.id.server_agreement)
	WebView serverAgreement;

	@FragmentArg
	String mHtmLTitle, mHtmlUrl;

	@ViewById(R.id.server_back)
	TextView mTiltle;
	

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onResume() {
		super.onResume();
		if (mHtmLTitle != null) {
			mTiltle.setText(mHtmLTitle);
		} else {
			mTiltle.setText("");
		}
		String url = Configuration.DEFAULT.getDomain() + "/"+mHtmlUrl; //agreement.html
		serverAgreement.getSettings().setJavaScriptEnabled(true);
		serverAgreement.loadUrl(url);
	}

	@Click(R.id.server_back)
	void server_back() {
		remove();
	}
}
