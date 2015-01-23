package com.huoqiu.framework.backstack;


public interface Op {
	public void perform(BackOpFragmentActivity activity);
	public void setTag(String tag);
	public String getTag();
}
