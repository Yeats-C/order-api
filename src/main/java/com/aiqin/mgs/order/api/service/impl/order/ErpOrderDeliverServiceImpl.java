package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics;
import com.aiqin.mgs.order.api.domain.request.order.*;
import com.aiqin.mgs.order.api.service.order.*;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.mgs.order.api.util.OrderPublic;
import org.apache.commons.lang.StringUtils;
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
public class ErpOrderDeliverServiceImpl implements ErpOrderDeliverService {

    @Resource
    private ErpOrderInfoService erpOrderInfoService;
    @Resource
    private ErpOrderQueryService erpOrderQueryService;
    @Resource
    private ErpOrderItemService erpOrderItemService;
    @Resource
    private ErpOrderLogisticsService erpOrderLogisticsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderDeliver(ErpOrderDeliverRequest erpOrderDeliverRequest) {

        if (erpOrderDeliverRequest == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(erpOrderDeliverRequest.getOrderCode())) {
            throw new BusinessException("缺少订单号");
        }
        AuthToken auth = new AuthToken();
        auth.setPersonId(erpOrderDeliverRequest.getPersonId());
        auth.setPersonName(erpOrderDeliverRequest.getPersonName());

        Map<Long, Long> paramLineQuantityMap = new HashMap<>(16);
        List<ErpOrderDeliverItemRequest> paramItemList = erpOrderDeliverRequest.getItemList();
        if (paramItemList == null || paramItemList.size() == 0) {
            throw new BusinessException("缺少发货数量明细");
        }
        for (ErpOrderDeliverItemRequest item :
                paramItemList) {
            paramLineQuantityMap.put(item.getLineCode(), item.getActualProductCount());
        }


        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(erpOrderDeliverRequest.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单号");
        }

        boolean flag = false;
        if (ErpOrderStatusEnum.ORDER_STATUS_6.getCode().equals(order.getOrderStatus())) {
            if (ErpOrderNodeStatusEnum.STATUS_8.getCode().equals(order.getOrderNodeStatus())) {
                flag = true;
            }
        }

        if (flag) {
            List<ErpOrderItem> itemList = erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId());
            //实发数量
            Long totalActualProductCount = 0L;
            //实发均摊金额
            BigDecimal actualTotalProductAmount = BigDecimal.ZERO;
            for (ErpOrderItem item :
                    itemList) {
                //行订货数量
                Long productCount = item.getProductCount();
                //行签收数量
                Long actualProductCount = paramLineQuantityMap.get(item.getLineCode());
                if (actualProductCount == null) {
                    throw new BusinessException("缺少行号为" + item.getLineCode() + "的发货数量");
                }
                if (actualProductCount < 0) {
                    throw new BusinessException("发货数量不能小于0");
                }
                if (actualProductCount > productCount) {
                    throw new BusinessException("发货数量不能超过订货数量");
                }
                item.setActualProductCount(actualProductCount);
                totalActualProductCount += actualProductCount;

                //行实际发货商品均摊金额
                BigDecimal actualProductAmount = BigDecimal.ZERO;
                if (productCount.equals(actualProductCount)) {
                    actualProductAmount = item.getTotalPreferentialAmount();
                } else {
                    actualProductAmount = new BigDecimal(actualProductCount).multiply(item.getPreferentialAmount());
                }
                actualTotalProductAmount = actualTotalProductAmount.add(actualProductAmount);

            }

            //TODO CT 计算体积和重量

            erpOrderItemService.updateOrderItemList(itemList, auth);

            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_9.getCode());
            order.setActualProductCount(totalActualProductCount);
            order.setActualTotalProductAmount(actualTotalProductAmount);
            order.setDeliveryTime(erpOrderDeliverRequest.getDeliveryTime());
            erpOrderInfoService.updateOrderByPrimaryKeySelectiveNoLog(order, auth);

        } else {
            throw new BusinessException("只有等待拣货状态且没有出货的订单才能执行该操作");
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderTransport(ErpOrderTransportRequest erpOrderTransportRequest) {

        if (erpOrderTransportRequest == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(erpOrderTransportRequest.getPersonId())) {
            throw new BusinessException("缺失操作人id");
        }
        if (StringUtils.isEmpty(erpOrderTransportRequest.getPersonName())) {
            throw new BusinessException("缺失操作人名称");
        }
        //操作人
        AuthToken auth = new AuthToken();
        auth.setPersonId(erpOrderTransportRequest.getPersonId());
        auth.setPersonName(erpOrderTransportRequest.getPersonName());

        //物流信息参数
        ErpOrderTransportLogisticsRequest logistics = erpOrderTransportRequest.getLogistics();

        //物流信息
        ErpOrderLogistics orderLogistics = null;
        if (logistics != null && StringUtils.isNotEmpty(logistics.getLogisticsCode())) {
            if (StringUtils.isEmpty(logistics.getLogisticsCentreCode())) {
                throw new BusinessException("缺失物流公司编号");
            }
            if (StringUtils.isEmpty(logistics.getLogisticsCentreName())) {
                throw new BusinessException("缺失物流公司名称");
            }
            if (StringUtils.isEmpty(logistics.getSendRepertoryCode())) {
                throw new BusinessException("缺失发货仓库编码");
            }
            if (StringUtils.isEmpty(logistics.getSendRepertoryName())) {
                throw new BusinessException("缺失发货仓库名称");
            }
            if (logistics.getLogisticsFee() == null) {
                throw new BusinessException("缺失物流费用");
            }
            orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsCode(logistics.getLogisticsCode());
            if (orderLogistics == null) {
                //新的物流信息，先保存物流信息

                ErpOrderLogistics newOrderLogistics = new ErpOrderLogistics();
                newOrderLogistics.setLogisticsId(OrderPublic.getUUID());
                newOrderLogistics.setLogisticsCode(logistics.getLogisticsCode());
                newOrderLogistics.setLogisticsCentreCode(logistics.getLogisticsCentreCode());
                newOrderLogistics.setLogisticsCentreName(logistics.getLogisticsCentreName());
                newOrderLogistics.setSendRepertoryCode(logistics.getSendRepertoryCode());
                newOrderLogistics.setSendRepertoryName(logistics.getSendRepertoryName());
                newOrderLogistics.setLogisticsFee(logistics.getLogisticsFee());
                newOrderLogistics.setPayStatus(ErpPayStatusEnum.UNPAID.getCode());
                erpOrderLogisticsService.saveOrderLogistics(newOrderLogistics, auth);
                orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsCode(logistics.getLogisticsCode());
            } else {
                if (orderLogistics.getLogisticsFee().compareTo(logistics.getLogisticsFee()) != 0) {
                    throw new BusinessException("物流单费用与已有物流单费用不相等");
                }
                if (!orderLogistics.getLogisticsCentreCode().equals(logistics.getLogisticsCentreCode())) {
                    throw new BusinessException("物流公司编码与已有物流单物流公司编码不相同");
                }
                if (!orderLogistics.getLogisticsCentreName().equals(logistics.getLogisticsCentreName())) {
                    throw new BusinessException("物流公司名称与已有物流单物流公司名称不相同");
                }
                if (!orderLogistics.getSendRepertoryCode().equals(logistics.getSendRepertoryCode())) {
                    throw new BusinessException("发货仓库编码与已有物流单发货仓库编码不相同");
                }
                if (!orderLogistics.getSendRepertoryName().equals(logistics.getSendRepertoryName())) {
                    throw new BusinessException("发货仓库名称与已有物流单发货仓库名称不相同");
                }
            }
        }

        List<String> orderCodeList = erpOrderTransportRequest.getOrderCodeList();
        if (orderCodeList == null || orderCodeList.size() <= 0) {
            throw new BusinessException("缺失发运订单号");
        }

        //订单类型
        ErpOrderTypeEnum orderTypeEnum = null;
        //加盟商id
        String franchiseeId = null;

        for (String orderCode :
                orderCodeList) {

            if (StringUtils.isEmpty(orderCode)) {
                throw new BusinessException("空订单号");
            }
            ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
            if (order == null) {
                throw new BusinessException("无效的订单号" + orderCode);
            }
            if (StringUtils.isEmpty(franchiseeId)) {
                franchiseeId = order.getFranchiseeId();
            } else {
                if (!franchiseeId.equals(order.getFranchiseeId())) {
                    throw new BusinessException("每次发运只能同时发运同一个加盟商的订单");
                }
            }
            if (orderTypeEnum == null) {
                orderTypeEnum = ErpOrderTypeEnum.getEnum(order.getOrderTypeCode());
            } else {
                if (!orderTypeEnum.getValue().equals(order.getOrderTypeCode())) {
                    throw new BusinessException("每次发运只能同时发运相同类型的订单");
                }
            }
            if (!ErpOrderNodeStatusEnum.STATUS_9.getCode().equals(order.getOrderNodeStatus())) {
                throw new BusinessException("不是待发运的订单不能发运");
            }

            ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(order.getOrderTypeCode(), order.getOrderCategoryCode());
            if (processTypeEnum == null) {
                throw new BusinessException("订单类型数据异常");
            }
            if (orderLogistics != null) {
                order.setLogisticsId(orderLogistics.getLogisticsId());
            } else {
                if (processTypeEnum.isHasLogisticsFee()) {
                    //如果物流单信息为空但是订单类型必须要物流单信息
                    throw new BusinessException("订单" + orderCode + "必须关联物流信息");
                }
            }

            order.setTransportStatus(erpOrderTransportRequest.getTransportStatus());
            order.setTransportTime(erpOrderTransportRequest.getTransportTime());
            order.setDistributionModeCode(erpOrderTransportRequest.getDistributionModeCode());
            order.setDistributionModeName(erpOrderTransportRequest.getDistributionModeName());
            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_11.getCode());
            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_10.getCode());
            erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);

            if (!processTypeEnum.isHasLogisticsFee()) {
                //不需要支付物流费用的订单
                order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_11.getCode());
                erpOrderInfoService.updateOrderByPrimaryKeySelectiveNoLog(order, auth);
            } else {
                if (orderLogistics != null && ErpPayStatusEnum.SUCCESS.getCode().equals(orderLogistics.getPayStatus())) {
                    order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_12.getCode());
                    order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_11.getCode());
                    erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);
                }
            }
        }

        if (orderLogistics != null) {
            //均摊物流费用
            this.distributeLogisticsFee(orderLogistics.getLogisticsCode());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void distributeLogisticsFee(String logisticsCode) {

        AuthToken auth = new AuthToken();
        //物流单
        ErpOrderLogistics orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsCode(logisticsCode);
        if (orderLogistics == null) {
            throw new BusinessException("无效的物流单");
        }
        auth.setPersonId(orderLogistics.getCreateById());
        auth.setPersonName(orderLogistics.getCreateByName());

        if (!ErpPayStatusEnum.SUCCESS.getCode().equals(orderLogistics.getPayStatus())) {
            //物流费用没有支付成功，不做计算
            return;
        }

        List<ErpOrderInfo> orderList = erpOrderQueryService.getOrderByLogisticsId(orderLogistics.getLogisticsId());
        if (orderList == null || orderList.size() == 0) {
            //没有订单
            return;
        }

        //所有订单发货商品总数量
        Long totalCount = 0L;
        for (ErpOrderInfo item :
                orderList) {
            totalCount += item.getActualProductCount();
        }

        //物流单实际支付费用
        BigDecimal fee = orderLogistics.getBalancePayFee();
        //占用费用
        BigDecimal useFee = BigDecimal.ZERO;
        for (int i = 0; i < orderList.size(); i++) {
            ErpOrderInfo order = orderList.get(i);
            BigDecimal thisFee = BigDecimal.ZERO;
            if (i < orderList.size() - 1) {
                //该订单占用运费
                thisFee = new BigDecimal(order.getActualProductCount()).divide(new BigDecimal(totalCount), 4, RoundingMode.HALF_UP);
                useFee = useFee.add(thisFee);
            } else {
                thisFee = fee.subtract(useFee);
            }

            order.setDeliverAmount(thisFee);
            erpOrderInfoService.updateOrderByPrimaryKeySelectiveNoLog(order, auth);
        }
    }
}
