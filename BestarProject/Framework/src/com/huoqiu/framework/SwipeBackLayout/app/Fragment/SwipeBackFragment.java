
package com.huoqiu.framework.SwipeBackLayout.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import com.huoqiu.framework.SwipeBackLayout.TSwipeBackLayout;
import com.huoqiu.framework.app.SuperFragment;

/**
 * @author XiongWei
 */
public class SwipeBackFragment<T> extends SuperFragment<T> implements SwipeBackFragmentBase {
    private SwipeBackFragmentHelper mHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackFragmentHelper(this);
        mHelper.onActivityCreate();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHelper.onPostCreate();
    }


    @Override
    public TSwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setmEnableSwipe(enable);
    }

    @Override
    public void scrollToRemoveFragment() {
        remove();
    }
}
