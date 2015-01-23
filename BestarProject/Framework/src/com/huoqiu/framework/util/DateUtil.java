package com.huoqiu.framework.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static final String TIMEZONE_CN = "Asia/Shanghai";
    /**
     * ********************SIMPLEFORMATTYPE对应的字串*********************
     */
    /**
     * SIMPLEFORMATTYPESTRING1 对应类型：yyyyMMddHHmmss
     */
    public final static String SIMPLEFORMATTYPESTRING1 = "yyyyMMddHHmmss";

    /**
     * SIMPLEFORMATTYPESTRING2 对应的类型：yyyy-MM-dd HH:mm:ss
     */
    public final static String SIMPLEFORMATTYPESTRING2 = "yyyy-MM-dd HH:mm:ss";

    /**
     * SIMPLEFORMATTYPESTRING3 对应的类型：yyyy-M-d HH:mm:ss
     */
    public final static String SIMPLEFORMATTYPESTRING3 = "yyyy-M-d HH:mm:ss";

    /**
     * SIMPLEFORMATTYPESTRING4对应的类型：yyyy-MM-dd HH:mm
     */
    public final static String SIMPLEFORMATTYPESTRING4 = "yyyy-MM-dd HH:mm";

    /**
     * SIMPLEFORMATTYPESTRING5 对应的类型：yyyy-M-d HH:mm
     */
    public final static String SIMPLEFORMATTYPESTRING5 = "yyyy-M-d HH:mm";

    /**
     * SIMPLEFORMATTYPESTRING6对应的类型：yyyyMMdd
     */
    public final static String SIMPLEFORMATTYPESTRING6 = "yyyyMMdd";

    /**
     * SIMPLEFORMATTYPESTRING7对应的类型：yyyy-MM-dd
     */
    public final static String SIMPLEFORMATTYPESTRING7 = "yyyy-MM-dd";


    /**
     * SIMPLEFORMATTYPESTRING8对应的类型： yyyy-M-d
     */
    public final static String SIMPLEFORMATTYPESTRING8 = "yyyy-M-d";

    /**
     * SIMPLEFORMATTYPESTRING9对应的类型：yyyy年MM月dd日
     */
    public final static String SIMPLEFORMATTYPESTRING9 = "yyyy年MM月dd日";

    /**
     * SIMPLEFORMATTYPESTRING10对应的类型：yyyy年M月d日
     */
    public final static String SIMPLEFORMATTYPESTRING10 = "yyyy年M月d日";

    /**
     * SIMPLEFORMATTYPESTRING11对应的类型：M月d日
     */
    public final static String SIMPLEFORMATTYPESTRING11 = "M月d日";

    /**
     * SIMPLEFORMATTYPESTRING12对应的类型：HH:mm:ss
     */
    public final static String SIMPLEFORMATTYPESTRING12 = "HH:mm:ss";

    /**
     * SIMPLEFORMATTYPESTRING13对应的类型：HH:mm
     */
    public final static String SIMPLEFORMATTYPESTRING13 = "HH:mm";
    /**
     * SIMPLEFORMATTYPESTRING14对应的类型：yyyy-MM-dd
     */
    public final static String SIMPLEFORMATTYPESTRING14 = "yyyy/MM/dd";
    /**
     * SIMPLEFORMATTYPESTRING15对应的类型：MM/dd
     */
    public final static String SIMPLEFORMATTYPESTRING15 = "MM/dd";
    /**
     * SIMPLEFORMATTYPESTRING16对应的类型：MM月dd日
     */
    public final static String SIMPLEFORMATTYPESTRING16 = "MM月dd日";
    /**
     * SIMPLEFORMATTYPESTRING17对应的类型：MM月dd日 HH:mm
     */
    public final static String SIMPLEFORMATTYPESTRING17 = "MM月dd日 HH:mm";
    /**
     * SIMPLEFORMATTYPESTRING18对应的类型：yyyy年MM月dd日 HH:mm
     */
    public final static String SIMPLEFORMATTYPESTRING18 = "yyyy年MM月dd日 HH:mm";

    /**
     * SIMPLEFORMATTYPESTRING7对应的类型： yy-MM-dd
     */
    public final static String SIMPLEFORMATTYPESTRING19 = "yy-MM-dd";

    /**
     * SIMPLEFORMATTYPESTRING20对应的类型： yy-MM-dd
     */
    public final static String SIMPLEFORMATTYPESTRING20 = "yy-MM-dd  HH:mm";

    /**
     * SIMPLEFORMATTYPESTRING20对应的类型： yy-MM-dd
     */
    public final static String SIMPLEFORMATTYPESTRING21 = "mm月dd日  HH:mm ";

    /**
     * 将时间转换为simpleFormatString对应的格式输出
     *
     * @param calendar
     * @param simpleFormatString
     * @return
     */
    public static String getCalendarStrBySimpleDateFormat(Calendar calendar, String simpleFormatString) {
        String str = "";
        if (!StringUtil.isEmptyOrNull(simpleFormatString) && calendar != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(simpleFormatString);
            dateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE_CN));
            str = (dateFormat).format(calendar.getTime());
        }
        return str;
    }

    /**
     * 将时间转换为simpleFormatString对应的格式输出
     *
     * @param time
     * @param simpleFormatString
     * @return
     */
    public static String getCalendarStrBySimpleDateFormat(Long time, String simpleFormatString) {
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat(simpleFormatString);
        return dateFormat.format(date);
    }

    /**
     * 将时间字串#yyyyMMddHHmmss转为 Calendar
     *
     * @param dateStr 小于8位时返回null，不足14位补0
     * @return
     */
    @SuppressWarnings("ResourceType")
    public static Calendar getCalendarByDateTimeStr(String dateStr) {
        if (StringUtil.isEmptyOrNull(dateStr) || dateStr.length() < 8)
            return null;
        while (dateStr.length() < 14) {
            dateStr += "0";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(TIMEZONE_CN));
        int year = StringUtil.toInt(dateStr.substring(0, 4));
        int month = StringUtil.toInt(dateStr.substring(4, 6));
        int day = StringUtil.toInt(dateStr.substring(6, 8));
        int hour = StringUtil.toInt(dateStr.substring(8, 10));
        int min = StringUtil.toInt(dateStr.substring(10, 12));
        int second = 0;
        if (dateStr.length() >= 14)
            second = StringUtil.toInt(dateStr.substring(12, 14));
        calendar.set(year, month - 1, day, hour, min, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * 获取手机系统日期
     *
     * @return
     */
    public static Calendar getCalendarTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(TIMEZONE_CN));
        return calendar;
    }

    /**
     * 获取服务器系统时间
     *
     * @return
     */
    public static Calendar getCurrentTime() {
        return TimeUtil.getCurrentTime();
    }

    /**
     * 发布时间格式： 1小时内:XX分钟前 24小时内：XX小时前 7天内：X天前 超过7天：显示日期
     *
     * @param date
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String fattrDate(Date date) {
        String fattrStr = "";
        if (date != null) {
            long time = getCurrentTime().getTime().getTime() - date.getTime();// 得到 当前时间的毫秒数
            long minute = time / toMinute; // 分钟
            long hours = time / toHour;// 小时
            long day = caculateDay(getCurrentTime().getTime(), date); // 天
            if (day >= 7) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
                fattrStr = sdf.format(date);
            } else if (day >= 1 && day < 7) {
                fattrStr = day + "天前";
            } else if (1 <= hours && hours < 24) {
                fattrStr = hours + "小时前";
            } else if (hours < 1) {
                fattrStr = (minute <= 1 ? 1 : minute) + "分钟前";
            }
        }
        return fattrStr;
    }

    private final static long toDay = 1000 * 60 * 60 * 24; // 将毫秒数转换为天
    private final static long toHour = 1000 * 60 * 60; // 将毫秒数转换为小时
    private final static long toMinute = 1000 * 60; // 将毫秒数转换为分钟

    /**
     * 发布时间格式： 1小时内:XX分钟前 24小时内：XX小时前 7天内：X天前
     * <p/>
     * 超过一天显示“**天前”；当天显示“刚刚”
     *
     * @param date
     * @return
     */
    public static String fattrDay(Date date) {
        String fattrStr = "";
        if (date != null) {
            long currentDay = getCurrentTime().getTimeInMillis() / toDay;
            long desDay = date.getTime() / toDay;
            if (currentDay == desDay) {
                fattrStr = "刚刚";
            } else {
                long day = currentDay - desDay;// 得到 当前相隔的天数
                fattrStr = day + "天前";
            }
        }
        return fattrStr;
    }

    /**
     * 返回两个日期的小时差，异常返回-1；
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long caculateHour(Date date1, Date date2) {

        long result = -1;
        if (date1 != null && date2 != null) {
            long maxDay = date1.getTime() / toHour;
            long minDay = date2.getTime() / toHour;
            result = maxDay > minDay ? maxDay - minDay : minDay - maxDay;
        }
        return result;

    }

    /**
     * 返回两个日期的天数差，同一天返回0，异常返回-1；
     *
     * @param maxDate
     * @param minDate
     * @return
     */
    public static long caculateDay(Date maxDate, Date minDate) {
        long day = -1;
        if (maxDate != null && minDate != null) {
            long maxDay = maxDate.getTime() / toDay;
            long minDay = minDate.getTime() / toDay;
            day = maxDay > minDay ? maxDay - minDay : minDay - maxDay;
        }
        return day;
    }

    /**
     * 返回目标日期同今天的天数差，同一天返回0，异常返回-1；
     *
     * @param desDate
     * @return
     */
    public static long caculateDayFromToday(Date desDate) {
        return caculateDay(getCurrentTime().getTime(), desDate);
    }

    /**
     * /**
     * 返回目标日期同今天的天数差，同一天返回true，异常返回false；
     *
     * @param dateA , dateB
     * @return
     */

    public static boolean areSameDay(Date dateA, Date dateB) {
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(dateA);

        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(dateB);

        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * /**
     * 返回目标日期同今天的天数差，同一天返回true，异常返回false；
     *
     * @param desDate
     * @return
     */
    public static boolean ifSameDate(Date desDate) {
        return areSameDay(getCurrentTime().getTime(), desDate);
    }
}
