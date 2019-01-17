/*****************************************************************

* 模块名称：订单后台-实现层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05
* 
* 2019-01-08 调整接口addordta;返回值由order_id 调整为order_code
*            调整接口addOrderList;返回值由order_id 调整为order_code
*            调整接口addpamo;orderId 通过后台查询出来

* ****************************************************************************/
package com.aiqin.mgs.order.api.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aiqin.ground.util.exception.GroundRuntimeException;
import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.CartDao;
import com.aiqin.mgs.order.api.dao.OrderCouponDao;
import com.aiqin.mgs.order.api.dao.OrderDao;
import com.aiqin.mgs.order.api.dao.OrderDetailDao;
import com.aiqin.mgs.order.api.dao.OrderLogDao;
import com.aiqin.mgs.order.api.dao.OrderPayDao;
import com.aiqin.mgs.order.api.dao.OrderReceivingDao;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailQuery;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.OrderListInfo;
import com.aiqin.mgs.order.api.domain.OrderLog;
import com.aiqin.mgs.order.api.domain.OrderPayInfo;
import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.domain.OrderRelationCouponInfo;
import com.aiqin.mgs.order.api.domain.OrderodrInfo;
import com.aiqin.mgs.order.api.domain.ProductCycle;
import com.aiqin.mgs.order.api.domain.SettlementInfo;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.request.DevelRequest;
import com.aiqin.mgs.order.api.domain.request.OrderAndSoOnRequest;
import com.aiqin.mgs.order.api.domain.request.OrderDetailRequest;
import com.aiqin.mgs.order.api.domain.request.ReorerRequest;
import com.aiqin.mgs.order.api.domain.response.OrderOverviewMonthResponse;
import com.aiqin.mgs.order.api.domain.response.OrderResponse;
import com.aiqin.mgs.order.api.domain.response.OrderbyReceiptSumResponse;
import com.aiqin.mgs.order.api.domain.response.WscSaleResponse;
import com.aiqin.mgs.order.api.domain.response.WscWorkViewResponse;
import com.aiqin.mgs.order.api.domain.response.DistributorMonthResponse;
import com.aiqin.mgs.order.api.domain.response.LastBuyResponse;
import com.aiqin.mgs.order.api.domain.response.MevBuyResponse;
import com.aiqin.mgs.order.api.domain.response.OradskuResponse;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.OrderDetailService;
import com.aiqin.mgs.order.api.service.OrderLogService;
import com.aiqin.mgs.order.api.service.OrderService;
import com.aiqin.mgs.order.api.service.SettlementService;
import com.aiqin.mgs.order.api.util.OrderPublic;
import com.fasterxml.jackson.core.type.TypeReference;

@SuppressWarnings("all")
@Service
public class OrderServiceImpl implements OrderService{

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	
	@Resource
    private OrderDao orderDao;
	
	@Resource
    private OrderLogDao orderLogDao;
	
	@Resource
    private OrderCouponDao orderCouponDao;
	
	@Resource
    private OrderPayDao orderPayDao;
	
	@Resource
    private OrderDetailService orderDetailService;
	
	@Resource
    private SettlementService settlementService;
	
	@Resource
    private OrderLogService orderLogService;
	
	@Resource
    private CartService cartService;
	
	@Resource
    private OrderDetailDao orderDetailDao;
	

	//模糊查询订单列表
	@Override
	public HttpResponse selectOrder(OrderQuery orderQuery) {
		
			LOGGER.info("模糊查询订单列表", orderQuery);

			try {
				
				List<OrderInfo> OrderInfolist = orderDao.selectOrder(OrderPublic.getOrderQuery(orderQuery));
				if(OrderInfolist !=null && OrderInfolist.size()>0) {
					for(int i=0;i<OrderInfolist.size();i++) {
						OrderInfo info = new OrderInfo();
						info = OrderInfolist.get(i);
						
						//订单明细数据
						OrderDetailQuery orderDetailQuery = new OrderDetailQuery();
						orderDetailQuery.setOrderId(info.getOrderId());
					    List<OrderDetailInfo> detailList = orderDetailDao.selectDetailById(orderDetailQuery);
					    info.setOrderdetailList(detailList);
					    OrderInfolist.set(i,info);
					}
				}
				
				//计算总数据量
				Integer totalCount = 0;
				Integer icount =null;
//				orderQuery.setIcount(icount);
				totalCount = orderDao.selectOrderCount(OrderPublic.getOrderQuery(orderQuery));
				
//				return HttpResponse.success(OrderPublic.getData(OrderInfolist));
				return HttpResponse.success(new PageResData(totalCount,OrderInfolist));
				
			} catch (Exception e) {
				LOGGER.info("模糊查询订单列表報錯", e);
				return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
			}
			
	}


	//添加新的订单主数据
	@Override
	public OrderInfo addOrderInfo(@Valid OrderInfo orderInfo) throws Exception {
		
		String orderId = "";
		String orderCode = "";
		String receiveCode = "";
		
		//生成订单ID
		orderId = OrderPublic.getUUID();
		orderInfo.setOrderId(orderId);
		
		//生成订单号
		String logo = "";
		if(orderInfo.getOriginType() == Global.ORIGIN_TYPE_0) {
			logo = Global.ORIGIN_COME_3;
		}
		if(orderInfo.getOriginType() == Global.ORIGIN_TYPE_1) {
			logo = Global.ORIGIN_COME_4;
		}
		if(orderInfo.getOriginType() == Global.ORIGIN_TYPE_3) {
			logo = Global.ORIGIN_COME_5;
		}
		//公司标识
		String companyCode = "";
		if(orderInfo.getCompanyCode() !=null && orderInfo.getCompanyCode().equals(Global.COMPANY_01)) {
			companyCode = Global.COMPANY_01;
		}else {
			companyCode = Global.COMPANY_01;
		}
		//yyMMddHHmmss+订单来源+销售渠道标识+公司标识+4位数的随机数
		orderCode = OrderPublic.currentDate()+logo+String.valueOf(Global.ORDERID_CHANNEL_4)+companyCode+OrderPublic.randomNumberF();
		orderInfo.setOrderCode(orderCode);
		
		//初始化提货码
		if(orderInfo.getOriginType() == Global.ORIGIN_TYPE_1) {
			if(orderInfo.getReceiveType().equals(Global.RECEIVE_TYPE_0)) {
				receiveCode =OrderPublic.randomNumberE();
				orderInfo.setReceiveCode(receiveCode);
			}
		}
		
		//订单主数据
		orderDao.addOrderInfo(orderInfo);
		
		return orderInfo;
	}


