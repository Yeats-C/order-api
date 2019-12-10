package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.dao.order.ErpOrderInfoDao;
import com.aiqin.mgs.order.api.domain.po.order.*;
import com.aiqin.mgs.order.api.service.order.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ErpOrderQueryServiceImpl implements ErpOrderQueryService {

    @Resource
    private ErpOrderInfoDao erpOrderInfoDao;
    @Resource
    private ErpOrderItemService erpOrderItemService;
    @Resource
    private ErpOrderPayService erpOrderPayService;
    @Resource
    private ErpOrderConsigneeService erpOrderConsigneeService;
    @Resource
    private ErpOrderOperationLogService erpOrderOperationLogService;
    @Resource
    private ErpOrderLogisticsService erpOrderLogisticsService;

    @Override
    public ErpOrderInfo getOrderByOrderId(String orderId) {
        ErpOrderInfo order = null;
        if (StringUtils.isNotEmpty(orderId)) {
            ErpOrderInfo orderInfoQuery = new ErpOrderInfo();
            orderInfoQuery.setOrderId(orderId);
            List<ErpOrderInfo> select = erpOrderInfoDao.select(orderInfoQuery);
            if (select != null && select.size() > 0) {
                order = select.get(0);
            }
        }
        return order;
    }

    @Override
    public ErpOrderInfo getOrderByOrderCode(String orderCode) {
        ErpOrderInfo order = null;
        if (StringUtils.isNotEmpty(orderCode)) {
            ErpOrderInfo orderInfoQuery = new ErpOrderInfo();
            orderInfoQuery.setOrderCode(orderCode);
            List<ErpOrderInfo> select = erpOrderInfoDao.select(orderInfoQuery);
            if (select != null && select.size() > 0) {
                order = select.get(0);
            }
        }
        return order;
    }

    @Override
    public ErpOrderInfo getOrderByPayId(String payId) {
        ErpOrderInfo order = null;
        if (StringUtils.isNotEmpty(payId)) {
            ErpOrderInfo orderInfoQuery = new ErpOrderInfo();
            orderInfoQuery.setPayId(payId);
            List<ErpOrderInfo> select = erpOrderInfoDao.select(orderInfoQuery);
            if (select != null && select.size() > 0) {
                order = select.get(0);
            }
        }
        return order;
    }

    @Override
    public ErpOrderInfo getOrderDetailByOrderCode(String orderCode) {
        ErpOrderInfo order = this.getOrderByOrderCode(orderCode);
        if (order != null) {

            //订单明细行
            List<ErpOrderItem> orderItemList = erpOrderItemService.selectOrderItemListByOrderId(order.getOrderId());
            order.setOrderItemList(orderItemList);

            //订单关联支付单
            ErpOrderPay orderPay = erpOrderPayService.getOrderPayByPayId(order.getPayId());
            order.setOrderPay(orderPay);

            //订单收货人信息
            ErpOrderConsignee orderConsignee = erpOrderConsigneeService.getOrderConsigneeByOrderId(order.getOrderId());
            order.setOrderConsignee(orderConsignee);

            //订单操作日志
            List<ErpOrderOperationLog> orderOperationLogList = erpOrderOperationLogService.selectOrderOperationLogListByOrderId(order.getOrderId());
            order.setOrderOperationLogList(orderOperationLogList);

            //订单物流信息
            ErpOrderLogistics orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsId(order.getOrderId());
            order.setOrderLogistics(orderLogistics);
        }
        return order;
    }

    @Override
    public List<ErpOrderInfo> getOrderByLogisticsId(String logisticsId) {
        List<ErpOrderInfo> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(logisticsId)) {
            ErpOrderInfo orderInfoQuery = new ErpOrderInfo();
            orderInfoQuery.setLogisticsId(logisticsId);
            list = erpOrderInfoDao.select(orderInfoQuery);
        }
        return list;
    }

}
