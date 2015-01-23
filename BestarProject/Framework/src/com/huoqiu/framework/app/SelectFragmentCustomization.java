package com.huoqiu.framework.app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huoqiu.framework.R;
import com.huoqiu.framework.util.CheckDoubleClick;

/**
 * 该控件已经废弃不用
 * 
 * @see SelectFragment
 * 
 * @author dench
 * 
 * @param <T>
 */
@Deprecated
public abstract class SelectFragmentCustomization<T> extends SuperFragment<T> {

	private View content;
	private ViewGroup layout;
	private View trigger;
	private Context mContext;

	public void setTrigger(Context context, View trigger) {
		this.mContext = context;
		this.trigger = trigger;
		trigger.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!CheckDoubleClick.isFastDoubleClick()) {
					reverseCurrentShow();
				}
			}
		});
	}

	public void reverseCurrentShow() {
		Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_arrow_up);
		RelativeLayout view = (RelativeLayout) trigger;
		TextView mTitleBarText = (TextView) view.getChildAt(0);
		if (manager.findFragmentByTag(tag) == null) {
			show(SHOW_ADD);
			drawable = mContext.getResources().getDrawable(R.drawable.ic_arrow_up);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			mTitleBarText.setCompoundDrawables(null, null, drawable, null);
		} else {
			drawable = mContext.getResources().getDrawable(R.drawable.ic_arrow_down);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			mTitleBarText.setCompoundDrawables(null, null, drawable, null);
			if (getBackOpActivity() != null)
				remove();
		}
	}

	public abstract View addContentView(LayoutInflater inflater, ViewGroup container);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		layout = (ViewGroup) inflater.inflate(R.layout.select_fragment, container, false);

		content = addContentView(inflater, layout);
		layout.addView(content);
		content.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_arrow_up);
				RelativeLayout view = (RelativeLayout) trigger;
				TextView mTitleBarText = (TextView) view.getChildAt(0);
				drawable = mContext.getResources().getDrawable(R.drawable.ic_arrow_down);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				mTitleBarText.setCompoundDrawables(null, null, drawable, null);
				remove();
			}
		});

		int[] location = new int[2];
		trigger.getLocationOnScreen(location);

		View androidContent = getActivity().findViewById(Window.ID_ANDROID_CONTENT);
		int[] loc = new int[2];
		androidContent.getLocationOnScreen(loc);

		layout.setPadding(0, location[1] - loc[1] + trigger.getHeight() + 1, 0, 0);
		return layout;
	}
}