
package com.huoqiu.framework.SwipeBackLayout.app.Fragment;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import com.huoqiu.framework.R;
import com.huoqiu.framework.SwipeBackLayout.TSwipeBackLayout;

/**
 * @author XiongWei
 */
public class SwipeBackFragmentHelper {
    private Fragment mFragment;

    private TSwipeBackLayout mSwipeBackLayout;

    public SwipeBackFragmentHelper(Fragment fragment) {
        mFragment = fragment;
    }

    public void onActivityCreate() {
        mSwipeBackLayout = (TSwipeBackLayout) LayoutInflater.from(mFragment.getActivity()).inflate(
                R.layout.swipeback_layout, null);
    }

    public void onPostCreate() {
        mSwipeBackLayout.attachToFragment(mFragment);
    }

    public TSwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }
}
