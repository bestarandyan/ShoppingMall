package com.huoqiu.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class FangyouReleasedViewPage extends ViewPager{

	private float lastX = 0, lastY = 0;
	
	public FangyouReleasedViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initUI();
	}

	public FangyouReleasedViewPage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initUI();
	}
	
	public void initUI() {
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				float x = event.getX();
				float y = event.getY();
				switch (action & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					lastX = event.getX();
					lastY = event.getY();
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					break;
				case MotionEvent.ACTION_UP:
					lastX = 0;
					lastY = 0;
					break;
				case MotionEvent.ACTION_POINTER_UP:
					break;
				case MotionEvent.ACTION_MOVE:
					float dY = Math.abs(y - lastY);
					float dX = Math.abs(x - lastX);
					lastY = y;
					lastX = x;
					if (dX > 30 && dY > 30) {
						return true;
					}
					break;
				}
				return false;
			}
		});
	}

}
