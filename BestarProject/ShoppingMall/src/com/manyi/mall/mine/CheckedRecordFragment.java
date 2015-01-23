/**
 * 
 */
package com.manyi.mall.mine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.StringUtil;
import com.huoqiu.widget.pagertabstrip.PagerSlidingTabStrip;
import com.huoqiu.widget.pinnedlistview.PinnedHeaderListView;
import com.huoqiu.widget.pinnedlistview.SectionedBaseAdapter;
import com.huoqiu.widget.pullrefresh.PullToRefreshBase;
import com.huoqiu.widget.pullrefresh.PullToRefreshBase.Mode;
import com.huoqiu.widget.pullrefresh.PullToRefreshBase.OnRefreshListener2;
import com.huoqiu.widget.pullrefresh.PullToRefreshListView;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.mine.CheckedRecordsRequest;
import com.manyi.mall.cachebean.mine.CheckedRecordsResponse;
import com.manyi.mall.cachebean.mine.CheckedRecordsResponse.CheckedResponse;
import com.manyi.mall.cachebean.mine.CheckingRecordsRequest;
import com.manyi.mall.cachebean.mine.CheckingRecordsResponse;
import com.manyi.mall.cachebean.mine.CheckingRecordsResponse.ExamineRecoding;
import com.manyi.mall.common.Constants;
import com.manyi.mall.release.ReleasedRecordInfoFragment;
import com.manyi.mall.service.RentService;
import com.manyi.mall.service.SellService;
import com.manyi.mall.widget.refreshview.NLPullRefreshView;
import com.manyi.mall.widget.refreshview.NLPullRefreshView.RefreshListener;

/**
 * @author bestar
 * 
 */
@SuppressLint("SimpleDateFormat")
@EFragment(R.layout.fragment_checked_record)
public class CheckedRecordFragment extends SuperFragment<Object> implements RefreshListener {
	@ViewById(R.id.topTabs)
	PagerSlidingTabStrip mTopTabs;

	@ViewById(R.id.recordPager)
	ViewPager mPager;

	PinnedHeaderListView mCheckedListView;

	PullToRefreshListView mCheckingListView;

	ViewpageAdapter mAdapter;

	List<View> mPagers = new ArrayList<View>();

	CheckedSectionListAdapter mCheckedAdapter = null;
	CheckingListAdapter mCheckingAdapter = null;

	CheckedRecordsResponse mCheckedResponse;
	CheckingRecordsResponse mCheckingResponse;

	
	RentService mRentService;
	SellService mSellService;

	List<Map<String, Object>> mCheckedList = new ArrayList<Map<String, Object>>();
	List<ExamineRecoding> mCheckingList = new ArrayList<ExamineRecoding>();

	TextView checkingCountTv;
	NLPullRefreshView mRefreshableView;

	LinearLayout checkingTopLayout;
	
	
	@FragmentArg
	String releaseRecordCheckType;
	
	

	@AfterViews
	void initData() {
		mPagers.add(getCheckedView());
		mPagers.add(getCheckingView());
		mAdapter = new ViewpageAdapter();
		mPager.setAdapter(mAdapter);
		getCheckedRecords(false);

		mTopTabs.setViewPager(mPager);
		mTopTabs.setOnPageChangeListener(new PageSelectListener());

	}

