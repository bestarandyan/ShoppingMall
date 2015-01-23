package com.manyi.mall.user;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.backstack.AbsOp;
import com.huoqiu.framework.backstack.BackOpFragmentActivity;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.MainActivity;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.user.LoginRequest;
import com.manyi.mall.cachebean.user.LoginResponse;
import com.manyi.mall.common.CommonConfig;
import com.manyi.mall.common.Constants;
import com.manyi.mall.common.util.AESUtil;
import com.manyi.mall.service.UcService;

@SuppressLint({"WorldReadableFiles", "WorldWriteableFiles"})
@EFragment(R.layout.fragment_login)
public class LoginFragment extends SuperFragment<Integer> {
    private final String mPageName = LoginFragment.class.getSimpleName();
    private UcService mUserService;
    private boolean isfisrt;

    @ViewById(R.id.login_password)
    EditText mLoginPassword;
    @ViewById(R.id.login_username)
    EditText mLoginUsername;

    @Override
    public void onResume() {
        super.onResume();
        ManyiAnalysis.onPageStart(mPageName);
    }

    @Override
    public void onPause() {
        super.onPause();
        ManyiAnalysis.onPageEnd(mPageName);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLoginPassword != null) {
            ManyiUtils.closeKeyBoard(getActivity(), mLoginPassword);
        }
    }

    @Click(R.id.forget_tv)
    @UiThread
    void forgetv() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        ForgetPasswordFragment forgetPasswordFragment = GeneratedClassUtils.getInstance(ForgetPasswordFragment.class);
        Bundle bundle = new Bundle();
        bundle.putString("phone", mLoginUsername.getText().toString().trim());
        forgetPasswordFragment.setArguments(bundle);
        forgetPasswordFragment.tag = ForgetPasswordFragment.class.getName();

        forgetPasswordFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        forgetPasswordFragment.setManager(getFragmentManager());
        forgetPasswordFragment.show(SHOW_ADD_HIDE);
        ManyiUtils.closeKeyBoard(getActivity(), mLoginPassword);
    }

    @SuppressLint("CommitPrefEdits")
    @Click(R.id.login_loginbutton)
    void loginbutton() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        ManyiAnalysis.onEvent(getActivity(), "UserLoginClick");
        if (mLoginPassword.getText().toString().trim().length() != 0 && mLoginUsername.getText().toString().trim().length() != 0) {
            loginw();
        } else if (mLoginUsername.getText().toString().trim().length() == 0) {
            DialogBuilder.showSimpleDialog("请输入手机号", getBackOpActivity());

        } else if (mLoginPassword.getText().toString().trim().length() == 0) {
            DialogBuilder.showSimpleDialog("请输入密码", getBackOpActivity());

        }
    }

    @Background
    public void loginw() {
        String password = mLoginPassword.getText().toString().trim();
        String name = mLoginUsername.getText().toString().trim();

        LoginRequest req = new LoginRequest();
        req.setLoginName(name);
        req.setPassword(password);

        try {
            LoginResponse res = mUserService.login(req);
            if (getActivity() != null && !getActivity().isFinishing()) {
                int state = res.getState();
                String bankCode = res.getBankCode();
                SharedPreferences sharedPreferences2 = getBackOpActivity().getSharedPreferences(Constants.LOGIN_TIMES, Context.MODE_PRIVATE);
                Editor editor = sharedPreferences2.edit();
                editor.putInt("uid", res.getUid());
                editor.putInt("sumCount", res.getPublishCount());
                editor.putInt("state", res.getState());
                try {
                    editor.putString("password", AESUtil.encrypt(password, CommonConfig.AES_KEY));
                    editor.putString("name", AESUtil.encrypt(name, CommonConfig.AES_KEY));
                    editor.putString("userName", AESUtil.encrypt(res.getUserName(), CommonConfig.AES_KEY));
                    editor.putString("realName", AESUtil.encrypt(res.getRealName(), CommonConfig.AES_KEY));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                editor.putInt("PublishCount", res.getPublishCount());
                editor.putInt("surplusCount", res.getSumCount());
                editor.putBoolean("isfisrt", isfisrt);
                editor.putInt("cityId", res.getCityId());
                editor.putString("cityName", res.getCityName());
                editor.commit();
                initTab(state, bankCode);
                ManyiUtils.closeKeyBoard(getBackOpActivity(), mLoginPassword);
            }

        } catch (RestException e) {
            throw e;
        }

    }

    @UiThread
    void initTab(int state, String alipayAccount) {
        boolean isSkip = getBackOpActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getBoolean("isSkip", false);
        switch (state) {
            case Constants.ACCOUNT_REVIEW_SUCCESS:
                if (isSkip || (alipayAccount != null && !TextUtils.isEmpty(alipayAccount))) {
                    initMainActivity();
                } else {
                    BandBankCardFragment bandBankCardFragment = GeneratedClassUtils.getInstance(BandBankCardFragment.class);
                    bandBankCardFragment.tag = BandBankCardFragment.class.getName();
                    bandBankCardFragment.setManager(getFragmentManager());
                    bandBankCardFragment.setBackOp(new AbsOp(ReviewFragment.class.getName()) {

                        @Override
                        public void perform(BackOpFragmentActivity activity) {
                            initMainActivity();
                        }
                    });
                    bandBankCardFragment.show(SHOW_ADD_HIDE);
                    SharedPreferences sharedPreferences = getBackOpActivity().getSharedPreferences(Constants.LOGIN_TIMES, Context.MODE_PRIVATE);
                    Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isShowBind", true);
                    editor.commit();
                }
                break;
            case Constants.ACCOUNT_REVIEW_ING:
                ReviewFragment reviewFragment = GeneratedClassUtils.getInstance(ReviewFragment.class);
                reviewFragment.tag = ReviewFragment.class.getName();
                reviewFragment.setManager(getFragmentManager());
                reviewFragment.setBackOp(new AbsOp(ReviewFragment.class.getName()) {

                    @Override
                    public void perform(BackOpFragmentActivity activity) {
                        getActivity().finish();
                    }
                });
                reviewFragment.show(SHOW_ADD_HIDE);
                break;
            case Constants.ACCOUNT_REVIEW_FAILED:
                ReviewFailedFragment reviewFailedFragment = GeneratedClassUtils.getInstance(ReviewFailedFragment.class);
                Bundle argsBundle = new Bundle();
                argsBundle.putString("mUserName", mLoginUsername.getText().toString().trim());
                reviewFailedFragment.setArguments(argsBundle);
                reviewFailedFragment.tag = ReviewFailedFragment.class.getName();
                reviewFailedFragment.setManager(getFragmentManager());
                reviewFailedFragment.setBackOp(new AbsOp(ReviewFragment.class.getName()) {

                    @Override
                    public void perform(BackOpFragmentActivity activity) {
                        getActivity().finish();
                    }
                });
                reviewFailedFragment.show(SHOW_ADD_HIDE);
                break;
        }
    }

    private void initMainActivity() {
        Intent loginIntent = new Intent(getActivity(), GeneratedClassUtils.get(MainActivity.class));
        getActivity().overridePendingTransition(R.anim.anim_fragment_in, R.anim.anim_fragment_out);
        startActivity(loginIntent);
        getActivity().finish();
    }

    @Click(R.id.loginback)
    @UiThread
    public void loginBack() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        ManyiUtils.closeKeyBoard(getActivity(), mLoginPassword);
        remove();
    }

    @UiThread
    void showError(String e) {
        DialogBuilder.showSimpleDialog(e, getBackOpActivity());
    }
}
