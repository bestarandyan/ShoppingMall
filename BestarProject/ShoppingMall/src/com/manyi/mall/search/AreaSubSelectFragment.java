package com.manyi.mall.search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.BaseAdapter;

import com.huoqiu.framework.util.GeneratedClassUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.AreaRequest;
import com.manyi.mall.cachebean.search.AreasResponse;
import com.manyi.mall.cachebean.search.AreasResponse.AreaResponse;
import com.manyi.mall.common.Constants;
import com.manyi.mall.provider.contract.LocalHistoryContract;
import com.manyi.mall.release.RentSearchResultFragment;
import com.manyi.mall.release.SellSearchResultFragment;

@EFragment(R.layout.fragment_select_base)
public class AreaSubSelectFragment extends BaseSelectFragment<AreaResponse> {
	AreasResponse mRes;

	@FragmentArg
	int areaId;

	@FragmentArg
	String targetClass;
	
	@FragmentArg
	String title;

	@Background
	@Override
	public void getData() {
		try {
			AreaRequest req = new AreaRequest();
			req.setAreaId(areaId);
			mRes = mCommonService.getDistrict(req);
			notifyListView();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void setOnItemClick(Object resopnse) {
		AreaResponse area = (AreaResponse) resopnse;
		Cursor cursor = getActivity().getContentResolver().query(LocalHistoryContract.CONTENT_URI, new String[]{LocalHistoryContract._ID, LocalHistoryContract.ESTATE_ID}, 
				LocalHistoryContract.ESTATE_ID + "=?", new String[]{String.valueOf(area.getAreaId())}, null);
		if (cursor.getCount() == 0) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
			ContentValues values = new ContentValues();
			values.put(LocalHistoryContract.ESTATE_ID, area.getAreaId());
			values.put(LocalHistoryContract.DATE, df.format(new Date()));
			values.put(LocalHistoryContract.NAME, area.getName());
			int userId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
			values.put(LocalHistoryContract.USER_ID, String.valueOf(userId));
			values.put(LocalHistoryContract.IS_AREA, LocalHistoryContract.AREA_TYPE);
			getActivity().getContentResolver().insert(LocalHistoryContract.CONTENT_URI, values);
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
			ContentValues values = new ContentValues();
			values.put(LocalHistoryContract.DATE, df.format(new Date()));
			getActivity().getContentResolver().update(LocalHistoryContract.CONTENT_URI, values, 
					LocalHistoryContract.ESTATE_ID + "=?", new String[]{String.valueOf(area.getAreaId())});
		}
		cursor.close();
		
		SearchResultFragment targetFragment;
		if (targetClass.equals(SellSearchResultFragment.class.getName())) {
			targetFragment = GeneratedClassUtils.getInstance(SellSearchResultFragment.class);
			targetFragment.tag = SellSearchResultFragment.class.getName();
		} else if (targetClass.equals(RentSearchResultFragment.class.getName())) {
			targetFragment = GeneratedClassUtils.getInstance(RentSearchResultFragment.class);
			targetFragment.tag = RentSearchResultFragment.class.getName();
		} else {
			return;
		}
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("area", area);
		targetFragment.setArguments(bundle);
		targetFragment.setManager(getFragmentManager());
		targetFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		targetFragment.setSelectListener(new SelectListener<Object>() {
			@Override
			public void onSelected(Object t) {
				if (getSelectListener() != null) notifySelected(null);
			}

			@Override
			public void onCanceled() {
			}
		});
		targetFragment.show();
	}

	@Override
	public ArrayList<AreaResponse> setDataList() {
		return (ArrayList<AreaResponse>) mRes.getAreaList();
	}

	@Override
	public BaseAdapter setAdapter() {
		return null;
	}

	@Override
	public String getTitle() {
		return title;
	}

}
