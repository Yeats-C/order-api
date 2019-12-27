package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.dao.order.ErpOrderInfoDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.ProductInfo;
import com.aiqin.mgs.order.api.domain.po.order.*;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderEditRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderProductItemRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderSignRequest;
import com.aiqin.mgs.order.api.service.order.*;
import com.aiqin.mgs.order.api.util.AuthUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ErpOrderInfoServiceImpl implements ErpOrderInfoService {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderInfoServiceImpl.class);

    @Resource
    private ErpOrderInfoDao erpOrderInfoDao;
    @Resource
    private ErpOrderQueryService erpOrderQueryService;
    @Resource
    private ErpOrderItemService erpOrderItemService;
    @Resource
    private ErpOrderLogisticsService erpOrderLogisticsService;
    @Resource
    private ErpOrderFeeService erpOrderFeeService;
    @Resource
    private ErpOrderOperationLogService erpOrderOperationLogService;
    @Resource
    private ErpOrderRequestService erpOrderRequestService;
    @Resource
    private ErpOrderPayService erpOrderPayService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrder(ErpOrderInfo po, AuthToken auth) {
        po.setCreateById(auth.getPersonId());
        po.setCreateByName(auth.getPersonName());
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        po.setUseStatus(StatusEnum.YES.getCode());
        Integer insert = erpOrderInfoDao.insert(po);

        //保存日志
        erpOrderOperationLogService.saveOrderOperationLog(po.getOrderStoreCode(),  ErpLogOperationTypeEnum.ADD, ErpOrderStatusEnum.ORDER_STATUS_1.getCode(), null, auth);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderNoLog(ErpOrderInfo po, AuthToken auth) {
        po.setCreateById(auth.getPersonId());
        po.setCreateByName(auth.getPersonName());
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer insert = erpOrderInfoDao.insert(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderByPrimaryKeySelective(ErpOrderInfo po, AuthToken auth) {

        //更新订单数据
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer integer = erpOrderInfoDao.updateByPrimaryKeySelective(po);

        //保存订单操作日志
        erpOrderOperationLogService.saveOrderOperationLog(po.getOrderStoreCode(), ErpLogOperationTypeEnum.ADD, po.getOrderStatus(), null, auth);

    }

    @Override
    public void updateOrderByPrimaryKeySelectiveNoLog(ErpOrderInfo po, AuthToken auth) {
        //更新订单数据
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer integer = erpOrderInfoDao.updateByPrimaryKeySelective(po);
    }

    @Override
    public void addProductGift(ErpOrderEditRequest erpOrderEditRequest) {

        AuthToken auth = AuthUtil.getCurrentAuth();

        if (erpOrderEditRequest == null || StringUtils.isEmpty(erpOrderEditRequest.getOrderCode())) {
            throw new BusinessException("缺失订单编号");
        }
        if (erpOrderEditRequest.getProductGiftList() == null || erpOrderEditRequest.getProductGiftList().size() == 0) {
            throw new BusinessException("缺失赠品行");
        }
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(erpOrderEditRequest.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }

        ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(order.getOrderTypeCode(), order.getOrderCategoryCode());
        if (processTypeEnum == null) {
            throw new BusinessException("异常的订单类型");
        }

        if (!processTypeEnum.isAddProductGift()) {
            throw new BusinessException("该类型的订单不能增加赠品行");
        }
        if (!ErpOrderStatusEnum.ORDER_STATUS_1.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException("只有" + ErpOrderStatusEnum.ORDER_STATUS_1.getDesc() + "的订单才能增加赠品行");
        }
        ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByFeeId(order.getFeeId());
        if (orderFee == null) {
            throw new BusinessException("订单费用信息异常");
        }
        if (StringUtils.isNotEmpty(orderFee.getPayId())) {
            ErpOrderPay orderPay = erpOrderPayService.getOrderPayByPayId(orderFee.getPayId());
            if (orderPay != null) {
//                if (ErpPayPollingBackStatusEnum.) {
                //TODO 判断是否已经发起支付，抛异常
//                }
            }

        }

        if (1 == 1) {
            //TODO CT 测试中断操作
            throw new BusinessException("test");
        }

        //订单原商品明细行
        List<ErpOrderItem> orderItemList = erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId());
        //记录原订单明细行最大行号最后三位数的值
        long maxLineIndex = 0;
        for (ErpOrderItem item :
                orderItemList) {
            maxLineIndex = maxLineIndex > item.getLineCode() ? maxLineIndex : item.getLineCode();
        }

        List<ErpOrderItem> addGiftList = new ArrayList<>();
        int lineIndex = 0;
        for (ErpOrderProductItemRequest item :
                erpOrderEditRequest.getProductGiftList()) {
            lineIndex++;
            maxLineIndex++;
            if (StringUtils.isEmpty(item.getSpuCode())) {
                throw new BusinessException("赠品行第" + lineIndex + "行缺失spu编码");
            }
            if (StringUtils.isEmpty(item.getSkuCode())) {
                throw new BusinessException("赠品行第" + lineIndex + "行缺失sku");
            }
            if (item.getQuantity() == null) {
                throw new BusinessException("赠品行第" + lineIndex + "行缺少数量");
            }
            ProductInfo product = erpOrderRequestService.getSkuDetail(order.getCompanyCode(), item.getSkuCode());
            if (product == null) {
                throw new BusinessException("赠品行第" + lineIndex + "行商品未找到");
            }

            ErpOrderItem orderItem = new ErpOrderItem();
            //spu编码
            orderItem.setSpuCode(product.getSpuCode());
            //spu名称
            orderItem.setSpuName(product.getSpuName());
            //sku编码
            orderItem.setSkuCode(product.getSkuCode());
            //sku名称
            orderItem.setSkuName(product.getSkuName());
            //条形码
            orderItem.setBarCode(product.getBarCode());
            //图片地址
            orderItem.setPictureUrl(product.getPictureUrl());
            //规格
            orderItem.setProductSpec(product.getProductSpec());
            //颜色编码
            orderItem.setColorCode(product.getColorCode());
            //颜色名称
            orderItem.setColorName(product.getColorName());
            //型号
            orderItem.setModelCode(product.getModelCode());
            //单位编码
            orderItem.setUnitCode(product.getUnitCode());
            //单位名称
            orderItem.setUnitName(product.getUnitName());
            //折零系数 不填
            orderItem.setZeroDisassemblyCoefficient(null);
            //商品类型  0商品 1赠品
            orderItem.setProductType(ErpProductGiftEnum.PRODUCT.getCode());
            //商品属性编码
            orderItem.setProductPropertyCode(product.getProductPropertyCode());
            //商品属性名称
            orderItem.setProductPropertyName(product.getProductPropertyName());
            //供应商编码
            orderItem.setSupplierCode(product.getSupplierCode());
            //供应商名称
            orderItem.setSupplierName(product.getSupplierName());
            //商品数量
            orderItem.setProductCount((long) item.getQuantity());
            //商品单价
            orderItem.setProductAmount(BigDecimal.ZERO);
            //商品总价
            orderItem.setTotalProductAmount(BigDecimal.ZERO);
            //实际商品总价
            orderItem.setActualTotalProductAmount(BigDecimal.ZERO);
            //优惠分摊总金额
            orderItem.setTotalPreferentialAmount(BigDecimal.ZERO);
            //分摊后单价
            orderItem.setTotalPreferentialAmount(BigDecimal.ZERO);
            //活动优惠总金额
            orderItem.setTotalAcivityAmount(BigDecimal.ZERO);
            //实际商品数量
            orderItem.setActualProductCount(null);
            //税率
            orderItem.setTaxRate(product.getTaxRate());
            //公司编码
            orderItem.setCompanyCode(order.getCompanyCode());
            //公司名称
            orderItem.setCompanyName(order.getCompanyName());
            addGiftList.add(orderItem);
        }

        if (processTypeEnum.isLockStock()) {
            //TODO 锁库存
//            erpOrderRequestService.lockStockInSupplyChain();
        }

        erpOrderItemService.saveOrderItemList(addGiftList, auth);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderSplit(ErpOrderInfo orderInfo) {
        /*AuthToken auth = AuthUtil.getSystemAuth();
        String orderStoreCode = orderInfo.getOrderStoreCode();
        if (StringUtils.isEmpty(orderStoreCode)) {
            throw new BusinessException("订单号为空");
        }

        //原订单
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderStoreCode);
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }
        //原订单商品明细
        List<ErpOrderItem> orderItemList = erpOrderItemService.selectOrderItemListByOrderId(order.getOrderId());
        order.setOrderItemList(orderItemList);
        //原订单收货人信息
        ErpOrderConsignee orderConsignee = erpOrderConsigneeService.getOrderConsigneeByOrderId(order.getOrderId());
        //原订单日志
        List<ErpOrderOperationLog> orderOperationLogList = erpOrderOperationLogService.selectOrderOperationLogListByOrderId(order.getOrderId());

        if (!ErpOrderStatusEnum.ORDER_STATUS_2.getCode().equals(order.getOrderStatus()) && !ErpOrderStatusEnum.ORDER_STATUS_92.getCode().equals(order.getOrderStatus()) && !ErpOrderStatusEnum.ORDER_STATUS_93.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException("只有支付成功且未拆分的的订单才能拆分");
        }
        ErpOrderCategoryEnum orderTypeEnum = ErpOrderCategoryEnum.getEnum(order.getOrderType());
        if (orderTypeEnum == null) {
            throw new BusinessException("订单类型异常");
        }

        if (ErpOrderTypeEnum.STORAGE_RACK == orderTypeEnum.getErpOrderTypeEnum() || ErpOrderTypeEnum.DIRECT_SEND == orderTypeEnum.getErpOrderTypeEnum()) {
            //按照供应商拆分订单

//            //sku - 商品详情 map
//            Map<String, ProductInfo> skuProductMap = new HashMap<>(16);
//            for (OrderStoreOrderProductItem item :
//                    orderItemList) {
//                if (!skuProductMap.containsKey(item.getSkuCode())) {
//                    ProductInfo productInfo = erpOrderRequestService.getSkuDetail(order.getStoreId(), item.getProductId(), item.getSkuCode());
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
//                    orderItemList) {
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
//                    String newOrderCode = OrderPublic.generateOrderCode(order.getOrderOriginType(), order.getOrderChannelType());
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
//                        item.setOrderStoreCode(newOrderCode);
//                        //订单明细行编号
//                        item.setOrderItemCode(newOrderCode + String.format("%03d", orderItemNum++));
//
//                        //订货金额汇总
//                        moneyTotal = moneyTotal.add(item.getMoney() == null ? BigDecimal.ZERO : item.getMoney());
//                        //实际支付金额汇总
//                        realMoneyTotal = realMoneyTotal.add(item.getPayMoney() == null ? BigDecimal.ZERO : item.getPayMoney());
////                        //活动优惠金额汇总
////                        activityMoneyTotal = activityMoneyTotal.add(item.getActivityMoney() == null ? BigDecimal.ZERO : item.getActivityMoney());
////                        //服纺券优惠金额汇总
////                        spinCouponMoneyTotal = spinCouponMoneyTotal.add(item.getSpinCouponMoney() == null ? BigDecimal.ZERO : item.getSpinCouponMoney());
////                        //A品券优惠金额汇总
////                        topCouponMoneyTotal = topCouponMoneyTotal.add(item.getTopCouponMoney() == null ? BigDecimal.ZERO : item.getTopCouponMoney())
//                    }
//                    erpOrderProductItemService.saveOrderProductItemList(orderItemList, auth);
//
//                    //保存订单信息
//                    OrderStoreOrderInfo orderInfo = new OrderStoreOrderInfo();
//                    orderInfo.setOrderStoreCode(newOrderCode);
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
//                    orderInfo.setPrimaryCode(order.getOrderStoreCode());
//                    orderInfo.setSupplierCode(entry.getKey());
//                    orderInfo.setSupplierCode(supplierCodeNameMap.get(entry.getKey()));
//                    erpOrderOperationService.saveOrder(orderInfo, auth);
//                }
//            }
//            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_4.getCode());
//            erpOrderOperationService.updateOrderByPrimaryKeySelective(order, auth);
        } else {
            //按照库存拆分

            //请求供应链获取订单商品库存分组
            ErpOrderInfo paramOrder = erpOrderRequestService.sendOrderToSupplyChainAndGetSplitGroup(order);

            List<ErpOrderItem> paramItemList = paramOrder.getOrderItemList();

            if (paramItemList == null || paramItemList.size() <= 0) {
                throw new BusinessException("订单商品明细行为空");
            }

            //订单行号-(仓库编码-数量)
            Map<String, Map<String, Integer>> itemCodeRepertoryQuantityMap = new HashMap<>(16);
            //仓库编码-仓库名称
            Map<String, String> repertoryCodeNameMap = new HashMap<>(16);
            //分组Map
            Map<String, List<ErpOrderItem>> splitMap = new HashMap<>(16);

            int lineIndex = 0;
            //遍历参数商品行
            for (ErpOrderItem item :
                    paramItemList) {
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
            for (ErpOrderItem item :
                    orderItemList) {
                if (!itemCodeRepertoryQuantityMap.containsKey(item.getOrderItemCode())) {
                    throw new BusinessException("缺少商品行" + item.getOrderItemCode() + "的库存信息");
                }

                Map<String, Integer> repertoryQuantityMap = itemCodeRepertoryQuantityMap.get(item.getOrderItemCode());
                Integer quantityTotal = 0;
                for (Map.Entry<String, Integer> entry :
                        repertoryQuantityMap.entrySet()) {
                    quantityTotal += entry.getValue();

                    List<ErpOrderItem> splitItemList = new ArrayList<>();
                    if (splitMap.containsKey(entry.getKey())) {
                        splitItemList.addAll(splitMap.get(entry.getKey()));
                    }
                    ErpOrderItem splitItem = new ErpOrderItem();
                    try {
                        PropertyUtils.copyProperties(splitItem, item);
                    } catch (Exception e) {
                        logger.info("操作异常：{}", e);
                    }
                    splitItem.setId(null);
                    splitItem.setQuantity(entry.getValue());
                    splitItem.setMoney(splitItem.getPrice().multiply(new BigDecimal(splitItem.getQuantity())));

                    //TODO 尾差处理 如果有活动或者优惠券的情况下可能会除不尽
                    splitItem.setActualMoney(item.getActualMoney().multiply(new BigDecimal(entry.getValue())).divide(BigDecimal.TEN, 4, RoundingMode.HALF_UP));
                    splitItem.setActivityMoney(item.getActivityMoney().multiply(new BigDecimal(entry.getValue())).divide(BigDecimal.TEN, 4, RoundingMode.HALF_UP));

                    splitItemList.add(splitItem);
                    splitMap.put(entry.getKey(), splitItemList);
                }
                if (!quantityTotal.equals(item.getQuantity())) {
                    throw new BusinessException("商品行" + item.getOrderItemCode() + "数量汇总与原数量不相等");
                }

            }

            //更新订单状态为拆分中
            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_3.getCode());
            updateOrderByPrimaryKeySelective(order, auth);


            List<ErpOrderInfo> newSplitOrderList = new ArrayList<>();
            //是否拆分
            boolean splitFlag = false;
            if (splitMap.size() == 1) {
                //不拆分

            } else {
                //拆分订单

                splitFlag = true;
                for (Map.Entry<String, List<ErpOrderItem>> entry :
                        splitMap.entrySet()) {

                    List<ErpOrderItem> splitOrderItemList = entry.getValue();

                    //生成订单id
                    String newOrderId = OrderPublic.getUUID();
                    //生成订单code
                    String newOrderCode = OrderPublic.generateOrderCode(order.getOrderOriginType(), order.getOrderChannelType());

                    //订货金额汇总
                    BigDecimal moneyTotal = BigDecimal.ZERO;
                    //实际支付金额汇总
                    BigDecimal realMoneyTotal = BigDecimal.ZERO;
//                    //活动优惠金额汇总
                    BigDecimal activityMoneyTotal = BigDecimal.ZERO;
//                    //服纺券优惠金额汇总
//                    BigDecimal spinCouponMoneyTotal = BigDecimal.ZERO;
//                    //A品券优惠金额汇总
//                    BigDecimal topCouponMoneyTotal = BigDecimal.ZERO;
                    int orderItemNum = 1;
                    for (ErpOrderItem item :
                            splitOrderItemList) {

                        //订单id
                        item.setOrderId(newOrderId);
                        //订单明细行编号
                        item.setOrderItemCode(newOrderCode + String.format("%03d", orderItemNum++));

                        //订货金额汇总
                        moneyTotal = moneyTotal.add(item.getMoney() == null ? BigDecimal.ZERO : item.getMoney());
                        //实际支付金额汇总
                        realMoneyTotal = realMoneyTotal.add(item.getActualMoney() == null ? BigDecimal.ZERO : item.getActualMoney());
                        //活动优惠金额汇总
                        activityMoneyTotal = activityMoneyTotal.add(item.getActivityMoney() == null ? BigDecimal.ZERO : item.getActivityMoney());
//                    //服纺券优惠金额汇总
//                    spinCouponMoneyTotal = spinCouponMoneyTotal.add(item.getSpinCouponMoney() == null ? BigDecimal.ZERO : item.getSpinCouponMoney());
//                    //A品券优惠金额汇总
//                    topCouponMoneyTotal = topCouponMoneyTotal.add(item.getTopCouponMoney() == null ? BigDecimal.ZERO : item.getTopCouponMoney())
                    }
                    erpOrderItemService.saveOrderItemList(splitOrderItemList, auth);

                    //保存新订单信息
                    ErpOrderInfo newOrder = new ErpOrderInfo();
                    newOrder.setOrderStoreCode(newOrderCode);
                    newOrder.setOrderId(newOrderId);
                    newOrder.setOrderChannelType(order.getOrderChannelType());
                    newOrder.setOrderOriginType(order.getOrderOriginType());
                    newOrder.setPayStatus(order.getPayStatus());
                    newOrder.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_3.getCode());
                    //TODO CT 金额需要重新计算
//                    newOrder.setPayMoney(realMoneyTotal);
//                    newOrder.setTotalMoney(moneyTotal);
                    newOrder.setOrderType(order.getOrderType());
                    newOrder.setFranchiseeId(order.getFranchiseeId());
                    newOrder.setFranchiseeCode(order.getFranchiseeCode());
                    newOrder.setFranchiseeName(order.getFranchiseeName());
                    newOrder.setStoreId(order.getStoreId());
                    newOrder.setStoreCode(order.getStoreCode());
                    newOrder.setStoreName(order.getStoreName());
                    newOrder.setOrderLevel(ErpOrderLevelEnum.SECONDARY.getCode());
                    newOrder.setReturnStatus(YesOrNoEnum.NO.getCode());
                    newOrder.setSplitStatus(YesOrNoEnum.NO.getCode());
                    newOrder.setPrimaryCode(order.getOrderStoreCode());
                    newOrder.setRepertoryCode(entry.getKey());
                    newOrder.setRepertoryName(repertoryCodeNameMap.get(entry.getKey()));
                    this.saveOrderNoLog(newOrder, auth);

                    //保存收货人信息
                    ErpOrderConsignee newOrderConsignee = new ErpOrderConsignee();
                    try {
                        PropertyUtils.copyProperties(newOrderConsignee, orderConsignee);
                        newOrderConsignee.setOrderId(newOrderId);
                    } catch (Exception e) {
                        throw new BusinessException("操作异常");
                    }
                    erpOrderConsigneeService.saveOrderConsignee(newOrderConsignee, auth);

                    //复制日志
                    erpOrderOperationLogService.copySplitOrderLog(newOrderId, orderOperationLogList);

                    ErpOrderInfo orderByOrderId = erpOrderQueryService.getOrderByOrderId(newOrderId);
                    orderByOrderId.setOrderConsignee(newOrderConsignee);
                    orderByOrderId.setOrderItemList(splitOrderItemList);
                    newSplitOrderList.add(orderByOrderId);
                }

            }

            //订单同步
            if (splitFlag) {
                for (ErpOrderInfo item :
                        newSplitOrderList) {
                    item.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_4.getCode());
                    this.updateOrderByPrimaryKeySelective(item, auth);
                }
                order.setSplitStatus(YesOrNoEnum.YES.getCode());
            }
            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_4.getCode());
            this.updateOrderByPrimaryKeySelective(order, auth);

            //调用同步到供应链接口
            erpOrderRequestService.sendSplitOrderToSupplyChain(order, newSplitOrderList);

            if (splitFlag) {
                for (ErpOrderInfo item :
                        newSplitOrderList) {
                    item.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_6.getCode());
                    this.updateOrderByPrimaryKeySelective(item, auth);
                }
            } else {
                order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_6.getCode());
                this.updateOrderByPrimaryKeySelective(order, auth);
            }
        }
*/
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderSign(ErpOrderSignRequest erpOrderSignRequest) {
        if (erpOrderSignRequest == null || StringUtils.isEmpty(erpOrderSignRequest.getPersonId())) {
            throw new BusinessException("缺失用户id");
        }
        if (StringUtils.isEmpty(erpOrderSignRequest.getPersonName())) {
            throw new BusinessException("缺失用户名称");
        }
        AuthToken auth = new AuthToken();
        auth.setPersonId(erpOrderSignRequest.getPersonId());
        auth.setPersonName(erpOrderSignRequest.getPersonName());
        if (StringUtils.isEmpty(erpOrderSignRequest.getOrderCode())) {
            throw new BusinessException("订单编号为空");
        }

        List<ErpOrderProductItemRequest> paramItemList = erpOrderSignRequest.getItemList();
        if (paramItemList == null || paramItemList.size() <= 0) {
            throw new BusinessException("商品明细为空");
        }
        Map<Long, ErpOrderProductItemRequest> orderItemSignMap = new HashMap<>(16);
        for (ErpOrderProductItemRequest item :
                paramItemList) {
            if (item.getLineCode() == null) {
                throw new BusinessException("缺失行号");
            }
            if (item.getActualInboundCount() == null || item.getActualInboundCount() <= 0) {
                throw new BusinessException("第" + item.getLineCode() + "行签收数量有误");
            }
            orderItemSignMap.put(item.getLineCode(), item);
        }

        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(erpOrderSignRequest.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }

        //节点控制类型
        ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(order.getOrderTypeCode(), order.getOrderCategoryCode());
        if (processTypeEnum == null) {
            throw new BusinessException("订单类型有误");
        }
        if (processTypeEnum.isHasLogisticsFee()) {
            //需要先支付物流费用
            if (!ErpOrderStatusEnum.ORDER_STATUS_12.getCode().equals(order.getOrderStatus())) {
                throw new BusinessException("该订单不是" + ErpOrderStatusEnum.ORDER_STATUS_12.getDesc() + "的订单，不能签收");
            }
        } else {
            if (!ErpOrderStatusEnum.ORDER_STATUS_11.getCode().equals(order.getOrderStatus())) {
                throw new BusinessException("该订单不是" + ErpOrderStatusEnum.ORDER_STATUS_11.getDesc() + "的订单，不能签收");
            }
        }
        List<ErpOrderItem> itemList = erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId());
        List<ErpOrderItem> updateItemList = new ArrayList<>();
        for (ErpOrderItem item :
                itemList) {
            if (!orderItemSignMap.containsKey(item.getLineCode())) {
                throw new BusinessException("第" + item.getLineCode() + "行签收信息为空");
            }
            ErpOrderProductItemRequest signItem = orderItemSignMap.get(item.getLineCode());
            ErpOrderItem updateItem = new ErpOrderItem();
            updateItem.setId(item.getId());
            updateItem.setActualInboundCount(signItem.getActualInboundCount());
            updateItem.setSignDifferenceReason(signItem.getSignDifferenceReason());
            updateItemList.add(updateItem);
        }
        erpOrderItemService.updateOrderItemList(updateItemList, auth);

        ErpOrderInfo updateOrder = new ErpOrderInfo();
        updateOrder.setId(order.getId());
        updateOrder.setOrderStoreCode(order.getOrderStoreCode());
        updateOrder.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_13.getCode());
        this.updateOrderByPrimaryKeySelective(updateOrder, auth);
    }

}