	class PageSelectListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int position) {
			if (position == 0 && (mCheckedResponse == null || mCheckedResponse.getResult() == null)) {
				// setProgressVisibility(mCheckedProgressLayout,View.VISIBLE);
				getCheckedRecords(false);
			} else if (position == 1 && (mCheckingResponse == null)) {
				// setProgressVisibility(mCheckingProgressLayout,View.VISIBLE);
				getCheckingRecords(false, Constants.DOWN_REFRESH);
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}
	}

	/**
	 * 审核通过
	 * @param isRefresh
	 */
	@Background
	void getCheckedRecords(boolean isRefresh) {
		SharedPreferences sharedPreferences = getBackOpActivity().getSharedPreferences("LOGIN_times", Context.MODE_PRIVATE);
		CheckedRecordsRequest checkedRequest = new CheckedRecordsRequest();
		checkedRequest.setUid(sharedPreferences.getInt("uid", 0));
		try {
			if (isRefresh) {
				if(releaseRecordCheckType == Constants.CHECK_RECORD_RENT){ //出租
					mCheckedResponse = mRentService.getCheckedRentRecordsNoLoading(checkedRequest);
				}else if(releaseRecordCheckType == Constants.CHECK_RECORD_SELL){ //出售
					mCheckedResponse = mSellService.getCheckedSellRecordsNoLoading(checkedRequest);
				}
			} else {
				if(releaseRecordCheckType == Constants.CHECK_RECORD_RENT){
					mCheckedResponse = mRentService.getCheckedRentRecords(checkedRequest);
				}else if(releaseRecordCheckType == Constants.CHECK_RECORD_SELL){ 
					mCheckedResponse = mSellService.getCheckedSellRecords(checkedRequest);
				}
			}
			notifyCheckedList(isRefresh);
		} catch (Exception e) {
			showErrorDialog(e);
		}

		// setProgressVisibility(mCheckedProgressLayout,View.GONE);
	}

	@UiThread
	void showErrorDialog(Exception e) {
		mRefreshableView.finishRefresh();
		if (getActivity() == null || e.getMessage() == null)
			return;
		DialogBuilder.showSimpleDialog(e.getMessage(), getActivity());
	}

	/**
	 * 审核中
	 * @param isRefresh
	 * @param refreshType
	 */
	@Background
	void getCheckingRecords(boolean isRefresh, int refreshType) {
		SharedPreferences sharedPreferences = getBackOpActivity().getSharedPreferences("LOGIN_times", Context.MODE_PRIVATE);
		CheckingRecordsRequest request = new CheckingRecordsRequest();
		request.setUid(sharedPreferences.getInt("uid", 0));
		if (refreshType == Constants.UP_REFRESH) {// 上加载更多
			request.setStart(mCheckingList.size());
			request.setMaxResult(mCheckingList.size() + 25);
		} else if (refreshType == Constants.DOWN_REFRESH) {// 下拉刷新数据
			request.setStart(0);
			request.setMaxResult(25);

		}
		try {
			if(releaseRecordCheckType == Constants.CHECK_RECORD_RENT){
				mCheckingResponse = mRentService.getCheckingRentRecords(request);
			}else if(releaseRecordCheckType == Constants.CHECK_RECORD_SELL){
				mCheckingResponse = mSellService.getCheckingSellRecords(request);
			}
			if (mCheckingResponse.getErrorCode() == 0) {
				if (refreshType == Constants.UP_REFRESH) {
					mCheckingList.addAll(mCheckingResponse.getExamineRecodList());
				} else {
					mCheckingList.clear();
					mCheckingList = mCheckingResponse.getExamineRecodList();
				}
				notifyCheckingList(isRefresh);
			}
		} catch (Exception e) {
			notifyCheckingList(isRefresh);
		}
	}

	private View getCheckedView() {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_checked, null);
		mRefreshableView = (NLPullRefreshView) view.findViewById(R.id.refreshable_view);
		mCheckedListView = (PinnedHeaderListView) view.findViewById(R.id.checkedListView);
		mRefreshableView.setRefreshListener(this);
		notifyCheckedList(false);
		return view;
	}

	@UiThread
	void notifyCheckedList(boolean isRefresh) {
		if (isRefresh) {
			mRefreshableView.finishRefresh();
		}
		TextView emptyView = new TextView(getActivity());
		emptyView.setText("无审核中房源，请先发布！");
		emptyView.setTextColor(Color.parseColor("#333333"));
		emptyView.setGravity(Gravity.CENTER);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		emptyView.setLayoutParams(layoutParams);
		mCheckedListView.setEmptyView(emptyView);
		if (mCheckedResponse == null || mCheckedResponse.getErrorCode() != 0 || mCheckedResponse.getResult() == null) {
			return;
		}
		mCheckedAdapter = new CheckedSectionListAdapter(mCheckedResponse);
		mCheckedListView.setAdapter(mCheckedAdapter);

	}

	@UiThread
	void setProgressVisibility(View view, int visibility) {
		view.setVisibility(visibility);
	}

	private View getCheckingView() {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_checking, null);
		mCheckingListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
		checkingCountTv = (TextView) view.findViewById(R.id.checkingCountTv);
		checkingTopLayout = (LinearLayout) view.findViewById(R.id.checkingTopLayout);
		// mCheckingProgressLayout = (LinearLayout) view.findViewById(R.id.progressBarLayout);
		TextView emptyView = new TextView(getActivity());
		emptyView.setText("无审核中房源，请先发布！");
		emptyView.setTextColor(Color.parseColor("#333333"));
		emptyView.setGravity(Gravity.CENTER);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		emptyView.setLayoutParams(layoutParams);
