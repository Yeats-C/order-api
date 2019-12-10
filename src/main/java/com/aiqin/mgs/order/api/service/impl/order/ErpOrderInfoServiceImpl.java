package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpProductTypeEnum;
import com.aiqin.mgs.order.api.component.enums.YesOrNoEnum;
import com.aiqin.mgs.order.api.dao.order.ErpOrderInfoDao;
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

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ErpOrderInfoServiceImpl implements ErpOrderInfoService {

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

    @Override
    public void saveOrder(ErpOrderInfo po, AuthToken auth) {
        po.setCreateById(auth.getPersonId());
        po.setCreateByName(auth.getPersonName());
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer insert = erpOrderInfoDao.insert(po);

        //保存日志
        erpOrderOperationLogService.saveOrderOperationLog(po.getOrderId(), ErpOrderStatusEnum.ORDER_STATUS_1, auth);
    }

    @Override
    public void saveOrderNoLog(ErpOrderInfo po, AuthToken auth) {
        po.setCreateById(auth.getPersonId());
        po.setCreateByName(auth.getPersonName());
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer insert = erpOrderInfoDao.insert(po);
    }

    @Override
    public void updateOrderByPrimaryKeySelective(ErpOrderInfo po, AuthToken auth) {

        //更新订单数据
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer integer = erpOrderInfoDao.updateByPrimaryKeySelective(po);

        //保存订单操作日志
        erpOrderOperationLogService.saveOrderOperationLog(po.getOrderId(), ErpOrderStatusEnum.getEnum(po.getOrderStatus()), auth);
    }

    @Override
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
            if (StringUtils.isEmpty(paramOrderLogistics.getLogisticCode())) {
                throw new BusinessException("缺失物流单号");
            }
            if (StringUtils.isEmpty(paramOrderLogistics.getLogisticCentreCode())) {
                throw new BusinessException("缺失物流公司编码");
            }
            if (StringUtils.isEmpty(paramOrderLogistics.getLogisticCentreName())) {
                throw new BusinessException("缺失物流公司名称");
            }
            if (paramOrderLogistics.getLogisticFee() == null) {
                throw new BusinessException("缺失物流费用");
            } else {
                if (paramOrderLogistics.getLogisticFee().compareTo(BigDecimal.ZERO) < 0) {
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

        ErpOrderLogistics orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsCode(paramOrderLogistics.getLogisticCode());
        if (orderLogistics == null) {
            paramOrderLogistics.setLogisticsId(OrderPublic.getUUID());
            paramOrderLogistics.setPaid(YesOrNoEnum.NO.getCode());
            erpOrderLogisticsService.saveOrderLogistics(paramOrderLogistics, auth);
        } else {
            if (orderLogistics.getLogisticFee().compareTo(paramOrderLogistics.getLogisticFee()) != 0) {
                throw new BusinessException("物流单费用与已有物流单费用不相等");
            }
            if (!orderLogistics.getLogisticCentreCode().equals(paramOrderLogistics.getLogisticCentreCode())) {
                throw new BusinessException("物流公司编码与已有物流单物流公司编码不相同");
            }
            if (!orderLogistics.getLogisticCentreName().equals(paramOrderLogistics.getLogisticCentreName())) {
                throw new BusinessException("物流公司名称与已有物流单物流公司名称不相同");
            }
            if (!orderLogistics.getSendRepertoryCode().equals(paramOrderLogistics.getSendRepertoryCode())) {
                throw new BusinessException("发货仓库编码与已有物流单发货仓库编码不相同");
            }
            if (!orderLogistics.getSendRepertoryName().equals(paramOrderLogistics.getSendRepertoryName())) {
                throw new BusinessException("发货仓库名称与已有物流单发货仓库名称不相同");
            }
            paramOrderLogistics.setLogisticsId(orderLogistics.getLogisticsId());
            paramOrderLogistics.setPaid(orderLogistics.getPaid());
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

            if (YesOrNoEnum.YES.getCode().equals(paramOrderLogistics.getPaid())) {
                //如果物流单已经支付成功，订单修改为已支付物流费用
                order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_12.getCode());
                this.updateOrderByPrimaryKeySelective(order, auth);
            }

        }
    }

    @Override
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
