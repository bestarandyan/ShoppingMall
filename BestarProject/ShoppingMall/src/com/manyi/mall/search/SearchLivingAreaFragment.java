package com.manyi.mall.search;

import java.io.Serializable;
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
import org.springframework.util.StringUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.SearchRequest;
import com.manyi.mall.cachebean.search.SearchRespose;
import com.manyi.mall.cachebean.search.SearchRespose.Estate;
import com.manyi.mall.cachebean.search.SubEstateResponse;
import com.manyi.mall.common.Constants;
import com.manyi.mall.provider.LoaderIds;
import com.manyi.mall.provider.contract.EstateSubAreaContract;
import com.manyi.mall.provider.contract.LocalHistoryContract;
import com.manyi.mall.service.CommonService;
import com.manyi.mall.user.EstateFeedbackFragment;

@EFragment(R.layout.fragment_search_living_area)
public class SearchLivingAreaFragment extends SuperFragment<Object> implements LoaderCallbacks<Cursor> {
	


	public CommonService mCommonService;

	@ViewById(R.id.localDataTv)
	TextView mLocalDataTv;

	@ViewById(R.id.search_living_area_layout)
	public LinearLayout mFragmentLayout;

	@ViewById(R.id.search_living_area_name)
	public EditText mSearchKey;

	@ViewById(R.id.search_living_area_clear)
	public LinearLayout mClear;

	@ViewById(R.id.search_living_area_list)
	public ListView mList;

	@ViewById(R.id.search_living_area_nodata)
	public TextView mNoData;
	
	@ViewById(R.id.search_living_area_nodata_layout)
	public RelativeLayout mNoDataLayout;
	
	@ViewById(R.id.goto_estate_feedback)
	public TextView mGotoEstateFeedback;

	@ViewById(R.id.search_living_progressbar)
	public ProgressBar mProgressBar;

	@FragmentArg
	int searchType;
	
	@FragmentArg
	int subEstateId;

	private TextWatcher mSearchTextWatcher;
	private SearchLivingAreaAdapter mLocalHistoryAdapter;
	private SearchLivingAreaRemoteAdapter mRemoteAdapter;
	private int mUserId;
	private long lastSeatchTime = 0;
	@Click(R.id.search_by_condition_back)
	void back() {
		remove();
	}

	@AfterViews
	public void init() {
		mUserId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);

