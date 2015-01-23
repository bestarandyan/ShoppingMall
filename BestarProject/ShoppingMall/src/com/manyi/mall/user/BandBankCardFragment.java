package com.manyi.mall.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.huoqiu.widget.BankEditTextView;
import com.manyi.mall.MainActivity;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.user.BandBankCardRequest;
import com.manyi.mall.cachebean.user.UpdateBankCardRequest;
import com.manyi.mall.common.Constants;
import com.manyi.mall.common.util.AESUtil;
import com.manyi.mall.service.UcService;

@EFragment(R.layout.fragment_bind_bank_card)
public class BandBankCardFragment extends SuperFragment<Integer> {
	// commit button
	@ViewById(R.id.band_bank_card_commit)
	Button mCommitButton;

	// account name text view
	@ViewById(R.id.bank_card_name)
	TextView mUserNametTextView;

	// account city text view
	@ViewById(R.id.bank_card_city)
	TextView mAccountCitytTextView;

	// bank card number text view
	@ViewById(R.id.bank_card_number)
	BankEditTextView mCardNumberTextView;

	// bank name
	@ViewById(R.id.bank_card_bank_name)
	TextView mBankNametTextView;

	// sub bank name
	@ViewById(R.id.bank_card_location)
	EditText mSubBankNameEditText;

	// layout of FYB password
	@ViewById(R.id.password_tabrow)
	TableRow mFYBPasswordTableRow;

	// edit text of FYB password
	@ViewById(R.id.bank_card_password)
	EditText mFYBPasswordEditText;

	// jump over button
	@ViewById(R.id.jump_over)
	Button mJumpOverButton;

	// fragment title
	@ViewById(R.id.bind_card_center_title)
	TextView mTitletTextView;

	// text view of back
	@ViewById(R.id.bind_card_back)
	TextView mBacktTextView;

	@FragmentArg
	String bankCode;

	@FragmentArg
	String bankName;

	@FragmentArg
	String subBankName;

	private UcService mUserService;

