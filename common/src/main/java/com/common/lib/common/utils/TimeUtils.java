package com.common.lib.common.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by FPM on 2018/3/16.
 */

public class TimeUtils {

    /**
     * 将毫秒转化成固定格式的时间
     * 时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param millisecond
     * @return
     */
    public static String getDateTimeSecondFromMillisecond(Long millisecond){

        if (millisecond==null||millisecond==0){
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }
    public static String getDateTimeSecondFromMillisecond(String mString){

        if (TextUtils.isEmpty(mString)){
            return "";
        }
        Long millisecond= Long.parseLong(mString);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    /**
     * 将毫秒转化成固定格式的时间
     * 时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param millisecond
     * @return
     */
    public static String getDateTimeMinFromMillisecond(Long millisecond){

        if (millisecond==null||millisecond==0){
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }
    public static String getDateTimeMinFromMillisecond(String mString){


        if (TextUtils.isEmpty(mString)){
            return "";
        }
        Long millisecond= Long.parseLong(mString);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    /**
     * 将毫秒转化成固定格式的时间
     * 时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param millisecond
     * @return
     */
    public static String getDateDayFromMillisecond(Long millisecond){

        if (millisecond==null||millisecond==0){
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }
    public static String getDateDayFromMillisecond(String millisecond){

        if (TextUtils.isEmpty(millisecond)||millisecond.equals("0")){
            return "";
        }

        long parseLong = Long.parseLong(millisecond);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(parseLong);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    public static String getDateMothFromMillisecond(Long millisecond){

        if (millisecond==null||millisecond==0){
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }
    public static String getDateMothFromMillisecond(String millisecond){

        if (TextUtils.isEmpty(millisecond)||millisecond.equals("0")){
            return "";
        }

        long parseLong = Long.parseLong(millisecond);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date(parseLong);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    /**
     * 将毫秒转化成固定格式的时间
     * 时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param millisecond
     * @return
     */
    public static String getChineseDayFromMillisecond(Long millisecond){

        if (millisecond==null||millisecond==0){
            return "";
        }

        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
        Date date = new Date(millisecond);
        df.applyPattern("yyyy'年'M'月'd'日'");

        return df.format(date);
    }
    public static String getChinaDayFromMillisecond(String millisecond){

        if (TextUtils.isEmpty(millisecond)||millisecond.equals("0")){
            return "";
        }
        long parseLong = Long.parseLong(millisecond);
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
        Date date = new Date(parseLong);
        df.applyPattern("yyyy'年'M'月'd'日'");

        return df.format(date);
    }
}
