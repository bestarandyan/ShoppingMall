package com.manyi.mall.search;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.manyi.mall.FybaoApplication;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.City;
import com.manyi.mall.cachebean.search.IncrementalCityRequest;
import com.manyi.mall.cachebean.search.IncrementalCityResponce;
import com.manyi.mall.common.util.DBUtil;
import com.manyi.mall.service.CommonService;

@EFragment(R.layout.fragment_city_base)
public class CityBaseFragment extends SuperFragment<City> {

	CommonService commenService;

	@ViewById(R.id.city_select_list)
	ListView mCitySelectList;

	List<City> cityList; // 列表数据
	CityListAdapter adapter; // 列表Adapter

	@AfterViews
	void afterViews() {
		//initVariable();
		// 发送增量服务
		sendCityListService();
		// 刷新数据
		//refreshListView();
		// 注册兼听
		registerListener();
	}

	/**
	 * 条目点击兼听
	 */
	private void registerListener() {
		mCitySelectList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position >= 0 && position < cityList.size()) {
					notifySelected(cityList.get(position));
				}
			}

		});
	}

//	private void initVariable() {
//		cityList = DBUtil.getInstance().getCityList(getActivity());
//	}

	@Background
	void sendCityListService() {
		IncrementalCityRequest req = new IncrementalCityRequest();
		req.setVersion(DBUtil.getInstance().getUserSetting(getActivity(), DBUtil.city_table_version));
		req.setUid(001);

		IncrementalCityResponce response = commenService.incrementalCity(req);
		updateDBDate(response);
		if (response != null ) {
			cityList = response.getCityList();
			refreshListView();
		}
	}

	/**
	 * 更新数据库
	 * 
	 * @param response
	 */
	private void updateDBDate(IncrementalCityResponce response) {
		if (response != null && response.getCityList() != null && !response.getCityList().isEmpty()) {
			DBUtil.getInstance().updateCityTable(getActivity(), response.getVersion(), response.getCityList());
		}
	}

	@UiThread
	void refreshListView() {
		if (cityList == null || cityList.size() == 0) {
			Toast.makeText(getActivity(), "数据为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (adapter == null) {
			adapter = new CityListAdapter();
			mCitySelectList.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}

	}

	class CityListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return cityList.size();
		}

		@Override
		public Object getItem(int position) {
			return cityList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			// 布局
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(FybaoApplication.getInstance()).inflate(R.layout.item_city_base_list, null);
				holder = new ViewHolder();
				holder.mGPSCity = (TextView) convertView.findViewById(R.id.city_from_gps);
				holder.mCityName = (TextView) convertView.findViewById(R.id.city_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// 设置名称
			City city = cityList.get(position);
			if (city != null) {
				holder.mCityName.setText(city.getName());
				holder.mGPSCity.setVisibility(View.GONE);
			}

			return convertView;
		}

		class ViewHolder {
			TextView mGPSCity; // isGPS城市
			TextView mCityName;// 城市名称
		}
	}

	@Click({ R.id.city_select_back })
	public void registerBack() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		remove();
	}

}
