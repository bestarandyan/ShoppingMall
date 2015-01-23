package com.huoqiu.framework.exception;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.app.AppConfig;
import com.huoqiu.framework.util.*;

import java.io.*;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;

/**
 * Created by Ted on 14-7-3.
 */
public class BaseUncaughtExceptionHandler implements UncaughtExceptionHandler {
    private Activity mActivity;
    private PendingIntent mIntent;
    private UncaughtExceptionHandler defaultExceptionHandler;
    private static BaseUncaughtExceptionHandler baseUncaughtExceptionHandler;
    private static String FOLDER = File.separator + "MANYI" + File.separator;
    final static String FILENAME = "Errorlog";
    final static String FILENAMES = "Errorlogs";
    final static String SUFFIX = ".txt";
    private String mExceptionInfo;
    private String TAG = "BaseUncaughtExceptionHandler";

    private BaseUncaughtExceptionHandler() {

    }

    public static synchronized BaseUncaughtExceptionHandler getInstance() {
        if (baseUncaughtExceptionHandler == null) {
            baseUncaughtExceptionHandler = new BaseUncaughtExceptionHandler();
        }
        return baseUncaughtExceptionHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (null != defaultExceptionHandler) {
            if (ex instanceof ClientException) {

                ClientException ce = (ClientException) ex;

                if (ce.getErrorCode() == ClientException.REPEATED_REQUEST || ce.getErrorCode() == ClientException.REQUEST_ABORTED)
                    return;
                else {
                    showErrorInfo(ex.getMessage());
                    LogUtil.w(TAG, ex);
                }

            } else if (ex instanceof RestException) {
                showErrorInfo(ex.getMessage());
                LogUtil.w(TAG, ex);

            } else if (ex instanceof ServerException) {
                if (AppConfig.IS_RELEASE_VERSION) {
                    // 收集错误信息
                    ManyiAnalysis.reportError(mActivity, ex);
                }
                showErrorInfo(ex.getMessage());
                LogUtil.w(TAG, ex);
            } else {
                if (AppConfig.IS_RELEASE_VERSION) {
                    // 收集错误信息
                    ManyiAnalysis.reportError(mActivity, ex);
                }
                // 处理错误信息
                handleUncaughtException(thread, ex);
            }
        }
    }

    /**
     * 处理错误信息
     *
     * @param thread
     * @param ex
     */
    protected void handleUncaughtException(Thread thread, Throwable ex) {
        String logLabel = "\r\n" + "异常发生时间:" + DateUtil.getCalendarStrBySimpleDateFormat(TimeUtil.getCurrentTime(), DateUtil.SIMPLEFORMATTYPESTRING4) + "\r\n";
        String exceptionInfo = "\n ExceptionStart" + logLabel + "\r\nPlatform:" + getPlatformInfo() + "\r\n" + getVersionInfo() + "\r\n" + getMobileInfo() + "\r\n" + getErrorInfo(ex) + "\r\n ExceptionEND";
        LogUtil.e(TAG, "Exception" + exceptionInfo);
        LogUtil.w(TAG, ex);
        mExceptionInfo = exceptionInfo;
        /**错误信息写到本地*/
        writeFile();
        /**重启应用*/
        reboot();
//        LogUtil.w(TAG,ex);
//        System.exit(0);
//        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 在UI线程弹框
     *
     * @param detailMessage
     */
    private void showErrorInfo(final String detailMessage) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogBuilder.showSimpleDialog(detailMessage, mActivity);
            }
        });
    }

    public void init(Activity activity) {
        mActivity = activity;
        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void setmIntent(PendingIntent mIntent) {
        this.mIntent = mIntent;
    }

    public void reboot() {
        if (mIntent != null) {
            AlarmManager mgr = (AlarmManager) mActivity.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mIntent);
            System.exit(1);
            return;
        } else {
            LogUtil.e(TAG, "已经捕获到Exception，但是没有获取到PendingIntent，应用无法正常重启");
        }
        System.exit(1);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void writeFile() {
        if (StringUtil.isEmptyOrNull(mExceptionInfo)) {
            return;
        }
        String folderName = Environment.getExternalStorageDirectory().getPath() + FOLDER;
        File folder = new File(folderName);
        if (folder != null && !folder.exists()) {
            if (!folder.mkdir() && !folder.isDirectory()) {
                LogUtil.e(TAG, "创建文件失败");
                return;
            }
        }

        String targetPath = folderName + FILENAME + SUFFIX;
        File targetFile = new File(targetPath);
        if (targetFile != null) {

            FileWriter ss = null;
            try {
                /**记录最近一次崩溃日志*/
                ss = new FileWriter(targetFile, false);
                ss.append(mExceptionInfo + "\r\n");
                ss.flush();
                ss.close();
                /**记录所有的崩溃日志*/
                String targetPath2 = folderName + FILENAMES + SUFFIX;
                File targetFile2 = new File(targetPath2);
                FileWriter ss2 = new FileWriter(targetFile2, true);
                ss2.append(mExceptionInfo + "\r\n");
                ss2.flush();
                ss2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取错误的信息
     *
     * @param arg1
     * @return
     */
    private String getErrorInfo(Throwable arg1) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        arg1.printStackTrace(pw);

        String[] errorInfo = writer.toString().split("\n\tat");
        String error = "";
        int length = errorInfo.length > 6 ? 6 : errorInfo.length;
        for (int i = 0; i < length; i++) {
            error = error + errorInfo[i] + "\n\t";
        }
        pw.close();
        return "异常内容：" + error;
    }

    /**
     * 获取手机的硬件信息
     *
     * @return
     */
    private String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        // 通过反射获取系统的硬件信息
        try {

            Field[] fields = Build.class.getDeclaredFields();
            sb.append("手机硬件信息：");
            int lenth = fields.length;
            for (Field field : fields) {
                // 暴力反射 ,获取私有的信息
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                if (field.equals(fields[lenth / 2])) {
                    sb.append(name + "=" + value + "||\r\n");
                } else {
                    sb.append(name + "=" + value + "||");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 获取手机的版本信息
     *
     * @return
     */
    private String getVersionInfo() {
        try {
            PackageManager pm = mActivity.getPackageManager();
            PackageInfo info = pm.getPackageInfo(mActivity.getPackageName(), 0);
            String softInfo = "版本号信息：" + info.applicationInfo.packageName + "  " + info.versionName + "  " + AppConfig.channelNo;
            return softInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return "版本号信息未知";
        }
    }

    private String getPlatformInfo() {
        return AppConfig.platform;
    }
}
