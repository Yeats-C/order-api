/*****************************************************************

* 模块名称：订单售后后台-实现层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 
* 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.aiqin.mgs.order.api.domain.pay.PayReq;
import com.aiqin.mgs.order.api.service.*;
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
import com.aiqin.mgs.order.api.dao.OrderCouponDao;
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
import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.domain.OrderRelationCouponInfo;
import com.aiqin.mgs.order.api.domain.SettlementInfo;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.response.OrderJoinResponse;
import com.aiqin.mgs.order.api.domain.response.OrderNoCodeResponse.AddReturnOrderResonse;
import com.aiqin.mgs.order.api.util.DateUtil;
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
	
	@Resource
    private SettlementService settlementService;
	
	@Resource
    private OrderCouponDao orderCouponDao;
	
	@Resource
	private BridgePayService bridgePayService;
	
	
	//支持-条件查询售后维权列表 /条件查询退货信息 分页
	@Override
	public HttpResponse selectOrderAfter(@Valid OrderAfterSaleQuery orderAfterSaleQuery) {
		
		List<OrderAfterSaleInfo> OrderAfterSaleInfolist;
		try {
			
			OrderAfterSaleInfolist = orderAfterDao.selectOrderAfter(OrderPublic.getOrderAfterSaleQuery(orderAfterSaleQuery));

			//计算总数据量
			Integer totalCount = 0;
			Integer icount =null;
			orderAfterSaleQuery.setIcount(icount);
			List<OrderAfterSaleInfo> Icount_list= orderAfterDao.selectOrderAfter(OrderPublic.getOrderAfterSaleQuery(orderAfterSaleQuery));
			if(Icount_list !=null && Icount_list.size()>0) {
				totalCount = Icount_list.size();
			}
			
//			return HttpResponse.success(OrderPublic.getData(OrderAfterSaleInfolist));
			return HttpResponse.success(new PageResData(totalCount,OrderAfterSaleInfolist));
		} catch (Exception e) {
			
			LOGGER.error("条件查询售后维权列表 /条件查询退货信息报错 {}", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}

	
	//TOC订单-添加新的订单售后数据+订单售后明细数据+修改订单表+修改订单明细表
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
			}
			if(orderAfterSaleInfo.getOriginType() == Global.ORIGIN_TYPE_1) {
				logo = Global.ORIGIN_COME_4;
			}
			if(orderAfterSaleInfo.getOriginType() == Global.ORIGIN_TYPE_3) {
				logo = Global.ORIGIN_COME_5;
			}
			afterSaleCode = DateUtil.sysDate()+logo+String.valueOf(Global.ORDERID_CHANNEL_4)+OrderPublic.randomNumberF();
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
			//仅更改退货状态-订单主表
			orderService.retustus(orderId,returnStatus,updateBy);
			
			
			//修改订单表明细表
			if(orderAfterDetailList !=null && orderAfterDetailList.size()>0) {
				for(OrderAfterSaleDetailInfo info :orderAfterDetailList) {
					
					String orderDetailId = info.getOrderDetailId();
					Integer returnAmount = info.getReturnAmount();
					
					OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
					orderDetailInfo.setOrderDetailId(orderDetailId);
					orderDetailInfo.setReturnAmount(returnAmount);
					orderDetailInfo.setUpdateBy(updateBy);
					orderDetailInfo.setReturnStatus(returnStatus);
					
					//是否已有退货如果存在退货  
					if(orderDetailInfo !=null) {
						OrderDetailInfo detailInfo = new OrderDetailInfo();
						detailInfo = orderDetailDao.setail(orderDetailInfo);
						//已退数量+现退数量 = 正确的退货数量
						if(detailInfo !=null && detailInfo.getReturnAmount() !=null) {
							returnAmount = detailInfo.getReturnAmount()+returnAmount;
							orderDetailInfo.setReturnAmount(returnAmount);
						}
					}
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








			//调用支付中心退款
			toRefund(orderAfterSaleInfo);








			return HttpResponse.success(after_sale_code);
		
		} catch (Exception e) {
			LOGGER.error("添加新的订单售后数据报错 {}", e);
			return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
		}
	}

	private void toRefund(OrderAfterSaleInfo orderAfterSaleInfo) {
		PayReq payReq=new PayReq();
		payReq.setStoreId(orderAfterSaleInfo.getDistributorId());
		payReq.setStoreName(orderAfterSaleInfo.getDistributorName());
		payReq.setOrderNo(orderAfterSaleInfo.getOrderCode());
		payReq.setOrderSource(orderAfterSaleInfo.getOriginType());
		payReq.setOrderTime(orderAfterSaleInfo.getOrderTime());
		payReq.setPayType(orderAfterSaleInfo.getPayType());
		payReq.setRefundAmount(Long.valueOf(orderAfterSaleInfo.getReturnPrice()));
		payReq.setCreateBy(orderAfterSaleInfo.getCreateBy());
		payReq.setMemberId(orderAfterSaleInfo.getMemberId());
		payReq.setMemberName(orderAfterSaleInfo.getMemberName());
		payReq.setMemberPhone(orderAfterSaleInfo.getMemberPhone());
		payReq.setFranchiseeId(orderAfterSaleInfo.getFranchiseeId());
		payReq.setPayOrderType(orderAfterSaleInfo.getOrderType());
		payReq.setRefundType(orderAfterSaleInfo.getReturnMoneyType());
		bridgePayService.toRefund(payReq);
	}


	//服务商品-添加新的订单售后数据+订单售后明细数据+修改订单表+修改订单明细表
	@Override
	@Transactional
	public HttpResponse addAfterNoCodeOrder(@Valid OrderAfterSaleInfo orderAfterSaleInfo) {
		
		String afterSaleId = "";
		String afterSaleCode = "";
		
		//返回参数
		AddReturnOrderResonse addReturnOrderResonse = new AddReturnOrderResonse();
		
		try {
			//生成订单售后ID
			afterSaleId = OrderPublic.getUUID();
			orderAfterSaleInfo.setAfterSaleId(afterSaleId);
			
			//生成订单售后编号
			String logo = "";
			if(orderAfterSaleInfo.getOriginType() == Global.ORIGIN_TYPE_0) {
				logo = Global.ORIGIN_COME_3;
			}
			if(orderAfterSaleInfo.getOriginType() == Global.ORIGIN_TYPE_1) {
				logo = Global.ORIGIN_COME_4;
			}
			if(orderAfterSaleInfo.getOriginType() == Global.ORIGIN_TYPE_3) {
				logo = Global.ORIGIN_COME_5;
			}
			afterSaleCode = DateUtil.sysDate()+logo+String.valueOf(Global.ORDERID_CHANNEL_4)+OrderPublic.randomNumberF();
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
			if(orderAfterDetailList !=null && orderAfterDetailList.size()>0) {
				for(OrderAfterSaleDetailInfo info :orderAfterDetailList) {
					
					String orderDetailId = info.getOrderDetailId();
					Integer returnAmount = info.getReturnAmount();
					
					OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
					orderDetailInfo.setOrderDetailId(orderDetailId);
					orderDetailInfo.setReturnAmount(returnAmount);
					orderDetailInfo.setUpdateBy(updateBy);
					orderDetailInfo.setReturnStatus(returnStatus);
					
					//是否已有退货如果存在退货  
					if(orderDetailInfo !=null) {
						OrderDetailInfo detailInfo = new OrderDetailInfo();
						detailInfo = orderDetailDao.setail(orderDetailInfo);
						//已退数量+现退数量 = 正确的退货数量
						if(detailInfo !=null && detailInfo.getReturnAmount() !=null) {
							returnAmount = detailInfo.getReturnAmount()+returnAmount;
							orderDetailInfo.setReturnAmount(returnAmount);
						}
					}
					//更新数据
					orderDetailService.returnStatus(orderDetailId,returnStatus,returnAmount,updateBy);
					
				}
			}
			
			//生成订单日志
			OrderLog rderLog = OrderPublic.addOrderLog(afterSaleId,Global.STATUS_2,"OrderServiceAfterImpl.addAfterOrder()",
			OrderPublic.getStatus(Global.STATUS_2,returnStatus),updateBy);
			
			orderLogService.addOrderLog(rderLog);
			
			
			addReturnOrderResonse.setAfterSaleId(afterSaleId);
			addReturnOrderResonse.setAfterSaleCode(afterSaleCode);
			
			return HttpResponse.success(addReturnOrderResonse);
		
		} catch (Exception e) {
			LOGGER.error("添加新的订单售后数据报错 {}", e);
			return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
		}
	}

	
	//查询BYorderid-返回订单订单主数据、退货数据、退货明细数据、订单明细数据、优惠券数据
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
			
			if(orderAfterSaleInfo !=null) {
				//组装退货明细数据
				List<OrderAfterSaleDetailInfo> saleDetailInfoList = new ArrayList();
				saleDetailInfoList = orderAfterDetailDao.selectReturnDetailbyId(orderAfterSaleQuery);
				
				if(saleDetailInfoList !=null && saleDetailInfoList.size()>0) {
					for(int j=0;j<saleDetailInfoList.size();j++) {
						OrderAfterSaleDetailInfo info = new OrderAfterSaleDetailInfo();
						info = saleDetailInfoList.get(j);
						//将明细中的金额放入退货明细数据中
						OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
						orderDetailInfo.setOrderDetailId(info.getOrderDetailId());
						orderDetailInfo = orderDetailDao.setail(orderDetailInfo);
						if(orderDetailInfo !=null ) {
							info.setRetailPrice(orderDetailInfo.getRetailPrice());
							info.setActualPrice(orderDetailInfo.getActualPrice());
							info.setSpec(orderDetailInfo.getSpec());
							info.setUnit(orderDetailInfo.getUnit());
							info.setLogo(orderDetailInfo.getLogo());
						}
						saleDetailInfoList.set(j, info);
					}
					orderAfterSaleInfo.setDetailList(saleDetailInfoList);
					
				}
				
				orderJoinResponse.setOrderaftersaleinfo(orderAfterSaleInfo);	
			}
			
			if(orderAfterSaleInfo !=null) {
				
				//组装订单数据
				String orderId ="";
				OrderInfo orderInfo = new OrderInfo();
				orderId = orderAfterSaleInfo.getOrderId();
				orderDetailQuery.setOrderId(orderId);
				orderInfo = orderDao.selecOrderById(orderDetailQuery);
			
				//组装订单中的商品数量
				Integer skuSum = 0;
				if(orderInfo !=null ) {
					if(orderInfo.getOrderId() !=null && !orderInfo.getOrderId().equals("")) {
						skuSum = orderDetailService.getSkuSum(orderInfo.getOrderId());
					}
					if(skuSum !=null) {
						orderInfo.setSkuSum(skuSum);
					}
				}
				
				//组装订单明细数据
				List<OrderDetailInfo> orderDetailInfoList = orderDetailDao.selectDetailById(orderDetailQuery);
				
				//组装订单明细与优惠券的关系
				if(orderId !=null && !orderId.equals("")) {
					OrderRelationCouponInfo orderRelationCouponInfo = new OrderRelationCouponInfo();
					orderRelationCouponInfo.setOrderId(orderId);
					List<OrderRelationCouponInfo> couponInfolist = orderCouponDao.soupon(orderRelationCouponInfo);
					if(couponInfolist !=null && couponInfolist.size()>0) {
						for(OrderRelationCouponInfo couponInfo :couponInfolist) {
							if(couponInfo.getOrderDetailId() !=null && !couponInfo.getOrderDetailId().equals("")) {
								if(orderDetailInfoList !=null && orderDetailInfoList.size()>0) {
									for(int i=0;i<orderDetailInfoList.size();i++) {
										OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
										orderDetailInfo = orderDetailInfoList.get(i); 
										if(couponInfo.getOrderDetailId().equals(orderDetailInfo.getOrderDetailId())) {
											orderDetailInfo.setCouponInfo(couponInfo);
											orderDetailInfoList.set(i, orderDetailInfo);
										}
									}
								}
							}
						}	
					}
				}
				if(orderDetailInfoList !=null && orderDetailInfoList.size()>0) {
					orderInfo.setOrderdetailList(orderDetailInfoList);
				}
				if(orderInfo !=null) {
					orderJoinResponse.setOrderInfo(orderInfo);
					
					//20190401 返回结算信息
					OrderQuery orderQuery = new OrderQuery();
					orderQuery.setOrderId(orderId);
					SettlementInfo settlementInfo = new SettlementInfo();
					HttpResponse httpResponse = settlementService.jkselectsettlement(orderQuery);
					settlementInfo = (SettlementInfo) httpResponse.getData();
					if(settlementInfo !=null) {
						orderJoinResponse.setSettlementInfo(settlementInfo);
					}
				}
			}
			return HttpResponse.success(orderJoinResponse);
			
		} catch (Exception e) {
			
			LOGGER.error("查询BYorderid-返回订单订单数据、退货数据、退货明细数据报错 {}", e);
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
	public HttpResponse returus(@Valid String afterSaleId, Integer afterSaleStatus, String updateBy,String updateByName) {
		
		try {
		   
			//修改售后表的退货状态
		    OrderAfterSaleInfo orderAfterSaleInfo = new OrderAfterSaleInfo();
		    orderAfterSaleInfo.setAfterSaleId(afterSaleId);
		    orderAfterSaleInfo.setAfterSaleStatus(afterSaleStatus);
		    orderAfterSaleInfo.setUpdateBy(updateBy);
		    orderAfterSaleInfo.setUpdateByName(updateByName);
			orderAfterDao.returnStatus(orderAfterSaleInfo);
			
			//生成订单日志
			OrderLog rderLog = OrderPublic.addOrderLog(afterSaleId,Global.STATUS_2,"OrderServiceImpl.returnStatus()",
			OrderPublic.getStatus(Global.STATUS_2,afterSaleStatus),updateBy);
			orderLogService.addOrderLog(rderLog);
			
			return HttpResponse.success();
		} catch (Exception e) {
			LOGGER.error("更改退货状态(售后表+订单表)报错 {}", e);
			return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
		}
	}


	//模糊查询查询退货信息+退货明细+订单明细信息
	@Override
	public HttpResponse aftlst(@Valid OrderAfterSaleQuery orderAfterSaleQuery) {
		try {
			//组装退货信息
			List<OrderAfterSaleInfo> orderAfterSaleInfolist = new ArrayList();
			orderAfterSaleInfolist = orderAfterDao.selectOrderAfter(OrderPublic.getOrderAfterSaleQuery(orderAfterSaleQuery));
		   
			if(orderAfterSaleInfolist !=null && orderAfterSaleInfolist.size()>0) {
				for(int i=0;i<orderAfterSaleInfolist.size();i++) {
					OrderAfterSaleInfo orderAfterSaleInfo = new OrderAfterSaleInfo();
					orderAfterSaleInfo = orderAfterSaleInfolist.get(i);
					
					//封装查询条件
					OrderAfterSaleQuery orderAfterSaleQuery1 = new OrderAfterSaleQuery();
					orderAfterSaleQuery1.setAfterSaleId(orderAfterSaleInfo.getAfterSaleId());
					
					//组装退货明细数据
					List<OrderAfterSaleDetailInfo> saleDetailInfoList = orderAfterDetailDao.selectReturnDetailbyId(orderAfterSaleQuery1);
					
					if(saleDetailInfoList !=null && saleDetailInfoList.size()>0) {
						for(int j=0;j<saleDetailInfoList.size();j++) {
							OrderAfterSaleDetailInfo info = new OrderAfterSaleDetailInfo();
							info = saleDetailInfoList.get(j);
							//将明细中的金额放入退货明细数据中
							OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
							orderDetailInfo.setOrderDetailId(info.getOrderDetailId());
							orderDetailInfo = orderDetailDao.setail(orderDetailInfo);
							if(orderDetailInfo !=null ) {
								info.setRetailPrice(orderDetailInfo.getRetailPrice());
								info.setActualPrice(orderDetailInfo.getActualPrice());
								info.setSpec(orderDetailInfo.getSpec());
								info.setUnit(orderDetailInfo.getUnit());
								info.setLogo(orderDetailInfo.getLogo());
							}
							saleDetailInfoList.set(j, info);
						}
							orderAfterSaleInfo.setDetailList(saleDetailInfoList);
					}
					
//					//组装列表图放入退货明细
//					if(saleDetailInfoList !=null && saleDetailInfoList.size()>0) {
//						for(int j=0;j<saleDetailInfoList.size();j++) {
//							OrderAfterSaleDetailInfo orderAfterSaleDetailInfo = new OrderAfterSaleDetailInfo();
//							orderAfterSaleDetailInfo = saleDetailInfoList.get(i);
//							//封装查询条件
//							OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
//							orderDetailInfo.setOrderDetailId(orderAfterSaleDetailInfo.getOrderDetailId());
//							//查询订单明细-订单明细编码
//							orderDetailInfo = orderDetailDao.setail(orderDetailInfo);
//							orderAfterSaleDetailInfo.setLogo(orderDetailInfo.getLogo());
//							saleDetailInfoList.set(i, orderAfterSaleDetailInfo);
//						}
//					}
				}	
			}
			
			//计算总数据量
			Integer totalCount = 0;
			Integer icount =null;
			orderAfterSaleQuery.setIcount(icount);
			List<OrderAfterSaleInfo> IcountList= orderAfterDao.selectOrderAfter(OrderPublic.getOrderAfterSaleQuery(orderAfterSaleQuery));
			if(IcountList !=null && IcountList.size()>0) {
				totalCount = IcountList.size();
			}

			return HttpResponse.success(new PageResData(totalCount,orderAfterSaleInfolist));
			
		} catch (Exception e) {
			LOGGER.error("模糊查询查询退货信息+退货明细+订单明细信息报错 {}", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}
	

}
