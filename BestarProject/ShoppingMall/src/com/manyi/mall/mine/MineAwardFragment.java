package com.manyi.mall.mine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.widget.pullrefresh.PullToRefreshBase;
import com.huoqiu.widget.pullrefresh.PullToRefreshBase.Mode;
import com.huoqiu.widget.pullrefresh.PullToRefreshBase.OnRefreshListener2;
import com.huoqiu.widget.pullrefresh.PullToRefreshListView;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.mine.MineBonusCollectRequest;
import com.manyi.mall.cachebean.mine.MineBonusCollectResponse;
import com.manyi.mall.cachebean.mine.MineBonusCollectResponse.MergerAwardResponse;
import com.manyi.mall.common.Constants;
import com.manyi.mall.release.ReleasedSellFragment;
import com.manyi.mall.service.UcService;
import com.manyi.mall.user.RuleContentFragment;

@EFragment(R.layout.fragment_mine_bonus)
public class MineAwardFragment extends SuperFragment<Object> {
	MineBonusCollectResponse mRes;
	Adapter mAdapter;
	MineBonusCollectRequest mReq = new MineBonusCollectRequest();
	private UcService mUserService;
	@ViewById(R.id.nodata)
	public LinearLayout mNoData;
	@ViewById(R.id.mine_bonus_list)
	public PullToRefreshListView mPullRefreshListView;

	private int mPageSize = 25;

