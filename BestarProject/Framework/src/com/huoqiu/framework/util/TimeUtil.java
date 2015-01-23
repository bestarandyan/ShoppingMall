/**
 * File: TimeUtil
 */
package com.huoqiu.framework.util;
import java.util.Calendar;

public class TimeUtil {
	private static long localTime; // 本地时间
	private static long serviceTime;// 服务器时间
	private static boolean fetchTime = false; // 获取服务器时间是否成功
	private static boolean timeFlag = true; // 时间是否有效

	// 确定服务器时间和本地时间是否一致，30秒内认为一致
	public static void calTime(long inputTime) {
		serviceTime = inputTime;
		setFetchTime(true);
		localTime = System.currentTimeMillis();
		if (Math.abs(localTime - serviceTime) > 60000) {
			timeFlag = false;
		}
	}

	public static boolean isFetchTime() {
		return fetchTime;
	}

	public static void setFetchTime(boolean fetchTime) {
		TimeUtil.fetchTime = fetchTime;
	}

	/**
	 * 服务器时间获取是否成功，获取校正后的本地时间 否则获取手机本地时间
	 * 
	 * @return Calendar
	 * @see DateUtil#
	 */
	public static Calendar getCurrentTime() {
		Calendar currentCalendar = DateUtil.getCalendarTime();
		if (!timeFlag) {
			currentCalendar.setTimeInMillis(serviceTime + System.currentTimeMillis() - localTime);
		}
		return currentCalendar;
	}
}
