package com.manyi.mall.mine;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.EditText;

import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.MainActivity;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.mine.BindPaypalRequest;
import com.manyi.mall.common.Constants;
import com.manyi.mall.service.UcService;

@EFragment(R.layout.fragment_first_binding)
public class FirstBindingFragment extends SuperFragment<Object> {
    @ViewById(R.id.first_binding)
    EditText mBingdingEmail;

    @UiThread
    @Click(R.id.jump_over)
    void jumpOver() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        ManyiAnalysis.onEvent(getActivity(), "SkipBindAlipayClick");

        SharedPreferences sharedPreferences = getBackOpActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean("isSkip", true);
        editor.commit();
        ManyiUtils.closeKeyBoard(getActivity(), mBingdingEmail);

        initMainActivity2();
    }


    private UcService mService;

    @Click(R.id.first_binding_confirm)
    void bindingConfirm() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        if (mBingdingEmail.getText().toString().length() != 0) {
            quest();
        } else if (mBingdingEmail.getText().toString().length() == 0) {
            DialogBuilder.showSimpleDialog("请输入支付宝号码", getBackOpActivity());

        }
    }

    @Override
    public void onAttach(Activity activity) {
        //setBackOp(null);
        super.onAttach(activity);
    }

    @Background
    void quest() {

        try {
            BindPaypalRequest req = new BindPaypalRequest();
            req.setPaypalAccount(mBingdingEmail.getText().toString());
            int uid = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
            req.setUid(uid);
            mService.bindPaypal(req);
            SharedPreferences sharedPreferences = getBackOpActivity().getSharedPreferences(Constants.LOGIN_TIMES,
                    Context.MODE_PRIVATE + Context.MODE_PRIVATE);
            Editor editor = sharedPreferences.edit();
            editor.putBoolean("isSkip", true);
            editor.commit();
            ManyiUtils.closeKeyBoard(getActivity(), mBingdingEmail);

            initMainActivity2();

        } catch (RestException e) {
            showtoast(e.getMessage());

        }

    }

    @UiThread
    void showtoast(String e) {
        DialogBuilder.showSimpleDialog(e, getBackOpActivity());
    }

    private void initMainActivity2() {
        Intent loginIntent = new Intent(getActivity(), GeneratedClassUtils.get(MainActivity.class));
        getActivity().overridePendingTransition(R.anim.anim_fragment_in, R.anim.anim_fragment_out);
        startActivity(loginIntent);
        getActivity().finish();
    }
}
