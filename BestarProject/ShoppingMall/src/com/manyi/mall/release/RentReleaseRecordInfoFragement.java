package com.manyi.mall.release;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import android.os.Bundle;

import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.manyi.mall.R;
import com.manyi.mall.common.Constants;
import com.manyi.mall.mine.CheckedRecordFragment;
import com.manyi.mall.mine.MineRecordsFragment;

@EFragment(R.layout.fragment_rent_release_record)
public class RentReleaseRecordInfoFragement extends SuperFragment<Object>{

	@Click(R.id.rent_release_date_look)
	public void onReleaseDateLook(){
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		ManyiAnalysis.onEvent(getActivity(), "PublishRecordsClick");

		MineRecordsFragment mineRecordsFragment = GeneratedClassUtils.getInstance(MineRecordsFragment.class);
		mineRecordsFragment.tag = MineRecordsFragment.class.getName();
		Bundle bundle = new Bundle();
		bundle.putString("releaseRecordType", Constants.RELEASE_RECORD_RENT);
		mineRecordsFragment.setArguments(bundle);
		mineRecordsFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		mineRecordsFragment.setContainerId(R.id.main_container);
		mineRecordsFragment.setManager(getFragmentManager());
		mineRecordsFragment.show(SuperFragment.SHOW_ADD_HIDE);
	}
	
	
	@Click(R.id.rent_release_date_check)
	public void onReleaseDateCheck(){
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		CheckedRecordFragment checkedRecordFragment = GeneratedClassUtils.getInstance(CheckedRecordFragment.class);
		checkedRecordFragment.tag = CheckedRecordFragment.class.getName();
		Bundle bundle = new Bundle();
		bundle.putString("releaseRecordCheckType", Constants.CHECK_RECORD_RENT);  
		checkedRecordFragment.setArguments(bundle);
		checkedRecordFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
				R.anim.anim_fragment_close_out);
		checkedRecordFragment.setContainerId(R.id.main_container);
		checkedRecordFragment.setManager(getFragmentManager());
		checkedRecordFragment.show(SuperFragment.SHOW_ADD_HIDE);
	}
	
	
	@Click(R.id.rent_release_record_back)
	public void onBack(){
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		remove();
	}

}
