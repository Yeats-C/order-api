package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderNodeProcessTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderTypeEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderDeliverRequest;
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
        AuthToken auth = AuthUtil.getCurrentAuth();

        if (erpOrderDeliverRequest == null) {
            throw new BusinessException("空参数");
        }
        List<ErpOrderInfo> paramOrderList = erpOrderDeliverRequest.getOrderList();
        if (paramOrderList == null || paramOrderList.size() <= 0) {
            throw new BusinessException("缺失发货订单为空");
        }
        //第一个订单参数
        ErpOrderInfo paramFirstOrderLine = paramOrderList.get(0);
        if (paramFirstOrderLine == null || StringUtils.isEmpty(paramFirstOrderLine.getOrderStoreCode())) {
            throw new BusinessException("发货订单缺失订单号");
        }
        //第一个订单
        ErpOrderInfo firstOrderLine = erpOrderQueryService.getOrderByOrderCode(paramFirstOrderLine.getOrderStoreCode());
        if (firstOrderLine == null) {
            throw new BusinessException("订单号" + paramFirstOrderLine.getOrderStoreCode() + "无效");
        }
        //订单加盟商id
        String franchiseeId = firstOrderLine.getFranchiseeId();
        ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(firstOrderLine.getOrderTypeCode(), firstOrderLine.getOrderCategoryCode());
        if (processTypeEnum == null) {
            throw new BusinessException("订单" + firstOrderLine.getOrderStoreCode() + "数据异常");
        }
        //是否支付物流单费用
        boolean hasLogisticsFee = processTypeEnum.isHasLogisticsFee();
        //订单类型
        ErpOrderTypeEnum orderTypeEnum = ErpOrderTypeEnum.getEnum(firstOrderLine.getOrderTypeCode());
        if (orderTypeEnum == null) {
            throw new BusinessException("订单" + firstOrderLine.getOrderStoreCode() + "数据异常");
        }


        //物流信息参数
        ErpOrderLogistics paramOrderLogistics = erpOrderDeliverRequest.getOrderLogistics();
        if (paramOrderLogistics == null) {
            if (hasLogisticsFee) {
                throw new BusinessException(orderTypeEnum.getDesc() + "必须关联物流信息为空");
            }
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


            ErpOrderLogistics orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsCode(paramOrderLogistics.getLogisticsCode());
            if (orderLogistics == null) {
                //如果是新物流单，先保存
                paramOrderLogistics.setLogisticsId(OrderPublic.getUUID());
                paramOrderLogistics.setPayStatus(ErpPayStatusEnum.UNPAID.getCode());
                erpOrderLogisticsService.saveOrderLogistics(paramOrderLogistics, auth);
            } else {
                //已有物流单，检验参数
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
        }

        int paramOrderIndex = 0;
        for (ErpOrderInfo paramOrder :
                paramOrderList) {
            paramOrderIndex++;
            if (paramOrder == null) {
                throw new BusinessException("发货订单参数第" + paramOrderIndex + "个订单为空");
            }
            if (StringUtils.isEmpty(paramOrder.getOrderStoreCode())) {
                throw new BusinessException("发货订单参数第" + paramOrderIndex + "个订单编号缺失");
            }
            List<ErpOrderItem> paramItemList = paramOrder.getItemList();
            if (paramItemList == null || paramItemList.size() <= 0) {
                throw new BusinessException("订单" + paramOrder.getOrderStoreCode() + "缺少发货商品明细行");
            }

            ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(paramOrder.getOrderStoreCode());
            if (order == null) {
                throw new BusinessException("订单号" + paramOrder.getOrderStoreCode() + "无效");
            }
            if (StringUtils.isEmpty(franchiseeId)) {
                franchiseeId = order.getFranchiseeId();
            }
            if (!franchiseeId.equals(order.getFranchiseeId())) {
                throw new BusinessException("不是同一个加盟商的订单不能同时发货");
            }
            if (!orderTypeEnum.getValue().equals(order.getOrderTypeCode())) {
                throw new BusinessException("不是相同类型的订单不能同时发货");
            }
            if (!ErpOrderStatusEnum.ORDER_STATUS_6.getCode().equals(order.getOrderStatus())) {
                throw new BusinessException("订单" + order.getOrderStoreCode() + "不是" + ErpOrderStatusEnum.ORDER_STATUS_6.getDesc() + "的订单不能发货");
            }

            //待发货订单商品明细行
            List<ErpOrderItem> orderItemList = erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId());
            //待发货订单商品明细行Map key:行号
            Map<Long, ErpOrderItem> orderItemMap = new HashMap<>(16);
            for (ErpOrderItem item :
                    orderItemList) {
                orderItemMap.put(item.getLineCode(), item);
            }

            //校验参数订单商品行
            Map<Long, ErpOrderItem> paramItemMap = new HashMap<>(16);
            for (ErpOrderItem item :
                    paramItemList) {
                if (item.getLineCode() == null) {
                    throw new BusinessException("订单" + order.getOrderStoreCode() + "第" + item.getLineCode() + "行缺失订单商品行号");
                }
                if (!orderItemMap.containsKey(item.getLineCode())) {
                    throw new BusinessException("订单" + order.getOrderStoreCode() + "第" + item.getLineCode() + "行商品明细行号无效");
                }
                if (paramItemMap.containsKey(item.getLineCode())) {
                    throw new BusinessException("订单" + order.getOrderStoreCode() + "第" + item.getLineCode() + "行商品明细行重复");
                }
                if (item.getActualProductCount() == null) {
                    throw new BusinessException("订单" + order.getOrderStoreCode() + "第" + item.getLineCode() + "行缺失发货数量");
                } else {
                    if (item.getActualProductCount() < 0) {
                        throw new BusinessException("订单" + order.getOrderStoreCode() + "第" + item.getLineCode() + "行发货数量不能小于0");
                    }
                }
                paramItemMap.put(item.getLineCode(), item);
            }

            //需要更新的明细行
            List<ErpOrderItem> updateProductItemList = new ArrayList<>();

            //是否生成冲减单
            boolean reductionFlag = false;
            //实发数量汇总
            long totalActualProductCount = 0L;
            //遍历修改订单商品行
            for (ErpOrderItem item :
                    orderItemList) {
                if (!paramItemMap.containsKey(item.getLineCode())) {
                    throw new BusinessException("订单" + order.getOrderStoreCode() + "缺失行号为" + item.getLineCode() + "的商品行");
                }
                //发货参数明细行
                ErpOrderItem paramItem = paramItemMap.get(item.getLineCode());
                if (paramItem.getActualProductCount() > item.getProductCount()) {
                    throw new BusinessException("订单" + order.getOrderStoreCode() + "订单明细行" + item.getLineCode() + "发货数量大于下单数量");
                }

                //发货总数量汇总
                totalActualProductCount += paramItem.getActualProductCount();

                ErpOrderItem updateItem = new ErpOrderItem();
                updateItem.setId(item.getId());
                updateItem.setActualProductCount(paramItem.getActualProductCount());
                updateProductItemList.add(updateItem);

                if (paramItem.getActualProductCount() < item.getProductCount()) {
                    //发货数量小于下单数量，该订单需要生成冲减单
                    reductionFlag = true;
                }
            }

            //订单自动跳过正在拣货
            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_7.getCode());
            erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);

            //订单自动跳过扫描完成
            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_8.getCode());
            erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);

            //修改订单明细行
            erpOrderItemService.updateOrderItemList(updateProductItemList, auth);
            //订单修改为已发货
            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_11.getCode());
            order.setDistributionModeCode(paramOrder.getDistributionModeCode());
            order.setDistributionModeName(paramOrder.getDistributionModeName());
            order.setDeliveryTime(paramOrder.getDeliveryTime());
            order.setTransportTime(paramOrder.getTransportTime());
            order.setTransportStatus(paramOrder.getTransportStatus());
            order.setReceiveTime(paramOrder.getReceiveTime());
            order.setActualProductCount(totalActualProductCount);
            if (paramOrderLogistics != null) {
                //如果关联了物流单
                order.setTransportCenterCode(paramOrderLogistics.getLogisticsCentreCode());
                order.setTransportCenterName(paramOrderLogistics.getLogisticsCentreName());
                order.setTransportCode(paramOrderLogistics.getLogisticsCode());
                order.setLogisticsId(paramOrderLogistics.getLogisticsId());
            }
            erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);

            if (paramOrderLogistics != null) {
                //如果物流单已经支付成功，订单修改为已支付物流费用
                if (ErpPayStatusEnum.SUCCESS.getCode().equals(paramOrderLogistics.getPayStatus())) {
                    order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_12.getCode());
                    erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);
                    //分配运费
                    this.distributeLogisticsFee(paramOrderLogistics.getLogisticsCode());
                }
            }

            if (reductionFlag) {
                //TODO 标记订单需要生成冲减单
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void distributeLogisticsFee(String logisticsCode) {

        AuthToken auth = AuthUtil.getCurrentAuth();

        //物流单
        ErpOrderLogistics orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsCode(logisticsCode);
        if (orderLogistics == null) {
            throw new BusinessException("无效的物流单");
        }

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
