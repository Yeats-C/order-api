package com.aiqin.mgs.order.api.domain.constant;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ErpOrderCodeSequence {

    public static String currentDay = "";
    public static Long num;

//    public static String getOrderCode() {
//        String curDayStr = getCurDayStr();
//        if (!currentDay.equals(curDayStr)) {
//            currentDay = curDayStr;
//            num = 0L;
//        }
//        return null;
//    }
//
//    private static String getCurDayStr() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        return sdf.format(new Date());
//    }
}
