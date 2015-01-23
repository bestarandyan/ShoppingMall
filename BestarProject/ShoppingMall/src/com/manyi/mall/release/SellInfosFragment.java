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
import android.view.View;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.StringUtil;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.release.SellInfoRequest;
import com.manyi.mall.cachebean.release.SellInfoResponse;
import com.manyi.mall.service.SellService;

@SuppressLint("SimpleDateFormat")
@EFragment(R.layout.fragment_sell_infos)
public class SellInfosFragment extends SuperFragment<Integer>{

	@ViewById(R.id.area_plate_infos)
	TextView mSellAreaPlateInfos; // 区域板块

	@ViewById(R.id.sell_area_info)
	TextView mSellAreaInfos; // 小区名称

	@ViewById(R.id.building_infos)
	TextView mSellBuildingInfos; // 栋座

	@ViewById(R.id.sell_price)
	TextView mSellPriceInfos; // 到手价

	@ViewById(R.id.sell_room_type_infos)
	TextView mSellRoomTypeInfos; // 房型

	@ViewById(R.id.sell_space_area)
	TextView mSellSpaceAreaInfos; // 面积

	@ViewById(R.id.sell_info_back)
	TextView mSellInfoBack;
	
	@ViewById(R.id.sell_release_time)
	TextView mSellReleaseTime;
	
	@ViewById
	View getTaskBtn;
	
	private SellService mSellService;
	private SellInfoResponse mInfosResponse;
	public int mTaskState = 0;
	@FragmentArg
	public int houseId;
	public boolean mFlag;
	Dialog mDialog = null;
	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
	@AfterViews
	public void sellInfo() {
		if (!mFlag)
			infoSellRequest();
	}

	/**
	 * 访问接口获取详情
	 */
	@Background
	public void infoSellRequest() {
		try {
			SellInfoRequest infosRequest = new SellInfoRequest();
			infosRequest.setHouseId(houseId);
			mInfosResponse = mSellService.sellInfos(infosRequest);
			sellInfosSet();
		} catch (Exception e) {
			throw e;
		}

	}
	
	/**
	 * 详情设置
	 */
	@UiThread
	public void sellInfosSet() {
		if(mInfosResponse!=null){
			if(mInfosResponse.getTownName()== null ){
				DialogBuilder.showSimpleDialog("网络异常,请稍后再试!", getBackOpActivity());
			}else if(mInfosResponse.getTownName().toString().length()== 0){
				DialogBuilder.showSimpleDialog("网络异常,请稍后再试!", getBackOpActivity());
			}else{
				mSellAreaPlateInfos.setText(mInfosResponse.getCityName()+" - "+mInfosResponse.getTownName());
			}
			
			if(!TextUtils.isEmpty(mInfosResponse.getEstateName()) && !TextUtils.isEmpty(mInfosResponse.getSubEstateName())){
				mSellAreaInfos.setText(mInfosResponse.getEstateName().toString() +"  "+ mInfosResponse.getSubEstateName().toString());
			}else if(!TextUtils.isEmpty(mInfosResponse.getEstateName())){
				mSellAreaInfos.setText(mInfosResponse.getEstateName().toString());
			}
			
			if(!TextUtils.isEmpty(mInfosResponse.getBuildingNameStr())){
				mSellBuildingInfos.setText(mInfosResponse.getBuildingNameStr() +" · "+ mInfosResponse.getRoom().toString()+"室");
			}else{
				mSellBuildingInfos.setText(mInfosResponse.getRoom().toString()+"室");
			}
			if(mInfosResponse.getSellPrice()!=null){
				BigDecimal sellPriceStr = mInfosResponse.getSellPrice();
				double sellPrice = 0.00;
				if (sellPriceStr != null) {
					sellPrice = Double.parseDouble(sellPriceStr + "");
				}
				String price = StringUtil.fromatString(sellPrice);
				mSellPriceInfos.setText(price + "万");
			}
			mSellRoomTypeInfos.setText(mInfosResponse.getBedroomSum() + "室" + mInfosResponse.getLivingRoomSum() + "厅"
					+ mInfosResponse.getWcSum() + "卫");
			if(mInfosResponse.getSpaceArea()!=null){
				BigDecimal spaceAreaStr = mInfosResponse.getSpaceArea();
				double dSpaceAre = 0.00;
				if (spaceAreaStr != null) {
					dSpaceAre = Double.parseDouble(spaceAreaStr + "");
				}
				String space = StringUtil.fromatString(dSpaceAre);
				mSellSpaceAreaInfos.setText(space + "平米");
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(mInfosResponse.getPublishDate()!= null){
				String date = sdf.format(mInfosResponse.getPublishDate());
				mSellReleaseTime.setText(getString(R.string.release_time, date));
			}
		}
	}
	
	
	/**
	 * 发布失败的返回值
	 * @param e
	 * @author bestar
	 */
	@UiThread
	public void onReleasedSellNextError(String e) {
		if (this.getActivity() == null || this.getActivity().isFinishing()) {
			return;
		}
		DialogBuilder.showSimpleDialog(e, getBackOpActivity());
	}


	@Click(R.id.sell_info_back)
	public void sellInfoBack() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		 	remove();
	}
	
}
