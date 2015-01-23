package com.manyi.mall.search;

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
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.AreasResponse.AreaResponse;
import com.manyi.mall.cachebean.search.SearchRequest;
import com.manyi.mall.cachebean.search.SearchRespose;
import com.manyi.mall.cachebean.search.SearchRespose.Estate;
import com.manyi.mall.cachebean.search.SubEstateResponse;
import com.manyi.mall.common.Constants;
import com.manyi.mall.provider.contract.EstateSubAreaContract;
import com.manyi.mall.provider.contract.LocalHistoryContract;
import com.manyi.mall.release.RentSearchResultFragment;
import com.manyi.mall.release.SellSearchResultFragment;
import com.manyi.mall.service.CommonService;
import com.peony.listadapter.BindDictionary;
import com.peony.listadapter.ListAdapter;
import com.peony.listadapter.extractors.StringExtractor;

@EFragment(R.layout.fragement_search_by_district)
public class SearchByDistrictFragment extends SearchFragment {

	@ViewById(R.id.search_by_condition_district_search)
	RelativeLayout mSearchByConditionLayout;

	@ViewById(R.id.search_by_condition_back)
	TextView mBack;

	@ViewById(R.id.search_by_condition_search_clear)
	LinearLayout mSearchClear;

	@ViewById(R.id.search_by_condition_search_icon)
	ImageView mSearchIcon;

	@ViewById(R.id.search_by_condition_progressbar)
	ProgressBar mSearchProgressBar;

	@ViewById(R.id.search_by_condition_list)
	ListView mList;

	@ViewById(R.id.search_by_condition_search_key)
	public EditText mSearchKey;

	@FragmentArg(Constants.SEARCH_TARGET_CLASS)
	String mTargetClass;

    boolean mIsSell;

