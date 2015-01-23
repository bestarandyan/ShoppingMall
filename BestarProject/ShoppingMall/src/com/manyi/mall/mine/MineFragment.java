package com.manyi.mall.mine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.app.AppConfig;
import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.ClientException;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.StringUtil;
import com.huoqiu.widget.filedownloader.FileDownloadListener;
import com.manyi.mall.R;
import com.manyi.mall.StartActivity;
import com.manyi.mall.cachebean.mine.MineHomeRequest;
import com.manyi.mall.cachebean.mine.MineHomeResponse;
import com.manyi.mall.cachebean.user.AutoUpdateResponse;
import com.manyi.mall.cachebean.user.VersionRequest;
import com.manyi.mall.common.Constants;
import com.manyi.mall.service.CommonService;
import com.manyi.mall.service.UcService;
import com.manyi.mall.user.BandBankCardFragment;
import com.manyi.mall.user.UpdateLoginPwdFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.springframework.util.StringUtils;

import java.io.File;

@EFragment(R.layout.fragment_mine)
public class MineFragment extends SuperFragment<Object> implements android.content.DialogInterface.OnClickListener {

    private UcService mUserService;
    private CommonService mAppLoadService;
    private MineHomeResponse mRes;

    @ViewById(R.id.spreadId)
    TextView mSpreadId;
    @ViewById(R.id.spreanCount)
    TextView mSpreanCount;

    @ViewById(R.id.bonus)
    TextView mBonus;
    @ViewById(R.id.AliPay)
    TextView mAliPay;
    @ViewById(R.id.modification)
    TextView mModification;
    @ViewById(R.id.mine_AliPay)
    TextView mMineAliPay;
    @ViewById(R.id.alipy_all)
    RelativeLayout mAlipyAll;
    @ViewById(R.id.now_versions)
    TextView nowVersions;
    @ViewById(R.id.check_update)
    RelativeLayout mCheckUpdateLayout;
    @ViewById(R.id.mine_bank_card_all)
    RelativeLayout mineBankCardAll;
    @ViewById(R.id.bank_card)
    TextView mBankCard;
    @ViewById(R.id.modification_bank_card)
    TextView mModificationBankCard;
    @ViewById(R.id.mine_bank_card)
    TextView mMineBankCard;
    @ViewById(R.id.call_400_text)
    TextView call400Text;
    @ViewById(R.id.mine_phone_number)
    public TextView MminePhoneNumber;
    @ViewById(R.id.modification_mine_phone)
    protected TextView mModificationMinePhone;
    private AutoUpdateResponse mResponse;

    @AfterViews
    void loadDate() {

        getData();
    }

    @Override
    public void onAttach(Activity activity) {
        setBackOp(null);
        super.onAttach(activity);
    }

    @Background
    void getData() {
        try {
            MineHomeRequest req = new MineHomeRequest();
            int uid = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
            req.setUid(uid);
            mRes = mUserService.Minehome(req);

            setText();

        } catch (Exception e) {
            throw e;
        }

    }

