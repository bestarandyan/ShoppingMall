/**
 *
 */
package com.manyi.mall.common.location;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

/**
 * @author bestar
 */
@SuppressLint("SimpleDateFormat")
public class ManyiLoacationMannger {
    private final static String TAG = ManyiLoacationMannger.class.getSimpleName();
    private LocationClient mLocationClient = null;
    private BDLocationListener mBdLocationListener = new MyLocationListener();
    private OnLocationReceivedListener mOnLocationReceivedListener;
    private final long INTERVAL_TIME = 2 * 60 * 1000;//两次定位的间隔时间
    private double LOCATION_FAILED = 4.9E-324;

    public ManyiLoacationMannger(Context context, int time) {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(context);
        }

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(time);
        option.setIsNeedAddress(true);
        option.setNeedDeviceDirect(true);
        option.setTimeOut(12 * 1000);
        mLocationClient.setLocOption(option);
    }

    public void start() {
        if (ManyiLocation.getInstant().getCurrentTime() != null) {//全局的定位时间间隔控制，
            long diff = System.currentTimeMillis() - ManyiLocation.getInstant().getCurrentTime();
            if (diff < INTERVAL_TIME && ManyiLocation.getInstant().getCurrentTime() != null
                    && mOnLocationReceivedListener != null) {
                mOnLocationReceivedListener.onReceiveLocation(ManyiLocation.getInstant());
                return;
            }
        }
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        mLocationClient.registerLocationListener(mBdLocationListener);
        mLocationClient.requestLocation();
    }

    public void stop() {
        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(mBdLocationListener);
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
        	if (location == null || location.getLatitude() == LOCATION_FAILED || location.getLongitude() == LOCATION_FAILED){
				mOnLocationReceivedListener.onFailedLocation("定位失败");
				return;
			}
            if (mOnLocationReceivedListener == null){
            	return;
            }

            ManyiLocation.getInstant().setAddress(location.getAddrStr());
            ManyiLocation.getInstant().setLatitude(location.getLatitude());
            ManyiLocation.getInstant().setLongitude(location.getLongitude());
            ManyiLocation.getInstant().setProvince(location.getProvince());
            ManyiLocation.getInstant().setCity(location.getCity());
            ManyiLocation.getInstant().setDistrict(location.getDistrict());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date;
            try {
                date = df.parse(location.getTime());
                ManyiLocation.getInstant().setCurrentTime(date.getTime());
                mOnLocationReceivedListener.onReceiveLocation(ManyiLocation.getInstant());
            } catch (Exception e) {
                ManyiLocation.getInstant().setCurrentTime(System.currentTimeMillis());
                e.printStackTrace();
            }

            stop();
        }

    }

    public void setOnLocationReceivedListener(OnLocationReceivedListener listener) {
        mOnLocationReceivedListener = listener;
    }

    public interface OnLocationReceivedListener {
        public void onReceiveLocation(ManyiLocation location);
        
        public void onFailedLocation(String msg);
    }

    public static double distanceTo(String startLatitude, String startLongitude, String endLatitude, String endLongitude) {
        if (startLatitude == null || startLongitude == null || endLatitude == null || endLongitude == null) {
            Log.e(TAG, "Illeagal arguments in position point");
            return 0.0;
        }
        try {
            double startLat = Double.parseDouble(startLatitude);
            double startLon = Double.parseDouble(startLongitude);
            double endLat = Double.parseDouble(endLatitude);
            double endLon = Double.parseDouble(endLongitude);
            LatLng startLatLng = new LatLng(startLat, startLon);
            LatLng endLatLng = new LatLng(endLat, endLon);
            return DistanceUtil.getDistance(startLatLng, endLatLng);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Arguments format error.");
            return 0.0;
        }
    }

}
