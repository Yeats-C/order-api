/*****************************************************************

* 模块名称：时间类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-15

* ****************************************************************************/
package com.aiqin.mgs.order.api.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.joda.time.DateTime;

import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleQuery;
import com.aiqin.mgs.order.api.domain.OrderDetailQuery;
import com.aiqin.mgs.order.api.domain.OrderLog;
import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.service.OrderService;

public class DateUtil {
	
// static SimpleDateFormat sdf2 = new SimpleDateFormat("yyMMddHHmmss");
// static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd"); 
// static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
// static SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM"); 
	

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
	public static String getMonth(int i) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cale = Calendar.getInstance();
		cale.add(Calendar.MONTH, i); //0:当月  1:下月
		cale.set(Calendar.DAY_OF_MONTH, 1);  //第几天
		String firstday = sdf1.format(cale.getTime());
		return firstday;
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
	    Date date=new Date();  
	    Calendar calendar = Calendar.getInstance();  
	    calendar.setTime(date);  
	    calendar.add(Calendar.DAY_OF_MONTH,i);  
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

 

	public static void main(String[] args) {
	}
	
}

