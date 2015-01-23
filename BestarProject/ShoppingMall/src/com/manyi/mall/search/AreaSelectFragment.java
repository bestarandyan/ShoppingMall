package com.manyi.mall.search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.AreaRequest;
import com.manyi.mall.cachebean.search.AreasResponse;
import com.manyi.mall.cachebean.search.AreasResponse.AreaResponse;
import com.manyi.mall.common.Constants;
import com.manyi.mall.provider.contract.LocalHistoryContract;
import com.manyi.mall.release.RentSearchResultFragment;
import com.manyi.mall.release.SellSearchResultFragment;
import com.manyi.mall.service.CommonService;
import com.peony.listadapter.BindDictionary;
import com.peony.listadapter.ListAdapter;
import com.peony.listadapter.extractors.StringExtractor;

@EFragment(R.layout.fragment_select_base)
public class AreaSelectFragment extends SuperFragment<Object>{
	
	AreasResponse mDistrictAreasResponse;

	public CommonService mCommonService;

	@FragmentArg
	String targetClass;
	
	@ViewById(R.id.district_select_title)
	RelativeLayout mTitleLayout;
	
	@ViewById(R.id.select_listview)
	ListView mDistrictList;
	
	@ViewById(R.id.subdistrict_select_list)
	ListView mSubDistrictList;
	
	@ViewById(R.id.subdistrict_progressbar)
	ProgressBar mSubDistrictProgressBar;
	
	@ViewById(R.id.district_select_back)
	Button mBackButton;
	
	private List<AreaResponse> mDistrictData = new ArrayList<>();
	private BindDictionary<AreaResponse> mDistrictBindDictionary = new BindDictionary<AreaResponse>();
	private ListAdapter<AreaResponse> mDistrictAdapter;
	
	private List<AreaResponse> mSubDistrictData = new ArrayList<>();
	private BindDictionary<AreaResponse> mSubDistrictBindDictionary = new BindDictionary<AreaResponse>();
	private ListAdapter<AreaResponse> mSubDistrictAdapter;
	
	private int mScreenWidth;
	private int mSubDistrictWidth;
	
	private String mDistrictName;
	
	@AfterViews
	public void init() {
		final DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

		mScreenWidth = metrics.widthPixels;
		mSubDistrictWidth = mScreenWidth * 3 /5;
		
		mSubDistrictList.post(new Runnable() {
			@Override
			public void run() {
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mSubDistrictWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
				layoutParams.leftMargin = mScreenWidth - mSubDistrictWidth;
				layoutParams.topMargin = mTitleLayout.getHeight();
				mSubDistrictList.setLayoutParams(layoutParams);
				
				layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = mScreenWidth - mSubDistrictWidth/2;
				layoutParams.topMargin = (metrics.heightPixels - mTitleLayout.getHeight())/2;
				mSubDistrictProgressBar.setLayoutParams(layoutParams);
			}
		});
		
		initBinding();
		
		mDistrictList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mDistrictList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (CheckDoubleClick.isFastDoubleClick()) {
					return;
				}
				mDistrictList.setItemChecked(position, true);
				onDistrictItemClicked(position);
			}
		});
		
		mSubDistrictList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (CheckDoubleClick.isFastDoubleClick()) {
					return;
				}
				onSubDistrictSelected(position, null);
			}
		});
		
		mDistrictName = "";
		
		getData();
	}
	
	private void initBinding() {
		mDistrictBindDictionary.addStringField(R.id.district_name, new StringExtractor<AreaResponse>() {
			@Override
			public String getStringValue(AreaResponse areaResponse, int position) {
				return areaResponse.getName();
			}
		});
		
		mDistrictAdapter = new ListAdapter<AreaResponse>(getActivity(), mDistrictData, R.layout.fragment_district_parent_item, mDistrictBindDictionary);
		
		mSubDistrictBindDictionary.addStringField(R.id.district_name, new StringExtractor<AreaResponse>() {
			@Override
			public String getStringValue(AreaResponse areaResponse, int position) {
				return areaResponse.getName();
			}
		});
		
		mSubDistrictAdapter = new ListAdapter<AreaResponse>(getActivity(), mSubDistrictData, R.layout.fragment_district_child_item, mSubDistrictBindDictionary);
	}

	@Background
	public void getData() {
		try {
			AreaRequest req = new AreaRequest();
			int cityId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("cityId", 0);
			req.setAreaId(cityId);
			mDistrictAreasResponse = mCommonService.getDistrict(req);
			updateDistrict(mDistrictAreasResponse.getAreaList());
		} catch (Exception e) {
			throw e;
		}
	}
	
	@UiThread
	public void updateDistrict(List<AreaResponse> districts) {
		mDistrictData = districts;
		mDistrictAdapter.updateData(districts);
		mDistrictList.setAdapter(mDistrictAdapter);
	}
	
	private void onDistrictItemClicked(int position) {
		AreaResponse area = mDistrictData.get(position);
		mDistrictName = area.getName();
		mSubDistrictProgressBar.setVisibility(View.VISIBLE);
		
		if (area.isFlag()) {
			onSubDistrictSelected(position, area);
		} else {
			getSubDistrictData(area.getAreaId());
		}
	}
	
	@Background
	public void getSubDistrictData(int areaId) {
		try {
			AreaRequest req = new AreaRequest();
			req.setAreaId(areaId);
			AreasResponse areasResponse = mCommonService.getSubDistrict(req);
			mSubDistrictData = areasResponse.getAreaList();
			showSubDistrict(mSubDistrictData);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@UiThread
	public void showSubDistrict(List<AreaResponse> subDistricts) {
		mSubDistrictProgressBar.setVisibility(View.GONE);
		mSubDistrictList.setVisibility(View.VISIBLE);
		mSubDistrictAdapter.updateData(subDistricts);
		mSubDistrictList.setAdapter(mSubDistrictAdapter);
	}
	
	private void onSubDistrictSelected(int position, AreaResponse areaResponse) {
		AreaResponse area;
		if (areaResponse == null) {
			area = mSubDistrictData.get(position);
		} else {
			area = areaResponse;
		}
		
		Cursor cursor = getActivity().getContentResolver().query(LocalHistoryContract.CONTENT_URI, new String[]{LocalHistoryContract._ID, LocalHistoryContract.ESTATE_ID}, 
				LocalHistoryContract.ESTATE_ID + "=?", new String[]{String.valueOf(area.getAreaId())}, null);
		if (cursor.getCount() == 0) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
			ContentValues values = new ContentValues();
			values.put(LocalHistoryContract.ESTATE_ID, area.getAreaId());
			values.put(LocalHistoryContract.DATE, df.format(new Date()));
			values.put(LocalHistoryContract.NAME, mDistrictName + "-" + area.getName());
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
		targetFragment.setContainerId(R.id.main_container);
		targetFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		targetFragment.setSelectListener(new SelectListener<Object>() {
			@Override
			public void onSelected(Object t) {
				if (AreaSelectFragment.this.isDetached() ||getActivity() == null) return;
				if (getSelectListener() != null) notifySelected(null);
			}

			@Override
			public void onCanceled() {
			}
		});
		targetFragment.show(SHOW_ADD_HIDE);
		mSubDistrictProgressBar.setVisibility(View.GONE);
	}
	
	@Click(R.id.district_select_back)
	public void onBack() {
		if (CheckDoubleClick.isFastDoubleClick()) return;
		if (this.isDetached() || getActivity() == null) return;
		
		if (getSelectListener() != null) notifySelected(null);
	}
}
