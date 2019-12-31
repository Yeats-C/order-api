package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderFee;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderCancelRequest;
import com.aiqin.mgs.order.api.service.order.*;
import com.aiqin.mgs.order.api.util.AuthUtil;
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
    private ErpOrderFeeService erpOrderFeeService;
    @Resource
    private ErpOrderItemService erpOrderItemService;

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
        if (!ErpOrderStatusEnum.ORDER_STATUS_6.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException(ErpOrderStatusEnum.getEnumDesc(order.getOrderStatus()) + "的订单不支持该操作");
        }

        //修改订单状态为缺货终止交易
//        order.setBeforeCancelStatus(order.getOrderStatus());
        order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_97.getCode());
        erpOrderInfoService.updateOrderByPrimaryKeySelective(order, AuthUtil.getCurrentAuth());

        //TODO 返还物流券 优惠券
        //解锁库存
        erpOrderRequestService.unlockStockInSupplyChain(order,ErpOrderLockStockTypeEnum.REDUCE_AND_UNLOCK,null);
        //TODO 生成退款单
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrderRejectSign(ErpOrderCancelRequest erpOrderCancelRequest) {
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
//        ErpOrderCategoryEnum orderTypeEnum = ErpOrderCategoryEnum.getEnum(order.getOrderType());
//        if (orderTypeEnum == null) {
//            throw new BusinessException("订单类型异常");
//        }
//        if (orderTypeEnum.getErpOrderTypeEnum() == ErpOrderTypeEnum.DISTRIBUTION) {
//            //配送订单
//            if (!ErpOrderStatusEnum.ORDER_STATUS_12.getCode().equals(order.getOrderStatus())) {
//                throw new BusinessException(ErpOrderStatusEnum.getEnumDesc(order.getOrderStatus()) + "的订单不支持该操作");
//            }
//        } else {
//            if (!ErpOrderStatusEnum.ORDER_STATUS_11.getCode().equals(order.getOrderStatus())) {
//                throw new BusinessException(ErpOrderStatusEnum.getEnumDesc(order.getOrderStatus()) + "的订单不支持该操作");
//            }
//        }
//
//        //修改订单状态为拒收终止交易
//        order.setBeforeCancelStatus(order.getOrderStatus());
//        order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_96.getCode());
//        erpOrderInfoService.updateOrderByPrimaryKeySelective(order, AuthUtil.getCurrentAuth());

        //TODO 返还物流券 优惠券
        //解锁库存
        erpOrderRequestService.unlockStockInSupplyChain(order,ErpOrderLockStockTypeEnum.REDUCE_AND_UNLOCK,null);
        //TODO 发起退货退款
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyCancelOrder(ErpOrderCancelRequest erpOrderCancelRequest) {

        AuthToken auth = AuthUtil.getCurrentAuth();

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

//        ErpOrderStatusEnum orderStatusEnum = ErpOrderStatusEnum.getEnum(order.getOrderStoreCode());
//        if (ErpOrderStatusEnum.ORDER_STATUS_1 == orderStatusEnum) {
//            //待支付
//
//            ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByOrderId(order.getOrderId());
//            if (orderFee == null) {
//                throw new BusinessException("订单费用数据异常");
//            }
//            if (ErpPayStatusEnum.PAYING.getCode().equals(orderFee.getPayStatus())) {
//                throw new BusinessException("订单正在支付中，不能取消");
//            }
//
//            //取消订单
//            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_99.getCode());
//            erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);
//            //解锁库存
//            erpOrderRequestService.unlockStockInSupplyChain(order);
//        } else if (ErpOrderStatusEnum.ORDER_STATUS_6 == orderStatusEnum) {
//            //等待拣货
//
//            order.setBeforeCancelStatus(order.getOrderStatus());
//            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_106.getCode());
//            erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);
//
//            //请求供应链取消订单
//            erpOrderRequestService.applyToCancelOrderRequest(order);
//
//        } else {
//            throw new BusinessException(orderStatusEnum.getDesc() + "的订单不能申请取消");
//        }
    }

    @Override
    public void orderCancelResultCallback(ErpOrderCancelRequest erpOrderCancelRequest) {

        AuthToken auth = AuthUtil.getCurrentAuth();

        if (erpOrderCancelRequest == null || StringUtils.isEmpty(erpOrderCancelRequest.getOrderCode())) {
            throw new BusinessException("缺失订单编号");
        }
        if (erpOrderCancelRequest.getStatus() == null) {
            throw new BusinessException("缺失状态");
        }
        if (YesOrNoEnum.exist(erpOrderCancelRequest.getStatus())) {
            throw new BusinessException("不合法的参数");
        }
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(erpOrderCancelRequest.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编码");
        }
        if (!ErpOrderStatusEnum.ORDER_STATUS_106.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException("不是" + ErpOrderStatusEnum.ORDER_STATUS_106.getDesc() + "的订单不能执行该操作");
        }

        if (YesOrNoEnum.YES.getCode().equals(erpOrderCancelRequest.getStatus())) {
            //可以取消

            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_98.getCode());
            erpOrderInfoService.updateOrderByPrimaryKeySelective(order,auth);

            order.setItemList(erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId()));

            //解锁库存
            erpOrderRequestService.unlockStockInSupplyChain(order,ErpOrderLockStockTypeEnum.REDUCE_AND_UNLOCK,auth);

            //TODO CT 生成退款单


        } else {

//            if (order.getBeforeCancelStatus() == null) {
//                throw new BusinessException("订单数据异常");
//            }

            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_94.getCode());
            erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);

//            order.setOrderStatus(order.getBeforeCancelStatus());
//            erpOrderInfoService.updateOrderByPrimaryKeySelectiveNoLog(order, auth);

        }


    }

}
