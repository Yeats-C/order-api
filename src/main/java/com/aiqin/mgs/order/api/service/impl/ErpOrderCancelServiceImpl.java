package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.OrderTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ProductTypeEnum;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;
import com.aiqin.mgs.order.api.service.ErpOrderCancelService;
import com.aiqin.mgs.order.api.service.ErpOrderOperationService;
import com.aiqin.mgs.order.api.service.ErpOrderQueryService;
import com.aiqin.mgs.order.api.service.ErpOrderRequestService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ErpOrderCancelServiceImpl implements ErpOrderCancelService {

    @Resource
    private ErpOrderQueryService erpOrderQueryService;
    @Resource
    private ErpOrderOperationService erpOrderOperationService;
    @Resource
    private ErpOrderRequestService erpOrderRequestService;

    @Override
    public void cancelOrderWithoutStock(OrderStoreOrderInfo orderStoreOrderInfo) {

        if (orderStoreOrderInfo == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(orderStoreOrderInfo.getOrderCode())) {
            throw new BusinessException("缺失订单编号");
        }
        OrderStoreOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderStoreOrderInfo.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }
        if (!ErpOrderStatusEnum.ORDER_STATUS_6.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException(ErpOrderStatusEnum.getEnumDesc(order.getOrderStatus()) + "的订单不支持该操作");
        }

        //修改订单状态为缺货终止交易
        order.setBeforeCancelStatus(order.getOrderStatus());
        order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_97.getCode());
        erpOrderOperationService.updateOrderByPrimaryKeySelective(order, AuthUtil.getCurrentAuth());

        //TODO 返还物流券 优惠券
        //TODO 解锁库存
        //TODO 生成退款单
    }

    @Override
    public void cancelOrderRejectSign(OrderStoreOrderInfo orderStoreOrderInfo) {

        if (orderStoreOrderInfo == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(orderStoreOrderInfo.getOrderCode())) {
            throw new BusinessException("缺失订单编号");
        }
        OrderStoreOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderStoreOrderInfo.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }
        OrderTypeEnum orderTypeEnum = OrderTypeEnum.getEnum(order.getOrderType());
        if (orderTypeEnum == null) {
            throw new BusinessException("订单类型异常");
        }
        if (orderTypeEnum.getProductTypeEnum()== ProductTypeEnum.DISTRIBUTION) {
            //配送订单
            if (!ErpOrderStatusEnum.ORDER_STATUS_12.getCode().equals(order.getOrderStatus())) {
                throw new BusinessException(ErpOrderStatusEnum.getEnumDesc(order.getOrderStatus()) + "的订单不支持该操作");
            }
        } else {
            if (!ErpOrderStatusEnum.ORDER_STATUS_11.getCode().equals(order.getOrderStatus())) {
                throw new BusinessException(ErpOrderStatusEnum.getEnumDesc(order.getOrderStatus()) + "的订单不支持该操作");
            }
        }

        //修改订单状态为拒收终止交易
        order.setBeforeCancelStatus(order.getOrderStatus());
        order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_96.getCode());
        erpOrderOperationService.updateOrderByPrimaryKeySelective(order, AuthUtil.getCurrentAuth());

        //TODO 返还物流券 优惠券
        //TODO 解锁库存
        //TODO 发起退货退款

    }

    @Override
    public void applyCancelOrder(OrderStoreOrderInfo orderStoreOrderInfo) {

        if (orderStoreOrderInfo == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(orderStoreOrderInfo.getOrderCode())) {
            throw new BusinessException("缺失订单编号");
        }
        OrderStoreOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderStoreOrderInfo.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }

        ErpOrderStatusEnum orderStatusEnum = ErpOrderStatusEnum.getEnum(order.getOrderCode());
        if (ErpOrderStatusEnum.ORDER_STATUS_1 == orderStatusEnum) {
            //取消、解锁库存
        }



    }

}
