/*****************************************************************

* 模块名称：公共方法类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-15

* ****************************************************************************/
package com.aiqin.mgs.order.api.util;

import java.text.SimpleDateFormat;
import java.util.Date;

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
 
 
 
	/**
	 * 获取当前系统日期 yyMMddHHmmss
	 */
    static SimpleDateFormat sdf2 = new SimpleDateFormat("yyMMddHHmmss");
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
	
//	/**
//	 * TEST
//	 * @param args
//	 */
//	public static void main(String[] args) {
//
//		System.out.println(currentDate());
//	}
	
}

