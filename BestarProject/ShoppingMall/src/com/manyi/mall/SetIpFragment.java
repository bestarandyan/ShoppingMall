package com.manyi.mall;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.widget.EditText;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.rest.Configuration;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.manyi.mall.common.util.DBUtil;

/**
 * IP Settings
 *
 * @author dench
 */
@EFragment(R.layout.fragment_set_ip)
public class SetIpFragment extends SuperFragment<Object> {
    @ViewById EditText mIpEdt; // IP Edit
    @ViewById EditText mPortEdt; // Port Edit

    @AfterViews
    void refreshView() {
        Configuration conf = Configuration.DEFAULT;
        /* Only Test Environment will show IP */
        if (conf == Configuration.TEST) {
            mIpEdt.setText(conf.hostname);
            mPortEdt.setText(conf.port + "");
        }
    }

    /**
     * save and exit
     */
    @Click(R.id.mSaveIp)
    void doSaveIp() {
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        try {
            Configuration conf = Configuration.DEFAULT;
            /* Only Test Environment can reset IP */
            if (conf == Configuration.TEST) {
                conf.hostname = mIpEdt.getText().toString().trim();
                conf.port = Integer.valueOf(mPortEdt.getText().toString().trim());

                // set ip and port
                DBUtil.getInstance().setUserSetting(getActivity(), DBUtil.ip, conf.hostname);
                DBUtil.getInstance().setUserSetting(getActivity(), DBUtil.port, mPortEdt.getText().toString().trim());

            }
        } catch (Exception e) {
            e.printStackTrace();
            // do nothing here is OK
        }
        DialogBuilder.showSimpleToast(getActivity(), getString(R.string.settings_success)); //show success notice
        remove();
    }
}
