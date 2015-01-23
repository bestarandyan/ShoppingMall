package com.manyi.mall.user;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.AreaRequest;
import com.manyi.mall.cachebean.search.AreasResponse;
import com.manyi.mall.cachebean.search.AreasResponse.AreaResponse;
import com.manyi.mall.service.CommonService;
@EFragment(R.layout.fragment_register_town)
public class RegisterSelectTownFragment extends SuperFragment<AreaResponse>{
	AreasResponse res;

	private CommonService commonService;
	@ViewById(R.id.townlist)
	ListView townList;
	@ViewById(R.id.town_name)
	TextView townName;

	@FragmentArg
	public int townId ;
	@FragmentArg
	String townNameString;
	
	@Background
	void getdate() {
		AreaRequest req = new AreaRequest();
		req.setAreaId(townId);
		req.setNotHasAll(true);
		res = commonService.getTown(req);
		if (res != null) {
			adapter();
		}
	}

	@UiThread
	void adapter() {

		townList.setAdapter(new BaseAdapter() {

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

				
				class ViewHolder{
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
		townName.setText(townNameString);
		getdate();
	}

	@ItemClick(R.id.townlist)
	@UiThread
	public void onItemClick(AreaResponse person) {
		AreaResponse areaResponse = new AreaResponse();
		areaResponse.setAreaId(person.getAreaId());
		areaResponse.setName(person.getName());
		notifySelected(areaResponse);
	}

	@Click(R.id.town_back)
	void getback() {
		if (CheckDoubleClick.isFastDoubleClick())
		return;
		pop().perform(getBackOpActivity());
	}
	
}
