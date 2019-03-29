/*****************************************************************

* 模块名称：结算后台-实现层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service.impl;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.CartDao;
import com.aiqin.mgs.order.api.dao.OrderDao;
import com.aiqin.mgs.order.api.dao.OrderPayDao;
import com.aiqin.mgs.order.api.dao.SettlementDao;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.FrozenInfo;
import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.domain.OrderDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.OrderPayInfo;
import com.aiqin.mgs.order.api.domain.PaymentInfo;
import com.aiqin.mgs.order.api.domain.PaymentReturnInfo;
import com.aiqin.mgs.order.api.domain.SettlementInfo;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.service.OrderLogService;
import com.aiqin.mgs.order.api.service.SettlementService;
import com.aiqin.mgs.order.api.util.OrderPublic;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;

@SuppressWarnings("all")
@Service
public class SettlementServiceImpl implements SettlementService{

	private static final Logger LOGGER = LoggerFactory.getLogger(SettlementServiceImpl.class);
	
	
	@Resource
    private SettlementDao settlementDao;
	
	@Resource
    private OrderPayDao orderPayDao;
	
	@Resource
    private OrderLogService orderLogService;
	
	//查询结算
	@Override
	public HttpResponse jkselectsettlement(@Valid OrderQuery orderQuery) {
		
		try {
			SettlementInfo settlementInfo = new SettlementInfo();
			if(orderQuery !=null && orderQuery.getOrderId() !=null && !orderQuery.getOrderId().equals("")) {
				settlementInfo = settlementDao.jkselectsettlement(orderQuery);
			}
			
			return HttpResponse.success(settlementInfo);
		
		} catch (Exception e) {

			LOGGER.error("查询结算信息失败: {}", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}


    //添加新的结算数据
	@Override
	@Transactional
	public void addSettlement(@Valid SettlementInfo settlementInfo, @Valid String orderId) throws Exception {

		if(settlementInfo !=null) {
			   settlementInfo.setOrderId(orderId);
			   settlementInfo.setSettlementId(OrderPublic.getUUID());
				
				   settlementDao.addSettlement(settlementInfo);
		}else {
			LOGGER.warn("未获取到结算数据: {},orderId: {}",settlementInfo,orderId);
		}
	}

	
    //添加新的支付数据
	@Override
	@Transactional
	public void addOrderPayList(@Valid List<OrderPayInfo> orderPayList, @Valid String orderId,@Valid String orderCode) throws Exception {
		
		if(orderPayList !=null && orderPayList.size()>0) {
			for(OrderPayInfo info :orderPayList) {
				
				//订单ID,支付ID
				info.setOrderId(orderId);
				info.setOrderCode(orderCode);
				info.setPayId(OrderPublic.getUUID());
				
			    orderPayDao.addOrderPay(info);
			}
		}else {
			LOGGER.warn("未获取到支付数据orderPayList :{},orderId : {}",orderPayList,orderId);
		}
	}

	
	//查询支付数据通过Order_id
	@Override
	public HttpResponse pay(@Valid String orderId) {
		try {
			OrderPayInfo info = new OrderPayInfo();
			info.setOrderId(orderId);
			List<OrderPayInfo> list = orderPayDao.pay(info);
			return HttpResponse.success(list);
		} catch (Exception e) {
			LOGGER.error("查询支付数据通过Order_id报错: {}",e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}


	//已存在订单更新支付状态、重新生成支付数据(更改订单表、删除新增支付表)
	@Override
	@Transactional
	public void deleteOrderPayList(@Valid String orderId) throws Exception {
		
	   orderPayDao.deleteOrderPayList(orderId);
		
	}
}
