package com.manyi.mall.release;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.StringUtil;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.release.RentAndSellReleaseRecordInfoRequest;
import com.manyi.mall.cachebean.release.RentAndSellReleaseRecordInfoResponse;
import com.manyi.mall.common.Constants;
import com.manyi.mall.service.RentService;
import com.manyi.mall.service.SellService;

@SuppressLint("SimpleDateFormat")
@EFragment(R.layout.fragment_released_record)
public class ReleasedRecordInfoFragment extends SuperFragment<Object> {

	@ViewById(R.id.check_state)
	public TextView mCheckState; // 审核中/审核通过

	@ViewById(R.id.failure_reasons)
	public TextView mFailureReasons; // 失败原因

	@ViewById(R.id.record_area_name)
	public TextView mRecordAreaName; // 小区名

	@ViewById(R.id.record_building)
	public TextView mRecordBuilding; // 栋座号

	@ViewById(R.id.record_room)
	public TextView mRecordRoom; // 室号

	@ViewById(R.id.record_owner_info)
	public TextView mRecordOwnerInfo; // 业主姓名

	@ViewById(R.id.record_price_title)
	public TextView mRecprdPriceTitle; // 到手价title

	@ViewById(R.id.record_price)
	public TextView mRecordPrice; // 到手价

	@ViewById(R.id.record_house_type)
	public TextView mRecordHouseType; // 房子类型

	@ViewById(R.id.record_proportion)
	public TextView mRecordProportion; // 面积

	@ViewById(R.id.record_area)
	public TextView mRecordArea; // 区域

	@ViewById(R.id.record_plate)
	public TextView mRecordPlate; // 板块

	@ViewById(R.id.released_record_time)
	public TextView mRecprdTime;

	@ViewById(R.id.record_load_info)
	public LinearLayout mRecordLoadInfo;

	@ViewById(R.id.again_release)
	public TextView mAginRelease; // 重新发布

	@FragmentArg
	public String returnMoney;
	@FragmentArg
	public int historyId;
	@FragmentArg
	public int houseId;
	@FragmentArg
	public int typeId;
	@FragmentArg
	public int estateId;
	@FragmentArg
	public String typeName;
	@FragmentArg
	int releaseRecordType;
	@FragmentArg
	String releaseTypeId;
	
	private RentService mRentService;
	private SellService mSellService;
	RentAndSellReleaseRecordInfoRequest recordInfoRequest = new RentAndSellReleaseRecordInfoRequest();
	RentAndSellReleaseRecordInfoResponse mReleaseRecordInfoResponse;

	@AfterViews
	public void recordInfoLoad() {
		recordInfoRequest();
	}
	@Background
	public void recordInfoRequest() {

		try {
			if (releaseTypeId == Constants.RECORD_INFO_RENT) {
				recordInfoRequest.setHistoryId(historyId);
				mReleaseRecordInfoResponse = mRentService.rentRecordInfo(recordInfoRequest);
			} else if (releaseTypeId == Constants.RECORD_INFO_SELL) {
				recordInfoRequest.setHistoryId(historyId);
				mReleaseRecordInfoResponse = mSellService.sellRecordInfo(recordInfoRequest);
			}
			recordGetInfo();
		} catch (Exception e) {
			onRecordInfoRequestError(e.getMessage());
		}
	}

	@UiThread
	public void onRecordInfoRequestError(String e) {
		if (getActivity() == null || getActivity().isFinishing()) {
			return;
		}
		DialogBuilder.showSimpleDialog(e, getBackOpActivity());
	}

