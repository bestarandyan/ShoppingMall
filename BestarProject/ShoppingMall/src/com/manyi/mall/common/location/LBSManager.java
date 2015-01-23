package com.manyi.mall.common.location;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.location.Location;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.huoqiu.framework.app.AppConfig;
import com.huoqiu.framework.util.StringUtil;

public class LBSManager {
	public static LBSManager getInstance() {
		if (mLBSManager == null) {
			mLBSManager = new LBSManager();
		}
		return mLBSManager;
	}

	private static LBSManager mLBSManager; 
	private LocationClient mBDLocationClient;

	private LBSManager() {
		initBaiduClient();
	}

	private void initBaiduClient() {
		mBDLocationClient = new LocationClient(AppConfig.application);
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("gcj02");
		option.setOpenGps(false);
		option.setScanSpan(5000); 
		option.setIsNeedAddress(true);
		option.setTimeOut(2000);
		mBDLocationClient.setLocOption(option);
	}

	public  void startBaidu(BDLocationListener mBDLocationListener) {
		if (mBDLocationClient == null) {
			initBaiduClient();
		}
		if (!mBDLocationClient.isStarted()) {
			mBDLocationClient.start();
			mBDLocationClient.registerLocationListener(mBDLocationListener);
			mBDLocationClient.requestLocation();
		}
	}
	
	public void stopBaidu(){
		if(mBDLocationClient!=null)
			mBDLocationClient.stop();
	}
	
	@SuppressLint("SimpleDateFormat")
	private Location converBDToLocation(BDLocation bdLocation) {
		Location location = null;
		if (bdLocation != null) {
			int loc_result = bdLocation.getLocType();
			switch (loc_result) {
			case BDLocation.TypeCriteriaException:
			case BDLocation.TypeNetWorkException:
			case 162:
			case 163:
			case 164:
			case 165:
			case 166:
			case BDLocation.TypeOffLineLocation:
			case BDLocation.TypeOffLineLocationFail:
			case BDLocation.TypeOffLineLocationNetworkFail:
			case BDLocation.TypeServerError:
				return null;
			default:
				break;
			}
			location = new Location("baidu");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
			Date date = LBSHelper.getCurrentDate();
			if (!StringUtil.isEmptyOrNull(bdLocation.getTime())) {
				try {
					date = dateFormat.parse(bdLocation.getTime());
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (date != null) {
				location.setTime(date.getTime());
			} else {
				location.setTime(System.currentTimeMillis());
			}
			if (bdLocation.hasRadius()) {
				location.setAccuracy(bdLocation.getRadius());
			}
			location.setLatitude(bdLocation.getLatitude());
			location.setLongitude(bdLocation.getLongitude());
		}
		return location;
	}

}
