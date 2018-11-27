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
import com.aiqin.mgs.order.api.service.SettlementService;
import com.aiqin.mgs.order.api.util.HttpRequest;
import com.aiqin.mgs.order.api.util.OrderPublic;
import com.aiqin.mgs.order.api.util.Signature;
import com.aiqin.mgs.order.api.util.Xiadan;
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
	
//	@Resource
//    private CartDao cartDao;
//	
//	@Resource
//    private OrderDao orderDao;
	
	
	@Override
	public HttpResponse jkselectsettlement(@Valid OrderQuery orderQuery) {
		
		return HttpResponse.success(settlementDao.jkselectsettlement(orderQuery));
	}


    //添加新的结算数据
	@Override
	public HttpResponse addSettlement(@Valid SettlementInfo settlementInfo, @Valid String orderId) {

		
		if(settlementInfo !=null) {
			   settlementInfo.setOrderId(orderId);
			   settlementInfo.setSettlement_id(OrderPublic.getUUID());
			   try {
				settlementDao.addSettlement(settlementInfo);
			} catch (Exception e) {
				LOGGER.info("添加新的结算数据失败......", e);
				return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
			}
		}
		return HttpResponse.success();
	}

    //添加新的支付数据
	@Override
	public HttpResponse addOrderPayList(@Valid List<OrderPayInfo> orderPayList, @Valid String orderId) {
		
		if(orderPayList !=null && orderPayList.size()>0) {
			for(OrderPayInfo info :orderPayList) {
				
				//订单ID,支付ID
				info.setOrderId(orderId);
				info.setPayId(OrderPublic.getUUID());
				
				try {
					orderPayDao.orderPayList(info);
				} catch (Exception e) {
					LOGGER.info("添加新的支付数据失败......", e);
					return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
				}
			}
		}
		return HttpResponse.success();
	}
	

//	@Override
//	public HttpResponse selectSettlement(List<CartInfo> cartInfolist) {
//		
//		
//		LOGGER.info("查询結算頁面列表", cartInfolist);
//		
//		SettlementInfo info = new SettlementInfo();
//		
//		if(cartInfolist !=null) {
//			
//			//将购物车信息传递给結算
//			info = getSettlementInfo(info,cartInfolist); 
//		}
//		
//		
//		return HttpResponse.success(info);
//	}





//    private SettlementInfo getSettlementInfo(SettlementInfo info,List<CartInfo> cartInfolist) {
//		
//		
//        CartInfo cartInfo = cartInfolist.get(0); 
//            info.setProductSum(cartInfo.getAcountAmount());
//            info.setFreight(0);//一期
//            
//            //一期
//            if(cartInfo.getAgioType().equals(Global.AGIOTYPE_1)) {
//            	info.setActivityDiscount(cartInfo.getActivityDiscount());	 //活动优惠
//            }else if(cartInfo.getAgioType().equals(Global.AGIOTYPE_2)){}
//            else {
//            	info.setTotalCouponsDiscount(cartInfo.getActivityDiscount());//优惠券总优惠金额
//            }
//              
//            
//            info.setOrderSum(cartInfo.getAcountTotalprice());	//订单合计=应付金额总和
//            info.setOrderReceivable(cartInfo.getAcountActualprice());	//订单应收=实付金额总和
//            info.setOrderActual(cartInfo.getAcountActualprice());	//订单实收
//            info.setOrderChange(0);	//订单找零
//            
//		return info;
//	}





//	@Override
//	public HttpResponse settlement(SettlementInfo info,List<CartInfo> cartInfoList,OrderQuery conditionInfo) {
//		
//		if(info !=null ) {
//			
//		}else {
//			//未完成    无结算信息 参数异常 
//		}
//		
//		//生成订单号
////		OrderPublic.currentDate()+String.valueOf(info.getOriginType())+String.valueOf(Global.ORDERID_channel_4)+OrderPublic.randomNumberF();
//		String orderCode = "";
//		
////		//现金支付
////		if(info.getPayType() !=null && info.getPayType().equals(Global.PAY_TYPE_2)) {
////			
//			//结算信息 存库
//			info.setOrderId(orderCode);
//			settlementDao.insertSettlement(info);
//					    
//			//支付信息 存库
//			settlementDao.insertOrderPay(getPayInfo(orderCode,info,conditionInfo));
//
//			
////		    //优惠关系 存库 
////			
////			未完成
//	
////			//订单信息 存库
////			orderDao.insertOrder(getOrderInfo(orderCode,info,conditionInfo));
////			
////			
////			//已支付订单明细 存库
////			OrderDetailInfo  orderDetailInfo = orderDao.selectOrderSKUId("会员号");
////			orderDao.insertOrderDetail(orderDetailInfo);
////			
////			
////			
//			//删除购物车信息 (针对微商城)未完成
//			if(conditionInfo.getOriginType().equals(Global.ORIGIN_TYPE_4)) {
//				String memberId = "";
//				try {
//					cartDao.deleteCartInfo(conditionInfo.getMemberId());
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			
//			//插入 日志  未完成 依赖
//			orderDao.inserOrderLog(orderCode);
//		}else {
			
			//调用第三方支付
			
