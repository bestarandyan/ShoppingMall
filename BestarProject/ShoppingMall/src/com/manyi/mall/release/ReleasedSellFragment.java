package com.manyi.mall.release;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.release.CommonReleasedFirstRequest;
import com.manyi.mall.cachebean.release.CommonReleasedFirstResponse;
import com.manyi.mall.cachebean.release.RentAndSellReleaseRecordInfoResponse;
import com.manyi.mall.cachebean.search.BuildingResponse;
import com.manyi.mall.cachebean.search.SearchRespose.Estate;
import com.manyi.mall.cachebean.search.SubEstateStateRequest;
import com.manyi.mall.cachebean.search.SubEstateStateResponse;
import com.manyi.mall.common.Constants;
import com.manyi.mall.search.SearchBuildingFragment;
import com.manyi.mall.search.SearchLivingAreaFragment;
import com.manyi.mall.service.CommonService;
import com.manyi.mall.service.EstateService;
import com.manyi.mall.user.EstateFeedbackFragment;
import com.manyi.mall.user.RuleContentFragment;

@SuppressLint("DefaultLocale")
@EFragment(R.layout.fragment_released_sell)
public class ReleasedSellFragment extends SuperFragment<Object> {

	@ViewById(R.id.sell_seat_number)
	public EditText mSellSeatNumber; //楼栋号
	
	@ViewById(R.id.sell_room_no)
	public EditText mSellRoomNO; //室号

	@ViewById(R.id.sell_area_name)
	public TextView mSellAreaName;

	@ViewById(R.id.sell_single_building)
	public CheckBox mSellSingleBuilding; //独栋
	
	@ViewById(R.id.sell_not_unit)
	public CheckBox mSellNotUnit; //无单元
	
	@ViewById(R.id.sell_villa)
	public CheckBox mSellVillaCheckBox; //无单元

	@ViewById(R.id.sell_unit_no)
	public EditText mUtilNot;  //单元号

	@ViewById(R.id.unit_number_layout)
	public LinearLayout mUnitNumberLayout;

	@ViewById(R.id.unit_prompt)
	public LinearLayout mUnitPrompt;

	@ViewById(R.id.released_sell_room_no)
	public TextView mReleasedSellRoomNo;

	@ViewById(R.id.sell_building_layout)
	public LinearLayout mSellBuildingLayout;

	@ViewById(R.id.sell_building_prompt)
	public LinearLayout mSellBuildingPrompt;

	private CommonService mCommonService;
	private EstateService mSubEstateService;
	@SuppressWarnings("unused")
	private boolean mIsAreaSelect;
	private Estate mEstate;
	public String mSellBuilding;
	public String mSellUnitNumber;
	public String mSellRoomNumber;
	private int cityId = 0;
	private CommonReleasedFirstResponse commonFirst;
	private SubEstateStateResponse mCheckedResponse ;
	private BuildingResponse mBuildingResponse;
	private boolean isCheckedAreaName = false;//判断是否选择过小区名称
	private CommonReleasedFirstRequest firstRequest;

	@FragmentArg
	RentAndSellReleaseRecordInfoResponse mReleaseRecordInfoResponse;
	

	

	@Override
	public void onResume() {
		super.onResume();
		if (mEstate != null) {
			mSellAreaName.setText(mEstate.getEstateName());
		}
	}

	/**
	 * 根据北京上海 区分 单元号的显示
	 */
	@AfterViews
	public void initUnitNo() {
		cityId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("cityId", 0);
		switch (cityId) {
		case Constants.ID_CITY_SHANGHAI:
			initViewForShangHai();
			break;
		case Constants.ID_CITY_BEIJING:
			initViewForBeijing();
			break;
		default:
			initViewForGuangzhouOrOther();
			break;
		}
		mSellSeatNumber.setClickable(false);
	}
	
