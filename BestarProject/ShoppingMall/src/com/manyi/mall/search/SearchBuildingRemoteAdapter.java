package com.manyi.mall.search;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.huoqiu.framework.util.TextViewUtil;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.BuildingResponse;

public class SearchBuildingRemoteAdapter extends ArrayAdapter<BuildingResponse>{
	private Context mContext;
	private int mLayout;
	private List<BuildingResponse> mBuildingList;
	String mSearchKey = "";

	public SearchBuildingRemoteAdapter(Context context, int resource,List<BuildingResponse> objects,String mSearchKey) {
		super(context, resource, objects);
		mContext = context;
		mLayout = resource;
		mBuildingList = objects;
		this.mSearchKey = mSearchKey;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(mLayout, parent, false);
			holder = new ViewHolder();
			holder.buildName = (TextView) convertView.findViewById(R.id.buildingName);
			holder.buildNameStr = (TextView) convertView.findViewById(R.id.buildingNameStr);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		BuildingResponse building = mBuildingList.get(position);
		String nameString = building.getBuildingNameStr();
//		holder.buildName.setText(nameString==null?"":nameString);
		if (nameString == null) {
			nameString ="";
		}
		TextViewUtil.setTextColorSpan(holder.buildName, nameString, mSearchKey);
		
        String buildNameStr = building.getSubEstateName();
        if (buildNameStr==null || buildNameStr.equals("")) {
        	holder.buildNameStr.setVisibility(View.GONE);
		}else{
			holder.buildNameStr.setVisibility(View.VISIBLE);
			holder.buildNameStr.setText(buildNameStr==null?"":buildNameStr);
		}
        
		return convertView;
	}
	
	private class ViewHolder{
		TextView buildName,buildNameStr;
	}
}
