/*****************************************************************

* 模块名称：公共方法类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-15

* ****************************************************************************/
package com.aiqin.mgs.order.api.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class OrderPublic {
	

	 /**
	  * 
	  * @return 5位数的随机数
	  */
 public static String randomNumberF() {
	 
	 String randomNumber = ""; //随机数
	 
	 final int num = 5; //5位
	  
	  int Random[] = new int[num];
	  for(int i = 0 ; i < num ; i++)
	  {
	  // int ran=-1;
	   while(true)
	   {
	    int ran = (int)(num*Math.random());
	    for(int j = 0 ; j < i ; j++)
	    {
	     if(Random[j] == ran) 
	     {
	      ran = -1;
	      break;
	     } 
	    }
	    if(ran != -1) 
	    {
	     Random[i] = ran;
	     break;
	    }
	    
	   }
	   
	  }
	  
	  for(int i = 0 ; i < num ; i ++)
	  {
		  randomNumber +=Random[i];
	  }
	  return randomNumber;
 }
 
 
 /**
  * 
  * @return 2位数的随机数
  */
 public static String randomNumberT() {
	 
	 String randomNumber = ""; //随机数
	 
	 final int num = 2; //2位
	  
	  int Random[] = new int[num];
	  for(int i = 0 ; i < num ; i++)
	  {
	  // int ran=-1;
	   while(true)
	   {
	    int ran = (int)(num*Math.random());
	    for(int j = 0 ; j < i ; j++)
	    {
	     if(Random[j] == ran) 
	     {
	      ran = -1;
	      break;
	     } 
	    }
	    if(ran != -1) 
	    {
	     Random[i] = ran;
	     break;
	    }
	    
	   }
	   
	  }
	  
	  for(int i = 0 ; i < num ; i ++)
	  {
		  randomNumber +=Random[i];
	  }
	  return randomNumber;
 }
 
 static SimpleDateFormat sdf2 = new SimpleDateFormat("yyMMddHHmmss");
 static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd"); 
 static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	/**
	 * 获取当前系统日期 yyMMddHHmmss
	 */

	public static String currentDate() {
		Date date = new Date();
		String sysDate = sdf2.format(date);
		return sysDate;
	}
	
	
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
	 * 获取当前系统日期 yyyy
	 */
	public static String sysDateyyyy() {
    	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
		Date date = new Date();
		String sysDate = sdf2.format(date);
		return sysDate;
	}
	
	
	/**
	 * 获取当前系统日期 MM
	 */
	public static String sysDatemm() {
    	SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
		Date date = new Date();
		String sysDate = sdf2.format(date);
		return sysDate;
	}
	
	
	/**
	 * 格式化日期
	 * 
	 * @return String
	 */
	public static String formatDate(Date date) {
		String strDate = sdf1.format(date);
		return strDate;
	}
	
	/**
	 * 格式化日期
	 * 
	 * @return Date
	 */
	public static Date formatDate(String strDate) {
		Date date = null;
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
		String sysDate = sdf1.format(date);
		return formatDate(sysDate);
	}
	
	/**
	 * 获取多少天后的日期
	 */
	public static Date afterThirdMonth(int i) {
		Date date = getCurrentDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, i);
		date = cal.getTime();
		String stringDate = sdf3.format(date);
		return formatDate(stringDate);
	}
	
	public static String getUUID(){ 
	
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "").toUpperCase(); 
	
	return uuid; 
	}

	
	public static void main(String[] args) {

		System.out.println(sysDatemm());
	}
	
}