//		 mCheckingListView.setEmptyView(emptyView);
		mCheckingListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				getCheckingRecords(true, Constants.DOWN_REFRESH);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				getCheckingRecords(true, Constants.UP_REFRESH);
			}
		});
		mCheckingListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (CheckDoubleClick.isFastDoubleClick())
					return;
				ManyiAnalysis.onEvent(getActivity(), "CheckingRecordItemClick");
				ReleasedRecordInfoFragment releasedRecordInfoFragment = GeneratedClassUtils.getInstance(ReleasedRecordInfoFragment.class);
				Bundle args = new Bundle();
				ExamineRecoding response = mCheckingList.get(position - 1);
				if(releaseRecordCheckType == Constants.CHECK_RECORD_SELL){ //出售
					args.putString("releaseTypeId", Constants.RECORD_INFO_SELL); 
				}else if(releaseRecordCheckType ==Constants.CHECK_RECORD_RENT){ //出租
					args.putString("releaseTypeId", Constants.RECORD_INFO_RENT);
				}
				args.putInt("historyId", response.getHistoryId());
				args.putInt("houseId", response.getHouseId());
				args.putInt("typeId", response.getTypeId());
				args.putString("typeName", response.getTypeName());
				releasedRecordInfoFragment.setArguments(args);
				releasedRecordInfoFragment.setContainerId(R.id.main_container);
				releasedRecordInfoFragment.tag = ReleasedRecordInfoFragment.class.getName();
				releasedRecordInfoFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
						R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);
				releasedRecordInfoFragment.setManager(getFragmentManager());
				releasedRecordInfoFragment.show(SuperFragment.SHOW_ADD_HIDE);
			}
		});

		mCheckingListView.setMode(Mode.BOTH);
		if (mAdapter != null) {
			mCheckingListView.setAdapter(mCheckingAdapter);
			mAdapter.notifyDataSetChanged();
		}
		showBottomOnHasMoreData();
		notifyCheckingList(false);
		return view;
	}

	public class CheckingListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mCheckingList.size();
		}

		@Override
		public Object getItem(int position) {
			return mCheckingList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint({ "ResourceAsColor", "InlinedApi" })
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_select_config_content, null);
				holder.contentTv = (TextView) convertView.findViewById(R.id.contentTv);
				holder.dateTv = (TextView) convertView.findViewById(R.id.sendDate);
				holder.stateTv = (TextView) convertView.findViewById(R.id.stateTv);
				holder.hotCityTv = (TextView) convertView.findViewById(R.id.hotCity);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// Map<String,String> map = ((ArrayList<Map<String,String>>)mList.get(section).get("itemList")).get(position);
			ExamineRecoding response = mCheckingList.get(position);
			String content;
			String name = null;
			if (!StringUtil.isEmptyOrNull(response.getSubEstateName())) {
				name = response.getEstateName() + "  " + response.getSubEstateName();
			} else {
				name = response.getEstateName();
			}
			if (("").equals(response.getBuildingNameStr())) {
				content = name;
			} else {
				content = name + "·" + response.getBuildingNameStr();
			}

			String dateString = response.getPublishDate() + "  " + response.getHouseStateStr();
			String stateStr = response.getStatusStr();
			holder.contentTv.setText(content);
			holder.dateTv.setText(dateString);
			holder.stateTv.setText(stateStr);
			holder.stateTv.setTextColor(Color.parseColor("#8a000000"));
			int isHot = response.getHot();
			if (isHot == 1) {
				holder.hotCityTv.setVisibility(View.VISIBLE);
				holder.hotCityTv.setText("[热点小区]");
			} else {
				holder.hotCityTv.setVisibility(View.GONE);
			}
			return convertView;
		}

		public class ViewHolder {
			TextView contentTv, dateTv, stateTv, hotCityTv;
		}
	}

	public void showBottomOnHasMoreData() {
		mCheckingListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多！");
		mCheckingListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.pull_to_refresh_refreshing_label));
		mCheckingListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松手加载！");
	}

	public void showBottomOnDataEnd() {
		mCheckingListView.getLoadingLayoutProxy(false, true).setPullLabel("没有更多数据！");
		mCheckingListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("没有更多数据！");
		mCheckingListView.getLoadingLayoutProxy(false, true).setReleaseLabel("没有更多数据！");
	}

	@UiThread
	void notifyCheckingList(boolean isRefresh) {
		if (isRefresh) {
			mCheckingListView.onRefreshComplete();
		}
		if (mCheckingList == null) {
			return;
		}
		if (mCheckingList.size() == 0) {
			mCheckingAdapter = new CheckingListAdapter();
			mCheckingListView.setAdapter(mCheckingAdapter);
		} else {
			mCheckingAdapter.notifyDataSetChanged();
			checkingTopLayout.setVisibility(View.VISIBLE);
		}
		if (checkingCountTv != null && mCheckingResponse != null) {
			checkingCountTv.setText("您有" + mCheckingResponse.getRecordCount() + "套房源审核中");
		}

	}

	/**
	 * view page Adapater
	 * 
	 * @author jason
	 * 
	 */
	class ViewpageAdapter extends PagerAdapter {
		// 选择颜色 ： 12c1c4

		private final String[] TITLES = { "已审核", "审核中" };

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(mPagers.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = mPagers.get(position);
			container.addView(view);
			view.setTag(position);
			return view;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}

	public class CheckedSectionListAdapter extends SectionedBaseAdapter {

		CheckedRecordsResponse responses;

		// 选择颜色 ： 12c1c4
		public CheckedSectionListAdapter(CheckedRecordsResponse responses) {
			this.responses = responses;
		}

		// 审核成功：文字颜色12c1c4 失败：8a000000
		@Override
		public Object getItem(int section, int position) {
			return responses.getResult().get(section).getExamineRecodList().get(position);
		}

		@Override
		public long getItemId(int section, int position) {
			return position;
		}

		@Override
		public int getSectionCount() {
			return responses.getResult().size();
		}

		@Override
		public int getCountForSection(int section) {
			return responses.getResult().get(section).getExamineRecodList().size();
		}

		@Override
		public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_select_config_content, null);
				holder.contentTv = (TextView) convertView.findViewById(R.id.contentTv);
				holder.dateTv = (TextView) convertView.findViewById(R.id.sendDate);
				holder.stateTv = (TextView) convertView.findViewById(R.id.stateTv);
				holder.hotCityTv = (TextView) convertView.findViewById(R.id.hotCity);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// Map<String,String> map = ((ArrayList<Map<String,String>>)mList.get(section).get("itemList")).get(position);
			final CheckedResponse response = responses.getResult().get(section).getExamineRecodList().get(position);
			String content;
			String name = null;
			if (!StringUtil.isEmptyOrNull(response.getSubEstateName())) {
				name = response.getEstateName() + response.getSubEstateName();
			} else {
				name = response.getEstateName();
			}

			if (("").equals(response.getBuildingNameStr())) {
				content = name;
			} else {
				content = name + "·" + response.getBuildingNameStr();
			}

			String dateString = response.getPublishDate() + "  " + response.getHouseStateStr();
			int state = response.getStatus();
			String stateStr = response.getStatusStr();
			holder.contentTv.setText(content);
			holder.dateTv.setText(dateString);
			if (state == 1) {// 审核成功
				holder.stateTv.setTextColor(Color.parseColor("#12c1c4"));
			} else if (state == 3) {// 审核失败
				holder.stateTv.setTextColor(Color.parseColor("#8a000000"));
			}
			holder.stateTv.setText(stateStr);

			int isHot = response.getHot();
			if (isHot == 1) {
				holder.hotCityTv.setVisibility(View.VISIBLE);
				holder.hotCityTv.setText("[热点小区]");
			} else {
				holder.hotCityTv.setVisibility(View.GONE);
			}
			convertView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					ManyiAnalysis.onEvent(getActivity(), "CheckedRecordItemClick");
					ReleasedRecordInfoFragment releasedRecordInfoFragment = GeneratedClassUtils
							.getInstance(ReleasedRecordInfoFragment.class);
					Bundle args = new Bundle();
					if(releaseRecordCheckType == Constants.CHECK_RECORD_SELL){ //出售
						args.putString("releaseTypeId", Constants.RECORD_INFO_SELL); 
					}else if(releaseRecordCheckType ==Constants.CHECK_RECORD_RENT){ //出租
						args.putString("releaseTypeId", Constants.RECORD_INFO_RENT);
					}
					args.putInt("historyId", response.getHistoryId());
					args.putInt("houseId", response.getHouseId());
					args.putInt("typeId", response.getTypeId());
					args.putString("typeName", response.getTypeName());
					releasedRecordInfoFragment.setArguments(args);
					releasedRecordInfoFragment.tag = ReleasedRecordInfoFragment.class.getName();
					releasedRecordInfoFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
							R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);
					releasedRecordInfoFragment.setContainerId(R.id.main_container);
					releasedRecordInfoFragment.setManager(getFragmentManager());
					releasedRecordInfoFragment.show(SuperFragment.SHOW_ADD_HIDE);
				}
			});
			return convertView;
		}

		public class ViewHolder {
			TextView contentTv, dateTv, stateTv, hotCityTv;
		}

		public class SectionHolder {
			TextView titleTv;
		}

		@Override
		public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
			SectionHolder holder = null;
			if (convertView == null) {
				holder = new SectionHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_select_config_title, null);
				holder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
				convertView.setTag(holder);
			} else {
				holder = (SectionHolder) convertView.getTag();
			}
			String countString = " 审核成功" + responses.getResult().get(section).getRecordCount() + "条";
			String title = responses.getResult().get(section).getResultDateStr();

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

			Date localDate = new Date(System.currentTimeMillis());// 获取当前时间
			String dateString = formatter.format(localDate);
			Date dateData = null;
			try {
				dateData = formatter.parse(title);
				localDate = formatter.parse(dateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if (localDate.getTime() == dateData.getTime()) {
				title = "今天";
			} else if (localDate.getTime() - dateData.getTime() == 1 * 24 * 60 * 60 * 1000) {
				title = "昨天";
			} else {
				title = responses.getResult().get(section).getResultDateStr();
			}
			holder.titleTv.setText(title + countString);
			return convertView;
		}
	}

	@Click(R.id.mrecordback)
	public void onBack() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		remove();
	}

	@Override
	public void onRefresh(NLPullRefreshView view) {
		getCheckedRecords(true);
	}

	@Click(R.id.review_rule)
	public void gotoReviewContent() {
		CommonProblemFragment commonProblemFragment = GeneratedClassUtils.getInstance(CommonProblemFragment.class);
		Bundle argsBundle = new Bundle();
		argsBundle.putBoolean("isFromReviewRule", true);
		commonProblemFragment.setArguments(argsBundle);
		commonProblemFragment.tag = CommonProblemFragment.class.getName();
		commonProblemFragment.setManager(getFragmentManager());
		commonProblemFragment.setContainerId(R.id.main_container);
		commonProblemFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);

		commonProblemFragment.show(SHOW_ADD_HIDE);
	}
}
