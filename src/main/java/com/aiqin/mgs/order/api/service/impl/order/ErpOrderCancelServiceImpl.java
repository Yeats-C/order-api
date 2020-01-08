package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.component.enums.pay.ErpRequestPayTransactionTypeEnum;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderCancelRequest;
import com.aiqin.mgs.order.api.service.bill.PurchaseOrderService;
import com.aiqin.mgs.order.api.service.order.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class ErpOrderCancelServiceImpl implements ErpOrderCancelService {

    @Resource
    private ErpOrderInfoService erpOrderInfoService;
    @Resource
    private ErpOrderQueryService erpOrderQueryService;
    @Resource
    private ErpOrderRequestService erpOrderRequestService;
    @Resource
    private ErpOrderItemService erpOrderItemService;
    @Resource
    private ErpOrderRefundService erpOrderRefundService;
    @Resource
    private PurchaseOrderService purchaseOrderService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrderWithoutStock(ErpOrderCancelRequest erpOrderCancelRequest) {

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

        AuthToken auth = new AuthToken();
        auth.setPersonId(erpOrderCancelRequest.getPersonId());
        auth.setPersonName(erpOrderCancelRequest.getPersonName());

        boolean cancelFlag = false;

        if (ErpOrderStatusEnum.ORDER_STATUS_6.getCode().equals(order.getOrderStatus())) {
            if (ErpOrderNodeStatusEnum.STATUS_8.getCode().equals(order.getOrderNodeStatus())) {
                cancelFlag = true;
            }
        }

        if (!cancelFlag) {
            throw new BusinessException("不是等待拣货状态不能执行该操作");
        }

        ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(order.getOrderTypeCode(), order.getOrderCategoryCode());
        if (processTypeEnum.isLockStock()) {
            //解锁库存
            boolean flag = erpOrderRequestService.unlockStockInSupplyChainByDetail(order, ErpOrderLockStockTypeEnum.REDUCE_AND_UNLOCK, auth);
            if (!flag) {
                throw new BusinessException("解锁库存失败");
            }
        }

        //退款
        erpOrderRefundService.orderRefundPay(order.getOrderStoreCode(), ErpRequestPayTransactionTypeEnum.OUT_OF_STOCK_CANCELLATION, auth);


        //注销订单生成的 物流券 优惠券
        erpOrderRequestService.turnOffCouponsByOrderId(order.getOrderStoreId());

        //修改订单状态为缺货终止交易
        order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_97.getCode());
        order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_23.getCode());
        erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyCancelOrder(ErpOrderCancelRequest erpOrderCancelRequest) {

        if (erpOrderCancelRequest == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(erpOrderCancelRequest.getOrderCode())) {
            throw new BusinessException("缺失订单编号");
        }
        AuthToken auth = new AuthToken();
        auth.setPersonId(erpOrderCancelRequest.getPersonId());
        auth.setPersonName(erpOrderCancelRequest.getPersonName());

        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(erpOrderCancelRequest.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }
        order.setItemList(erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId()));

        ErpOrderStatusEnum orderStatusEnum = ErpOrderStatusEnum.getEnum(order.getOrderStoreCode());
        ErpOrderNodeStatusEnum orderNodeStatusEnum = ErpOrderNodeStatusEnum.getEnum(order.getOrderNodeStatus());
        ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(order.getOrderTypeCode(), order.getOrderCategoryCode());
        //待支付
        if (ErpOrderNodeStatusEnum.STATUS_1 == orderNodeStatusEnum || ErpOrderNodeStatusEnum.STATUS_4 == orderNodeStatusEnum) {

            boolean flag = erpOrderRequestService.unlockStockInSupplyChainByOrderCode(order, auth);
            if (flag) {
                order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_99.getCode());
                order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_21.getCode());
                erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);
            } else {
                throw new BusinessException("解锁库存失败");
            }

        } else if (ErpOrderNodeStatusEnum.STATUS_8 == orderNodeStatusEnum) {

            boolean flag = erpOrderRequestService.applyToCancelOrderRequest(order.getOrderStoreCode(), auth);
            if (!flag) {
                throw new BusinessException("订单不能取消");
            }

            //调用采购单取消
            purchaseOrderService.updateCancelOrderinfo(order.getOrderStoreCode());

            //发起退款
            erpOrderRefundService.orderRefundPay(order.getOrderStoreCode(), ErpRequestPayTransactionTypeEnum.STORE_CANCEL_ORDER, auth);

            //注销订单关联优惠券物流券
            erpOrderRequestService.turnOffCouponsByOrderId(order.getOrderStoreId());

            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_98.getCode());
            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_22.getCode());
            if (OrderSucessEnum.ORDER_SYNCHRO_WAIT.getCode().equals(order.getOrderSuccess())) {
                order.setOrderSuccess(OrderSucessEnum.ORDER_SYNCHRO_NO.getCode());
            }
            erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);

            if (processTypeEnum.isLockStock()) {
                //解锁库存
                boolean unlockFlag = erpOrderRequestService.unlockStockInSupplyChainByDetail(order, ErpOrderLockStockTypeEnum.REDUCE_AND_UNLOCK, auth);
                if (!unlockFlag) {
                    throw new BusinessException("解锁库存失败");
                }
            }

        } else {
            throw new BusinessException("当前状态的订单不能申请取消");
        }
    }

}
