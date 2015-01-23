package com.manyi.mall.search;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.manyi.mall.R;
import com.manyi.mall.provider.contract.LocalHistoryContract;

public class SearchLivingAreaAdapter extends CursorAdapter{

	public SearchLivingAreaAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder viewHolder = (ViewHolder)view.getTag();
		final String name = cursor.getString(cursor.getColumnIndex(LocalHistoryContract.NAME));
		final String aliasName = cursor.getString(cursor.getColumnIndex(LocalHistoryContract.ALIASNAME));
		final String cityName = cursor.getString(cursor.getColumnIndex(LocalHistoryContract.CITYNAME));
		final String townName = cursor.getString(cursor.getColumnIndex(LocalHistoryContract.TOWNNAME));
		final int estateId = cursor.getInt(cursor.getColumnIndex(LocalHistoryContract.ESTATE_ID));
			if(aliasName!=null && !aliasName.equals("null") && !aliasName.equals("")){
				viewHolder.name.setText(name+"("+aliasName+")");
			}else {
				viewHolder.name.setText(name);
			}
		
			if(cityName!=null && !cityName.equals("null") && !cityName.equals("")){
				viewHolder.district.setVisibility(View.VISIBLE);
				viewHolder.district.setText(cityName+"-"+townName);
			}else {
				viewHolder.district.setVisibility(View.GONE);
				viewHolder.district.setText("");
			}
		
        viewHolder.hotCityTv.setVisibility(View.GONE);
		viewHolder.history.setVisibility(View.VISIBLE);
		
		if (SearchLivingAreaFragment.hasSubEsate(mContext, estateId)) {
			viewHolder.hasSubArea.setVisibility(View.VISIBLE);
		} else {
			viewHolder.hasSubArea.setVisibility(View.GONE);
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_search_living_area, null);
		ViewHolder viewHolder = new ViewHolder();
        viewHolder.name = (TextView)view.findViewById(R.id.search_living_area_item_name);
        viewHolder.history = (ImageView)view.findViewById(R.id.search_living_area_history);
        viewHolder.hasSubArea = (ImageView)view.findViewById(R.id.search_living_area_item_arrow);
        viewHolder.district = (TextView) view.findViewById(R.id.area_district);
        viewHolder.hotCityTv = (TextView) view.findViewById(R.id.hotCityTv);

        view.setTag(viewHolder);
		return view;
	}
	
	private class ViewHolder{
       TextView name,hotCityTv;
       TextView district;
       ImageView history;
       ImageView hasSubArea;
    }
}
