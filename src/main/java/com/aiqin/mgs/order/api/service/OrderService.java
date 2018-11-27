/*****************************************************************

* 模块名称：订单后台-接口层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service;

import java.util.List;

import javax.validation.Valid;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailQuery;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.OrderLog;
import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.domain.OrderRelationCouponInfo;

@SuppressWarnings("all")
public interface OrderService {

	
	HttpResponse selectOrder(@Valid OrderQuery OrderQuery);

	HttpResponse addOrderInfo(@Valid OrderInfo orderInfo);

	HttpResponse addOrderLog(@Valid OrderLog logInfo);

	
	//添加新的订单优惠券关系表数据
	HttpResponse addOrderCoupon(@Valid List<OrderRelationCouponInfo> orderCouponList, @Valid String orderId);

	//接口-分销机构维度-总销售额 返回INT
	HttpResponse selectOrderAmt(@Valid String distributorId, String originType);

	//接口-分销机构+当月维度-当月销售额、当月实收、当月支付订单数
	HttpResponse selectorderbymonth(@Valid String distributorId, String originType);


}
