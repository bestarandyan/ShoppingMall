package com.huoqiu.framework.analysis;

import android.content.Context;
import com.huoqiu.framework.rest.Configuration;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

public class ManyiAnalysis {

    public static void init(Context context) {
        MobclickAgent.setDebugMode(false);
        //SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        //然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setCatchUncaughtExceptions(false);
        MobclickAgent.updateOnlineConfig(context);
    }

    public static void init(Context context,String key) {
        init(context);
        AnalyticsConfig.setAppkey(key);
    }

    public static void onEvent(Context context, String eventId) {
        if (Configuration.DEFAULT == Configuration.TEST) {
            return;
        }
        MobclickAgent.onEvent(context, eventId);
    }

    public static void onPageStart(String page) {
        if (Configuration.DEFAULT == Configuration.TEST) {
            return;
        }
        MobclickAgent.onPageStart(page);
    }

    public static void onPageEnd(String page) {
        if (Configuration.DEFAULT == Configuration.TEST) {
            return;
        }
        MobclickAgent.onPageEnd(page);
    }

    public static void onResume(Context context) {
        if (Configuration.DEFAULT == Configuration.TEST) {
            return;
        }
        MobclickAgent.onResume(context);
    }

    public static void onPause(Context context) {
        if (Configuration.DEFAULT == Configuration.TEST) {
            return;
        }
        MobclickAgent.onPause(context);
    }

    public static void reportError(Context context, Throwable e){
        if (Configuration.DEFAULT == Configuration.TEST) {
            return;
        }
        MobclickAgent.reportError(context, e);
        MobclickAgent.onKillProcess(context);
    }
}