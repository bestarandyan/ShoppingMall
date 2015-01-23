package com.manyi.mall.cachebean.search;

import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.manyi.mall.common.util.DBUtil;
import com.manyi.mall.provider.contract.CityContract;

public class CityUtil {

	static HashMap<String, List<City>> cityMap = new HashMap<String, List<City>>();

	public static List<City> getCityList(Context context, String tag) {
		List<City> cityList = cityMap.get(CityContract.TABLE_NAME);
		if (cityList == null) {
			cityList = DBUtil.getInstance().getCityList(context);
			cityMap.put(CityContract.TABLE_NAME, cityList);
		}
		return cityList;
	}

}
