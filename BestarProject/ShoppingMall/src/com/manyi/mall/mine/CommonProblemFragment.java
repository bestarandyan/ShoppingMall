package com.manyi.mall.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.manyi.mall.R;
import com.manyi.mall.common.Constants;
import com.manyi.mall.user.RuleContentFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_common_problem)
public class CommonProblemFragment extends SuperFragment<Integer> {
	
	@ViewById(R.id.common_problem_back)
	public TextView mTitle;
	
	@ViewById(R.id.award_rule)
	public TextView awardRule;
	
	@FragmentArg
	public boolean isFromReviewRule;

	@Click(R.id.common_problem_back)
	void back() {
		if (CheckDoubleClick.isFastDoubleClick())
		return;
		remove();
	}
	
	@AfterViews
	public void initViews(){
		if (isFromReviewRule) {
			mTitle.setText(getString(R.string.common_problem_review_rule));
			awardRule.setVisibility(View.GONE);
			//int cityId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, Context.MODE_PRIVATE).getInt("cityId", 0);
			//if (cityId == Constants.ID_CITY_GUANGZHOU) {

			//}
		} 
	}
	
	@Click(R.id.rent_rule)
	void rentRule() {
		if (CheckDoubleClick.isFastDoubleClick())
		return;
		gotoRuleFragment(Constants.GOTO_RENT_CONTENT);
	}
	
	@Click(R.id.sell_rule)
	void sellRule() {
		if (CheckDoubleClick.isFastDoubleClick())
		return;
		gotoRuleFragment(Constants.GOTO_SELL_CONTENT);
	}
	
	@Click(R.id.award_rule)
	void awardRule() {
		if (CheckDoubleClick.isFastDoubleClick())
		return;
		gotoRuleFragment(Constants.GOTO_AWARD_CONTENT);
	}
	
	@Click(R.id.review_rule)
	void reviewRule() {
		if (CheckDoubleClick.isFastDoubleClick())
		return;
		gotoRuleFragment(Constants.GOTO_REVIEW_CONTENT);
	}
	

	
	
	@UiThread
	public void gotoRuleFragment(int type){
		RuleContentFragment ruleContentFragment = GeneratedClassUtils.getInstance(RuleContentFragment.class);
		Bundle argsBundle = new Bundle();
		argsBundle.putInt("type", type);
		ruleContentFragment.setArguments(argsBundle);
		ruleContentFragment.tag = RuleContentFragment.class.getName();
		ruleContentFragment.setManager(getFragmentManager());
		ruleContentFragment.setContainerId(R.id.main_container);
		ruleContentFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
				R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);

		ruleContentFragment.show(SHOW_ADD_HIDE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
