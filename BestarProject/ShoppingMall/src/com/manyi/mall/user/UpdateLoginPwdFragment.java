package com.manyi.mall.user;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.rest.Response;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.user.ModifyLoginPwdRequest;
import com.manyi.mall.common.CommonConfig;
import com.manyi.mall.common.Constants;
import com.manyi.mall.common.util.AESUtil;
import com.manyi.mall.service.UcService;

@EFragment(R.layout.fragment_update_login_pwd)
public class UpdateLoginPwdFragment extends SuperFragment<Object> {

	@ViewById(R.id.update_pwd_commit)
	public TextView mModifyLoginPassword;

	@ViewById(R.id.old_password)
	public EditText mOldPassword;

	@ViewById(R.id.new_password)
	public EditText mNewPassword;

	@ViewById(R.id.confirm_password)
	public EditText mConfirmPassword;

	@ViewById(R.id.update_pwd_back)
	public TextView mModifyPasswordBack;

	
	private UcService mUserService;

	private ProgressDialog mUpdateDialog;
	private ProgressDialog mUpdateSuccessDialog;
	private boolean isFirstEnter = true;

	@AfterViews
	public void showKeyBoard(){
		addAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (isFirstEnter) {
					ManyiUtils.showKeyBoard(getActivity(), mOldPassword);
					isFirstEnter = false;
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
	}

	@Click({ R.id.update_pwd_back })
	public void registerBack() {
		if (CheckDoubleClick.isFastDoubleClick())
		return;
		ManyiUtils.closeKeyBoard(getActivity(), mOldPassword);
		remove();
	}

	@Click(R.id.update_pwd_commit)
	public void updateLoginPassword() {
		if (CheckDoubleClick.isFastDoubleClick())
		return;
		boolean isSame = mNewPassword.getText().toString().trim().equals(mConfirmPassword.getText().toString().trim());
		if (isSame) {
			boolean hasDigit = false;
			boolean hasLetter = false;
			String str = mNewPassword.getText().toString().trim();
			for (int i = 0; i < str.length(); i++) {
				if (Character.isDigit(str.charAt(i))) {
					hasDigit = true;
				}
				if (Character.isLetter(str.charAt(i))) {
					hasLetter = true;
				}
			}
			if (hasDigit && hasLetter && str.length() >= 6 && str.length() <= 20) {
				mUpdateDialog = ProgressDialog.show(getActivity(), null, "修改中", true);
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							ModifyLoginPwdRequest updateLoginPwdRequest = new ModifyLoginPwdRequest();
							int uid = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
							updateLoginPwdRequest.setUid(uid);
							updateLoginPwdRequest.setOldPwd(mOldPassword.getEditableText().toString().trim());
							updateLoginPwdRequest.setNewPwd(mNewPassword.getEditableText().toString().trim());
							updateLoginPwdRequest.setConfPwd(mConfirmPassword.getEditableText().toString().trim());
							Response response = mUserService.modifyLoginPwd(updateLoginPwdRequest);
							int responseCode = response.getErrorCode();
							if (responseCode == 0) {
								// 修改成功
								onUpdateSuccess1();
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								onUpdateSuccess2();
							} else {
								onNetworkFailed(response.getMessage());
							}
						} catch (Exception e) {
							// 修改失败
							onUpdateFailed(e.getMessage());
						}
					}
				}).start();
			} else {
				DialogBuilder.showSimpleDialog("密码格式不对", getBackOpActivity());
			}
		} else {
			DialogBuilder.showSimpleDialog("两次密码输入不一致", getBackOpActivity());
		}
	}

	@UiThread
	public void onUpdateSuccess1() {
		mUpdateDialog.dismiss();
		mUpdateSuccessDialog = ProgressDialog.show(getBackOpActivity(), null, "修改成功", true);
	}

	@UiThread
	public void onUpdateSuccess2() {
		try {
			mUpdateSuccessDialog.dismiss();
			SharedPreferences sharedPreferences = getBackOpActivity().getSharedPreferences(Constants.LOGIN_TIMES,
					Context.MODE_PRIVATE + Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			editor.putString("password", AESUtil.encrypt(mConfirmPassword.getEditableText().toString().trim(), CommonConfig.AES_KEY));
			editor.commit();
			ManyiUtils.closeKeyBoard(getActivity(), mOldPassword);
			remove();
		} catch (Exception e) {
			e.printStackTrace();
			Thread.getDefaultUncaughtExceptionHandler().uncaughtException(null, e);
		}
	}

	@UiThread
	public void onUpdateFailed(String msg) {
		mUpdateDialog.dismiss();
		DialogBuilder.showSimpleDialog(msg, getBackOpActivity());
	}

	@UiThread
	public void onNetworkFailed(String msg) {
		mUpdateDialog.dismiss();
		DialogBuilder.showSimpleDialog(msg, getBackOpActivity());
	}

	@UiThread
	public void onGerRegisterDataError(RestException e) {
		DialogBuilder.showSimpleDialog(e.getMessage(), getBackOpActivity());
	}

}