    @SuppressLint({"ResourceAsColor", "InlinedApi"})
    @UiThread
    void setText() {
        modificationView();

        //400电话
        call400Text.setText("400" + "-" + "700" + "-" + "9922");

        //我的奖金
        if (mRes.getAwardCount() == 0) {
            mBonus.setText("");
            mBonus.setVisibility(View.GONE);

        } else {
            mBonus.setVisibility(View.VISIBLE);
            mBonus.setText(mRes.getAwardCount() + "");
        }
        mSpreadId.setText("我的邀请码：" + mRes.getSpreadId() + "");
        mSpreanCount.setText("已推广" + mRes.getSpreanCount() + "人");

        if (mRes.getPaypalAccount() != null) {
            Log.v("res.getPaypalAccount()", mRes.getPaypalAccount());
            mMineAliPay.setText("支付宝" + ":  ");
            mAliPay.setText(mRes.getPaypalAccount());
            mModification.setTextColor(Color.parseColor("#39A3E2"));
            mModification.setText("修改");
            mAlipyAll.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    ManyiAnalysis.onEvent(getActivity(), "MyAlipayClick");
                    if (CheckDoubleClick.isFastDoubleClick())
                        return;
                    ModifyAlipayFragment modificationaAlipayFragment = GeneratedClassUtils.getInstance(ModifyAlipayFragment.class);
                    modificationaAlipayFragment.tag = BindingAlipayFragment.class.getName();
                    modificationaAlipayFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
                            R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);
                    modificationaAlipayFragment.setManager(getFragmentManager());
                    modificationaAlipayFragment.setContainerId(R.id.main_container);
                    modificationaAlipayFragment.setSelectListener(new SelectListener<Object>() {

                        @Override
                        public void onSelected(Object t) {
                            getData();
                        }

                        @Override
                        public void onCanceled() {
                        }
                    });
                    modificationaAlipayFragment.show(SHOW_ADD_HIDE);

                }
            });

        } else if (mRes.getPaypalAccount() == null) {
            mMineAliPay.setText("支付宝" + " ");
            // modification.setText(" ");
            mModification.setTextColor(android.graphics.Color.RED);
            mModification.setText("未绑定");
            mAlipyAll.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    ManyiAnalysis.onEvent(getActivity(), "MyAlipayClick");
                    if (CheckDoubleClick.isFastDoubleClick())
                        return;
                    BindingAlipayFragment bindingAlipayFragment = GeneratedClassUtils.getInstance(BindingAlipayFragment.class);
                    bindingAlipayFragment.tag = BindingAlipayFragment.class.getName();
                    bindingAlipayFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
                            R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);
                    bindingAlipayFragment.setManager(getFragmentManager());
                    bindingAlipayFragment.setContainerId(R.id.main_container);
                    bindingAlipayFragment.setSelectListener(new SelectListener<Object>() {

                        @Override
                        public void onSelected(Object t) {
                            getData();
                        }

                        @Override
                        public void onCanceled() {

                        }
                    });
                    bindingAlipayFragment.show(SHOW_ADD_HIDE);
                }
            });
        }

        //修改银行卡
        if (StringUtil.isEmptyOrNull(mRes.getBankCode())) {// mRes.getBankCode().equals("")
            mMineBankCard.setText("银行卡" + " ");
            // modification.setText(" ");
            mModificationBankCard.setTextColor(android.graphics.Color.RED);
            mModificationBankCard.setText("未绑定");
            mineBankCardAll.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    BandBankCardFragment bndBankCardFragment = GeneratedClassUtils.getInstance(BandBankCardFragment.class);
                    bndBankCardFragment.tag = BandBankCardFragment.class.getName();
                    Bundle bundle = new Bundle();
                    bundle.putString("bankCode", "notBind");
                    bndBankCardFragment.setArguments(bundle);
                    bndBankCardFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
                            R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);
                    bndBankCardFragment.setManager(getFragmentManager());
                    bndBankCardFragment.setContainerId(R.id.main_container);
                    bndBankCardFragment.setSelectListener(new SelectListener<Integer>() {

                        @Override
                        public void onSelected(Integer t) {
                            // TODO Auto-generated method stub
                            getData();
                        }

                        @Override
                        public void onCanceled() {
                            // TODO Auto-generated method stub

                        }
                    });
                    bndBankCardFragment.show(SHOW_ADD_HIDE);
                }
            });

        } else if (!StringUtil.isEmptyOrNull(mRes.getBankCode())) {
            mMineBankCard.setText("我的银行卡" + "   ");

            mBankCard.setText("");
            mModificationBankCard.setTextColor(Color.parseColor("#39A3E2"));
            mModificationBankCard.setText("修改");

            mineBankCardAll.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    BandBankCardFragment bndBankCardFragment = GeneratedClassUtils.getInstance(BandBankCardFragment.class);
                    bndBankCardFragment.tag = BandBankCardFragment.class.getName();
                    Bundle bundle = new Bundle();
                    bundle.putString("bankCode", mRes.getBankCode());
                    bundle.putString("bankName", mRes.getBank());
                    bundle.putString("subBankName", mRes.getSubBank());
                    bndBankCardFragment.setArguments(bundle);
                    bndBankCardFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
                            R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);
                    bndBankCardFragment.setManager(getFragmentManager());
                    bndBankCardFragment.setContainerId(R.id.main_container);
                    bndBankCardFragment.setSelectListener(new SelectListener<Integer>() {

                        @Override
                        public void onSelected(Integer t) {
                            getData();

                        }

                        @Override
                        public void onCanceled() {
                            // TODO Auto-generated method stub

                        }
                    });
                    bndBankCardFragment.show(SHOW_ADD_HIDE);
                }
            });

        }

        try {
            PackageManager packageManager = getActivity().getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
            String version = packInfo.versionName;
            nowVersions.setText("当前版本" + " " + version);
        } catch (Exception e) {

        }

    }

    @SuppressLint("WorldReadableFiles")
    @Click(R.id.mine_exit)
    void exit() {
        ManyiAnalysis.onEvent(getActivity(), "LogoutClick");
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        DialogBuilder.showSimpleDialog("您确定退出?", "确定", "取消", getBackOpActivity(), new DialogInterface.OnClickListener() {

            @SuppressLint({"CommitPrefEdits", "WorldWriteableFiles"})
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // @SuppressWarnings({ })
                SharedPreferences sharedPreferences2 = getBackOpActivity().getSharedPreferences(Constants.LOGIN_TIMES, Context.MODE_PRIVATE);
                Editor editor = sharedPreferences2.edit();

                editor.putString("password", null);
                editor.putString("name", null);
                editor.putString("realName", null);
                editor.putBoolean("isSkip", false);
                editor.putInt("cityId", 0);
                editor.putString("cityName", null);
                editor.commit();

                Intent intent = new Intent(getBackOpActivity(), GeneratedClassUtils.get(StartActivity.class));
                Bundle bundle = new Bundle();
                bundle.putBoolean("isEnterFromLauncher", false);
                intent.putExtra("bundle", bundle);
                startActivity(intent);

                getBackOpActivity().finish();

            }
        });
    }

    @Click(R.id.feedback)
    void feedBack() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        FeedBackFragment feedBackFragment = GeneratedClassUtils.getInstance(FeedBackFragment.class);
        feedBackFragment.tag = MineAwardFragment.class.getName();
        feedBackFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        feedBackFragment.setManager(getFragmentManager());
        feedBackFragment.setContainerId(R.id.main_container);
        feedBackFragment.show(SHOW_ADD_HIDE);
    }

    @Click(R.id.call_400)
    void call400() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        DialogBuilder.showSimpleDialog("客服电话：" + "400" + "-" + "700" + "-" + "9922", "拨打", "取消", getActivity(), this);// ("客服电话："+"400"+"-"+"700"+"-"+"9922",
    }

    @Click(R.id.mine_bonus)
    void mineBonus() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        ManyiAnalysis.onEvent(getActivity(), "MyRewardsClick");

        MineAwardFragment mineAwardFragBment = GeneratedClassUtils.getInstance(MineAwardFragment.class);
        mineAwardFragBment.tag = MineAwardFragment.class.getName();
        mineAwardFragBment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        mineAwardFragBment.setManager(getFragmentManager());
        mineAwardFragBment.setContainerId(R.id.main_container);
        mineAwardFragBment.setSelectListener(new SelectListener<Object>() {

            @Override
            public void onSelected(Object t) {
                getData();
            }

            @Override
            public void onCanceled() {
            }
        });
        mineAwardFragBment.show(SHOW_ADD_HIDE);
    }

    @Click(R.id.change_password)
    void changePassword() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        ManyiAnalysis.onEvent(getActivity(), "ChangePasswordClick");

        UpdateLoginPwdFragment updateLoginPwdFragment = GeneratedClassUtils.getInstance(UpdateLoginPwdFragment.class);
        updateLoginPwdFragment.tag = UpdateLoginPwdFragment.class.getName();
        updateLoginPwdFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        updateLoginPwdFragment.setManager(getFragmentManager());
        updateLoginPwdFragment.setContainerId(R.id.main_container);
        updateLoginPwdFragment.show(SHOW_ADD_HIDE);
    }

    @Click(R.id.check_update)
    @Background
    void checkUpdate() {

        try {
            ManyiAnalysis.onEvent(getActivity(), "CheckUpdateClick");
            if (CheckDoubleClick.isFastDoubleClick())
                return;
            mResponse = new AutoUpdateResponse();
            VersionRequest versionRequest = new VersionRequest();
            versionRequest.setVersion(AppConfig.versionName);
            mResponse = mAppLoadService.getUpdateResponse2(versionRequest);
            // 服务器版本大于当前版本
            if (mResponse != null && StringUtils.hasLength(mResponse.getUrl()) && !mResponse.getVersion().equals(AppConfig.versionName)) {
                String title = /* Const.APP_UPDATE_TITLE + */"发现新版本:" + mResponse.getVersion();
                String msg = mResponse.getMessage().toString();
                // String msg1 = "2.0版本已发布,更新内容：\n\n 1. 加了好多东西\n 2.修了好多Bug";
                msg = msg.replace("\\n", "\n");
                updateSuccess(title, msg, mResponse.getUrl(), mResponse.isIfForced());
            } else {
                updateFailed(mResponse);
            }
        } catch (final RestException ex) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    restErrorProcess(ex.getMessage(), RestException.class.cast(ex).getErrorCode());
                }
            });

        } catch (final ClientException ex) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    unexpectedErrorProcess(ex.getMessage());
                    ex.printStackTrace();
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @UiThread
    public void checkEnable() {
        mCheckUpdateLayout.setEnabled(true);
    }

    @UiThread
    public void checkUnable() {
        mCheckUpdateLayout.setEnabled(false);

    }

    @UiThread
    void updateFailed(AutoUpdateResponse response) {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }

        if (response == null) {
            DialogBuilder.showSimpleDialog("服务失败了！", getBackOpActivity());
        } else {
            // 已经是最新版本了
            DialogBuilder.showSimpleDialog(response.getMessage(), getBackOpActivity());
        }
    }

    @UiThread
    void updateSuccess(String title, String message, final String url, boolean isForced) {
        doUpdate(title, message, url, isForced);
    }

    /**
     * 更新程序
     */
    private void doUpdate(String title, String message, final String url, boolean isForced) {
        if (isForced) {
            new AlertDialog.Builder(getActivity()).setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doDownload(url);
                    // getActivity().finish();
                }
            }).setMessage(message).setCancelable(false).show();
        } else {
            Dialog dialog = new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(message)
                    .setPositiveButton(("立即更新"), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doDownload(url);
                        }
                    }).setNegativeButton(("以后再说"), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        }
    }

    private ProgressDialog mProgress;

    /**
     * 显示更新界面
     */
    private void doDownload(String url) {
        mProgress = new ProgressDialog(getActivity());
        mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // mProgress.setTitle("正在下载");

        mProgress.setCancelable(false);

        mProgress.setMessage("正在下载");
        try {
            new FileDownloadListener(getActivity(), mProgress, url, callBack, R.drawable.launcher_icon, "房源宝", "FYB");
        } catch (Exception e) {
            e.printStackTrace();
            mProgress.dismiss();
            DialogBuilder.showSimpleDialog("版本更新失败，请手动检测更新。", getActivity());
        }
    }

    FileDownloadListener.DownloadCallBack callBack = new FileDownloadListener.DownloadCallBack() {

        @Override
        public void downloadSuccess(File file) {
            // 下载完成后退出进度条
            mProgress.dismiss();
            // 安装apk
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // 执行的数据类型
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void downloadFail() {
            mProgress.dismiss();
            Toast.makeText(getActivity(), "下载过程中出现错误", Toast.LENGTH_SHORT).show();
        }
    };

    private void restErrorProcess(String detailMessage, int errorCode) {
        if (errorCode == RestException.INVALID_TOKEN) {
            DialogBuilder.showSimpleDialog(detailMessage, getActivity());
        } else {
            DialogBuilder.showSimpleDialog(detailMessage, getActivity());
        }
    }

    private void unexpectedErrorProcess(String detailMessage) {
        DialogBuilder.showSimpleDialog(detailMessage, getActivity());
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "4007009922"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Click(R.id.common_problem)
    public void gotoCommonProblem() {
        CommonProblemFragment commonProblemFragment = GeneratedClassUtils.getInstance(CommonProblemFragment.class);
        commonProblemFragment.tag = CommonProblemFragment.class.getName();
        commonProblemFragment.setManager(getFragmentManager());
        commonProblemFragment.setContainerId(R.id.main_container);
        commonProblemFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);

        commonProblemFragment.show(SHOW_ADD_HIDE);
    }

    @UiThread
    public void modificationView() {

        //修改手机号码
   // mRes.getSpreadId()
        MminePhoneNumber.setText(mRes.getSpreadId());

        //是否可以修改手机号码  0：可以修改 1：不能修改
        switch (mRes.getAllowUpdateMobile()) {
            case 0:
                mModificationMinePhone.setTextColor(Color.parseColor("#39A3E2"));
                //  mModificationMinePhone.setBackgroundColor(Color.parseColor("#FF4404"));

                mModificationMinePhone.setText(getString(R.string.change_new_phone_text));

                break;
            case 1:
                //  android:background="#FF4404"
                mModificationMinePhone.setTextColor(Color.parseColor("#7f7f7f"));
                mModificationMinePhone.setText(getString(R.string.change_new_phone_text));
                break;
        }

    }

    @Click(R.id.my_phone_number)
    public void modification() {
        //是否可以修改手机号码  0：可以修改 1：不能修改
        switch (mRes.getAllowUpdateMobile()) {
            case 0:
                if (CheckDoubleClick.isFastDoubleClick()) {
                    return;
                }
                ChangePhoneNumberFragment changePhoneNumberFragment = GeneratedClassUtils.getInstance(ChangePhoneNumberFragment.class);
                changePhoneNumberFragment.tag = ChangePhoneNumberFragment.class.getName();
                changePhoneNumberFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                        R.anim.anim_fragment_close_out);
                changePhoneNumberFragment.setManager(getFragmentManager());
                changePhoneNumberFragment.setContainerId(R.id.main_container);
                changePhoneNumberFragment.setSelectListener(new SelectListener<Integer>() {
                    @Override
                    public void onSelected(Integer integer) {
                        getData();
                    }

                    @Override
                    public void onCanceled() {
                        getData();
                    }
                });
                changePhoneNumberFragment.show(SHOW_ADD_HIDE);

                break;
            case 1:
                showDialog(mRes.getMsg());
                break;
        }

    }

    private void showDialog(String msgString) {
        DialogBuilder.showSimpleDialog(msgString, getActivity());
    }
}
