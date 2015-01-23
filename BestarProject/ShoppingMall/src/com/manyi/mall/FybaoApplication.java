package com.manyi.mall;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EApplication;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.huoqiu.framework.app.AppConfig;
import com.manyi.mall.common.util.DBUtil;

@EApplication
public class FybaoApplication extends Application {
	private static FybaoApplication application = null;

	public static FybaoApplication getInstance() {
		return application;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		// 初始化Cookies设置
		initConnCookies();
		// 初始化AppStatus参数
		initAppStatus();

		loadData();
	}

	/**
	 * 拷贝数据库
	 */
	@Background
	public void loadData() {
		DBUtil.loadDataFromRawDatabase(getApplicationContext());
	}

	/**
	 * 初始化Cookies设置
	 */
	private void initConnCookies() {
		System.setProperty("http.keepAlive", "false");
		System.setProperty("apache.commons.httpclient.cookiespec", "COMPATIBILITY");
		System.setProperty("HTTPClient.cookies.save", "true");
		System.setProperty("HTTPClient.cookies.jar", "/home/misha/.httpclient_cookies");
	}

	/**
	 * 初始化AppStatus参数
	 */
	private void initAppStatus() {

		// 初始化版本信息
		try {
			PackageManager pm = getPackageManager();
			PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
			AppConfig.versionName = pi.versionName;
			AppConfig.versionCode = pi.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 初始化Application
		AppConfig.application = application;
		 /**渠道号，统计崩溃信息用*/
	    AppConfig.channelNo = "Fybao";
	    /**是否线上版本*/
	    AppConfig.IS_RELEASE_VERSION = true;
	    /**平台名称 IWJW或者FYB*/
	    AppConfig.platform = "FYB";

	}
}
