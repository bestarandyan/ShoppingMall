package com.manyi.mall.user;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.EditText;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.backstack.AbsOp;
import com.huoqiu.framework.backstack.BackOpFragmentActivity;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.user.ResetPasswordRequest;
import com.manyi.mall.service.UcService;

@EFragment(R.layout.fragment_reset_password)
public class ResetPasswordFragment extends SuperFragment<Integer> {
	private Bundle mBundle;
	private UcService mUserService;
	@ViewById(R.id.new_password)
	EditText mNewPassword;
	@ViewById(R.id.confirm_password)
	EditText mConfirmPassword;
	private boolean isFirstEnter = true;

	@AfterViews
	public void showKeyBoard() {
		addAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (isFirstEnter) {
					ManyiUtils.showKeyBoard(getActivity(), mNewPassword);
					isFirstEnter = false;
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
	}

	@Click(R.id.rest_line)
	@UiThread
	void restLine() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		if (mNewPassword.getText().toString().length() == 0) {
			DialogBuilder.showSimpleDialog("请输入新密码", getBackOpActivity());
			return;
		} else if (mConfirmPassword.getText().toString().length() == 0) {
			DialogBuilder.showSimpleDialog("请再次输入", getBackOpActivity());
			return;
		} else if (!mNewPassword.getText().toString().equals(mConfirmPassword.getText().toString())) {
			DialogBuilder.showSimpleDialog("两次密码输入不一样", getBackOpActivity());
			return;
		}

		restPassword();
	}

	@Background
	void restPassword() {
		try {
			mBundle = getArguments();
			String userName = mBundle.getString("phone");
			Log.v("userName", userName + "------------------------>");
			String password = mConfirmPassword.getText().toString().trim();

			ResetPasswordRequest req = new ResetPasswordRequest();
			req.setPhone(userName);
			req.setNewPwd(password);

			mUserService.Restpassword(req);
			restSucess();

		} catch (RestException e) {
			error(e.getMessage());
		}

	}

	@UiThread
	void restSucess() {
		DialogBuilder.showSimpleDialog("修改密码成功", "确定", getBackOpActivity(), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				push(new AbsOp(ForgetPasswordFragment.class.getName()) {
					@Override
					public void perform(BackOpFragmentActivity activity) {
						remove(getTag());
					}
				});
				remove();
				ManyiUtils.closeKeyBoard(getActivity(), mConfirmPassword);
			}
		});
	}

	@UiThread
	void error(String e) {
		DialogBuilder.showSimpleDialog(e, getBackOpActivity());
	}

	@Click(R.id.reset_password_back)
	void back() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		ManyiUtils.closeKeyBoard(getActivity(), mConfirmPassword);
		remove();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ManyiUtils.closeKeyBoard(getActivity(), mConfirmPassword);
	}
}
