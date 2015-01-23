package com.manyi.mall.user;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.EstateFeedbackRequest;
import com.manyi.mall.common.Constants;
import com.manyi.mall.service.EstateService;

@EFragment(R.layout.fragment_estate_feedback)
public class EstateFeedbackFragment extends SuperFragment<Integer> {
	@ViewById(R.id.estate_feedback_estate_name)
	EditText mEstateNameeEditText;

	@ViewById(R.id.estate_feedback_estate_address)
	EditText mEstateAddresseEditText;

	@ViewById(R.id.estate_feedback_problem_decription)
	EditText mEstateDescriptioneEditText;

	private EstateService mUserService;

	/**
	 * Show keyboard after animation.
	 */
	@AfterViews
	public void init() {
		addAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				ManyiUtils.showKeyBoard(getActivity(), mEstateNameeEditText);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
	}
	
	/**
	 * Close keyboard after exit fragment.
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		ManyiUtils.closeKeyBoard(getActivity(), mEstateNameeEditText);
	}

	/**
	 * Submit estate back request, check the input before submit.
	 */
	@Background
	void submitFeedback() {
		try {
			if (TextUtils.isEmpty(mEstateNameeEditText.getEditableText().toString().trim())) {
				estateNameWarning();
				return;
			}

			if (TextUtils.isEmpty(mEstateAddresseEditText.getEditableText().toString().trim())) {
				estateAddressWarning();
				return;
			}

			if (mEstateDescriptioneEditText.getEditableText().toString().trim().length() > 100) {
				estateDescriptionWarning();
				return;
			}

			EstateFeedbackRequest request = new EstateFeedbackRequest();
			request.setEstateName(mEstateNameeEditText.getEditableText().toString());
			request.setEstateAddr(mEstateAddresseEditText.getEditableText().toString());
			request.setContent(mEstateDescriptioneEditText.getEditableText().toString());
			int userId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, Context.MODE_PRIVATE).getInt("uid", 0);
			request.setUserId(userId);
			mUserService.estateFeedback(request);
			submitSuccess();
		} catch (RestException e) {
			error(e.getMessage());
		}

	}

	@UiThread
	public void estateNameWarning() {
		DialogBuilder.showSimpleDialog(getString(R.string.fill_in_estate_name), getActivity());
	}

	@UiThread
	public void estateAddressWarning() {
		DialogBuilder.showSimpleDialog(getString(R.string.fill_in_estate_address), getActivity());
	}

	@UiThread
	public void estateDescriptionWarning() {
		DialogBuilder.showSimpleDialog(getString(R.string.estate_description_too_long), getActivity());
	}

	/**
	 * Show a toast after submit success.
	 */
	@UiThread
	void submitSuccess() {
		remove();
		LayoutInflater inflater = getBackOpActivity().getLayoutInflater();
		View layout = inflater.inflate(R.layout.item_estate_feedback_toast, null);
		TextView textView = (TextView) layout.findViewById(R.id.toast_text);
		textView.setText(getString(R.string.estate_feedback_success));
		Toast toast = new Toast(getBackOpActivity());
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setView(layout);
		toast.show();
	}

	@UiThread
	void error(String e) {
		DialogBuilder.showSimpleDialog(e, getActivity());
	}

	@Click(R.id.estate_feedback_commit)
	public void submit() {
		if (CheckDoubleClick.isFastDoubleClick()){
			return ;
		}
		submitFeedback();
	}
	
	/**
	 * Back to the previous fragment.
	 */
	@Click(R.id.estate_feedback_back)
	void back() {
		if (CheckDoubleClick.isFastDoubleClick()){
			return ;
		}
		remove();
	}
}
