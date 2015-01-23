package com.manyi.mall.common.location;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.location.Location;

import com.manyi.mall.cachebean.search.City;
import com.manyi.mall.common.Constants;
import com.manyi.mall.common.util.DBUtil;


public class LBSHelper {
    private static City mGPSCity;

    public static Date getCurrentDate() {
        return Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai")).getTime();
    }

    private static boolean isCacheTimeOut(Location location) {
        if (location == null) {
            return true;
        }
        if (getCurrentDate().getTime() - location.getTime() >= Constants.DEFAULT_TIME_OUT) {
            return true;
        }
        return false;
    }

    public static boolean isLocationCacheValid(Location localLocation) {
        if (localLocation != null && isRightLocation(localLocation) && !isCacheTimeOut(localLocation)
                && localLocation.getAccuracy() <= 30.0) {
            return true;
        }
        return false;
    }

    /**
     * ��������:( �Ƿ�Ϊ�Ϸ���ַ)
     *
     * @param location
     * @return
     */
    public static boolean isRightLocation(Location location) {
        if (Math.abs(location.getLatitude()) <= 90 && Math.abs(location.getLongitude()) <= 180) {
            return true;
        }
        return false;
    }

    public static City getCityFromCityName(Context context, String cityName) {
        if (cityName != null) {
            ArrayList<City> cityList = DBUtil.getInstance().getCityList(context);
            if (cityList != null && cityList.size() > 0) {
                for (int i = 0; i < cityList.size(); i++) {
                    if (cityName.contains(cityList.get(i).getName())) {
                        return cityList.get(i);
                    }
                }
            }
        }
        return null;
    }

}
