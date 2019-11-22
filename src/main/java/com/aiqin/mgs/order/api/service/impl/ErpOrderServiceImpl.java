package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.dao.OrderStoreOrderInfoDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.request.ErpOrderSaveRequest;
import com.aiqin.mgs.order.api.domain.response.ErpOrderDetailResponse;
import com.aiqin.mgs.order.api.domain.response.cart.OrderConfirmResponse;
import com.aiqin.mgs.order.api.service.*;
import com.aiqin.mgs.order.api.util.*;
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
    private ErpOrderProductItemService erpOrderProductItemService;

    @Resource
    private ErpOrderPayService erpOrderPayService;

    @Resource
    private ErpOrderReceivingService erpOrderReceivingService;

    @Resource
    private ErpOrderSendingService erpOrderSendingService;

    @Resource
    private ErpOrderOperationLogService erpOrderOperationLogService;

    @Resource
    private CartOrderService cartOrderService;

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
        List<OrderStoreOrderProductItem> orderProductItemList = erpOrderProductItemService.selectOrderProductListByOrderId(order.getOrderId());
        response.setOrderProductItemList(orderProductItemList);

        //查询订单支付信息
        OrderStoreOrderPay orderPay = erpOrderPayService.getOrderPayByOrderId(order.getOrderId());
        response.setOrderPay(orderPay);

        //查询收货信息
        OrderStoreOrderReceiving orderReceiving = erpOrderReceivingService.getOrderReceivingByOrderId(order.getOrderId());
        response.setOrderReceiving(orderReceiving);

        //查询订单发货信息
        OrderStoreOrderSending orderSending = erpOrderSendingService.getOrderSendingByOrderId(order.getOrderId());
        response.setOrderSending(orderSending);

        //查询订单操作日志
        List<OrderStoreOrderOperationLog> orderOperationLogList = erpOrderOperationLogService.selectOperationLogListByOrderId(order.getOrderId());
        response.setOrderOperationLogList(orderOperationLogList);

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrder(ErpOrderSaveRequest erpOrderSaveRequest) {

        AuthToken auth = AuthUtil.getCurrentAuth();
        if (auth.getPersonId() == null || "".equals(auth.getPersonId())) {
            throw new BusinessException("请先登录");
        }
        if (erpOrderSaveRequest == null || StringUtils.isEmpty(erpOrderSaveRequest.getStoreId())) {
            throw new BusinessException("请选择门店");
        }
        if ("test".equals(erpOrderSaveRequest.getStoreId())) {
            test(auth);
            return;
        }

        //获取购物车商品
        OrderConfirmResponse storeCartProduct = getStoreCartProduct(erpOrderSaveRequest.getStoreId());

        //TODO 购物车数据校验

        //获取门店信息
        StoreInfo storeInfo = getStoreInfoByStoreId(erpOrderSaveRequest.getStoreId());

        //生成订单
        generateOrder(storeCartProduct, storeInfo, erpOrderSaveRequest);

        //删除购物车商品
        deleteOrderProductFromCart(storeCartProduct);

    }

    /**
     * 根据门店id获取门店信息
     *
     * @param storeId
     * @return com.aiqin.mgs.order.api.domain.StoreInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 16:07
     */
    private StoreInfo getStoreInfoByStoreId(String storeId) {
        StoreInfo storeInfo = new StoreInfo();
        storeInfo.setStoreId(storeId);
        storeInfo.setStoreName("门店1");
        storeInfo.setFranchiseeId("123456");
        return storeInfo;
    }

    /**
     * 从购物车获取选择的商品
     *
     * @param storeId 门店id
     * @return com.aiqin.mgs.order.api.domain.response.cart.OrderConfirmResponse
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 16:00
     */
    private OrderConfirmResponse getStoreCartProduct(String storeId) {
        CartOrderInfo cartOrderInfoQuery = new CartOrderInfo();
        cartOrderInfoQuery.setStoreId(storeId);
        cartOrderInfoQuery.setLineCheckStatus(YesOrNoEnum.YES.getCode());
        HttpResponse listHttpResponse = cartOrderService.displayCartLineCheckProduct(cartOrderInfoQuery);
        if (!RequestReturnUtil.validateHttpResponse(listHttpResponse)) {
            throw new BusinessException("获取购物车商品失败");
        }
        OrderConfirmResponse data = (OrderConfirmResponse) listHttpResponse.getData();
        if (data == null || data.getCartOrderInfos() == null || data.getCartOrderInfos().size() == 0) {
            throw new BusinessException("购物车没有勾选的商品");
        }
        return data;
    }

    /**
     * 构建订单数据
     *
     * @param orderConfirmResponse 购物车数据
     * @param storeInfo            门店信息
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 16:01
     */
    private void generateOrder(OrderConfirmResponse orderConfirmResponse, StoreInfo storeInfo, ErpOrderSaveRequest erpOrderSaveRequest) {
        String orderId = OrderPublic.getUUID();
        String orderCode = OrderPublic.generateOrderCode(OrderOriginTypeEnum.ORIGIN_COME_5.getCode(), OrderChannelEnum.CHANNEL_3.getCode());

        AuthToken auth = AuthUtil.getCurrentAuth();

        List<CartOrderInfo> cartOrderInfoList = orderConfirmResponse.getCartOrderInfos();
        List<OrderStoreOrderProductItem> orderProductItemList = new ArrayList<>();
        for (CartOrderInfo item :
                cartOrderInfoList) {
            OrderStoreOrderProductItem orderProductItem = new OrderStoreOrderProductItem();
            orderProductItem.setOrderId(orderId);
            orderProductItem.setOrderCode(orderCode);
            orderProductItem.setOrderProductId(item.getProductId());
            //仓库实发数量
//            orderProductItem.setActualDeliverNum(10);
            //门店实际接收数量
//            orderProductItem.setActualStoreNum(10);
            orderProductItem.setOrderMoney(item.getAcountTotalprice());
            orderProductItem.setOriginalProductPrice(item.getPrice());
            orderProductItem.setProductNumber(item.getAmount());
            //订货价
//            orderProductItem.setProductOrderPrice(BigDecimal.TEN);
            //分摊后单价？
//            orderProductItem.setShareAfterPrice(BigDecimal.TEN);
            orderProductItem.setSkuCode(item.getSkuId());
            //skuName从哪里来
//            orderProductItem.setSkuName("");
            //单位
//            orderProductItem.setUnit("");

            orderProductItem.setCreateById(auth.getPersonId());
            orderProductItem.setCreateByName(auth.getPersonName());
            orderProductItem.setUpdateById(auth.getPersonId());
            orderProductItem.setUpdateByName(auth.getPersonName());
            orderProductItemList.add(orderProductItem);
        }
        erpOrderProductItemService.saveOrderProductItemList(orderProductItemList);

        //保存订单信息
        OrderStoreOrderInfo orderInfo = new OrderStoreOrderInfo();
        orderInfo.setOrderCode(orderCode);
        orderInfo.setOrderId(orderId);
        orderInfo.setPayStatus(PayStatusEnum.UNPAID.getCode());
        orderInfo.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_1.getCode());
        orderInfo.setOrderLevel(OrderLevelEnum.PRIMARY.getCode());
        orderInfo.setActualPrice(orderConfirmResponse.getAcountActualprice());
        orderInfo.setTotalPrice(orderConfirmResponse.getAcountActualprice());
        orderInfo.setOrderType(erpOrderSaveRequest.getOrderType());
        orderInfo.setReturnStatus(YesOrNoEnum.NO.getCode());
        orderInfo.setFranchiseeId(storeInfo.getFranchiseeId());
        orderInfo.setStoreId(storeInfo.getStoreId());
        orderInfo.setStoreName(storeInfo.getStoreName());
        erpOrderOperationService.saveOrder(orderInfo, auth);

        //保存订单支付信息
        OrderStoreOrderPay orderPay = new OrderStoreOrderPay();
        orderPay.setOrderId(orderId);
        orderPay.setOrderTotal(orderInfo.getTotalPrice());
        orderPay.setPayStatus(orderInfo.getPayStatus());
