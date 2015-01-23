package com.manyi.mall.release;

import java.math.BigDecimal;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.huoqiu.framework.util.StringUtil;
import com.huoqiu.widget.pullrefresh.PullToRefreshBase;
import com.huoqiu.widget.pullrefresh.PullToRefreshBase.Mode;
import com.huoqiu.widget.pullrefresh.PullToRefreshBase.OnRefreshListener2;
import com.huoqiu.widget.pullrefresh.PullToRefreshListView;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.user.HouseInfo;
import com.manyi.mall.cachebean.user.HouseResponse;
import com.manyi.mall.release.RentHouseTypeSelectFragment.BedroomOption;
import com.manyi.mall.release.RentHousesizeSelectFragment.SpaceAreaOption;
import com.manyi.mall.release.SellHuosePriceSelectFragment.PriceOption;
import com.manyi.mall.search.SearchResultFragment;
import com.manyi.mall.service.HouseResourceService;

@EFragment(R.layout.fragment_rent_search)
public class SellSearchResultFragment extends SearchResultFragment {
	public HouseResourceService mHousereSourceService;

	Adapter mAdapter;
	@ViewById(R.id.pull_refresh_list2)
	public PullToRefreshListView mPullRefreshListView;

	private boolean isDataEnd = false;

	public boolean flag;
	List<HouseInfo> mList = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	public void init() {
		mPullRefreshListView.setVisibility(View.VISIBLE);
		loadData();
	}

	boolean isFirst = true;

	/**
	 * 首次加载数据
	 */
	private void loadData() {

		getData(true);

		isFirst = false;

	}

	@AfterViews
	public void requestList() {

		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if (CheckDoubleClick.isFastDoubleClick())
					return;

				mAdapter.notifyDataSetChanged(position);
				intentToDetail(position);

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

		// 第三个列表
		final SellHuosePriceSelectFragment housePriceFragment = GeneratedClassUtils.getInstance(SellHuosePriceSelectFragment.class);
		housePriceFragment.setManager(getFragmentManager());
		housePriceFragment.setTag(SellHuosePriceSelectFragment.class.getName());
		// housePriceFragment.setCustomAnimations(R.anim.push_left_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
		// R.anim.push_left_out);
		housePriceFragment.setSelectListener(new SelectListener<PriceOption>() {
			@Override
			public void onSelected(PriceOption source) {
				mReq.setEndPrice(source.getEndPrice());
				mReq.setStartPrice(source.getStartPrice());
				mPriceButton.setText(source.getLabel3());
				if (source.getLabel3().equals("不限")) {
					mPriceButton.setText("价格 ");
				}
				loadByConditions();
			}

			@Override
			public void onCanceled() {
			}
		});
		housePriceFragment.setTrigger(mPriceButton);

		// 第一个列表
		final RentHousesizeSelectFragment houseSizeFragment = GeneratedClassUtils.getInstance(RentHousesizeSelectFragment.class);
		houseSizeFragment.setManager(getFragmentManager());
		houseSizeFragment.setTag(RentHousesizeSelectFragment.class.getName());
		houseSizeFragment.setSelectListener(new SelectListener<SpaceAreaOption>() {
			@Override
			public void onSelected(SpaceAreaOption source) {
				mReq.setEndSpaceArea(source.getEndSpaceArea());
				mReq.setStartSpaceArea(source.getStartSpaceArea());
				mAreaButton.setText(source.getLabel2());

				if (source.getLabel2().equals("不限")) {
					mAreaButton.setText("面积 ");
				}

				loadByConditions();
			}

			@Override
			public void onCanceled() {
			}
		});
		houseSizeFragment.setTrigger(mAreaButton);

		// 第二个列表
		final RentHouseTypeSelectFragment houseTypeFragment = GeneratedClassUtils.getInstance(RentHouseTypeSelectFragment.class);

		houseTypeFragment.setManager(getFragmentManager());
		houseTypeFragment.setTag(RentHouseTypeSelectFragment.class.getName());
		houseTypeFragment.setSelectListener(new SelectListener<BedroomOption>() {
			@Override
			public void onSelected(BedroomOption source) {
				mReq.setBedroomSum(source.getBedroomSum());
				mHouseButton.setText(source.getLabel1());
				loadByConditions();
				if (source.getLabel1().equals("不限")) {
					mHouseButton.setText("房型 ");
				}
			}

			@Override
			public void onCanceled() {
			}
		});
		houseTypeFragment.setTrigger(mHouseButton);
		if (estate != null) {
			mReq.setAreaId(estate.getEstateId());
			mTextSearch.setText(estate.getEstateName());
			// getData();
		}

		if (area != null) {
			mReq.setAreaId(area.getAreaId());
			mTextSearch.setText(area.getName());
			// getData();
		}
		mPullRefreshListView.setMode(Mode.PULL_FROM_END);
		showBottomOnHasMoreData();
		// getData();
		mAdapter = new Adapter((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), mRes);
		mPullRefreshListView.setAdapter(mAdapter);
	}

