package com.huoqiu.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huoqiu.framework.util.StringUtil;

public class FangYouInfoBar extends RelativeLayout {
	// <!-- Icon 文字 -->
	private int iconRes;
	// <!-- 标题文字 -->
	private String titleText;
	// <!-- 标题文字颜色 -->
	private int titleColor;
	// <!-- 标题文字大小 -->
	private float titleSize;
	// <!-- 描述文字 -->
	private String description;
	// <!-- 描述文字颜色 -->
	private int descriptionColor;
	// <!-- 描述文字大小 -->
	private float descriptionSize;

	protected TextView mIcon; // Icon
	protected TextView mTitle;// 标题
	protected TextView mDescription; // 描述

	public FangYouInfoBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initFromAttributes(context, attrs);
		setUpView(context);
	}

	public FangYouInfoBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initFromAttributes(context, attrs);
		setUpView(context);
	}

	private void initFromAttributes(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FangYouInfoBar);
		if (a != null) {
			float defaultTitleSize = getTypedValue(context, TypedValue.COMPLEX_UNIT_DIP, 18);
			float defaultDescriptionSize = getTypedValue(context, TypedValue.COMPLEX_UNIT_DIP, 14);

			iconRes = a.getResourceId(R.styleable.FangYouInfoBar_iconRes, R.drawable.ic_launcher);
			titleText = a.getString(R.styleable.FangYouInfoBar_titleText);
			titleColor = a.getColor(R.styleable.FangYouInfoBar_titleColor, R.color.color_d0d0d0);
			titleSize = a.getDimension(R.styleable.FangYouInfoBar_titleSize, defaultTitleSize);
			description = a.getString(R.styleable.FangYouInfoBar_description);
			descriptionColor = a.getColor(R.styleable.FangYouInfoBar_descriptionColor, R.color.color_f5f5f5);
			descriptionSize = a.getDimension(R.styleable.FangYouInfoBar_descriptionSize, defaultDescriptionSize);
			a.recycle();
		}
	}

	/**
	 * 初始化界面
	 * 
	 * @param context
	 */
	private void setUpView(Context context) {
		setGravity(Gravity.CENTER_VERTICAL);
		int margins = getTypedValue(context, TypedValue.COMPLEX_UNIT_DIP, 12);
		mIcon = new TextView(context);
		mIcon.setId(0xff12);
		mIcon.setGravity(Gravity.CENTER);
		mIcon.setBackgroundResource(iconRes);
		RelativeLayout.LayoutParams rlpm = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlpm.setMargins(margins, 0, 0, 0);
		addView(mIcon, rlpm);

		mTitle = new TextView(context);
		mTitle.setId(0xff17);
		mTitle.setText(titleText);
		mTitle.setTextColor(titleColor);
		mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
		rlpm = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlpm.addRule(RelativeLayout.RIGHT_OF, mIcon.getId());
		rlpm.setMargins(margins, 0, 0, 0);
		addView(mTitle, rlpm);

		mDescription = new TextView(context);
		mDescription.setText(description);
		mDescription.setTextColor(descriptionColor);
		mDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, descriptionSize);
		rlpm = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlpm.addRule(RelativeLayout.RIGHT_OF, mIcon.getId());
		rlpm.addRule(RelativeLayout.BELOW, mTitle.getId());
		rlpm.setMargins(margins, 4, 0, 0);
		addView(mDescription, rlpm);
		if (StringUtil.isEmptyOrNull(description)) {
			mDescription.setVisibility(View.GONE);
		}
	}

	private int getTypedValue(Context c, int unit, float size) {
		Resources r;
		if (c == null)
			r = Resources.getSystem();
		else
			r = c.getResources();
		return (int) TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setTitleText(String title) {
		if (mTitle != null) {
			this.titleText = title;
			mTitle.setText(title);
		}
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setDescriptionText(String description) {
		if (mDescription != null) {
			this.description = description;
			mDescription.setText(description);
			if (mDescription.getVisibility() != View.VISIBLE) {
				mDescription.setVisibility(View.VISIBLE);
			}
		}
	}
}
