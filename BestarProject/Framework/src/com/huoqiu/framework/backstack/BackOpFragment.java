package com.huoqiu.framework.backstack;

import android.app.Activity;
import android.support.v4.app.Fragment;

public class BackOpFragment extends Fragment implements BackOpController {

	public BackOpFragmentActivity getBackOpActivity() {
		Activity activity = getActivity();
		if (activity == null) {
			throw new RuntimeException("Please call after the " + getClass().getName() + " attached to the Activity!");
		}
		if (activity instanceof BackOpFragmentActivity) {
			return (BackOpFragmentActivity) activity;
		} else {
			throw new RuntimeException("Does not support backStack framework!");
		}

	}

	@Override
	public void push(Op op) {
		getBackOpActivity().push(op);
	}

	@Override
	public Op pop() {
		return getBackOpActivity().pop();
	}

	@Override
	public Op peek() {
		return getBackOpActivity().peek();
	}

	@Override
	public Op pop(String tag) {
		return getBackOpActivity().pop(tag);
	}

	/***
	 * 在Activity接收到返回事件时，想要移除顶部的fragment之前会首先调用此接口，检查一下是否可以返回
	 * 
	 * 如果子类希望在返回键被调用时，处理相关事情，必须重写本方法
	 * 
	 * @param from
	 *            返回事件的来源 0表示系统的返回按钮 1代表应用内顶部的返回上一页按钮
	 * @return true代表当前页面可以被销毁 false代表当前页面存在未完成的事情，不能返回
	 * 
	 */
	public boolean canFragmentGoback(int from) {
		return true;
	}

}