	/**
	 * Initialize fragment, show keyboard after animation, and show or hide
	 * views by arguments.
	 */
	@AfterViews
	public void init() {
		try {
			addAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					ManyiUtils.showKeyBoard(getActivity(), mCardNumberTextView);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}
			});
			String getEnRealName = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getString("realName", null);
			String getRealName = AESUtil.decrypt(getEnRealName, com.manyi.mall.common.CommonConfig.AES_KEY);
			mUserNametTextView.setText(getRealName);
			String cityName = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getString("cityName", null);
			mAccountCitytTextView.setText(cityName);
			if (bankCode != null) {
				mJumpOverButton.setVisibility(View.GONE);
				mTitletTextView.setVisibility(View.GONE);
				mBacktTextView.setVisibility(View.VISIBLE);
				if (!bankCode.equals("notBind")) {
					mFYBPasswordTableRow.setVisibility(View.VISIBLE);
					mCardNumberTextView.setText(bankCode);
					mCommitButton.setText(getResources().getString(R.string.update_bank_card_commit));
					mBankNametTextView.setText(bankName);
					mSubBankNameEditText.setText(subBankName);
					if (bankCode.length() % 4 == 0) {
						mCardNumberTextView.setSelection(bankCode.length() + Math.min(bankCode.length() / 4 - 1, 4));
					} else {
						mCardNumberTextView.setSelection(bankCode.length() + Math.min(bankCode.length() / 4, 4));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Close keyboard when exit the fragment.
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (getActivity() == null || getActivity().isFinishing()) {
			return;
		}
		ManyiUtils.closeKeyBoard(getActivity(), mCardNumberTextView);
	}

	/**
	 * Update or bind bank card.
	 */
	@Background
	void bindBankCard() {
		if (bankCode != null) {
			if (bankCode.equals("notBind")) {
				bindCard();
			} else {
				updateCard();
			}
		} else {
			bindCard();
		}
		bindSucess();
	}

	/**
	 * Click to choose bank.
	 */
	@Click(R.id.bank_card_bank_name)
	void selectBankName() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		ManyiUtils.closeKeyBoard(getActivity(), mCardNumberTextView);
		BankSelectFragment bankSelectFragment = GeneratedClassUtils.getInstance(BankSelectFragment.class);
		bankSelectFragment.tag = BankSelectFragment.class.getName();
		bankSelectFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		bankSelectFragment.setManager(getFragmentManager());
		bankSelectFragment.setSelectListener(new SelectListener<String>() {

			@Override
			public void onSelected(String t) {
				mBankNametTextView.setText(t);
			}

			@Override
			public void onCanceled() {

			}

		});
		bankSelectFragment.show(SHOW_ADD_HIDE);
	}

	@Click(R.id.band_bank_card_commit)
	void commitBandCard() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		if (!checkInputStyle()) {
			return;
		}
		bindBankCard();
	}

	/**
	 * Show a toast after bind success.
	 */
	@UiThread
	void bindSucess() {
		if (bankCode != null && !bankCode.equals("notBind")) {
			showCustomToast(getResources().getString(R.string.update_bank_card_success));
		} else {
			showCustomToast(getResources().getString(R.string.band_bank_card_success));
		}
		ManyiUtils.closeKeyBoard(getActivity(), mCardNumberTextView);
		notifySelected(null);
	}

	/**
	 * jump over this fragment.
	 */
	@Click(R.id.jump_over)
	public void jumpOver() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		initMainActivity();
	}

	/**
	 * Back to the previous fragment.
	 */
	@Click(R.id.bind_card_back)
	public void bindCardBack() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		remove();
	}

	private void initMainActivity() {
		Intent loginIntent = new Intent(getActivity(), GeneratedClassUtils.get(MainActivity.class));
		getActivity().overridePendingTransition(R.anim.anim_fragment_in, R.anim.anim_fragment_out);
		startActivity(loginIntent);
		getActivity().finish();
	}

	private boolean checkInputStyle() {
		String cardNumber = mCardNumberTextView.getTextString();
		String subBankCardName = mSubBankNameEditText.getText().toString();
		Pattern numberPa = Pattern.compile("^[0-9]\\d*|0$");
		Matcher numberMatcher = numberPa.matcher(cardNumber);
		if (!numberMatcher.matches()) {
			showCustomToast(getResources().getString(R.string.band_bank_card_error1));
			return false;
		}

		Pattern bankNamePa = Pattern.compile("^[\u4e00-\u9fa5]*$");
		Matcher subBankMatcher = bankNamePa.matcher(subBankCardName);
		if (!subBankMatcher.find() || TextUtils.isEmpty(subBankCardName)) {
			showCustomToast(getResources().getString(R.string.band_bank_card_error3));
			return false;
		}

		if (bankCode != null && !bankCode.equals("notBind")) {
			String fybPassword = mFYBPasswordEditText.getText().toString();
			if (fybPassword == null || TextUtils.isEmpty(fybPassword)) {
				showCustomToast(getResources().getString(R.string.band_bank_card_error4));
				return false;
			}
		}

		return true;
	}

	private void showCustomToast(String message) {
		LayoutInflater inflater = getBackOpActivity().getLayoutInflater();
		View layout = inflater.inflate(R.layout.item_bind_bank_card_toast, null);
		TextView textView = (TextView) layout.findViewById(R.id.toast_text);
		textView.setText(message);
		Toast toast = new Toast(getBackOpActivity());
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setView(layout);
		toast.show();
	}

	private void bindCard() {
		BandBankCardRequest request = new BandBankCardRequest();
		int uid = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
		request.setUserId(uid);
		request.setBank(mBankNametTextView.getText().toString().trim());
		request.setSubBank(mSubBankNameEditText.getText().toString().trim());
		request.setBankCode(mCardNumberTextView.getTextString().trim());
		int cityId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("cityId", 0);
		request.setCityId(cityId);
		mUserService.bandBankCard(request);
	}

	private void updateCard() {
		UpdateBankCardRequest request = new UpdateBankCardRequest();
		int uid = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
		request.setUserId(uid);
		request.setBank(mBankNametTextView.getText().toString().trim());
		request.setSubBank(mSubBankNameEditText.getText().toString().trim());
		request.setBankCode(mCardNumberTextView.getTextString().trim());
		int cityId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("cityId", 0);
		request.setCityId(cityId);
		request.setFybpsd(mFYBPasswordEditText.getText().toString().trim());
		mUserService.updateBankCard(request);
	}
}