	//添加订单日志
	@Override
	@Transactional
	public HttpResponse addOrderLog(@Valid OrderLog logInfo) {
		try {
			
			//生成日志ID
			orderLogDao.addOrderLog(logInfo);
			return HttpResponse.success();
		
		} catch (Exception e) {
			LOGGER.info("添加订单日志報錯", e);
			return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
		}
	}


	//添加新的订单优惠券关系表数据
	@Override
	public void addOrderCoupon(@Valid List<OrderRelationCouponInfo> orderCouponList, @Valid String orderId) throws Exception {
            
		   if(orderCouponList !=null && orderCouponList.size()>0) {
			   for(OrderRelationCouponInfo info : orderCouponList) {
	        	    info.setOrderId(orderId);
	        	    info.setOrdercouponId(OrderPublic.getUUID());
	                //保存优惠券信息
					orderCouponDao.addOrderCoupon(info);
	            } 
		   }
	}

//	//接口-分销机构维度-总销售额 返回INT
//	@Override
//	public HttpResponse selectOrderAmt(String distributorId, String originType) {
//		
//		try {
//			Integer  total_price = orderDao.selectOrderAmt(distributorId,originType);
//			
//			return HttpResponse.success(total_price);
//		} catch (Exception e) {
//
//			LOGGER.info("接口-分销机构维度-总销售额 返回INT報錯", e);
//			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
//		}
//		
//		
//	}


