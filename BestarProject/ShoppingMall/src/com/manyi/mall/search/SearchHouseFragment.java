package com.manyi.mall.search;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.AreasResponse.AreaResponse;
import com.manyi.mall.cachebean.search.SearchRespose.Estate;
import com.manyi.mall.common.Constants;
import com.manyi.mall.provider.contract.LocalHistoryContract;
import com.manyi.mall.release.RentSearchResultFragment;
import com.manyi.mall.release.SellSearchResultFragment;

@EFragment(R.layout.fragment_search_house)
public class SearchHouseFragment extends SearchFragment {
	
	
	@ViewById(R.id.search_house_title_layout)
	RelativeLayout mTitleLayout;
	
	@ViewById(android.R.id.list)
	ListView mList;
	
	@ViewById(R.id.search_house_sell)
	TextView mSellTextView;
	
	@ViewById(R.id.search_house_rent)
	TextView mRentTextView;
	
	@ViewById(R.id.search_house_arrow)
	ImageView mArrow;
	
	private String mTargetClass = SellSearchResultFragment.class.getName();
	private int mArrowStartX;
	private int mArrowEndX;
	private boolean mIsSell;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		setBackOp(null);
		super.onAttach(activity);
	}
	
	@AfterViews
	public void init() {
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				processItemClick(parent, position, id);
			}
		});
		
		mArrow.post(new Runnable() {
			@Override
			public void run() {
				mArrowStartX = (mSellTextView.getLeft() + mSellTextView.getRight() - mArrow.getWidth()) / 2;
				mArrowEndX = (mRentTextView.getLeft() + mRentTextView.getRight() - mArrow.getWidth()) / 2;
				TranslateAnimation translateAnimation = new TranslateAnimation(0, mArrowStartX, Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF);
				translateAnimation.setDuration(Constants.SHORT_ANIMATION_INTERVERL);
				translateAnimation.setFillAfter(true);
				mArrow.startAnimation(translateAnimation);
				
				SharedPreferences perf = getActivity().getSharedPreferences(Constants.PERF_SEARCH, Context.MODE_PRIVATE);
		    	mIsSell = perf.getBoolean(Constants.KEY_SEARCH_SELL, true);
		    	if (mIsSell) {
		    		setSellStatus(true);
//		    		startArrowAnimation(false, SHORT_ANIMATION_INTERVERL);
		    		translateAnimation = new TranslateAnimation(mArrowStartX, mArrowStartX, Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF);
					translateAnimation.setDuration(Constants.SHORT_ANIMATION_INTERVERL);
					translateAnimation.setFillAfter(true);
					mArrow.startAnimation(translateAnimation);
		    	} else {
		    		setSellStatus(false);
//		    		startArrowAnimation(true, SHORT_ANIMATION_INTERVERL);
		    		translateAnimation = new TranslateAnimation(mArrowEndX, mArrowEndX, Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF);
					translateAnimation.setDuration(Constants.SHORT_ANIMATION_INTERVERL);
					translateAnimation.setFillAfter(true);
					mArrow.startAnimation(translateAnimation);
		    	}
			}
		});
	    
		startLoadData();
	}
	
	private void processItemClick(AdapterView<?> parent, int position, long id) {
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
		
		int type = getSearchType(id);
		if (type == LocalHistoryContract.AREA_TYPE) {
			AreaResponse areaResponse = getArea(id);
			bundle.putSerializable("area", areaResponse);
		} else if (type == LocalHistoryContract.ESTATE_TYPE) {
			Estate estate = getEstate(id);
			if (estate == null) return;
			bundle.putSerializable("estate", estate);
		}
		
		targetFragment.setArguments(bundle);
		targetFragment.setContainerId(R.id.main_container);
		targetFragment.setManager(getFragmentManager());
		targetFragment.setSelectListener(new SelectListener<Object>() {
			@Override
			public void onSelected(Object t) {
				init();
			}

			@Override
			public void onCanceled() {
			}
		});
		targetFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);
		targetFragment.show(SHOW_ADD_HIDE);
	}
	
	@Click(R.id.search_house_sell)
	public void onSellClick() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		SharedPreferences perf = getActivity().getSharedPreferences(Constants.PERF_SEARCH, Context.MODE_PRIVATE);
		Editor editor = perf.edit();
		editor.putBoolean(Constants.KEY_SEARCH_SELL, true);
		editor.commit();
		
		setSellStatus(true);
		startArrowAnimation(false, Constants.NORMAL_ANIMATION_INTERVERL);
	}
	
	@Click(R.id.search_house_rent)
	public void onRentClick() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		SharedPreferences perf = getActivity().getSharedPreferences(Constants.PERF_SEARCH, Context.MODE_PRIVATE);
		Editor editor = perf.edit();
		editor.putBoolean(Constants.KEY_SEARCH_SELL, false);
		editor.commit();
		
		setSellStatus(false);
		startArrowAnimation(true, Constants.NORMAL_ANIMATION_INTERVERL);
	}
	
	private void setSellStatus(boolean isSell) {
		if (isSell) {
			mTargetClass = SellSearchResultFragment.class.getName();
			mSellTextView.setTextColor(Color.parseColor("#ffffff"));
			mRentTextView.setTextColor(Color.parseColor("#8affffff"));
		} else {
			mTargetClass = RentSearchResultFragment.class.getName();
			mSellTextView.setTextColor(Color.parseColor("#8affffff"));
			mRentTextView.setTextColor(Color.parseColor("#ffffff"));
		}
	}
	
	@Click(R.id.main_search)
	public void onSearch() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		final SearchByDistrictFragment searchFragment = GeneratedClassUtils.getInstance(SearchByDistrictFragment.class);
		searchFragment.tag = SearchByDistrictFragment.class.getName();
		searchFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		Bundle bundle = new Bundle();
		bundle.putString(Constants.SEARCH_TARGET_CLASS, mTargetClass);
		searchFragment.setArguments(bundle);
		searchFragment.setManager(getFragmentManager());
		searchFragment.setContainerId(R.id.main_container);
		searchFragment.setSelectListener(new SelectListener<Object>() {
			@Override
			public void onSelected(Object t) {
				init();
				ManyiUtils.closeKeyBoard(getActivity(), mSellTextView);
			}

			@Override
			public void onCanceled() {
				init();
				ManyiUtils.closeKeyBoard(getActivity(), mSellTextView);	
			}
		});
		searchFragment.show(SHOW_ADD_HIDE);	
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		super.onLoadFinished(loader, cursor);
		mList.setAdapter(mAdapter);
	}
	
	private void startArrowAnimation(boolean fromSell, long interval) {
		TranslateAnimation translateAnimation;
		if (fromSell) {
			translateAnimation = new TranslateAnimation(mArrowStartX, mArrowEndX, Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF);
		} else {
			translateAnimation = new TranslateAnimation(mArrowEndX, mArrowStartX, Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF);
		}
		 
		//translateAnimation.setRepeatMode(0);
		translateAnimation.setDuration(interval);
		translateAnimation.setFillAfter(true);
		mArrow.startAnimation(translateAnimation);
		translateAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mArrow.setVisibility(View.VISIBLE);
			}
		});
	}
}
