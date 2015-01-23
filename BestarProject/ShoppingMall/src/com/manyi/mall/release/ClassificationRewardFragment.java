package com.manyi.mall.release;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.release.MonthAwardRequest;
import com.manyi.mall.cachebean.release.MonthAwardResponse;
import com.manyi.mall.common.Constants;
import com.manyi.mall.service.MonthAwardService;
import com.manyi.mall.user.RuleContentFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_classification_reward)
public class ClassificationRewardFragment extends SuperFragment<Object> {
    MonthAwardService mMonthawardService;
    MonthAwardRequest mAwardRequest = new MonthAwardRequest();
    MonthAwardResponse mAwardResponse = new MonthAwardResponse();
    @ViewById(R.id.level_tv)
    TextView mLevelTv;
    @ViewById(R.id.added_bonus_this)
    TextView mAddedBonusThis;
    @ViewById(R.id.the_next_level)
    TextView mTheNextLevel;

    @ViewById(R.id.sucess_this)
    TextView mSucessThis;
    @ViewById(R.id.add_bonus_totals)
    TextView mAddBonusTotals;
    @ViewById(R.id.short_off_next_level)
    TextView mShortOffNextLevel;
    @ViewById(R.id.short_off_level)
    TextView mShortOffLevel;
    @ViewById(R.id.every_bunus)
    TextView mEveryBunus;

    @ViewById(R.id.last_level)
    TextView mLastLevel;
    @ViewById(R.id.last_every_bunus)
    TextView mLastEveryBunus;
    @ViewById(R.id.last_succeed_totals)
    TextView mLastSucceedTotals;
    @ViewById(R.id.add_bunus_total_last)
    TextView mAddBunusTotalLast;
    @ViewById(R.id.next_level_ui)
    LinearLayout mNextLevelUi;
    @ViewById(R.id.end_level)
    LinearLayout mEndLevel;
    @ViewById(R.id.miss_upgrade_tv)
    TextView mMissUpgradeTv;
    @ViewById(R.id.last_level_ui)
    LinearLayout mLastLevelUi;
    @ViewById(R.id.miss_upgrade)
    LinearLayout mMissUpgrade;
    @ViewById(R.id.miss_upgrade_this_month)
    LinearLayout MmissUpgradeThisMonth;
    @ViewById(R.id.miss_upgrade_this_month_tv)
    TextView mMissThisMonthTv;
    @ViewById(R.id.this_month_level_reached)
    LinearLayout mThisMonthLevelReached;
    @FragmentArg
    public String mType;
    @ViewById(R.id.classification_back)
    TextView mClassificationBack;
    @ViewById(R.id.level_reached)
    TextView mLevelReached ;

    @AfterViews
    void init() {
        if (mType.equals("rent")) {
            mClassificationBack.setText(getString(R.string.rent_classification_reward_back));
        } else if (mType.equals("sell")) {
            mClassificationBack.setText(getString(R.string.sell_classification_reward_back));
        }
        getData();
    }

    @Background
    void getData() {
        try {
            int userId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
            mAwardRequest.setUserId(userId);
            //1 出租 2 出售 （目前默认1）  type
            if (mType.equals("rent")) {
                mAwardRequest.setType(1);
            } else {
                mAwardRequest.setType(2);
            }
            mAwardResponse = mMonthawardService.getMonthAward(mAwardRequest);
            setView();
        } catch (Exception e) {
            throw e;
        }

    }

