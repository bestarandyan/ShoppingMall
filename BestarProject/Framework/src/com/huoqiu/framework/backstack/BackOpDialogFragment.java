/**
 * 
 */
package com.huoqiu.framework.backstack;

import android.app.Activity;
import android.support.v4.app.DialogFragment;

/**
 * @author Administrator
 *
 */
public abstract class BackOpDialogFragment extends DialogFragment {
	
	public BackOpFragmentActivity getBackOpActivity() {
		Activity activity = getActivity();
		
		if(activity==null)
			throw new RuntimeException("Please call after the " + BackOpDialogFragment.class.getName() + " attached to the Activity!");
		
		if (activity instanceof BackOpFragmentActivity)
			return (BackOpFragmentActivity) activity;
		else
			throw new RuntimeException("Does not support backStack framework!");
	}

}
