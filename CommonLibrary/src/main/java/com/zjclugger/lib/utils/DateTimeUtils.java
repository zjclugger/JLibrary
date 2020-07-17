package com.zjclugger.lib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类<br>
 * Created by King.Zi on 2020/7/6.<br>
 * Copyright (c) 2020 zjclugger.com
 */
public class DateTimeUtils {
    public static final String DATE_FORMAT_YMDHM = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";

    private DateTimeUtils() {
    }

    /**
     * 获取年
     *
     * @return
     */
    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 获取月
     *
     * @return
     */
    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日
     *
     * @return
     */
    public static int getDay() {
        return Calendar.getInstance().get(Calendar.DATE);
    }

    /**
     * 获取时
     *
     * @return
     */
    public static int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR);
    }

    /**
     * 获取分
     *
     * @return
     */
    public static int getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    public static Calendar dataToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Calendar dataToCalendar(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(date));
            return calendar;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取日期时间
     *
     * @param date
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getDateTime(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static long dateToStamp(String dataTime, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(dataTime).getTime();
        } catch (ParseException e) {
            return 0l;
        }
    }

    public static String stampToDate(long dateTime, String format) {
        Date date = new Date(dateTime);
        return getDateTime(date, format);
    }
}