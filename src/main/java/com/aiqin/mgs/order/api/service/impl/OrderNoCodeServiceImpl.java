/*****************************************************************

* 模块名称：服务订单后台-实现层
* 开发人员: 黄祉壹
* 开发时间: 2019-02-15

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
import com.aiqin.mgs.order.api.dao.OrderNoCodeDao;
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
import com.aiqin.mgs.order.api.domain.request.DetailCouponRequest;
import com.aiqin.mgs.order.api.domain.request.DevelRequest;
import com.aiqin.mgs.order.api.domain.request.DistributorMonthRequest;
import com.aiqin.mgs.order.api.domain.request.MemberByDistributorRequest;
import com.aiqin.mgs.order.api.domain.request.OrderAndSoOnRequest;
import com.aiqin.mgs.order.api.domain.request.OrderDetailRequest;
import com.aiqin.mgs.order.api.domain.request.ReorerRequest;
import com.aiqin.mgs.order.api.domain.response.OrderOverviewMonthResponse;
import com.aiqin.mgs.order.api.domain.response.OrderResponse;
import com.aiqin.mgs.order.api.domain.response.OrderbyReceiptSumResponse;
import com.aiqin.mgs.order.api.domain.response.SelectByMemberPayCountResponse;
import com.aiqin.mgs.order.api.domain.response.WscSaleResponse;
import com.aiqin.mgs.order.api.domain.response.WscWorkViewResponse;
import com.aiqin.mgs.order.api.domain.response.OrderNoCodeResponse.SelectSumByStoreIdResonse;
import com.aiqin.mgs.order.api.domain.response.DistributorMonthResponse;
import com.aiqin.mgs.order.api.domain.response.LastBuyResponse;
import com.aiqin.mgs.order.api.domain.response.MevBuyResponse;
import com.aiqin.mgs.order.api.domain.response.OradskuResponse;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.OrderDetailService;
import com.aiqin.mgs.order.api.service.OrderLogService;
import com.aiqin.mgs.order.api.service.OrderNoCodeService;
import com.aiqin.mgs.order.api.service.OrderService;
import com.aiqin.mgs.order.api.service.SettlementService;
import com.aiqin.mgs.order.api.util.OrderPublic;
import com.fasterxml.jackson.core.type.TypeReference;

@SuppressWarnings("all")
@Service
public class OrderNoCodeServiceImpl implements OrderNoCodeService{

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderNoCodeServiceImpl.class);
	
	
	@Resource
    private OrderNoCodeDao orderNoCodeDao;


	//订单概览统计
	@Override
	public HttpResponse selectSumByStoreId(@Valid String distributorId) {
		
		//参数
		String beginDate = "";
		String endDate = "";
		
		//返回值
		SelectSumByStoreIdResonse info = new SelectSumByStoreIdResonse();
		
		Integer price = null;
		Integer count = null;
		Integer passengerFlow = null;
		Integer yesterdayPrice = null;
		Integer yesterdayCount = null;
		Integer yesterdayPassengerFlow = null;
		
		//总销售金额
		Integer incomePrice = orderNoCodeDao.getIncomePrice(distributorId,null,null);
		//总退次金额
		Integer returnPrice = orderNoCodeDao.getReturnPrice(distributorId,null,null);
		if(incomePrice == null) {
			price = 0;
		}else {
			if(returnPrice == null) {
				price = incomePrice;
			}else {
				price = incomePrice.intValue()-returnPrice.intValue();
			}
		}

		//总销量
		Integer incomeCount = orderNoCodeDao.getIncomeCount(distributorId,null,null); 
		//总退货量
		Integer returnCount = orderNoCodeDao.getReturnCount(distributorId,null,null);
		if(incomeCount == null) {
			count = 0;
		}else {
			if(returnCount == null) {
				count = incomeCount;
			}else {
				count = incomeCount.intValue()-returnCount.intValue();
			}
		}
		
		//当月客流量
		beginDate = OrderPublic.getMonth(0);
		endDate = OrderPublic.NextDate(0);
		passengerFlow = orderNoCodeDao.getPassengerFlow(distributorId,beginDate,endDate);
		
		
		beginDate = OrderPublic.NextDate(-1);
		endDate = OrderPublic.NextDate(-1);
		
		
		//昨日订单金额
		Integer yesterdayIncomePrice = orderNoCodeDao.getIncomePrice(distributorId,beginDate,endDate);
		//昨日订单的当天退货金额
		Integer yesterdayReturnPrice = orderNoCodeDao.getYesterdayReturnPrice(distributorId,beginDate,endDate);
		if(yesterdayIncomePrice == null) {
			yesterdayPrice = 0;
	    }else {
		   if(yesterdayReturnPrice == null) {
			   yesterdayPrice = yesterdayIncomePrice;
		   }else {
			   yesterdayPrice = yesterdayIncomePrice.intValue()-yesterdayReturnPrice.intValue();
		   }
	    }
		
		
		//昨日订单销量
		Integer yesterdayIncomeCount = orderNoCodeDao.getIncomeCount(distributorId,beginDate,endDate);
		//昨日订单的当天退货量
		Integer yesterdayReturnCount = orderNoCodeDao.getYesterdayReturnCount(distributorId,beginDate,endDate);
		
		if(yesterdayIncomeCount == null) {
			yesterdayCount = 0;
	    }else {
		   if(yesterdayReturnCount == null) {
			   yesterdayCount = yesterdayIncomeCount;
		   }else {
			   yesterdayCount = yesterdayIncomeCount.intValue()-yesterdayReturnCount.intValue();
		   }
	    }
		
		//昨日客流量
		yesterdayPassengerFlow = orderNoCodeDao.getPassengerFlow(distributorId,beginDate,endDate);
		
		info.setPrice(price);
		info.setCount(count);
		info.setPassengerFlow(passengerFlow);
		info.setYesterdayPrice(yesterdayPrice);
		info.setYesterdayCount(yesterdayCount);
		info.setYesterdayPassengerFlow(yesterdayPassengerFlow);
		
		return HttpResponse.success(info);
	}
}


