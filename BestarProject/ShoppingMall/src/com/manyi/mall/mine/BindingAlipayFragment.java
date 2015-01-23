package com.manyi.mall.mine;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.widget.EditText;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.mine.BindPaypalRequest;
import com.manyi.mall.common.Constants;
import com.manyi.mall.service.UcService;

@EFragment(R.layout.fragment_binding_alipay)
public class BindingAlipayFragment extends SuperFragment<Object> {
    @ViewById(R.id.bingding_email)
    EditText mBingDingEmail;

    private UcService mService;

    @Click(R.id.binding_confirm)
    void bindingConfirm() {
        if (CheckDoubleClick.isFastDoubleClick()) return;
        if (mBingDingEmail.getText().toString().length() != 0) {

            quest();
        } else if (mBingDingEmail.getText().toString().length() == 0) {
            DialogBuilder.showSimpleDialog("请输入支付宝号码", getBackOpActivity());

        }
    }

    @AfterViews
    void init() {
        mBingDingEmail.requestFocus();
        ManyiUtils.showKeyBoard(getActivity(), mBingDingEmail);
    }

    @Background
    void quest() {

        try {
            BindPaypalRequest req = new BindPaypalRequest();
            req.setPaypalAccount(mBingDingEmail.getText().toString());
            int uid = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
            req.setUid(uid);
            mService.bindPaypal(req);
            sucess();
        } catch (RestException e) {
            showToast(e.getMessage());

        }

    }

    @UiThread
    void sucess() {
        ManyiUtils.closeKeyBoard(getActivity(), mBingDingEmail);
        notifySelected(null);

    }

    @Click(R.id.binding_back)
    void bindingBack() {
        if (CheckDoubleClick.isFastDoubleClick()) return;
        remove();
        ManyiUtils.closeKeyBoard(getActivity(), mBingDingEmail);
    }

    @UiThread
    void showToast(String e) {
        DialogBuilder.showSimpleDialog(e, getBackOpActivity());
    }
}
