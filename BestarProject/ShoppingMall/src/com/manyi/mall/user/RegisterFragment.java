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
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.ClientException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.user.CaptchaCodeRequest;
import com.manyi.mall.cachebean.user.CheckMsgCodeRequest;
import com.manyi.mall.cachebean.user.RegistRequest;
import com.manyi.mall.service.UcService;

@EFragment(R.layout.fragment_register)
public class RegisterFragment extends SuperFragment<Object> {

	@ViewById(R.id.phone_number)
	EditText mPhoneNumber;
	@ViewById(R.id.tv_unable_number)
	TextView mUnableNumber;

	@ViewById(R.id.sms_captcha)
	EditText mSmsCaptcha;
	@ViewById(R.id.register_pwd)
	EditText mRegisterPwd;
	@ViewById(R.id.confirm_pwd)
	EditText mConfirmPwd;

	@ViewById(R.id.sms_register)
	Button mSmsRegister;

	private UcService mUserService;

	private boolean isFirstEnter = true;

	@Click({ R.id.sms_register })
	@Background
	public void sendSMS() {
		try {
			if (CheckDoubleClick.isFastDoubleClick())
				return;

			changeRandom(false);

			if (!phonenumValidate()) {
				return;
			}
			RegistRequest request = new RegistRequest();
			request.setMobile(mPhoneNumber.getText().toString());
			mUserService.check(request);

			CaptchaCodeRequest req = new CaptchaCodeRequest();
			req.setMobile(mPhoneNumber.getText().toString());
			mUserService.code(req);
			onSendSMSSuccess();

		} catch (Exception e) {
			if (e instanceof ClientException) {
				ClientException ce = (ClientException) e;
				if (ce.getErrorCode() == ClientException.REPEATED_REQUEST || ce.getErrorCode() == ClientException.REQUEST_ABORTED)
					return;
			}
			onSendSMSError(e.getMessage());
		}
	}

	@AfterViews
	public void showKeyBoard() {
			addAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					if (isFirstEnter) {
						ManyiUtils.showKeyBoard(getActivity(), mPhoneNumber);
						isFirstEnter = false ;
					}
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}
			});

	}

	@UiThread
	public void changeRandom(boolean status) {
		mSmsRegister.setEnabled(status);
	}

	@UiThread
	public void onSendSMSSuccess() {
		if (getActivity() == null || getActivity().isFinishing()) {
			return;
		}
		mSmsRegister.setEnabled(false);
		DialogBuilder.showSimpleDialog("验证码已发送!", getActivity(), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				timer.start();
				int left = mSmsRegister.getPaddingLeft();
				int right = mSmsRegister.getPaddingRight();
				mSmsRegister.setBackgroundResource(R.drawable.btn_orange_dis);
				mSmsRegister.setPadding(left, 0, right, 0);
			}
		});
	}

	@UiThread
	public void onSendSMSError(String e) {
		if (getActivity() == null || getActivity().isFinishing()) {
			return;
		}
		mSmsRegister.setEnabled(true);
		DialogBuilder.showSimpleDialog(e, getActivity());
	}

	CountDownTimer timer = new CountDownTimer(60000, 1000) {
		@Override
		public void onTick(long arg0) {
			mSmsRegister.setText("再次获取(" + arg0 / 1000 + ")秒");
		}

		@Override
		public void onFinish() {
			setTimerViewCanceledState();
		}
	};

	@Click({ R.id.register_back })
	public void registerBack() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		remove();
	}

	private boolean phonenumValidate() {
		if (mPhoneNumber.getText().toString().length() == 0) {
			onSendSMSError("请输入手机号");
			return false;
		} else if (mPhoneNumber.getText().toString().length() < 11) {
			onSendSMSError("手机号码格式不正确!");
			return false;
		} else
			return true;
	}

	@Click(R.id.register_next)
	@Background
	public void getRegisterData() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		if (!phonenumValidate()) {
			return;
		}
		if (mSmsCaptcha.getText().toString().length() == 0) {
			onSendSMSError("请输入验证码!");
			return;
		} else if (mRegisterPwd.getText().toString().length() == 0) {
			onSendSMSError("请输入密码");
			return;
		} else if (mConfirmPwd.getText().toString().length() == 0) {
			onSendSMSError("请再次输入密码");
			return;
		} else if (!mConfirmPwd.getText().toString().equals(mRegisterPwd.getText().toString())) {
			onSendSMSError("输入密码不一致!");
			return;
		}

		try {

			CheckMsgCodeRequest nextRequest = new CheckMsgCodeRequest();
			nextRequest.setCheckVerifyCode(mSmsCaptcha.getText().toString().trim());
			nextRequest.setPassword(mConfirmPwd.getText().toString().trim());
			mUserService.checkMsgCode(nextRequest);
			onGerRegisterDataSuccess();
		} catch (Exception e) {
			if (e instanceof ClientException) {
				ClientException ce = (ClientException) e;
				if (ce.getErrorCode() == ClientException.REPEATED_REQUEST || ce.getErrorCode() == ClientException.REQUEST_ABORTED)
					return;
			}
			onGerRegisterDataError(e);
		}

	}

	/**
	 * 关闭倒计时，改变时间控件状态
	 * 
	 * @author bestar
	 */
	private void setTimerViewCanceledState() {
		mSmsRegister.setText(R.string.register_get_captcha);
		mSmsRegister.setEnabled(true);
		int left = mSmsRegister.getPaddingLeft();
		int right = mSmsRegister.getPaddingRight();
		mSmsRegister.setBackgroundResource(R.drawable.start_selector);
		mSmsRegister.setPadding(left, 0, right, 0);
	}

	@UiThread
	public void onGerRegisterDataSuccess() {
		timer.cancel();
		setTimerViewCanceledState();
		ManyiUtils.closeKeyBoard(getActivity(), mPhoneNumber);
		RegisterNextFragment registerNextFragment = GeneratedClassUtils.getInstance(RegisterNextFragment.class);
		Bundle bundle = new Bundle();
		bundle.putString("mPhoneNumber", mPhoneNumber.getText().toString());
		bundle.putString("mSmsCaptcha", mSmsCaptcha.getText().toString());
		bundle.putString("mRegisterPwd", mRegisterPwd.getText().toString());
		bundle.putString("mConfirmPwd", mConfirmPwd.getText().toString());
		registerNextFragment.setArguments(bundle);
		registerNextFragment.tag = RegisterNextFragment.class.getName();
		registerNextFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		registerNextFragment.setManager(getFragmentManager());
		registerNextFragment.show(SHOW_ADD_HIDE);
	}

	@UiThread
	public void onGerRegisterDataError(Exception e) {
		DialogBuilder.showSimpleDialog(e.getMessage(), getActivity());
	}

}
