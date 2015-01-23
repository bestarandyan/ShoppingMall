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

public class SearchAdapter extends CursorAdapter{

	public SearchAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder viewHolder = (ViewHolder)view.getTag();
		final String name = cursor.getString(cursor.getColumnIndex(LocalHistoryContract.NAME));
		viewHolder.name.setText(name);
		viewHolder.history.setImageResource(R.drawable.ic_list_history);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		View view = LayoutInflater.from(context).inflate(R.layout.fragment_search_item, null);
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.history = (ImageView)view.findViewById(R.id.search_item_icon);
        viewHolder.name = (TextView)view.findViewById(R.id.search_item_title);
        view.setTag(viewHolder);
		return view;
	}
	
	private class ViewHolder{
		ImageView history;
		TextView name;
    }
}