	public void initViewForBeijing(){
		mSellSeatNumber.addTextChangedListener(new TextWatcher() {
			 String seatNumberText = "";
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (seatNumberText.equals(s.toString())) {
				     return;
				    }
				  String newText = s.toString().replace("-", "");
				  if (newText.equals(seatNumberText)) {
					  mSellSeatNumber.setText(newText);
					 }
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		
		mUnitNumberLayout.setVisibility(View.VISIBLE);
		mUnitPrompt.setVisibility(View.VISIBLE);
		mReleasedSellRoomNo.setText("门牌号");
		mSellSingleBuilding.setText("小区单栋");
		if(mReleaseRecordInfoResponse != null){ // 重新发布  详情页Response
			
			if(!TextUtils.isEmpty(mReleaseRecordInfoResponse.getEstateName().toString()) && !TextUtils.isEmpty(mReleaseRecordInfoResponse.getSubEstateName().toString())){
				mSellAreaName.setText(mReleaseRecordInfoResponse.getEstateName().toString()+"  "+mReleaseRecordInfoResponse.getSubEstateName().toString());
			}else if(!TextUtils.isEmpty(mReleaseRecordInfoResponse.getEstateName().toString())){
				mSellAreaName.setText(mReleaseRecordInfoResponse.getEstateName().toString());
			}
			String mBuilding = mReleaseRecordInfoResponse.getBuilding();
			if (mBuilding!=null && mBuilding.equals("0")) { //2-0
				mSellSingleBuilding.setChecked(true);
				mSellNotUnit.setChecked(true);
			}else if(mBuilding.contains("-")){
				String[] newBuilding = mBuilding.split("-");
				String str1 = newBuilding[0];
				String str2 = newBuilding[1];
				String num1 = String.valueOf(str1);
				String num2 = String.valueOf(str2);
				if (num1.equals("0")) {
					mSellSingleBuilding.setChecked(true);
				}else {
					mSellSeatNumber.setText(num1);
				}
				if (num2.equals("0")) {
					mSellNotUnit.setChecked(true);
				}else {
					mUtilNot.setText(num2);
				}
			}
			String sellRoomNumber = mReleaseRecordInfoResponse.getRoom();
			if (sellRoomNumber.equals("0000")) {
				mSellVillaCheckBox.setChecked(true);
			} else {
				mSellRoomNO.setText(sellRoomNumber);
			}
			
			checkSubEstate(mReleaseRecordInfoResponse.getSubEstateId());
		}
	
	}

	public void initViewForShangHai(){
		mUnitNumberLayout.setVisibility(View.GONE);
		mUnitPrompt.setVisibility(View.GONE);
		mReleasedSellRoomNo.setText("室号");
		mSellSingleBuilding.setText("独栋");
		if(mReleaseRecordInfoResponse != null){
			if(!TextUtils.isEmpty(mReleaseRecordInfoResponse.getEstateName().toString()) && !TextUtils.isEmpty(mReleaseRecordInfoResponse.getSubEstateName().toString())){
				mSellAreaName.setText(mReleaseRecordInfoResponse.getEstateName().toString()+"  "+mReleaseRecordInfoResponse.getSubEstateName().toString());
			}else if(!TextUtils.isEmpty(mReleaseRecordInfoResponse.getEstateName().toString())){
				mSellAreaName.setText(mReleaseRecordInfoResponse.getEstateName().toString());
			}
			
			String mBuilding = mReleaseRecordInfoResponse.getBuilding().toString();
			if("0".equals(mBuilding)){
				mSellSingleBuilding.setChecked(true);
			}else{
				mSellSeatNumber.setText(mBuilding);
			}
			String sellRoomNumber = mReleaseRecordInfoResponse.getRoom();
			if (sellRoomNumber.equals("0000")) {
				mSellVillaCheckBox.setChecked(true);
			} else {
				mSellRoomNO.setText(sellRoomNumber);
			}
			checkSubEstate(mReleaseRecordInfoResponse.getSubEstateId());
		}
		mSellBuildingPrompt.setVisibility(View.GONE);
	}
	public void initViewForGuangzhouOrOther(){
		mUnitNumberLayout.setVisibility(View.GONE);
		mUnitPrompt.setVisibility(View.GONE);
		mReleasedSellRoomNo.setText("室号");
		mSellSingleBuilding.setText("独栋");
		if(mReleaseRecordInfoResponse != null){
			if(!TextUtils.isEmpty(mReleaseRecordInfoResponse.getEstateName().toString()) && !TextUtils.isEmpty(mReleaseRecordInfoResponse.getSubEstateName().toString())){
				mSellAreaName.setText(mReleaseRecordInfoResponse.getEstateName().toString()+"  "+mReleaseRecordInfoResponse.getSubEstateName().toString());
			}else if(!TextUtils.isEmpty(mReleaseRecordInfoResponse.getEstateName().toString())){
				mSellAreaName.setText(mReleaseRecordInfoResponse.getEstateName().toString());
			}
			
			String mBuilding = mReleaseRecordInfoResponse.getBuilding().toString();
			if("0".equals(mBuilding)){
				mSellSingleBuilding.setChecked(true);
			}else{
				mSellSeatNumber.setText(mBuilding);
			}
			String sellRoomNumber = mReleaseRecordInfoResponse.getRoom();
			if (sellRoomNumber.equals("0000")) {
				mSellVillaCheckBox.setChecked(true);
			} else {
				mSellRoomNO.setText(sellRoomNumber);
			}
			checkSubEstate(mReleaseRecordInfoResponse.getSubEstateId());
		}
		mSellBuildingPrompt.setVisibility(View.GONE);
	}
	
	
	/**
	 * 字段检索
	 * 
	 * @return
	 */
	private boolean isGetData() {
		if (mSellSeatNumber.getText().toString().trim().length() == 0&& !mSellSingleBuilding.isChecked()) {
			showDialog(getString(R.string.dialog_msg_seat));
			return false;
		} else if (cityId == Constants.ID_CITY_BEIJING
				&&mCheckedResponse!=null && mCheckedResponse.getLockStatus()!= 0 
				&& mCheckedResponse.getLockStatus() == SubEstateStateResponse.NOTLOCKED
				&&(mSellUnitNumber == null || mSellUnitNumber.toString().trim().length() == 0)) {
			showDialog(getString(R.string.dialog_msg_util));
			return false;
		} else if (TextUtils.isEmpty(mSellRoomNumber)|| TextUtils.isEmpty(mSellRoomNumber.trim())) {
			if (cityId == Constants.ID_CITY_BEIJING) {// 北京
				showDialog(getString(R.string.dialog_msg_room_menpai));
			} else {
				showDialog(getString(R.string.dialog_msg_room_shi));
			}
			return false;
		}
		return true;
	}

	@UiThread
	public void showDialog(String msgString) {
		DialogBuilder.showSimpleDialog(msgString, getActivity());
	}

	/**
	 * 下一步
	 */
	@Background
	@Click(R.id.released_next)
	public void releasedNext() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		ManyiUtils.closeKeyBoard(getActivity(), mSellRoomNO);
		if (!isGetData()) {
			return;
		}
		ManyiAnalysis.onEvent(getActivity(), "NextOnSellPublishClick");

		try {
			if (mSellAreaName.getText().length() == 0) {
				onReleasedSellError(getActivity().getString(
						R.string.release_sell_dialog_error));
				return;
			}

			firstRequest = new CommonReleasedFirstRequest();

			if(mEstate == null && mReleaseRecordInfoResponse!= null){
				firstRequest.setSubEstateId(mReleaseRecordInfoResponse.getSubEstateId());
			}else{
				firstRequest.setSubEstateId(mEstate == null ? 0 : mEstate.getEstateId());
			}
			
			if(mCheckedResponse!=null && mCheckedResponse.getLockStatus() == SubEstateStateResponse.NOTLOCKED){
				if (!TextUtils.isEmpty(mSellBuilding) && !TextUtils.isEmpty(mSellUnitNumber)) {
					firstRequest.setBuilding(mSellBuilding.toString().toUpperCase()+ "-" + mSellUnitNumber.toString().toUpperCase());
				} else if (!TextUtils.isEmpty(mSellBuilding)) {
					firstRequest.setBuilding(mSellBuilding.toString().toUpperCase());
				}
			}else if(mCheckedResponse!=null && mCheckedResponse.getLockStatus() == SubEstateStateResponse.LOCKEDANDDUDONG){
				firstRequest.setBuilding("0");
			}else {
				if(mBuildingResponse!= null){
					firstRequest.setBuilding(mBuildingResponse.getBuildingName());
				}else {
					if (!TextUtils.isEmpty(mSellBuilding) && !TextUtils.isEmpty(mSellUnitNumber)) {
						firstRequest.setBuilding(mSellBuilding.toString().toUpperCase()+ "-" + mSellUnitNumber.toString().toUpperCase());
					} else if (!TextUtils.isEmpty(mSellBuilding)) {
						firstRequest.setBuilding(mSellBuilding.toString().toUpperCase());
					}
				}
			}

			
			firstRequest.setRoom(mSellRoomNumber.trim().toUpperCase());
			firstRequest.setHouseType(2);
			commonFirst = mCommonService.commonReleased(firstRequest);
			if(commonFirst!=null && commonFirst.getErrorCode() == 0){
				afterReleasedNext(commonFirst);
			}else {
				releasedFailed("提交失败！");
			}
		} catch (RestException e) {
			onReleasedSellError(e.getMessage());
		}
	}
	@UiThread
	void releasedFailed(String message){
		DialogBuilder.showSimpleDialog(message, getActivity());
	}
	@Click(R.id.sell_seat_number)
	void getBuildingClick(){
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		if (mCheckedResponse == null || mCheckedResponse.getLockStatus()!= SubEstateStateResponse.LOCKED){
			return;
		}
		ManyiAnalysis.onEvent(this.getActivity(), "AreaNameOnRentPublishClick");

		SearchBuildingFragment searchFragment = GeneratedClassUtils.getInstance(SearchBuildingFragment.class);
		searchFragment.tag = SearchBuildingFragment.class.getName();
		searchFragment.setCustomAnimations(R.anim.anim_fragment_in,
				R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		Bundle bundle = new Bundle();
		if(mEstate != null&&mEstate.getEstateId()!= 0){
			bundle.putInt("subEstateId", mEstate.getEstateId());
		}else{
			bundle.putInt("subEstateId", mReleaseRecordInfoResponse.getSubEstateId());
		}
		searchFragment.setArguments(bundle);
		searchFragment.setContainerId(R.id.main_container);
		searchFragment.setManager(getFragmentManager());
		searchFragment.setSelectListener(new SelectListener<Object>() {
			@Override
			public void onSelected(Object t) {
				mBuildingResponse = (BuildingResponse) t;
				if (mBuildingResponse != null) {
					mSellSeatNumber.setText(mBuildingResponse.getBuildingNameStr());
				}
			}
			@Override
			public void onCanceled() {
			}

		});
		searchFragment.show(SuperFragment.SHOW_ADD_HIDE);
	}
	@CheckedChange(R.id.sell_single_building)
	public void buildingCheckChange(CompoundButton buttonView, boolean isChecked) {
		ManyiAnalysis.onEvent(getActivity(), "SingleHoseOnSellPublishClick");

		if (isChecked) {
			mSellBuilding = "0";
			mSellSeatNumber.setVisibility(View.GONE);
		} else {
			mSellBuilding = mSellSeatNumber.getText().toString();
			mSellSeatNumber.setVisibility(View.VISIBLE);
		}
	}

	@CheckedChange(R.id.sell_not_unit)
	public void sellNotUnit(CompoundButton buttonView, boolean isChecked) {
		ManyiAnalysis.onEvent(getActivity(), "SingleHoseOnSellPublishClick");

		if (isChecked) {
			mSellUnitNumber = "0";
			mUtilNot.setVisibility(View.GONE);
		} else {
			mSellUnitNumber = mUtilNot.getText().toString();
			mUtilNot.setVisibility(View.VISIBLE);
		}
	}
	
	@CheckedChange(R.id.sell_villa)
	public void sellVilla(CompoundButton buttonView, boolean isChecked) {
		ManyiAnalysis.onEvent(getActivity(), "VillaOnSellPublishClick");
		if (isChecked) {
			mSellRoomNumber = "0000";
			mSellRoomNO.setVisibility(View.GONE);
		} else {
			mSellRoomNumber = mSellRoomNO.getText().toString();
			mSellRoomNO.setVisibility(View.VISIBLE);
		}
	}

	@TextChange(R.id.sell_seat_number)
	public void seatNumberChange() {
		mSellBuilding = mSellSeatNumber.getText().toString();
		ManyiAnalysis.onEvent(this.getActivity(),
				"HouseNumberOnRentPublishClick");
	}

	@TextChange(R.id.sell_unit_no)
	public void sellNotUnits() {
		mSellUnitNumber = mUtilNot.getText().toString();
		ManyiAnalysis.onEvent(this.getActivity(),
				"HouseNumberOnRentPublishClick");
	}

	/**
	 * 室号
	 */
	@TextChange(R.id.sell_room_no)
	public void roomNumber() {
		mSellRoomNumber = mSellRoomNO.getText().toString();
		ManyiAnalysis.onEvent(this.getActivity(),
				"HouseRoomNumberOnSellPublishClick");
	}

	@UiThread
	public void afterReleasedNext(CommonReleasedFirstResponse commonFirst) {
		ManyiUtils.closeKeyBoard(getActivity(), mSellRoomNO);

		ReleasedSellNextFragment releasedSellNextFragment = GeneratedClassUtils
				.getInstance(ReleasedSellNextFragment.class);
		Bundle args = new Bundle();
		args.putInt("subEstateId", firstRequest.getSubEstateId());
		args.putString("room", firstRequest.getRoom());
		args.putString("building", firstRequest.getBuilding());
		args.putString("token", commonFirst.getToken());
		if(mReleaseRecordInfoResponse!= null){
			args.putSerializable("mRecordInfoResponse", mReleaseRecordInfoResponse);
		}
		releasedSellNextFragment.setArguments(args);
		releasedSellNextFragment.tag = ReleasedSellNextFragment.class.getName();
		releasedSellNextFragment.setCustomAnimations(R.anim.anim_fragment_in,
				R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		releasedSellNextFragment.setContainerId(R.id.main_container);
		releasedSellNextFragment.setManager(getFragmentManager());

		releasedSellNextFragment
				.setSelectListener(new SelectListener<Object>() {

					@Override
					public void onSelected(Object t) {
						if (getSelectListener() != null) {
							notifySelected(null);
						} else {
							remove();
						}
					}

					@Override
					public void onCanceled() {
					}
				});
		releasedSellNextFragment.show(SHOW_ADD_HIDE);
	}

	@UiThread
	public void onReleasedSellError(String e) {
		DialogBuilder.showSimpleDialog(e, getBackOpActivity());

	}

	@Click(R.id.sell_area_name)
	@UiThread
	public void sellAreaNameSelect() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		ManyiAnalysis.onEvent(this.getActivity(), "AreaNameOnSellPublishClick");

		showSearchFragment();

	}

	private void showSearchFragment() {
		SearchLivingAreaFragment searchFragment = GeneratedClassUtils.getInstance(SearchLivingAreaFragment.class);
		searchFragment.tag = SearchLivingAreaFragment.class.getName();
		searchFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		searchFragment.setContainerId(R.id.main_container);
		searchFragment.setManager(getFragmentManager());
        Bundle bundle = new Bundle();
        bundle.putInt("searchType",2);
        searchFragment.setArguments(bundle);
		searchFragment.setSelectListener(new SelectListener<Object>() {
			@Override
			public void onSelected(Object t) {
				mEstate = (Estate) t;
				if (mEstate != null) {
					isCheckedAreaName = true;
					if(!mEstate.getEstateName().equals(mSellAreaName.getText().toString()) && (mReleaseRecordInfoResponse == null || isCheckedAreaName)){//如果小区名称改变， 则将楼栋号清空
						mSellSeatNumber.setText("");
					}
					mSellAreaName.setText(mEstate.getEstateName());
					checkSubEstate(mEstate.getEstateId());
				}
			}

			@Override
			public void onCanceled() {
			}
			
		});
		searchFragment.show(SuperFragment.SHOW_ADD_HIDE);		

	}
	@Background
	public void checkSubEstate(int subEstateId){
		SubEstateStateRequest request = new SubEstateStateRequest();
		request.setSubEstateId(subEstateId);
		try {
			mCheckedResponse = mSubEstateService.checkSellSubEstate(request);
			setBuildingUI(mCheckedResponse);
		} catch (Exception e) {
			processException();
		}
	}
	
	@UiThread
	void processException(){
		mSellSingleBuilding.setVisibility(View.VISIBLE);
		mSellSingleBuilding.setChecked(false);
		mSellSingleBuilding.setClickable(true);
		
		mSellSeatNumber.setVisibility(View.VISIBLE);
		mSellSeatNumber.setFocusable(true);
		mSellSeatNumber.setFocusableInTouchMode(true);
		mSellSeatNumber.requestFocus();
		if (cityId == Constants.ID_CITY_BEIJING) {
			mUnitNumberLayout.setVisibility(View.VISIBLE);
			mUnitPrompt.setVisibility(View.VISIBLE);
		}	
	}
	@UiThread
	void setBuildingUI(SubEstateStateResponse response){
		if (response.getErrorCode() == 0) {
			switch (response.getLockStatus()) {
			case SubEstateStateResponse.LOCKED://锁定且不是独栋
				mSellSingleBuilding.setVisibility(View.GONE);
				mSellSeatNumber.setVisibility(View.VISIBLE);
				mSellSeatNumber.setFocusable(false);
				mSellSeatNumber.setFocusableInTouchMode(false);
				if (cityId == Constants.ID_CITY_BEIJING) {
					mUnitNumberLayout.setVisibility(View.GONE);
					mUnitPrompt.setVisibility(View.GONE);
				}
				if((mReleaseRecordInfoResponse != null && !isCheckedAreaName)){
					mSellSeatNumber.setText("");
				}
				break;
			case SubEstateStateResponse.NOTLOCKED://没锁定
				if(mReleaseRecordInfoResponse == null || isCheckedAreaName){
					mSellSingleBuilding.setVisibility(View.VISIBLE);
					mSellSingleBuilding.setChecked(false);
					mSellSingleBuilding.setClickable(true);
					
					mSellSeatNumber.setVisibility(View.VISIBLE);
					mSellSeatNumber.setFocusable(true);
					mSellSeatNumber.setFocusableInTouchMode(true);
					mSellSeatNumber.requestFocus();
					if (cityId == Constants.ID_CITY_BEIJING) {
						mUnitNumberLayout.setVisibility(View.VISIBLE);
						mUnitPrompt.setVisibility(View.VISIBLE);
					}	
				}
				break;
			case SubEstateStateResponse.LOCKEDANDDUDONG://锁定且独栋
				mSellSingleBuilding.setVisibility(View.VISIBLE);
				mSellSeatNumber.setVisibility(View.GONE);
				if (cityId == Constants.ID_CITY_BEIJING) {
					mSellSingleBuilding.setText("小区单栋");
				}else {
					mSellSingleBuilding.setText("独栋");
				}
				mSellSingleBuilding.setChecked(true);
				mSellSingleBuilding.setClickable(false);
				if (cityId == Constants.ID_CITY_BEIJING) {
					mUnitNumberLayout.setVisibility(View.GONE);
					mUnitPrompt.setVisibility(View.GONE);
				}
				if((mReleaseRecordInfoResponse != null && !isCheckedAreaName)){
					mSellSeatNumber.setText("");
				}
				break;

			default:
				break;
			}
		}
	}
	@Click(R.id.released_sell_back)
	public void sellBack() {

		if (CheckDoubleClick.isFastDoubleClick())
			return;
		ManyiUtils.closeKeyBoard(getActivity(), mSellRoomNO);
		remove();
	}

	public void errorDialog(String str) {
		DialogBuilder.showSimpleDialog(str, getBackOpActivity());
	}
	
	@Click(R.id.goto_estate_feedback)
	public void gotoEstateFeedback(){
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		if (CheckDoubleClick.isFastDoubleClick()) return;
		EstateFeedbackFragment estateFeedbackFragment = GeneratedClassUtils.getInstance(EstateFeedbackFragment.class);
		estateFeedbackFragment.tag = EstateFeedbackFragment.class.getName();
		estateFeedbackFragment.setManager(getFragmentManager());
		estateFeedbackFragment.setContainerId(R.id.main_container);
		estateFeedbackFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
				R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);

		estateFeedbackFragment.show(SHOW_ADD_HIDE);
	}
	
	@Click(R.id.show_sell_rule_content)
	public void showRentRule(){
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		RuleContentFragment ruleContentFragment = GeneratedClassUtils.getInstance(RuleContentFragment.class);
		Bundle argsBundle = new Bundle();
		argsBundle.putInt("type", Constants.GOTO_SELL_CONTENT);
		ruleContentFragment.setArguments(argsBundle);
		ruleContentFragment.tag = RuleContentFragment.class.getName();
		ruleContentFragment.setManager(getFragmentManager());
		ruleContentFragment.setContainerId(R.id.main_container);
		ruleContentFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
				R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);

		ruleContentFragment.show(SHOW_ADD_HIDE);
	}

}
