package com.manyi.mall.mine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.mine.ChangePhoneNumberNextRequest;
import com.manyi.mall.cachebean.user.CaptchaCodeRequest;
import com.manyi.mall.common.CommonConfig;
import com.manyi.mall.common.Constants;
import com.manyi.mall.common.util.AESUtil;
import com.manyi.mall.service.UcService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_change_phone_number)
public class ChangePhoneNumberFragment extends SuperFragment<Integer> {
    @ViewById(R.id.current_phone_number)
    TextView mCurrentPhoneNumber;
    @ViewById(R.id.verify_code)
    EditText mVerifyCode;
    @ViewById(R.id.code_number)
    EditText mCodeNumber;
    @ViewById(R.id.get_verify_code)
    Button mGetVerifyCode;
    private String phone;
    private UcService mUserService;
    private boolean isFirstEnter = true;

    /**
     * Initialize fragment, show keyboard after animation.
     */
    @AfterViews
    void init() {
        try {
            String phoneString = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, Context.MODE_PRIVATE).getString("userName", null);
            if (phoneString != null) {
                phone = AESUtil.decrypt(phoneString, CommonConfig.AES_KEY);
            }
            if (phone != null) {
                mCurrentPhoneNumber.setText(getString(R.string.current_phone_number, phone));
            }
            addAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (isFirstEnter) {
                        ManyiUtils.showKeyBoard(getActivity(), mVerifyCode);
                        isFirstEnter = false;
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ManyiUtils.closeKeyBoard(getActivity(), mVerifyCode);
    }

    /**
     * Get verify code of the user phone number.
     */
    @Background
    public void getVerifyCode() {
        try {
            CaptchaCodeRequest req = new CaptchaCodeRequest();
            req.setMobile(phone);
            mUserService.getVerifyCode(req);
            onSendSMSSuccess();
        } catch (RestException e) {
            logincodeError(e.getMessage());
        }
    }

    @Background
    void next(String code) {
        try {
            ChangePhoneNumberNextRequest req = new ChangePhoneNumberNextRequest();
            req.verifyCode = code;
            req.uid = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, Context.MODE_PRIVATE).getInt("uid", 0);
            req.idCard = mCodeNumber.getText().toString().trim();
            mUserService.changePhoneNumberNext(req);
            intentRest();
        } catch (RestException e) {
            nextError(e.getMessage());
        }
    }

    @UiThread
    public void onSendSMSSuccess() {
        mGetVerifyCode.setEnabled(false);
        DialogBuilder.showSimpleDialog("验证码已发送!", getBackOpActivity(), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timer.start();
                int left = mGetVerifyCode.getPaddingLeft();
                int right = mGetVerifyCode.getPaddingRight();
                mGetVerifyCode.setBackgroundResource(R.drawable.btn_orange_dis);
                mGetVerifyCode.setPadding(left, 0, right, 0);
            }
        });
    }

    @UiThread
    public void logincodeError(String e) {
        DialogBuilder.showSimpleDialog(e, getBackOpActivity());
    }

    @UiThread
    public void showerror() {
        DialogBuilder.showSimpleDialog("验证码不能为空", getBackOpActivity());
    }

    @UiThread
    void intentRest() {
        ChangeNewPhoneNumberFragment changeNewPhoneNumber = GeneratedClassUtils.getInstance(ChangeNewPhoneNumberFragment.class);
        changeNewPhoneNumber.setSelectListener(new SelectListener<Object>() {
            @Override
            public void onSelected(Object o) {
                notifySelected(null);
            }

            @Override
            public void onCanceled() {
                remove();
            }
        });
        changeNewPhoneNumber.tag = ChangeNewPhoneNumberFragment.class.getName();
        changeNewPhoneNumber.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        changeNewPhoneNumber.setManager(getFragmentManager());
        changeNewPhoneNumber.setSelectListener(new SelectListener<Object>() {
            @Override
            public void onSelected(Object o) {
                notifySelected(null);
            }

            @Override
            public void onCanceled() {
                notifySelected(null);
            }
        });
        changeNewPhoneNumber.show(SHOW_ADD_HIDE);
        timer.cancel();
        ManyiUtils.closeKeyBoard(getActivity(), mVerifyCode);

    }

    @UiThread
    @Click(R.id.change_phone_number_next)
    public void nextStep() {
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        if (TextUtils.isEmpty(mVerifyCode.getText().toString()) || TextUtils.isEmpty(mVerifyCode.getText().toString().trim())) {
            DialogBuilder.showSimpleDialog("请输入验证码", getBackOpActivity());
            return;
        }
        if (TextUtils.isEmpty(mCodeNumber.getText().toString()) || TextUtils.isEmpty(mCodeNumber.getText().toString().trim())) {
            DialogBuilder.showSimpleDialog("请输入身份证号", getBackOpActivity());
            return;
        }
        next(mVerifyCode.getText().toString().trim());
    }

    @UiThread
    public void nextError(String e) {
        DialogBuilder.showSimpleDialog(e, getBackOpActivity());
    }

    @Click(R.id.cancel_change_phone_number)
    void back() {
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        ManyiUtils.closeKeyBoard(getActivity(), mVerifyCode);
        remove();
    }

    @Click(R.id.get_verify_code)
    void sendVerifyCodeService() {
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        getVerifyCode();
    }

    CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long arg0) {
            mGetVerifyCode.setText("获取短信(" + arg0 / 1000 + ")...");
        }

        @Override
        public void onFinish() {
            mGetVerifyCode.setText(R.string.register_get_captcha);
            mGetVerifyCode.setEnabled(true);
            int left = mGetVerifyCode.getPaddingLeft();
            int right = mGetVerifyCode.getPaddingRight();
            mGetVerifyCode.setBackgroundResource(R.drawable.start_selector);
            mGetVerifyCode.setPadding(left, 0, right, 0);
        }
    };
}
