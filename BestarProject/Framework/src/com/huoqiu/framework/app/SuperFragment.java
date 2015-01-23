/**
 * 
 */
package com.huoqiu.framework.app;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.RelativeLayout;

import com.huoqiu.framework.backstack.AbsOp;
import com.huoqiu.framework.backstack.BackOpFragmentActivity;
import com.huoqiu.framework.backstack.Op;
import com.huoqiu.framework.rest.RestProxyFragment;

/**
 * @author leo
 * 
 */
public abstract class SuperFragment<T> extends RestProxyFragment {

	public final static int SHOW_REPLACE = 0;
	public final static int SHOW_ADD = 1;
	public final static int SHOW_ATTACH = 2;
	public final static int SHOW_DETACH = -1;
	public final static int SHOW_ADD_HIDE = 3;

	protected FragmentManager manager;

	public RelativeLayout mSildingLayout;

	public String tag = "default";
	protected int containerId = Window.ID_ANDROID_CONTENT;
	private boolean selected;
	private Op backOp = new AbsOp(tag) {
		@Override
		public void perform(BackOpFragmentActivity activity) {
			if (getFragmentManager() != null && getTag() != null) {
				getFragmentManager().popBackStack(getTag(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
				if (!selected) {
					if (selectListener != null) {
						selectListener.onCanceled();
					}
				}
			}
		}
	};

	protected int enter;
	protected int exit;
	protected int popEnter;
	protected int popExit;

	private SelectListener<T> selectListener;
	private collectionListener collectionListener;

	public Op getBackOp() {
		return backOp;
	}

	public void setBackOp(Op backOp) {
		this.backOp = backOp;
	}

	public collectionListener getCollectionListener() {
		return collectionListener;
	}

	public void setCollectionListener(collectionListener collectionListener) {
		this.collectionListener = collectionListener;
	}

	public void setManager(FragmentManager manager) {
		this.manager = manager;
	}

	public void setCustomAnimations(int enter, int exit, int popEnter, int popExit) {
		this.enter = enter;
		this.exit = exit;
		this.popEnter = popEnter;
		this.popExit = popExit;
	}

	public SelectListener<T> getSelectListener() {
		return selectListener;
	}

	public void setSelectListener(SelectListener<T> selectListener) {
		this.selectListener = selectListener;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setContainerId(int containerId) {
		this.containerId = containerId;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (getBackOp() != null) {
			push(getBackOp());
		}
	}

	/**
	 * 展示加载Fragment
	 * 
	 * @param op
	 */
	public void show(int... op) {
        /**添加判空处理 */
        if(null != backOp)backOp.setTag(tag);
		FragmentTransaction ft = manager.beginTransaction();
		if (enter > 0 && exit > 0) {
			if (popEnter > 0 && popExit > 0) {
				ft.setCustomAnimations(enter, exit, popEnter, popExit);
			} else {
				ft.setCustomAnimations(enter, exit);
			}
		}
		int _op = op.length > 0 ? op[0] : SHOW_REPLACE;
		switch (_op) {
		case SHOW_REPLACE:
			ft.replace(containerId, this, tag);
			break;
		case SHOW_ADD:
			ft.add(containerId, this, tag);
			break;
		case SHOW_ADD_HIDE:
			Fragment fragment = manager.findFragmentById(containerId);
			if (fragment != null) {
				ft.hide(fragment);
			}
			ft.add(containerId, this, tag);
			break;
		case SHOW_ATTACH:
			ft.attach(this);
			break;
		case SHOW_DETACH:
			ft.detach(this);
			break;
		default:
			ft.replace(containerId, this, tag);
		}

		if (getBackOp() != null) {
			ft.addToBackStack(tag);
		}
		ft.commitAllowingStateLoss();

	}

	/**
	 * 强制移除Fragment
	 */
	public void remove() {
		Op op = pop();
		if (op != null) {
			op.perform(getBackOpActivity());
		} else {
			getActivity().onBackPressed();
		}
	}

	/**
	 * 强制移除Fragment
	 * 
	 * @param tag
	 */
	public void remove(String tag) {
		pop(tag).perform(getBackOpActivity());
	}

	/**
	 * 强制移除Fragment并回调
	 * 
	 * @param t
	 */
	public void notifySelected(T t) {
		selected = true;
		remove();
		if (selectListener != null) {
			selectListener.onSelected(t);
		}
	}

	/**
	 * 强制移除Fragment并取消回调
	 */
	public void notifyCanceled() {
		selected = false;
		remove();
	}

	/**
	 * Fragment回调监听接口
	 * 
	 * @author dench
	 * 
	 * @param <T>
	 */
	public static interface SelectListener<T> {
		public void onSelected(T t);

		public void onCanceled();
	}

	public static interface collectionListener {
		public void onCollectionSelected(long id, boolean collect);

	}

}