	@Background
	@Override
	public void loadMore() {
		if (!isDataEnd) {
			int lengh = mRes.getHouseList().size();
			mReq.setStart(lengh);
			mReq.setMax(25);

			try {
				List<HouseInfo> list = mHousereSourceService.findSellByPage2(mReq).getHouseList();
				mRes.getHouseList().addAll(list);
				if (list.size() == 0 || list.size() < 25) {
					showBottomOnDataEnd();
				}
				setCatchLayoutVisible(View.GONE);
			} catch (final Exception e) {
				catchSet(e);

				mCurrentFlag = FLAG_LOADMORE;
				throw e;
			} finally {
				upData();
			}
		} else {
			upData();
		}
	}

	@Background
	@Override
	public void getData(boolean b) {
		showBottomOnHasMoreData();

		mReq.setStart(0);
		mReq.setMax(25);

		try {
			if (b) {
				mRes = mHousereSourceService.findSellByPage(mReq);
			} else {
				mRes = mHousereSourceService.findSellByPage2(mReq);

			}
			setCatchLayoutVisible(View.GONE);
		} catch (final Exception e) {
			catchSet(e);
			mCurrentFlag = FLAG_GETDATA;
			throw e;

		} finally {
			upData();
		}
	}

	@UiThread
	void catchSet(Exception e) {
		// DialogBuilder.showSimpleDialog(e.getMessage(), getActivity());
		setCatchLayoutVisible(View.VISIBLE);
		checkInternetTv.setText(e.getMessage());
		mAgainBtn.setClickable(true);
	}

	@UiThread
	void setCatchLayoutVisible(int state) {
		catchLayout.setVisibility(state);
		catchLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
	}

