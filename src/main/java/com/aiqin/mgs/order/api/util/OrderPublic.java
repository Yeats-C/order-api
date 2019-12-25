/*****************************************************************

* 模块名称：公共方法类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-15

* ****************************************************************************/
package com.aiqin.mgs.order.api.util;

import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleQuery;
import com.aiqin.mgs.order.api.domain.OrderDetailQuery;
import com.aiqin.mgs.order.api.domain.OrderLog;
import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.domain.constant.Global;

import java.util.List;
import java.util.UUID;

public class OrderPublic {
	
	
	/**
	 * 生成4位随机数
	 * @return
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
	  * 生成8位随机数
	  * @return
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
	  * 生成2位随机数
	  * @return
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
     * 生成32位编码
     * @return
     */
	public static String getUUID(){ 
	
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "").toUpperCase(); 
	
	return uuid; 
	}
	
	/**
	 * 订单日志参数
	 * @param orderId
	 * @param status
	 * @param statusCode
	 * @param detailStatus
	 * @param createBy
	 * @return
	 * @throws Exception
	 */
	public static OrderLog addOrderLog(String orderId,String status,String statusCode,String detailStatus,
			String createBy) throws Exception{ 
		OrderPublic orderPublic = new OrderPublic();
		OrderLog orderLog = orderPublic.OrderLog(orderId,status,statusCode,detailStatus,createBy);
		
		return orderLog;
	}
	

	/**
	 * 封装订单日志信息
	 * @param orderId
	 * @param status
	 * @param statusCode
	 * @param detailStatus
	 * @param createBy
	 * @return
	 * @throws Exception
	 */
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
	

	/**
	 * 订单  - 封装查询条件. 
	 * @param query
	 * @return
	 */
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
			
			//会员集合
			if(query.getMemberidList() !=null && query.getMemberidList().size()>0) {
				
			}else {
				query.setMemberidList(null);
			}
			
			//订单集合
			if(query.getOrderIdList() !=null && query.getOrderIdList().size()>0) {
				
			}else {
				query.setOrderIdList(null);
			}
			
			//排除订单集合
			if(query.getNoExistOrderCodeList() !=null && query.getNoExistOrderCodeList().size()>0) {
				
			}else {
				query.setNoExistOrderCodeList(null);
			}
			
			//日期转换  YYYY-MM-DD 转换 2000-00-00 00:00:00 /2000-00-00 59:59:59
			if(query.getBeginDate() !=null && !query.getBeginDate().equals("")) {
				query.setBeginTime(DateUtil.getDayBegin(query.getBeginDate()));
			}
			if(query.getEndDate() !=null && !query.getEndDate().equals("")) {
				query.setEndTime(DateUtil.getDayEnd(query.getEndDate()));
			}
			if(query.getBeginTime() !=null) {
				query.setBeginTime(DateUtil.getDayBegin(query.getBeginTime()));
			}
			if(query.getEndTime() !=null) {
				query.setEndTime(DateUtil.getDayEnd(query.getEndTime()));
			}
		}
			return query;
		}
	
	
	/**
	 * 订单明细  - 封装查询条件.
	 * @param query
	 * @return
	 */
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
		  
		  //订单集合
		  if(query.getOrderIdList() !=null && query.getOrderIdList().size()>0) {
				
		  }else {
			  query.setOrderIdList(null);
		  }
		  
		  //日期转换  YYYY-MM-DD 转换 2000-00-00 00:00:00 /2000-00-00 59:59:59
		  if(query.getBeginDate() !=null) {
			query.setBeginDate(DateUtil.getDayBegin(query.getBeginDate()));
		  }
		  if(query.getEndDate() !=null) {
			query.setEndDate(DateUtil.getDayEnd(query.getEndDate()));
		  }
		}
		return query;		
	}	
	
	
	/**
	 * 订单售后  - 封装查询条件.
	 * @param query
	 * @return
	 */
	public static OrderAfterSaleQuery getOrderAfterSaleQuery(OrderAfterSaleQuery query){
		
		if(query !=null) {
			//来源类型  2=null
			if(query.getOriginTypeList() !=null && query.getOriginTypeList().size()>0) {
				for(Integer originType : query.getOriginTypeList()) {
				    if(originType.equals(Global.ORIGIN_TYPE_2)) {
					  query.setOriginTypeList(null);
					}
				}
			  }else {
				  query.setOriginTypeList(null);
			  }
			
			//日期转换  YYYY-MM-DD 转换 2000-00-00 00:00:00 /2000-00-00 59:59:59
			if(query.getBeginDate() !=null && !query.getBeginDate().equals("")) {
				query.setBeginTime(DateUtil.getDayBegin(query.getBeginDate()));
			}
			if(query.getEndDate() !=null && !query.getEndDate().equals("")) {
				query.setEndTime(DateUtil.getDayEnd(query.getEndDate()));
			}
			if(query.getBeginTime() !=null) {
				query.setBeginTime(DateUtil.getDayBegin(query.getBeginTime()));
			}
			if(query.getEndTime() !=null) {
				query.setEndTime(DateUtil.getDayEnd(query.getEndTime()));
			}
			
		}
		return query;		
	}
	
	
	/**
	 * list集合转 分页对象
	 * @param list
	 * @return
	 */
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

	
	
	/**
	 * 日志所使用的订单状态
	 * @param statusType
	 * @param status
	 * @return
	 */
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

	public static void main(String[] args) {
		System.out.println(DateUtil.getDayBegin(DateUtil.getCurrentDate()));
	}
	
}

