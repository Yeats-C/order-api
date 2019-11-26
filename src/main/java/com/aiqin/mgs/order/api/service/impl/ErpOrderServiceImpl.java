package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.PagesRequest;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.dao.OrderStoreOrderInfoDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.request.ErpOrderSaveRequest;
import com.aiqin.mgs.order.api.domain.response.ErpOrderDetailResponse;
import com.aiqin.mgs.order.api.domain.response.cart.OrderConfirmResponse;
import com.aiqin.mgs.order.api.service.*;
import com.aiqin.mgs.order.api.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ErpOrderServiceImpl implements ErpOrderService {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderServiceImpl.class);

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

    @Resource
    private UrlProperties urlProperties;

    @Override
    public PageResData<OrderStoreOrderInfo> findOrderList(OrderStoreOrderInfo orderStoreOrderInfo) {
        //查询主订单列表
        orderStoreOrderInfo.setOrderLevel(OrderLevelEnum.PRIMARY.getCode());
        PagesRequest page = new PagesRequest();
        page.setPageNo(orderStoreOrderInfo.getPageNo() == null ? 1 : orderStoreOrderInfo.getPageNo());
        page.setPageSize(orderStoreOrderInfo.getPageSize() == null ? 10 : orderStoreOrderInfo.getPageSize());
        PageResData<OrderStoreOrderInfo> pageResData = PageAutoHelperUtil.generatePageRes(() -> orderStoreOrderInfoDao.findOrderList(orderStoreOrderInfo), page);

        //查询子订单列表
        List<OrderStoreOrderInfo> dataList = pageResData.getDataList();
        if (dataList != null && dataList.size() > 0) {

            //获取主订单编码，检查待支付和已取消订单支付状态
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

                //支付状态与订单中心不同步的订单标记
                item.setRepayOperation(YesOrNoEnum.NO.getCode());
                //只检查待支付和已取消的订单
                if (ErpOrderStatusEnum.ORDER_STATUS_1.getCode().equals(item.getOrderStatus()) || ErpOrderStatusEnum.ORDER_STATUS_99.getCode().equals(item.getOrderStatus())) {
                    try {
                        //TODO 请求支付中心接口查询订单支付状态
                        Map<String, Object> paramMap = new HashMap<>();
                        HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/test").json(paramMap);
                        HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
                        });
                        PayStatusEnum payStatusEnum = PayStatusEnum.SUCCESS;
                        if (payStatusEnum == PayStatusEnum.SUCCESS) {
                            //如果支付状态是成功的
                            item.setRepayOperation(YesOrNoEnum.YES.getCode());
                        }
                    } catch (Exception e) {
                        logger.error("请求支付中心查询支付状态失败", e);
                    }
                }
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
        if (auth.getPersonId() == null) {
            throw new BusinessException("请先登录");
        }
        //校验参数
        validateSaveOrderRequest(erpOrderSaveRequest);

        //临时生成假数据使用
        if ("test".equals(erpOrderSaveRequest.getStoreId())) {
            test(auth);
            return;
        }

        //获取购物车商品
        OrderConfirmResponse storeCartProduct = getStoreCartProduct(erpOrderSaveRequest.getStoreId());

        //TODO 购物车数据校验

        //根据购物车的商品信息从供应链获取商品详情
        Map<String, ProductInfo> productDetailMap = getProductDetail(storeCartProduct.getCartOrderInfos());

        //获取门店信息
        StoreInfo storeInfo = getStoreInfoByStoreId(erpOrderSaveRequest.getStoreId());

        //生成订单
        generateOrder(storeCartProduct, storeInfo, erpOrderSaveRequest, productDetailMap);

        //删除购物车商品
        deleteOrderProductFromCart(erpOrderSaveRequest.getStoreId(), storeCartProduct);

    }

    /**
     * 校验保存订单参数
     *
     * @param erpOrderSaveRequest
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/25 9:40
     */
    private void validateSaveOrderRequest(ErpOrderSaveRequest erpOrderSaveRequest) {
        if (erpOrderSaveRequest == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(erpOrderSaveRequest.getStoreId())) {
            throw new BusinessException("请传入门店id");
        }
        if (erpOrderSaveRequest.getOrderType() == null) {
            throw new BusinessException("请传入订单类型");
        } else {
            if (!OrderTypeEnum.exist(erpOrderSaveRequest.getOrderType())) {
                throw new BusinessException("无效的订单类型");
            }
        }
        if (erpOrderSaveRequest.getOrderOriginType() == null) {
            throw new BusinessException("请选择订单来源");
        } else {
            if (!OrderOriginTypeEnum.exist(erpOrderSaveRequest.getOrderOriginType())) {
                throw new BusinessException("无效的订单来源");
            }
        }
        if (erpOrderSaveRequest.getOrderChannel() == null) {
            throw new BusinessException("请选择销售渠道");
        } else {
            if (!OrderChannelEnum.exist(erpOrderSaveRequest.getOrderChannel())) {
                throw new BusinessException("无效的销售渠道");
            }
        }
        if (erpOrderSaveRequest.getBillStatus() == null) {
            throw new BusinessException("请确认是否需要发票");
        }
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
        //TODO 根据门店id跨项目查询门店信息
        StoreInfo storeInfo = new StoreInfo();
        storeInfo.setStoreId(storeId);
        storeInfo.setStoreCode("123456");
        storeInfo.setStoreName("门店1");
        storeInfo.setFranchiseeId("123456");
        storeInfo.setFranchiseeCode("123456");
        storeInfo.setFranchiseeName("加盟商1");
        storeInfo.setContacts("张三");
        storeInfo.setContactsPhone("12345678910");
        storeInfo.setAddress("北京市朝阳区");
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
     * 根据购物车商品从供应链获取商品详情
     *
     * @param list
     * @return java.util.Map
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/25 9:42
     */
    private Map<String, ProductInfo> getProductDetail(List<CartOrderInfo> list) {

        //保存商品详情  key：sku  value：商品详情
        Map<String, ProductInfo> productMap = new HashMap<>(16);

        //TODO 从供应链获取商品详情
        for (CartOrderInfo item :
                list) {
            ProductInfo product = new ProductInfo();
            product.setProductId(item.getProductId());
            product.setProductCode("1"+System.currentTimeMillis());
            product.setProductName(item.getProductName());
            product.setSkuCode(item.getSkuId());
            product.setSkuName(item.getSkuId() + System.currentTimeMillis());
            product.setUnit("个");
            product.setPrice(BigDecimal.TEN);
            product.setTaxPurchasePrice(BigDecimal.TEN.add(BigDecimal.ONE));

            productMap.put(item.getSkuId(), product);
        }
        return productMap;
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
    private void generateOrder(OrderConfirmResponse orderConfirmResponse, StoreInfo storeInfo, ErpOrderSaveRequest erpOrderSaveRequest, Map<String, ProductInfo> productMap) {

        //生成订单id
        String orderId = OrderPublic.getUUID();
        //生成订单code
        String orderCode = OrderPublic.generateOrderCode(erpOrderSaveRequest.getOrderOriginType(), erpOrderSaveRequest.getOrderChannel());

        //获取登录用户信息
        AuthToken auth = AuthUtil.getCurrentAuth();

        //购物车商品
        List<CartOrderInfo> cartOrderInfoList = orderConfirmResponse.getCartOrderInfos();

        //第一次遍历 构建订单明细行商品 TODO 没有计算活动优惠、A品券优惠、服纺券优惠，没有计算分摊后单价
        List<OrderStoreOrderProductItem> orderProductItemList = new ArrayList<>();
        //订货金额汇总
        BigDecimal moneyTotal = BigDecimal.ZERO;
        //实际支付金额汇总
        BigDecimal realMoneyTotal = BigDecimal.ZERO;
        //活动优惠金额汇总
        BigDecimal activityMoneyTotal = BigDecimal.ZERO;
        //服纺券优惠金额汇总
        BigDecimal spinCouponMoneyTotal = BigDecimal.ZERO;
        //A品券优惠金额汇总
        BigDecimal topCouponMoneyTotal = BigDecimal.ZERO;
        int orderItemNum = 1;
        for (CartOrderInfo item :
                cartOrderInfoList) {
            ProductInfo productInfo = productMap.get(item.getSkuId());
            OrderStoreOrderProductItem orderProductItem = new OrderStoreOrderProductItem();

            //订单id
            orderProductItem.setOrderId(orderId);
            //订单编号
            orderProductItem.setOrderCode(orderCode);
            //订单明细行编号
            orderProductItem.setOrderItemCode(orderCode + String.format("%03d", orderItemNum++));

            //商品ID
            orderProductItem.setProductId(productInfo.getProductId());
            //商品编码
            orderProductItem.setProductCode(productInfo.getProductCode());
            //商品名称
            orderProductItem.setProductName(productInfo.getProductName());
            //商品sku码
            orderProductItem.setSkuCode(productInfo.getSkuCode());
            //商品名称
            orderProductItem.setSkuName(productInfo.getSkuName());
            //单位
            orderProductItem.setUnit(productInfo.getUnit());
            //本品赠品标记 ProductGiftEnum
            orderProductItem.setProductGift(ProductGiftEnum.PRODUCT.getCode());
            //赠品行关联本品行编码
            orderProductItem.setParentOrderItemCode("");

            //活动id
            orderProductItem.setActivityId(null);
            //服纺券id
            orderProductItem.setSpinCouponId(null);
            //A品券id
            orderProductItem.setTopCouponId(null);

            //订货数量
            orderProductItem.setQuantity(item.getAmount());
            //仓库实发数量
            orderProductItem.setActualDeliverQuantity(null);
            //门店实收数量
            orderProductItem.setActualStoreQuantity(null);

            //订货价
            orderProductItem.setPrice(productInfo.getPrice());
            //含税采购价
            orderProductItem.setTaxPurchasePrice(productInfo.getTaxPurchasePrice());
            //分摊后单价 TODO 计算分摊后单价
            orderProductItem.setSharePrice(productInfo.getPrice());

            //订货金额
            orderProductItem.setMoney(productInfo.getPrice().multiply(new BigDecimal(item.getAmount())).setScale(4, RoundingMode.UP));
            //活动优惠金额
            orderProductItem.setActivityMoney(BigDecimal.ZERO);
            //服纺券优惠金额
            orderProductItem.setSpinCouponMoney(BigDecimal.ZERO);
            //A品券优惠金额
            orderProductItem.setTopCouponMoney(BigDecimal.ZERO);
            //实际支付金额
            orderProductItem.setRealMoney(orderProductItem.getMoney().subtract(orderProductItem.getActivityMoney()).subtract(orderProductItem.getSpinCouponMoney()).subtract(orderProductItem.getTopCouponMoney()));

            orderProductItemList.add(orderProductItem);

            //订货金额汇总
            moneyTotal = moneyTotal.add(orderProductItem.getMoney()==null?BigDecimal.ZERO:orderProductItem.getMoney());
            //实际支付金额汇总
            realMoneyTotal = realMoneyTotal.add(orderProductItem.getRealMoney()==null?BigDecimal.ZERO:orderProductItem.getRealMoney());
            //活动优惠金额汇总
            activityMoneyTotal = activityMoneyTotal.add(orderProductItem.getActivityMoney()==null?BigDecimal.ZERO:orderProductItem.getActivityMoney());
            //服纺券优惠金额汇总
            spinCouponMoneyTotal = spinCouponMoneyTotal.add(orderProductItem.getSpinCouponMoney()==null?BigDecimal.ZERO:orderProductItem.getSpinCouponMoney());
            //A品券优惠金额汇总
            topCouponMoneyTotal = topCouponMoneyTotal.add(orderProductItem.getTopCouponMoney()==null?BigDecimal.ZERO:orderProductItem.getTopCouponMoney());

        }
        erpOrderProductItemService.saveOrderProductItemList(orderProductItemList, auth);

        //保存订单信息
        OrderStoreOrderInfo orderInfo = new OrderStoreOrderInfo();
        orderInfo.setOrderCode(orderCode);
        orderInfo.setOrderId(orderId);
        orderInfo.setPayStatus(PayStatusEnum.UNPAID.getCode());
        orderInfo.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_1.getCode());
        orderInfo.setOrderLevel(OrderLevelEnum.PRIMARY.getCode());
        orderInfo.setActualPrice(realMoneyTotal);
        orderInfo.setTotalPrice(moneyTotal);
        orderInfo.setOrderType(erpOrderSaveRequest.getOrderType());
        orderInfo.setReturnStatus(YesOrNoEnum.NO.getCode());
        orderInfo.setFranchiseeId(storeInfo.getFranchiseeId());
        orderInfo.setStoreId(storeInfo.getStoreId());
        orderInfo.setStoreName(storeInfo.getStoreName());
        erpOrderOperationService.saveOrder(orderInfo, auth);

        //保存订单支付信息
        OrderStoreOrderPay orderPay = new OrderStoreOrderPay();
        orderPay.setOrderId(orderId);
        orderPay.setOrderTotal(moneyTotal);
        orderPay.setPayStatus(orderInfo.getPayStatus());
        orderPay.setPayId(OrderPublic.getUUID());
        orderPay.setActivityCouponMoney(topCouponMoneyTotal);
        orderPay.setActivityMoney(activityMoneyTotal);
        orderPay.setClothCouponMoney(spinCouponMoneyTotal);
        orderPay.setActualMoney(realMoneyTotal);
        erpOrderPayService.saveOrderPay(orderPay, auth);

        //保存订单收货信息
        OrderStoreOrderReceiving orderReceiving = new OrderStoreOrderReceiving();
        orderReceiving.setOrderId(orderId);
        orderReceiving.setReceiveId(OrderPublic.getUUID());
        orderReceiving.setReceiveName(storeInfo.getContacts());
        orderReceiving.setReceiveAddress(storeInfo.getAddress());
        orderReceiving.setReceivePhone(storeInfo.getContactsPhone());
        orderReceiving.setBillStatus(erpOrderSaveRequest.getBillStatus());
        erpOrderReceivingService.saveOrderReceiving(orderReceiving, auth);

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
    private void deleteOrderProductFromCart(String storeId, OrderConfirmResponse orderConfirmResponse) {
        for (CartOrderInfo item:
             orderConfirmResponse.getCartOrderInfos()) {
            cartOrderService.deleteCartInfo(storeId, item.getSkuId(), YesOrNoEnum.YES.getCode());
        }
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
        erpOrderPayService.saveOrderPay(orderPay, auth);

        //保存订单收货信息
        OrderStoreOrderReceiving orderReceiving = new OrderStoreOrderReceiving();
        orderReceiving.setOrderId(orderId);
        orderReceiving.setReceiveId(OrderPublic.getUUID());
        orderReceiving.setReceiveName("张三");
        orderReceiving.setReceiveAddress("北京市朝阳区");
        orderReceiving.setReceivePhone("12345678910");
        orderReceiving.setBillStatus(YesOrNoEnum.YES.getCode());
        erpOrderReceivingService.saveOrderReceiving(orderReceiving, auth);

        //保存订单商品明细
        List<OrderStoreOrderProductItem> orderProductItemList = new ArrayList<>();
        OrderStoreOrderProductItem orderProductItem1 = new OrderStoreOrderProductItem();
        //订单id
        orderProductItem1.setOrderId(orderId);
        //订单编号
        orderProductItem1.setOrderCode(orderCode);
        //订单明细行编号
        orderProductItem1.setOrderItemCode(orderCode + String.format("%03d", 1));

        //商品ID
        orderProductItem1.setProductId("12321232123");
        //商品编码
        orderProductItem1.setProductCode("12343211");
        //商品名称
        orderProductItem1.setProductName("衣服");
        //商品sku码
        orderProductItem1.setSkuCode("12321");
        //商品名称
        orderProductItem1.setSkuName("XXL");
        //单位
        orderProductItem1.setUnit("件");
        //本品赠品标记 ProductGiftEnum
        orderProductItem1.setProductGift(ProductGiftEnum.PRODUCT.getCode());
        //赠品行关联本品行编码
        orderProductItem1.setParentOrderItemCode("");

        //活动id
        orderProductItem1.setActivityId(null);
        //服纺券id
        orderProductItem1.setSpinCouponId(null);
        //A品券id
        orderProductItem1.setTopCouponId(null);

        //订货数量
        orderProductItem1.setQuantity(10);
        //仓库实发数量
        orderProductItem1.setActualDeliverQuantity(null);
        //门店实收数量
        orderProductItem1.setActualStoreQuantity(null);

        //订货价
        orderProductItem1.setPrice(BigDecimal.TEN);
        //含税采购价
        orderProductItem1.setTaxPurchasePrice(BigDecimal.TEN.add(BigDecimal.ONE));
        //分摊后单价 TODO 计算分摊后单价
        orderProductItem1.setSharePrice(BigDecimal.TEN);

        //订货金额
        orderProductItem1.setMoney(BigDecimal.TEN.multiply(new BigDecimal(10)).setScale(4, RoundingMode.UP));
        //活动优惠金额
        orderProductItem1.setActivityMoney(BigDecimal.ZERO);
        //服纺券优惠金额
        orderProductItem1.setSpinCouponMoney(BigDecimal.ZERO);
        //A品券优惠金额
        orderProductItem1.setTopCouponMoney(BigDecimal.ZERO);
        //实际支付金额
        orderProductItem1.setRealMoney(orderProductItem1.getMoney().subtract(orderProductItem1.getActivityMoney()).subtract(orderProductItem1.getSpinCouponMoney()).subtract(orderProductItem1.getTopCouponMoney()));

        //创建人id
        orderProductItem1.setCreateById(auth.getPersonId());
        //创建人姓名
        orderProductItem1.setCreateByName(auth.getPersonName());
        //修改人id
        orderProductItem1.setUpdateById(auth.getPersonId());
        //修改人姓名
        orderProductItem1.setUpdateByName(auth.getPersonName());
        orderProductItemList.add(orderProductItem1);

        OrderStoreOrderProductItem orderProductItem2 = new OrderStoreOrderProductItem();
        //订单id
        orderProductItem2.setOrderId(orderId);
        //订单编号
        orderProductItem2.setOrderCode(orderCode);
        //订单明细行编号
        orderProductItem2.setOrderItemCode(orderCode + String.format("%03d", 2));

        //商品ID
        orderProductItem2.setProductId("12321232123");
        //商品编码
        orderProductItem2.setProductCode("12343211");
        //商品名称
        orderProductItem2.setProductName("衣服");
        //商品sku码
        orderProductItem2.setSkuCode("12321");
        //商品名称
        orderProductItem2.setSkuName("XXL");
        //单位
        orderProductItem2.setUnit("件");
        //本品赠品标记 ProductGiftEnum
        orderProductItem2.setProductGift(ProductGiftEnum.PRODUCT.getCode());
        //赠品行关联本品行编码
        orderProductItem2.setParentOrderItemCode("");

        //活动id
        orderProductItem2.setActivityId(null);
        //服纺券id
        orderProductItem2.setSpinCouponId(null);
        //A品券id
        orderProductItem2.setTopCouponId(null);

        //订货数量
        orderProductItem2.setQuantity(10);
        //仓库实发数量
        orderProductItem2.setActualDeliverQuantity(null);
        //门店实收数量
        orderProductItem2.setActualStoreQuantity(null);

        //订货价
        orderProductItem2.setPrice(BigDecimal.TEN);
        //含税采购价
        orderProductItem2.setTaxPurchasePrice(BigDecimal.TEN.add(BigDecimal.ONE));
        //分摊后单价 TODO 计算分摊后单价
        orderProductItem2.setSharePrice(BigDecimal.TEN);

        //订货金额
        orderProductItem2.setMoney(BigDecimal.TEN.multiply(new BigDecimal(10)).setScale(4, RoundingMode.UP));
        //活动优惠金额
        orderProductItem2.setActivityMoney(BigDecimal.ZERO);
        //服纺券优惠金额
        orderProductItem2.setSpinCouponMoney(BigDecimal.ZERO);
        //A品券优惠金额
        orderProductItem2.setTopCouponMoney(BigDecimal.ZERO);
        //实际支付金额
        orderProductItem2.setRealMoney(orderProductItem2.getMoney().subtract(orderProductItem2.getActivityMoney()).subtract(orderProductItem2.getSpinCouponMoney()).subtract(orderProductItem2.getTopCouponMoney()));
        orderProductItemList.add(orderProductItem2);

        erpOrderProductItemService.saveOrderProductItemList(orderProductItemList, auth);

    }
}
