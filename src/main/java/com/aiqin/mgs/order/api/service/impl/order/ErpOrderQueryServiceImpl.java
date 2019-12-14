package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.PagesRequest;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.dao.order.ErpOrderInfoDao;
import com.aiqin.mgs.order.api.domain.EnumItemInfo;
import com.aiqin.mgs.order.api.domain.po.order.*;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderQueryRequest;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderPayStatusResponse;
import com.aiqin.mgs.order.api.service.order.*;
import com.aiqin.mgs.order.api.util.PageAutoHelperUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    private ErpOrderConsigneeService erpOrderConsigneeService;
    @Resource
    private ErpOrderOperationLogService erpOrderOperationLogService;
    @Resource
    private ErpOrderLogisticsService erpOrderLogisticsService;
    @Resource
    private ErpOrderFeeService erpOrderFeeService;
    @Resource
    private ErpOrderRequestService erpOrderRequestService;

    @Override
    public ErpOrderInfo getOrderByOrderId(String orderId) {
        ErpOrderInfo order = null;
        if (StringUtils.isNotEmpty(orderId)) {
            ErpOrderInfo orderInfoQuery = new ErpOrderInfo();
            orderInfoQuery.setOrderId(orderId);
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
            orderInfoQuery.setOrderCode(orderCode);
            List<ErpOrderInfo> select = erpOrderInfoDao.select(orderInfoQuery);
            if (select != null && select.size() > 0) {
                order = select.get(0);
            }
        }
        return order;
    }

    @Override
    public List<ErpOrderInfo> getSecondOrderListByPrimaryCode(String orderCode) {
        List<ErpOrderInfo> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(orderCode)) {
            ErpOrderInfo query = new ErpOrderInfo();
            query.setOrderLevel(ErpOrderLevelEnum.SECONDARY.getCode());
            query.setPrimaryCode(orderCode);
            list = erpOrderInfoDao.select(query);
        }
        return list;
    }

    @Override
    public ErpOrderInfo getOrderDetailByOrderCode(String orderCode) {
        ErpOrderInfo order = this.getOrderByOrderCode(orderCode);
        if (order != null) {

            if (ErpOrderLevelEnum.PRIMARY.getCode().equals(order.getOrderLevel())) {
                //主订单

                //获取拆分订单
                if (YesOrNoEnum.YES.getCode().equals(order.getSplitStatus())) {
                    List<ErpOrderInfo> secondOrderList = getSecondOrderListByPrimaryCode(order.getOrderCode());
                    order.setSecondaryOrderList(secondOrderList);
                }

            } else {
                //子订单

                //获取上级订单
                ErpOrderInfo primaryOrder = getOrderByOrderCode(order.getPrimaryCode());
                order.setPrimaryOrder(primaryOrder);
            }

            //订单明细行
            List<ErpOrderItem> orderItemList = erpOrderItemService.selectOrderItemListByOrderId(order.getOrderId());
            order.setOrderItemList(orderItemList);

            //订单费用
            ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByFeeId(order.getFeeId());
            if (orderFee != null) {
                order.setPayStatus(orderFee.getPayStatus());
                ErpOrderPay orderPay = erpOrderPayService.getOrderPayByPayId(orderFee.getPayId());
                orderFee.setOrderPay(orderPay);
            }
            order.setOrderFee(orderFee);

            //订单收货人信息
            ErpOrderConsignee orderConsignee = erpOrderConsigneeService.getOrderConsigneeByOrderId(order.getOrderId());
            order.setOrderConsignee(orderConsignee);

            //订单操作日志
            List<ErpOrderOperationLog> orderOperationLogList = erpOrderOperationLogService.selectOrderOperationLogListByOrderId(order.getOrderId());
            order.setOrderOperationLogList(orderOperationLogList);

            //订单物流信息
            ErpOrderLogistics orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsId(order.getLogisticsId());
            if (orderLogistics != null) {
                ErpOrderPay logisticsPay = erpOrderPayService.getOrderPayByPayId(orderLogistics.getPayId());
                orderLogistics.setLogisticsPay(logisticsPay);
            }
            order.setOrderLogistics(orderLogistics);

            //能不能编辑新增赠品行
            order.setAddProductGiftOperation(YesOrNoEnum.NO.getCode());
            ErpOrderTypeEnum erpOrderTypeEnum = ErpOrderTypeEnum.getEnum(order.getOrderType());
            if (ErpOrderStatusEnum.ORDER_STATUS_1.getCode().equals(order.getOrderStatus())) {
                if (erpOrderTypeEnum != null && erpOrderTypeEnum.isAddProductGift()) {
                    order.setAddProductGiftOperation(YesOrNoEnum.YES.getCode());
                }
            }

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
    public PageResData<ErpOrderInfo> findOrderList(ErpOrderQueryRequest erpOrderQueryRequest) {
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
                primaryOrderCodeList.add(item.getOrderCode());
            }

            //查询子订单列表
            Map<String, List<ErpOrderInfo>> secondaryOrderMap = new HashMap<>(16);
            List<ErpOrderInfo> secondaryOrderList = erpOrderInfoDao.findSecondaryOrderList(primaryOrderCodeList);
            if (secondaryOrderList != null && secondaryOrderList.size() > 0) {
                for (ErpOrderInfo item :
                        secondaryOrderList) {
                    String primaryCode = item.getPrimaryCode();
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
                if (secondaryOrderMap.containsKey(item.getOrderCode())) {
                    item.setSecondaryOrderList(secondaryOrderMap.get(item.getOrderCode()));
                }

                //支付状态与订单中心不同步的订单标记
                item.setRepayOperation(YesOrNoEnum.NO.getCode());
                //只检查待支付和已取消的订单
                if (ErpOrderStatusEnum.ORDER_STATUS_1.getCode().equals(item.getOrderStatus()) || ErpOrderStatusEnum.ORDER_STATUS_99.getCode().equals(item.getOrderStatus())) {
                    ErpOrderPayStatusResponse orderPayStatusResponse = erpOrderRequestService.getOrderPayStatus(item.getOrderCode());
                    if (orderPayStatusResponse.isRequestSuccess() && ErpPayStatusEnum.SUCCESS == orderPayStatusResponse.getPayStatusEnum()) {
                        //如果支付状态是成功的
                        item.setRepayOperation(YesOrNoEnum.YES.getCode());
                    }
                }
            }
        }

        return pageResData;
    }

    @Override
    public PageResData<ErpOrderInfo> findRackOrderList(ErpOrderQueryRequest erpOrderQueryRequest) {
        List<Integer> orderTypeQueryList = new ArrayList<>();
        for (EnumItemInfo item :
                ErpOrderTypeEnum.STORAGE_RACK_SELECT_LIST) {
            orderTypeQueryList.add(item.getCode());
        }
        erpOrderQueryRequest.setOrderTypeQueryList(orderTypeQueryList);
        return this.findOrderList(erpOrderQueryRequest);
    }

    @Override
    public ErpOrderInfo getNeedSignOrderDetailByOrderCode(String orderCode) {
        ErpOrderInfo order = this.getOrderByOrderCode(orderCode);
        if (order != null) {

            //订单明细行
            List<ErpOrderItem> orderItemList = erpOrderItemService.selectOrderItemListByOrderId(order.getOrderId());
            order.setOrderItemList(orderItemList);

            BigDecimal shareMoneyTotal = BigDecimal.ZERO;
            BigDecimal moneyTotal = BigDecimal.ZERO;
            for (ErpOrderItem item :
                    orderItemList) {
                shareMoneyTotal = shareMoneyTotal.add(item.getShareMoney());
                moneyTotal = moneyTotal.add(item.getMoney());
            }
            order.setTotalMoney(moneyTotal);
            order.setPayMoney(shareMoneyTotal);

            //订单收货人信息
            ErpOrderConsignee orderConsignee = erpOrderConsigneeService.getOrderConsigneeByOrderId(order.getOrderId());
            order.setOrderConsignee(orderConsignee);

            //订单物流信息
            ErpOrderLogistics orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsId(order.getLogisticsId());
            order.setOrderLogistics(orderLogistics);

        }
        return order;
    }

    @Override
    public int getNeedSignOrderQuantity(String storeCode) {
        int quantity = 0;
        if (StringUtils.isEmpty(storeCode)) {
            throw new BusinessException("缺失门店编码");
        }
        ErpOrderInfo query = new ErpOrderInfo();
        query.setStoreCode(storeCode);
        query.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_11.getCode());
        List<ErpOrderInfo> select = erpOrderInfoDao.select(query);
        if (select != null && select.size() > 0) {
            for (ErpOrderInfo item :
                    select) {
                ErpOrderTypeEnum orderTypeEnum = ErpOrderTypeEnum.getEnum(item.getOrderType());
                if (orderTypeEnum != null) {
                    if (!orderTypeEnum.isHasLogisticsFee()) {
                        quantity++;
                    }
                }
            }
        }

        ErpOrderInfo logisticsQuery = new ErpOrderInfo();
        logisticsQuery.setStoreCode(storeCode);
        logisticsQuery.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_12.getCode());
        List<ErpOrderInfo> logisticsSelect = erpOrderInfoDao.select(logisticsQuery);
        if (logisticsSelect != null && logisticsSelect.size() > 0) {
            for (ErpOrderInfo item :
                    select) {
                quantity++;
            }
        }
        return quantity;
    }

}
