package com.manyi.mall;

import java.io.File;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.app.AppConfig;
import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.backstack.AbsOp;
import com.huoqiu.framework.backstack.BackOpFragmentActivity;
import com.huoqiu.framework.exception.ClientException;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.rest.Configuration;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.FileUtils;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.StringUtil;
import com.huoqiu.framework.util.TimeUtil;
import com.huoqiu.widget.filedownloader.FileDownloadListener;
import com.manyi.mall.cachebean.search.ServiceTimeResponse;
import com.manyi.mall.cachebean.user.AutoUpdateResponse;
import com.manyi.mall.cachebean.user.LoginRequest;
import com.manyi.mall.cachebean.user.LoginResponse;
import com.manyi.mall.cachebean.user.VersionRequest;
import com.manyi.mall.common.Constants;
import com.manyi.mall.common.util.AESUtil;
import com.manyi.mall.service.CommonService;
import com.manyi.mall.service.UcService;
import com.manyi.mall.user.BandBankCardFragment;
import com.manyi.mall.user.LoginFragment;
import com.manyi.mall.user.RegisterFragment;
import com.manyi.mall.user.ReviewFailedFragment;
import com.manyi.mall.user.ReviewFragment;

@EFragment(R.layout.fragment_start)
public class StartFragment extends SuperFragment<Object> {
    private CommonService mAppLoadService;
    private UcService mService;
    private CommonService mCommonService;
    private String getuserName, getpassword;
    private boolean isDimss = true;
    private AutoUpdateResponse mResponse;
    @ViewById(R.id.start_register)
    Button mStartRegister;
    @ViewById(R.id.start_login)
    Button mStartLogin;
    @ViewById(R.id.mSetting)
    Button mSetting;

    @ViewById(R.id.app_version)
    TextView mAppVersion;

    @FragmentArg
    boolean NotCheckNewVersion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            // 检查更新服务
            if (!NotCheckNewVersion) {
                checkVersoins();
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (NotCheckNewVersion) {
            enableButtons();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.GUIDE_SHAREDPREFERENCES_FLAG,
                Context.MODE_PRIVATE);
        boolean isGuided = sharedPreferences.getBoolean(Constants.GUIDE_IS_GUIDE, false);
        if (!isGuided) {
            try {
                File fybFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fyb");
                if (fybFile.exists()) {
                    File renameFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                            + System.currentTimeMillis());
                    fybFile.renameTo(renameFile);
                    FileUtils.deleteFile(renameFile);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.GUIDE_IS_GUIDE, true);
            editor.commit();
        }
    }

