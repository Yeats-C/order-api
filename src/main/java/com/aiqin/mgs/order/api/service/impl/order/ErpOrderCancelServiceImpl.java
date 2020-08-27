package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.service.bill.PurchaseOrderService;
import com.aiqin.mgs.order.api.service.order.*;
import com.aiqin.mgs.order.api.util.RequestReturnUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(rollbackFor = Exception.class)
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
    private PurchaseOrderService purchaseOrderService;

    @Override
    public void turnOffCoupon(String orderCode, AuthToken auth) {
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        if (ErpOrderNodeStatusEnum.STATUS_31.getCode().equals(order.getOrderNodeStatus())) {

            //注销订单关联优惠券物流券
            erpOrderRequestService.turnOffCouponsByOrderId(order.getOrderStoreCode());

            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_32.getCode());
            erpOrderInfoService.updateOrderByPrimaryKeySelectiveNoLog(order, auth);
        }
    }

    @Override
    public void unlockStock(String orderCode, ErpOrderNodeProcessTypeEnum processTypeEnum, boolean skipStep, AuthToken auth) {
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        if (ErpOrderNodeStatusEnum.STATUS_32.getCode().equals(order.getOrderNodeStatus())) {

            if (!skipStep && processTypeEnum.isLockStock()) {
                order.setItemList(erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId()));
//                boolean unlockFlag = erpOrderRequestService.unlockStockInSupplyChainByDetail(order, ErpOrderLockStockTypeEnum.REDUCE_AND_UNLOCK, auth);
//                if (!unlockFlag) {
//                    throw new BusinessException("解锁库存失败");
//                }
            }
            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_33.getCode());
            erpOrderInfoService.updateOrderByPrimaryKeySelectiveNoLog(order, auth);
        }
    }

    @Override
    public void cancelPurchaseOrder(String orderCode, ErpOrderNodeProcessTypeEnum processTypeEnum, AuthToken auth) {
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        if (ErpOrderNodeStatusEnum.STATUS_33.getCode().equals(order.getOrderNodeStatus())) {
            //调用采购单取消
            HttpResponse response = purchaseOrderService.updateCancelOrderinfo(order.getOrderStoreCode());
            if (!RequestReturnUtil.validateHttpResponse(response)) {
                throw new BusinessException("取消采购单失败");
            }
            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_34.getCode());
            erpOrderInfoService.updateOrderByPrimaryKeySelectiveNoLog(order, auth);
        }
    }

    @Override
    public void cancelOrderStatus(String orderCode, ErpOrderStatusEnum orderStatusEnum, ErpOrderNodeStatusEnum orderNodeStatusEnum, AuthToken auth) {
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        if (order != null) {
            order.setOrderStatus(orderStatusEnum.getCode());
            order.setOrderNodeStatus(orderNodeStatusEnum.getCode());
            if (OrderSucessEnum.ORDER_SYNCHRO_WAIT.getCode().equals(order.getOrderSuccess())) {
                order.setOrderSuccess(OrderSucessEnum.ORDER_SYNCHRO_NO.getCode());
            }
            erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);
        }
    }

}
