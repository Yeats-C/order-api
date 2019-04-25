/*****************************************************************

 * 模块名称：时间类
 * 开发人员: 黄祉壹
 * 开发时间: 2018-11-15

 * ****************************************************************************/
package com.aiqin.mgs.order.api.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.joda.time.DateTime;

public class DateUtil {

    private final static SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 获取当前系统日期 yyyyMMddHHmmss
     */
    public static String sysDate() {
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String sysDate = sdf2.format(date);
        return sysDate;
    }
    
	/**
	 * 获取当前系统日期 yyyy-MM-dd
	 */
	public static String sysDateYYYYMMDD() {
    	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String sysDate = sdf2.format(date);
		return sysDate;
	}


    /**
     * 获取当前月份 yyyy-MM
     */
    public static String sysDateyyyyMM() {
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        String sysDate = sdf2.format(date);
        return sysDate;
    }


    /**
     * 格式化日期 date to String
     *
     * @return String
     */
    public static String formatDate(Date date) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf1.format(date);
        return strDate;
    }

    /**
     * 格式化日期 date to String
     *
     * @return String
     */
    public static String formatDateLong(Date date) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf1.format(date);
        return strDate;
    }

    /**
     * 格式化日期 String to date
     *
     * @return Date
     */
    public static Date formatDate(String strDate) {
        Date date = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf1.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 获取当前系统日期
     *
     * @return Date
     */
    public static Date getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String sysDate = sdf1.format(date);
        return formatDate(sysDate);
    }

    /**
     * 获取月初日期
     */
    public static String getFirstMonth(int i) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, i); //0:当月  1:下月
        cale.set(Calendar.DAY_OF_MONTH, 1);  //月初
        String firstday = sdf1.format(cale.getTime());
        return firstday;
    }

    /**
     * 获取月末日期
     */
    public static String getLastMonth(int i) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, i); //1:当月  2:下月
        cale.set(Calendar.DAY_OF_MONTH, 0);  //月末
        String firstday = sdf1.format(cale.getTime());
        return firstday;
    }


    /**
     * 获取指定时间所在月的第一天（一天中最小时间）和最后一天（一天中最大时间）
     *
     * @param date return date  "yyyy-mm-dd 00:00:00"
     */
    public static Date getFristOfMonthDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.DAY_OF_MONTH, 1);
        Date firstDay = cal.getTime();

        //考虑到使用判断获取数据时存在部分数据不显示问题，逐将日期格式化处理
        Date firstDayNew = firstDay;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        try {
            firstDayNew = sdf.parse(sdf.format(firstDay));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return firstDayNew;
    }


    /**
     * 获取指定时间所在月的第一天（一天中最小时间）和最后一天（一天中最大时间）
     *
     * @param date return date  "yyyy-mm-dd 59:59:59"
     */
    public static Date getLashOfMonthDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.DAY_OF_MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date lastDay = cal.getTime();

        //考虑到使用判断获取数据时存在部分数据不显示问题，逐将日期格式化处理
        Date lastDayNew = lastDay;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        try {
            cal.setTime(sdf.parse(sdf.format(lastDay)));
            //用日期获取数据时会由1秒误差，若要求比较严格，建议不减1，获取下一天的开始时间
            cal.setTimeInMillis(cal.getTimeInMillis() + 24 * 60 * 60 * 1000 - 1);
            lastDayNew = cal.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lastDayNew;
    }

    /**
     * 获取多少月后的日期  YYYY-MM
     */
    public static String afterMonth(int i) {
        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM");
        Date date = getCurrentDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, i);
        date = cal.getTime();
        String strDate = sdf4.format(date);
        return strDate;
    }

    /*****
     * 得到多少天之后的数 yyyy-MM-dd
     * @param i
     * @return
     */
    public static String NextDate(int i) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, i);
        String str = sdf1.format(calendar.getTime());
        return str;
    }

    /**
     * 得到多少天之后的数 yyyy-MM-dd HH:mm:ss
     */
    public static Date afterThirdMonth(int i) {
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = getCurrentDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, i);
        date = cal.getTime();
        String stringDate = sdf3.format(date);
        return formatDate(stringDate);
    }


    public static Date geLastWeekMonday(Date date) {

        Calendar cal = Calendar.getInstance();

        cal.setTime(getThisWeekMonday(date));

        cal.add(Calendar.DATE, -7);

        return cal.getTime();

    }


    public static Date getThisWeekMonday(Date date) {

        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        // 获得当前日期是一个星期的第几天

        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);

        if (1 == dayWeek) {

            cal.add(Calendar.DAY_OF_MONTH, -1);

        }

        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一

        cal.setFirstDayOfWeek(Calendar.MONDAY);

        // 获得当前日期是一个星期的第几天

        int day = cal.get(Calendar.DAY_OF_WEEK);

        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值

        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);

        return cal.getTime();

    }


    /**
     * 获取下个星期的第一天<br>
     */
    public static Date getNextWeekMonday(Date date) {

        Calendar cal = Calendar.getInstance();

        cal.setTime(getThisWeekMonday(date));

        cal.add(Calendar.DATE, 7);

        return cal.getTime();

    }


    /**
     * 获取所在天的开始时间 参数:date<br>
     */
    public static Date getDayBegin(Date date) {
        return new DateTime(date).millisOfDay().withMinimumValue().toDate();
    }

    /**
     * 获取所在天的结束时间 参数:date<br>
     */
    public static Date getDayEnd(Date date) {
        return new DateTime(date).millisOfDay().withMaximumValue().toDate();
    }


    /**
     * 获取所在天的开始时间 参数:String<br>
     */
    public static Date getDayBegin(String date) {
        return new DateTime(date).millisOfDay().withMinimumValue().toDate();
    }

    /**
     * 获取所在天的结束时间  参数:String<br>
     */
    public static Date getDayEnd(String date) {
        return new DateTime(date).millisOfDay().withMaximumValue().toDate();
    }


    //获取昨天的开始时间
    public static Date getBeginYesterday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return new DateTime(cal.getTime()).millisOfDay().withMinimumValue().toDate();
    }

    //获取昨天的结束时间
    public static Date getEndYesterDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return new DateTime(cal.getTime()).millisOfDay().withMaximumValue().toDate();
    }

    /**
     * 当前年的开始时间
     *
     * @return
     */
    public static Date getCurrentYearBeginTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.DATE, 1);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获取日期前多少天的时间
     *
     * @param date
     * @param day
     * @return
     */
    public static Date getBeforeDate(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -day);
        return new DateTime(cal.getTime()).millisOfDay().withMinimumValue().toDate();
    }

    /**
     * 过期时间/秒
     *
     * @param randomSeconds
     * @return
     */
    public static Long getExpireEndRandomSeconds(Long randomSeconds) {
        Long diffSeconds = (getDayEnd(getCurrentDate()).getTime() - System.currentTimeMillis()) / 1000;
        return diffSeconds - (long)(Math.random()*randomSeconds);
    }
    
    /**
     * 获取所在天的开始时间 参数:String<br>
     */
    public static String getTimeBegin(String date) {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		date = sdf.format(new DateTime(date).millisOfDay().withMinimumValue().toDate());
    	
        return date;
    }
    
    /**
     * 获取所在天的结束时间  参数:String<br>
     */
    public static String getTimeEnd(String date) {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	date = sdf.format(new DateTime(date).millisOfDay().withMaximumValue().toDate());
        return date;
    }

    public static void main(String[] args) {
//		Date date=formatDate("2019-02-00");
//		System.out.println(getFristOfMonthDay(formatDate("2019-02")));

        System.out.println(getTimeBegin(sysDateYYYYMMDD()));
    }

}

