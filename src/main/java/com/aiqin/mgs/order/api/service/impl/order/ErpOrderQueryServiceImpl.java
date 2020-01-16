package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.PagesRequest;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.dao.order.ErpOrderInfoDao;
import com.aiqin.mgs.order.api.domain.po.order.*;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderQueryRequest;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderOperationControlResponse;
import com.aiqin.mgs.order.api.service.order.*;
import com.aiqin.mgs.order.api.util.PageAutoHelperUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ErpOrderQueryServiceImpl implements ErpOrderQueryService {

    @Resource
    private ErpOrderInfoDao erpOrderInfoDao;
    @Resource
    private ErpOrderItemService erpOrderItemService;
    @Resource
    private ErpOrderPayService erpOrderPayService;
    @Resource
    private ErpOrderOperationLogService erpOrderOperationLogService;
    @Resource
    private ErpOrderLogisticsService erpOrderLogisticsService;
    @Resource
    private ErpOrderFeeService erpOrderFeeService;
    @Resource
    private ErpOrderRefundService erpOrderRefundService;

    @Override
    public ErpOrderInfo getOrderByOrderId(String orderId) {
        ErpOrderInfo order = null;
        if (StringUtils.isNotEmpty(orderId)) {
            ErpOrderInfo orderInfoQuery = new ErpOrderInfo();
            orderInfoQuery.setOrderStoreId(orderId);
            List<ErpOrderInfo> select = erpOrderInfoDao.select(orderInfoQuery);
            if (select != null && select.size() > 0) {
                order = select.get(0);
            }
        }
        return order;
    }

    @Override
    public ErpOrderInfo getOrderByOrderCode(String orderCode) {
        ErpOrderInfo order = null;
        if (StringUtils.isNotEmpty(orderCode)) {
            ErpOrderInfo orderInfoQuery = new ErpOrderInfo();
            orderInfoQuery.setOrderStoreCode(orderCode);
            List<ErpOrderInfo> select = erpOrderInfoDao.select(orderInfoQuery);
            if (select != null && select.size() > 0) {
                order = select.get(0);
            }
        }
        return order;
    }

    @Override
    public ErpOrderInfo getOrderAndItemByOrderCode(String orderCode) {
        ErpOrderInfo order = getOrderByOrderCode(orderCode);
        if (order != null) {
            order.setItemList(erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId()));
        }
        return order;
    }

    @Override
    public List<ErpOrderInfo> getSecondOrderListByPrimaryCode(String orderCode) {
        List<ErpOrderInfo> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(orderCode)) {
            ErpOrderInfo query = new ErpOrderInfo();
            query.setOrderLevel(ErpOrderLevelEnum.SECONDARY.getCode());
            query.setMainOrderCode(orderCode);
            list = erpOrderInfoDao.select(query);
        }
        return list;
    }

    @Override
    public ErpOrderInfo getOrderDetailByOrderCode(String orderCode) {
        ErpOrderInfo order = this.getOrderByOrderCode(orderCode);
        if (order != null) {

            List<ErpOrderItem> orderItemList = erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId());
            order.setItemList(orderItemList);

            List<ErpOrderOperationLog> operationLogList = erpOrderOperationLogService.selectOrderOperationLogList(order.getOrderStoreCode());
            order.setOperationLogList(operationLogList);

            if (ErpOrderLevelEnum.PRIMARY.getCode().equals(order.getOrderLevel())) {
                //主订单

                //获取拆分订单
                if (StatusEnum.YES.getCode().equals(order.getSplitStatus())) {
                    List<ErpOrderInfo> secondOrderList = getSecondOrderListByPrimaryCode(order.getOrderStoreCode());
                    order.setSecondaryOrderList(secondOrderList);
                }

            }

            //订单费用
            ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByFeeId(order.getFeeId());
            if (orderFee != null) {
                ErpOrderPay orderPay = erpOrderPayService.getOrderPayByPayId(orderFee.getPayId());
                if (orderPay != null) {
                    orderFee.setOrderPay(orderPay);
                    orderFee.setPayCode(orderPay.getPayCode());
                }
            }
            order.setOrderFee(orderFee);


            //订单物流信息
            ErpOrderLogistics orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsId(order.getLogisticsId());
            order.setOrderLogistics(orderLogistics);

            //退款信息
            ErpOrderRefund orderRefund = erpOrderRefundService.getOrderRefundByOrderIdAndRefundType(order.getOrderStoreId(), ErpOrderRefundTypeEnum.ORDER_CANCEL);
            order.setOrderRefund(orderRefund);

            //操作按钮配置
            orderOperationConfig(order);


        }
        return order;
    }

    @Override
    public List<ErpOrderInfo> getOrderByLogisticsId(String logisticsId) {
        List<ErpOrderInfo> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(logisticsId)) {
            ErpOrderInfo orderInfoQuery = new ErpOrderInfo();
            orderInfoQuery.setLogisticsId(logisticsId);
            list = erpOrderInfoDao.select(orderInfoQuery);
        }
        return list;
    }

    @Override
    public List<ErpOrderInfo> select(ErpOrderInfo erpOrderInfo) {
        return erpOrderInfoDao.select(erpOrderInfo);
    }

    @Override
    public ErpOrderInfo getNeedSignOrderDetailByOrderCode(String orderCode) {
        ErpOrderInfo order = this.getOrderByOrderCode(orderCode);
        if (order != null) {

            //订单明细行
            List<ErpOrderItem> orderItemList = erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId());
            order.setItemList(orderItemList);

            //订单物流信息
            ErpOrderLogistics orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsId(order.getLogisticsId());
            order.setOrderLogistics(orderLogistics);

        }
        return order;
    }

    @Override
    public int getNeedSignOrderQuantity(String storeId) {
        int quantity = 0;
        if (StringUtils.isEmpty(storeId)) {
            throw new BusinessException("缺失门店id");
        }
        ErpOrderInfo query = new ErpOrderInfo();
        query.setStoreId(storeId);
        query.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_11.getCode());
        List<ErpOrderInfo> select = erpOrderInfoDao.select(query);
        if (select != null && select.size() > 0) {
            for (ErpOrderInfo item :
                    select) {
                ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(item.getOrderTypeCode(), item.getOrderCategoryCode());
                if (processTypeEnum != null && !processTypeEnum.isHasLogisticsFee()) {
                    quantity++;
                }
            }
        }

        ErpOrderInfo logisticsQuery = new ErpOrderInfo();
        logisticsQuery.setStoreId(storeId);
        logisticsQuery.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_12.getCode());
        List<ErpOrderInfo> logisticsSelect = erpOrderInfoDao.select(logisticsQuery);
        if (logisticsSelect != null && logisticsSelect.size() > 0) {
            quantity += logisticsSelect.size();
        }
        return quantity;
    }

    @Override
    public PageResData<ErpOrderInfo> findOrderList(ErpOrderQueryRequest erpOrderQueryRequest) {

        //增加查询类型类别范围
        ErpOrderTypeCategoryQueryTypeEnum queryTypeEnum = erpOrderQueryRequest.getQueryTypeEnum();
        List<Integer> orderTypeQueryList = new ArrayList<>();
        List<Integer> orderCategoryQueryList = new ArrayList<>();
        for (ErpOrderTypeCategoryControlEnum item :
                ErpOrderTypeCategoryControlEnum.values()) {
            if (queryTypeEnum == ErpOrderTypeCategoryQueryTypeEnum.ERP_ORDER_LIST_QUERY) {
                if (!item.isErpQuery()) {
                    continue;
                }
            } else if (queryTypeEnum == ErpOrderTypeCategoryQueryTypeEnum.ERP_RACK_ORDER_LIST_QUERY) {
                if (!item.isErpRackQuery()) {
                    continue;
                }
            } else if (queryTypeEnum == ErpOrderTypeCategoryQueryTypeEnum.STORE_ORDER_LIST_QUERY) {
                if (!item.isStoreQuery()) {
                    continue;
                }
            } else {
                continue;
            }
            if (!orderTypeQueryList.contains(item.getOrderTypeEnum().getCode())) {
                orderTypeQueryList.add(item.getOrderTypeEnum().getCode());
            }
            if (!orderCategoryQueryList.contains(item.getOrderCategoryEnum().getCode())) {
                orderCategoryQueryList.add(item.getOrderCategoryEnum().getCode());
            }
        }
        erpOrderQueryRequest.setOrderTypeQueryList(orderTypeQueryList);
        erpOrderQueryRequest.setOrderCategoryQueryList(orderCategoryQueryList);


        //查询主订单列表
        erpOrderQueryRequest.setOrderLevel(ErpOrderLevelEnum.PRIMARY.getCode());
        PagesRequest page = new PagesRequest();
        page.setPageNo(erpOrderQueryRequest.getPageNo() == null ? 1 : erpOrderQueryRequest.getPageNo());
        page.setPageSize(erpOrderQueryRequest.getPageSize() == null ? 10 : erpOrderQueryRequest.getPageSize());
        PageResData<ErpOrderInfo> pageResData = PageAutoHelperUtil.generatePageRes(() -> erpOrderInfoDao.findOrderList(erpOrderQueryRequest), page);

        //查询子订单列表
        List<ErpOrderInfo> dataList = pageResData.getDataList();
        if (dataList != null && dataList.size() > 0) {

            //获取主订单编码，检查待支付和已取消订单支付状态
            List<String> primaryOrderCodeList = new ArrayList<>();
            for (ErpOrderInfo item :
                    dataList) {
                primaryOrderCodeList.add(item.getOrderStoreCode());
            }

            //查询子订单列表
            Map<String, List<ErpOrderInfo>> secondaryOrderMap = new HashMap<>(16);
            ErpOrderQueryRequest secondOrderQueryRequest = new ErpOrderQueryRequest();
            secondOrderQueryRequest.setOrderLevel(ErpOrderLevelEnum.SECONDARY.getCode());
            secondOrderQueryRequest.setPrimaryOrderCodeList(primaryOrderCodeList);
            secondOrderQueryRequest.setOrderStatus(erpOrderQueryRequest.getOrderStatus());
            List<ErpOrderInfo> secondaryOrderList = erpOrderInfoDao.findSecondaryOrderList(secondOrderQueryRequest);
            if (secondaryOrderList != null && secondaryOrderList.size() > 0) {
                for (ErpOrderInfo item :
                        secondaryOrderList) {

                    //设置子订单按钮
                    orderOperationConfig(item);

                    String primaryCode = item.getMainOrderCode();
                    if (secondaryOrderMap.containsKey(primaryCode)) {
                        secondaryOrderMap.get(primaryCode).add(item);
                    } else {
                        List<ErpOrderInfo> newSecondaryOrderList = new ArrayList<>();
                        newSecondaryOrderList.add(item);
                        secondaryOrderMap.put(primaryCode, newSecondaryOrderList);
                    }
                }
            }

            for (ErpOrderInfo item :
                    dataList) {
                if (secondaryOrderMap.containsKey(item.getOrderStoreCode())) {
                    item.setSecondaryOrderList(secondaryOrderMap.get(item.getOrderStoreCode()));
                }

                //设置按钮
                orderOperationConfig(item);
            }
        }

        return pageResData;
    }

    @Override
    public PageResData<ErpOrderInfo> findErpOrderList(ErpOrderQueryRequest erpOrderQueryRequest) {
        erpOrderQueryRequest.setQueryTypeEnum(ErpOrderTypeCategoryQueryTypeEnum.ERP_ORDER_LIST_QUERY);
        return this.findOrderList(erpOrderQueryRequest);
    }

    @Override
    public PageResData<ErpOrderInfo> findErpRackOrderList(ErpOrderQueryRequest erpOrderQueryRequest) {
        erpOrderQueryRequest.setQueryTypeEnum(ErpOrderTypeCategoryQueryTypeEnum.ERP_RACK_ORDER_LIST_QUERY);
        return this.findOrderList(erpOrderQueryRequest);
    }

    @Override
    public PageResData<ErpOrderInfo> findStoreOrderList(ErpOrderQueryRequest erpOrderQueryRequest) {
        erpOrderQueryRequest.setQueryTypeEnum(ErpOrderTypeCategoryQueryTypeEnum.STORE_ORDER_LIST_QUERY);
        return this.findOrderList(erpOrderQueryRequest);
    }

    @Override
    public String getMaxOrderCodeByCurrentDay(String currentDay) {
        return erpOrderInfoDao.getMaxOrderCodeByCurrentDay(currentDay);
    }

    /**
     * 设置订单按钮控制参数
     *
     * @param order 订单
     */
    private void orderOperationConfig(ErpOrderInfo order) {

        if (order == null) {
            return;
        }
        ErpOrderOperationControlResponse control = new ErpOrderOperationControlResponse();
        order.setOperation(control);
        //订单状态
        ErpOrderStatusEnum orderStatusEnum = ErpOrderStatusEnum.getEnum(order.getOrderStatus());
        if (orderStatusEnum == null) {
            return;
        }
        ErpOrderNodeStatusEnum orderNodeStatusEnum = ErpOrderNodeStatusEnum.getEnum(order.getOrderNodeStatus());
        if (orderNodeStatusEnum == null) {
            return;
        }
        ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(order.getOrderTypeCode(), order.getOrderCategoryCode());
        if (processTypeEnum == null) {
            return;
        }
        ErpOrderCategoryEnum orderCategoryEnum = ErpOrderCategoryEnum.getEnum(order.getOrderCategoryCode());
        if (orderCategoryEnum == null) {
            return;
        }

        //爱掌柜查看按钮
        if (orderStatusEnum == ErpOrderStatusEnum.ORDER_STATUS_1) {
            control.setDetail(StatusEnum.NO.getCode());
        }

        //erp异常订单 确认订单按钮
        if (orderStatusEnum == ErpOrderStatusEnum.ORDER_STATUS_2 || orderStatusEnum == ErpOrderStatusEnum.ORDER_STATUS_3 || orderStatusEnum == ErpOrderStatusEnum.ORDER_STATUS_4) {
            if (!ErpOrderNodeStatusEnum.STATUS_4.getCode().equals(order.getOrderNodeStatus())) {
                control.setAbnormal(StatusEnum.YES.getCode());
            }
        } else if (orderStatusEnum == ErpOrderStatusEnum.ORDER_STATUS_1) {
            if (orderNodeStatusEnum == ErpOrderNodeStatusEnum.STATUS_1 || orderNodeStatusEnum == ErpOrderNodeStatusEnum.STATUS_4) {
                if (processTypeEnum.isAutoPay()) {
                    control.setAbnormal(StatusEnum.YES.getCode());
                }
            }
        } else if (orderStatusEnum == ErpOrderStatusEnum.ORDER_STATUS_97 || orderStatusEnum == ErpOrderStatusEnum.ORDER_STATUS_98) {
            if (ErpOrderNodeStatusEnum.STATUS_31.getCode() <= order.getOrderNodeStatus() && order.getOrderNodeStatus() < ErpOrderNodeStatusEnum.STATUS_37.getCode()) {
                control.setAbnormal(StatusEnum.YES.getCode());
            }
        } else {

        }

        //erp添加赠品
        if (processTypeEnum.isAddProductGift() && orderStatusEnum == ErpOrderStatusEnum.ORDER_STATUS_1) {
            control.setAddGift(StatusEnum.YES.getCode());
        }

        //支付订单费用
        if (orderStatusEnum == ErpOrderStatusEnum.ORDER_STATUS_1) {
            if (!orderCategoryEnum.isFirstOrder()) {
                control.setPayOrder(StatusEnum.YES.getCode());
            }
        }

        //签收按钮
        boolean allowedSign = (processTypeEnum.isHasLogisticsFee() && orderStatusEnum == ErpOrderStatusEnum.ORDER_STATUS_12) || (!processTypeEnum.isHasLogisticsFee() && orderStatusEnum == ErpOrderStatusEnum.ORDER_STATUS_11);
        if (allowedSign) {
            control.setSign(StatusEnum.YES.getCode());
        }

        //支付物流费用
        if (processTypeEnum.isHasLogisticsFee() && orderStatusEnum == ErpOrderStatusEnum.ORDER_STATUS_11) {
            control.setPayLogistics(StatusEnum.YES.getCode());
        }

        //取消
        if (orderStatusEnum == ErpOrderStatusEnum.ORDER_STATUS_1 || orderStatusEnum == ErpOrderStatusEnum.ORDER_STATUS_6) {
            if (!orderCategoryEnum.isFirstOrder()) {
                control.setCancel(StatusEnum.YES.getCode());
            }
        }

        //是否显示物流详情
        if (processTypeEnum.isHasLogisticsFee()) {
            control.setLogisticsArea(StatusEnum.YES.getCode());
        }

        //重新加入购物车
        if (orderStatusEnum == ErpOrderStatusEnum.ORDER_STATUS_99 || orderStatusEnum == ErpOrderStatusEnum.ORDER_STATUS_98) {
            control.setRejoinCart(StatusEnum.YES.getCode());
        }

        //退货
        if (orderStatusEnum == ErpOrderStatusEnum.ORDER_STATUS_13) {
            if (!ErpOrderReturnStatusEnum.SUCCESS.getCode().equals(order.getOrderReturn())) {
                if (StatusEnum.NO.getCode().equals(order.getOrderReturnProcess())) {
                    if (!orderCategoryEnum.isFirstOrder()) {
                        control.setOrderReturn(StatusEnum.YES.getCode());
                    }
                }
            }
        }

        //退款
        if (orderStatusEnum == ErpOrderStatusEnum.ORDER_STATUS_97 || orderStatusEnum == ErpOrderStatusEnum.ORDER_STATUS_98) {
            control.setRefund(StatusEnum.YES.getCode());
            //获取退款状态
            ErpOrderRefund orderRefund = erpOrderRefundService.getOrderRefundByOrderIdAndRefundType(order.getOrderStoreId(), ErpOrderRefundTypeEnum.ORDER_CANCEL);
            if (orderRefund != null) {
                order.setOrderRefund(orderRefund);
            }
        }
    }
}