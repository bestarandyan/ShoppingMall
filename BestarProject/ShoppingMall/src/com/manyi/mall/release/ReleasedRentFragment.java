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
import com.manyi.mall.cachebean.search.SubEstateStateRequest;
import com.manyi.mall.cachebean.search.SubEstateStateResponse;
import com.manyi.mall.cachebean.search.SearchRespose.Estate;
import com.manyi.mall.common.Constants;
import com.manyi.mall.search.SearchBuildingFragment;
import com.manyi.mall.search.SearchLivingAreaFragment;
import com.manyi.mall.service.CommonService;
import com.manyi.mall.service.EstateService;
import com.manyi.mall.user.EstateFeedbackFragment;
import com.manyi.mall.user.RuleContentFragment;

@SuppressLint("DefaultLocale")
@EFragment((R.layout.fragment_release_rent))
public class ReleasedRentFragment extends SuperFragment<Object> {

	@ViewById(R.id.rent_area_name)
	public TextView mRentAreaName;

	@ViewById(R.id.rent_seat_number)
	public EditText mRentSeatNumber;

	@ViewById(R.id.rent_room_no)
	public EditText mRentRoomNO;

	@ViewById(R.id.rent_unit_no)
	public EditText mRentUnitNo;

	@ViewById(R.id.room_no)
	public TextView mRoomNoDescribe;
	
	@ViewById(R.id.rent_single_building)
	public CheckBox mRentSingleBuilding;

	@ViewById(R.id.rent_not_unit)
	public CheckBox mRentNotUnit;
	
	@ViewById(R.id.rent_villa)
	public CheckBox mRentVilla;

	@ViewById(R.id.rent_unit_number_layout)
	public LinearLayout mRentUnitNumberLayout;

	@ViewById(R.id.rent_building_layout)
	public LinearLayout mRentBuildingLayout;

	@ViewById(R.id.rent_building_prompt)
	public LinearLayout mRentBuildingPrompt;

	@ViewById(R.id.rent_unit_prompt)
	public LinearLayout mRentUnitPrompt;

	public String mRentBuilding;
	public String mRentUnitNumber;
	// room number
	public String mRentRoomNumber;
	private Estate mEstate;
	private BuildingResponse mBuildingResponse;
	private SubEstateStateResponse mCheckedResponse ;
	private int cityId = 0;
	private CommonService mCommonService;
	private EstateService mSubEstateService;
	private boolean isCheckedAreaName = false;//判断是否选择过小区名称

