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
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.StringUtil;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.release.RentInfoRequest;
import com.manyi.mall.cachebean.release.RentInfoResponse;
import com.manyi.mall.cachebean.search.GetTaskResponse;
import com.manyi.mall.service.RentService;
import com.manyi.mall.service.SourceLogService;
import com.manyi.mall.service.UcService;
import com.manyi.mall.service.UserTaskService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

@EFragment(R.layout.fragment_rent_infos)
public class RentInfoFragment extends SuperFragment<Integer>{


	@ViewById(R.id.rent_area_plate_infos)
	TextView mRentAreaPlateInfos; // 区域板块

	@ViewById(R.id.rent_info_area)
	TextView mRentArea; // 	小区

	@ViewById(R.id.rent_building_infos)
	TextView mRentBuildingInfos; // 栋座


	@ViewById(R.id.rent_price)
	TextView mRentPriceInfos; // 到手价

	@ViewById(R.id.rent_room_type_infos)
	TextView mRentRoomTypeInfos; // 房型

	@ViewById(R.id.rent_space_area)
	TextView mRentSpaceAreaInfos; // 面积

	@ViewById(R.id.rent_time_infos)
	TextView mRentTimeInfos; // 时间
	
	
	@ViewById(R.id.rent_info_take_picture)
	LinearLayout mRentInfoTakePicture;
	
	@ViewById(R.id.rent_info_image_layout)
	FrameLayout mRentInfoImageLayout;

	
	@ViewById(R.id.rent_info_image)
	ImageView mRentInfoImage;
	
	@ViewById(R.id.rent_info_image_size)
	TextView mRentInfoImageSize;
	
	@ViewById(R.id.rent_release_time)
	TextView mRentReleaseTime;

	RentService mRentService;

	UcService mUserService;

	UserTaskService userTaskService;
	
	@FragmentArg
	public int houseId;
	

	SourceLogService mSourceLogService;

	private RentInfoResponse mInfosResponse;
	
	GetTaskResponse getTaskResponse = null;

	private boolean mFlag;
	public int mTaskState = 0;
	
	Dialog mDialog = null;
	
	protected ImageLoader mImageLoader = ImageLoader.getInstance();

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
			if (!mImageLoader.isInited()) {
				mImageLoader.init(ImageLoaderConfiguration.createDefault(getBackOpActivity()));
		}
	}
	
	@AfterViews
	public void rentInfo() {
		
		if (!mFlag)
			infoRentRequest();
	}

	@SuppressLint("SimpleDateFormat")
	@UiThread
	public void rentGetInfo() {
		mRentAreaPlateInfos.setText(mInfosResponse.getCityName()+" - "+mInfosResponse.getTownName().toString());
		
		
		if(!TextUtils.isEmpty(mInfosResponse.getEstateName()) && !TextUtils.isEmpty(mInfosResponse.getSubEstateName())){
			mRentArea.setText(mInfosResponse.getEstateName().toString() +"  "+ mInfosResponse.getSubEstateName().toString());
		}else if(!TextUtils.isEmpty(mInfosResponse.getEstateName())){
			mRentArea.setText(mInfosResponse.getEstateName().toString());
		}
	
		if(!TextUtils.isEmpty(mInfosResponse.getBuildingNameStr())){
			mRentBuildingInfos.setText(mInfosResponse.getBuildingNameStr() +" · "+ mInfosResponse.getRoom().toString()+"室");
		}else{
			mRentBuildingInfos.setText(mInfosResponse.getRoom().toString()+"室");
		}
		
		if(mInfosResponse.getRentPrice()!= null){
			BigDecimal spacePriceStr = mInfosResponse.getRentPrice();
			double dSpacePrice = 0.00;
			if(spacePriceStr != null){
				dSpacePrice = Double.parseDouble(spacePriceStr+"");
			}
			String spacePrice = StringUtil.fromatString(dSpacePrice);
			mRentPriceInfos.setText(spacePrice + getString(R.string.rent_info_price_yuan));
		}
		
		mRentRoomTypeInfos.setText(mInfosResponse.getBedroomSum() + getString(R.string.rent_info_room) + mInfosResponse.getLivingRoomSum() + getString(R.string.rent_info_ting) + mInfosResponse.getWcSum()
				+ getString(R.string.rent_info_wei));
		
		
		if(mInfosResponse.getSpaceArea()!= null){
			BigDecimal spaceAreaStr = mInfosResponse.getSpaceArea();
			double dSpaceAre = 0.00;
			if(spaceAreaStr != null){
				dSpaceAre = Double.parseDouble(spaceAreaStr+"");
			}
			String space = StringUtil.fromatString(dSpaceAre);
			mRentSpaceAreaInfos.setText(space + getString(R.string.rent_info_square_meters));
		}
		
		
		if(mInfosResponse.getPublishDate()!= null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date = sdf.format(mInfosResponse.getPublishDate());
			mRentTimeInfos.setText("发布时间: "+date);
			mRentReleaseTime.setText(getString(R.string.release_time, date));
		}
		

	}
	

	@Background
	public void infoRentRequest() {
		
		try {

			RentInfoRequest rentInfoRequest = new RentInfoRequest();
			rentInfoRequest.setHouseId(houseId);

			mInfosResponse = mRentService.rentInfo(rentInfoRequest);
			rentGetInfo();
		} catch (RestException e) {
			onRentInfoError(e);
		}
	}

	@UiThread
	public void onRentInfoError(RestException e){
		DialogBuilder.showSimpleDialog(e.getMessage(), getBackOpActivity());
	}
	

	@Click(R.id.rent_info_back)
	public void rentInfoBack() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		remove();
	}

	
}
