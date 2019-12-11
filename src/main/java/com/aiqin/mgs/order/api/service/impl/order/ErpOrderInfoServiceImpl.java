package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.dao.order.ErpOrderInfoDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.*;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderDeliverRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderEditRequest;
import com.aiqin.mgs.order.api.service.order.*;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.mgs.order.api.util.OrderPublic;
import org.apache.commons.beanutils.PropertyUtils;
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
    private ErpOrderOperationLogService erpOrderOperationLogService;
    @Resource
    private ErpOrderRequestService erpOrderRequestService;
    @Resource
    private ErpOrderConsigneeService erpOrderConsigneeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrder(ErpOrderInfo po, AuthToken auth) {
        po.setCreateById(auth.getPersonId());
        po.setCreateByName(auth.getPersonName());
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        po.setStatus(YesOrNoEnum.YES.getCode());
        Integer insert = erpOrderInfoDao.insert(po);

        //保存日志
        erpOrderOperationLogService.saveOrderOperationLog(po.getOrderId(), ErpOrderStatusEnum.ORDER_STATUS_1, auth);
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
        erpOrderOperationLogService.saveOrderOperationLog(po.getOrderId(), ErpOrderStatusEnum.getEnum(po.getOrderStatus()), auth);
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
        if (erpOrderEditRequest == null || StringUtils.isEmpty(erpOrderEditRequest.getOrderCode())) {
            throw new BusinessException("缺失订单编号");
        }
        if (erpOrderEditRequest.getProductGiftList() == null || erpOrderEditRequest.getProductGiftList().size() == 0) {
            throw new BusinessException("缺失赠品行");
        }
        ErpOrderInfo order = erpOrderQueryService.getOrderDetailByOrderCode(erpOrderEditRequest.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }
        if (!ErpOrderStatusEnum.ORDER_STATUS_1.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException("只有" + ErpOrderStatusEnum.ORDER_STATUS_1.getDesc() + "的订单才能增加赠品行");
        }
        if (!ErpPayStatusEnum.UNPAID.getCode().equals(order.getPayStatus())) {
            throw new BusinessException("订单已经发起支付，不能编辑");
        }

        int lineIndex = 0;
        for (ErpOrderItem item :
                erpOrderEditRequest.getProductGiftList()) {
            lineIndex++;
            if (StringUtils.isEmpty(item.getProductId())) {
                throw new BusinessException("赠品行第" + lineIndex + "行缺失商品id");
            }
            if (StringUtils.isEmpty(item.getSkuCode())) {
                throw new BusinessException("赠品行第" + lineIndex + "行缺失sku");
            }
            if (item.getQuantity() == null) {
                throw new BusinessException("赠品行第" + lineIndex + "行缺少数量");
            }
        }



    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderSplit(ErpOrderInfo orderInfo) {
        AuthToken auth = AuthUtil.getSystemAuth();
        String orderCode = orderInfo.getOrderCode();
        if (StringUtils.isEmpty(orderCode)) {
            throw new BusinessException("订单号为空");
        }

        //原订单
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
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
        ErpOrderTypeEnum orderTypeEnum = ErpOrderTypeEnum.getEnum(order.getOrderType());
        if (orderTypeEnum == null) {
            throw new BusinessException("订单类型异常");
        }

        if (ErpProductTypeEnum.STORAGE_RACK == orderTypeEnum.getErpProductTypeEnum() || ErpProductTypeEnum.DIRECT_SEND == orderTypeEnum.getErpProductTypeEnum()) {
            //按照供应商拆分订单

//            //sku - 商品详情 map
//            Map<String, ProductInfo> skuProductMap = new HashMap<>(16);
//            for (OrderStoreOrderProductItem item :
//                    orderItemList) {
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
                    newOrder.setOrderCode(newOrderCode);
                    newOrder.setOrderId(newOrderId);
                    newOrder.setOrderChannelType(order.getOrderChannelType());
                    newOrder.setOrderOriginType(order.getOrderOriginType());
                    newOrder.setPayStatus(order.getPayStatus());
                    newOrder.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_3.getCode());
                    //TODO CT 金额需要重新计算
                    newOrder.setPayMoney(realMoneyTotal);
                    newOrder.setTotalMoney(moneyTotal);
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
                    newOrder.setPrimaryCode(order.getOrderCode());
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

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderDeliver(ErpOrderDeliverRequest erpOrderDeliverRequest) {
        AuthToken auth = AuthUtil.getSystemAuth();

        if (erpOrderDeliverRequest == null) {
            throw new BusinessException("空参数");
        }
        List<ErpOrderInfo> paramOrderList = erpOrderDeliverRequest.getOrderList();
        if (paramOrderList == null || paramOrderList.size() <= 0) {
            throw new BusinessException("缺失发货订单为空");
        }
        ErpOrderLogistics paramOrderLogistics = erpOrderDeliverRequest.getOrderLogistics();
        if (paramOrderLogistics == null) {
            throw new BusinessException("缺失物流信息为空");
        } else {
            if (StringUtils.isEmpty(paramOrderLogistics.getLogisticsCode())) {
                throw new BusinessException("缺失物流单号");
            }
            if (StringUtils.isEmpty(paramOrderLogistics.getLogisticsCentreCode())) {
                throw new BusinessException("缺失物流公司编码");
            }
            if (StringUtils.isEmpty(paramOrderLogistics.getLogisticsCentreName())) {
                throw new BusinessException("缺失物流公司名称");
            }
            if (paramOrderLogistics.getLogisticsFee() == null) {
                throw new BusinessException("缺失物流费用");
            } else {
                if (paramOrderLogistics.getLogisticsFee().compareTo(BigDecimal.ZERO) < 0) {
                    throw new BusinessException("物流费用不能小于0");
                }
            }
            if (StringUtils.isEmpty(paramOrderLogistics.getSendRepertoryCode())) {
                throw new BusinessException("缺失发货仓库编码");
            }
            if (StringUtils.isEmpty(paramOrderLogistics.getSendRepertoryName())) {
                throw new BusinessException("缺失发货仓库名称");
            }
        }

        ErpOrderLogistics orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsCode(paramOrderLogistics.getLogisticsCode());
        if (orderLogistics == null) {
            paramOrderLogistics.setLogisticsId(OrderPublic.getUUID());
            paramOrderLogistics.setPayStatus(ErpPayStatusEnum.UNPAID.getCode());
            erpOrderLogisticsService.saveOrderLogistics(paramOrderLogistics, auth);
        } else {
            if (orderLogistics.getLogisticsFee().compareTo(paramOrderLogistics.getLogisticsFee()) != 0) {
                throw new BusinessException("物流单费用与已有物流单费用不相等");
            }
            if (!orderLogistics.getLogisticsCentreCode().equals(paramOrderLogistics.getLogisticsCentreCode())) {
                throw new BusinessException("物流公司编码与已有物流单物流公司编码不相同");
            }
            if (!orderLogistics.getLogisticsCentreName().equals(paramOrderLogistics.getLogisticsCentreName())) {
                throw new BusinessException("物流公司名称与已有物流单物流公司名称不相同");
            }
            if (!orderLogistics.getSendRepertoryCode().equals(paramOrderLogistics.getSendRepertoryCode())) {
                throw new BusinessException("发货仓库编码与已有物流单发货仓库编码不相同");
            }
            if (!orderLogistics.getSendRepertoryName().equals(paramOrderLogistics.getSendRepertoryName())) {
                throw new BusinessException("发货仓库名称与已有物流单发货仓库名称不相同");
            }
            paramOrderLogistics.setLogisticsId(orderLogistics.getLogisticsId());
            paramOrderLogistics.setPayStatus(orderLogistics.getPayStatus());
        }

        int paramOrderIndex = 0;
        for (ErpOrderInfo paramOrder :
                paramOrderList) {
            paramOrderIndex++;
            if (paramOrder == null) {
                throw new BusinessException("发货订单参数第" + paramOrderIndex + "个订单为空");
            }
            if (StringUtils.isEmpty(paramOrder.getOrderCode())) {
                throw new BusinessException("发货订单参数第" + paramOrderIndex + "个订单编号缺失");
            }
            List<ErpOrderItem> paramItemList = paramOrder.getOrderItemList();
            if (paramItemList == null || paramItemList.size() <= 0) {
                throw new BusinessException("订单" + paramOrder.getOrderCode() + "缺少发货商品明细行");
            }
            ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(paramOrder.getOrderCode());
            if (order == null) {
                throw new BusinessException("订单号" + paramOrder.getOrderCode() + "无效");
            }
            if (!ErpOrderStatusEnum.ORDER_STATUS_6.getCode().equals(order.getOrderStatus())) {
                throw new BusinessException("订单" + order.getOrderCode() + "不是" + ErpOrderStatusEnum.ORDER_STATUS_6.getDesc() + "的订单不能发货");
            }

            //待发货订单商品明细行
            List<ErpOrderItem> orderItemList = erpOrderItemService.selectOrderItemListByOrderId(order.getOrderId());
            //待发货订单商品明细行 key:orderItemCode
            Map<String, ErpOrderItem> orderItemMap = new HashMap<>(16);
            for (ErpOrderItem item :
                    orderItemList) {
                orderItemMap.put(item.getOrderItemCode(), item);
            }

            //校验参数订单商品行
            Map<String, ErpOrderItem> paramItemMap = new HashMap<>(16);
            int lineIndex = 0;
            for (ErpOrderItem item :
                    paramItemList) {
                lineIndex++;
                if (item == null) {
                    throw new BusinessException("订单" + order.getOrderCode() + "第" + lineIndex + "行发货商品行数据为空");
                }
                if (StringUtils.isEmpty(item.getOrderItemCode())) {
                    throw new BusinessException("订单" + order.getOrderCode() + "第" + lineIndex + "行缺失订单商品行编码");
                }
                if (!orderItemMap.containsKey(item.getOrderItemCode())) {
                    throw new BusinessException("订单" + order.getOrderCode() + "第" + lineIndex + "行订单商品明细行编码无效");
                }
                if (paramItemMap.containsKey(item.getOrderItemCode())) {
                    throw new BusinessException("订单" + order.getOrderCode() + "第" + lineIndex + "行订单商品明细行重复");
                }
                if (item.getDeliverQuantity() == null) {
                    throw new BusinessException("订单" + order.getOrderCode() + "第" + lineIndex + "行缺失发货数量");
                } else {
                    if (item.getDeliverQuantity() < 0) {
                        throw new BusinessException("订单" + order.getOrderCode() + "第" + lineIndex + "行发货数量不能小于0");
                    }
                }
                paramItemMap.put(item.getOrderItemCode(), item);
            }

            //需要更新的明细行
            List<ErpOrderItem> updateProductItemList = new ArrayList<>();

            boolean reductionFlag = false;
            //遍历修改订单商品行
            for (ErpOrderItem item :
                    orderItemList) {
                if (!paramItemMap.containsKey(item.getOrderItemCode())) {
                    throw new BusinessException("订单" + order.getOrderCode() + "缺失订单行号为" + item.getOrderItemCode() + "的订单商品行");
                }
                ErpOrderItem paramItem = paramItemMap.get(item.getOrderItemCode());
                if (paramItem.getDeliverQuantity() > item.getQuantity()) {
                    throw new BusinessException("订单" + order.getOrderCode() + "订单明细行" + item.getOrderItemCode() + "发货数量大于下单数量");
                }

                ErpOrderItem updateItem = new ErpOrderItem();
                updateItem.setId(item.getId());
                updateItem.setDeliverQuantity(item.getDeliverQuantity());
                updateProductItemList.add(updateItem);

                if (paramItem.getDeliverQuantity() < item.getQuantity()) {
                    //发货数量小于下单数量，该订单需要生成冲减单
                    reductionFlag = true;
                }
            }

            //订单自动跳过正在拣货
            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_7.getCode());
            this.updateOrderByPrimaryKeySelective(order, auth);

            //订单自动跳过扫描完成
            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_8.getCode());
            this.updateOrderByPrimaryKeySelective(order, auth);

            //修改订单明细行
            erpOrderItemService.updateOrderItemList(updateProductItemList, auth);
            //订单修改为已发货
            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_11.getCode());
            if (reductionFlag) {
                //TODO 标记订单需要生成冲减单
            }
            this.updateOrderByPrimaryKeySelective(order, auth);

            if (ErpPayStatusEnum.SUCCESS.getCode().equals(paramOrderLogistics.getPayStatus())) {
                //如果物流单已经支付成功，订单修改为已支付物流费用
                order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_12.getCode());
                this.updateOrderByPrimaryKeySelective(order, auth);
            }

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderSign(ErpOrderInfo erpOrderInfo) {
        AuthToken auth = AuthUtil.getCurrentAuth();
        if (erpOrderInfo == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(erpOrderInfo.getOrderCode())) {
            throw new BusinessException("订单编号为空");
        }

        List<ErpOrderItem> paramItemList = erpOrderInfo.getOrderItemList();
        if (paramItemList == null || paramItemList.size() <= 0) {
            throw new BusinessException("商品明细为空");
        }
        Map<String, ErpOrderItem> orderItemSignMap = new HashMap<>(16);
        int lineIndex = 0;
        for (ErpOrderItem item :
                paramItemList) {
            lineIndex++;
            if (item == null) {
                throw new BusinessException("第" + lineIndex + "行数据为空");
            }
            if (StringUtils.isEmpty(item.getOrderItemCode())) {
                throw new BusinessException("第" + lineIndex + "行订单行号为空");
            }
            if (item.getSignQuantity() == null || item.getSignQuantity() <= 0) {
                throw new BusinessException("第" + lineIndex + "行签收数量有误");
            }
            orderItemSignMap.put(item.getOrderItemCode(), item);
        }

        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(erpOrderInfo.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }
        ErpOrderTypeEnum orderTypeEnum = ErpOrderTypeEnum.getEnum(order.getOrderType());
        if (orderTypeEnum == null) {
            throw new BusinessException("订单类型有误");
        }
        ErpProductTypeEnum productTypeEnum = orderTypeEnum.getErpProductTypeEnum();
        if (productTypeEnum == ErpProductTypeEnum.DISTRIBUTION) {
            //配送订单
            if (!ErpOrderStatusEnum.ORDER_STATUS_12.getCode().equals(order.getOrderStatus())) {
                throw new BusinessException("该" + productTypeEnum.getDesc() + "订单不是" + ErpOrderStatusEnum.ORDER_STATUS_12.getDesc() + "的订单，不能签收");
            }
        } else {
            if (!ErpOrderStatusEnum.ORDER_STATUS_11.getCode().equals(order.getOrderStatus())) {
                throw new BusinessException("该" + productTypeEnum.getDesc() + "订单不是" + ErpOrderStatusEnum.ORDER_STATUS_12.getDesc() + "的订单，不能签收");
            }
        }
        List<ErpOrderItem> itemList = erpOrderItemService.selectOrderItemListByOrderId(order.getOrderId());
        List<ErpOrderItem> updateItemList = new ArrayList<>();
        for (ErpOrderItem item :
                itemList) {
            if (!orderItemSignMap.containsKey(item.getOrderItemCode())) {
                throw new BusinessException("商品" + item.getProductName() + item.getSkuName() + "签收信息为空");
            }
            ErpOrderItem signItem = orderItemSignMap.get(item.getOrderItemCode());
            ErpOrderItem updateItem = new ErpOrderItem();
            updateItem.setId(item.getId());
            updateItem.setSignQuantity(signItem.getSignQuantity());
            updateItem.setSignDifferenceReason(signItem.getSignDifferenceReason());
            updateItemList.add(updateItem);
        }
        erpOrderItemService.updateOrderItemList(updateItemList, auth);

        ErpOrderInfo updateOrder = new ErpOrderInfo();
        updateOrder.setId(order.getId());
        updateOrder.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_13.getCode());
        updateOrderByPrimaryKeySelective(updateOrder, auth);
    }

}