//        orderPay.setPayId("");
        orderPay.setActivityCouponMoney(BigDecimal.ZERO);
        orderPay.setActivityMoney(BigDecimal.ZERO);
        orderPay.setClothCouponMoney(BigDecimal.ZERO);
        orderPay.setActualMoney(orderInfo.getTotalPrice());
        orderPay.setUpdateById(auth.getPersonId());
        orderPay.setCreateByName(auth.getPersonName());
        orderPay.setUpdateById(auth.getPersonId());
        orderPay.setUpdateByName(auth.getPersonName());
        erpOrderPayService.saveOrderPay(orderPay);

        //保存订单发货信息
        OrderStoreOrderReceiving orderReceiving = new OrderStoreOrderReceiving();
        orderReceiving.setOrderId(orderId);
        orderReceiving.setReceiveId(OrderPublic.getUUID());
        orderReceiving.setReceiveName(orderConfirmResponse.getStoreContacts());
        orderReceiving.setReceiveAddress(orderConfirmResponse.getStoreAddress());
        orderReceiving.setReceivePhone(orderConfirmResponse.getStoreContactsPhone());
        orderReceiving.setBillStatus(erpOrderSaveRequest.getBillStatus());
        orderReceiving.setUpdateById(auth.getPersonId());
        orderReceiving.setCreateByName(auth.getPersonName());
        orderReceiving.setUpdateById(auth.getPersonId());
        orderReceiving.setUpdateByName(auth.getPersonName());
        erpOrderReceivingService.saveOrderReceiving(orderReceiving);

    }

    /**
     * 从购物车删除已经生成订单的商品
     *
     * @param orderConfirmResponse 购物车商品
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 16:02
     */
    private void deleteOrderProductFromCart(OrderConfirmResponse orderConfirmResponse) {

    }

    private void test(AuthToken auth) {
        String orderId = OrderPublic.getUUID();
        String orderCode = OrderPublic.generateOrderCode(OrderOriginTypeEnum.ORIGIN_COME_5.getCode(), OrderChannelEnum.CHANNEL_3.getCode());

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

        erpOrderProductItemService.saveOrderProductItemList(orderProductItemList);

    }
}
