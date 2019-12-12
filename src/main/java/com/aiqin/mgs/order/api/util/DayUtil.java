package com.aiqin.mgs.order.api.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public  class DayUtil {
    public static String getDayStr(int days){
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.add(Calendar.DATE,days);
        Date date = ca.getTime();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(date);
    }

    public static String getYearStr(int year){
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.add(Calendar.YEAR,year);
        Date date = ca.getTime();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy");
        return sf.format(date);
    }

    public static String getYearMonthStr(){
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.get((Calendar.MONTH) + 1);
        Date date = ca.getTime();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
        return sf.format(date);
    }

    public static String getMonthStr(){
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.get((Calendar.MONTH) + 1);
        Date date = ca.getTime();
        SimpleDateFormat sf = new SimpleDateFormat("MM");
        return sf.format(date);
    }

    // 获取10年前
    public static String getYearStr(){
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.add(Calendar.YEAR,-10);
        Date date = ca.getTime();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(date);
    }

    public static String getDaySecondStr(int days){
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.add(Calendar.DATE,days);
        Date date = ca.getTime();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(date);
    }
}
