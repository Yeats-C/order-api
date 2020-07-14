package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderNodeProcessTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderNodeStatusEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpRequestPayTransactionTypeEnum;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderCancelRequest;
import com.aiqin.mgs.order.api.service.order.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ErpOrderCancelNoTransactionalServiceImpl implements ErpOrderCancelNoTransactionalService {

    @Resource
    private ErpOrderQueryService erpOrderQueryService;
    @Resource
    private ErpOrderRequestService erpOrderRequestService;
    @Resource
    private ErpOrderItemService erpOrderItemService;
    @Resource
    private ErpOrderRefundService erpOrderRefundService;
    @Resource
    private ErpOrderCancelService erpOrderCancelService;
    @Resource
    private ErpOrderRefundNoTransactionalService erpOrderRefundNoTransactionalService;

    @Override
    public void cancelOrderRequestGroup(String orderCode, AuthToken auth) {

        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        if (order != null) {

            ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(order.getOrderTypeCode(), order.getOrderCategoryCode());
            ErpOrderStatusEnum orderStatusEnum = ErpOrderStatusEnum.getEnum(order.getOrderStatus());

            if (ErpOrderStatusEnum.ORDER_STATUS_98 == orderStatusEnum || ErpOrderStatusEnum.ORDER_STATUS_97 == orderStatusEnum) {

                boolean skipStockStep = ErpOrderStatusEnum.ORDER_STATUS_97 == orderStatusEnum;

                //注销优惠券
                erpOrderCancelService.turnOffCoupon(order.getOrderStoreCode(), auth);

                //解锁库存
                erpOrderCancelService.unlockStock(order.getOrderStoreCode(), processTypeEnum, skipStockStep, auth);

                //取消采购单
                erpOrderCancelService.cancelPurchaseOrder(order.getOrderStoreCode(), processTypeEnum, auth);

                //生成退款单
                erpOrderRefundService.generateOrderRefund(order.getOrderStoreCode(), auth);

                //发起退款
                erpOrderRefundService.orderRefundPay(order.getOrderStoreCode(), ErpRequestPayTransactionTypeEnum.STORE_CANCEL_ORDER, auth);

                //退款轮询
                erpOrderRefundNoTransactionalService.orderRefundPolling(order.getOrderStoreCode(), auth);
            }
        }
    }

    @Override
    public void cancelOrderWithoutStock(ErpOrderCancelRequest erpOrderCancelRequest, AuthToken auth) {

        if (erpOrderCancelRequest == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(erpOrderCancelRequest.getOrderCode())) {
            throw new BusinessException("缺失订单编号");
        }

        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(erpOrderCancelRequest.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }

        boolean cancelFlag = false;

        if (ErpOrderStatusEnum.ORDER_STATUS_6.getCode().equals(order.getOrderStatus())) {
            if (ErpOrderNodeStatusEnum.STATUS_8.getCode().equals(order.getOrderNodeStatus())) {
                cancelFlag = true;
            }
        }

        if (!cancelFlag) {
            throw new BusinessException("不是等待拣货状态不能执行该操作");
        }

        //修改订单状态为缺货终止交易
        erpOrderCancelService.cancelOrderStatus(order.getOrderStoreCode(), ErpOrderStatusEnum.ORDER_STATUS_97, ErpOrderNodeStatusEnum.STATUS_31, auth);

        //取消后续流程
        this.cancelOrderRequestGroup(order.getOrderStoreCode(), auth);

    }

    @Override
    public void applyCancelOrder(ErpOrderCancelRequest erpOrderCancelRequest, AuthToken auth) {

        if (erpOrderCancelRequest == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(erpOrderCancelRequest.getOrderCode())) {
            throw new BusinessException("缺失订单编号");
        }
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(erpOrderCancelRequest.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }
        order.setItemList(erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId()));

        ErpOrderNodeStatusEnum orderNodeStatusEnum = ErpOrderNodeStatusEnum.getEnum(order.getOrderNodeStatus());
        ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(order.getOrderTypeCode(), order.getOrderCategoryCode());
        //待支付
        if (ErpOrderNodeStatusEnum.STATUS_1 == orderNodeStatusEnum || ErpOrderNodeStatusEnum.STATUS_4 == orderNodeStatusEnum) {

            if (processTypeEnum.isLockStock()) {
                boolean flag = erpOrderRequestService.unlockStockInSupplyChainByOrderCode(order, auth);
                if (flag) {
                    //修改订单状态为  超时未支付成功
                    erpOrderCancelService.cancelOrderStatus(order.getOrderStoreCode(), ErpOrderStatusEnum.ORDER_STATUS_99, ErpOrderNodeStatusEnum.STATUS_31, auth);
                } else {
                    throw new BusinessException("解锁库存失败");
                }
            }else{
                //修改订单状态为  超时未支付成功
                erpOrderCancelService.cancelOrderStatus(order.getOrderStoreCode(), ErpOrderStatusEnum.ORDER_STATUS_99, ErpOrderNodeStatusEnum.STATUS_31, auth);
            }

        } else if (ErpOrderNodeStatusEnum.STATUS_8 == orderNodeStatusEnum) {

            boolean flag = erpOrderRequestService.applyToCancelOrderRequest(order.getOrderStoreCode(), auth);
            if (!flag) {
                throw new BusinessException("订单不能取消");
            }

            //修改订单状态为交易异常终止
            erpOrderCancelService.cancelOrderStatus(order.getOrderStoreCode(), ErpOrderStatusEnum.ORDER_STATUS_98, ErpOrderNodeStatusEnum.STATUS_31, auth);

            //取消后续流程
            this.cancelOrderRequestGroup(order.getOrderStoreCode(), auth);

        } else {
            throw new BusinessException("当前状态的订单不能申请取消");
        }
    }

}