	//接口-分销机构+当月维度-当月销售额、当月实收、当月支付订单数
	@Override
	public HttpResponse selectorderbymonth(@Valid String distributorId,List<Integer> originTypeList) {

		//系统日期:年月
		String yearMonth = OrderPublic.sysDateyyyyMM();
		
		//取昨日日期
		String yesterday = OrderPublic.NextDate(-1);
		
		OrderOverviewMonthResponse info = new OrderOverviewMonthResponse();
		
		if(originTypeList !=null && originTypeList.size()>0) {
			for(Integer originType :originTypeList) {
				if(originType.equals(Global.ORIGIN_TYPE_2)) {
					originTypeList =null;
				}
			}
		}else {
			originTypeList =null;
		}
		
		//总销售额：包含退货、不包含取消、未付款的订单
		Integer total_price = 0;
		
		//当月销售额：包含退货、不包含取消、未付款的订单
		Integer monthtotal_price=0;
		
		//昨日销售额：包含退货、不包含取消、未付款的订单
        Integer yesdaytotal_price=0;
        
        //当月支付订单数：包含退货、不包含取消、未付款的订单
      	Integer monthorder_acount=0;
      	
        //昨日支付订单数：包含退货、不包含取消、未付款的订单
        Integer yesdayorder_acount=0;
		
		//当月实收：不包含退货、未付款、取消的订单
		Integer monthreal_amt=0;
		
		//昨日实收：不包含退货、未付款、取消的订单
        Integer yesdayreal_amt;

		
		try {
			System.out.println("总销售额：包含退货、取消的订单==");
			total_price = orderDao.selectOrderAmt(distributorId,originTypeList);
			System.out.println("当月销售额：包含退货、取消的订单==");
			monthtotal_price = orderDao.selectByMonthAllAmt(distributorId,originTypeList,yearMonth);
			System.out.println("昨日销售额：包含退货、取消的订单==");
			yesdaytotal_price = orderDao.selectByYesdayAllAmt(distributorId,originTypeList,yesterday);
			System.out.println("当月支付订单数：包含退货、取消的订单==");
			monthorder_acount = orderDao.selectByMonthAcount(distributorId,originTypeList,yearMonth);
			System.out.println("昨日支付订单数：包含退货、取消的订单==");
			yesdayorder_acount = orderDao.selectByYesdayAcount(distributorId,originTypeList,yesterday);
			System.out.println("当月实收：不包含退货、取消的订单==");
			monthreal_amt = orderDao.selectbByMonthRetailAmt(distributorId,originTypeList,yearMonth);
			System.out.println("昨日实收：不包含退货、取消的订单==");
			yesdayreal_amt = orderDao.selectbByYesdayRetailAmt(distributorId,originTypeList,yesterday);
			
			
			
			if(total_price ==null) {
				total_price =0;
			}
			if(monthtotal_price ==null) {
				monthtotal_price =0;
			}
			if(monthreal_amt ==null) {
				monthreal_amt =0;
			}
			if(monthorder_acount ==null) {
				monthorder_acount =0;
			}
			if(yesdaytotal_price ==null) {
				yesdaytotal_price =0;
			}
			if(yesdayreal_amt ==null) {
				yesdayreal_amt =0;
			}
			if(yesdayorder_acount ==null) {
				yesdayorder_acount =0;
			}
			
			info.setAllIncome(total_price);
			info.setMonthSalesVolume(monthtotal_price); 
			info.setMonthCash(monthreal_amt);
			info.setMonthPaymentOrderNum(monthorder_acount);
			
			info.setYesterdaySalesVolume(yesdaytotal_price);
			info.setYesterdayCash(yesdayreal_amt);
			info.setYesterdayPaymentOrderNum(yesdayorder_acount);
			
			return HttpResponse.success(info);
			
		} catch (Exception e) {
			
			LOGGER.info("接口-分销机构+当月维度-当月销售额、当月实收、当月支付订单数報錯", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
		
		
		
		
	}


	//接口-订单概览-分销机构、小于当前日期9天内的实付金额、订单数量
	@Override
	public HttpResponse selectOrderByNineDate(@Valid String distributorId,List<Integer> originTypeList) {
		
		String  beginDate= OrderPublic.NextDate(-8);
		System.out.println("小于当前日期的第9天日期:"+beginDate);
		try {
			OrderQuery orderQuery = new OrderQuery();
			orderQuery.setDistributorId(distributorId);
			orderQuery.setOriginTypeList(originTypeList);
			orderQuery.setBeginDate(beginDate);
			List<OrderResponse> list = new ArrayList();
			list = orderDao.selectOrderByNineDate(OrderPublic.getOrderQuery(orderQuery));
			return HttpResponse.success(list);
		} catch (Exception e) {
			
			LOGGER.info("接口-订单概览-分销机构、小于当前日期9天内的实付金额、订单数量報錯", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}

		
	}
	
	
	//接口-订单概览-分销机构、小于当前日期9周内的实付金额、订单数量
	@Override
	public HttpResponse selectOrderByNineWeek(@Valid String distributorId,List<Integer> originTypeList) {
		
		try {
			
			//查询条件
			OrderQuery orderQuery = new OrderQuery();
			orderQuery.setDistributorId(distributorId);
			orderQuery.setOriginTypeList(originTypeList);
			
			//返回
			List<OrderResponse> list = new ArrayList();
			OrderResponse info = new OrderResponse();
			for(int i=0;i<9;i++) {
				orderQuery.setAny(i);
				info = orderDao.selectOrderByNineWeek(OrderPublic.getOrderQuery(orderQuery));
				list.add(info);
			}
			return HttpResponse.success(list);
		} catch (Exception e) {
			
			LOGGER.info("接口-订单概览-分销机构、小于当前日期9周内的实付金额、订单数量報錯", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}
	
	//接口-订单概览-分销机构、小于当前日期9个月内的实付金额、订单数量
	@Override
	public HttpResponse selectOrderByNineMonth(@Valid String distributorId,List<Integer> originTypeList) {

        String  beginDate = OrderPublic.afterMonth(-8); //YYYY-MM
        
        OrderQuery orderQuery = new OrderQuery();
		orderQuery.setDistributorId(distributorId);
		orderQuery.setOriginTypeList(originTypeList);
		orderQuery.setBeginDate(beginDate);
		
		try {
			List<OrderResponse> list = orderDao.selectOrderByNineMonth(OrderPublic.getOrderQuery(orderQuery));
			return HttpResponse.success(list);
		} catch (Exception e) {
			
			LOGGER.info("接口-订单概览-分销机构、小于当前日期9个月内的实付金额、订单数量報錯", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}


	//添加新的订单主数据以及其他订单关联数据 事务未生效 暂不处理
	@Override
	@Transactional
	public HttpResponse addOrderList(@Valid OrderAndSoOnRequest orderAndSoOnRequest){
		
	 try {
		OrderInfo orderInfo = orderAndSoOnRequest.getOrderInfo();
		List<OrderDetailInfo> detailList = orderAndSoOnRequest.getDetailList();
		SettlementInfo settlementInfo = orderAndSoOnRequest.getSettlementInfo();
		List<OrderPayInfo> orderPayList = orderAndSoOnRequest.getOrderPayList();
		List<OrderRelationCouponInfo> orderCouponList = orderAndSoOnRequest.getOrderCouponList();
		
		//新增订单主数据
		String orderId = "";
		String orderCode = "";
		if(orderInfo !=null ) {
			orderInfo = addOrderInfo(orderInfo);
			orderId = orderInfo.getOrderId();
			orderCode = orderInfo.getOrderCode();
			if(orderId.equals("")) {
				return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
			}
	        
			//生成订单日志
			OrderLog rderLog = OrderPublic.addOrderLog(orderId,Global.STATUS_0,"OrderServiceImpl.addOrderInfo()",
	      	OrderPublic.getStatus(Global.STATUS_0,orderInfo.getOrderStatus()),orderInfo.getCreateBy());
			orderLogService.addOrderLog(rderLog);
			
		
		//新增订单明细数据
		if(detailList !=null && detailList.size()>0) {
			detailList = orderDetailService.addDetailList(detailList,orderId);
		}
		//新增订单结算数据
		if(settlementInfo !=null) {
            settlementService.addSettlement(settlementInfo,orderId);
		}
		//新增订单支付数据
        if(orderPayList != null && orderPayList.size()>0) {
        	settlementService.addOrderPayList(orderPayList,orderId);
		}
        //新增订单与优惠券关系数据
        if(orderCouponList !=null && orderCouponList.size()>0) {
        	addOrderCoupon(orderCouponList,orderId);
        }
        //新增订单明细与优惠券关系数据
        if(detailList !=null && detailList.size()>0) {
        	
        	for(OrderDetailInfo orderDetailInfo : detailList) {
        		OrderRelationCouponInfo info = new OrderRelationCouponInfo();
        		info = orderDetailInfo.getCouponInfo();
        		if(info !=null ) {
        			if(orderDetailInfo.getOrderDetailId() !=null) {
        				info.setOrderDetailId(orderDetailInfo.getOrderDetailId());
        			}
        			orderCouponList.add(info);
        		}
        		
        	}
        	addOrderCoupon(orderCouponList,orderId);
		}
        
        //删除购物车数据
        String memberId = "";
        List<String> skuCodes = new ArrayList();
        String distributorId = "";
        if(orderInfo !=null ) {
        	
        	//微商城删除购物车
        	if(orderInfo.getOriginType() !=null && orderInfo.getOriginType().equals(Global.ORIGIN_TYPE_1)) {
        		if(orderInfo.getMemberId()!=null ) {
        			memberId = orderInfo.getMemberId();
            	}
        		if(orderInfo.getDistributorId() !=null ) {
        			distributorId = orderInfo.getDistributorId();
        		}
        		if(detailList !=null && detailList.size()>0) {
        			for(OrderDetailInfo info :detailList) {
            			if(info !=null && info.getSkuCode()!=null) {
            				skuCodes.add(info.getSkuCode());
            			}
            		}
        		}
        		
        		//删除
        		System.out.println("开始删除购物车======");
        		cartService.deleteCartInfoById(memberId, skuCodes, distributorId);
        	
        	}
        	
        }
	  }  
        String order_code = orderCode;
		return HttpResponse.success(order_code);
	 }catch (Exception e){
		 LOGGER.info("添加新的订单主数据以及其他订单关联数据報錯", e);
		 throw new RuntimeException("添加新的订单主数据以及其他订单关联数据報錯");
		}
	}

	//接口-关闭订单
	@Override
	@Transactional
	public HttpResponse closeorder(@Valid String orderId, String updateBy) {
		
		try {
			
			//关闭订单
			orderDao.closeOrder(orderId,updateBy);
			
			//生成订单日志
			OrderLog rderLog = OrderPublic.addOrderLog(orderId,Global.STATUS_0,"OrderServiceImpl.closeorder()",
			OrderPublic.getStatus(Global.STATUS_0,Global.ORDER_STATUS_4),updateBy);
			orderLogService.addOrderLog(rderLog);
			
			return HttpResponse.success();
		} catch (Exception e) {
			LOGGER.info("接口-关闭订单報錯", e);
			return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
		}
	}


	//接口-更新商户备注
	@Override
	@Transactional
	public HttpResponse updateorderbusinessnote(@Valid String orderId, String updateBy, String businessNote) {

		try {
			OrderInfo orderInfo = new OrderInfo();
			orderInfo.setOrderId(orderId);
			orderInfo.setUpdateBy(updateBy);
			orderInfo.setBusinessNote(businessNote);
			orderDao.updateorderbusinessnote(orderInfo);
			return HttpResponse.success();
		} catch (Exception e) {
			LOGGER.info("接口-更新商户备注報錯", e);
			return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
		}		
		
	}


	//更改订单状态/支付状态/修改员...
	@Override
	@Transactional
	public HttpResponse updateOrderStatus(@Valid String orderId, Integer orderStatus, Integer payStatus,
			String updateBy) {
		
		try {
			OrderInfo orderInfo = new OrderInfo();
			orderInfo.setOrderId(orderId);
			orderInfo.setOrderStatus(orderStatus);
			orderInfo.setPayStatus(payStatus);
			orderInfo.setUpdateBy(updateBy);
			
			//更新订单主数据
			orderDao.updateOrderStatus(orderInfo);
			
			OrderPayInfo orderPayInfo = new OrderPayInfo();
			orderPayInfo.setOrderId(orderId);
			orderPayInfo.setPayStatus(payStatus);
			orderPayInfo.setUpdateBy(updateBy);
			
			//更新支付数据
			orderPayDao.usts(orderPayInfo);
			
			//生成订单日志
			OrderLog rderLog = OrderPublic.addOrderLog(orderId,Global.STATUS_1,"OrderServiceImpl.updateOrderStatus()",
			OrderPublic.getStatus(Global.STATUS_1,payStatus),updateBy);
			orderLogService.addOrderLog(rderLog);
			
			return HttpResponse.success();
		} catch (Exception e) {
			LOGGER.info("更改订单状态/支付状态/支付方式/修改员報錯", e);
			return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
		}		
	}

    //仅更改退货状态-订单主表
	@Override
	public void retustus(@Valid String orderId, Integer returnStatus, String updateBy) throws Exception {

			OrderInfo orderInfo = new OrderInfo();
			orderInfo.setOrderId(orderId);
			orderInfo.setReturnStatus(returnStatus);
			orderInfo.setUpdateBy(updateBy);
			
			//更新订单
			orderDao.retustus(orderInfo);			
			
			//生成订单日志
			OrderLog rderLog = OrderPublic.addOrderLog(orderId,Global.STATUS_2,"OrderServiceImpl.returnStatus()",
			OrderPublic.getStatus(Global.STATUS_2,returnStatus),updateBy);
			orderLogService.addOrderLog(rderLog);
	}


	//仅变更订单状态
	@Override
	@Transactional
	public HttpResponse onlyStatus(@Valid String orderId, Integer orderStatus, String updateBy) {
		try {
			OrderInfo orderInfo = new OrderInfo();
			orderInfo.setOrderId(orderId);
			orderInfo.setOrderStatus(orderStatus);
			orderInfo.setUpdateBy(updateBy);
			
			//更新订单
			orderDao.onlyStatus(orderInfo);
			
			//判断是否变更为待提货、待提货订单需重新生成提货码.
			if(orderStatus == Global.RECEIVE_TYPE_0) {
				rede(orderId);
			}
			
			//生成订单日志
			OrderLog rderLog = OrderPublic.addOrderLog(orderId,Global.STATUS_0,"OrderServiceImpl.onlyStatus()",
			OrderPublic.getStatus(Global.STATUS_0,orderStatus),updateBy);
			orderLogService.addOrderLog(rderLog);
			
			return HttpResponse.success();
		} catch (Exception e) {
			LOGGER.info("仅变更订单状态-BY order_id,update_by modity order_status報錯", e);
			return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
		}
	}

	
	//接口-收银员交班收银情况统计
	@Override
	public HttpResponse cashier(@Valid String cashierId, String beginTime, String endTime) {
		try {
	    
		OrderbyReceiptSumResponse receiptSumInfo = new OrderbyReceiptSumResponse();
		
		OrderQuery orderQuery = new OrderQuery();
		orderQuery.setCashierId(cashierId);
		orderQuery.setBeginDate(beginTime);
		orderQuery.setEndDate(endTime);
		
		//获取收银员、支付类型金额
		List<OrderbyReceiptSumResponse> list= orderDao.cashier(orderQuery);
		
		
		//获取退款金额、退款订单数、销售额、销售订单数
		OrderbyReceiptSumResponse receiptSum= orderDao.byCashierSum(orderQuery);
		if(list !=null && list.size()>0) {
			for(int i=0;i<list.size();i++) {
				OrderbyReceiptSumResponse info = new OrderbyReceiptSumResponse();
				info = list.get(i);
				
				receiptSumInfo.setCashierId(info.getCashierId());
				receiptSumInfo.setCashierName(info.getCashierId());
				if(info.getPayType().equals(Global.P_TYPE_3)) {
					receiptSumInfo.setCash(info.getPayPrice());
				}else if(info.getPayType().equals(Global.P_TYPE_4)) {
					receiptSumInfo.setWeChat(info.getPayPrice());
				}
				else if(info.getPayType().equals(Global.P_TYPE_5)) {
					receiptSumInfo.setAliPay(info.getPayPrice());
				}
				else if(info.getPayType().equals(Global.P_TYPE_6)) {
					receiptSumInfo.setBankCard(info.getPayPrice());
				}
				else {
					receiptSumInfo.setCash(info.getPayPrice());
				}
			}
		}
		
		
		receiptSumInfo.setReturnOrderAmount(receiptSum.getReturnOrderAmount());
		receiptSumInfo.setReturnPrice(receiptSum.getReturnPrice());
		receiptSumInfo.setSalesAmount(receiptSum.getSalesAmount());
		receiptSumInfo.setSalesOrderAmount(receiptSum.getSalesOrderAmount());
		
		return HttpResponse.success(receiptSumInfo);
		} catch (Exception e) {
			LOGGER.info("接口-收银员交班收银情况统计報錯", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}


	//接口-通过会员查询最后一次的消费记录.
	@Override
	public HttpResponse last(@Valid String memberId) {
		
		
		OrderInfo info = new OrderInfo();
		LastBuyResponse lastBuyResponse = new LastBuyResponse();
		info.setMemberId(memberId);
		try {
			
			List<LastBuyResponse> list = orderDao.last(info);
			
			if(list !=null && list.size()>0) {
				lastBuyResponse = list.get(0);
				List<String> prodcuts = new ArrayList();
				for(LastBuyResponse lastBuyInfo : list) {
					if(lastBuyInfo !=null && lastBuyInfo.getProduct() !=null) {
						prodcuts.add(lastBuyInfo.getProduct());
					}
				}
				lastBuyResponse.setNewConsumeProduct(prodcuts);
				lastBuyResponse.setProduct("");
			}
		    return HttpResponse.success(lastBuyResponse);
		} catch (Exception e) {
			LOGGER.info("接口-通过会员查询最后一次的消费记录報錯", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
		
	}

	//接口-生成提货码
	@Override
	@Transactional
	public HttpResponse rede(@Valid String orderId) {
		
		//参数
		OrderQuery orderQuery = new OrderQuery();
		orderQuery.setOrderId(orderId);
		
		//生成8位提货码
		String receiveCode = "";
		receiveCode = OrderPublic.randomNumberE();
		orderQuery.setReceiveCode(receiveCode);
		
		//将提货码更新订单表中
		try {
			
			orderDao.rede(orderQuery);
			
			return HttpResponse.success(receiveCode);
					
		} catch (Exception e) {
			LOGGER.info("接口-将提货码插入订单表中報錯", e);
			return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
		}
	}


	//接口-可退货的订单查询分页
	@Override
	public HttpResponse reorer(@Valid ReorerRequest reorerRequest) {
		
		try {
			List<OrderInfo> list = new ArrayList();
			list = orderDao.reorer(reorerRequest);
			
			//计算总数据量
			Integer totalCount = 0;
			Integer icount =null;
			reorerRequest.setIcount(icount);
			List<OrderInfo> Icount_list= orderDao.reorer(reorerRequest);
			if(Icount_list !=null && Icount_list.size()>0) {
				totalCount = Icount_list.size();
			}
			
//			return HttpResponse.success(OrderPublic.getData(list));
			return HttpResponse.success(new PageResData(totalCount,list));
		} catch (Exception e) {
			LOGGER.info("接口-可退货的订单查询報錯", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
		
	}

	//接口-注销提货码
	@Override
	@Transactional
	public HttpResponse reded(@Valid String orderId) {
		
		 try {
			OrderQuery orderQuery = new OrderQuery();
		    orderQuery.setOrderId(orderId);
			orderDao.reded(orderQuery);
			return HttpResponse.success();
		} catch (Exception e) {
			LOGGER.info("接口-注销提货码報錯", e);
			return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
		}
	}

	//未付款订单30分钟后自动取消
	@Override
	public List<String> nevder() {
		
		List<String> list= null;
		try {
			
			list= orderDao.nevder();
		} catch (Exception e) {
			
			LOGGER.info("接口-注销提货码報錯", e);
		}
		return list;
	}

	//提货码10分钟后失效.
	@Override
	public List<String> nevred() {
		List<String> list= null;
		try {
			list= orderDao.nevred();
		} catch (Exception e) {
			LOGGER.info("提货码10分钟后失效.報錯", e);
		}
		return list;
	}


	//接口-通过当前门店,等级会员list、 统计订单使用的会员数、返回7天内的统计.
	@Override
	public HttpResponse devel(@Valid String distributorId, @Valid List<String> memberList) {
		
		try {
		//参数
		DevelRequest develRequest = new DevelRequest();
		develRequest.setDistributorId(distributorId);
		develRequest.setMemberList(memberList);
		
		//查询
		List<DevelRequest> list = new ArrayList();
			list = orderDao.devel(develRequest);

		
		//返回
		MevBuyResponse info = new MevBuyResponse();
		if(list !=null && list.size()>0) {
			info = getMevBuyResponse(list);
		}
		return HttpResponse.success(info);
		
		} catch (Exception e) {
			LOGGER.info("接口-通过当前门店,等级会员list、 统计订单使用的会员数、返回7天内的统计.報錯", e);
			return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
		}
	}


	//返回值处理  通过当前门店,等级会员list、 统计订单使用的会员数、返回7天内的统计.
	private MevBuyResponse getMevBuyResponse(List<DevelRequest> list) {
		MevBuyResponse info = new MevBuyResponse();
		if(list !=null && list.size()>0) {
			for(DevelRequest develRequest :list) {
				if(develRequest.getTrante().equals(OrderPublic.NextDate(0))) {
					info.setSysDate(develRequest.getTrante());
				    if(develRequest.getAcount() !=null) {
				    	info.setSysNumber(develRequest.getAcount());
				    }
				}
				if(develRequest.getTrante().equals(OrderPublic.NextDate(-1))) {
					info.setOneDate(develRequest.getTrante());
				    if(develRequest.getAcount() !=null) {
				    	info.setOneNumber(develRequest.getAcount());
				    }
				}
				if(develRequest.getTrante().equals(OrderPublic.NextDate(-2))) {
					info.setTwoDate(develRequest.getTrante());
				    if(develRequest.getAcount() !=null) {
				    	info.setTwoNumber(develRequest.getAcount());
				    }
				}
				if(develRequest.getTrante().equals(OrderPublic.NextDate(-3))) {
					info.setThreeDate(develRequest.getTrante());
				    if(develRequest.getAcount() !=null) {
				    	info.setThreeNumber(develRequest.getAcount());
				    }
				}
				if(develRequest.getTrante().equals(OrderPublic.NextDate(-4))) {
					info.setFourDate(develRequest.getTrante());
				    if(develRequest.getAcount() !=null) {
				    	info.setFourNumber(develRequest.getAcount());
				    }
				}
				if(develRequest.getTrante().equals(OrderPublic.NextDate(-5))) {
					info.setFiveDate(develRequest.getTrante());
				    if(develRequest.getAcount() !=null) {
				    	info.setFiveNumber(develRequest.getAcount());
				    }
				}
				if(develRequest.getTrante().equals(OrderPublic.NextDate(-6))) {
					info.setSixDate(develRequest.getTrante());
				    if(develRequest.getAcount() !=null) {
				    	info.setSixNumber(develRequest.getAcount());
				    }
				}
				if(develRequest.getTrante().equals(OrderPublic.NextDate(-7))) {
					info.setSevenDate(develRequest.getTrante());
				    if(develRequest.getAcount() !=null) {
				    	info.setSevenNumber(develRequest.getAcount());
				    }
				}
			}
		}
		
		return info;
	}


	//模糊查询订单列表+订单中商品sku数量分页
	@Override
	public HttpResponse oradsku(@Valid OrderQuery orderQuery) {
		
		LOGGER.info("模糊查询订单列表+订单中商品sku数量", orderQuery);

		try {
			
			//订单列表
			List<OradskuResponse> OrderInfolist = orderDao.selectskuResponse(OrderPublic.getOrderQuery(orderQuery));
			OradskuResponse oradskuResponse = new OradskuResponse();
			
			//订单中商品sku数量
			Integer skuSum = 0;
            if(OrderInfolist !=null && OrderInfolist.size()>0) {
            	for(int i=0;i<OrderInfolist.size();i++) {
    				oradskuResponse = OrderInfolist.get(i);
    				skuSum = orderDetailService.getSkuSum(oradskuResponse.getOrderId());
    				oradskuResponse.setSkuSum(skuSum);
    				OrderInfolist.set(i, oradskuResponse);
    			}
			}
			
          //计算总数据量
			Integer totalCount = 0;
			Integer icount =null;
			orderQuery.setIcount(icount);
			List<OradskuResponse> Icount_list= orderDao.selectskuResponse(OrderPublic.getOrderQuery(orderQuery));
			if(Icount_list !=null && Icount_list.size()>0) {
				totalCount = Icount_list.size();
			}
            
//			return HttpResponse.success(OrderPublic.getData(OrderInfolist));
			return HttpResponse.success(new PageResData(totalCount,OrderInfolist));
			
		} catch (Exception e) {
			LOGGER.info("模糊查询订单列表+订单中商品sku数量報錯", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}

    //查询订单日志数据
	@Override
	public HttpResponse orog(@Valid String orderId) {
		
		return orderLogService.orog(orderId);
	}


	//导出订单列表
	@Override
	public HttpResponse exorder(@Valid OrderQuery orderQuery) {

		try {
			Integer icount =null;
			orderQuery.setIcount(icount);
			List<OrderInfo> OrderInfolist = orderDao.selectOrder(OrderPublic.getOrderQuery(orderQuery));
			
			return HttpResponse.success(OrderInfolist);
			
		} catch (Exception e) {
			LOGGER.info("导出订单列表報錯", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}
	
	
	//添加订单主数据+添加订单明细数据+返回订单编号
	@Override
	@Transactional
	public HttpResponse addOrdta(@Valid OrderAndSoOnRequest orderAndSoOnRequest){
		
	 try {
		OrderInfo orderInfo = orderAndSoOnRequest.getOrderInfo();
		List<OrderDetailInfo> detailList = orderAndSoOnRequest.getDetailList();
		
		//新增订单主数据
		String orderId = "";
		String orderCode = "";
		if(orderInfo !=null ) {
			orderInfo = addOrderInfo(orderInfo);
			orderId = orderInfo.getOrderId();
			orderCode = orderInfo.getOrderCode();
			if(orderId.equals("")) {
				return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
			}
			
		  if(orderId !=null && !orderId.equals("")) {
				
			//生成订单日志
			OrderLog rderLog = OrderPublic.addOrderLog(orderId,Global.STATUS_0,"OrderServiceImpl.addOrderInfo()",
	      	OrderPublic.getStatus(Global.STATUS_0,orderInfo.getOrderStatus()),orderInfo.getCreateBy());
			orderLogService.addOrderLog(rderLog);
			
		//新增订单明细数据
		if(detailList !=null && detailList.size()>0) {
			detailList = orderDetailService.addDetailList(detailList,orderId);
		}
        
        //删除购物车数据
        String memberId = "";
        List<String> skuCodes = new ArrayList();
        String distributorId = "";
        if(orderInfo !=null ) {
        	
        	//微商城删除购物车
        	if(orderInfo.getOriginType() !=null && orderInfo.getOriginType().equals(Global.ORIGIN_TYPE_1)) {
        		if(orderInfo.getMemberId()!=null ) {
        			memberId = orderInfo.getMemberId();
            	}
        		if(orderInfo.getDistributorId() !=null ) {
        			distributorId = orderInfo.getDistributorId();
        		}
        		if(detailList !=null && detailList.size()>0) {
        			for(OrderDetailInfo info :detailList) {
            			if(info !=null && info.getSkuCode()!=null) {
            				skuCodes.add(info.getSkuCode());
            			}
            		}
        		}
        		
        		//删除
        		System.out.println("开始删除购物车======");
        		cartService.deleteCartInfoById(memberId, skuCodes, distributorId);
        	
        	}	
        }
       } 
	  }
        String order_code = orderCode;
		return HttpResponse.success(order_code);
	 }catch (Exception e){
		 LOGGER.info("添加新的订单主数据以及其他订单关联数据報錯", e);
		 throw new RuntimeException("添加新的订单主数据以及其他订单关联数据報錯");
		}
	}
	
	
	//添加结算数据+添加支付数据+添加优惠关系数据+修改订单主数据+修改订单明细数据
	@Override
	@Transactional
	public HttpResponse addPamo(@Valid OrderAndSoOnRequest orderAndSoOnRequest){
		
	 try {
		OrderInfo orderInfo = orderAndSoOnRequest.getOrderInfo();
		List<OrderDetailInfo> detailList = orderAndSoOnRequest.getDetailList();
		SettlementInfo settlementInfo = orderAndSoOnRequest.getSettlementInfo();
		List<OrderPayInfo> orderPayList = orderAndSoOnRequest.getOrderPayList();
		List<OrderRelationCouponInfo> orderCouponList = orderAndSoOnRequest.getOrderCouponList();
		
		//修改订单主数据
		String orderId = "";
		String orderCode = "";
		if(orderInfo !=null ) {
			orderCode = orderInfo.getOrderCode();
//			orderId = orderInfo.getOrderId();
			//通过订单编码查询订单ID
			try {
			orderId = orderDao.getOrderIdByCode(orderCode);
			}catch (Exception e){
				 LOGGER.info("orderDao.getOrderIdByCode(orderCode)", e);
		    }
			orderInfo.setOrderId(orderId);
			if(orderId !=null && !orderId.equals("")) {
			
//			//生成订单号
//		    String logo = "";
//			if(orderInfo.getOriginType() == Global.ORIGIN_TYPE_0) {
//				logo = Global.ORIGIN_COME_3;
//			}
//			if(orderInfo.getOriginType() == Global.ORIGIN_TYPE_1) {
//				logo = Global.ORIGIN_COME_4;
//			}
//			if(orderInfo.getOriginType() == Global.ORIGIN_TYPE_3) {
//				logo = Global.ORIGIN_COME_5;
//			}
//			//公司标识
//			String companyCode = "";
//			if(orderInfo.getCompanyCode() !=null && orderInfo.getCompanyCode().equals(Global.COMPANY_01)) {
//				companyCode = Global.COMPANY_01;
//			}else {
//				companyCode = Global.COMPANY_01;
//			}
//			//yyMMddHHmmss+订单来源+销售渠道标识+公司标识+4位数的随机数
//			String orderCode = "";
//			orderCode = OrderPublic.currentDate()+logo+String.valueOf(Global.ORDERID_CHANNEL_4)+companyCode+OrderPublic.randomNumberF();
//			orderInfo.setOrderCode(orderCode);
			
			//初始化提货码
			if(orderInfo.getOriginType() == Global.ORIGIN_TYPE_1) {
				if(orderInfo.getReceiveType().equals(Global.RECEIVE_TYPE_0)) {
					orderInfo.setReceiveCode(OrderPublic.randomNumberE());
				}
			}
			
			//删除订单主数据
			try {
			orderDao.deleteOrderInfo(orderInfo);
			}catch (Exception e){
				 LOGGER.info("orderDao.deleteOrderInfo(orderInfo)", e);
		    }
			
			//新增订单主数据
			try {
			orderDao.addOrderInfo(orderInfo);
			}catch (Exception e){
				 LOGGER.info("orderDao.addOrderInfo(orderInfo)", e);
		    }
	        
			//生成订单日志
			OrderLog rderLog = OrderPublic.addOrderLog(orderId,Global.STATUS_0,"OrderServiceImpl.addOrderInfo()",
	      	OrderPublic.getStatus(Global.STATUS_0,orderInfo.getOrderStatus()),orderInfo.getCreateBy());
			try {
			orderLogService.addOrderLog(rderLog);
			}catch (Exception e){
				 LOGGER.info("orderLogService.addOrderLog(rderLog)1", e);
		    }
			//删除订单明细数据
			try {
			orderDetailDao.deleteOrderDetailInfo(orderInfo); 
			}catch (Exception e){
				 LOGGER.info("orderLogService.addOrderLog(rderLog)1", e);
		    }
		//新增订单明细数据
		if(detailList !=null && detailList.size()>0) {
			try {
			detailList = orderDetailService.addDetailList(detailList,orderId);
		    }catch (Exception e){
			 LOGGER.info("orderLogService.addOrderLog(rderLog)1", e);
	        }
		}
		//新增订单结算数据
		if(settlementInfo !=null) {
			try {
            settlementService.addSettlement(settlementInfo,orderId);
		    }catch (Exception e){
			 LOGGER.info("orderLogService.addOrderLog(rderLog)1", e);
	        }
		}
		//新增订单支付数据
        if(orderPayList != null && orderPayList.size()>0) {
        	try {
        	settlementService.addOrderPayList(orderPayList,orderId);
            }catch (Exception e){
			 LOGGER.info("orderLogService.addOrderLog(rderLog)1", e);
	        }
		}
        //新增订单与优惠券关系数据
        if(orderCouponList !=null && orderCouponList.size()>0) {
        	addOrderCoupon(orderCouponList,orderId);
        }
        //新增订单明细与优惠券关系数据
        if(detailList !=null && detailList.size()>0) {
        	
        	for(OrderDetailInfo orderDetailInfo : detailList) {
        		OrderRelationCouponInfo info = new OrderRelationCouponInfo();
        		info = orderDetailInfo.getCouponInfo();
        		if(info !=null ) {
        			if(orderDetailInfo.getOrderDetailId() !=null) {
        				info.setOrderDetailId(orderDetailInfo.getOrderDetailId());
        			}
        			orderCouponList.add(info);
        		}
        		
        	}
        	addOrderCoupon(orderCouponList,orderId);
		}
	   }
	  }
		
		return HttpResponse.success();
	 }catch (Exception e){
		 LOGGER.info("添加新的订单主数据以及其他订单关联数据報錯", e);
		 throw new RuntimeException("添加新的订单主数据以及其他订单关联数据報錯");
		}
	}


	//删除订单主数据+删除订单明细数据
	@Override
	public HttpResponse deordta(@Valid String orderId) {
		
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setOrderId(orderId);
		try {
			//删除订单主数据
			orderDao.deleteOrderInfo(orderInfo);
			
			//删除订单明细数据
			orderDetailDao.deleteOrderDetailInfo(orderInfo); 
			
			return HttpResponse.success();
		} catch (Exception e) {
			LOGGER.info("删除订单主数据+删除订单明细数据報錯", e);
			return HttpResponse.failure(ResultCode.DELETE_EXCEPTION);
		}
	
	}


	//微商城-销售总览
	@Override
	public HttpResponse wssev(@Valid String distributorId) {
		
		
	  try {
		WscSaleResponse wscSaleResponse = new WscSaleResponse();
		String beginDate = "";
        String endDate = "";
		
        //今日订单数
        Integer todayCount = null;
		//昨日订单数
        Integer yesterdayCount = null;
		//近七日数据订单数
        Integer weekCount = null;
        
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setDistributorId(distributorId);
        
        orderQuery.setBeginDate(OrderPublic.NextDate(0));
        orderQuery.setEndDate(OrderPublic.NextDate(0));
		todayCount = orderDao.selectAcountByEcshop(orderQuery);
		
		orderQuery.setBeginDate(OrderPublic.NextDate(-1));
        orderQuery.setEndDate(OrderPublic.NextDate(-1));
        yesterdayCount = orderDao.selectAcountByEcshop(orderQuery);
        
        orderQuery.setBeginDate(OrderPublic.NextDate(-7));
        orderQuery.setEndDate(OrderPublic.NextDate(0));
        weekCount = orderDao.selectAcountByEcshop(orderQuery);
        
        //昨日订单金额
        Integer todayAmount = null;
        //今日订单金额
        Integer yesterdayAmount = null;
        //近七日数据订单金额
        Integer weekAmount = null;
        
        orderQuery.setBeginDate(OrderPublic.NextDate(0));
        orderQuery.setEndDate(OrderPublic.NextDate(0));
        todayAmount = orderDao.selectAmountByEcshop(orderQuery);
        
        orderQuery.setBeginDate(OrderPublic.NextDate(-1));
        orderQuery.setEndDate(OrderPublic.NextDate(-1));
        yesterdayAmount = orderDao.selectAmountByEcshop(orderQuery);
        
        orderQuery.setBeginDate(OrderPublic.NextDate(-7));
        orderQuery.setEndDate(OrderPublic.NextDate(0));
        weekAmount = orderDao.selectAmountByEcshop(orderQuery);
		
		//今日成交客户
        Integer todayMembers = null;
        orderQuery.setBeginDate(OrderPublic.NextDate(0));
        orderQuery.setEndDate(OrderPublic.NextDate(0));
        todayMembers = orderDao.selectMembersByEcshop(orderQuery);
        
	    //昨日成交客户
        Integer yesterdayMembers = null;
        orderQuery.setBeginDate(OrderPublic.NextDate(-1));
        orderQuery.setEndDate(OrderPublic.NextDate(-1));
        yesterdayMembers = orderDao.selectMembersByEcshop(orderQuery);
		
        wscSaleResponse.setTodayAmount(todayAmount);
        wscSaleResponse.setTodayCount(todayCount);
        wscSaleResponse.setTodayMembers(todayMembers);
        wscSaleResponse.setWeekAmount(weekAmount);
        wscSaleResponse.setWeekCount(weekCount);
        wscSaleResponse.setYesterdayAmount(yesterdayAmount);
        wscSaleResponse.setYesterdayCount(yesterdayCount);
        wscSaleResponse.setYesterdayMembers(yesterdayMembers);
        
        return HttpResponse.success(wscSaleResponse);
        
		} catch (Exception e) {
			LOGGER.info("微商城-销售总览報錯", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}


	//微商城-事务总览
	@Override
	public HttpResponse wsswv(@Valid String distributorId) {
		
	  try {
		 WscWorkViewResponse wscWorkViewResponse = new WscWorkViewResponse();
		
		 //待付款订单
		 Integer unPaid = null;
		 //待提货订单
		 Integer notPick = null;
		 //待发货订单
		 Integer notDelivery = null;
		 //待确认售后单
		 Integer notSure = null;
		 //待处理退款申请
		 Integer notRefund = null;
		 
		 OrderQuery orderQuery = new OrderQuery();
	     orderQuery.setDistributorId(distributorId);
	     
	     orderQuery.setOrderStatus(Global.ORDER_STATUS_0);
	     unPaid = orderDao.selectCountByStatus(orderQuery);
	     
	     orderQuery.setOrderStatus(Global.ORDER_STATUS_2);
	     notPick = orderDao.selectCountByStatus(orderQuery);
	     
	     orderQuery.setOrderStatus(Global.ORDER_STATUS_1);
	     notDelivery = orderDao.selectCountByStatus(orderQuery);
	     
	     
	     wscWorkViewResponse.setNotDelivery(notDelivery);
	     wscWorkViewResponse.setNotPick(notPick);
	     wscWorkViewResponse.setNotRefund(notRefund);
	     wscWorkViewResponse.setNotSure(notSure);
	     wscWorkViewResponse.setUnPaid(unPaid);
	     
		return HttpResponse.success(wscWorkViewResponse);
	        
	  } catch (Exception e) {
		LOGGER.info("微商城-事务总览報錯", e);
		return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
	  }
	}


	//已存在订单更新支付状态、重新生成支付数据(更改订单表、删除新增支付表)
	@Override
	public HttpResponse repast(@Valid String orderId, @Valid String payType, @Valid List<OrderPayInfo> orderPayList) {
		
		try {
			
		  //删除订单支付数据
			settlementService.deleteOrderPayList(orderId);
	      //新增订单支付数据
          if(orderPayList != null && orderPayList.size()>0) {
        	settlementService.addOrderPayList(orderPayList,orderId);
		  }
          
          //修改订单支付方式信息
          OrderQuery orderQuery = new OrderQuery();
          orderQuery.setOrderId(orderId);
          orderQuery.setPayType(payType);
          orderDao.onlyPayType(orderQuery);
          
        return HttpResponse.success();
        
	    } catch (Exception e) {
		  LOGGER.info("已存在订单更新支付状态、重新生成支付数据(更改订单表、删除新增支付表)報錯", e);
		  return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
	    }
	}

	//销售目标管理-分销机构-月销售额
	@Override
	public HttpResponse selectDistributorMonth(@Valid List<String> distributorCodeList,String beginTime,String endTime) {
		try {
			List<DistributorMonthResponse> list = new ArrayList();
			//当月销售额：包含退货、不包含取消、未付款的订单
			Integer monthtotalPrice=0;
			if(distributorCodeList !=null && distributorCodeList.size()>0) {
				for(String distributorCode : distributorCodeList) {
					DistributorMonthResponse info = new DistributorMonthResponse();
					monthtotalPrice = orderDao.selectDistributorMonth(distributorCode,beginTime,endTime);
					info.setDistributorCode(distributorCode);
					if(monthtotalPrice ==null) {
						info.setPrice(0);
					}else {
						info.setPrice(monthtotalPrice);
					}
					list.add(info);
				}
			}
		return HttpResponse.success(list);
    } catch (Exception e) {
	  LOGGER.info("销售目标管理-分销机构-月销售额報錯", e);
	  return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
    }
	}
	
}


