package com.manyi.mall.search;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.util.StringUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.huoqiu.framework.app.SuperFragment;
import com.manyi.mall.cachebean.search.AreasResponse.AreaResponse;
import com.manyi.mall.cachebean.search.SearchRespose.Estate;
import com.manyi.mall.common.Constants;
import com.manyi.mall.provider.LoaderIds;
import com.manyi.mall.provider.contract.LocalHistoryContract;

public class SearchFragment extends SuperFragment<Object> implements LoaderCallbacks<Cursor> {
	protected SearchAdapter mAdapter;
	protected int mUserId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUserId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
		mAdapter = new SearchAdapter(getActivity(), null, true);
	}

	public void startLoadData() {
		if (getActivity() == null)
			return;
		getActivity().getSupportLoaderManager().initLoader(LoaderIds.SEARCHFRAGMENT_LOADER_ID, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getActivity(), LocalHistoryContract.CONTENT_URI,
				new String[] { LocalHistoryContract._ID, LocalHistoryContract.NAME, LocalHistoryContract.ESTATE_ID,
						LocalHistoryContract.DATE, LocalHistoryContract.USER_ID, LocalHistoryContract.IS_AREA,
						LocalHistoryContract.ALIASNAME, LocalHistoryContract.CITYNAME, LocalHistoryContract.TOWNNAME,LocalHistoryContract.ESTATENAMESTR },
				LocalHistoryContract.USER_ID + " = ? ", new String[] { String.valueOf(mUserId) }, LocalHistoryContract.DEFAULT_SORT);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (getActivity() == null) {
			return;
		}
		if (cursor.getCount() > Constants.MAX_LOCAL_HISTORY) {
			for (cursor.moveToPosition(Constants.MAX_LOCAL_HISTORY); !cursor.isAfterLast(); cursor.moveToNext()) {
				String id = cursor.getString(cursor.getColumnIndex(LocalHistoryContract._ID));
				getActivity().getContentResolver().delete(LocalHistoryContract.CONTENT_URI, LocalHistoryContract._ID + "=?",
						new String[] { id });
			}
		}

		mAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

	public void updateLocalDate(long esateId) {
		ContentValues history = new ContentValues();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
		history.put(LocalHistoryContract.DATE, df.format(new Date()));
		getActivity().getContentResolver().update(LocalHistoryContract.CONTENT_URI, history, LocalHistoryContract.ESTATE_ID + "=?",
				new String[] { String.valueOf(esateId) });
	}

	protected int getSearchType(long id) {
		Cursor cursor = getActivity().getContentResolver().query(
				LocalHistoryContract.CONTENT_URI,
				new String[] { LocalHistoryContract.NAME, LocalHistoryContract.ESTATE_ID, LocalHistoryContract.IS_AREA,
						LocalHistoryContract.ALIASNAME, LocalHistoryContract.CITYNAME, LocalHistoryContract.TOWNNAME ,LocalHistoryContract.ESTATENAMESTR},
				LocalHistoryContract._ID + " = ? ", new String[] { String.valueOf(id) }, null);
		if (cursor.getCount() == 0) {
			cursor.close();
			return 0;
		}

		cursor.moveToFirst();
		int type = cursor.getInt(cursor.getColumnIndex(LocalHistoryContract.IS_AREA));
		cursor.close();
		return type;
	}

	protected AreaResponse getArea(long id) {
		Cursor cursor = getActivity().getContentResolver().query(
				LocalHistoryContract.CONTENT_URI,
				new String[] { LocalHistoryContract.NAME, LocalHistoryContract.ESTATE_ID, LocalHistoryContract.ALIASNAME,
						LocalHistoryContract.CITYNAME, LocalHistoryContract.TOWNNAME ,LocalHistoryContract.ESTATENAMESTR}, LocalHistoryContract._ID + " = ? ",
				new String[] { String.valueOf(id) }, null);
		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		cursor.moveToFirst();
		int estateId = cursor.getInt(cursor.getColumnIndex(LocalHistoryContract.ESTATE_ID));
		String names = cursor.getString(cursor.getColumnIndex(LocalHistoryContract.NAME));
		String[] name = names.split("-");
		AreaResponse area = new AreaResponse();
		area.setAreaId(estateId);
		if (name.length > 1) {
			area.setName(name[1]);
		} else {
			area.setName(names);
		}
		updateLocalDate(estateId);
		cursor.close();
		return area;
	}

	protected Estate getEstate(long id) {
		Estate estate = null;
		Cursor cursor = null;
		try {
			cursor = getActivity().getContentResolver().query(
					LocalHistoryContract.CONTENT_URI,
					new String[] { LocalHistoryContract.NAME, LocalHistoryContract.ESTATE_ID, LocalHistoryContract.ALIASNAME,
							LocalHistoryContract.CITYNAME, LocalHistoryContract.TOWNNAME,LocalHistoryContract.ESTATENAMESTR }, LocalHistoryContract._ID + " = ? ",
					new String[] { String.valueOf(id) }, null);
			if (cursor.getCount() != 0) {
				cursor.moveToFirst();
				int estateId = cursor.getInt(cursor.getColumnIndex(LocalHistoryContract.ESTATE_ID));
				String name = cursor.getString(cursor.getColumnIndex(LocalHistoryContract.NAME));
				String estateNameStr = cursor.getString(cursor.getColumnIndex(LocalHistoryContract.ESTATENAMESTR));
				String aliasname = cursor.getString(cursor.getColumnIndex(LocalHistoryContract.ALIASNAME));
				String cityname = cursor.getString(cursor.getColumnIndex(LocalHistoryContract.CITYNAME));
				String townname = cursor.getString(cursor.getColumnIndex(LocalHistoryContract.TOWNNAME));
				estate = new Estate();
				estate.setEstateId(estateId);
				estate.setEstateName(name);
				estate.setAliasName(StringUtils.tokenizeToStringArray(aliasname, ","));
				estate.setCityName(cityname);
				estate.setTownName(townname);
				estate.setEstateNameStr(estateNameStr);
				updateLocalDate(estateId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return estate;
	}
}
