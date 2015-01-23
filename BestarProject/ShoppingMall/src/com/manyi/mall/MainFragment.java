package com.manyi.mall;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.manyi.mall.common.Constants;
import com.manyi.mall.mine.MineFragment;
import com.manyi.mall.release.ReleaseFragment;
import com.manyi.mall.search.SearchHouseFragment;

@EFragment(R.layout.fragment_main)
public class MainFragment extends SuperFragment<Object>{
	
	private FragmentManager mFragmentManager;
	
	@ViewById(android.R.id.tabhost)
	public FragmentTabHost mTabHost;

	@AfterViews
	public void init() {
        mFragmentManager = getActivity().getSupportFragmentManager();
        mTabHost.setup(getActivity(), mFragmentManager, R.id.realtabcontent);

        mTabHost.addTab(createSpec(Constants.TAB_SEATCH_HOUSE, getString(R.string.main_tab_search_house)), GeneratedClassUtils.get(SearchHouseFragment.class), null);
        mTabHost.addTab(createSpec(Constants.TAB_POST, getString(R.string.main_tab_post)), GeneratedClassUtils.get(ReleaseFragment.class), null);
        mTabHost.addTab(createSpec(Constants.TAB_MINE, getString(R.string.main_tab_mine)), GeneratedClassUtils.get(MineFragment.class), null);

        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                View searchHouseView = mTabHost.getTabWidget().getChildTabViewAt(0);
                View postView = mTabHost.getTabWidget().getChildTabViewAt(1);
                View mineView = mTabHost.getTabWidget().getChildTabViewAt(2);
                TextView searchTextView = (TextView) searchHouseView.findViewById(R.id.main_tab_item);
                TextView postTextView = (TextView) postView.findViewById(R.id.main_tab_item);
                TextView mineTextView = (TextView) mineView.findViewById(R.id.main_tab_item);
                if (tabId.equals(Constants.TAB_SEATCH_HOUSE)) {
                    searchTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_search_pre, 0, 0, 0);
                    searchTextView.setTextColor(Color.parseColor(Constants.SLECTED_TEXT_COLOR));
                    postTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_post_org, 0, 0, 0);
                    postTextView.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                    mineTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_agent_org, 0, 0, 0);
                    mineTextView.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                } else if (tabId.equals(Constants.TAB_POST)) {
                    searchTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_search_org, 0, 0, 0);
                    searchTextView.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                    postTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_post_pre, 0, 0, 0);
                    postTextView.setTextColor(Color.parseColor(Constants.SLECTED_TEXT_COLOR));
                    mineTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_agent_org, 0, 0, 0);
                    mineTextView.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                } else if (tabId.equals(Constants.TAB_MINE)) {
                    searchTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_search_org, 0, 0, 0);
                    searchTextView.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                    postTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_post_org, 0, 0, 0);
                    postTextView.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                    mineTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_agent_pre, 0, 0, 0);
                    mineTextView.setTextColor(Color.parseColor(Constants.SLECTED_TEXT_COLOR));
                }
            }
        });

        mTabHost.getTabWidget().setDividerDrawable(null);

        mTabHost.setCurrentTab(1);
    }

	private TabSpec createSpec(String tag, String label) {
        View spec = View.inflate(getActivity(), R.layout.view_tab_indicator, null);
        TextView title = (TextView) spec.findViewById(R.id.main_tab_item);
        if (tag.equals(Constants.TAB_SEATCH_HOUSE)) {
			title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_search_org, 0, 0, 0);
		} else if (tag.equals(Constants.TAB_POST)) {
			title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_post_org, 0, 0, 0);
		} else if (tag.equals(Constants.TAB_MINE)) {
			title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_agent_org, 0, 0, 0);
		}
		title.setText(label);

		return mTabHost.newTabSpec(tag).setIndicator(spec);
	}
}
