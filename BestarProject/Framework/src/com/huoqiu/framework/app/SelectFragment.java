/**
 * 
 */
package com.huoqiu.framework.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;

import com.huoqiu.framework.R;
import com.huoqiu.framework.util.CheckDoubleClick;

/**
 * @author leo
 * 
 */
public abstract class SelectFragment<T> extends SuperFragment<T> {

	private View content;
	private ViewGroup layout;
	private View trigger;

	public void setTrigger(View trigger) {
		this.trigger = trigger;
		trigger.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!CheckDoubleClick.isFastDoubleClick()) {
					if (manager.findFragmentByTag(tag) == null)
						show(SHOW_ADD);
					else
						remove();
				}
			}
		});
	}

	public abstract View addContentView(LayoutInflater inflater, ViewGroup container);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		layout = (ViewGroup) inflater.inflate(R.layout.select_fragment, container, false);

		content = addContentView(inflater, layout);
		layout.addView(content);

		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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