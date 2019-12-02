package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.OrderTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ProductTypeEnum;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderProductItem;
import com.aiqin.mgs.order.api.service.ErpOrderOperationService;
import com.aiqin.mgs.order.api.service.ErpOrderProductItemService;
import com.aiqin.mgs.order.api.service.ErpOrderQueryService;
import com.aiqin.mgs.order.api.service.ErpOrderSignService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ErpOrderSignServiceImpl implements ErpOrderSignService {

    @Resource
    private ErpOrderQueryService erpOrderQueryService;
    @Resource
    private ErpOrderOperationService erpOrderOperationService;
    @Resource
    private ErpOrderProductItemService erpOrderProductItemService;

    @Override
    public OrderStoreOrderInfo getOrderSignDetail(OrderStoreOrderInfo orderStoreOrderInfo) {
        if (orderStoreOrderInfo == null || StringUtils.isEmpty(orderStoreOrderInfo.getOrderCode())) {
            throw new BusinessException("空参数");
        }
        OrderStoreOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderStoreOrderInfo.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }
        List<OrderStoreOrderProductItem> orderProductItemList = erpOrderProductItemService.selectOrderProductListByOrderId(order.getOrderId());
        order.setProductItemList(orderProductItemList);
        return order;
    }

    @Override
    public void orderSign(OrderStoreOrderInfo orderStoreOrderInfo) {
        AuthToken auth = AuthUtil.getCurrentAuth();
        if (orderStoreOrderInfo == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(orderStoreOrderInfo.getOrderCode())) {
            throw new BusinessException("订单编号为空");
        }
        /* ----------TODO 测试代码，供对接口使用，用完删除 start----------*/
        if ("10000".equals(orderStoreOrderInfo.getOrderCode())) {
            throw new BusinessException("测试失败返回值");
        }
        if ("10001".equals(orderStoreOrderInfo.getOrderCode())) {
            return;
        }
        /* ----------TODO 测试代码，供对接口使用，用完删除 end----------*/

        List<OrderStoreOrderProductItem> productItemList = orderStoreOrderInfo.getProductItemList();
        if (productItemList == null || productItemList.size() <= 0) {
            throw new BusinessException("商品明细为空");
        }
        Map<String, OrderStoreOrderProductItem> orderItemSignMap = new HashMap<>(16);
        int lineIndex = 0;
        for (OrderStoreOrderProductItem item :
                productItemList) {
            lineIndex++;
            if (item == null) {
                throw new BusinessException("第" + lineIndex + "行数据为空");
            }
            if (StringUtils.isEmpty(item.getOrderItemCode())) {
                throw new BusinessException("第" + lineIndex + "行订单行号为空");
            }
            if (item.getActualStoreQuantity() == null || item.getActualStoreQuantity() <= 0) {
                throw new BusinessException("第" + lineIndex + "行签收数量有误");
            }
            orderItemSignMap.put(item.getOrderItemCode(), item);
        }

        OrderStoreOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderStoreOrderInfo.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }
        OrderTypeEnum orderTypeEnum = OrderTypeEnum.getEnum(order.getOrderType());
        if (orderTypeEnum == null) {
            throw new BusinessException("订单类型有误");
        }
        ProductTypeEnum productTypeEnum = orderTypeEnum.getProductTypeEnum();
        if (productTypeEnum == ProductTypeEnum.DISTRIBUTION) {
            //配送订单
            if (!ErpOrderStatusEnum.ORDER_STATUS_12.getCode().equals(order.getOrderStatus())) {
                throw new BusinessException("该" + productTypeEnum.getDesc() + "订单不是" + ErpOrderStatusEnum.ORDER_STATUS_12.getDesc() + "的订单，不能签收");
            }
        } else {
            if (!ErpOrderStatusEnum.ORDER_STATUS_11.getCode().equals(order.getOrderStatus())) {
                throw new BusinessException("该" + productTypeEnum.getDesc() + "订单不是" + ErpOrderStatusEnum.ORDER_STATUS_12.getDesc() + "的订单，不能签收");
            }
        }
        List<OrderStoreOrderProductItem> orderProductItemList = erpOrderProductItemService.selectOrderProductListByOrderId(order.getOrderId());
        List<OrderStoreOrderProductItem> updateOrderProductItemList = new ArrayList<>();
        for (OrderStoreOrderProductItem item :
                orderProductItemList) {
            if (!orderItemSignMap.containsKey(item.getOrderItemCode())) {
                throw new BusinessException("商品" + item.getProductName() + item.getSkuName() + "签收信息为空");
            }
            OrderStoreOrderProductItem orderProductSignItem = orderItemSignMap.get(item.getOrderItemCode());
            OrderStoreOrderProductItem updateOrderProductItem = new OrderStoreOrderProductItem();
            updateOrderProductItem.setId(item.getId());
            updateOrderProductItem.setActualStoreQuantity(orderProductSignItem.getActualStoreQuantity());
            updateOrderProductItem.setSignDifferenceReason(orderProductSignItem.getSignDifferenceReason());
            updateOrderProductItemList.add(updateOrderProductItem);
        }
        erpOrderProductItemService.updateOrderProductItemList(updateOrderProductItemList, auth);

        OrderStoreOrderInfo updateOrder = new OrderStoreOrderInfo();
        updateOrder.setId(order.getId());
        updateOrder.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_13.getCode());
        erpOrderOperationService.updateOrderByPrimaryKeySelective(updateOrder, ErpOrderStatusEnum.ORDER_STATUS_13.getDesc(), auth);
    }
}
