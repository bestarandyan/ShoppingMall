package com.manyi.mall.user;

import android.os.Bundle;

import com.huoqiu.framework.app.SuperFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class ImageLoaderFragment extends SuperFragment<Object> {
	protected ImageLoader mImageLoader = ImageLoader.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (!mImageLoader.isInited()) {
			mImageLoader.init(ImageLoaderConfiguration.createDefault(getBackOpActivity()));
		}
	}
}