	@AfterViews
	void refreshView() {
		if (mAdapter != null) {
			mPullRefreshListView.setAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();

			mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
				@Override
				public void onPullDownToRefresh(PullToRefreshBase<ListView> ListView) {
					String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
							DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
					mPullRefreshListView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
					loadData();
				}

				@Override
				public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
					loadMore();
				}

			});

			if (mAdapter != null) {
				mPullRefreshListView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
			}
			mPullRefreshListView.setMode(Mode.BOTH);
			showBottomOnHasMoreData();
		}

		if (isFirst) {
			loadData();
		}
		mPullRefreshListView.setMode(Mode.BOTH);
		showBottomOnHasMoreData();

		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				inDetail(position);
			}

		});
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				getData(false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadMore();
			}

		});

	}

	boolean isFirst = true;

	/**
	 * 首次加载数据
	 */

	private void loadData() {

		getData(true);
		isFirst = false;
	}

	private void inDetail(int position) {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		final AwardDetailFragment awardDetailFragment = GeneratedClassUtils.getInstance(AwardDetailFragment.class);
		awardDetailFragment.tag = AwardDetailFragment.class.getName();
		Bundle bundle = new Bundle();
		bundle.putString("payTime", mRes.getAward4MeSumList().get(position - 1).getPayTime());
		awardDetailFragment.setArguments(bundle);
		awardDetailFragment.setContainerId(R.id.main_container);
		awardDetailFragment.setManager(getFragmentManager());
		awardDetailFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		awardDetailFragment.setSelectListener(new SelectListener<Object>() {

			@Override
			public void onSelected(Object t) {
				notifySelected();
			}

			@Override
			public void onCanceled() {
				// TODO Auto-generated method stub

			}
		});
		awardDetailFragment.show(SHOW_ADD_HIDE);

	}

	@Background
	void getData(boolean b) {

		showBottomOnHasMoreData();

		int uid = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
		mReq.setUid(uid);
		mReq.setStart(0);
		mReq.setMaxResult(mPageSize);
		// mReq.setMarkTime(null);
		try {
			if (b) {
				mRes = mUserService.award4MeSum(mReq);
			} else {
				mRes = mUserService.award4MeSum2(mReq);
			}

			if (mRes == null || mRes.getAward4MeSumList().size() == 0) {

				noData();
			}
			upData();
		} catch (RestException e) {
			onRefreshComplete();
			throw e;
		} catch (Exception e) {
			onRefreshComplete();
			throw e;
		}

	}

	@UiThread
	void onRefreshComplete() {
		if (mPullRefreshListView != null) {
			mPullRefreshListView.onRefreshComplete();
		}

	}

	@UiThread
	void upData() {

		if (this.getActivity() == null || this.getActivity().isFinishing() || mRes == null) {
			return;
		}
		if (mAdapter == null) {
			mAdapter = new Adapter((LayoutInflater) getBackOpActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), mRes);
			mPullRefreshListView.setAdapter(mAdapter);
		} else {

			mAdapter.res = mRes;
			mAdapter.notifyDataSetChanged();
			mPullRefreshListView.onRefreshComplete();
		}

	}

	public void notifySelected() {
		if (getSelectListener() != null)
			getSelectListener().onSelected(null);

	}

	@Click(R.id.sellrealse)
	void sellRealse() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		final ReleasedSellFragment releasedSellFragment = GeneratedClassUtils.getInstance(ReleasedSellFragment.class);
		releasedSellFragment.tag = ReleasedSellFragment.class.getName();
		releasedSellFragment.setManager(getFragmentManager());
		releasedSellFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		releasedSellFragment.show(SHOW_ADD_HIDE);

	}

	public class Adapter extends BaseAdapter {
		MineBonusCollectResponse res = null;
		private LayoutInflater mInflater;
		private int currentPosition = -1;

		public Adapter(LayoutInflater mInflater, MineBonusCollectResponse res) {
			this.res = res;
			this.mInflater = mInflater;
		}

		private int mResource = R.layout.item_mine_award;

		public void notifyDataSetChanged(int currentPosition) {
			this.currentPosition = currentPosition;
			super.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return res.getAward4MeSumList().size();
		}

		@Override
		public Object getItem(int position) {
			return res.getAward4MeSumList().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			View view;

			if (convertView == null) {
				view = mInflater.inflate(mResource, parent, false);

			} else
				view = convertView;

			if (currentPosition - 1 == position)
				view.setBackgroundColor(0xecf0f1);
			else
				view.setBackgroundColor(0xecf0f1);

			// TextView townName = (TextView) view.findViewById(R.id.townName);
			TextView awardNum = (TextView) view.findViewById(R.id.awardNum);
			TextView awardDate = (TextView) view.findViewById(R.id.awardDate);
			TextView payType = (TextView) view.findViewById(R.id.payType);
			MergerAwardResponse o = (MergerAwardResponse) getItem(position);
			int i;
			try {
				i = (int) Double.valueOf(o.getAwardNum()).doubleValue();

			} catch (Exception e) {
				throw e;
			}
			awardNum.setText(i + "元" + ",  " + "共" + o.getPayCount() + "笔");
			// private int payTypeId; // 发布奖金类型 //0,未付款（未打款）;1,付款成功,2,付款失败，3,付款中（已打款）
			Map<Integer, String> map = new HashMap<>();
			map.put(0, "#8a0000");
			map.put(1, "#12c1c4");
			map.put(2, "#12c1c4");
			map.put(3, "#ff5104");

			payType.setTextColor(Color.parseColor(map.get(o.getPayStateId())));
			payType.setText(o.getPayState());
			awardDate.setText(o.getPayTime());
			payType.setText(o.getPayState());
			awardDate.setText(o.getPayTime());
			return view;
		}
	}

	@Click(R.id.minehomeback)
	void mineHomeBack() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		remove();
	}
	
	@Click(R.id.goto_award_content)
	public void gotoAwardRuleContent(){
		RuleContentFragment ruleContentFragment = GeneratedClassUtils.getInstance(RuleContentFragment.class);
		Bundle argsBundle = new Bundle();
		argsBundle.putInt("type", Constants.GOTO_AWARD_CONTENT);
		ruleContentFragment.setArguments(argsBundle);
		ruleContentFragment.tag = RuleContentFragment.class.getName();
		ruleContentFragment.setManager(getFragmentManager());
		ruleContentFragment.setContainerId(R.id.main_container);
		ruleContentFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
				R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);

		ruleContentFragment.show(SHOW_ADD_HIDE);
	}

	@Background
	public void loadMore() {
		if (this.getActivity() == null || this.getActivity().isFinishing() || mRes == null) {
			return;
		}

		int lengh = mRes.getAward4MeSumList().size();

		int start = lengh;
		mReq.setStart(start);
		mReq.setMaxResult(mPageSize);
		int userId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
		mReq.setUid(userId);
		if (lengh > 0) {
			mReq.setMarkTime(mRes.getAward4MeSumList().get(0).getMarkTime());
		}
		try {
			List<MergerAwardResponse> list = mUserService.award4MeSum2(mReq).getAward4MeSumList();
			mRes.getAward4MeSumList().addAll(list);
			if (list.size() == 0 || list.size() < mPageSize) {
				showBottomOnDataEnd();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (mRes == null || mRes.getAward4MeSumList().toArray().length == 0) {
				noData();
			}
			upData();

		}

	}

	@UiThread
	public void showBottomOnHasMoreData() {
		// isDataEnd = false;
		mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多！");
		mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.pull_to_refresh_refreshing_label));
		mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松手加载！");
	}

	@UiThread
	public void showBottomOnDataEnd() {
		// isDataEnd = true;
		mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("没有更多数据！");
		mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("没有更多数据！");
		mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("没有更多数据！");

	}

	@UiThread
	void noData() {

		mPullRefreshListView.setVisibility(View.GONE);

	}

}
