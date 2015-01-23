/**
 * 
 */
package com.manyi.mall.release;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.AdvertDetailRequest;
import com.manyi.mall.cachebean.search.AdvertDetailResponse;
import com.manyi.mall.service.AdvertisingService;

/**
 * @author bestar
 *
 */
@EFragment(R.layout.activity_advert_detail)
public class AdvertDetailFragment extends SuperFragment<AdvertDetailResponse>{
	@ViewById(R.id.advertTitle)
	TextView advertTitle;
	
	@ViewById(R.id.advertContent)
	TextView advertContent;
	
	@FragmentArg
	int advertId;
	
	AdvertisingService mAdvertService;
	
	@AfterViews
	void initData(){
		getAdvertDetail();
	}
	
	@Background
	void getAdvertDetail(){
		AdvertDetailRequest request = new AdvertDetailRequest();
		request.setId(advertId);
		
		AdvertDetailResponse response = mAdvertService.getAdvertDetail(request);
		if(response.getErrorCode() == 0){
			setDetailInfo(response);
		}
	}
	
	@UiThread
	void setDetailInfo(AdvertDetailResponse response){
		advertContent.setText(response.getContent());
		advertTitle.setText(response.getTitle());
	}
	
	@Click(R.id.backBtn)
	void back(){
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		remove();
	}
	
	
}