    @AfterViews
    void refreshView() {
        try {
            PackageManager packageManager = getActivity().getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
            String version = packInfo.versionName;
            mAppVersion.setText("V" + version);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (Configuration.DEFAULT == Configuration.TEST) {
            // 测试环境，打开设置IP开关
            mSetting.setVisibility(View.VISIBLE);
            mSetting.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (CheckDoubleClick.isFastDoubleClick())
                        return;
                    settingsNext();
                }
            });
        }
    }

    /**
     * 注册
     */
    @Click({R.id.start_register})
    public void registerNext() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        ManyiAnalysis.onEvent(getBackOpActivity(), "regist");
        RegisterFragment registerFragment = GeneratedClassUtils.getInstance(RegisterFragment.class);
        registerFragment.tag = RegisterFragment.class.getName();
        registerFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        registerFragment.setManager(getFragmentManager());
        registerFragment.show(SHOW_ADD_HIDE);
    }

    /**
     * 登录
     */
    @Click(R.id.start_login)
    public void loginNext() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        ManyiAnalysis.onEvent(getBackOpActivity(), "UserLoginClick");
        LoginFragment loginFragment = GeneratedClassUtils.getInstance(LoginFragment.class);
        loginFragment.tag = LoginFragment.class.getName();
        loginFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        loginFragment.setManager(getFragmentManager());
        loginFragment.show(SHOW_ADD_HIDE);
    }

    /**
     * 设置IP页
     */
    private void settingsNext() {
        SetIpFragment setIpFragment = GeneratedClassUtils.getInstance(SetIpFragment.class);
        setIpFragment.tag = SetIpFragment.class.getName();
        setIpFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        setIpFragment.setManager(getFragmentManager());
        setIpFragment.show(SHOW_ADD_HIDE);
    }

    @Background
    public void checkVersoins() {
        try {
            // 同步时间服务
            ServiceTimeResponse timeResponse = mCommonService.getServerTime();
            TimeUtil.calTime(timeResponse.getSysDate());
            // 检查更新服务
            VersionRequest versionRequest = new VersionRequest();
            versionRequest.setVersion(AppConfig.versionName);
            mResponse = mAppLoadService.getUpdateResponse(versionRequest);
            if (mResponse != null && !StringUtil.isEmptyOrNull(mResponse.getUrl()) && !mResponse.getVersion().equals(AppConfig.versionName)) {
                // 是否强制更新
                String title = "发现新版本:" + mResponse.getVersion();
                String msg = mResponse.getMessage().toString();
                msg = msg.replace("\\n", "\n");
                // 更新程序
                doUpdate(title, msg, mResponse.getUrl(), mResponse.isIfForced());
            } else if (mResponse != null && !StringUtil.isEmptyOrNull(mResponse.getUrl())
                    && mResponse.getVersion().equals(AppConfig.versionName)) {
                goNext();
            }

        } catch (Exception ex) {
            // 不需要展示错误信息
            ex.printStackTrace();
        } finally {
            enableButtons();
        }

    }

    /**
     * 设置按钮可点击
     */
    @UiThread
    void enableButtons() {
        mStartRegister.setEnabled(true);
        mStartLogin.setEnabled(true);
    }

    /**
     * 更新程序
     */
    @UiThread
    public void doUpdate(String title, String message, final String url, boolean isForced) {
        if (isForced) {
            new AlertDialog.Builder(getActivity()).setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doDownload(url);
                }
            }).setMessage(message).setCancelable(false).show();
        } else {
            Dialog dialog = new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(message)
                    .setPositiveButton(("立即更新"), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doDownload(url);
                            isDimss = false;
                            mProgress.setOnDismissListener(new OnDismissListener() {

                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    goNext();
                                }
                            });
                        }
                    }).setNegativeButton(("以后再说"), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {

                    if (isDimss) {
                        goNext();
                    }

                }
            });

            dialog.show();
        }
    }

    /**
     * 更新APP的进度条
     */
    private ProgressDialog mProgress = null;

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
                cancelNotice();
                if (mResponse.isIfForced()) {
                    getActivity().finish();
                }

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

    /**
     * 显示更新界面
     */
    private void doDownload(String url) {
        mProgress = new ProgressDialog(getActivity());
        // mProgress.setTitle("正在下载");
        mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgress.setCancelable(false);
        mProgress.setMessage("正在下载");
        try {
            new FileDownloadListener(getActivity(), mProgress, url, callBack, R.drawable.launcher_icon, "房源宝", "FYB");
        } catch (Exception e) {
            mProgress.dismiss();
            DialogBuilder.showSimpleDialog("版本更新失败，请手动检测更新。", getActivity());
        }
    }

    @UiThread
    protected void goNext() {
        try {
            String getEnUserName = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getString("userName", null);
            getuserName = AESUtil.decrypt(getEnUserName, com.manyi.mall.common.CommonConfig.AES_KEY);
            String getEnPassword = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getString("password", null);
            getpassword = AESUtil.decrypt(getEnPassword, com.manyi.mall.common.CommonConfig.AES_KEY);
            if (getuserName != null && getpassword != null) {
                againlogin(getuserName, getpassword);
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

    @Background
    void againlogin(String againuserName, String againpassword) {

        try {
            LoginRequest req = new LoginRequest();
            req.setLoginName(againuserName);
            req.setPassword(againpassword);
            LoginResponse res = mService.startLogin(req);
            int state = res.getState();
            String bankCode = res.getBankCode();
            SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences(Constants.LOGIN_TIMES,
                    Context.MODE_PRIVATE + Context.MODE_PRIVATE);
            Editor editor = sharedPreferences2.edit();
            editor.putInt("cityId", res.getCityId());
            editor.putString("cityName", res.getCityName());
            editor.putInt("uid", res.getUid());
            editor.putInt("sumCount", res.getPublishCount());
            editor.putInt("state", res.getState());
            editor.putString("userName", AESUtil.encrypt(res.getUserName(), com.manyi.mall.common.CommonConfig.AES_KEY));
            editor.putString("realName", AESUtil.encrypt(res.getRealName(), com.manyi.mall.common.CommonConfig.AES_KEY));
            editor.putInt("PublishCount", res.getPublishCount());
            editor.putInt("surplusCount", res.getSumCount());
            editor.commit();
            initTab(state, bankCode);
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
    void initTab(int state, String alipayAccount) {
        switch (state) {
            case 1:
                SharedPreferences sharedPreferences = getBackOpActivity().getSharedPreferences(Constants.LOGIN_TIMES, Context.MODE_PRIVATE);
                boolean isShowBind = sharedPreferences.getBoolean("isShowBind", false);
                if (TextUtils.isEmpty(alipayAccount) && !isShowBind) {
                    BandBankCardFragment bandBankCardFragment = GeneratedClassUtils.getInstance(BandBankCardFragment.class);
                    bandBankCardFragment.tag = BandBankCardFragment.class.getName();
                    bandBankCardFragment.setManager(getFragmentManager());
                    bandBankCardFragment.setBackOp(new AbsOp(ReviewFragment.class.getName()) {

                        @Override
                        public void perform(BackOpFragmentActivity activity) {
                            initMainActivity2();
                        }
                    });
                    bandBankCardFragment.show(SHOW_ADD_HIDE);
                    Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isShowBind", true);
                    editor.commit();
                } else {
                    initMainActivity2();
                }
                break;
            case 2:
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
            case 3:
                ReviewFailedFragment reviewFailedFragment = GeneratedClassUtils.getInstance(ReviewFailedFragment.class);
                Bundle argsBundle = new Bundle();
                argsBundle.putString("mUserName", getuserName);
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

    private void initMainActivity2() {
        Intent loginIntent = new Intent(getActivity(), GeneratedClassUtils.get(MainActivity.class));
        getActivity().overridePendingTransition(R.anim.anim_fragment_in, R.anim.anim_fragment_out);
        startActivity(loginIntent);
        getActivity().finish();
    }

    /**
     * 取消通知栏
     */
    public void cancelNotice() {
        NotificationManager notificationMgr = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            notificationMgr.cancelAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    void showError(String e) {
        DialogBuilder.showSimpleDialog(e, getBackOpActivity());

    }

    @UiThread
    public void restErrorProcess(String detailMessage, int errorCode) {
        if (errorCode == RestException.INVALID_TOKEN) {
            DialogBuilder.showSimpleDialog(detailMessage, getActivity());
        } else {
            DialogBuilder.showSimpleDialog(detailMessage, getActivity());
        }
    }

    @UiThread
    public void unexpectedErrorProcess(String detailMessage) {
        DialogBuilder.showSimpleDialog(detailMessage, getActivity());
    }
}
