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
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.mgs.order.api.util.OrderPublic;
import com.aiqin.mgs.order.api.util.PageAutoHelperUtil;
import com.aiqin.mgs.order.api.util.RequestReturnUtil;
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
    public ErpOrderDetailResponse saveOrder(ErpOrderSaveRequest erpOrderSaveRequest) {

        //操作人信息
        AuthToken auth = AuthUtil.getCurrentAuth();
        //校验参数
        validateSaveOrderRequest(erpOrderSaveRequest);

        //获取门店信息
        StoreInfo storeInfo = getStoreInfoByStoreId(erpOrderSaveRequest.getStoreId());

        //获取购物车商品
        OrderConfirmResponse storeCartProduct = getStoreCartProduct(erpOrderSaveRequest.getStoreId());

        //构建订单商品明细行
        List<OrderStoreOrderProductItem> orderProductItemList = generateOrderProductList(storeCartProduct.getCartOrderInfos(), storeInfo);

        //购物车数据校验
        productCheck(erpOrderSaveRequest.getOrderType(), storeInfo, orderProductItemList);

        //生成订单，返回订单号
        String orderCode = generateOrder(orderProductItemList, storeInfo, erpOrderSaveRequest, auth);

        //删除购物车商品
        deleteOrderProductFromCart(erpOrderSaveRequest.getStoreId(), storeCartProduct);

        //返回订单明细
        OrderStoreOrderInfo createOrderQuery = new OrderStoreOrderInfo();
        createOrderQuery.setOrderCode(orderCode);
        return getOrderDetail(createOrderQuery);
    }

    @Override
    public ErpOrderDetailResponse saveRackOrder(ErpOrderSaveRequest erpOrderSaveRequest) {
        //操作人信息
        AuthToken auth = AuthUtil.getCurrentAuth();
        //校验参数
        validateSaveOrderRequest(erpOrderSaveRequest);
        //获取门店信息
        StoreInfo storeInfo = getStoreInfoByStoreId(erpOrderSaveRequest.getStoreId());
        //商品参数信息
        List<OrderStoreOrderProductItem> productList = erpOrderSaveRequest.getProductList();

        //构建货架订单商品信息行
        List<OrderStoreOrderProductItem> orderProductItemList = generateRackOrderProductList(productList, storeInfo);
        //构建和保存货架订单，返回订单编号
        String orderCode = generateRackOrder(erpOrderSaveRequest, orderProductItemList, storeInfo, auth);

        //返回订单明细
        OrderStoreOrderInfo createOrderQuery = new OrderStoreOrderInfo();
        createOrderQuery.setOrderCode(orderCode);
        return getOrderDetail(createOrderQuery);
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
        AuthToken auth = AuthUtil.getCurrentAuth();
        if (auth.getPersonId() == null) {
            throw new BusinessException("请先登录");
        }
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
    }

    /**
     * 从购物车获取选择的门店的勾选的商品
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
     * 构建订单商品明细行数据
     *
     * @param cartProductList 购物车商品行
     * @param storeInfo       门店信息
     * @return java.util.List<com.aiqin.mgs.order.api.domain.OrderStoreOrderProductItem>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/27 19:02
     */
    private List<OrderStoreOrderProductItem> generateOrderProductList(List<CartOrderInfo> cartProductList, StoreInfo storeInfo) {

        //商品详情Map
        Map<String, ProductInfo> productMap = new HashMap<>(16);

        //遍历参数商品列表，获取商品详情，校验数据
        for (CartOrderInfo item :
                cartProductList) {
            //获取商品详情
            ProductInfo product = getProductDetail(storeInfo.getStoreId(), item.getProductId(), item.getSkuId());
            if (product == null) {
                throw new BusinessException("未获取到商品" + item.getProductName() + "的信息");
            }
            productMap.put(item.getSkuId(), product);
        }

        //订单商品明细行
        List<OrderStoreOrderProductItem> orderProductItemList = new ArrayList<>();
        //遍历参数商品列表，构建订单商品明细行
        for (CartOrderInfo item :
                cartProductList) {
            ProductInfo productInfo = productMap.get(item.getSkuId());

            OrderStoreOrderProductItem orderProductItem = new OrderStoreOrderProductItem();

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
            orderProductItem.setPrice(item.getPrice());
            //含税采购价
//            orderProductItem.setTaxPurchasePrice();
            //分摊后单价 需要单独计算
            orderProductItem.setSharePrice(item.getPrice());

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
        }

        return orderProductItemList;
    }

    /**
     * 商品校验
     *
     * @param orderType            订单类型
     * @param storeInfo            门店信息
     * @param orderProductItemList 订单商品明细行
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/27 19:03
     */
    private void productCheck(Integer orderType, StoreInfo storeInfo, List<OrderStoreOrderProductItem> orderProductItemList) {

        //订单类型
        OrderTypeEnum orderTypeEnum = OrderTypeEnum.getEnum(orderType);
        if (orderTypeEnum == null) {
            throw new BusinessException("无效的订单类型");
        }

        if (orderTypeEnum.isActivityCheck()) {
            //TODO 活动校验
        }
        if (orderTypeEnum.isAreaCheck()) {
            //TODO 商品销售区域配置校验
        }
        if (orderTypeEnum.isPriceCheck()) {
            //TODO 商品销售价格校验
        }
        if (orderTypeEnum.isRepertoryCheck()) {
            //TODO 商品库存校验
        }

    }

    /**
     * 构建订单信息
     *
     * @param orderProductItemList 订单商品明细行
     * @param storeInfo            门店信息
     * @param erpOrderSaveRequest  保存订单参数
     * @param auth                 操作人信息
     * @return java.lang.String
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/27 19:04
     */
    private String generateOrder(List<OrderStoreOrderProductItem> orderProductItemList, StoreInfo storeInfo, ErpOrderSaveRequest erpOrderSaveRequest, AuthToken auth) {

        //生成订单id
        String orderId = OrderPublic.getUUID();
        //生成订单code
        String orderCode = OrderPublic.generateOrderCode(erpOrderSaveRequest.getOrderOriginType(), erpOrderSaveRequest.getOrderChannel());

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
        for (OrderStoreOrderProductItem item :
                orderProductItemList) {

            //订单id
            item.setOrderId(orderId);
            //订单编号
            item.setOrderCode(orderCode);
            //订单明细行编号
            item.setOrderItemCode(orderCode + String.format("%03d", orderItemNum++));


            //订货金额汇总
            moneyTotal = moneyTotal.add(item.getMoney() == null ? BigDecimal.ZERO : item.getMoney());
            //实际支付金额汇总
            realMoneyTotal = realMoneyTotal.add(item.getRealMoney() == null ? BigDecimal.ZERO : item.getRealMoney());
            //活动优惠金额汇总
            activityMoneyTotal = activityMoneyTotal.add(item.getActivityMoney() == null ? BigDecimal.ZERO : item.getActivityMoney());
            //服纺券优惠金额汇总
            spinCouponMoneyTotal = spinCouponMoneyTotal.add(item.getSpinCouponMoney() == null ? BigDecimal.ZERO : item.getSpinCouponMoney());
            //A品券优惠金额汇总
            topCouponMoneyTotal = topCouponMoneyTotal.add(item.getTopCouponMoney() == null ? BigDecimal.ZERO : item.getTopCouponMoney());
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

        return orderCode;
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
        for (CartOrderInfo item :
                orderConfirmResponse.getCartOrderInfos()) {
            cartOrderService.deleteCartInfo(storeId, item.getSkuId(), YesOrNoEnum.YES.getCode());
        }
    }

    /**
     * 构建货架订单商品信息行数据
     *
     * @param productList 货架商品参数
     * @param storeInfo   门店信息
     * @return java.util.List<com.aiqin.mgs.order.api.domain.OrderStoreOrderProductItem>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/27 17:43
     */
    private List<OrderStoreOrderProductItem> generateRackOrderProductList(List<OrderStoreOrderProductItem> productList, StoreInfo storeInfo) {

        if (productList == null || productList.size() == 0) {
            throw new BusinessException("缺少商品数据");
        }

        //商品详情Map
        Map<String, ProductInfo> productMap = new HashMap<>(16);

        //参数商品行标识
        int lineIndex = 0;

        //遍历参数商品列表，获取商品详情，校验数据
        for (OrderStoreOrderProductItem item :
                productList) {
            lineIndex++;
            if (StringUtils.isEmpty(item.getProductId())) {
                throw new BusinessException("第" + lineIndex + "行商品id为空");
            }
            if (StringUtils.isEmpty(item.getSkuCode())) {
                throw new BusinessException("第" + lineIndex + "行商品sku为空");
            }
            if (item.getTaxPurchasePrice() == null) {
                throw new BusinessException("第" + lineIndex + "行商品含税采购价为空");
            }
            if (item.getPrice() == null) {
                throw new BusinessException("第" + lineIndex + "行商品销售价为空");
            }
            if (item.getQuantity() == null) {
                throw new BusinessException("第" + lineIndex + "行商品数量为空");
            }

            //获取商品详情
            ProductInfo product = getProductDetail(storeInfo.getStoreId(), item.getProductId(), item.getSkuCode());
            if (product == null) {
                throw new BusinessException("第" + lineIndex + "行商品不存在");
            }

            productMap.put(item.getSkuCode(), product);
        }

        //订单商品明细行
        List<OrderStoreOrderProductItem> orderProductItemList = new ArrayList<>();
        //遍历参数商品列表，构建订单商品明细行
        for (OrderStoreOrderProductItem item :
                productList) {
            ProductInfo productInfo = productMap.get(item.getSkuCode());

            OrderStoreOrderProductItem orderProductItem = new OrderStoreOrderProductItem();

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
            orderProductItem.setQuantity(item.getQuantity());
            //仓库实发数量
            orderProductItem.setActualDeliverQuantity(null);
            //门店实收数量
            orderProductItem.setActualStoreQuantity(null);

            //订货价
            orderProductItem.setPrice(item.getPrice());
            //含税采购价
            orderProductItem.setTaxPurchasePrice(item.getTaxPurchasePrice());
            //分摊后单价 货架订单没有活动、优惠，分摊价等于原价
            orderProductItem.setSharePrice(item.getPrice());

            //订货金额
            orderProductItem.setMoney(item.getPrice().multiply(new BigDecimal(item.getQuantity())).setScale(4, RoundingMode.UP));
            //活动优惠金额
            orderProductItem.setActivityMoney(BigDecimal.ZERO);
            //服纺券优惠金额
            orderProductItem.setSpinCouponMoney(BigDecimal.ZERO);
            //A品券优惠金额
            orderProductItem.setTopCouponMoney(BigDecimal.ZERO);
            //实际支付金额
            orderProductItem.setRealMoney(orderProductItem.getMoney().subtract(orderProductItem.getActivityMoney()).subtract(orderProductItem.getSpinCouponMoney()).subtract(orderProductItem.getTopCouponMoney()));

            orderProductItemList.add(orderProductItem);
        }

        return orderProductItemList;
    }

    /**
     * 构建和保存货架订单数据
     *
     * @param erpOrderSaveRequest  入口参数
     * @param orderProductItemList 订单商品明细行
     * @param storeInfo            门店信息
     * @param auth                 操作人信息
     * @return java.lang.String
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/27 17:50
     */
    private String generateRackOrder(ErpOrderSaveRequest erpOrderSaveRequest, List<OrderStoreOrderProductItem> orderProductItemList, StoreInfo storeInfo, AuthToken auth) {
        //生成订单id
        String orderId = OrderPublic.getUUID();
        //生成订单code
        String orderCode = OrderPublic.generateOrderCode(erpOrderSaveRequest.getOrderOriginType(), erpOrderSaveRequest.getOrderChannel());

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

        //遍历商品订单行，汇总价格
        for (OrderStoreOrderProductItem item :
                orderProductItemList) {

            //订单id
            item.setOrderId(orderId);
            //订单编号
            item.setOrderCode(orderCode);
            //订单明细行编号
            item.setOrderItemCode(orderCode + String.format("%03d", orderItemNum++));

            //订货金额汇总
            moneyTotal = moneyTotal.add(item.getMoney() == null ? BigDecimal.ZERO : item.getMoney());
            //实际支付金额汇总
            realMoneyTotal = realMoneyTotal.add(item.getRealMoney() == null ? BigDecimal.ZERO : item.getRealMoney());
            //活动优惠金额汇总
            activityMoneyTotal = activityMoneyTotal.add(item.getActivityMoney() == null ? BigDecimal.ZERO : item.getActivityMoney());
            //服纺券优惠金额汇总
            spinCouponMoneyTotal = spinCouponMoneyTotal.add(item.getSpinCouponMoney() == null ? BigDecimal.ZERO : item.getSpinCouponMoney());
            //A品券优惠金额汇总
            topCouponMoneyTotal = topCouponMoneyTotal.add(item.getTopCouponMoney() == null ? BigDecimal.ZERO : item.getTopCouponMoney());
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

        return orderCode;
    }

    /**
     * 获取商品详情信息
     *
     * @param storeId   门店id
     * @param productId 商品id
     * @param skuCode   商品sku
     * @return com.aiqin.mgs.order.api.domain.ProductInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/27 16:21
     */
    private ProductInfo getProductDetail(String storeId, String productId, String skuCode) {
        ProductInfo product = new ProductInfo();
        try {
            product.setProductId(productId);
            product.setProductCode(productId + System.currentTimeMillis());
            product.setProductName("商品名称" + System.currentTimeMillis());
            product.setSkuCode(skuCode);
            product.setSkuName(skuCode + System.currentTimeMillis());
            product.setUnit("个");
            product.setPrice(BigDecimal.TEN);
            product.setTaxPurchasePrice(BigDecimal.TEN.add(BigDecimal.ONE));

            //TODO 从供应链获取商品详情
//            Map<String, Object> paramMap = new HashMap<>();
//            paramMap.put("storeId", storeId);
//            paramMap.put("productId", productId);
//            paramMap.put("skuCode", skuCode);
//            HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + "/test").json(paramMap);
//            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
//            });
        } catch (Exception e) {
            logger.error("获取商品信息失败：{}", e);
            throw new BusinessException("获取商品详情信息失败");
        }
        return product;
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
}
