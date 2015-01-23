package com.manyi.mall.search;

import java.util.List;

import org.springframework.util.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huoqiu.framework.util.TextViewUtil;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.SearchRespose.Estate;

public class SearchLivingAreaRemoteAdapter extends ArrayAdapter<Estate>{
	private Context mContext;
	private int mLayout;
	private List<Estate> mEstates;
    private int searchType;
    String mSearchKey = "";

	public SearchLivingAreaRemoteAdapter(Context context, int resource,List<Estate> objects,int searchType,String searchKey) {
		super(context, resource, objects);
		this.searchType = searchType;
		mContext = context;
		mLayout = resource;
		mEstates = objects;
		this.mSearchKey = searchKey;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(mLayout, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.search_living_area_item_name);
			holder.hasSubArea = (ImageView)convertView.findViewById(R.id.search_living_area_item_arrow);
			holder.district = (TextView) convertView.findViewById(R.id.area_district);
            holder.hotCityTv = (TextView) convertView.findViewById(R.id.hotCityTv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Estate estate = mEstates.get(position);
		String nameString = StringUtils.arrayToDelimitedString(estate.getAliasName(), ",");
		String estateNameStr = estate.getEstateNameStr();
		if (estateNameStr!=null && estateNameStr.length()>0) {
            TextViewUtil.setTextColorSpan(holder.name, estateNameStr, mSearchKey);
		}else{
			if(nameString!=null && !nameString.equals("null") && !nameString.equals("")){
				nameString = estate.getEstateName()+"("+nameString+")";
			}else {
				nameString = estate.getEstateName();
			}
			TextViewUtil.setTextColorSpan(holder.name, nameString, mSearchKey);
		}
        String cityName = estate.getCityName();
        
        
			if(cityName!=null && !cityName.equals("null") && !cityName.equals("")){
	            holder.district.setVisibility(View.VISIBLE);
	            TextViewUtil.setTextColorSpan(holder.district, estate.getCityName()+"-"+estate.getTownName(), mSearchKey);
	        }else {
	            holder.district.setVisibility(View.GONE);
	            holder.district.setText("");
	        }

        int isHot = estate.getHot();
        if(isHot == 1 && searchType==1){
            holder.hotCityTv.setVisibility(View.VISIBLE);
        }else{
            holder.hotCityTv.setVisibility(View.GONE);
        }


		if (estate.getSubEstatelList() != null && estate.getSubEstatelList().size() > 1) {
			holder.hasSubArea.setVisibility(View.VISIBLE);
		} else {
			holder.hasSubArea.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	private class ViewHolder{
		TextView name,district,hotCityTv;
	    ImageView hasSubArea;
	}
}
