package com.manyi.mall.user;

import android.content.Context;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.RuleContentRequest;
import com.manyi.mall.cachebean.search.RuleContentResponse;
import com.manyi.mall.common.Constants;
import com.manyi.mall.service.RulePromptService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_rule_content)
public class RuleContentFragment extends SuperFragment<Object> {

    @ViewById(R.id.rule_content_content)
    TextView ruleContent;

    @FragmentArg
    int type;

    @ViewById(R.id.rule_content_back)
    TextView mTiltle;

    private RulePromptService userService;
    private RuleContentResponse ruleResponse;

    @AfterViews
    public void init() {
        updateTitle();
        getContent();
    }

    private void updateTitle() {
        switch (type) {
            case Constants.GOTO_RENT_CONTENT:
                mTiltle.setText(getString(R.string.common_problem_rent_rule));
                break;
            case Constants.GOTO_SELL_CONTENT:
                mTiltle.setText(getString(R.string.common_problem_sell_rule));
                break;
            case Constants.GOTO_AWARD_CONTENT:
                mTiltle.setText(getString(R.string.common_problem_award_rule));
                break;
            case Constants.GOTO_REVIEW_CONTENT:
                mTiltle.setText(getString(R.string.common_problem_review_rule));
                break;
            case Constants.GOTO_MONTH_ARARD_CONTENT_RENT:
                mTiltle.setText(getString(R.string.common_problem_month_award_rule_rent));
                break;
            case Constants.GOTO_MONTH_ARARD_CONTENT_SELL:
                mTiltle.setText(getString(R.string.common_problem_month_award_rule_sell));
                break;
        }
    }

    @Background
    public void getContent() {
        RuleContentRequest request = new RuleContentRequest();
        request.setCityId(getActivity().getSharedPreferences(Constants.LOGIN_TIMES, Context.MODE_PRIVATE).getInt("cityId", 0));
        request.setType(type);
        ruleResponse = userService.getRuleContent(request);
        updateContent();
    }

    @UiThread
    public void updateContent() {
        if (ruleContent != null) {
            ruleContent.setText(ruleResponse.getContent());
        }
    }

    @Click(R.id.rule_content_back)
    void ruleBck() {
        remove();
    }
}
