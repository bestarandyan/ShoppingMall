package com.manyi.mall.search;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.AreaRequest;
import com.manyi.mall.cachebean.search.AreasResponse;
import com.manyi.mall.cachebean.search.AreasResponse.AreaResponse;
import com.manyi.mall.common.Constants;
import com.manyi.mall.service.CommonService;

@EFragment
public class AddEstateAreaFragment extends SuperFragment<AreaResponse> {
	AreasResponse mAreasResponse;

	public CommonService commonService;
	@ViewById(R.id.arealist)
	ListView mArealist;

	@FragmentArg
	String targetClass;

	private boolean isOnCreate = true;

	@Background
	void getAreas() {
		try {

			AreaRequest req = new AreaRequest();
			int cityId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("cityId", 0);
			req.setAreaId(cityId);
			req.setNotHasAll(true);
			mAreasResponse = commonService.getDistrict(req);

			adapter();

		} catch (Exception e) {
			throw e;
		}

	}

	@UiThread
	void adapter() {

		mArealist.setAdapter(new BaseAdapter() {

			private int mResource;

			@SuppressWarnings("unused")
			private Context mContext;

			private LayoutInflater mInflater;

			private List<AreaResponse> list = null;

			{
				init(getActivity(), R.layout.fragment_area_listitem, mAreasResponse.getAreaList());
			}

			void init(Context context, int resource, List<AreaResponse> list) {
				mContext = context;
				mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				mResource = resource;
				this.list = list;
			}

			@Override
			public int getCount() {
				return list.size();
			}

			@Override
			public Object getItem(int position) {
				return list.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@SuppressLint("CutPasteId")
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {

				class ViewHolder {
					TextView mTextView;
				}

				ViewHolder holder = null;
				if (convertView == null) {
					convertView = mInflater.inflate(mResource, null);
					holder = new ViewHolder();
					holder.mTextView = (TextView) convertView.findViewById(R.id.areanamelist);
					convertView.setTag(holder);

				} else
					holder = (ViewHolder) convertView.getTag();

				holder.mTextView.setText(mAreasResponse.getAreaList().get(position).getName());
				return convertView;
			}
		});

	}

	@AfterViews
	void bindAdapter() {
		if (isOnCreate) {
			getAreas();
		}
	}

	@ItemClick(R.id.arealist)
	@UiThread
	public void onItemClick(AreaResponse person) {
		if (person.isFlag()) {
			if (getSelectListener() != null)
				notifySelected(person);
			else {
				// 叶子节点
				SearchResultFragment targetFragment = SearchResultFragment.class.cast(Fragment
						.instantiate(getBackOpActivity(), targetClass));
				targetFragment.tag = targetClass;
				// targetFragment.area = area;
				Bundle bundle = new Bundle();
				bundle.putSerializable("area", person);
				targetFragment.setArguments(bundle);
				targetFragment.setManager(getFragmentManager());
				targetFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
						R.anim.anim_fragment_close_out);
				targetFragment.show();
			}
		} else {
			AddEstateAreaFragmentNext loginFragment4 = GeneratedClassUtils.getInstance(AddEstateAreaFragmentNext.class);
			Bundle bundle = new Bundle();
			bundle.putInt("areaId", person.getAreaId());
			bundle.putString("targetClass", targetClass);
			loginFragment4.setArguments(bundle);
			loginFragment4.tag = AddEstateAreaFragmentNext.class.getName();
			loginFragment4.setManager(getFragmentManager());
			loginFragment4.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
					R.anim.anim_fragment_close_out);
			loginFragment4.setSelectListener(new SelectListener<AreasResponse.AreaResponse>() {

				@Override
				public void onSelected(AreaResponse t) {
					if (getSelectListener() != null) {
						isOnCreate = false;
						notifySelected(t);
					}
				}

				@Override
				public void onCanceled() {
				}
			});
			loginFragment4.show();
		}
	}

	@Click(R.id.area_back)
	void getback() {
		remove();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_area, container, false);
	}

}