//			try {
//				
//				Xiadan(info,request,response);
//				
//			} catch (ServletException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			
//			if(1 != 1) {
//				//失败 
//				//应该生成待支付订单.
//			}else {
//				//成功
//				//结算信息 存库
//				//支付信息 存库
//			    //优惠关系 存库
//				//已支付订单 存库
//				//删除购物车信息
//				//插入 日志
//			}
			
//		}
//		return null;
//	}

//	/**
//	 * set值-订单表
//	 * @param orderId
//	 * @param info
//	 * @return
//	 */
//	private OrderInfo getOrderInfo(String orderCode, SettlementInfo info,OrderQuery conditionInfo) {
//		
//		OrderInfo orderInfo = new OrderInfo();
//		
//		
//		orderInfo.setOrderId("orderId"); //未完成
//		orderInfo.setOrderCode(orderCode);
//		orderInfo.setMemberId(conditionInfo.getMemberId());
//		orderInfo.setMemberName(conditionInfo.getMemberName());
//		orderInfo.setMemberPhone(conditionInfo.getMemberPhone());
//		orderInfo.setDistributorId(conditionInfo.getDistributorId());
//		orderInfo.setDistributorCode(conditionInfo.getDistributorCode());
//		orderInfo.setDistributorName(conditionInfo.getDistributorName());
//		orderInfo.setOriginType(conditionInfo.getOriginType());
//		orderInfo.setReceiveType(Global.RECEIVE_TYPE_0);//一期 ;二期需要调整
//		
//		//未完成  门店-现金：已完成 ；其他需根据第三方支付结果赋值       订单状态
//		if(info.getPayType().equals(Global.PAY_TYPE_2)) {
//			orderInfo.setOrderStatus(Global.ORDER_STATUS_5);
//		}else {
//			
//		}
//		orderInfo.setReturnStatus(Global.RETURN_STATUS_4);
//		orderInfo.setPayStatus(Global.PAY_STATUS_1); //未完成、 还需现金判断以及是否第三方支付成功
//		orderInfo.setPayType(info.getPayType());
//		orderInfo.setActualPrice(info.getOrderActual());
//		orderInfo.setTotalPrice(info.getOrderReceivable());
//		orderInfo.setCustomNote("未完成");
//		orderInfo.setBusinessNote("未完成");
//		orderInfo.setCashierId(conditionInfo.getCashierId());
//		orderInfo.setCashierName(conditionInfo.getCashierName());
//		orderInfo.setGuideId(conditionInfo.getGuideId());
//		orderInfo.setGuideName(conditionInfo.getGuideName());
////		orderInfo.setCreateTime();
////		orderInfo.setUpdateTime();
////		orderInfo.setCreateBy();
////		orderInfo.setUpdateBy();
//
//		return orderInfo;
//	}


//	/**
//	 * set值-订单支付表
//	 * @param orderId
//	 * @param info
//	 * @return
//	 */
//	private OrderPayInfo getPayInfo(String orderCode,SettlementInfo info,OrderQuery conditionInfo) {
//		
//		OrderPayInfo payInfo = new OrderPayInfo();
//		payInfo.setOrderId(orderCode);
//		payInfo.setPayType(getPayType(String.valueOf(conditionInfo.getOriginType()),String.valueOf(info.getPayType())));
//		payInfo.setPayName("未完成 支付描述");
//		payInfo.setPayStatus(Global.PAY_STATUS_1);   //未完成  最后如果在现金支付的判断中,那可以写死.
//		payInfo.setPayPrice(info.getOrderActual());
//		
//		return payInfo;
//	}


