package com.manyi.mall.user;

import java.util.ArrayList;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.AreaAndTownMessage;
import com.manyi.mall.cachebean.search.AreaRequest;
import com.manyi.mall.cachebean.search.AreasResponse;
import com.manyi.mall.cachebean.search.AreasResponse.AreaResponse;
import com.manyi.mall.common.Constants;
import com.manyi.mall.service.CommonService;

@EFragment(R.layout.fragment_register_area)
public class RegisterSelectAreaFragment extends SuperFragment<AreaAndTownMessage> {
	AreasResponse res;
	public List<AreaResponse> data = new ArrayList<AreaResponse>(0);

	private CommonService commonService;
	@ViewById
	ListView arealist;

	public ArrayList<AreaResponse> townList = new ArrayList<AreaResponse>();

	@FragmentArg
	int areaId;
	private boolean isFirstEnter = true;

	@Background
	void getdate() {
		AreaRequest req = new AreaRequest();
		if (areaId <= 0) {
			areaId = Constants.ID_CITY_SHANGHAI;
		}
		req.setAreaId(areaId);
		req.setNotHasAll(true);
		res = commonService.getArea(req);

		res.getAreaList().addAll(data);
		data = res.getAreaList();

		adapter();

	}

	@UiThread
	void adapter() {

		arealist.setAdapter(new BaseAdapter() {

			private int mResource;

			@SuppressWarnings("unused")
			private Context mContext;

			private LayoutInflater mInflater;

			private List<AreaResponse> list = null;

			{
				init(getActivity(), R.layout.fragment_area_listitem, res.getAreaList());
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

				} else {

					holder = (ViewHolder) convertView.getTag();
				}

				holder.mTextView.setText(res.getAreaList().get(position).getName());

				return convertView;
			}
		});

	}

	@AfterViews
	void bindAdapter() {
		if (isFirstEnter) {
			getdate();
			isFirstEnter = false;
		}
	}

	@ItemClick(R.id.arealist)
	@UiThread
	public void onItemClick(final AreaResponse person) {
		final RegisterSelectTownFragment townFragment = GeneratedClassUtils.getInstance(RegisterSelectTownFragment.class);
		Bundle argsBundle = new Bundle();
		argsBundle.putInt("townId", person.getAreaId());
		argsBundle.putString("townNameString", person.getName());
		townFragment.setArguments(argsBundle);
		townFragment.tag = townFragment.getClass().getName();
		townFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		townFragment.setManager(getFragmentManager());
		townFragment.setSelectListener(new SelectListener<AreaResponse>() {

			@Override
			public void onSelected(AreaResponse t) {
				AreaAndTownMessage areaAndTownMessage = new AreaAndTownMessage();
				areaAndTownMessage.setAreaId(person.getAreaId());
				areaAndTownMessage.setAreaName(person.getName());
				areaAndTownMessage.setTownId(t.getAreaId());
				areaAndTownMessage.setTownName(t.getName());
				notifySelected(areaAndTownMessage);
			}

			@Override
			public void onCanceled() {
				adapter();
			}
		});
		townFragment.show(SHOW_ADD_HIDE);
	}

	@Click(R.id.area_back)
	void getback() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		pop().perform(getBackOpActivity());
	}

}
