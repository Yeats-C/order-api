/*****************************************************************

* 模块名称：订单后台-实现层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

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
import org.springframework.transaction.annotation.Transactional;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.CartDao;
import com.aiqin.mgs.order.api.dao.OrderCouponDao;
import com.aiqin.mgs.order.api.dao.OrderDao;
import com.aiqin.mgs.order.api.dao.OrderDetailDao;
import com.aiqin.mgs.order.api.dao.OrderLogDao;
import com.aiqin.mgs.order.api.dao.OrderReceivingDao;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailQuery;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.OrderListInfo;
import com.aiqin.mgs.order.api.domain.OrderLog;
import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.domain.OrderRelationCouponInfo;
import com.aiqin.mgs.order.api.domain.OrderodrInfo;
import com.aiqin.mgs.order.api.domain.ProductCycle;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.request.OrderDetailRequest;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.OrderService;
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
	
	
	
	@Override
	public HttpResponse selectOrder(OrderQuery OrderQuery) {
		
			LOGGER.info("模糊查询订单列表", OrderQuery);

			try {
				List<OrderInfo> OrderInfolist = orderDao.selectOrder(OrderQuery);
				
				int total =0;
				if(OrderInfolist !=null && OrderInfolist.size()>0) {
					OrderInfo orderInfo = OrderInfolist.get(0);
					total = orderInfo.getRowno();  

				
				}
				return HttpResponse.success(new PageResData(total,OrderInfolist));
				
			} catch (Exception e) {
				LOGGER.info("模糊查询订单列表報錯", e);
				return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
			}
			
	}

	
	//添加新的订单主数据
	@Override
	public HttpResponse addOrderInfo(@Valid OrderInfo orderInfo) {
		
		String order_id = "";
		String order_code = "";
		try {
			//生成订单ID
			order_id = OrderPublic.getUUID();
			orderInfo.setOrderId(order_id);
			
			//生成订单号
			order_code = OrderPublic.currentDate()+String.valueOf(orderInfo.getOriginType())+String.valueOf(Global.ORDERID_channel_4)+OrderPublic.randomNumberF();
			orderInfo.setOrderCode(order_code);
			
			//订单主数据
			orderDao.addOrderInfo(orderInfo);
			
			return HttpResponse.success(order_id);
		} catch (Exception e) {
			LOGGER.info("添加新的订单主数据報錯", e);
			return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
		}
		
	}


	//添加订单日志
	@Override
	public HttpResponse addOrderLog(@Valid OrderLog logInfo) {
		try {
			
			//生成日志ID
			logInfo.setOrderId(OrderPublic.getUUID());
			
			orderLogDao.addOrderLog(logInfo);
			return HttpResponse.success();
		} catch (Exception e) {
			LOGGER.info("添加订单日志報錯", e);
			return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
		}
	}


	//添加新的订单优惠券关系表数据
	@Override
	public HttpResponse addOrderCoupon(@Valid List<OrderRelationCouponInfo> orderCouponList, @Valid String orderId) {
		
        for(OrderRelationCouponInfo info : orderCouponList) {
        	info.setOrderId(orderId);
        	info.setCouponId(OrderPublic.getUUID());
            orderCouponDao.addOrderCoupon(info);
        }
		return null;
	}

	//接口-分销机构维度-总销售额 返回INT
	@Override
	public HttpResponse selectOrderAmt(@Valid String distributorId, String originType) {
		
		try {
			int  total_price = orderDao.selectOrderAmt(distributorId,originType);
			
			return HttpResponse.success(total_price);
		} catch (Exception e) {

			LOGGER.info("接口-分销机构维度-总销售额 返回INT報錯", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
		
		
	}


	//接口-分销机构+当月维度-当月销售额、当月实收、当月支付订单数
	@Override
	public HttpResponse selectorderbymonth(@Valid String distributorId, String originType) {

		//系统日期:年月
		String year = OrderPublic.sysDateyyyy();
		String month = OrderPublic.sysDatemm();
		//当月销售额：包含退货、取消的订单
		orderDao.selectByMonthAllAmt(distributorId,originType,year,month);
		
		//当月实收：不包含退货、取消的订单
		orderDao.selectbByMonthRetailAmt(distributorId,originType,year,month);
		
		//当月支付订单数：包含退货、取消的订单
		orderDao.selectByMonthAcount(distributorId,originType,year,month);
		
		
		return null;
	}
	
	
	


	
	

	
	
}


