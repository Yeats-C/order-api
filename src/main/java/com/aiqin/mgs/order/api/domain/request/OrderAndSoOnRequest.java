package com.aiqin.mgs.order.api.domain.request;

import com.aiqin.mgs.order.api.domain.OrderDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.OrderLog;
import com.aiqin.mgs.order.api.domain.OrderPayInfo;
import com.aiqin.mgs.order.api.domain.OrderRelationCouponInfo;
import com.aiqin.mgs.order.api.domain.SettlementInfo;
import io.swagger.annotations.Api;

import java.util.List;

@Api("新增订单")
public class OrderAndSoOnRequest {

	private OrderInfo orderInfo;
	
	private List<OrderDetailInfo> detailList;
	
	private SettlementInfo settlementInfo;
	
	private List<OrderPayInfo> OrderPayList;
	
	private List<OrderRelationCouponInfo> orderCouponList;
	
	private OrderLog logInfo;

	public OrderInfo getOrderInfo() {
		return orderInfo;
	}
	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}
	public List<OrderDetailInfo> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<OrderDetailInfo> detailList) {
		this.detailList = detailList;
	}
	public SettlementInfo getSettlementInfo() {
		return settlementInfo;
	}
	public void setSettlementInfo(SettlementInfo settlementInfo) {
		this.settlementInfo = settlementInfo;
	}
	public List<OrderPayInfo> getOrderPayList() {
		return OrderPayList;
	}
	public void setOrderPayList(List<OrderPayInfo> orderPayList) {
		OrderPayList = orderPayList;
	}
	public List<OrderRelationCouponInfo> getOrderCouponList() {
		return orderCouponList;
	}
	public void setOrderCouponList(List<OrderRelationCouponInfo> orderCouponList) {
		this.orderCouponList = orderCouponList;
	}
	public OrderLog getLogInfo() {
		return logInfo;
	}
	public void setLogInfo(OrderLog logInfo) {
		this.logInfo = logInfo;
	}

	
}
