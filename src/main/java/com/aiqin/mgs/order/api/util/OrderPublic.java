/*****************************************************************

* 模块名称：公共方法类
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

import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleQuery;
import com.aiqin.mgs.order.api.domain.OrderDetailQuery;
import com.aiqin.mgs.order.api.domain.OrderLog;
import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.service.OrderService;

public class OrderPublic {
	
	 /**
	  * 
	  * @return 4位数的随机数
	  */
 public static String randomNumberF() {
	 
	 String randomNumber = ""; //随机数
	 
	 final int num = 4; //4位
	  
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
  * @return 8位数的随机数
  */
public static String randomNumberE() {
 
 String randomNumber = ""; //随机数
 
 final int num = 8; //8位
  
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
 static SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM"); 
	
    /**
	 * 获取当前系统时间戳 yyMMddHHmmss
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
	 * 获取当前月份 yyyy-MM
	 */
	public static String sysDateyyyyMM() {
    	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
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
	 * 获取月初日期
	 */
	public static String getMonth(int i) {
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
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
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
		Date date = getCurrentDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, i);
		date = cal.getTime();
		String stringDate = sdf3.format(date);
		return formatDate(stringDate);
		}
	
	
    //生成32位编码
	public static String getUUID(){ 
	
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "").toUpperCase(); 
	
	return uuid; 
	}

	
	//调用订单日志
	public static OrderLog addOrderLog(String orderId,String status,String statusCode,String detailStatus,
			String createBy) throws Exception{ 
		OrderPublic orderPublic = new OrderPublic();
		OrderLog orderLog = orderPublic.OrderLog(orderId,status,statusCode,detailStatus,createBy);
		
		return orderLog;
	}
	

	public OrderLog OrderLog(String orderId,String status,String statusCode,String detailStatus,
			String createBy) throws Exception{
		
		OrderLog logInfo = new OrderLog();
		logInfo.setOrderId(orderId);
		logInfo.setLogId(getUUID());
		logInfo.setStatus(status);
		logInfo.setStatusCode(statusCode);
		logInfo.setStatusContent(detailStatus);
		logInfo.setCreateBy(createBy);

		return logInfo;
	}
	

	//订单  - 封装查询条件. 
	public static OrderQuery getOrderQuery(OrderQuery query){
		
		//收货方式
		if(query !=null && query.getReceiveType() !=null && query.getReceiveType().equals(Global.RECEIVE_TYPE_2)) {
			query.setReceiveType(null);
		}
		
		if(query !=null ) {
			
			//来源类型
			if(query.getOriginTypeList() !=null && query.getOriginTypeList().size()>0) {
			  for(Integer originType : query.getOriginTypeList()) {
				if(originType.equals(Global.ORIGIN_TYPE_2)) {
					query.setOriginTypeList(null);
				}
			  }
			}else {
				query.setOriginTypeList(null);
			}
			
			//查询条件:订单状态集合
			if(query.getOrderStatusList() !=null && query.getOrderStatusList().size()>0) {
			
			}else {
				query.setOrderStatusList(null);
			}	
		}
			return query;
		}
	
	
	//订单明细  - 封装查询条件.
	public static OrderDetailQuery getOrderDetailQuery(OrderDetailQuery query){
			
		
		if(query !=null) {
			
		 //来源类型		
		 if(query.getOriginTypeList() !=null && query.getOriginTypeList().size()>0) {
			for(Integer originType : query.getOriginTypeList()) {
			    if(originType.equals(Global.ORIGIN_TYPE_2)) {
				  query.setOriginTypeList(null);
				}
			}
		  }else {
			  query.setOriginTypeList(null);
		  }
		 
		  //查询条件:订单状态集合
		  if(query.getActivityIdList() !=null && query.getActivityIdList().size()>0) {
			
		  }else {
			 query.setActivityIdList(null);
		  }
		  
		  //查询条件:会员IDlist
		  if(query.getMemberidList() !=null && query.getMemberidList().size()>0) {
			
		  }else {
			 query.setMemberidList(null);
		  }
		  
		  //sku集合
		  if(query.getSukList() !=null && query.getSukList().size()>0) {
			
		  }else {
			 query.setSukList(null);
		  }
		}
		return query;		
	}	
	
	//订单售后  - 封装查询条件.
	public static OrderAfterSaleQuery getOrderAfterSaleQuery(OrderAfterSaleQuery query){
		
		//来源类型
		if(query !=null) {
			if(query.getOriginTypeList() !=null && query.getOriginTypeList().size()>0) {
				for(Integer originType : query.getOriginTypeList()) {
				    if(originType.equals(Global.ORIGIN_TYPE_2)) {
					  query.setOriginTypeList(null);
					}
				}
			  }else {
				  query.setOriginTypeList(null);
			  }
		}
		return query;		
	}	
	
	//list集合转 分页对象
	public static PageResData getData(List list){
		
		PageResData data =  new PageResData();
		int total = 0;
		if(list !=null && list.size()>0) {
			total = list.size();
			data.setTotalCount(total);
			data.setDataList(list);
		}
		return data;
	}

	
	public static String getStatus(String statusType,Integer status) {
		String content = "";
		
		//订单状态
		if(statusType.equals(Global.STATUS_0)) {
			if(status == 0) {
				content = Global.STATUS_CONTENT_10;
			}else if(status == 1) {
				content = Global.STATUS_CONTENT_1;
			}else if(status == 2) {
				content = Global.STATUS_CONTENT_2;
			}else if(status == 3) {
				content = Global.STATUS_CONTENT_3;
			}else if(status == 4) {
				content = Global.STATUS_CONTENT_4;
			}else if(status == 5) {
				content = Global.STATUS_CONTENT_5;
			}else if(status == 6) {
				content = Global.STATUS_CONTENT_6;
			}else {
				content = Global.STATUS_CONTENT_10;
			}
		}
		
		//退货状态
		if(statusType.equals(Global.STATUS_2)) {
			if(status == 0) {
				content = Global.STATUS_CONTENT_100;
			}else if(status == 1) {
				content = Global.STATUS_CONTENT_11;
			}else if(status == 2) {
				content = Global.STATUS_CONTENT_22;
			}else if(status == 3) {
				content = Global.STATUS_CONTENT_33;
			}else if(status == 4) {
				content = Global.STATUS_CONTENT_44;
			}else if(status == 5) {
				content = Global.STATUS_CONTENT_55;
			}else if(status == 6) {
				content = Global.STATUS_CONTENT_66;
			}else {
				content = Global.STATUS_CONTENT_100;
			}
		}
		
		//支付状态
		if(statusType.equals(Global.STATUS_1)) {
			if(status == 0) {
				content = Global.STATUS_CONTENT_1000;
			}else if(status == 1) {
				content = Global.STATUS_CONTENT_111;
			}else {
				content = Global.STATUS_CONTENT_1000;
			}
		}		
		return content;
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

 

	public static Date getNextWeekMonday(Date date) {

		Calendar cal = Calendar.getInstance();

		cal.setTime(getThisWeekMonday(date));

		cal.add(Calendar.DATE, 7);

		return cal.getTime();

	}

 

	public static void main(String[] args) {
	}
	
}

