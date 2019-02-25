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
import com.aiqin.mgs.order.api.dao.SettlementDao;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailQuery;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.OrderListInfo;
import com.aiqin.mgs.order.api.domain.OrderLog;
import com.aiqin.mgs.order.api.domain.OrderNoCodeInfo;
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
import com.aiqin.mgs.order.api.domain.request.OrderNoCodeRequest;
import com.aiqin.mgs.order.api.domain.request.ReorerRequest;
import com.aiqin.mgs.order.api.domain.response.OrderOverviewMonthResponse;
import com.aiqin.mgs.order.api.domain.response.OrderResponse;
import com.aiqin.mgs.order.api.domain.response.OrderbyReceiptSumResponse;
import com.aiqin.mgs.order.api.domain.response.SelectByMemberPayCountResponse;
import com.aiqin.mgs.order.api.domain.response.WscSaleResponse;
import com.aiqin.mgs.order.api.domain.response.WscWorkViewResponse;
import com.aiqin.mgs.order.api.domain.response.OrderNoCodeResponse.SelectSaleViewResonse;
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
	
	@Resource
    private OrderDao orderDao;
	
	@Resource
    private OrderDetailDao orderDetailDao;
	
	@Resource
    private SettlementDao settlementDao;
	
	@Resource
    private OrderPayDao orderPayDao;
	
	
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

	
	//商品类别销售概况
	@Override
	public HttpResponse selectSaleView(@Valid String distributorId, @Valid String beginDate, @Valid String endDate) {
		
		//返回参数
		List<SelectSaleViewResonse> list = new ArrayList();
		
		//订单所涉及到的商品类别
		list = orderNoCodeDao.getTypeId(distributorId,beginDate,endDate);
		
		Integer price = null;
		Integer count = null;
		Integer passengerFlow = null;
						
		//拼接商品类别销售额
		  //订单的销售额
		  List<SelectSaleViewResonse> IncomePriceList = new ArrayList();
		  IncomePriceList = orderNoCodeDao.getIncomePriceGroupByTypeId(distributorId,beginDate,endDate);
		
		  //订单的退货金额
		  List<SelectSaleViewResonse> ReturnPriceList = new ArrayList();
		  ReturnPriceList = orderNoCodeDao.getReturnPriceGroupByTypeId(distributorId,beginDate,endDate);
		
		  if(IncomePriceList != null && IncomePriceList.size() >0) {
			   if(ReturnPriceList == null || ReturnPriceList.size()<=0) {

			   }else {
				   for(SelectSaleViewResonse info : ReturnPriceList) {
					   String typeId = info.getTypeId();
					   Integer returnPrice = info.getPrice();
					   for(int i = 0;i<IncomePriceList.size();i++) {
						   SelectSaleViewResonse saleInfo = new SelectSaleViewResonse();
						   saleInfo = IncomePriceList.get(i);
						   if(saleInfo.getTypeId().equals(typeId)) {
							   //销售额
							   saleInfo.setPrice(saleInfo.getPrice().intValue()-returnPrice.intValue());
							   IncomePriceList.set(i, saleInfo);
						   }
					   }
				   }
			   }
		   }
		  
	    //拼接商品类别销量
		  //订单销量
		  List<SelectSaleViewResonse> incomeCountList = new ArrayList();
		  incomeCountList = orderNoCodeDao.getIncomeCountGroupByTypeId(distributorId,beginDate,endDate);
		  //订单的退货量
		  List<SelectSaleViewResonse> returnCountList = new ArrayList();
		  returnCountList = orderNoCodeDao.getReturnCountGroupByTypeId(distributorId,beginDate,endDate);
          
		  if(incomeCountList != null && incomeCountList.size() >0) {
			   if(returnCountList == null || returnCountList.size()<=0) {

			   }else {
				   for(SelectSaleViewResonse info : returnCountList) {
					   String typeId = info.getTypeId();
					   Integer returnCount = info.getCount();
					   for(int i = 0;i<incomeCountList.size();i++) {
						   SelectSaleViewResonse saleInfo = new SelectSaleViewResonse();
						   saleInfo = incomeCountList.get(i);
						   if(saleInfo.getTypeId().equals(typeId)) {
							   //销量
							   saleInfo.setCount(saleInfo.getCount().intValue()-returnCount.intValue());
							   incomeCountList.set(i, saleInfo);
						   }
					   }
				   }
			   }
		   }
		  

	     //组装商品类别销售额
			if(IncomePriceList !=null && IncomePriceList.size()>0){
			    for(SelectSaleViewResonse priceInfo : IncomePriceList) {
			    	String typeId = priceInfo.getTypeId();
			    	for(int i=0;i<list.size();i++) {
			    		SelectSaleViewResonse info = new SelectSaleViewResonse();
			    		info = list.get(i);
			    		if(info.getTypeId().equals(typeId)) {
			    			info.setPrice(priceInfo.getPrice());
			    			list.set(i, info);
			    		}
			    	}
		        }	
			}
			
		  //组装商品类别客流量
			if(incomeCountList !=null && incomeCountList.size()>0){
			    for(SelectSaleViewResonse priceInfo : incomeCountList) {
			    	String typeId = priceInfo.getTypeId();
			    	for(int i=0;i<list.size();i++) {
			    		SelectSaleViewResonse info = new SelectSaleViewResonse();
			    		info = list.get(i);
			    		if(info.getTypeId().equals(typeId)) {
			    			info.setCount(priceInfo.getCount());
			    			list.set(i, info);
			    		}
			    	}
		        }	
			}
			
		   //组装商品类别销量
			if(incomeCountList !=null && incomeCountList.size()>0){
			    for(SelectSaleViewResonse priceInfo : incomeCountList) {
			    	String typeId = priceInfo.getTypeId();
			    	for(int i=0;i<list.size();i++) {
			    		SelectSaleViewResonse info = new SelectSaleViewResonse();
			    		info = list.get(i);
			    		if(info.getTypeId().equals(typeId)) {
			    			info.setCount(priceInfo.getCount());
			    			list.set(i, info);
			    		}
			    	}
		        }	
			}
			
			
			//客流量
			if(list !=null && list.size()>0) {
				for(int i=0;i<list.size();i++) {
					SelectSaleViewResonse info = new SelectSaleViewResonse();
					info = list.get(i);
					passengerFlow = orderNoCodeDao.getPassengerFlowGroupByTypeId(distributorId,beginDate,endDate,info.getTypeId());
					info.setPassengerFlow(passengerFlow);
					list.set(i, info);
				}
			}
			
			
			//有码商品的销售金额
			Integer codeOrderPrice = null;  
			try {
				codeOrderPrice = orderDao.selectDistributorMonth(distributorId,beginDate,endDate);
				
				if(codeOrderPrice !=null && codeOrderPrice != 0) {
					SelectSaleViewResonse info = new SelectSaleViewResonse();
					info.setTypeId("000000");
					info.setTypeName("有码商品");
					info.setPrice(codeOrderPrice);
					info.setCount(0);
					info.setPassengerFlow(0);
					list.add(info);
				}
			} catch (Exception e) {
				LOGGER.info("有码商品的销售金额查询异常");
				return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
			}
			
			
			
		return HttpResponse.success(list);
	}


	//订单列表
	@Override
	public HttpResponse<List<OrderInfo>> selectNoCodeList(@Valid OrderNoCodeRequest orderNoCodeBuyRequest) {
		
		//购买.退货
		try {
			Integer orderFlow = null;
            if(orderNoCodeBuyRequest !=null) {
            	 if(orderNoCodeBuyRequest.getOrderFlow() !=null) {
            		 orderFlow = orderNoCodeBuyRequest.getOrderFlow();
            	 }else {
            		 orderFlow = 0;
            	 }
            }
			
			//返回结果
			List<OrderNoCodeInfo> list = new ArrayList();
			
			//分页
			Integer totalCount = 0;
			
			if(orderFlow != Global.ORDER_FLOW_2) {
				//购买订单列表
				List<OrderNoCodeInfo> buyList = new ArrayList();
				buyList = orderNoCodeDao.selectNoCodeOrderList(orderNoCodeBuyRequest);
				if(buyList !=null && buyList.size()>0) {
					for(OrderNoCodeInfo info : buyList) {
						list.add(info);
					}	
				}
				//订单列表总数据条数
				totalCount += orderNoCodeDao.selectNoCodeOrderListCount(orderNoCodeBuyRequest);
			}
			if(orderFlow != Global.ORDER_FLOW_1) {
			    //退货订单列表
				List<OrderNoCodeInfo> returnList = new ArrayList();
				returnList = orderNoCodeDao.selectNoCodeReturnList(orderNoCodeBuyRequest);	
				if(returnList !=null && returnList.size()>0) {
					for(OrderNoCodeInfo info : returnList) {
						list.add(info);
					}	
				}
				//退货订单列表总数据条数
				totalCount += orderNoCodeDao.selectNoCodeReturnListCount(orderNoCodeBuyRequest);
			}
			
			return HttpResponse.success(new PageResData(totalCount,list));
	
		} catch (Exception e) {
			LOGGER.info("订单列表查询异常",e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
		
	}


	//编号查询订单.
	@Override
	public HttpResponse selectorderByCode(@Valid String orderCode) {
		
		//返回数据
		OrderodrInfo info = new OrderodrInfo();
		
		//查询条件
		OrderDetailQuery orderDetailQuery = new OrderDetailQuery();
		orderDetailQuery.setOrderCode(orderCode);
		//服务商品
		orderDetailQuery.setOrderType(Global.ORDER_TYPE_3);
		
		//组装订单主数据
		OrderInfo orderInfo = new OrderInfo();
		try {
			orderInfo = orderDao.selecOrderById(orderDetailQuery);
			if(orderInfo !=null && orderInfo.getOrderId() !=null ) {
				orderDetailQuery.setOrderId(orderInfo.getOrderId());
			}else {
				return HttpResponse.success(null);
			}
			info.setOrderInfo(orderInfo);
		} catch (Exception e) {
			LOGGER.error("查询BYorderid-返回订单主数据",e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
		
		
		//组装订单明细数据
		try {
	    List<OrderDetailInfo> detailList = orderDetailDao.selectDetailById(orderDetailQuery);
		
	        if(detailList !=null && detailList.size()>0) {
	    	    info.setDetailList(detailList);
	        }
		} catch (Exception e) {
			LOGGER.error("组装订单明细数据失败",e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}	
		
		//组装订单结算数据
		try {
	    OrderQuery orderQuery = new OrderQuery();
	    orderQuery.setOrderId(orderInfo.getOrderId());
		SettlementInfo settlementInfo = settlementDao.jkselectsettlement(orderQuery);
	        if(settlementInfo !=null) {
	    	    info.setSettlementInfo(settlementInfo);
	        }
	        
		} catch (Exception e) {
			LOGGER.error("组装订单结算数据失败",e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}	
		
		//组装订单支付数据
		try {
		OrderPayInfo orderPayInfo = new OrderPayInfo();
		orderPayInfo.setOrderId(orderInfo.getOrderId());
		List<OrderPayInfo> payList = orderPayDao.pay(orderPayInfo);
	        if(payList !=null && payList.size()>0) {
	    	    info.setPayList(payList);
	        }		
			
	        return HttpResponse.success(info);
	        
		} catch (Exception e) {
			LOGGER.error("组装订单支付数据失败",e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}	
	}
}


