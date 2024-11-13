package com.ssmalllucky.android.core.utils;

import android.text.TextUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期和时间格式转换工具类
 *
 * @author shuaijialin
 */
public class DateAndTimeUtils {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_FORMAT_PRECISION_3 = "yyyy-MM-dd HH:mm:ss";

    private static String getDateFormat(String format) {
        if (TextUtils.isEmpty(format)) {
            return DEFAULT_DATE_FORMAT;
        }
        return format;
    }

    /**
     * 得到当前时间
     *
     * @param format 日期或者时间格式
     * @return 字符串格式日期或者时间
     */
    public static String getCurrentDate(String format) {
        String resultDate = "";
        DateFormat sdf = new SimpleDateFormat(getDateFormat(format), Locale.CHINA);
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        try {
            resultDate = sdf.format(ts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultDate;
    }

    public static String date2String(Date date,String format) {
        if(format == null){
            format = DEFAULT_DATE_FORMAT;
        }
        String resultDate = "";
        DateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        try {
            resultDate = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultDate;
    }

    @SuppressWarnings("unused")
    public static long subCal(String beginTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        long betweenDate = 0;
        try {
            long beginDate = sdf.parse(beginTime).getTime();
            long nowDate;
            if (endTime == null)
                nowDate = calendar.getTime().getTime(); //Date.getTime() 获得毫秒型日期
            else
                nowDate = sdf.parse(endTime).getTime();
            betweenDate = (beginDate - nowDate) / (1000); //计算间隔多少天，则除以毫秒到天的转换公式(1000 * 60 * 60 * 24)
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return betweenDate;
    }

    @SuppressWarnings("unused")
    public static Timestamp stringToTimestamp(String tsStr) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        try {
            ts = Timestamp.valueOf(tsStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ts;
    }

    @SuppressWarnings("unused")
    public static String timestampToString(Timestamp ts) {
        String tsStr = "";
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
        try {
            tsStr = sdf.format(ts);
            System.out.println(tsStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tsStr;
    }

    @SuppressWarnings("unused")
    public static String timestampToString(Timestamp ts, String format) {
        String tsStr = "";
        DateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        try {
            tsStr = sdf.format(ts);
            System.out.println(tsStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tsStr;
    }

    @SuppressWarnings("unused")
    public static Date timestampToDate(Timestamp ts) {
        Date date = new Date();
        try {
            date = ts;
            System.out.println(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @param dt
     * @return
     */
    @SuppressWarnings("unused")
    public static Timestamp dateToTimestamp(Date dt) {
        return new Timestamp(dt.getTime());
    }

    public static Date stringToDate(String str) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return formatter.parse(str);
    }

    public static Calendar stringToCalendar(String str) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = formatter.parse(str);
            calendar.setTime(date);
            return calendar;
        } catch (Exception e) {
            return calendar;
        }
    }

    @SuppressWarnings("unused")
    public static String longToString(long time, String format) {
        String tsStr = "";
        DateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        Timestamp ts = new Timestamp(time);
        try {
            tsStr = sdf.format(ts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tsStr;
    }

    public static long compareDateWithToday(Date compareDate) {

        //首先对传入的日期做判空处理
        if (compareDate == null) {
            return 1;
        }

        Date nowDate = new Date();
        return nowDate.getTime() - compareDate.getTime();
    }

    public static long compareDateStrWithToday(String compareDateStr) {

        //首先对传入的日期字符串做判空处理
        if (TextUtils.isEmpty(compareDateStr)) {
            return 1;
        }

        try {
            Date compareDate = stringToDate(compareDateStr);
            Date nowDate = new Date();
            return nowDate.getTime() - compareDate.getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 比较日期时间大小。
     *
     * @param startDateStr 开始时间
     * @param endDateStr   结束时间
     * @param startOrEnd   true表示开始时间是否大于结束时间，false表示结束时间是否大于开始时间
     * @return long型格式日期或者时间
     */
    public static long compareDate(String startDateStr, String endDateStr, boolean startOrEnd) {

        //首先对传入的日期字符串做判空处理
        if (TextUtils.isEmpty(startDateStr) || TextUtils.isEmpty(endDateStr)) {
            return 1;
        }

        try {
            Date startDate = stringToDate(startDateStr);
            Date endDate = stringToDate(endDateStr);
            if (startOrEnd) {
                return startDate.getTime() - endDate.getTime();
            } else {
                return endDate.getTime() - startDate.getTime();
            }
        } catch (ParseException e) {
            return 1;
        }
    }
}
