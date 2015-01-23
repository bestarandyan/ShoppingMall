package com.huoqiu.framework.app;

import android.app.Application;

public class AppConfig {
	/**
	 * 应用初始化必须赋值
	 */
	public static Application application = null;
	public static int versionCode = 1;
	public static String versionName = "1.0";

    /**
     * 根据项目需求选择完善的属性
     */
    public static String UID = "-1"; // 已登录用户的Uid，默认为-1
    public static String u_ticket = ""; // 已登录用户的cookie，默认""


    /**add by w_xiong*/

    /**渠道号，统计崩溃信息用*/
    public static String channelNo = "iwjw";

    /**是否线上版本*/
    public static boolean IS_RELEASE_VERSION = true;

    /**平台名称 IWJW或者FYB*/
    public static String platform = "IWJW";

    public static String distance = "0.0";

}
