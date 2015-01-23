package com.manyi.mall.search;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.release.IntentDetailRequest;
import com.manyi.mall.cachebean.search.AreasResponse.AreaResponse;
import com.manyi.mall.cachebean.search.SearchRespose.Estate;
import com.manyi.mall.cachebean.user.HouseRequest;
import com.manyi.mall.cachebean.user.HouseResponse;
import com.manyi.mall.common.Constants;
import com.manyi.mall.release.ReleasedRentFragment;
import com.manyi.mall.release.ReleasedSellFragment;
import com.manyi.mall.release.ShortcutsSelectFragment;
import com.manyi.mall.release.ShortcutsSelectFragment.ReleaseShortcuts;
import com.manyi.mall.service.CommonService;

@EFragment
public abstract class SearchResultFragment extends SuperFragment<Object> {
	@FragmentArg
	public Estate estate;

	@FragmentArg
	public AreaResponse area;
	@ViewById(R.id.shortcuts_im)
	protected TextView mShortcutsIm;
	@ViewById(R.id.textsearch)
	protected TextView mTextSearch;
	@ViewById(R.id.housebutton)
	protected RadioButton mHouseButton;
	@ViewById(R.id.areabutton)
	protected RadioButton mAreaButton;
	@ViewById(R.id.pricebutton)
	protected RadioButton mPriceButton;
	@ViewById
	protected LinearLayout catchLayout;
	@ViewById
	protected TextView checkInternetTv;
	@ViewById(R.id.againBtn)
	protected Button mAgainBtn;
	@ViewById
	protected TextView noDataTv;

	public CommonService commonService;

	protected HouseRequest mReq = new HouseRequest();
	protected HouseResponse mRes = new HouseResponse();


	public abstract void requestList();

	public abstract void showState(int state);

	public abstract void loadMore();

	public abstract void getData(boolean b);

	public abstract void loadByConditions();

	public abstract void upData();

	public static final int FLAG_GETDATA = 1;// 标记当前动作为getData函数
	public static final int FLAG_CONDITION = 2;// 标记当前动作为loadByConditions函数
	public static final int FLAG_LOADMORE = 3;// 标记当前动作为loadMore函数
	public int mCurrentFlag = 0;// 标记当前请求服务器的动作

	public abstract void intentToDetailing(int position, FragmentManager manager);

	@Background
	public void intentToDetail(int position) {
		try {
			int uid = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
			IntentDetailRequest req = new IntentDetailRequest();
			req.setUid(uid);
			commonService.houseCount(req);

			intentToDetailing(position, getFragmentManager());

		} catch (Exception e) {
			throw e;
		}

	}

	@AfterViews
	public void addview() {
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int width = display.getWidth() / 3;
		mPriceButton.setWidth(width);
		mHouseButton.setWidth(width);
		mAreaButton.setWidth(width);
		final ShortcutsSelectFragment shortcutsSelectFragment = GeneratedClassUtils.getInstance(ShortcutsSelectFragment.class);

		shortcutsSelectFragment.setManager(getFragmentManager());
		shortcutsSelectFragment.setTag(ShortcutsSelectFragment.class.getName());
		shortcutsSelectFragment.setSelectListener(new SelectListener<ReleaseShortcuts>() {
			@Override
			public void onSelected(ReleaseShortcuts source) {

				// mShortcutsIm.setText(source.getShortcutstitle());
				if (source.getShortcutstitle().equals("发布出租")) {
					ManyiAnalysis.onEvent(getActivity(), "PublishRentClick");
					ReleasedRentFragment releasedRentFragment = GeneratedClassUtils.getInstance(ReleasedRentFragment.class);
					releasedRentFragment.tag = ReleasedRentFragment.class.getName();
					releasedRentFragment.setManager(getFragmentManager());
					releasedRentFragment.setContainerId(R.id.main_container);
					releasedRentFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
							R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);

					releasedRentFragment.show(SHOW_ADD_HIDE);
				} else if (source.getShortcutstitle().equals("发布出售")) {

					ManyiAnalysis.onEvent(getActivity(), "PublishRentClick");
					ReleasedSellFragment releasedSellFragment = GeneratedClassUtils.getInstance(ReleasedSellFragment.class);
					releasedSellFragment.tag = ReleasedSellFragment.class.getName();
					releasedSellFragment.setManager(getFragmentManager());
					releasedSellFragment.setContainerId(R.id.main_container);
					releasedSellFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
							R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);

					releasedSellFragment.show(SHOW_ADD_HIDE);
				} else if (source.getShortcutstitle().equals("租售改盘")) {
//					ChangeHouseFragment changeHouseFragment = GeneratedClassUtils.getInstance(ChangeHouseFragment.class);
//					changeHouseFragment.tag = ChangeHouseFragment.class.getName();
//					changeHouseFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
//							R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);
//					changeHouseFragment.setContainerId(R.id.main_container);
//					changeHouseFragment.setManager(getFragmentManager());
//					
//					changeHouseFragment.show(SHOW_ADD_HIDE);
				}

			}

			@Override
			public void onCanceled() {

			}
		});

		shortcutsSelectFragment.setTrigger(mShortcutsIm);
	}

}