		// dummy listener to stop clicking event at bottom of this layout
		mFragmentLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		mLocalHistoryAdapter = new SearchLivingAreaAdapter(getActivity(), null, true);
		mList.setAdapter(mLocalHistoryAdapter);

		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				processItemClick(parent, position, id);
			}
		});

		mClear.setVisibility(View.GONE);
		mSearchTextWatcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mProgressBar.setVisibility(View.VISIBLE);
				if (mSearchKey.getText().toString().trim().length() > 0) {
					mClear.setVisibility(View.VISIBLE);
					lastSeatchTime = System.currentTimeMillis();
					mSearchKey.postDelayed(runnable,300);//延时处理请求，避免连续请求时的异常
				} else {
					mClear.setVisibility(View.GONE);
					reloadRemoteData();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		};
		mSearchKey.addTextChangedListener(mSearchTextWatcher);

		getBackOpActivity().getSupportLoaderManager().initLoader(LoaderIds.SEARCHLIVINGAREAFRAGMENT_LOADER_ID, null, this);

        addAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ManyiUtils.showKeyBoard(getActivity(),mSearchKey);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
	}
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			long currentTime = System.currentTimeMillis();
			if (currentTime - lastSeatchTime >= 300) {
				reloadRemoteData();
			}
		}
	}; 
	@Background
	public void reloadRemoteData() {
		String searchKey = mSearchKey.getText().toString().trim();
		if (searchKey == null || searchKey.length() == 0) {
			updateDateFromLocal();
			return;
		}

		try {
			int cityId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("cityId", 0);
			SearchRequest searchRequest = new SearchRequest();
			searchRequest.setName(searchKey);
			searchRequest.setCityId(cityId);
			SearchRespose searchRespose = mCommonService.search(searchRequest);
			updateDataFromRemote(searchRespose.getEstateList());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@UiThread
	public void updateDateFromLocal() {
		getBackOpActivity().getSupportLoaderManager().initLoader(LoaderIds.SEARCHLIVINGAREAFRAGMENT_LOADER_ID, null, this);
		mList.setAdapter(mLocalHistoryAdapter);
	}

	@UiThread
	public void updateDataFromRemote(List<Estate> estates) {
		if (getActivity() == null || estates == null) {
			return;
		}
		mRemoteAdapter = new SearchLivingAreaRemoteAdapter(getActivity(), R.layout.item_search_living_area, estates, searchType,mSearchKey.getText().toString());
		mList.setAdapter(mRemoteAdapter);
		if (estates != null && estates.size() > 0) {
			updateViews(false, true, true);
		} else {
			updateViews(false, false, true);
		}
		mProgressBar.setVisibility(View.GONE);
		// mRemoteAdapter.notifyDataSetChanged();
	}
		
	/**
	 * @param estate
	 */
	private void gotoSubAreaFragment(final Estate estate) {
		ManyiUtils.closeKeyBoard(getActivity(), mSearchKey);

		if (estate.getSubEstatelList() == null || estate.getSubEstatelList().size() < 2) {
			if (estate.getSubEstatelList() != null && estate.getSubEstatelList().size() == 1) {
				estate.setEstateId(estate.getSubEstatelList().get(0).getSubEstateId());
			} else if (estate.getSubEstatelList() != null && estate.getSubEstatelList().size() == 0) {
				estate.setEstateId(0);// 服务端要求的处理
			}
			if (getSelectListener() != null)
				notifySelected(estate);
			return;
		}
		AreaChildDialogFragment areaChildDialogFragment = GeneratedClassUtils.getInstance(AreaChildDialogFragment.class);
		Bundle bundle = new Bundle();
		bundle.putInt(Constants.SELECTED_ESTATE_ID, estate.getEstateId());
		bundle.putString(Constants.SELECTED_ESTATE_NAME, estate.getEstateName());
		bundle.putSerializable(Constants.SELECTED_ESTATE_DATA, (Serializable) estate.getSubEstatelList());
		areaChildDialogFragment.setArguments(bundle);
		areaChildDialogFragment.tag = AreaChildDialogFragment.class.getName();
		areaChildDialogFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		areaChildDialogFragment.setContainerId(R.id.main_container);
		areaChildDialogFragment.setManager(getFragmentManager());
		areaChildDialogFragment.setSelectListener(new SelectListener<Estate>() {
			@Override
			public void onSelected(Estate t) {
				if (t != null)
					t.setEstateName(estate.getEstateName() + "  " + t.getEstateName());
				notifySelected(t);
			}

			@Override
			public void onCanceled() {
			}

		});
		areaChildDialogFragment.show(SuperFragment.SHOW_ADD_HIDE);
	}

	@Click(R.id.search_living_area_clear)
	public void clearSearchText() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		mSearchKey.setText("");
	}
	
	@Click(R.id.goto_estate_feedback)
	public void gotoEstateFeedback(){
		if (CheckDoubleClick.isFastDoubleClick()) return;
		EstateFeedbackFragment estateFeedbackFragment = GeneratedClassUtils.getInstance(EstateFeedbackFragment.class);
		estateFeedbackFragment.tag = EstateFeedbackFragment.class.getName();
		estateFeedbackFragment.setManager(getFragmentManager());
		estateFeedbackFragment.setContainerId(R.id.main_container);
		estateFeedbackFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
				R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);

		estateFeedbackFragment.show(SHOW_ADD_HIDE);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getActivity(), LocalHistoryContract.CONTENT_URI,
				new String[] { LocalHistoryContract._ID, LocalHistoryContract.NAME, LocalHistoryContract.ESTATE_ID,
						LocalHistoryContract.DATE, LocalHistoryContract.USER_ID, LocalHistoryContract.IS_AREA,
						LocalHistoryContract.ALIASNAME, LocalHistoryContract.CITYNAME, LocalHistoryContract.TOWNNAME ,LocalHistoryContract.ESTATENAMESTR},
				LocalHistoryContract.USER_ID + "=? AND " + LocalHistoryContract.IS_AREA + "=?", new String[] { String.valueOf(mUserId),
						String.valueOf(LocalHistoryContract.ESTATE_TYPE) }, LocalHistoryContract.DEFAULT_SORT);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (cursor == null) {
			return;
		}
		if (cursor.getCount() > Constants.MAX_LOCAL_HISTORY_SEARCH_LIVING) {
			for (cursor.moveToPosition(Constants.MAX_LOCAL_HISTORY_SEARCH_LIVING); !cursor.isAfterLast(); cursor.moveToNext()) {
				String id = cursor.getString(cursor.getColumnIndex(LocalHistoryContract._ID));
				if (getActivity()==null) {
					return ;
				}
				getActivity().getContentResolver().delete(LocalHistoryContract.CONTENT_URI, LocalHistoryContract._ID + " = ? ",
						new String[] { id });
			}
		}
		cursor.moveToFirst();
		if (mLocalHistoryAdapter==null) {
			return;
		}
		mLocalHistoryAdapter.swapCursor(cursor);

		if (cursor.getCount() == 0) {
			updateViews(false, false, false);
		} else {
			updateViews(true, false, false);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

	private void processItemClick(AdapterView<?> parent, int position, long id) {
		if (parent.getAdapter() instanceof SearchLivingAreaAdapter) {
			Cursor cursor = getActivity().getContentResolver().query(
					LocalHistoryContract.CONTENT_URI,
					new String[] { LocalHistoryContract.NAME, LocalHistoryContract.ESTATE_ID, LocalHistoryContract.ALIASNAME,
							LocalHistoryContract.CITYNAME, LocalHistoryContract.TOWNNAME,LocalHistoryContract.ESTATENAMESTR }, LocalHistoryContract._ID + "=?",
					new String[] { String.valueOf(id) }, null);
			if (cursor.getCount() == 0)
				return;

			cursor.moveToFirst();
			int estateId = cursor.getInt(cursor.getColumnIndex(LocalHistoryContract.ESTATE_ID));
			updateLocalDate(estateId);

			String name = cursor.getString(cursor.getColumnIndex(LocalHistoryContract.NAME));
			String estateNameStr = cursor.getString(cursor.getColumnIndex(LocalHistoryContract.ESTATENAMESTR));
			String aliasName = cursor.getString(cursor.getColumnIndex(LocalHistoryContract.ALIASNAME));
			String cityName = cursor.getString(cursor.getColumnIndex(LocalHistoryContract.CITYNAME));
			String townName = cursor.getString(cursor.getColumnIndex(LocalHistoryContract.TOWNNAME));
			Estate estate = new Estate();
			estate.setEstateName(name);
			estate.setEstateNameStr(estateNameStr);
			estate.setAliasName(StringUtils.tokenizeToStringArray(aliasName, ","));
			estate.setCityName(cityName);
			estate.setTownName(townName);
			estate.setEstateId(estateId);
			estate.setSubEstatelList(getSubEstates(estateId));
			gotoSubAreaFragment(estate);

			cursor.close();
		} else if (parent.getAdapter() instanceof SearchLivingAreaRemoteAdapter) {
			Estate estate = (Estate) parent.getAdapter().getItem(position);
			updateLocalDatabase(estate);
			gotoSubAreaFragment(estate);
		}
	}

	private void updateViews(boolean localHasData, boolean remoteHasData, boolean remoteFirst) {
		if (localHasData || remoteHasData) {
			mList.setVisibility(View.VISIBLE);
			if (mSearchKey.getText() == null || mSearchKey.getText().toString().trim().length() == 0) {
				mLocalDataTv.setVisibility(View.VISIBLE);
			} else {
				mLocalDataTv.setVisibility(View.GONE);
			}
			mNoDataLayout.setVisibility(View.GONE);
		} else {
			mNoDataLayout.setVisibility(View.VISIBLE);
			mList.setVisibility(View.GONE);
			mLocalDataTv.setVisibility(View.GONE);
			if (!localHasData) {
				mGotoEstateFeedback.setVisibility(View.GONE);
				mNoData.setText(R.string.search__local_nohistory);
			} else if (!remoteHasData) {
				mGotoEstateFeedback.setVisibility(View.VISIBLE);
				mNoData.setText(R.string.search_remote_nohistory);
			}
			if (remoteFirst){
				mGotoEstateFeedback.setVisibility(View.VISIBLE);
				mNoData.setText(R.string.search_remote_nohistory);
			}
				
		}
	}

	private void updateLocalDate(long esateId) {
		ContentValues history = new ContentValues();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
		history.put(LocalHistoryContract.DATE, df.format(new Date()));
		getActivity().getContentResolver().update(LocalHistoryContract.CONTENT_URI, history, LocalHistoryContract.ESTATE_ID + "=?",
				new String[] { String.valueOf(esateId) });
	}

	// always keep 50 items, if > 50 then deleting useless items
	private void updateLocalDatabase(Estate estate) {
		ContentValues history = new ContentValues();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);

		Cursor cursor = getActivity().getContentResolver().query(LocalHistoryContract.CONTENT_URI,
				new String[] { LocalHistoryContract.ESTATE_ID }, LocalHistoryContract.ESTATE_ID + "=?",
				new String[] { String.valueOf(estate.getEstateId()) }, null);

		if (cursor.getCount() > 0) { // update the current local item to latest
			history.put(LocalHistoryContract.DATE, df.format(new Date()));
			getActivity().getContentResolver().update(LocalHistoryContract.CONTENT_URI, history, LocalHistoryContract.ESTATE_ID + "=?",
					new String[] { String.valueOf(estate.getEstateId()) });
		} else {
			history.put(LocalHistoryContract.ESTATE_ID, estate.getEstateId());
			history.put(LocalHistoryContract.NAME, estate.getEstateName());
			history.put(LocalHistoryContract.ESTATENAMESTR, estate.getEstateNameStr());
			history.put(LocalHistoryContract.ALIASNAME, StringUtils.arrayToDelimitedString(estate.getAliasName(), ","));
			history.put(LocalHistoryContract.CITYNAME, estate.getCityName());
			history.put(LocalHistoryContract.TOWNNAME, estate.getTownName());
			int userId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
			history.put(LocalHistoryContract.USER_ID, String.valueOf(userId));
			history.put(LocalHistoryContract.DATE, df.format(new Date()));
			history.put(LocalHistoryContract.IS_AREA, LocalHistoryContract.ESTATE_TYPE);
			getActivity().getContentResolver().insert(LocalHistoryContract.CONTENT_URI, history);
		}

		if (estate.getSubEstatelList() != null) {
			// first delete old item
			getActivity().getContentResolver().delete(EstateSubAreaContract.CONTENT_URI, EstateSubAreaContract.ESTATE_ID + "=?",
					new String[] { String.valueOf(estate.getEstateId()) });

			// intert the latest
			for (SubEstateResponse est : estate.getSubEstatelList()) {
				ContentValues values = new ContentValues();
				values.put(EstateSubAreaContract.ESTATE_ID, estate.getEstateId());
				values.put(EstateSubAreaContract.SUBESTATE_NAME, est.getSubEstateName());
				values.put(EstateSubAreaContract.SUBESTATE_ID, est.getSubEstateId());
				getActivity().getContentResolver().insert(EstateSubAreaContract.CONTENT_URI, values);
			}
		}

		cursor.close();
	}

	private List<SubEstateResponse> getSubEstates(int estateId) {
		List<SubEstateResponse> subEstateList = new ArrayList<>();
		Cursor cursor = getActivity().getContentResolver().query(EstateSubAreaContract.CONTENT_URI,
				new String[] { EstateSubAreaContract.ESTATE_ID, EstateSubAreaContract.SUBESTATE_ID, EstateSubAreaContract.SUBESTATE_NAME },
				EstateSubAreaContract.ESTATE_ID + "=?", new String[] { String.valueOf(estateId) }, null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			SubEstateResponse subEstate = new SubEstateResponse();
			int subesateId = cursor.getInt(cursor.getColumnIndex(EstateSubAreaContract.SUBESTATE_ID));
			String subestateName = cursor.getString(cursor.getColumnIndex(EstateSubAreaContract.SUBESTATE_NAME));
			subEstate.setSubEstateId(subesateId);
			subEstate.setSubEstateName(subestateName);
			subEstateList.add(subEstate);
		}
		cursor.close();

		return subEstateList;
	}

	public static boolean hasSubEsate(Context context, int estateId) {
		boolean hasSubEstate = false;
		Cursor cursor = context.getContentResolver().query(EstateSubAreaContract.CONTENT_URI,
				new String[] { EstateSubAreaContract.SUBESTATE_NAME }, EstateSubAreaContract.ESTATE_ID + "=?",
				new String[] { String.valueOf(estateId) }, null);
		if (cursor.getCount() > 1) {
			hasSubEstate = true;
		}
		cursor.close();
		return hasSubEstate;
	}

}
