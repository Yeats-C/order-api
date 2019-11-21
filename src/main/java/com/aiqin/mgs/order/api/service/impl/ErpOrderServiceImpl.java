package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.dao.OrderStoreOrderInfoDao;
import com.aiqin.mgs.order.api.dao.OrderStoreOrderProductItemDao;
import com.aiqin.mgs.order.api.dao.OrderStoreOrderReceivingDao;
import com.aiqin.mgs.order.api.dao.OrderStoreOrderSendingDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.response.ErpOrderDetailResponse;
import com.aiqin.mgs.order.api.service.*;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.mgs.order.api.util.DateUtil;
import com.aiqin.mgs.order.api.util.OrderPublic;
import com.aiqin.mgs.order.api.util.PageAutoHelperUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ErpOrderServiceImpl implements ErpOrderService {

    @Resource
    private OrderStoreOrderInfoDao orderStoreOrderInfoDao;

    @Resource
    private ErpOrderQueryService erpOrderQueryService;

    @Resource
    private ErpOrderOperationService erpOrderOperationService;

    @Resource
    private OrderStoreOrderProductItemDao orderStoreOrderProductItemDao;

    @Resource
    private ErpOrderPayService erpOrderPayService;

    @Resource
    private OrderStoreOrderReceivingDao orderStoreOrderReceivingDao;

    @Resource
    private OrderStoreOrderSendingDao orderStoreOrderSendingDao;

    @Resource
    private ErpOrderOperationLogService erpOrderOperationLogService;

    @Override
    public PageResData<OrderStoreOrderInfo> findOrderList(OrderStoreOrderInfo orderStoreOrderInfo) {
        orderStoreOrderInfo.setOrderLevel(OrderLevelEnum.PRIMARY.getCode());
        PageResData<OrderStoreOrderInfo> pageResData = PageAutoHelperUtil.generatePageRes(() -> orderStoreOrderInfoDao.findOrderList(orderStoreOrderInfo), orderStoreOrderInfo);
        List<OrderStoreOrderInfo> dataList = pageResData.getDataList();
        List<String> primaryOrderCodeList = new ArrayList<>();
        for (OrderStoreOrderInfo item :
                dataList) {
            primaryOrderCodeList.add(item.getOrderCode());
        }

        //查询子订单列表
        Map<String, List<OrderStoreOrderInfo>> secondaryOrderMap = new HashMap<>(16);
        List<OrderStoreOrderInfo> secondaryOrderList = orderStoreOrderInfoDao.findSecondaryOrderList(primaryOrderCodeList);
        if (secondaryOrderList != null && secondaryOrderList.size() > 0) {
            for (OrderStoreOrderInfo item :
                    secondaryOrderList) {
                String primaryCode = item.getPrimaryCode();
                if (secondaryOrderMap.containsKey(primaryCode)) {
                    secondaryOrderMap.get(primaryCode).add(item);
                } else {
                    List<OrderStoreOrderInfo> newSecondaryOrderList = new ArrayList<>();
                    newSecondaryOrderList.add(item);
                    secondaryOrderMap.put(primaryCode, newSecondaryOrderList);
                }
            }
        }

        for (OrderStoreOrderInfo item :
                dataList) {
            if (secondaryOrderMap.containsKey(item.getOrderCode())) {
                item.setSecondaryOrderList(secondaryOrderMap.get(item.getOrderCode()));
            }
        }


        return pageResData;
    }

    @Override
    public ErpOrderDetailResponse getOrderDetail(OrderStoreOrderInfo orderStoreOrderInfo) {
        ErpOrderDetailResponse response = new ErpOrderDetailResponse();

        if (orderStoreOrderInfo == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(orderStoreOrderInfo.getOrderId()) && StringUtils.isEmpty(orderStoreOrderInfo.getOrderCode())) {
            throw new BusinessException("请传入订单id或订单编码");
        }

        //查询订单
        List<OrderStoreOrderInfo> orderList = erpOrderQueryService.selectOrderBySelective(orderStoreOrderInfo);
        if (orderList == null || orderList.size() <= 0) {
            throw new BusinessException("订单不存在");
        }
        OrderStoreOrderInfo order = orderList.get(0);
        response.setOrderInfo(order);

        //查询订单商品行
        List<OrderStoreOrderProductItem> orderProductItemList = orderStoreOrderProductItemDao.selectOrderProductListByOrderId(order.getOrderId());
        response.setOrderProductItemList(orderProductItemList);

        //查询订单支付信息
        OrderStoreOrderPay orderPay = erpOrderPayService.getOrderPayByOrderId(order.getOrderId());
        response.setOrderPay(orderPay);

        //查询收货信息
        OrderStoreOrderReceiving orderReceiving = orderStoreOrderReceivingDao.getOrderReceivingByOrderId(order.getOrderId());
        response.setOrderReceiving(orderReceiving);

        //查询订单发货信息
        OrderStoreOrderSending orderSending = orderStoreOrderSendingDao.getOrderSendingByOrderId(order.getOrderId());
        response.setOrderSending(orderSending);

        //查询订单操作日志
        List<OrderStoreOrderOperationLog> orderOperationLogList = erpOrderOperationLogService.selectOperationLogListByOrderId(order.getOrderId());
        response.setOrderOperationLogList(orderOperationLogList);

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrder(OrderStoreOrderInfo orderStoreOrderInfo) {

        AuthToken auth = AuthUtil.getCurrentAuth();
        if (auth.getPersonId() == null || "".equals(auth.getPersonId())) {
            throw new BusinessException("请先登录");
        }

        String orderId = OrderPublic.getUUID();
        String orderCode = generateOrderCode(OrderOriginTypeEnum.ORIGIN_COME_5.getCode(), OrderChannelEnum.CHANNEL_3.getCode());

        //保存订单信息
        OrderStoreOrderInfo orderInfo = new OrderStoreOrderInfo();
        orderInfo.setOrderCode(orderCode);
        orderInfo.setOrderId(orderId);
        orderInfo.setPayStatus(PayStatusEnum.UNPAID.getCode());
        orderInfo.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_1.getCode());
        orderInfo.setOrderLevel(OrderLevelEnum.PRIMARY.getCode());
        orderInfo.setActualPrice(new BigDecimal(120.0000));
        orderInfo.setTotalPrice(new BigDecimal(120.0000));
        orderInfo.setOrderType(OrderTypeEnum.ORDER_TYPE_1.getCode());
        orderInfo.setReturnStatus(YesOrNoEnum.NO.getCode());
        orderInfo.setFranchiseeId("12345");
        orderInfo.setStoreId("123456");
        orderInfo.setStoreName("门店1");
        erpOrderOperationService.saveOrder(orderInfo, auth);

        //保存订单支付信息
        OrderStoreOrderPay orderPay = new OrderStoreOrderPay();
        orderPay.setOrderId(orderId);
        orderPay.setOrderTotal(orderInfo.getTotalPrice());
        orderPay.setPayStatus(orderInfo.getPayStatus());
        orderPay.setPayId(OrderPublic.getUUID());
        orderPay.setActivityCouponMoney(BigDecimal.ZERO);
        orderPay.setActivityMoney(BigDecimal.ZERO);
        orderPay.setClothCouponMoney(BigDecimal.ZERO);
        orderPay.setActualMoney(new BigDecimal(120));
        orderPay.setUpdateById(auth.getPersonId());
        orderPay.setCreateByName(auth.getPersonName());
        orderPay.setUpdateById(auth.getPersonId());
        orderPay.setUpdateByName(auth.getPersonName());
        erpOrderPayService.saveOrderPay(orderPay);

        //保存订单商品明细
        List<OrderStoreOrderProductItem> orderProductItemList = new ArrayList<>();
        OrderStoreOrderProductItem orderProductItem1 = new OrderStoreOrderProductItem();
        orderProductItem1.setOrderId(orderId);
        orderProductItem1.setOrderCode(orderCode);
        orderProductItem1.setOrderProductId(OrderPublic.getUUID());
        orderProductItem1.setActualDeliverNum(10);
        orderProductItem1.setActualStoreNum(10);
        orderProductItem1.setOrderMoney(new BigDecimal(100));
        orderProductItem1.setOrderProductId("PD000001");
        orderProductItem1.setOriginalProductPrice(BigDecimal.TEN);
        orderProductItem1.setProductNumber(10);
        orderProductItem1.setProductOrderPrice(BigDecimal.TEN);
        orderProductItem1.setShareAfterPrice(BigDecimal.TEN);
        orderProductItem1.setSkuCode("001");
        orderProductItem1.setSkuName("大号");
        orderProductItem1.setUnit("件");
        orderProductItem1.setCreateById(auth.getPersonId());
        orderProductItem1.setCreateByName(auth.getPersonName());
        orderProductItem1.setUpdateById(auth.getPersonId());
        orderProductItem1.setUpdateByName(auth.getPersonName());
        orderProductItemList.add(orderProductItem1);

        OrderStoreOrderProductItem orderProductItem2 = new OrderStoreOrderProductItem();
        orderProductItem2.setOrderId(orderId);
        orderProductItem2.setOrderCode(orderCode);
        orderProductItem2.setOrderProductId(OrderPublic.getUUID());
        orderProductItem2.setActualDeliverNum(2);
        orderProductItem2.setActualStoreNum(2);
        orderProductItem2.setOrderMoney(new BigDecimal(20));
        orderProductItem2.setOrderProductId("PD000002");
        orderProductItem2.setOriginalProductPrice(BigDecimal.TEN);
        orderProductItem2.setProductNumber(2);
        orderProductItem2.setTaxPurchasePrice(BigDecimal.TEN);
        orderProductItem2.setProductOrderPrice(BigDecimal.TEN);
        orderProductItem2.setShareAfterPrice(BigDecimal.TEN);
        orderProductItem2.setSkuCode("002");
        orderProductItem2.setSkuName("中号");
        orderProductItem2.setUnit("件");
        orderProductItem2.setCreateById(auth.getPersonId());
        orderProductItem2.setCreateByName(auth.getPersonName());
        orderProductItem2.setUpdateById(auth.getPersonId());
        orderProductItem2.setUpdateByName(auth.getPersonName());
        orderProductItemList.add(orderProductItem2);

        for (OrderStoreOrderProductItem item :
                orderProductItemList) {
            orderStoreOrderProductItemDao.insert(item);
        }
    }

    /**
     * 生成订单编号
     *
     * @param originType   订单来源
     * @param orderChannel 销售渠道
     * @return java.lang.String
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/20 11:19
     */
    private String generateOrderCode(Integer originType, Integer orderChannel) {

        //判断订单来源和销售渠道是否合法
        if (!OrderOriginTypeEnum.exist(originType)) {
            throw new BusinessException("无效的订单来源");
        }
        if (!OrderChannelEnum.exist(orderChannel)) {
            throw new BusinessException("无效的销售渠道类型");
        }

        //订单号 yyMMddHHmmss+订单来源+销售渠道标识+4位数的随机数
        return DateUtil.sysDate() + originType + orderChannel + OrderPublic.randomNumberF();
    }

}
