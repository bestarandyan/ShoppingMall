package com.manyi.mall.cachebean.mine;

import org.androidannotations.annotations.EBean;

/**
 * Created by jason on 2014/12/12.
 */
@EBean
public class UpdateMobileRequest {
    private int uid;//用户id
    private String mobile;//新手机号码
    private String verifyCode;//验证码

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
