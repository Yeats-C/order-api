/*****************************************************************

* 模块名称：订单售后后台-实现层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.CartDao;
import com.aiqin.mgs.order.api.dao.OrderAfterDao;
import com.aiqin.mgs.order.api.dao.OrderAfterDetailDao;
import com.aiqin.mgs.order.api.dao.OrderDao;
import com.aiqin.mgs.order.api.dao.OrderDetailDao;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.OrderAfteIListInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleQuery;
import com.aiqin.mgs.order.api.domain.OrderDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailQuery;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.OrderLog;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.response.OrderJoinResponse;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.OrderAfterDetailService;
import com.aiqin.mgs.order.api.service.OrderAfterService;
import com.aiqin.mgs.order.api.service.OrderDetailService;
import com.aiqin.mgs.order.api.service.OrderLogService;
import com.aiqin.mgs.order.api.service.OrderService;
import com.aiqin.mgs.order.api.util.OrderPublic;

@SuppressWarnings("all")
@Service
public class OrderServiceAfterImpl implements OrderAfterService{

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceAfterImpl.class);
	
	
	@Resource
    private OrderAfterDao orderAfterDao;
	
	@Resource
    private OrderDetailDao orderDetailDao;
	
	@Resource
    private OrderAfterDetailDao orderAfterDetailDao;
		
	@Resource
    private OrderDao orderDao;
	
	@Resource
    private OrderAfterDetailService orderAfterDetailService;
	
	@Resource
    private OrderService orderService;
	
	@Resource
    private OrderDetailService orderDetailService;
	
	@Resource
    private OrderLogService orderLogService;
	
	
	//支持-条件查询售后维权列表 /条件查询退货信息 分页
	@Override
	public HttpResponse selectOrderAfter(@Valid OrderAfterSaleQuery orderAfterSaleQuery) {
		
		List<OrderAfterSaleInfo> OrderAfterSaleInfolist;
		try {
			
			OrderAfterSaleInfolist = orderAfterDao.selectOrderAfter(OrderPublic.getOrderAfterSaleQuery(orderAfterSaleQuery));

			return HttpResponse.success(OrderPublic.getData(OrderAfterSaleInfolist));
		} catch (Exception e) {
			
			LOGGER.info("条件查询售后维权列表 /条件查询退货信息報錯", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}

	
	//添加新的订单售后数据+订单售后明细数据+修改订单表+修改订单明细表
	@Override
	@Transactional
	public HttpResponse addAfterOrder(@Valid OrderAfterSaleInfo orderAfterSaleInfo) {
		
		String afterSaleId = "";
		String afterSaleCode = "";
		try {
			//生成订单售后ID
			afterSaleId = OrderPublic.getUUID();
			orderAfterSaleInfo.setAfterSaleId(afterSaleId);
			
			//生成订单售后编号
			String logo = "";
			if(orderAfterSaleInfo.getOriginType() == Global.ORIGIN_TYPE_0) {
				logo = Global.ORIGIN_COME_3;
			}else {
				logo = Global.ORIGIN_COME_4;
			}
			afterSaleCode = OrderPublic.currentDate()+logo+String.valueOf(Global.ORDERID_CHANNEL_4)+OrderPublic.randomNumberF();
			orderAfterSaleInfo.setAfterSaleCode(afterSaleCode);
			
			//保存订单售后
			orderAfterDao.addAfterOrder(orderAfterSaleInfo);
			
			//保存订单售后明细
			List<OrderAfterSaleDetailInfo> orderAfterDetailList = orderAfterSaleInfo.getDetailList();
			orderAfterDetailService.addAfterOrderDetail(orderAfterDetailList,afterSaleId);   
			
			
			//修改订单表
			String orderId = "";
			Integer returnStatus;
			String updateBy = "";
			orderId = orderAfterSaleInfo.getOrderId();
			returnStatus = Global.IS_RETURN_1;
			updateBy = orderAfterSaleInfo.getCreateBy();
			
			orderService.retustus(orderId,returnStatus,updateBy);
			
			
			//修改订单表明细表
			if(orderAfterDetailList !=null) {
				for(OrderAfterSaleDetailInfo info :orderAfterDetailList) {
					
					String orderDetailId = info.getOrderDetailId();
					Integer returnAmount = info.getReturnAmount();
					
					OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
					updateBy = info.getCreateBy();
					
					//是否已有退货如果存在退货  
					orderDetailInfo = orderDetailDao.setail(orderDetailInfo);
					
					//已退数量+现退数量 = 正确的退货数量
					if(orderDetailInfo !=null ) {
						returnAmount = orderDetailInfo.getReturnAmount()+returnAmount;
						orderDetailInfo.setReturnAmount(returnAmount);
					}else {
						orderDetailInfo.setReturnAmount(returnAmount);
					}
					orderDetailInfo.setOrderDetailId(orderDetailId);
					orderDetailInfo.setReturnStatus(returnStatus);
					orderDetailInfo.setUpdateBy(updateBy);
					
					//更新数据
					orderDetailService.returnStatus(orderDetailId,returnStatus,returnAmount,updateBy);
				}
			}
			
			//生成订单日志
			OrderLog rderLog = OrderPublic.addOrderLog(afterSaleId,Global.STATUS_2,"OrderServiceAfterImpl.addAfterOrder()",
			OrderPublic.getStatus(Global.STATUS_2,returnStatus),updateBy);
			
			orderLogService.addOrderLog(rderLog);
			
			//返回售后编号
			String after_sale_code = afterSaleId;
			return HttpResponse.success(after_sale_code);
		
		} catch (Exception e) {
			LOGGER.info("添加新的订单售后数据報錯", e);
			return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
		}
	}

	
	//查询BYorderid-返回订单订单数据、退货数据、退货明细数据
	@Override
	public HttpResponse selectDetail(@Valid String afterSaleId) {
		
		//查询条件
		OrderAfterSaleQuery orderAfterSaleQuery = new OrderAfterSaleQuery();
		orderAfterSaleQuery.setAfterSaleId(afterSaleId);
		
		OrderDetailQuery orderDetailQuery = new OrderDetailQuery();
		
		//返回值
		OrderJoinResponse orderJoinResponse = new OrderJoinResponse();
		
		try {
			//组装退货主数据
			OrderAfterSaleInfo orderAfterSaleInfo = new OrderAfterSaleInfo();
			orderAfterSaleInfo = orderAfterDao.selectOrderAfterById(orderAfterSaleQuery);
			
			//组装退货明细数据
			List<OrderAfterSaleDetailInfo> saleDetailInfoList = orderAfterDetailDao.selectDetailbyId(orderAfterSaleQuery);
			if(saleDetailInfoList !=null && saleDetailInfoList.size()>0) {
				orderAfterSaleInfo.setDetailList(saleDetailInfoList);
			}
			orderJoinResponse.setOrderaftersaleinfo(orderAfterSaleInfo);
			
			//组装订单数据
			String order_id ="";
			if(orderAfterSaleInfo !=null && orderAfterSaleInfo.getOrderId() !=null && !orderAfterSaleInfo.getOrderId().equals("")) {
				order_id = orderAfterSaleInfo.getOrderId();
			}
			orderDetailQuery.setOrderId(order_id);
			OrderInfo orderInfo = orderDao.selecOrderById(orderDetailQuery);
			if(orderInfo !=null) {
				orderJoinResponse.setOrderInfo(orderInfo);
			}

			return HttpResponse.success(orderJoinResponse);
			
		} catch (Exception e) {
			
			LOGGER.info("查询BYorderid-返回订单订单数据、退货数据、退货明细数据報錯", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
		
	}


//	//更改退货状态(售后明细表+售后表+订单表+订单明细表)
//	@Override
//	@Transactional
//	public HttpResponse returus(@Valid String afterSaleId, Integer afterSaleStatus, String updateBy) {
//		
//		try {
//		   
//			//修改售后表的退货状态
//		    OrderAfterSaleInfo orderAfterSaleInfo = new OrderAfterSaleInfo();
//		    orderAfterSaleInfo.setAfterSaleId(afterSaleId);
//		    orderAfterSaleInfo.setAfterSaleStatus(afterSaleStatus);
//		    orderAfterSaleInfo.setUpdateBy(updateBy);
//			orderAfterDao.returnStatus(orderAfterSaleInfo);
//			
//			//修改订单明细表
//			  //修改退货数量
//			if(afterSaleStatus == Global.AFTER_SALE_STATUS_4 || afterSaleStatus == Global.AFTER_SALE_STATUS_5) {
//				
//				//查询订单售后明细获取数量
//				OrderAfterSaleQuery orderAfterSaleQuery = new OrderAfterSaleQuery();
//				orderAfterSaleQuery.setAfterSaleId(afterSaleId);
//				List<OrderAfterSaleDetailInfo> saleDetailInfoList = orderAfterDetailDao.selectDetailbyId(orderAfterSaleQuery);
//				for(OrderAfterSaleDetailInfo orderAfterSaleDetailInfo :saleDetailInfoList) {
//					
//					
//				//退货数量 = 已退货数量 - 取消退货数量
//					//订单售后明细表 需取消退货数量
//					String orderDetailId = "";
//					int returnAmount = 0; //取消退货数量
//					if(orderAfterSaleDetailInfo.getOrderDetailId() !=null ) {
//						orderDetailId = orderAfterSaleDetailInfo.getOrderDetailId();
//					};
//					if(orderAfterSaleDetailInfo.getReturnAmount() !=null) {
//						returnAmount = orderAfterSaleDetailInfo.getReturnAmount();
//					}
//					
//					//订单明细 已退货数量
//					int amount = 0; //已退货数量
//					OrderDetailInfo orderDetailInfo  = new OrderDetailInfo();
//					orderDetailInfo.setOrderDetailId(orderDetailId);
//					orderDetailInfo = orderDetailDao.setail(orderDetailInfo);
//					amount = orderDetailInfo.getReturnAmount();
//					
//					//计算
//					int rightReturnAcount=0;
//					rightReturnAcount = amount-returnAmount;
//					orderDetailInfo.setReturnAmount(rightReturnAcount);
//					orderDetailInfo.setUpdateBy(updateBy);
//					if(rightReturnAcount ==0) {
//						orderDetailInfo.setReturnStatus(Global.IS_RETURN_0);
//					}
//					orderDetailDao.returnStatus(orderDetailInfo);
//				}
//			}
//			
//			
//			//修改订单表
//			   //判断售后表是否存在售后数据,如订单下所有售后ID状态均为4-已取消、5-已关闭 那么修改订单表中的退货状态
//			
//			
//			//生成订单日志
//			OrderLog rderLog = OrderPublic.addOrderLog(afterSaleId,Global.STATUS_2,"OrderServiceImpl.returnStatus()",
//			OrderPublic.getStatus(Global.STATUS_2,afterSaleStatus),updateBy);
//			orderLogService.addOrderLog(rderLog);
//			
//			return HttpResponse.success();
//		} catch (Exception e) {
//			LOGGER.info("个性化接口-通过ID更改售后表退货状态/修改员報錯", e);
//			return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
//		}
//	}
	
	//更改退货状态(售后表)
	@Override
	@Transactional
	public HttpResponse returus(@Valid String afterSaleId, Integer afterSaleStatus, String updateBy) {
		
		try {
		   
			//修改售后表的退货状态
		    OrderAfterSaleInfo orderAfterSaleInfo = new OrderAfterSaleInfo();
		    orderAfterSaleInfo.setAfterSaleId(afterSaleId);
		    orderAfterSaleInfo.setAfterSaleStatus(afterSaleStatus);
		    orderAfterSaleInfo.setUpdateBy(updateBy);
			orderAfterDao.returnStatus(orderAfterSaleInfo);
			
			//生成订单日志
			OrderLog rderLog = OrderPublic.addOrderLog(afterSaleId,Global.STATUS_2,"OrderServiceImpl.returnStatus()",
			OrderPublic.getStatus(Global.STATUS_2,afterSaleStatus),updateBy);
			orderLogService.addOrderLog(rderLog);
			
			return HttpResponse.success();
		} catch (Exception e) {
			LOGGER.info("更改退货状态(售后表+订单表)报错", e);
			return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
		}
	}
	

}
