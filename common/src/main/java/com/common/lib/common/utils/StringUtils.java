package com.common.lib.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作工具包
 *
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class StringUtils {
    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    private final static Pattern IMG_URL = Pattern
            .compile(".*?(gif|jpeg|png|jpg|bmp)");

    private final static Pattern URL = Pattern
            .compile("^(https|http)://.*?$(net|com|.com.cn|org|me|)");

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormat3 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }
    };
    private static SharedPreferences mShareConfig;

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        return toDate(sdate, dateFormater.get());
    }

    public static Date toDate(String sdate, SimpleDateFormat dateFormater) {
        try {
            return dateFormater.parse(sdate);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getDateString(Date date) {
        return dateFormater.get().format(date);
    }

    public static String getDateString(String sdate) {
        return dateFormat3.get().format(toDate(sdate));
    }

    /**
     * 智能格式化
     */
    public static String friendly_time3(String sdate) {
        String res = "";
        if (isEmpty(sdate))
            return "";

        Date date = StringUtils.toDate(sdate);
        if (date == null)
            return sdate;

        SimpleDateFormat format = dateFormater2.get();

        if (isToday(date.getTime())) {
            format.applyPattern(isMorning(date.getTime()) ? "上午 hh:mm" : "下午 hh:mm");
            res = format.format(date);
        } else if (isYesterday(date.getTime())) {
            format.applyPattern(isMorning(date.getTime()) ? "昨天 上午 hh:mm" : "昨天 下午 hh:mm");
            res = format.format(date);
        } else if (isCurrentYear(date.getTime())) {
            format.applyPattern(isMorning(date.getTime()) ? "MM-dd 上午 hh:mm" : "MM-dd 下午 hh:mm");
            res = format.format(date);
        } else {
            format.applyPattern(isMorning(date.getTime()) ? "yyyy-MM-dd 上午 hh:mm" : "yyyy-MM-dd 下午 hh:mm");
            res = format.format(date);
        }
        return res;
    }

    /**
     * @return 判断一个时间是不是上午
     */
    public static boolean isMorning(long when) {
        android.text.format.Time time = new android.text.format.Time();
        time.set(when);

        int hour = time.hour;
        return (hour >= 0) && (hour < 12);
    }

    /**
     * @return 判断一个时间是不是今天
     */
    public static boolean isToday(long when) {
        android.text.format.Time time = new android.text.format.Time();
        time.set(when);

        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;

        time.set(System.currentTimeMillis());
        return (thenYear == time.year)
                && (thenMonth == time.month)
                && (thenMonthDay == time.monthDay);
    }

    /**
     * @return 判断一个时间是不是昨天
     */
    public static boolean isYesterday(long when) {
        android.text.format.Time time = new android.text.format.Time();
        time.set(when);

        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;

        time.set(System.currentTimeMillis());
        return (thenYear == time.year)
                && (thenMonth == time.month)
                && (time.monthDay - thenMonthDay == 1);
    }

    /**
     * @return 判断一个时间是不是今年
     */
    public static boolean isCurrentYear(long when) {
        android.text.format.Time time = new android.text.format.Time();
        time.set(when);

        int thenYear = time.year;

        time.set(System.currentTimeMillis());
        return (thenYear == time.year);
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static int getWeekOfDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return w;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 是否是相同的一天
     *
     * @param sDate1 sDate1
     * @param sDate2 sDate2
     * @return
     */
    public static boolean isSameDay(String sDate1, String sDate2) {
        if (TextUtils.isEmpty(sDate1) || TextUtils.isEmpty(sDate2)) {
            return false;
        }
        boolean b = false;
        Date date1 = toDate(sDate1);
        Date date2 = toDate(sDate2);
        if (date1 != null && date2 != null) {
            String d1 = dateFormater2.get().format(date1);
            String d2 = dateFormater2.get().format(date2);
            if (d1.equals(d2)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 返回long类型的今天的日期
     *
     * @return
     */
    public static long getToday() {
        Calendar cal = Calendar.getInstance();
        String curDate = dateFormater2.get().format(cal.getTime());
        curDate = curDate.replace("-", "");
        return Long.parseLong(curDate);
    }

    public static String getCurTimeStr() {
        Calendar cal = Calendar.getInstance();
        String curDate = dateFormater.get().format(cal.getTime());
        return curDate;
    }

    /***
     * 计算两个时间差，返回的是的秒s
     *
     * @param dete1
     * @param date2
     * @return
     * @author 火蚁 2015-2-9 下午4:50:06
     */
    public static long calDateDifferent(String dete1, String date2) {

        long diff = 0;

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = dateFormater.get().parse(dete1);
            d2 = dateFormater.get().parse(date2);

            // 毫秒ms
            diff = d2.getTime() - d1.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return diff / 1000;
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 判断一个url是否为图片url
     *
     * @param url
     * @return
     */
    public static boolean isImgUrl(String url) {
        if (url == null || url.trim().length() == 0)
            return false;
        return IMG_URL.matcher(url).matches();
    }

    /**
     * 判断是否为一个合法的url地址
     *
     * @param str
     * @return
     */
    public static boolean isUrl(String str) {
        if (str == null || str.trim().length() == 0)
            return false;
        return URL.matcher(str).matches();
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    public static String getString(String s) {
        return s == null ? "" : s;
    }

    /**
     * 将一个InputStream流转换成字符串
     *
     * @param is
     * @return
     */
    public static String toConvertString(InputStream is) {
        StringBuffer res = new StringBuffer();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader read = new BufferedReader(isr);
        try {
            String line;
            line = read.readLine();
            while (line != null) {
                res.append(line + "<br>");
                line = read.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != isr) {
                    isr.close();
                    isr.close();
                }
                if (null != read) {
                    read.close();
                    read = null;
                }
                if (null != is) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
            }
        }
        return res.toString();
    }

    /***
     * 截取字符串
     *
     * @param start 从那里开始，0算起
     * @param num   截取多少个
     * @param str   截取的字符串
     * @return
     */
    public static String getSubString(int start, int num, String str) {
        if (str == null) {
            return "";
        }
        int leng = str.length();
        if (start < 0) {
            start = 0;
        }
        if (start > leng) {
            start = leng;
        }
        if (num < 0) {
            num = 1;
        }
        int end = start + num;
        if (end > leng) {
            end = leng;
        }
        return str.substring(start, end);
    }


    /**
     * 返回当前系统时间
     */
    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }


    /*   获取今天日期*/
    public static String getToday(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /*    获取明天日期*/
    public static String getTomorry(String format) {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String endtime = sdf2.format(date);
        return endtime;
    }

    /**
     * 判断是否是以1开头的11位手机号码
     *
     * @param string
     * @return true 如果匹配，false 不匹配
     */
    public static boolean isPhoneNum(String string) {
        if (TextUtils.isEmpty(string))
            return false;
        String reg = "^1(3[0-9]|4[57]|5[0-35-9]|7[0-35-9]|8[0-35-9])\\d{8}$";
//        String reg = "^[1][3-8]+\\d{9}";
        return string.matches(reg);
    }

    /**
     * 根据key得到信息
     *
     * @param key
     */
    public static String getStringByKey(Context context, String key) {
        String value = null;
        if (!TextUtils.isEmpty(key)) {
            mShareConfig = context.getSharedPreferences(
                    context.getPackageName(), Context.MODE_PRIVATE);
            if (null != mShareConfig) {
                value = mShareConfig.getString(key, "");
            }
        }
        try {
            return SimpleCrypto.decrypt(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }

    /**
     * 根据key得到信息
     *
     * @param key
     */
    public static int getIntByKey(Context context, String key) {
        int value = 0;
        if (!TextUtils.isEmpty(key)) {
            mShareConfig = context.getSharedPreferences(
                    context.getPackageName(), Context.MODE_PRIVATE);
            if (null != mShareConfig) {
                value = mShareConfig.getInt(key, 0);
            }
        }
        return value;
    }

    /**
     * 根据key得到boolean信息
     *
     * @param key
     */
    public static boolean getBooleanByKey(Context context, String key) {
        boolean value = false;
        if (!TextUtils.isEmpty(key)) {
            mShareConfig = context.getSharedPreferences(
                    context.getPackageName(), Context.MODE_PRIVATE);
            if (null != mShareConfig) {
                value = mShareConfig.getBoolean(key, false);
            }
        }
        return value;
    }

    /**
     * 根据key得到long信息
     *
     * @param key
     */
    public static long getLongByKey(Context context, String key) {
        long value = -1l;
        if (!TextUtils.isEmpty(key)) {
            mShareConfig = context.getSharedPreferences(
                    context.getPackageName(), Context.MODE_PRIVATE);
            if (null != mShareConfig) {
                value = mShareConfig.getLong(key, -1l);
            }
        }
        return value;
    }

    /**
     * 保存boolean值
     *
     * @param key
     * @param value
     */
    public static boolean saveBooleanByKey(Context context, String key,
                                           boolean value) {
        if (!TextUtils.isEmpty(key)) {
            mShareConfig = context.getSharedPreferences(
                    context.getPackageName(), Context.MODE_PRIVATE);
            if (null != mShareConfig) {
                final SharedPreferences.Editor edit = mShareConfig.edit();
                edit.putBoolean(key, value);
                edit.commit();
            }
        }
        return value;
    }

    /**
     * 保存String值
     *
     * @param key
     * @param value
     */
    public static void saveStringByKey(Context context, String key, String value) {
        if (!TextUtils.isEmpty(key) && context != null) {
            mShareConfig = context.getSharedPreferences(
                    context.getPackageName(), Context.MODE_PRIVATE);
            if (null != mShareConfig) {

                final SharedPreferences.Editor edit = mShareConfig.edit();
                try {
                    if (!TextUtils.isEmpty(value))
                        value = SimpleCrypto.encrypt(key, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                edit.putString(key, value);
                edit.commit();
            }
        }
    }

    /**
     * 保存Int值
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveIntByKey(Context context, String key, int value) {
        if (!TextUtils.isEmpty(key) && null != context) {
            mShareConfig = context.getSharedPreferences(
                    context.getPackageName(), Context.MODE_PRIVATE);
            if (null != mShareConfig) {
                final SharedPreferences.Editor edit = mShareConfig.edit();
                edit.putInt(key, value);
                edit.commit();
            }
        }
    }

    /**
     * 保存long值
     *
     * @param context
     * @param key
     * @param t
     */
    public static void saveLongByKey(Context context, String key, long t) {
        if (!TextUtils.isEmpty(key)) {
            mShareConfig = context.getSharedPreferences(
                    context.getPackageName(), Context.MODE_PRIVATE);
            if (null != mShareConfig) {
                final SharedPreferences.Editor edit = mShareConfig.edit();
                edit.putLong(key, t);
                edit.commit();
            }
        }
    }
    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmptyType(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }
}