	@Click(R.id.againBtn)
	void againBtnClick() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		switch (mCurrentFlag) {
		case FLAG_GETDATA:
			getData(true);
			break;
		case FLAG_CONDITION:
			loadByConditions();
			break;
		case FLAG_LOADMORE:
			loadMore();
			break;
		}
	}

	@Background
	@Override
	public void loadByConditions() {
		showBottomOnHasMoreData();

		mReq.setStart(0);
		mReq.setMax(25);

		try {
			mRes = mHousereSourceService.findSellByPage(mReq);
			setCatchLayoutVisible(View.GONE);
		} catch (final Exception e) {
			catchSet(e);
			mCurrentFlag = FLAG_CONDITION;
			throw e;
		} finally {
			upData();
		}
	}

	@UiThread
	public void intentToDetailing(int position, FragmentManager manager) {
		SellInfosFragment sellInfosFragment = GeneratedClassUtils.getInstance(SellInfosFragment.class);

		Bundle nBundle = new Bundle();
		nBundle.putInt("houseId", mRes.getHouseList().get(position - 1).getHouseId());
		sellInfosFragment.setArguments(nBundle);
		sellInfosFragment.tag = SellInfosFragment.class.getName();
		sellInfosFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		sellInfosFragment.setManager(manager);

		sellInfosFragment.setSelectListener(new SelectListener<Integer>() {

			@Override
			public void onSelected(Integer t) {
				mList = mRes.getHouseList();
				for (int i = 0; i < mList.size(); i++) {
					if (t == mList.get(i).getHouseId()) {
						// mList.get(i).setSourceState(2);

					}
				}
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onCanceled() {
			}
		});
		if (sellInfosFragment != null) {
			sellInfosFragment.show();
		}
	}

	@UiThread
	public void showState(int state) {
		if (state == 2) {
			DialogBuilder.showSimpleDialog("该房源还在审核中", getBackOpActivity());

		} else if (state == 3) {
			DialogBuilder.showSimpleDialog("审核未通过不能查看", getBackOpActivity());
		}
	}

	@Click(R.id.rentresultback)
	void back() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		if (this.isDetached() || getActivity() == null)
			return;

		ManyiUtils.closeKeyBoard(getActivity(), mPullRefreshListView);
		notifySelected(null);
	}

	public class Adapter extends BaseAdapter {
		HouseResponse res = null;
		private LayoutInflater mInflater;
		private int currentPosition = -1;

		public Adapter(LayoutInflater mInflater, HouseResponse res) {
			this.res = res;
			this.mInflater = mInflater;
		}

		private int mResource = R.layout.item_sell_home_list;

		public void notifyDataSetChanged(int currentPosition) {
			this.currentPosition = currentPosition;
			super.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return res.getHouseList().size();
		}

		@Override
		public Object getItem(int position) {
			return res.getHouseList().get(position);
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

			TextView estateName = (TextView) view.findViewById(R.id.estateName_new);
			TextView spaceArea = (TextView) view.findViewById(R.id.spaceArea_new);
			TextView price = (TextView) view.findViewById(R.id.price_new);
			TextView picNum = (TextView) view.findViewById(R.id.building_new_image);
			HouseInfo o = (HouseInfo) getItem(position);
			StringBuffer stringBuffer = null;

			// if（ Building = "" 只显示小区名字 ） else { if(SubEstateName = null){不显示子划分 }else{ 显示子划分} }
			if (!o.getBuilding().equals("")) {
				String Building = o.getBuilding();
				String[] newBuilding = Building.split("-");
				String str1 = newBuilding[0];// 比如 20-3 这里str1 就是 20 str2就是3
				stringBuffer = new StringBuffer();
				if (newBuilding.length == 1) {

					switch (Building) {
					case "0":
						stringBuffer.append("独栋");
						break;

					default:
						stringBuffer.append(Building + "号");
						break;
					}
				} else {

					String str2 = newBuilding[1];
					String num1 = String.valueOf(str1);
					String num2 = String.valueOf(str2);
					switch (num1) {
					case "0":
						stringBuffer.append("独栋");
						break;

					default:
						stringBuffer.append(num1 + "号");
						break;
					}
					switch (num2) {
					case "0":
						break;

					default:
						stringBuffer.append(num2 + "单元");
						break;
					}
				}
				if (o.getSubEstateName() == null) {
					estateName.setText(o.getEstateName() + " " + "・" + stringBuffer);
				} else {
					estateName.setText(o.getEstateName() + o.getSubEstateName() + " " + "・" + stringBuffer);
				}
			} else {

				estateName.setText(o.getEstateName());
			}

			price.setText(o.getPrice() + "万" + "  ");

			if (o.getPicNum() > 0) {
				// picNum.setText(R.string.picture_result);
				picNum.setText("");
			} else {
				picNum.setText("");
			}
			BigDecimal spaceAreaStr = o.getSpaceArea();
			double dSpaceAre = 0.00;
			if (spaceAreaStr != null) {
				dSpaceAre = Double.parseDouble(spaceAreaStr + "");
			}
			String space = StringUtil.fromatString(dSpaceAre);
			spaceArea.setText(o.getBedroomSum() + "室" + "  " + space + "平米" + "  " + o.getAreaName() + "-" + o.getTownName());
			return view;
		}
	}

	@UiThread
	public void showBottomOnHasMoreData() {
		mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多！");
		mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.pull_to_refresh_refreshing_label));
		mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载！");
	}

	@UiThread
	public void showBottomOnDataEnd() {
		mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("没有更多数据！");
		mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("没有更多数据！");
		mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("没有更多数据！");
	}

	@Override
	@UiThread
	public void upData() {
		if ((mRes.getHouseList() == null || mRes.getHouseList().size() == 0) && catchLayout.getVisibility() == View.GONE) {
			noDataTv.setVisibility(View.VISIBLE);
		} else {
			noDataTv.setVisibility(View.GONE);
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

	public void restErrorProcess(String detailMessage, int errorCode) {
		if (errorCode == RestException.INVALID_TOKEN) {
			DialogBuilder.showSimpleDialog(detailMessage, getActivity());
		} else {
			DialogBuilder.showSimpleDialog(detailMessage, getActivity());
		}
	}

	public void unexpectedErrorProcess(String detailMessage) {
		DialogBuilder.showSimpleDialog(detailMessage, getActivity());
	}

}
