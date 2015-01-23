package com.manyi.mall.mine;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.widget.EditText;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.mine.ChangePaypalRequest;
import com.manyi.mall.common.Constants;
import com.manyi.mall.service.UcService;

@EFragment(R.layout.fragment_modificationa_alipay)
public class ModifyAlipayFragment extends SuperFragment<Object> {
	@ViewById(R.id.email_phone)
	EditText mEmailPhone;
	@ViewById(R.id.password_modificationa)
	EditText mPasswordModificationa;

	private UcService mService;

	@Click(R.id.modification_back)
	void modificationBack() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		ManyiUtils.closeKeyBoard(getActivity(), mPasswordModificationa);
		remove();
	}

	@AfterViews
	void init() {
		mEmailPhone.requestFocus();
		ManyiUtils.showKeyBoard(getActivity(), mEmailPhone);

	}

	@Click(R.id.modify)
	void modify() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		if (mEmailPhone.getText().toString().length() != 0 && mPasswordModificationa.getText().toString().length() != 0) {
			quest();
		} else if (mEmailPhone.getText().toString().length() == 0) {
			DialogBuilder.showSimpleDialog("请输入支付宝号码", getBackOpActivity());

		} else if (mPasswordModificationa.getText().toString().length() == 0) {
			DialogBuilder.showSimpleDialog("请输入登录密码", getBackOpActivity());

		}
	}

	@Background
	void quest() {

		try {
			int uid = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
			ChangePaypalRequest alipayRequst = new ChangePaypalRequest();
			alipayRequst.setPaypalAccount(mEmailPhone.getText().toString());
			alipayRequst.setPassword(mPasswordModificationa.getText().toString());
			alipayRequst.setUid(uid);
			mService.changePaypal(alipayRequst);
			sucess();
		} catch (RestException e) {
			showToast(e.getMessage());

		}

	}

	@UiThread
	void sucess() {
		notifySelected(null);
		ManyiUtils.closeKeyBoard(getActivity(), mPasswordModificationa);
		// remove();

	}

	@UiThread
	void showToast(String e) {
		DialogBuilder.showSimpleDialog(e, getBackOpActivity());
	}
}