    @UiThread
    void setView() {

//this month Level Reached
        if (mAwardResponse.getCurLevelName().equals("无")&&mAwardResponse.getCurMonthConfig() != 0) {
            MmissUpgradeThisMonth.setVisibility(View.VISIBLE);
            mThisMonthLevelReached.setVisibility(View.GONE);
            //  level_tv.setText(mAwardResponse.getCurLevelName() + "");
            //level_tv.setBackgroundResource(R.drawable.indicator_copper);

        } else if (mAwardResponse.getCurLevelName().equals("土豪金")) {
            mLevelTv.setBackgroundResource(R.drawable.indicator_gold);
            mNextLevelUi.setVisibility(View.GONE);//
            mEndLevel.setVisibility(View.VISIBLE);

        } else if (mAwardResponse.getCurLevelName().equals("富贵银")) {
            mLevelTv.setBackgroundResource(R.drawable.indicator_sliver);
        } else if (mAwardResponse.getCurLevelName().equals("地主铜")) {
            mLevelTv.setBackgroundResource(R.drawable.indicator_copper);
        } else if (mAwardResponse.getCurMonthConfig() == 0) {
            MmissUpgradeThisMonth.setVisibility(View.VISIBLE);
            mThisMonthLevelReached.setVisibility(View.GONE);
            mLevelReached.setVisibility(View.GONE);
            mMissThisMonthTv.setText(getString(R.string.miss_this_no));
            mNextLevelUi.setVisibility(View.GONE);
            mTheNextLevel.setVisibility(View.GONE);
        }
        mAddedBonusThis.setText(getString(R.string.every_added_bonus, mAwardResponse.getCurAward() + "元"));
        setRed(getString(R.string.every_added_bonus, mAwardResponse.getCurAward() + "元"), mAddedBonusThis, 6, mAwardResponse.getCurAward().toString().trim().length() + 7);

        if (mType.equals("rent")) {
            mSucessThis.setText(getString(R.string.released_rent_succes, mAwardResponse.getRantHouseNum() + "套"));
            setRed(getString(R.string.released_rent_succes, mAwardResponse.getRantHouseNum() + "套"), mSucessThis, 6, mAwardResponse.getRantHouseNum().toString().trim().length() + 9);
        } else if (mType.equals("sell")) {
            mSucessThis.setText(getString(R.string.released_sell_succes, mAwardResponse.getRantHouseNum() + "套"));
            setRed(getString(R.string.released_sell_succes, mAwardResponse.getRantHouseNum() + "套"), mSucessThis, 6, mAwardResponse.getRantHouseNum().toString().trim().length() + 9);
        }

        mAddBonusTotals.setText(getString(R.string.added_bonus_total, mAwardResponse.getAwardTotal() + "元"));
        setRed(getString(R.string.added_bonus_total, mAwardResponse.getAwardTotal() + "元"), mAddBonusTotals, 6, mAwardResponse.getAwardTotal().toString().trim().length() + 9);


        //this month  short off level Reached

        mShortOffLevel.setText(getString(R.string.Short_of_level_up, mAwardResponse.getHaiChaiTaoNum() + ""));
        setRed(getString(R.string.Short_of_level_up, mAwardResponse.getHaiChaiTaoNum() + ""), mShortOffLevel, 2, mAwardResponse.getHaiChaiTaoNum().toString().trim().length() + 5);
        if (mAwardResponse.getNextLevelName().equals("无")) {
            mShortOffNextLevel.setText(mAwardResponse.getNextLevelName() + "");

            //level_tv.setBackgroundResource(R.drawable.indicator_copper);
        } else if (mAwardResponse.getNextLevelName().equals("土豪金")) {
            mShortOffNextLevel.setBackgroundResource(R.drawable.indicator_gold);

        } else if (mAwardResponse.getNextLevelName().equals("富贵银")) {
            mShortOffNextLevel.setBackgroundResource(R.drawable.indicator_sliver);
        } else if (mAwardResponse.getNextLevelName().equals("地主铜")) {
            mShortOffNextLevel.setBackgroundResource(R.drawable.indicator_copper);
        }

        //last month Level Reached

        mEveryBunus.setText(getString(R.string.added_bonus, mAwardResponse.getNextAward() + "元"));
        setRed(getString(R.string.added_bonus, mAwardResponse.getNextAward() + "元"), mEveryBunus, 5, mAwardResponse.getNextAward().toString().trim().length() + 7);
        if (mAwardResponse.getLastMonthLevelName().equals("无")&&mAwardResponse.getLastMonthConfig()!=0) {

            mLastLevelUi.setVisibility(View.GONE);
            mMissUpgrade.setVisibility(View.VISIBLE);

            // last_level.setText(mAwardResponse.getLastMonthLevelName() + "");
            mLastLevel.setBackgroundResource(R.drawable.indicator_gold);
        } else if (mAwardResponse.getLastMonthLevelName().equals("土豪金")) {
            mLastLevel.setBackgroundResource(R.drawable.indicator_gold);
        } else if (mAwardResponse.getLastMonthLevelName().equals("富贵银")) {
            mLastLevel.setBackgroundResource(R.drawable.indicator_sliver);
        } else if (mAwardResponse.getLastMonthLevelName().equals("地主铜")) {
            mLastLevel.setBackgroundResource(R.drawable.indicator_copper);
        }else if(mAwardResponse.getLastMonthConfig()==0){
            mLastLevelUi.setVisibility(View.GONE);
            mMissUpgrade.setVisibility(View.VISIBLE);
            mMissUpgradeTv.setText(getString(R.string.miss_last_no));
        }

        mLastEveryBunus.setText(getString(R.string.added_bonus, mAwardResponse.getLastMonthAward() + "元"));
        setRed(getString(R.string.added_bonus, mAwardResponse.getLastMonthAward() + "元"), mLastEveryBunus, 6, mAwardResponse.getLastMonthAward().toString().trim().length() + 7);

        if (mType.equals("rent")) {
            mLastSucceedTotals.setText(getString(R.string.released_rent_succes, mAwardResponse.getLastMonthRantHouseNum() + "套"));
            setRed(getString(R.string.released_rent_succes, mAwardResponse.getLastMonthRantHouseNum() + "套"), mLastSucceedTotals, 7, mAwardResponse.getLastMonthRantHouseNum().toString().trim().length() + 9);
        } else if (mType.equals("sell")) {
            mLastSucceedTotals.setText(getString(R.string.released_sell_succes, mAwardResponse.getLastMonthRantHouseNum() + "套"));
            setRed(getString(R.string.released_sell_succes, mAwardResponse.getLastMonthRantHouseNum() + "套"), mLastSucceedTotals, 7, mAwardResponse.getLastMonthRantHouseNum().toString().trim().length() + 9);
        }

        mAddBunusTotalLast.setText(getString(R.string.added_bonus_total, mAwardResponse.getLastMonthAwardTotal() + "元"));
        setRed(getString(R.string.added_bonus_total, mAwardResponse.getLastMonthAwardTotal() + "元"), mAddBunusTotalLast, 7, mAwardResponse.getLastMonthAwardTotal().toString().trim().length() + 9);

    }

    @UiThread
    void setRed(String String, TextView textView, int start, int end) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(String);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.raward_red)), start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_INCLUSIVE);
        textView.setText(spannableStringBuilder);

    }

    //classification_back
    @Click(R.id.classification_back)
    void back() {
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        remove();
    }

    @Click(R.id.reward_rule)
    void rewardRuleClik() {
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        RuleContentFragment ruleContentFragment = GeneratedClassUtils.getInstance(RuleContentFragment.class);
        Bundle argsBundle = new Bundle();
        if (mType.equals("rent")) {
            argsBundle.putInt("type", 5);
        } else {
            argsBundle.putInt("type", 6);
        }

        ruleContentFragment.setArguments(argsBundle);
        ruleContentFragment.tag = RuleContentFragment.class.getName();
        ruleContentFragment.setManager(getFragmentManager());
        ruleContentFragment.setContainerId(R.id.main_container);
        ruleContentFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
                R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);

        ruleContentFragment.show(SHOW_ADD_HIDE);
    }

}
