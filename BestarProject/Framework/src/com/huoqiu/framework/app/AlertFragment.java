/**
 * 
 */
package com.huoqiu.framework.app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.huoqiu.framework.R;
import com.huoqiu.framework.backstack.BackOpDialogFragment;

/**
 * @author Administrator
 * 
 */
@SuppressLint("NewApi")
public class AlertFragment extends BackOpDialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setStyle(STYLE_NO_TITLE, 0);
		return super.onCreateDialog(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.alert_fragment, container);
	}

//	@Override
//	public void show(FragmentManager manager, String tag) {
//		super.show(manager, tag);
//	}
//
//	@Override
//	public int show(FragmentTransaction transaction, String tag) {
//		int mBackStackId = super.show(transaction, tag);
//		return mBackStackId;
//	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	public void dismissAllowingStateLoss() {
		super.dismissAllowingStateLoss();
	}

}