	/**
	 * 重新发布 typeId: 1：发布出售    0：发布出租
	 */
	@Click(R.id.again_release)
	@UiThread
	public void againRelease() {

		switch (typeId) {
		case 1:
			ReleasedSellFragment releasedSellFragment = GeneratedClassUtils.getInstance(ReleasedSellFragment.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("mReleaseRecordInfoResponse",mReleaseRecordInfoResponse);
			releasedSellFragment.setArguments(bundle);
			releasedSellFragment.tag = ReleasedSellFragment.class.getName();
			releasedSellFragment.setCustomAnimations(R.anim.anim_fragment_in,
					R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
					R.anim.anim_fragment_close_out);
			releasedSellFragment.setContainerId(R.id.main_container);
			releasedSellFragment.setManager(getFragmentManager());
			releasedSellFragment.show(SHOW_ADD_HIDE);
			break;
		case 0:
			ReleasedRentFragment releasedRentFragment = GeneratedClassUtils
					.getInstance(ReleasedRentFragment.class);
			Bundle mBundle = new Bundle();
			mBundle.putSerializable("mReleaseRecordInfoResponse",mReleaseRecordInfoResponse);
			releasedRentFragment.setArguments(mBundle);
			releasedRentFragment.tag = ReleasedRentFragment.class.getName();
			releasedRentFragment.setCustomAnimations(R.anim.anim_fragment_in,
					R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
					R.anim.anim_fragment_close_out);

			releasedRentFragment.setContainerId(R.id.main_container);
			releasedRentFragment.setManager(getFragmentManager());

			releasedRentFragment.show(SHOW_ADD_HIDE);
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 */
	@UiThread
	public void recordGetInfo() {

		if (mReleaseRecordInfoResponse != null) {
			mRecordLoadInfo.setVisibility(View.VISIBLE);
			mCheckState.setText(mReleaseRecordInfoResponse.getStateStr());
			if ("审核失败".equals(mReleaseRecordInfoResponse.getStateStr())) {
				if (mReleaseRecordInfoResponse.getNote() == null|| "".equals(mReleaseRecordInfoResponse.getNote())) {
					mFailureReasons.setVisibility(View.GONE);
				} else {
					mFailureReasons.setVisibility(View.VISIBLE);
					mFailureReasons.setText("失败原因:"+ mReleaseRecordInfoResponse.getNote());
				}
				mAginRelease.setVisibility(View.VISIBLE);
			}
			if (!TextUtils.isEmpty(mReleaseRecordInfoResponse.getEstateName().toString())
					&& !TextUtils.isEmpty(mReleaseRecordInfoResponse.getSubEstateName().toString())) {
				mRecordAreaName.setText(mReleaseRecordInfoResponse.getEstateName().toString()+ "  "+ mReleaseRecordInfoResponse.getSubEstateName().toString());
			} else if (!TextUtils.isEmpty(mReleaseRecordInfoResponse.getEstateName().toString())) {
				mRecordAreaName.setText(mReleaseRecordInfoResponse.getEstateName().toString());
			}

			if (isAdded()) {
				if (!TextUtils.isEmpty(mReleaseRecordInfoResponse.getBuildingNameStr())) {
					mRecordBuilding.setText(mReleaseRecordInfoResponse.getBuildingNameStr()
							+ " · "
							+ mReleaseRecordInfoResponse.getRoom()
							+ this.getString(R.string.released_record_room));
				} else {
					mRecordBuilding.setText(mReleaseRecordInfoResponse.getRoom()+ this.getString(R.string.released_record_room));
				}
			}
			String ownerInfo = "";
			if (mReleaseRecordInfoResponse.getHoustConcatList() != null) {
				for (int i = 0; i < mReleaseRecordInfoResponse.getHoustConcatList().size(); i++) {
					String name = mReleaseRecordInfoResponse.getHoustConcatList().get(i).getHostName();
					String mobile = mReleaseRecordInfoResponse.getHoustConcatList().get(i).getHostMobile();
					if (mReleaseRecordInfoResponse.getHoustConcatList().size() > 1) {
						ownerInfo += name + " | " + mobile + "" + "\n";
					} else {
						ownerInfo += name + " | " + mobile + "";
					}
				}
				if (mReleaseRecordInfoResponse.getHoustConcatList().size() > 1) {
					ownerInfo = ownerInfo.substring(0, ownerInfo.length() - 1);
				}
				mRecordOwnerInfo.setText(ownerInfo);
			}
			if (isAdded()) {
				mRecordHouseType.setText(mReleaseRecordInfoResponse.getBedroomSum()
								+ getString(R.string.released_record_house_type_bedroom)
								+ mReleaseRecordInfoResponse.getLivingRoomSum()
								+ getString(R.string.released_record_house_type_livingRoom)
								+ mReleaseRecordInfoResponse.getWcSum()
								+ getString(R.string.released_record_house_type_wc));
			}
			BigDecimal spaceAreaStr = mReleaseRecordInfoResponse.getSpaceArea();
			double dSpaceAre = 0.00;
			if (spaceAreaStr != null) {
				dSpaceAre = Double.parseDouble(spaceAreaStr + "");
			}
			String space = StringUtil.fromatString(dSpaceAre);
			mRecordProportion.setText(space+ getString(R.string.released_record_proportion_meters));

			mRecordArea.setText(mReleaseRecordInfoResponse.getCityName().toString());
			mRecordPlate.setText(mReleaseRecordInfoResponse.getTownName().toString());
			BigDecimal priceStr = mReleaseRecordInfoResponse.getPrice();
			double mPriceStr = 0.00;
			if (priceStr != null) {
				mPriceStr = Double.parseDouble(priceStr + "");
			}
			String mPrice = StringUtil.fromatString(mPriceStr);
			if (mReleaseRecordInfoResponse.getPublishDate() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String date = sdf.format(mReleaseRecordInfoResponse.getPublishDate());
				if (releaseTypeId == Constants.RECORD_INFO_RENT) {
					mRecprdPriceTitle.setText(this.getString(R.string.released_record_rent_price));
					mRecordPrice.setText(mPrice+ getString(R.string.released_record_price_yuan));
					mRecprdTime.setText(date + " 发布出租");
				} else if (releaseTypeId == Constants.RECORD_INFO_SELL) {
					mRecprdPriceTitle.setText(this.getString(R.string.released_record_price));
					mRecordPrice.setText(mPrice+ getString(R.string.released_record_price_wan));
					mRecprdTime.setText(date + " 发布出售");
				}
			}
		}
	}
	@Click(R.id.record_info_back)
	public void recordInfoBack() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		remove();
	}
}
