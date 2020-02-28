package com.wlcb.wlj.module.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName DateUtils
 * @Description TODO 时间工具类
 * @Author 郭丁志
 * @Date 2020-02-03 18:40
 * @Version 1.0
 */
public class DateUtils {

    public static final String dateFormat = "yyyyMMdd";
    public static final String timeFormat = "HHmmss";
    public static final String dateTimeFormat = "yyyyMMddHHmmss";
    public static final String dateTimeMsFormat = "yyyyMMddHHmmssSSS";

    public DateUtils() {
    }

    public static String getDateTime() {
        return (new SimpleDateFormat("yyyyMMddHHmmss")).format(Calendar.getInstance().getTime());
    }

    public static String getDate() {
        return (new SimpleDateFormat("yyyyMMdd")).format(Calendar.getInstance().getTime());
    }

    public static String getTime() {
        return (new SimpleDateFormat("HHmmss")).format(Calendar.getInstance().getTime());
    }

    public static String getDate(int nday) {
        Date d = Calendar.getInstance().getTime();
        return (new SimpleDateFormat("yyyyMMdd")).format(getDate(d, nday));
    }

    public static String getDate(String date, int nday) {
        try {
            SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
            Date d = f.parse(date);
            return f.format(getDate(d, nday));
        } catch (ParseException var4) {
            return null;
        }
    }

    public static Date getDate(Date d, int nday) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(6, nday);
        return c.getTime();
    }

    public static String getDate(String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(Calendar.getInstance().getTime());
    }

    public static String getDate(Date date, String format) {
        return (new SimpleDateFormat(format)).format(date);
    }

    public static Date parseDate(String date, String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);

        try {
            return sf.parse(date);
        } catch (ParseException var4) {
            return null;
        }
    }

    public static DateUtils.DateAndTime getDateAndTime() {
        return new DateUtils.DateAndTime();
    }

    public static DateUtils.DateAndTime getDateAndTime(Date date) {
        return new DateUtils.DateAndTime(date);
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
        Date d = f.parse("20131107021217");
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(13, 303165);
        System.out.println(getDate(c.getTime(), "yyyyMMddHHmmss"));
    }

    public static class DateAndTime {
        private String date;
        private String time;

        public DateAndTime() {
            this(Calendar.getInstance().getTime());
        }

        public DateAndTime(Date d) {
            String s = (new SimpleDateFormat("yyyyMMddHHmmss")).format(d);
            this.date = s.substring(0, 8);
            this.time = s.substring(8, 14);
        }

        public String getDate() {
            return this.date;
        }

        public String getTime() {
            return this.time;
        }
    }

}
