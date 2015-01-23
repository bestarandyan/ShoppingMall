package com.huoqiu.framework.SwipeBackLayout.app.Fragment;

import com.huoqiu.framework.SwipeBackLayout.TSwipeBackLayout;

/**
 * @author XiongWei
 */
public interface SwipeBackFragmentBase {

    public abstract TSwipeBackLayout getSwipeBackLayout();

    public abstract void setSwipeBackEnable(boolean enable);

    /**
     * Scroll out contentView and remove the fragment
     */
    public abstract void scrollToRemoveFragment();

}