//	/**
//	 * 拼接支付类型 0-在线支付-微信、1-在线支付-支付宝  2-在线支付-银联、
//	 *         3-到店支付-现金支付、4-到店支付-微信、5-到店支付-支付宝、6-到店支付-银联
//	 * @param originType
//	 * @param payType
//	 * @return
//	 */
//	private Integer getPayType(String originType, String payType) {
//		
//		Integer ary= Global.P_TYPE_3;
//		if(originType.equals(Global.ORIGIN_TYPE_3)) {
//		    Integer PAY_TYPE_0 = 0;  //微信
//		    Integer PAY_TYPE_1 = 1;  //支付宝
//		    Integer PAY_TYPE_2 = 2;  //现金
//		    Integer PAY_TYPE_3 = 3;  //银联
//			if(payType.equals(Global.PAY_TYPE_0)) {
//				ary= Global.P_TYPE_0;
//			}else if(payType.equals(Global.PAY_TYPE_1)) {
//				ary= Global.P_TYPE_1;
//			}else if(payType.equals(Global.PAY_TYPE_3)) {
//				ary= Global.P_TYPE_2;
//			}else {
//				ary= Global.P_TYPE_0;
//			}
//		}else if(originType.equals(Global.ORIGIN_TYPE_4)) {
//			if(payType.equals(Global.PAY_TYPE_0)) {
//				ary= Global.P_TYPE_4;
//			}else if(payType.equals(Global.PAY_TYPE_1)) {
//				ary= Global.P_TYPE_5;
//			}else if(payType.equals(Global.PAY_TYPE_2)) {
//				ary= Global.P_TYPE_3;
//			}
//			else if(payType.equals(Global.PAY_TYPE_3)) {
//				ary= Global.P_TYPE_6;
//			}else {
//				ary= Global.P_TYPE_3;
//			}
//		}else {
//			
//		}
//		return ary;
//	}





//	@Override
//	public HttpResponse ajaxsettlement(@Valid SettlementInfo settlementInfo) {
//		
//		//输入现金金额
//		if(settlementInfo.getInputAmt() !=null) {
//			settlementInfo.getOrderReceivable();  //订单应收
//			settlementInfo.getOrderActual();      //订单实收
//			
//			//订单找零
//			settlementInfo.setOrderChange(settlementInfo.getInputAmt() - settlementInfo.getOrderActual()); 
//		}
//		
//		//输入积分
//		if(settlementInfo.getInputPoints() !=null) {
//			
//			settlementInfo.setPointPercentage(settlementInfo.getInputPoints()/100);
//			
//		}
//		
//		//舍零
//		if(settlementInfo.getIsAbandon() !=null) {
//			settlementInfo.getOrderReceivable();
//			
//			//未完成
////			settlementInfo.setOrderReceivable();
////			settlementInfo.setOrderActual();
//		}
//		
//		return HttpResponse.success(settlementInfo);
//	}


//	private void Xiadan(SettlementInfo info, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
//
//		PaymentInfo  paymentInfo= new PaymentInfo();
//		SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
//		String characterEncoding = "UTF-8";         //指定字符集UTF-8
//		parameters.put("mchId", paymentInfo.getMchId());
//		parameters.put("appId", paymentInfo.getAppId());
//		parameters.put("passageId", paymentInfo.getPassageId());
//		parameters.put("mchOrderNo", info.getOrderId());
//		parameters.put("channelId", paymentInfo.getChannelId());
//		parameters.put("currency", paymentInfo.getCurrency());
//		parameters.put("amount", info.getOrderActual());
//		parameters.put("clientIp", "127.0.0.1");
//		parameters.put("device", "pc");
//		parameters.put("notifyUrl", paymentInfo.getNotifyUrl());
//		parameters.put("subject", "商品主题");
//		parameters.put("body", "商品描述信息");
//		parameters.put("param1", "");
//		parameters.put("param2", "");
//		parameters.put("extra","");
//		String mySign = Signature.createSign(characterEncoding,parameters,paymentInfo.getApikey());
//		parameters.put("sign", mySign);
//		
//		String result;
//		try {
//			result = HttpRequest.sendPost("http://mch.t.fpay.org/api/pay/create_order", parameters);
//
//		System.out.println("parameters====="+parameters);
//		System.out.println("result===="+result);
//		System.out.println("---------下单返回:"+result);
//		
//		response.getWriter().append(result);
//		
//		} catch (UnrecoverableKeyException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (KeyManagementException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (KeyStoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	

}