	private TextWatcher mSearchTextWatcher;
	private CommonService commonService;
	private BindDictionary<Estate> mBindDictionary = new BindDictionary<Estate>();
	private ListAdapter<Estate> mRemoteAdapter;
	private List<Estate> mRemoteData = new ArrayList<Estate>();
	private long lastSeatchTime = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	public void init() {
		SharedPreferences perf = getActivity().getSharedPreferences(Constants.PERF_SEARCH, Context.MODE_PRIVATE);
    	mIsSell = perf.getBoolean(Constants.KEY_SEARCH_SELL, true);
		startLoadData();

		mSearchTextWatcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String searchKey = mSearchKey.getText().toString().trim();
				if (TextUtils.isEmpty(searchKey)) {
					updateDataFromLocal();
					setSearchVisibility(View.GONE);
					mSearchByConditionLayout.setVisibility(View.VISIBLE);
					return;
				}else {
					lastSeatchTime = System.currentTimeMillis();
					mSearchKey.postDelayed(runnable,300);//延时处理请求，避免连续请求时的异常
					setSearchVisibility(View.VISIBLE);
					mSearchByConditionLayout.setVisibility(View.GONE);
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

		initBinding();

		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				processItemClick(parent, position, id);
			}
		});
		
		addAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            	if (getActivity() != null) {
            		ManyiUtils.showKeyBoard(getActivity(),mSearchKey);
				}
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
	private void initBinding() {
		mBindDictionary.addStringField(R.id.search_item_title, new StringExtractor<SearchRespose.Estate>() {
			@Override
			public String getStringValue(Estate estate, int position) {
				String nameString = StringUtils.arrayToCommaDelimitedString(estate.getAliasName());
					if(nameString != null && !nameString.equals("null") && !nameString.equals("")){
						nameString = estate.getEstateName()+" ("+ StringUtils.arrayToCommaDelimitedString(estate.getAliasName())+")";
					}else {
						nameString = estate.getEstateName();
					}
					return nameString;
			}
		});
		mBindDictionary.addStringField(R.id.search_item_area, new StringExtractor<SearchRespose.Estate>() {
			@Override
			public String getStringValue(Estate estate, int position) {
                String cityName = estate.getCityName();
                if(cityName != null && !cityName.equals("null") && !cityName.equals("")){
                    cityName = estate.getCityName() + " - " + estate.getTownName();
                }else {
                    cityName = "";
                }
				return cityName;
			}
		}).visibilityIfNull(View.GONE);

        mBindDictionary.addStringField(R.id.hotCityTv,new StringExtractor<SearchRespose.Estate>() {
            @Override
            public String getStringValue(Estate estate, int position) {
              int isHot = estate.getHot();
                if(isHot == 1 && !mIsSell){
                    return "[热点]";
                }else{
                    return null;
                }
            }
        }).visibilityIfNull(View.GONE);

		mRemoteAdapter = new ListAdapter<Estate>(getActivity(), mRemoteData, R.layout.fragment_search_local_item, mBindDictionary);
	}

	@UiThread
	public void setSearchVisibility(int visibility) {
		if (visibility == View.GONE) {
			mSearchIcon.setImageResource(R.drawable.searchfield_ic_search);
			mSearchProgressBar.setVisibility(View.GONE);
		} else if (visibility == View.VISIBLE) {
			mSearchIcon.setImageResource(R.drawable.searchfield_ic_clear_org);
			mSearchProgressBar.setVisibility(View.VISIBLE);
		}

		if (mSearchKey.getText().toString().trim().length() > 0) mSearchIcon.setImageResource(R.drawable.searchfield_ic_clear_org);
	}

	private void processItemClick(AdapterView<?> parent, int position, long id) {
		ManyiUtils.closeKeyBoard(getActivity(), mSearchKey);

		SearchResultFragment targetFragment;
		if (mTargetClass.equals(SellSearchResultFragment.class.getName())) {
			targetFragment = GeneratedClassUtils.getInstance(SellSearchResultFragment.class);
			targetFragment.tag = SellSearchResultFragment.class.getName();
		} else if (mTargetClass.equals(RentSearchResultFragment.class.getName())) {
			targetFragment = GeneratedClassUtils.getInstance(RentSearchResultFragment.class);
			targetFragment.tag = RentSearchResultFragment.class.getName();
		} else {
			return;
		}

		Bundle bundle = new Bundle();

		if (parent.getAdapter() instanceof SearchAdapter) {
			Cursor cursor = getActivity().getContentResolver().query(LocalHistoryContract.CONTENT_URI,
					new String[] { LocalHistoryContract.NAME, LocalHistoryContract.ESTATE_ID, LocalHistoryContract.IS_AREA ,
					LocalHistoryContract.ALIASNAME,LocalHistoryContract.CITYNAME,LocalHistoryContract.TOWNNAME,LocalHistoryContract.ESTATENAMESTR},
					LocalHistoryContract._ID + " = ? ", new String[] { String.valueOf(id) }, null);
			if (cursor.getCount() == 0)
				return;

			cursor.moveToFirst();
			int estateId = cursor.getInt(cursor.getColumnIndex(LocalHistoryContract.ESTATE_ID));
			String name = cursor.getString(cursor.getColumnIndex(LocalHistoryContract.NAME));
			String aliasName = cursor.getString(cursor.getColumnIndex(LocalHistoryContract.ALIASNAME));
			String cityName = cursor.getString(cursor.getColumnIndex(LocalHistoryContract.CITYNAME));
			String townName = cursor.getString(cursor.getColumnIndex(LocalHistoryContract.TOWNNAME));
			String estateNameStr = cursor.getString(cursor.getColumnIndex(LocalHistoryContract.ESTATENAMESTR));
			int type = getSearchType(id);
			if (type == LocalHistoryContract.AREA_TYPE) {
				AreaResponse areaResponse = getArea(id);
				bundle.putSerializable("area", areaResponse);
			} else if (type == LocalHistoryContract.ESTATE_TYPE) {
				Estate estate = new Estate();
				estate.setEstateId(estateId);
				estate.setEstateName(name);
//				String tempString = StringUtils.arrayToDelimitedString(aliasName, ",");
				estate.setAliasName(StringUtils.tokenizeToStringArray(aliasName, ","));
				estate.setCityName(cityName);
				estate.setTownName(townName);
				estate.setEstateNameStr(estateNameStr);
				bundle.putSerializable("estate", estate);
			}

			updateLocalDate(estateId);
			cursor.close();
		} else if (parent.getAdapter() instanceof ListAdapter) {
			Estate estate = (Estate) parent.getAdapter().getItem(position);
			updateLocalDatabase(estate);
			bundle.putSerializable("estate", estate);
		}

		targetFragment.setArguments(bundle);
		targetFragment.setManager(getFragmentManager());
		targetFragment.setContainerId(R.id.main_container);
		targetFragment.setSelectListener(new SelectListener<Object>() {
			@Override
			public void onSelected(Object t) {
				init();
				/*if (getSelectListener() != null)
					notifySelected(t);*/
				mSearchByConditionLayout.setVisibility(View.VISIBLE);
			}

			@Override
			public void onCanceled() {
			}
		});
		targetFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		targetFragment.show(SHOW_ADD_HIDE);
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

		cursor.close();

		if (estate.getSubEstatelList() != null) {
			// first delete old item
			getActivity().getContentResolver().delete(EstateSubAreaContract.CONTENT_URI, EstateSubAreaContract.ESTATE_ID + "=?", new String[] { String.valueOf(estate.getEstateId()) });

			// intert the latest
			for (SubEstateResponse est : estate.getSubEstatelList()) {
				ContentValues values = new ContentValues();
				values.put(EstateSubAreaContract.ESTATE_ID, estate.getEstateId());
				values.put(EstateSubAreaContract.SUBESTATE_NAME, est.getSubEstateName());
				values.put(EstateSubAreaContract.SUBESTATE_ID, est.getSubEstateId());
				getActivity().getContentResolver().insert(EstateSubAreaContract.CONTENT_URI, values);
			}
		}
	}

	@UiThread
	public void updateDataFromLocal() {
		startLoadData();
	}

	@Background
	public void reloadRemoteData() {
		try {
			String searchKey = mSearchKey.getText().toString().trim();
			int cityId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("cityId", 0);
			SearchRequest searchRequest = new SearchRequest();
			searchRequest.setName(searchKey);
			searchRequest.setCityId(cityId);
			SearchRespose searchRespose = commonService.search(searchRequest);
			updateDataFromRemote(searchRespose.getEstateList());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			setSearchVisibility(View.GONE);
		}
	}

	@UiThread
	public void updateDataFromRemote(List<Estate> estates) {
		mRemoteAdapter.updateData(estates);
		mList.setAdapter(mRemoteAdapter);
	}

	@Click(R.id.search_by_condition_district_search)
	public void searchByCondition() {
		if (CheckDoubleClick.isFastDoubleClick()) return;

		ManyiUtils.closeKeyBoard(getActivity(), mSearchKey);

		AreaSelectFragment fragment = GeneratedClassUtils.getInstance(AreaSelectFragment.class);
		Bundle args = new Bundle();
		args.putString("targetClass", mTargetClass);
		fragment.setArguments(args);

		fragment.tag = AreaSelectFragment.class.getName();
		fragment.setManager(getFragmentManager());
		fragment.setContainerId(R.id.main_container);
		fragment.setSelectListener(new SelectListener<Object>() {
			@Override
			public void onSelected(Object t) {
				init();
				/*if (getSelectListener() != null)
					notifySelected(t);*/
				mSearchByConditionLayout.setVisibility(View.VISIBLE);
			}

			@Override
			public void onCanceled() {
			}
		});
		fragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		fragment.show(SHOW_ADD_HIDE);
	}

	@Click(R.id.search_by_condition_search_clear)
	public void clearSearchKey() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		mSearchKey.setText("");
	}

	@Click(R.id.search_by_condition_back)
	public void onBack() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		ManyiUtils.closeKeyBoard(getActivity(), mSearchKey);
		notifySelected(null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		super.onLoadFinished(loader, cursor);
		mList.setAdapter(mAdapter);
	}
}
