package com.manyi.mall.user;

import java.io.File;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.backstack.BackOpFragmentActivity;
import com.huoqiu.framework.backstack.Op;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.manyi.mall.R;
import com.manyi.mall.StartFragment;
import com.manyi.mall.cachebean.user.GetFailedDetailRequest;
import com.manyi.mall.cachebean.user.GetFailedDetailResponse;
import com.manyi.mall.common.Constants;
import com.manyi.mall.service.UcService;


@EFragment(R.layout.fragment_review_failed)
public class ReviewFailedFragment extends SuperFragment<Object> {
	private String mRealName;
	private int mCityId;// 城市ID
	private String mCityName;// 城市
	private int mAreaId;// 区域ID
	private String mAreaName;// 区域名称
	private int mTownId;// 板块ID
	private String mTownName;// 板块名称
	private String mCode;// 身份证号码
	private String mSpreadName;// 邀请码
	private File mCardFile;// 名片地址
	private File mCodeFile;// 身份证照片
	private String mRemark;// 失败原因
	private int mUserId; // 用户ID
	
	@FragmentArg
	public String mUserName;
	@ViewById(R.id.review_failed_detail)
	public TextView mFailedRemark;
	
	@ViewById(R.id.btn_regist_again)
	public Button registAgainButton;
	
	private UcService userService;

	@Click({ R.id.btn_exit_login })
	public void registerNext() {
		if (CheckDoubleClick.isFastDoubleClick())
		return;
		ManyiAnalysis.onEvent(getBackOpActivity(), "LogoutClick");
        DialogBuilder.showSimpleDialog("您确定退出?", "确定", "取消", getBackOpActivity(), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (CheckDoubleClick.isFastDoubleClick())
							return;
						SharedPreferences sharedPreferences2 = getBackOpActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0);
						Editor editor = sharedPreferences2.edit();

						editor.putString("password", null);
						editor.putString("name", null);
						editor.putString("realName", null);
						editor.putBoolean("isSkip", false);
						editor.putInt("cityId", 0);
						editor.putString("cityName", null);
						editor.commit();

						StartFragment startFragment = GeneratedClassUtils.getInstance(StartFragment.class);
						startFragment.tag = StartFragment.class.getName();
						Bundle bundle = new Bundle();
						bundle.putBoolean("NotCheckNewVersion", true);
						startFragment.setArguments(bundle);
						startFragment.setManager(getFragmentManager());
						startFragment.setBackOp(new Op() {

							@Override
							public void setTag(String tag) {
							}

							@Override
							public void perform(BackOpFragmentActivity activity) {
								getActivity().finish();
							}

							@Override
							public String getTag() {
								return StartFragment.class.getName();
							}
						});
						startFragment.show();
					}
				});
	}
	
	@AfterViews
	public void init(){
		getFailDetail();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Click({ R.id.btn_regist_again })
	@UiThread
	public void modifyLoginInfo() {
		if (CheckDoubleClick.isFastDoubleClick())
		return;
		ManyiAnalysis.onEvent(getBackOpActivity(), "ChangeMyInfoClick");
		RegisterNextFragment registerNextFragment = GeneratedClassUtils.getInstance(RegisterNextFragment.class);
		Bundle args = new Bundle();
		args.putString("mRealName", mRealName);
		args.putInt("mCityId", mCityId);
		args.putString("mCityName", mCityName);
		args.putInt("mAreaId", mAreaId);
		args.putString("mAreaName", mAreaName);
		args.putInt("mTownId", mTownId);
		args.putString("mTownName", mTownName);
		args.putString("mCode", mCode);
		args.putString("mSpreadName", mSpreadName);
		args.putSerializable("mCardFile", mCardFile); 
		args.putSerializable("mCodeFile", mCodeFile);
		args.putString("mRemark", mRemark);
		args.putInt("mUserId", mUserId);
		args.putBoolean("isRegisterAgain", true);
		registerNextFragment.setArguments(args);
		registerNextFragment.tag = RegisterNextFragment.class.getName();
		registerNextFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);
		registerNextFragment.setManager(getBackOpActivity().getSupportFragmentManager());
		registerNextFragment.show(SHOW_ADD_HIDE);
	}
	
	@Background
	public void getFailDetail(){
		GetFailedDetailRequest getFailedDetailRequest = new GetFailedDetailRequest();
		getFailedDetailRequest.setMobile(mUserName);
		GetFailedDetailResponse failedDetailResponse = userService.getFailedDetail(getFailedDetailRequest);
		if (failedDetailResponse != null && failedDetailResponse.getErrorCode() == 0) {
			mRealName = failedDetailResponse.getRealName();
			mCityId = failedDetailResponse.getCityId();
			mCityName = failedDetailResponse.getCityName();
			mAreaId = failedDetailResponse.getAreaId();
			mAreaName = failedDetailResponse.getAreaName();
			mTownId = failedDetailResponse.getTownId();
			mTownName = failedDetailResponse.getTownName();
			mCode = failedDetailResponse.getCode();
			mSpreadName = failedDetailResponse.getSpreadName();
			mCardFile = failedDetailResponse.getCardFile(); 
			mCodeFile = failedDetailResponse.getCodeFile();
			mRemark = failedDetailResponse.getRemark();
			mUserId = failedDetailResponse.getUserId();
			updateRegistAgainButton();
			setRemark();
		}
	}
	
	@UiThread
	public void setRemark(){
		mFailedRemark.setText(getString(R.string.failed_remark) + mRemark);
	}
	
	@UiThread
	public void updateRegistAgainButton(){
		registAgainButton.setBackgroundResource(R.drawable.start_selector);
		registAgainButton.setEnabled(true);
	}
}  
