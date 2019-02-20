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
import com.aiqin.mgs.order.api.domain.OrderPayInfo;
import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.domain.OrderRelationCouponInfo;
import com.aiqin.mgs.order.api.domain.OrderodrInfo;
import com.aiqin.mgs.order.api.domain.ProductCycle;
import com.aiqin.mgs.order.api.domain.SettlementInfo;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.request.OrderAndSoOnRequest;
import com.aiqin.mgs.order.api.domain.request.OrderDetailRequest;
import com.aiqin.mgs.order.api.domain.request.ReorerRequest;
import com.aiqin.mgs.order.api.domain.response.OrderOverviewMonthResponse;
import com.aiqin.mgs.order.api.domain.response.OrderResponse;
import com.aiqin.mgs.order.api.domain.response.OrderbyReceiptSumResponse;
import com.aiqin.mgs.order.api.domain.response.LastBuyResponse;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.OrderDetailService;
import com.aiqin.mgs.order.api.service.OrderLogService;
import com.aiqin.mgs.order.api.service.OrderService;
import com.aiqin.mgs.order.api.service.SettlementService;
import com.aiqin.mgs.order.api.util.OrderPublic;
import com.fasterxml.jackson.core.type.TypeReference;

@SuppressWarnings("all")
@Service
public class OrderLogServiceImpl implements OrderLogService{

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderLogServiceImpl.class);
	
	@Resource
    private OrderLogDao orderLogDao;
	

	//添加订单日志
	@Override
	@Transactional
	public void addOrderLog(@Valid OrderLog logInfo) throws Exception {

			//生成日志ID
			String status = logInfo.getStatusContent();
			if(status !=null && !status.equals("")) {
				if(status.equals(Global.STATUS_CONTENT_10)) {
					logInfo.setStatusType(Global.STATUS_TYPE_10);
				}
				if(status.equals(Global.STATUS_CONTENT_1)) {
					logInfo.setStatusType(Global.STATUS_TYPE_1);
				}
				if(status.equals(Global.STATUS_CONTENT_2)) {
					logInfo.setStatusType(Global.STATUS_TYPE_2);
				}
				if(status.equals(Global.STATUS_CONTENT_3)) {
					logInfo.setStatusType(Global.STATUS_TYPE_3);
				}
				if(status.equals(Global.STATUS_CONTENT_4)) {
					logInfo.setStatusType(Global.STATUS_TYPE_4);
				}
				if(status.equals(Global.STATUS_CONTENT_5)) {
					logInfo.setStatusType(Global.STATUS_TYPE_5);
				}
				if(status.equals(Global.STATUS_CONTENT_6)) {
					logInfo.setStatusType(Global.STATUS_TYPE_6);
				}
				if(status.equals(Global.STATUS_CONTENT_100)) {
					logInfo.setStatusType(Global.STATUS_TYPE_100);
				}
				if(status.equals(Global.STATUS_CONTENT_11)) {
					logInfo.setStatusType(Global.STATUS_TYPE_11);
				}
				if(status.equals(Global.STATUS_CONTENT_22)) {
					logInfo.setStatusType(Global.STATUS_TYPE_22);
				}
				if(status.equals(Global.STATUS_CONTENT_33)) {
					logInfo.setStatusType(Global.STATUS_TYPE_33);
				}
				if(status.equals(Global.STATUS_CONTENT_44)) {
					logInfo.setStatusType(Global.STATUS_TYPE_44);
				}
				if(status.equals(Global.STATUS_CONTENT_55)) {
					logInfo.setStatusType(Global.STATUS_TYPE_55);
				}
				if(status.equals(Global.STATUS_CONTENT_66)) {
					logInfo.setStatusType(Global.STATUS_TYPE_66);
				}
				if(status.equals(Global.STATUS_CONTENT_1000)) {
					logInfo.setStatusType(Global.STATUS_TYPE_1000);
				}
				if(status.equals(Global.STATUS_CONTENT_111)) {
					logInfo.setStatusType(Global.STATUS_TYPE_111);
				}
			}
			orderLogDao.addOrderLog(logInfo);
	}


	//查询订单日志
	@Override
	public HttpResponse orog(@Valid String orderId) {
		
		try {
			List<OrderLog> list = orderLogDao.orog(orderId);
			return HttpResponse.success(list);
		} catch (Exception e) {
			LOGGER.info("查询订单日志報錯", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}
}


