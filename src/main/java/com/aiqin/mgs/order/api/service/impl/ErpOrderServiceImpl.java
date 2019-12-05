package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.PagesRequest;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.dao.OrderStoreOrderInfoDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.request.ErpOrderSaveRequest;
import com.aiqin.mgs.order.api.domain.response.ErpOrderDetailResponse;
import com.aiqin.mgs.order.api.domain.response.ErpOrderPayStatusResponse;
import com.aiqin.mgs.order.api.domain.response.cart.OrderConfirmResponse;
import com.aiqin.mgs.order.api.service.*;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.mgs.order.api.util.OrderPublic;
import com.aiqin.mgs.order.api.util.PageAutoHelperUtil;
import com.aiqin.mgs.order.api.util.RequestReturnUtil;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

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
    private ErpOrderRequestService erpOrderRequestService;

    @Resource
    private CartOrderService cartOrderService;

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
                    ErpOrderPayStatusResponse orderPayStatusResponse = erpOrderRequestService.getOrderPayStatus(item.getOrderCode());
                    if (orderPayStatusResponse.isRequestSuccess() && PayStatusEnum.SUCCESS == orderPayStatusResponse.getPayStatusEnum()) {
                        //如果支付状态是成功的
                        item.setRepayOperation(YesOrNoEnum.YES.getCode());
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
    public OrderStoreOrderInfo getOrderDetail2(OrderStoreOrderInfo orderStoreOrderInfo) {

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

        //查询订单商品行
        List<OrderStoreOrderProductItem> orderProductItemList = erpOrderProductItemService.selectOrderProductListByOrderId(order.getOrderId());
        order.setProductItemList(orderProductItemList);

        //查询订单支付信息
        OrderStoreOrderPay orderPay = erpOrderPayService.getOrderPayByOrderId(order.getOrderId());
        order.setOrderPay(orderPay);

        //查询收货信息
        OrderStoreOrderReceiving orderReceiving = erpOrderReceivingService.getOrderReceivingByOrderId(order.getOrderId());
        order.setOrderReceiving(orderReceiving);

        //查询订单发货信息
        OrderStoreOrderSending orderSending = erpOrderSendingService.getOrderSendingByOrderId(order.getOrderId());
        order.setOrderSending(orderSending);

        //查询订单操作日志
        List<OrderStoreOrderOperationLog> orderOperationLogList = erpOrderOperationLogService.selectOperationLogListByOrderId(order.getOrderId());
        order.setOrderOperationLogList(orderOperationLogList);

        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderStoreOrderInfo saveDistributionOrder(ErpOrderSaveRequest erpOrderSaveRequest) {

        //操作人信息
        AuthToken auth = AuthUtil.getCurrentAuth();
        //校验参数
        validateSaveOrderRequest(erpOrderSaveRequest);

        //获取门店信息
        StoreInfo storeInfo = erpOrderRequestService.getStoreInfoByStoreId(erpOrderSaveRequest.getStoreId());

        //获取购物车商品
        OrderConfirmResponse storeCartProduct = getStoreCartProduct(erpOrderSaveRequest.getStoreId());

        //构建订单商品明细行
        List<OrderStoreOrderProductItem> orderProductItemList = generateDistributionOrderProductList(storeCartProduct.getCartOrderInfos(), storeInfo);

        //购物车数据校验
        productCheck(erpOrderSaveRequest.getOrderType(), storeInfo, orderProductItemList);

        //生成订单，返回订单号
        String orderCode = generateDistributionOrder(orderProductItemList, storeInfo, erpOrderSaveRequest, auth);

        //删除购物车商品
        deleteOrderProductFromCart(erpOrderSaveRequest.getStoreId(), storeCartProduct);

        //返回订单
        OrderStoreOrderInfo newOrderInfo = erpOrderQueryService.getOrderByOrderCode(orderCode);
        return newOrderInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderStoreOrderInfo saveRackOrder(ErpOrderSaveRequest erpOrderSaveRequest) {
        //操作人信息
        AuthToken auth = AuthUtil.getCurrentAuth();
        //校验参数
        validateSaveOrderRequest(erpOrderSaveRequest);
        //获取门店信息
        StoreInfo storeInfo = erpOrderRequestService.getStoreInfoByStoreId(erpOrderSaveRequest.getStoreId());
        //商品参数信息
        List<OrderStoreOrderProductItem> productList = erpOrderSaveRequest.getProductList();

        //构建货架订单商品信息行
        List<OrderStoreOrderProductItem> orderProductItemList = generateRackOrderProductList(productList, storeInfo);
        //构建和保存货架订单，返回订单编号
        String orderCode = generateRackOrder(erpOrderSaveRequest, orderProductItemList, storeInfo, auth);

        //返回订单
        OrderStoreOrderInfo newOrderInfo = erpOrderQueryService.getOrderByOrderCode(orderCode);
        return newOrderInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderSplit(OrderStoreOrderInfo orderStoreOrderInfo) {

        AuthToken auth = AuthUtil.getSystemAuth();
        String orderCode = orderStoreOrderInfo.getOrderCode();
        if (StringUtils.isEmpty(orderCode)) {
            throw new BusinessException("订单号为空");
        }

        //原订单
        OrderStoreOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }
        //原订单商品明细
        List<OrderStoreOrderProductItem> orderProductItemList = erpOrderProductItemService.selectOrderProductListByOrderId(order.getOrderId());
        //原订单收货人信息
        OrderStoreOrderReceiving orderReceiving = erpOrderReceivingService.getOrderReceivingByOrderId(order.getOrderId());
        //原订单日志
        List<OrderStoreOrderOperationLog> orderOperationLogList = erpOrderOperationLogService.selectOperationLogListByOrderId(order.getOrderId());

        if (!ErpOrderStatusEnum.ORDER_STATUS_2.getCode().equals(order.getOrderStatus()) && !ErpOrderStatusEnum.ORDER_STATUS_92.getCode().equals(order.getOrderStatus()) && !ErpOrderStatusEnum.ORDER_STATUS_93.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException("只有支付成功且未拆分的的订单才能拆分");
        }
        OrderTypeEnum orderTypeEnum = OrderTypeEnum.getEnum(order.getOrderType());
        if (orderTypeEnum == null) {
            throw new BusinessException("订单类型异常");
        }

        if (ProductTypeEnum.STORAGE_RACK == orderTypeEnum.getProductTypeEnum() || ProductTypeEnum.DIRECT_SEND == orderTypeEnum.getProductTypeEnum()) {
            //按照供应商拆分订单

//            //sku - 商品详情 map
//            Map<String, ProductInfo> skuProductMap = new HashMap<>(16);
//            for (OrderStoreOrderProductItem item :
//                    orderProductItemList) {
//                if (!skuProductMap.containsKey(item.getSkuCode())) {
//                    ProductInfo productInfo = erpOrderRequestService.getProductDetail(order.getStoreId(), item.getProductId(), item.getSkuCode());
//                    if (productInfo == null) {
//                        throw new BusinessException("未找到商品" + item.getProductName() + item.getSkuName());
//                    }
//                    skuProductMap.put(productInfo.getSkuCode(), productInfo);
//                }
//            }
//
//            //供应商编码-订单商品行 Map
//            Map<String, List<OrderStoreOrderProductItem>> supplierItemMap = new LinkedHashMap<>(16);
//            //供应商编码-供应商名称 Map
//            Map<String, String> supplierCodeNameMap = new LinkedHashMap<>(16);
//
//            for (OrderStoreOrderProductItem item :
//                    orderProductItemList) {
//                ProductInfo productInfo = skuProductMap.get(item.getSkuCode());
//                //供应商编码
//                String supplierCode = productInfo.getSupplierCode();
//                if (!supplierCodeNameMap.containsKey(supplierCode)) {
//                    supplierCodeNameMap.put(supplierCode, productInfo.getSupplierName());
//                }
//
//                List<OrderStoreOrderProductItem> list = new ArrayList<>();
//                if (supplierItemMap.containsKey(supplierCode)) {
//                    list.addAll(supplierItemMap.get(supplierCode));
//                }
//                list.add(item);
//                supplierItemMap.put(supplierCode, list);
//            }
//
//            if (supplierItemMap.size() > 1) {
//
//                //拆单
//                for (Map.Entry<String, List<OrderStoreOrderProductItem>> entry :
//                        supplierItemMap.entrySet()) {
//                    List<OrderStoreOrderProductItem> splitOrderProductItemList = entry.getValue();
//
//                    //生成订单id
//                    String newOrderId = OrderPublic.getUUID();
//                    //生成订单code
//                    String newOrderCode = OrderPublic.generateOrderCode(order.getOrderOriginType(), order.getOrderChannel());
//
//                    //订货金额汇总
//                    BigDecimal moneyTotal = BigDecimal.ZERO;
//                    //实际支付金额汇总
//                    BigDecimal realMoneyTotal = BigDecimal.ZERO;
//                    //活动优惠金额汇总
//                    BigDecimal activityMoneyTotal = BigDecimal.ZERO;
//                    //服纺券优惠金额汇总
//                    BigDecimal spinCouponMoneyTotal = BigDecimal.ZERO;
//                    //A品券优惠金额汇总
//                    BigDecimal topCouponMoneyTotal = BigDecimal.ZERO;
//                    int orderItemNum = 1;
//                    for (OrderStoreOrderProductItem item :
//                            splitOrderProductItemList) {
//
//                        //订单id
//                        item.setOrderId(newOrderId);
//                        //订单编号
//                        item.setOrderCode(newOrderCode);
//                        //订单明细行编号
//                        item.setOrderItemCode(newOrderCode + String.format("%03d", orderItemNum++));
//
//                        //订货金额汇总
//                        moneyTotal = moneyTotal.add(item.getMoney() == null ? BigDecimal.ZERO : item.getMoney());
//                        //实际支付金额汇总
//                        realMoneyTotal = realMoneyTotal.add(item.getRealMoney() == null ? BigDecimal.ZERO : item.getRealMoney());
////                        //活动优惠金额汇总
////                        activityMoneyTotal = activityMoneyTotal.add(item.getActivityMoney() == null ? BigDecimal.ZERO : item.getActivityMoney());
////                        //服纺券优惠金额汇总
////                        spinCouponMoneyTotal = spinCouponMoneyTotal.add(item.getSpinCouponMoney() == null ? BigDecimal.ZERO : item.getSpinCouponMoney());
////                        //A品券优惠金额汇总
////                        topCouponMoneyTotal = topCouponMoneyTotal.add(item.getTopCouponMoney() == null ? BigDecimal.ZERO : item.getTopCouponMoney())
//                    }
//                    erpOrderProductItemService.saveOrderProductItemList(orderProductItemList, auth);
//
//                    //保存订单信息
//                    OrderStoreOrderInfo orderInfo = new OrderStoreOrderInfo();
//                    orderInfo.setOrderCode(newOrderCode);
//                    orderInfo.setOrderId(newOrderId);
//                    orderInfo.setPayStatus(order.getPayStatus());
//                    orderInfo.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_4.getCode());
//                    orderInfo.setActualPrice(realMoneyTotal);
//                    orderInfo.setTotalPrice(moneyTotal);
//                    orderInfo.setOrderType(order.getOrderType());
//                    orderInfo.setReturnStatus(YesOrNoEnum.NO.getCode());
//                    orderInfo.setFranchiseeId(order.getFranchiseeId());
//                    orderInfo.setStoreId(order.getStoreId());
//                    orderInfo.setStoreName(order.getStoreName());
//                    orderInfo.setOrderLevel(OrderLevelEnum.SECONDARY.getCode());
//                    orderInfo.setSplitStatus(YesOrNoEnum.NO.getCode());
//                    orderInfo.setPrimaryCode(order.getOrderCode());
//                    orderInfo.setSupplierCode(entry.getKey());
//                    orderInfo.setSupplierCode(supplierCodeNameMap.get(entry.getKey()));
//                    erpOrderOperationService.saveOrder(orderInfo, auth);
//                }
//            }
//            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_4.getCode());
//            erpOrderOperationService.updateOrderByPrimaryKeySelective(order, auth);
        } else {
            //按照库存拆分

            List<OrderStoreOrderProductItem> paramProductItemList = orderStoreOrderInfo.getProductItemList();

            if (paramProductItemList == null || paramProductItemList.size() <= 0) {
                throw new BusinessException("订单商品明细行为空");
            }

            //订单行号-(仓库编码-数量)
            Map<String, Map<String, Integer>> itemCodeRepertoryQuantityMap = new HashMap<>(16);
            //仓库编码-仓库名称
            Map<String, String> repertoryCodeNameMap = new HashMap<>(16);
            //分组Map
            Map<String, List<OrderStoreOrderProductItem>> splitMap = new HashMap<>(16);

            int lineIndex = 0;
            //遍历参数商品行
            for (OrderStoreOrderProductItem item :
                    paramProductItemList) {
                lineIndex++;
                if (item == null) {
                    throw new BusinessException("第" + lineIndex + "行参数为空");
                }
                if (StringUtils.isEmpty(item.getOrderItemCode())) {
                    throw new BusinessException("第" + lineIndex + "行商品明细行编号为空");
                }
                if (StringUtils.isEmpty(item.getRepertoryCode())) {
                    throw new BusinessException("第" + lineIndex + "行仓库编码为空");
                }
                if (StringUtils.isEmpty(item.getRepertoryName())) {
                    throw new BusinessException("第" + lineIndex + "行仓库名称为空");
                }
                if (item.getQuantity() == null) {
                    throw new BusinessException("第" + lineIndex + "行商品数量为空");
                } else {
                    if (item.getQuantity() <= 0) {
                        throw new BusinessException("第" + lineIndex + "行商品数量必须大于0");
                    }
                }

                //记录发货仓库编码名称
                if (!repertoryCodeNameMap.containsKey(item.getRepertoryCode())) {
                    repertoryCodeNameMap.put(item.getRepertoryCode(), item.getRepertoryName());
                }

                //记录商品行拆分库房数量
                Map<String, Integer> repertoryQuantityMap = itemCodeRepertoryQuantityMap.containsKey(item.getOrderItemCode()) ? itemCodeRepertoryQuantityMap.get(item.getOrderItemCode()) : new HashMap<>();
                repertoryQuantityMap.put(item.getRepertoryCode(), (repertoryQuantityMap.get(item.getRepertoryCode()) != null ? repertoryQuantityMap.get(item.getRepertoryCode()) : 0) + item.getQuantity());
                itemCodeRepertoryQuantityMap.put(item.getOrderItemCode(), repertoryQuantityMap);
            }

            //遍历原订单商品行
            for (OrderStoreOrderProductItem item :
                    orderProductItemList) {
                if (!itemCodeRepertoryQuantityMap.containsKey(item.getOrderItemCode())) {
                    throw new BusinessException("缺少商品行" + item.getOrderItemCode() + "的库存信息");
                }

                Map<String, Integer> repertoryQuantityMap = itemCodeRepertoryQuantityMap.get(item.getOrderItemCode());
                Integer quantityTotal = 0;
                for (Map.Entry<String, Integer> entry :
                        repertoryQuantityMap.entrySet()) {
                    quantityTotal += entry.getValue();

                    List<OrderStoreOrderProductItem> splitItemList = new ArrayList<>();
                    if (splitMap.containsKey(entry.getKey())) {
                        splitItemList.addAll(splitMap.get(entry.getKey()));
                    }
                    OrderStoreOrderProductItem splitItem = new OrderStoreOrderProductItem();
                    try {
                        PropertyUtils.copyProperties(splitItem, item);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    splitItem.setId(null);
                    splitItem.setQuantity(entry.getValue());
                    splitItem.setMoney(splitItem.getPrice().multiply(new BigDecimal(splitItem.getQuantity())));

                    //TODO 尾差处理 如果有活动或者优惠券的情况下可能会除不尽
                    splitItem.setRealMoney(item.getRealMoney().multiply(new BigDecimal(entry.getValue())).divide(BigDecimal.TEN, 4, RoundingMode.HALF_UP));
                    splitItem.setActivityMoney(item.getActivityMoney().multiply(new BigDecimal(entry.getValue())).divide(BigDecimal.TEN, 4, RoundingMode.HALF_UP));
                    splitItem.setSpinCouponMoney(item.getSpinCouponMoney().multiply(new BigDecimal(entry.getValue())).divide(BigDecimal.TEN, 4, RoundingMode.HALF_UP));
                    splitItem.setTopCouponMoney(item.getTopCouponMoney().multiply(new BigDecimal(entry.getValue())).divide(BigDecimal.TEN, 4, RoundingMode.HALF_UP));

                    splitItemList.add(splitItem);
                    splitMap.put(entry.getKey(), splitItemList);
                }
                if (!quantityTotal.equals(item.getQuantity())) {
                    throw new BusinessException("商品行" + item.getOrderItemCode() + "数量汇总与原数量不相等");
                }

            }

            //更新订单状态为拆分中
            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_3.getCode());
            erpOrderOperationService.updateOrderByPrimaryKeySelective(order, auth);


            List<OrderStoreOrderInfo> newSplitOrderList = new ArrayList<>();
            //是否拆分
            boolean splitFlag = false;
            if (splitMap.size() == 1) {
                //不拆分

            } else {
                //拆分订单

                splitFlag = true;
                for (Map.Entry<String, List<OrderStoreOrderProductItem>> entry :
                        splitMap.entrySet()) {

                    List<OrderStoreOrderProductItem> splitOrderProductItemList = entry.getValue();

                    //生成订单id
                    String newOrderId = OrderPublic.getUUID();
                    //生成订单code
                    String newOrderCode = OrderPublic.generateOrderCode(order.getOrderOriginType(), order.getOrderChannel());

                    //订货金额汇总
                    BigDecimal moneyTotal = BigDecimal.ZERO;
                    //实际支付金额汇总
                    BigDecimal realMoneyTotal = BigDecimal.ZERO;
//                    //活动优惠金额汇总
//                    BigDecimal activityMoneyTotal = BigDecimal.ZERO;
//                    //服纺券优惠金额汇总
//                    BigDecimal spinCouponMoneyTotal = BigDecimal.ZERO;
//                    //A品券优惠金额汇总
//                    BigDecimal topCouponMoneyTotal = BigDecimal.ZERO;
                    int orderItemNum = 1;
                    for (OrderStoreOrderProductItem item :
                            splitOrderProductItemList) {

                        //订单id
                        item.setOrderId(newOrderId);
                        //订单编号
                        item.setOrderCode(newOrderCode);
                        //订单明细行编号
                        item.setOrderItemCode(newOrderCode + String.format("%03d", orderItemNum++));

                        //订货金额汇总
                        moneyTotal = moneyTotal.add(item.getMoney() == null ? BigDecimal.ZERO : item.getMoney());
                        //实际支付金额汇总
                        realMoneyTotal = realMoneyTotal.add(item.getRealMoney() == null ? BigDecimal.ZERO : item.getRealMoney());
//                    //活动优惠金额汇总
//                    activityMoneyTotal = activityMoneyTotal.add(item.getActivityMoney() == null ? BigDecimal.ZERO : item.getActivityMoney());
//                    //服纺券优惠金额汇总
//                    spinCouponMoneyTotal = spinCouponMoneyTotal.add(item.getSpinCouponMoney() == null ? BigDecimal.ZERO : item.getSpinCouponMoney());
//                    //A品券优惠金额汇总
//                    topCouponMoneyTotal = topCouponMoneyTotal.add(item.getTopCouponMoney() == null ? BigDecimal.ZERO : item.getTopCouponMoney())
                    }
                    erpOrderProductItemService.saveOrderProductItemList(splitOrderProductItemList, auth);

                    //保存新订单信息
                    OrderStoreOrderInfo newOrder = new OrderStoreOrderInfo();
                    newOrder.setOrderCode(newOrderCode);
                    newOrder.setOrderId(newOrderId);
                    newOrder.setPayStatus(order.getPayStatus());
                    newOrder.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_3.getCode());
                    newOrder.setActualPrice(realMoneyTotal);
                    newOrder.setTotalPrice(moneyTotal);
                    newOrder.setOrderType(order.getOrderType());
                    newOrder.setReturnStatus(YesOrNoEnum.NO.getCode());
                    newOrder.setFranchiseeId(order.getFranchiseeId());
                    newOrder.setStoreId(order.getStoreId());
                    newOrder.setStoreName(order.getStoreName());
                    newOrder.setOrderLevel(OrderLevelEnum.SECONDARY.getCode());
                    newOrder.setSplitStatus(YesOrNoEnum.NO.getCode());
                    newOrder.setPrimaryCode(order.getOrderCode());
                    newOrder.setRepertoryCode(entry.getKey());
                    newOrder.setRepertoryName(repertoryCodeNameMap.get(entry.getKey()));
                    erpOrderOperationService.saveOrderNoLog(newOrder, auth);

                    //保存收货人信息
                    OrderStoreOrderReceiving newOrderReceiving = new OrderStoreOrderReceiving();
                    newOrderReceiving.setOrderId(newOrderId);
                    newOrderReceiving.setReceiveId(OrderPublic.getUUID());
                    newOrderReceiving.setReceiveName(orderReceiving.getReceiveName());
                    newOrderReceiving.setReceiveAddress(orderReceiving.getReceiveAddress());
                    newOrderReceiving.setReceivePhone(orderReceiving.getReceivePhone());
                    newOrderReceiving.setBillStatus(orderReceiving.getBillStatus());
                    erpOrderReceivingService.saveOrderReceiving(newOrderReceiving, auth);

                    //复制日志
                    erpOrderOperationLogService.copySplitOrderLog(newOrderId, orderOperationLogList);

                    OrderStoreOrderInfo orderByOrderId = erpOrderQueryService.getOrderByOrderId(newOrderId);
                    orderByOrderId.setOrderReceiving(newOrderReceiving);
                    orderByOrderId.setProductItemList(splitOrderProductItemList);
                    newSplitOrderList.add(orderByOrderId);
                }

            }

            //订单同步
            if (splitFlag) {
                for (OrderStoreOrderInfo item :
                        newSplitOrderList) {
                    item.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_4.getCode());
                    erpOrderOperationService.updateOrderByPrimaryKeySelective(item, auth);
                }
                order.setSplitStatus(YesOrNoEnum.YES.getCode());
            }
            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_4.getCode());
            erpOrderOperationService.updateOrderByPrimaryKeySelective(order, auth);

            //调用同步到供应链接口
            erpOrderRequestService.sendSplitOrderToSupplyChain(order, newSplitOrderList);

            if (splitFlag) {
                for (OrderStoreOrderInfo item :
                        newSplitOrderList) {
                    item.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_6.getCode());
                    erpOrderOperationService.updateOrderByPrimaryKeySelective(item, auth);
                }
            } else {
                order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_6.getCode());
                erpOrderOperationService.updateOrderByPrimaryKeySelective(order, auth);
            }
        }

    }

    @Override
    public void orderSend(OrderStoreOrderInfo orderStoreOrderInfo) {

        //登录检查
        AuthUtil.loginCheck();
        AuthToken auth = AuthUtil.getCurrentAuth();

        //校验参数
        if (orderStoreOrderInfo == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(orderStoreOrderInfo.getOrderCode())) {
            throw new BusinessException("缺失订单编号");
        }
        OrderStoreOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderStoreOrderInfo.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }
        if (!ErpOrderStatusEnum.ORDER_STATUS_6.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException(ErpOrderStatusEnum.getEnumDesc(order.getOrderStatus()) + "状态的订单不能发货");
        }
        List<OrderStoreOrderProductItem> paramProductItemList = orderStoreOrderInfo.getProductItemList();
        if (paramProductItemList == null || paramProductItemList.size() <= 0) {
            throw new BusinessException("缺失订单商品行");
        }
        //校验物流信息参数
        OrderStoreOrderSending paramOrderSending = orderStoreOrderInfo.getOrderSending();
        if (paramOrderSending == null) {
            throw new BusinessException("缺失物流信息");
        } else {
            if (StringUtils.isEmpty(paramOrderSending.getLogisticCode())) {
                throw new BusinessException("缺失物流单号");
            }
            if (StringUtils.isEmpty(paramOrderSending.getLogisticCentreCode())) {
                throw new BusinessException("缺失物流公司编码");
            }
            if (StringUtils.isEmpty(paramOrderSending.getLogisticCentreName())) {
                throw new BusinessException("缺失物流公司名称");
            }
            if (paramOrderSending.getLogisticFee() == null) {
                throw new BusinessException("缺失物流费用");
            }
            if (StringUtils.isEmpty(paramOrderSending.getSendRepertoryCode())) {
                throw new BusinessException("订单发货仓库编码");
            }
            if (StringUtils.isEmpty(paramOrderSending.getSendRepertoryName())) {
                throw new BusinessException("订单发货仓库名称");
            }
        }

        //待发货订单商品明细行
        List<OrderStoreOrderProductItem> orderProductItemList = erpOrderProductItemService.selectOrderProductListByOrderId(order.getOrderId());
        Map<String, OrderStoreOrderProductItem> orderProductItemMap = new HashMap<>(16);
        for (OrderStoreOrderProductItem item :
                orderProductItemList) {
            orderProductItemMap.put(item.getOrderItemCode(), item);
        }

        //校验参数订单商品行
        Map<String, OrderStoreOrderProductItem> paramProductItemMap = new HashMap<>(16);
        int lineIndex = 0;
        for (OrderStoreOrderProductItem item :
                paramProductItemList) {
            lineIndex++;
            if (item == null) {
                throw new BusinessException("第" + lineIndex + "行发货商品行数据为空");
            }
            if (StringUtils.isEmpty(item.getOrderItemCode())) {
                throw new BusinessException("第" + lineIndex + "行缺失订单商品行编码");
            }
            if (!orderProductItemMap.containsKey(item.getOrderItemCode())) {
                throw new BusinessException("第" + lineIndex + "行订单商品明细行编码无效");
            }
            if (paramProductItemMap.containsKey(item.getOrderItemCode())) {
                throw new BusinessException("第" + lineIndex + "行订单商品明细行重复");
            }
            if (item.getActualDeliverQuantity() == null) {
                throw new BusinessException("第" + lineIndex + "行缺失发货数量");
            } else {
                if (item.getActualDeliverQuantity() < 0) {
                    throw new BusinessException("第" + lineIndex + "行发货数量不能小于0");
                }
            }
            paramProductItemMap.put(item.getOrderItemCode(), item);
        }

        List<OrderStoreOrderProductItem> updateProductItemList = new ArrayList<>();
        List<OrderStoreOrderProductItem> reductionProductItemList = new ArrayList<>();
        //遍历修改订单商品行
        for (OrderStoreOrderProductItem item :
                orderProductItemList) {
            if (!paramProductItemMap.containsKey(item.getOrderItemCode())) {
                throw new BusinessException("缺失订单行号为" + item.getOrderItemCode() + "的订单商品行");
            }
            OrderStoreOrderProductItem paramProductItem = paramProductItemMap.get(item.getOrderItemCode());
            if (paramProductItem.getActualDeliverQuantity() > item.getQuantity()) {
                throw new BusinessException("订单明细行" + item.getOrderItemCode() + "发货数量大于下单数量");
            }

            OrderStoreOrderProductItem updateProductItem = new OrderStoreOrderProductItem();
            updateProductItem.setId(item.getId());
            updateProductItem.setActualDeliverQuantity(item.getActualDeliverQuantity());

            if (paramProductItem.getActualDeliverQuantity() < item.getQuantity()) {
                //发货数量小于下单数量，生成冲减单明细行
                OrderStoreOrderProductItem reductionProductItem = new OrderStoreOrderProductItem();
                try {
                    PropertyUtils.copyProperties(reductionProductItem, item);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                reductionProductItem.setQuantity(item.getQuantity() - paramProductItem.getActualDeliverQuantity());
                reductionProductItem.setMoney(item.getSharePrice().multiply(new BigDecimal(reductionProductItem.getQuantity())).setScale(4, RoundingMode.HALF_UP));
                reductionProductItemList.add(reductionProductItem);
            }
        }

        //保存物流信息
        OrderStoreOrderSending orderSending = new OrderStoreOrderSending();
        orderSending.setOrderId(order.getOrderId());
        orderSending.setSendingId(OrderPublic.getUUID());
        orderSending.setLogisticCode(paramOrderSending.getLogisticCode());
        orderSending.setLogisticCentreName(paramOrderSending.getLogisticCentreName());
        orderSending.setLogisticCentreCode(paramOrderSending.getLogisticCentreCode());
        orderSending.setLogisticFee(paramOrderSending.getLogisticFee());
        orderSending.setLogisticStatus(null);
        orderSending.setSendRepertoryCode(paramOrderSending.getSendRepertoryCode());
        orderSending.setSendRepertoryName(paramOrderSending.getSendRepertoryName());
        orderSending.setPayStatus(PayStatusEnum.UNPAID.getCode());
        erpOrderSendingService.saveOrderSending(orderSending, auth);

        //订单自动跳过正在拣货
        order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_7.getCode());
        erpOrderOperationService.updateOrderByPrimaryKeySelective(order, auth);

        //订单自动跳过扫描完成
        order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_8.getCode());
        erpOrderOperationService.updateOrderByPrimaryKeySelective(order, auth);

        //修改订单明细行
        erpOrderProductItemService.updateOrderProductItemList(updateProductItemList, auth);
        //订单修改为已发货
        order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_11.getCode());
        erpOrderOperationService.updateOrderByPrimaryKeySelective(order, auth);

        if (reductionProductItemList.size() > 0) {
            //TODO 生成发货冲减单
        }

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
     * 构建配送订单商品明细行数据
     *
     * @param cartProductList 购物车商品行
     * @param storeInfo       门店信息
     * @return java.util.List<com.aiqin.mgs.order.api.domain.OrderStoreOrderProductItem>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/27 19:02
     */
    private List<OrderStoreOrderProductItem> generateDistributionOrderProductList(List<CartOrderInfo> cartProductList, StoreInfo storeInfo) {

        //商品详情Map
        Map<String, ProductInfo> productMap = new HashMap<>(16);

        //遍历参数商品列表，获取商品详情，校验数据
        for (CartOrderInfo item :
                cartProductList) {
            //获取商品详情
            ProductInfo product = erpOrderRequestService.getProductDetail(storeInfo.getStoreId(), item.getProductId(), item.getSkuId());
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
            //活动校验
            boolean flag = erpOrderRequestService.activityCheck(storeInfo, orderProductItemList);
            if (!flag) {
                throw new BusinessException("活动校验失败");
            }
        }
        if (orderTypeEnum.isAreaCheck()) {
            //商品销售区域配置校验
            boolean flag = erpOrderRequestService.areaCheck(storeInfo, orderProductItemList);
            if (!flag) {
                throw new BusinessException("商品销售区域配置校验失败");
            }
        }
        if (orderTypeEnum.isPriceCheck()) {
            //商品销售价格校验
            boolean flag = erpOrderRequestService.priceCheck(storeInfo, orderProductItemList);
            if (!flag) {
                throw new BusinessException("商品销售价格校验失败");
            }
        }
        if (orderTypeEnum.isRepertoryCheck()) {
            //商品库存校验
            boolean flag = erpOrderRequestService.repertoryCheck(storeInfo, orderProductItemList);
            if (!flag) {
                throw new BusinessException("商品库存校验失败");
            }
        }

    }

    /**
     * 构建配送订单信息
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
    private String generateDistributionOrder(List<OrderStoreOrderProductItem> orderProductItemList, StoreInfo storeInfo, ErpOrderSaveRequest erpOrderSaveRequest, AuthToken auth) {

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
        orderInfo.setSplitStatus(YesOrNoEnum.NO.getCode());
        orderInfo.setOrderOriginType(erpOrderSaveRequest.getOrderOriginType());
        orderInfo.setOrderChannel(erpOrderSaveRequest.getOrderChannel());
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
            ProductInfo product = erpOrderRequestService.getProductDetail(storeInfo.getStoreId(), item.getProductId(), item.getSkuCode());
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
        orderInfo.setSplitStatus(YesOrNoEnum.NO.getCode());
        orderInfo.setOrderOriginType(erpOrderSaveRequest.getOrderOriginType());
        orderInfo.setOrderChannel(erpOrderSaveRequest.getOrderChannel());
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

}
