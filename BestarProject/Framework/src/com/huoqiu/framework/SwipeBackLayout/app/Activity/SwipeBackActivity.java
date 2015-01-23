
package com.huoqiu.framework.SwipeBackLayout.app.Activity;

import android.os.Bundle;
import android.view.View;

import com.huoqiu.framework.SwipeBackLayout.TSwipeBackLayout;
import com.huoqiu.framework.rest.RestProxyActivity;

public class SwipeBackActivity extends RestProxyActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
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
    public void scrollToFinishActivity() {
        finish();
    }
}
