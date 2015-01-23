package com.manyi.mall.user;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.backstack.BackOpFragmentActivity;
import com.huoqiu.framework.backstack.Op;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.manyi.mall.R;
import com.manyi.mall.StartFragment;
import com.manyi.mall.common.Constants;

@EFragment(R.layout.fragment_reviewing)
public class ReviewFragment extends SuperFragment<Object> {

	@Override
	public void onAttach(Activity activity) {
		// setBackOp(null);
		super.onAttach(activity);
	}

	@Click({ R.id.btn_exit_login })
	public void registerExit() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		ManyiAnalysis.onEvent(getBackOpActivity(), "LogoutClick");
		DialogBuilder.showSimpleDialog("您确定退出?", "确定", "取消", getBackOpActivity(), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (CheckDoubleClick.isFastDoubleClick())
					return;

				SharedPreferences sharedPreferences2 = getBackOpActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0);
				Editor editor = sharedPreferences2.edit();

				editor.putString("password", null);
				editor.putString("name", null);
				editor.putString("realName", null);
				editor.putBoolean("isSkip", false);
				editor.putInt("cityId", 0);
				editor.putString("cityName", null);
				editor.commit();

				StartFragment startFragment = GeneratedClassUtils.getInstance(StartFragment.class);
				startFragment.tag = StartFragment.class.getName();
				Bundle bundle = new Bundle();
				bundle.putBoolean("NotCheckNewVersion", true);
				startFragment.setArguments(bundle);
				startFragment.setManager(getFragmentManager());
				startFragment.setBackOp(new Op() {

					@Override
					public void setTag(String tag) {
					}

					@Override
					public void perform(BackOpFragmentActivity activity) {
						getActivity().finish();
					}

					@Override
					public String getTag() {
						return StartFragment.class.getName();
					}
				});
				startFragment.show();
			}
		});
	}

}