	@FragmentArg
	RentAndSellReleaseRecordInfoResponse mReleaseRecordInfoResponse;
	

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mEstate != null) {
			mRentAreaName.setText(mEstate.getEstateName());
		}
	}

	@AfterViews
	public void initUnit() {
		cityId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("cityId", 0);
		switch (cityId) {
		case Constants.ID_CITY_SHANGHAI:
			initViewShangHai();
			break;
		case Constants.ID_CITY_BEIJING:
			initViewForBeijing();
			break;
		default:
			initViewGuangZhouOrOther();
			break;
		}
		mRentUnitNo.setClickable(false);
	}
	
	
	public void initViewForBeijing(){
		//北京
		mRentSeatNumber.addTextChangedListener(new TextWatcher() {
			 String seatNumberText = "";
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (seatNumberText.equals(s.toString())) {
				     return;
				    }
				  String newText = s.toString().replace("-", "");
				  if (newText.equals(seatNumberText)) {
					  mRentSeatNumber.setText(newText);
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
		
		
		mRentUnitNumberLayout.setVisibility(View.VISIBLE);
		mRentUnitPrompt.setVisibility(View.VISIBLE);
		mRentBuildingPrompt.setVisibility(View.VISIBLE);
		mRentSingleBuilding.setText("小区单栋");
		mRoomNoDescribe.setText("门牌号");

		if (mReleaseRecordInfoResponse != null) {

			 if (!TextUtils.isEmpty(mReleaseRecordInfoResponse.getEstateName().toString())
					&& !TextUtils.isEmpty(mReleaseRecordInfoResponse.getSubEstateName().toString())) {
				mRentAreaName.setText(mReleaseRecordInfoResponse.getEstateName().toString()+ "  "+ mReleaseRecordInfoResponse.getSubEstateName().toString());
			}else if (!TextUtils.isEmpty(mReleaseRecordInfoResponse.getEstateName().toString())) {
				mRentAreaName.setText(mReleaseRecordInfoResponse.getEstateName().toString());
			}
			String mBuilding = mReleaseRecordInfoResponse.getBuilding();
			if(mBuilding!=null && mBuilding.equals("0")){
				mRentSingleBuilding.setChecked(true);
				mRentNotUnit.setChecked(true);
			}else if(mBuilding.contains("-")){
				String[] newBuilding = mBuilding.split("-");
				String str1 = newBuilding[0];
				String str2 = newBuilding[1];
				String num1 = String.valueOf(str1);
				String num2 = String.valueOf(str2);
				if (num1.equals("0")) {
					mRentSingleBuilding.setChecked(true);
				}else {
					mRentSeatNumber.setText(num1);
				}
				if (num2.equals("0")) {
					mRentNotUnit.setChecked(true);
				}else {
					mRentUnitNo.setText(num2);
				}

			}
			String rentRoomNumber = mReleaseRecordInfoResponse.getRoom();
			if (rentRoomNumber.equals("0000")) {
				mRentVilla.setChecked(true);
			} else {
				mRentRoomNO.setText(rentRoomNumber);
			}
			checkSubEstate(mReleaseRecordInfoResponse.getSubEstateId());
		}
	
	}
	
	public void initViewShangHai(){
		mRentUnitNumberLayout.setVisibility(View.GONE);
		mRentSingleBuilding.setVisibility(View.VISIBLE);
		mRentSingleBuilding.setText("独栋");
		mRentUnitPrompt.setVisibility(View.GONE);
		mRoomNoDescribe.setText("室号");
		mRentBuildingPrompt.setVisibility(View.GONE);
		if (mReleaseRecordInfoResponse != null) {
			if (!TextUtils.isEmpty(mReleaseRecordInfoResponse.getEstateName().toString())
					&& !TextUtils.isEmpty(mReleaseRecordInfoResponse.getSubEstateName().toString())) {
				mRentAreaName.setText(mReleaseRecordInfoResponse.getEstateName().toString()
								+ "  "
								+ mReleaseRecordInfoResponse.getSubEstateName()
										.toString());
			}else if (!TextUtils.isEmpty(mReleaseRecordInfoResponse.getEstateName().toString())) {
				mRentAreaName.setText(mReleaseRecordInfoResponse.getEstateName().toString());
			}

			String mBuilding = mReleaseRecordInfoResponse.getBuilding().toString();
			if ("0".equals(mBuilding)) {
				mRentSingleBuilding.setChecked(true);
			} else {
				mRentSeatNumber.setText(mBuilding);
			}
			String rentRoomNumber = mReleaseRecordInfoResponse.getRoom();
			if (rentRoomNumber.equals("0000")) {
				mRentVilla.setChecked(true);
			} else {
				mRentRoomNO.setText(rentRoomNumber);
			}
			checkSubEstate(mReleaseRecordInfoResponse.getSubEstateId());
		}
	}
	public void initViewGuangZhouOrOther(){
		mRentUnitNumberLayout.setVisibility(View.GONE);
		mRentSingleBuilding.setVisibility(View.VISIBLE);
		mRentSingleBuilding.setText("独栋");
		mRentUnitPrompt.setVisibility(View.GONE);
		mRoomNoDescribe.setText("室号");
		mRentBuildingPrompt.setVisibility(View.GONE);
		if (mReleaseRecordInfoResponse != null) {
			if (!TextUtils.isEmpty(mReleaseRecordInfoResponse.getEstateName().toString())
					&& !TextUtils.isEmpty(mReleaseRecordInfoResponse.getSubEstateName().toString())) {
				mRentAreaName.setText(mReleaseRecordInfoResponse.getEstateName().toString()
						+ "  "
						+ mReleaseRecordInfoResponse.getSubEstateName()
						.toString());
			}else if (!TextUtils.isEmpty(mReleaseRecordInfoResponse.getEstateName().toString())) {
				mRentAreaName.setText(mReleaseRecordInfoResponse.getEstateName().toString());
			}
			
			String mBuilding = mReleaseRecordInfoResponse.getBuilding().toString();
			if ("0".equals(mBuilding)) {
				mRentSingleBuilding.setChecked(true);
			} else {
				mRentSeatNumber.setText(mBuilding);
			}
			String rentRoomNumber = mReleaseRecordInfoResponse.getRoom();
			if (rentRoomNumber.equals("0000")) {
				mRentVilla.setChecked(true);
			} else {
				mRentRoomNO.setText(rentRoomNumber);
			}
			checkSubEstate(mReleaseRecordInfoResponse.getSubEstateId());
		}
	}
	

	/**
	 * 字段检索
	 * 
	 * @return
	 */
	private boolean isGetData() {
		if (mRentSeatNumber.getText().toString().trim().length() == 0
				&& !mRentSingleBuilding.isChecked()) {
			showDialog(getString(R.string.dialog_msg_seat));
			return false;
		} else if (cityId == Constants.ID_CITY_BEIJING
				&& mCheckedResponse!=null && mCheckedResponse.getLockStatus()!= 0 
				&& mCheckedResponse.getLockStatus() == SubEstateStateResponse.NOTLOCKED
				&& (mRentUnitNumber == null || mRentUnitNumber.toString().trim().length() == 0)) {
			showDialog(getString(R.string.dialog_msg_util));
			return false;
		} else if (TextUtils.isEmpty(mRentRoomNumber) || TextUtils.isEmpty(mRentRoomNumber.trim())) {
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

	@Click(R.id.released_rent_next)
	@Background
	public void releasedRentNext() {
		if (CheckDoubleClick.isFastDoubleClick())return;
		ManyiUtils.closeKeyBoard(getActivity(), mRentRoomNO);
		if (!isGetData()) return;
		ManyiAnalysis.onEvent(getActivity(), "NextOnRentPublishClick");
		try {
			CommonReleasedFirstRequest mFirstRequest = new CommonReleasedFirstRequest();
			if (mEstate == null && mReleaseRecordInfoResponse!= null) {
					mFirstRequest.setSubEstateId(mReleaseRecordInfoResponse.getSubEstateId());
			} else {
				mFirstRequest.setSubEstateId(mEstate == null ? 0 : mEstate.getEstateId());
			}
			if(mCheckedResponse!=null && mCheckedResponse.getLockStatus() == SubEstateStateResponse.NOTLOCKED){
				if (!TextUtils.isEmpty(mRentBuilding) && !TextUtils.isEmpty(mRentUnitNumber)) {
					mFirstRequest.setBuilding(mRentBuilding.toString().toUpperCase()+ "-"+ mRentUnitNumber.toString().toUpperCase());
				} else if (!TextUtils.isEmpty(mRentBuilding)) {
					mFirstRequest.setBuilding(mRentBuilding.toString().toUpperCase());
				}
			}else if(mCheckedResponse!=null && mCheckedResponse.getLockStatus() == SubEstateStateResponse.LOCKEDANDDUDONG){
				mFirstRequest.setBuilding("0");
			}else {//锁定但不是独栋
				if(mBuildingResponse!= null){
					mFirstRequest.setBuilding(mBuildingResponse.getBuildingName());
				}else {
					if (!TextUtils.isEmpty(mRentBuilding) && !TextUtils.isEmpty(mRentUnitNumber)) {
						mFirstRequest.setBuilding(mRentBuilding.toString().toUpperCase()+ "-" + mRentUnitNumber.toString().toUpperCase());
					} else if (!TextUtils.isEmpty(mRentBuilding)) {
						mFirstRequest.setBuilding(mRentBuilding.toString().toUpperCase());
					}
				}
			}
			
			
			mFirstRequest.setRoom(mRentRoomNumber.trim().toUpperCase());
			mFirstRequest.setHouseType(1);
			CommonReleasedFirstResponse response = mCommonService.commonReleased2(mFirstRequest);
			if(response!=null && response.getErrorCode()==0){
				onReleasedRentNextSuccess(response);
			}else {
				releasedFailed("提交失败！");
			}
		} catch (RestException e) {
			onReleasedRentError(e.getMessage());
		}
	}
	@UiThread
	void releasedFailed(String message){
		DialogBuilder.showSimpleDialog(message, getActivity());
	}
	@CheckedChange(R.id.rent_single_building)
	public void buildingCheckChange(CompoundButton buttonView, boolean isChecked) {
		ManyiAnalysis.onEvent(getActivity(), "SingleHoseOnRentPublishClick");
		if (isChecked) {
			mRentBuilding = "0";
			mRentSeatNumber.setVisibility(View.GONE);
		} else {
			mRentBuilding = mRentSeatNumber.getText().toString();
			mRentSeatNumber.setVisibility(View.VISIBLE);
		}
	}

	@CheckedChange(R.id.rent_not_unit)
	public void unitCheckChange(CompoundButton buttonViews, boolean isChecked) {
		ManyiAnalysis.onEvent(getActivity(), "SingleHoseOnRentPublishClick");
		if (isChecked) {
			mRentUnitNumber = "0";
			mRentUnitNo.setVisibility(View.GONE);
		} else {
			mRentUnitNumber = mRentUnitNo.getText().toString();
			mRentUnitNo.setVisibility(View.VISIBLE);
		}
	}
	
	@CheckedChange(R.id.rent_villa)
	public void villaCheckChange(CompoundButton buttonViews, boolean isChecked) {
		ManyiAnalysis.onEvent(getActivity(), "VillaOnRentPublishClick");
		if (isChecked) {
			mRentRoomNumber = "0000";
			mRentRoomNO.setVisibility(View.GONE);
		} else {
			mRentRoomNumber = mRentRoomNO.getText().toString();
			mRentRoomNO.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 栋座
	 */
	@TextChange(R.id.rent_seat_number)
	public void seatNumberChange() {
		mRentBuilding = mRentSeatNumber.getText().toString();
		ManyiAnalysis.onEvent(this.getActivity(),"HouseNumberOnRentPublishClick");
	}
	
	@Click(R.id.rent_seat_number)
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
		if(mEstate!= null && mEstate.getEstateId()!= 0){
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
					mRentSeatNumber.setText(mBuildingResponse.getBuildingNameStr());
				}
			}

			@Override
			public void onCanceled() {
			}

		});
		searchFragment.show(SuperFragment.SHOW_ADD_HIDE);

	}

	/**
	 * 单元
	 */
	@TextChange(R.id.rent_unit_no)
	public void unitNoChange() {
		mRentUnitNumber = mRentUnitNo.getText().toString();
		ManyiAnalysis.onEvent(this.getActivity(),
				"HouseNumberOnRentPublishClick");
	}

	/**
	 * 单元号 
	 */
	// @TextChange(R.id.rent_unit_no)
	// public void unitNumberChange() {
	// mRentUnitNumber = mRentUnitNo.getText().toString();
	// //trim heading zero
	// while (mRentBuilding.length() > 0) {
	// if (mRentUnitNumber.startsWith("0")) {
	// mRentUnitNumber = mRentUnitNumber.substring(1);
	// } else {
	// break;
	// }
	// }
	//
	// ManyiAnalysis.onEvent(this.getActivity(),
	// "HouseNumberOnRentPublishClick");
	// }
	//

	@TextChange(R.id.rent_room_no)
	public void roomNumber() {
		mRentRoomNumber = mRentRoomNO.getText().toString();
		ManyiAnalysis.onEvent(this.getActivity(),"HouseRoomNumberOnRentPublishClick");
	}

	@UiThread
	public void onReleasedRentNextSuccess(CommonReleasedFirstResponse firstResponse) {
		ManyiUtils.closeKeyBoard(getActivity(), mRentRoomNO);

		ReleasedRentNextFragment releasedRentNextFragment = GeneratedClassUtils
				.getInstance(ReleasedRentNextFragment.class);
		Bundle args = new Bundle();
		args.putInt("houseId", firstResponse.getHouseId());
		args.putString("token", firstResponse.getToken());
		if (mReleaseRecordInfoResponse != null) {
			args.putSerializable("mRecordInfoResponse", mReleaseRecordInfoResponse);
		}
		releasedRentNextFragment.setArguments(args);

		releasedRentNextFragment.tag = ReleasedRentNextFragment.class.getName();

		releasedRentNextFragment.setCustomAnimations(R.anim.anim_fragment_in,
				R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		releasedRentNextFragment.setContainerId(R.id.main_container);
		releasedRentNextFragment.setManager(getFragmentManager());

		releasedRentNextFragment
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
		releasedRentNextFragment.show(SHOW_ADD_HIDE);
	}

	@UiThread
	public void onReleasedRentError(String e) {
		DialogBuilder.showSimpleDialog(e, getBackOpActivity());
	}

	@Click(R.id.rent_area_name)
	@UiThread
	public void rentAreaNameSelect() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}

		ManyiAnalysis.onEvent(this.getActivity(), "AreaNameOnRentPublishClick");

		SearchLivingAreaFragment searchFragment = GeneratedClassUtils
				.getInstance(SearchLivingAreaFragment.class);
		searchFragment.tag = SearchLivingAreaFragment.class.getName();
		searchFragment.setCustomAnimations(R.anim.anim_fragment_in,
				R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		Bundle bundle = new Bundle();
		bundle.putInt("searchType", 1);
		searchFragment.setArguments(bundle);
		searchFragment.setContainerId(R.id.main_container);
		searchFragment.setManager(getFragmentManager());
		searchFragment.setSelectListener(new SelectListener<Object>() {
			@Override
			public void onSelected(Object t) {
				mEstate = (Estate) t;
				if (mEstate != null) {
					isCheckedAreaName = true;
					if(!mEstate.getEstateName().equals(mRentAreaName.getText().toString()) && (mReleaseRecordInfoResponse == null || isCheckedAreaName)){//如果小区名称改变，则将楼栋号清空
						mRentSeatNumber.setText("");
					}
					mRentAreaName.setText(mEstate.getEstateName());
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
		try {
			SubEstateStateRequest request = new SubEstateStateRequest();
			request.setSubEstateId(subEstateId);
			mCheckedResponse = mSubEstateService.checkRentSubEstate(request);
			setBuildingUI();
		} catch (Exception e) {
			processException();
		}
		
	}
	
	@UiThread
	void processException(){
		mRentSingleBuilding.setVisibility(View.VISIBLE);
		mRentSingleBuilding.setChecked(false);
		mRentSingleBuilding.setClickable(true);
		
		mRentSeatNumber.setVisibility(View.VISIBLE);
		mRentSeatNumber.setFocusable(true);
		mRentSeatNumber.setFocusableInTouchMode(true);
		mRentSeatNumber.requestFocus();
		if (cityId == Constants.ID_CITY_BEIJING) {
			mRentUnitNumberLayout.setVisibility(View.VISIBLE);
			mRentUnitPrompt.setVisibility(View.VISIBLE);
		}
		
	}
	@UiThread
	void setBuildingUI(){
		if (mCheckedResponse.getErrorCode() == 0) {
			switch (mCheckedResponse.getLockStatus()) {
			case SubEstateStateResponse.LOCKED://锁定且不是独栋
					mRentSingleBuilding.setVisibility(View.GONE);
					mRentSeatNumber.setVisibility(View.VISIBLE);
					mRentSeatNumber.setFocusable(false);
					mRentSeatNumber.setFocusableInTouchMode(false);
					if (cityId == Constants.ID_CITY_BEIJING) {
						mRentUnitNumberLayout.setVisibility(View.GONE);
						mRentUnitPrompt.setVisibility(View.GONE);
					}
					if((mReleaseRecordInfoResponse != null && !isCheckedAreaName)){
						mRentSeatNumber.setText("");
					}
				break;
			case SubEstateStateResponse.NOTLOCKED://没锁定
				if(mReleaseRecordInfoResponse == null || isCheckedAreaName){
					mRentSingleBuilding.setVisibility(View.VISIBLE);
					mRentSingleBuilding.setChecked(false);
					mRentSingleBuilding.setClickable(true);
					
					mRentSeatNumber.setVisibility(View.VISIBLE);
					mRentSeatNumber.setFocusable(true);
					mRentSeatNumber.setFocusableInTouchMode(true);
					mRentSeatNumber.requestFocus();
					if (cityId == Constants.ID_CITY_BEIJING) {
						mRentUnitNumberLayout.setVisibility(View.VISIBLE);
						mRentUnitPrompt.setVisibility(View.VISIBLE);
					}
				}
				break;
			case SubEstateStateResponse.LOCKEDANDDUDONG://锁定且独栋
			
					mRentSingleBuilding.setVisibility(View.VISIBLE);
					mRentSeatNumber.setVisibility(View.GONE);
					if (cityId == Constants.ID_CITY_BEIJING) {
						mRentSingleBuilding.setText("小区单栋");
					}else {
						mRentSingleBuilding.setText("独栋");
					}
					mRentSingleBuilding.setChecked(true);
					mRentSingleBuilding.setClickable(false);
					if (cityId == Constants.ID_CITY_BEIJING) {
						mRentUnitNumberLayout.setVisibility(View.GONE);
						mRentUnitPrompt.setVisibility(View.GONE);
					}
					
					if((mReleaseRecordInfoResponse != null && !isCheckedAreaName)){
						mRentSeatNumber.setText("");
					}
					break;
				

			default:
				break;
			}
		}
	}

	@Click(R.id.released_rent_back)
	public void releasedRentBack() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		ManyiUtils.closeKeyBoard(getActivity(), mRentRoomNO);
		remove();
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
	
	@Click(R.id.show_rent_rule_content)
	public void showRentRule(){
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		RuleContentFragment ruleContentFragment = GeneratedClassUtils.getInstance(RuleContentFragment.class);
		Bundle argsBundle = new Bundle();
		argsBundle.putInt("type", Constants.GOTO_RENT_CONTENT);
		ruleContentFragment.setArguments(argsBundle);
		ruleContentFragment.tag = RuleContentFragment.class.getName();
		ruleContentFragment.setManager(getFragmentManager());
		ruleContentFragment.setContainerId(R.id.main_container);
		ruleContentFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
				R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);

		ruleContentFragment.show(SHOW_ADD_HIDE);
	}
	
}
