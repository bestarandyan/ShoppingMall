package com.manyi.mall.mine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.widget.pullrefresh.PullToRefreshBase;
import com.huoqiu.widget.pullrefresh.PullToRefreshBase.Mode;
import com.huoqiu.widget.pullrefresh.PullToRefreshBase.OnRefreshListener2;
import com.huoqiu.widget.pullrefresh.PullToRefreshListView;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.mine.AwardDetailRequest;
import com.manyi.mall.cachebean.mine.AwardDetailResponse;
import com.manyi.mall.cachebean.mine.AwardDetailResponse.AwardResponse;
import com.manyi.mall.common.Constants;
import com.manyi.mall.service.UcService;

@EFragment(R.layout.fragment_award_detail)
public class AwardDetailFragment extends SuperFragment<Object> implements OnRefreshListener2<ListView> {
	@FragmentArg
	String payTime;
	@ViewById(R.id.award_detail_list)
	PullToRefreshListView mDetailList;
	@ViewById(R.id.award_detail_back)
	Button mDetailBack;
	UcService mService;
	AwardDetailRequest mReq = new AwardDetailRequest();
	AwardDetailResponse mRes;
	@ViewById(R.id.noDataTv)
	TextView nodate;
	@ViewById(R.id.award_detail_back)
	Button awardDetailBack;
	private int indexPage = 0;
	private int maxResult = 25;
	private DetailAdpter mAdpter;

	@AfterViews
	void init() {
		awardDetailBack.setText(payTime + " 奖金");
		mDetailList.setOnRefreshListener(this);
		mDetailList.setMode(Mode.BOTH);
		getdata(true);
		showBottomOnHasMoreData();
	}

	@Background
	void getdata(Boolean b) {
		showBottomOnHasMoreData();
		try {
			int uid = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
			mReq.setUid(uid);
			mReq.setPayTime(payTime);
			mReq.setStart(indexPage);
			mReq.setMaxResult(maxResult);
			mReq.setMarkTime(null);
			if (b) {
				mRes = mService.awardDetail(mReq);
			} else {
				mRes = mService.awardDetail2(mReq);
			}

			if (mRes == null || mRes.getAwardList().toArray().length == 0) {
				noData();
			}
			upData();
		} catch (Exception e) {
			mDetailList.onRefreshComplete();
			throw e;
		}

	}

	@Background
	void loadMore() {
		if (this.getActivity() == null || this.getActivity().isFinishing() || mRes == null) {
			return;
		}

		int lengh = mRes.getAwardList().size();

		int start = lengh;
		mReq.setStart(start);
		mReq.setMaxResult(maxResult);
		int userId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
		mReq.setUid(userId);
		if (lengh > 0) {
			mReq.setMarkTime(mRes.getAwardList().get(0).getMarkTime());
		}
		try {
			List<AwardResponse> list = mService.awardDetail2(mReq).getAwardList();
			mRes.getAwardList().addAll(list);
			if (list.size() == 0 || list.size() < maxResult) {
				showBottomOnDataEnd();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (mRes == null || mRes.getAwardList().toArray().length == 0) {
				noData();
			}
			upData();

		}

	}

	@UiThread
	void upData() {
		notifySelected();
		if (this.getActivity() == null || this.getActivity().isFinishing() || mRes == null) {
			return;
		}

		if (mAdpter == null) {
			mAdpter = new DetailAdpter();
			mDetailList.setAdapter(mAdpter);
		} else {

			mAdpter.notifyDataSetChanged();
			mDetailList.onRefreshComplete();
		}

	}

	private void notifySelected() {
		if (getSelectListener() != null)
			getSelectListener().onSelected(null);

	}

	private class DetailAdpter extends BaseAdapter {
		private LayoutInflater mInflater = (LayoutInflater) getBackOpActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		@Override
		public int getCount() {

			return mRes.getAwardList().size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mRes.getAwardList().get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view;
			if (convertView == null) {
				view = mInflater.inflate(R.layout.item_mine_award, parent, false);
			} else {
				view = convertView;
			}

			TextView awardNum = (TextView) view.findViewById(R.id.awardNum);
			TextView awardDate = (TextView) view.findViewById(R.id.awardDate);
			TextView payType = (TextView) view.findViewById(R.id.payType);
			AwardResponse o = (AwardResponse) getItem(position);
			int i;
			try {
				i = (int) Double.valueOf(o.getAwardNum()).doubleValue();

			} catch (Exception e) {
				throw e;
			}
			awardNum.setText(i + "元");
			// private int payTypeId; // 发布奖金类型 //0,未付款（未打款）;1,付款成功,2,付款失败，3,付款中（已打款）
			Map<Integer, String> map = new HashMap<>();
			map.put(0, "#8a0000");
			map.put(1, "#12c1c4");
			map.put(2, "#12c1c4");
			map.put(3, "#ff5104");
			payType.setTextColor(Color.parseColor(map.get(o.getPayStateId())));
			payType.setText(o.getPayState());
			int le = 0;
			if (o.getPayType() != null) {// ff5104
				le = o.getPayType().length();
			}
			if (o.getHot() == 1) {
				SpannableStringBuilder builder = new SpannableStringBuilder(o.getPayType() + "    [热点小区]");
				// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
				ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.parseColor("#ff5104"));
				builder.setSpan(redSpan, le, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				// awardDate.setTextColor(Color.parseColor(map.get(o.getPayStateId())));
				awardDate.setText(builder);//
			} else {
				awardDate.setText(o.getPayType());//
			}

			return view;

		}

	}

	@Click(R.id.award_detail_back)
	void awardDetailBack() {
		remove();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		mDetailList.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		getdata(false);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		mDetailList.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		loadMore();
	}

	@UiThread
	public void showBottomOnHasMoreData() {
		// isDataEnd = false;
		mDetailList.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多！");
		mDetailList.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.pull_to_refresh_refreshing_label));
		mDetailList.getLoadingLayoutProxy(false, true).setReleaseLabel("松手加载！");
	}

	@UiThread
	public void showBottomOnDataEnd() {
		// isDataEnd = true;
		mDetailList.getLoadingLayoutProxy(false, true).setPullLabel("没有更多数据！");
		mDetailList.getLoadingLayoutProxy(false, true).setRefreshingLabel("没有更多数据！");
		mDetailList.getLoadingLayoutProxy(false, true).setReleaseLabel("没有更多数据！");

	}

	@UiThread
	void noData() {
		nodate.setVisibility(View.VISIBLE);
		// mDetailList.setVisibility(View.GONE);

	}
}
