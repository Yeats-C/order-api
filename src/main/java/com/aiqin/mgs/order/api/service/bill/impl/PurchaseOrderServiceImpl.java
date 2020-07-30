package com.aiqin.mgs.order.api.service.bill.impl;

import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.mgs.order.api.component.enums.PurchaseOrderStatusEnum;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderDeliverItemRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderTransportLogisticsRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderTransportRequest;
import com.aiqin.mgs.order.api.service.bill.CreatePurchaseOrderService;
import com.google.common.collect.Lists;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.*;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderDeliverRequest;
import com.aiqin.mgs.order.api.service.bill.PurchaseOrderService;
import com.aiqin.mgs.order.api.service.order.ErpOrderDeliverService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * 爱亲采购单 实现类
 */
@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);

    @Resource
    PurchaseOrderDao purchaseOrderDao;
    @Resource
    PurchaseOrderDetailDao purchaseOrderDetailDao;
    @Resource
    ErpOrderDeliverService erpOrderDeliverService;
    @Resource
    CreatePurchaseOrderService createPurchaseOrderService;

    @Resource
    PurchaseOrderDetailBatchDao purchaseOrderDetailBatchDao;

    @Override
    public HttpResponse createPurchaseOrder(@Valid List<ErpOrderInfo> erpOrderInfos) {
        LOGGER.info("根据ERP订单生成爱亲采购单，采购单开始，erpOrderInfo{}", JsonUtil.toJson(erpOrderInfos));
        if (CollectionUtils.isNotEmpty(erpOrderInfos)) {
            //异步执行。
            purchaseOrderExecutor(erpOrderInfos);
            return HttpResponse.success();
        } else {
            LOGGER.error("订单为空 erpOrderInfo {}" + erpOrderInfos);
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
    }

    @Override
    @Transactional
    public HttpResponse updatePurchaseInfo(OrderIogisticsVo purchaseInfo) {
        if (purchaseInfo == null
                || purchaseInfo.getOrderStoreDetail() == null
                || purchaseInfo.getOrderStoreDetail().size() <= 0) {
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
        LOGGER.info("耘链销售单回传更新开始 参数purchaseInfo{}" + purchaseInfo);
        try {
            //更新订单&订单明细
            ErpOrderDeliverRequest erpOrderDeliverRequest = new ErpOrderDeliverRequest();
            erpOrderDeliverRequest.setOrderCode(purchaseInfo.getOrderStoreCode());
            erpOrderDeliverRequest.setDeliveryTime(purchaseInfo.getDeliveryTime());
            erpOrderDeliverRequest.setPersonId(purchaseInfo.getPersonId());
            erpOrderDeliverRequest.setPersonName(purchaseInfo.getPersonName());
            List<ErpOrderDeliverItemRequest> orderDeliverItemList = Lists.newArrayList();
            if(CollectionUtils.isNotEmpty(purchaseInfo.getOrderStoreDetail())) {
                for (OrderStoreDetail orderStoreDetail : purchaseInfo.getOrderStoreDetail()) {
                    ErpOrderDeliverItemRequest orderDeliverItem = new ErpOrderDeliverItemRequest();
                    orderDeliverItem.setLineCode(orderStoreDetail.getLineCode());
                    orderDeliverItem.setActualProductCount(orderStoreDetail.getActualProductCount());
                    orderDeliverItemList.add(orderDeliverItem);
                }
            }
            erpOrderDeliverRequest.setItemList(orderDeliverItemList);
            erpOrderDeliverService.orderDeliver(erpOrderDeliverRequest);

            //更新采购单
            PurchaseOrderInfo purchaseOrder = new PurchaseOrderInfo();
            purchaseOrder.setActualTotalCount(purchaseInfo.getActualTotalCount());
            purchaseOrder.setDeliveryTime(purchaseInfo.getDeliveryTime());
            purchaseOrder.setContactPerson(purchaseInfo.getDeliveryPersonId());
            purchaseOrder.setPurchaseOrderCode(purchaseInfo.getOrderStoreCode());
            purchaseOrder.setUpdateById(purchaseInfo.getPersonId());
            purchaseOrder.setUpdateByName(purchaseInfo.getPersonName());
            purchaseOrder.setUpdateTime(new Date());
            purchaseOrderDao.updateByPrimaryKey(purchaseOrder);

            //更新采购单明细
            for (OrderStoreDetail orderStoreDetail : purchaseInfo.getOrderStoreDetail()) {
                PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
                //行号
                purchaseOrderDetail.setLineCode(orderStoreDetail.getLineCode());
                //SKU编码
                purchaseOrderDetail.setSkuCode(orderStoreDetail.getSkuCode());
                //SKU名称
                purchaseOrderDetail.setSkuName(orderStoreDetail.getSkuName());
                //实发数量
                purchaseOrderDetail.setTotalCount(orderStoreDetail.getActualProductCount());
                purchaseOrderDetail.setPurchaseOrderCode(purchaseInfo.getOrderStoreCode());
                purchaseOrderDetail.setUpdateById(purchaseInfo.getPersonId());
                purchaseOrderDetail.setUpdateByName(purchaseInfo.getPersonName());
                purchaseOrderDetail.setUpdateTime(new Date());
                purchaseOrderDetailDao.updateByPurchaseOrderCode(purchaseOrderDetail);
            }

            //添加订单批次明细
            if (CollectionUtils.isNotEmpty(purchaseInfo.getOrderBatchStoreDetail())) {
                for (OrderBatchStoreDetail batchStoreDetail : purchaseInfo.getOrderBatchStoreDetail()) {
                    PurchaseOrderDetailBatch purchaseOrderDetailBatch = new PurchaseOrderDetailBatch();
                    purchaseOrderDetailBatch.setPurchaseOrderDetailBatchId(IdUtil.purchaseId());
                    purchaseOrderDetailBatch.setPurchaseOrderCode(purchaseInfo.getOrderStoreCode());
                    purchaseOrderDetailBatch.setBatchCode(batchStoreDetail.getBatchCode());
                    //SKU编码
                    purchaseOrderDetailBatch.setSkuCode(batchStoreDetail.getSkuCode());
                    //SKU名称
                    purchaseOrderDetailBatch.setSkuCode(batchStoreDetail.getSkuName());
                    //实际销售数量
                    purchaseOrderDetailBatch.setActualTotalCount(purchaseInfo.getActualTotalCount());
                    //SKU名称
                    purchaseOrderDetailBatch.setSkuName(batchStoreDetail.getSkuName());
                    //行号
                    purchaseOrderDetailBatch.setLineCode(batchStoreDetail.getLineCode());
                    purchaseOrderDetailBatch.setCreateById(purchaseInfo.getPersonId());
                    purchaseOrderDetailBatch.setCreateByName(purchaseInfo.getPersonName());
                    purchaseOrderDetailBatch.setCreateTime(new Date());
                    purchaseOrderDetailBatchDao.insert(purchaseOrderDetailBatch);
                }
            }
            return HttpResponse.success();
        } catch (Exception e) {
            LOGGER.error("耘链销售单回传更新失败 {}", e);
            return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
    }

    @Override
    public HttpResponse updateOrderStoreLogistics(DeliveryInfoVo deliveryInfoVo) {
        LOGGER.info("发运单回传开始，deliveryInfoVo {}" + JsonUtil.toJson(deliveryInfoVo));
        try {
            ErpOrderTransportRequest orderTransport = new ErpOrderTransportRequest();
            ErpOrderTransportLogisticsRequest logistics = new ErpOrderTransportLogisticsRequest();
            //物流单号
            logistics.setLogisticsCode(deliveryInfoVo.getTransportCode());
            //物流公司编码
            logistics.setLogisticsCentreCode(deliveryInfoVo.getTransportCompanyCode());
            //物流公司名称
            logistics.setLogisticsCentreName(deliveryInfoVo.getTransportCompanyName());
            //发货仓库编码
            logistics.setSendRepertoryCode(deliveryInfoVo.getTransportCenterCode());
            //发货仓库名称
            logistics.setSendRepertoryName(deliveryInfoVo.getTransportCenterName());
            //物流费用
            logistics.setLogisticsFee(deliveryInfoVo.getTransportAmount());
            //物流信息 不需要物流单的订单不需要传
            orderTransport.setLogistics(logistics);
            //发运时间
            orderTransport.setTransportTime(deliveryInfoVo.getTransportDate());
            //orderTransport.setDistributionModeCode(deliveryInfoVo.getDistributionModeCode());//配送方式编码
            //orderTransport.setDistributionModeName(deliveryInfoVo.getDistributionModeName());//配送方式名称
            orderTransport.setPersonId(deliveryInfoVo.getCustomerCode());
            orderTransport.setPersonName(deliveryInfoVo.getCustomerName());
            orderTransport.setTransportStatus(deliveryInfoVo.getTransportStatus());
            List<String> listDeliveryDetail = new ArrayList<>();
            deliveryInfoVo.getDeliveryDetail();
            for (DeliveryDetailInfo deliveryDetails : deliveryInfoVo.getDeliveryDetail()) {
                listDeliveryDetail.add(deliveryDetails.getOrderCode());
            }
            orderTransport.setOrderCodeList(listDeliveryDetail);
            //orderTransport.setOrderCodeList(deliveryInfoVo.getTransportCode());//该物流单关联的订单，必须是同一个加盟商，同一个类型的订单
            erpOrderDeliverService.orderTransport(orderTransport);
            return HttpResponse.success();
        } catch (Exception e) {
            LOGGER.error("发运单回传失败 {}", e);
            return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
    }

    /**
     * 异步执行.
     *
     */
    private void purchaseOrderExecutor(List<ErpOrderInfo> erpOrderInfos) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //根据ERP订单生成爱亲采购单&采购单明细&修改订单同步状态
                    createPurchaseOrderService.addOrderAndDetail(erpOrderInfos);
                } catch (Exception e) {
                    LOGGER.error("同步ERP采购单失败" + e);
                    throw new RuntimeException();
                }
            }
        });
    }

    //取消订单
    @Override
    public HttpResponse updateCancelOrderinfo(String orderStoreCode) {
        PurchaseOrderInfo purchaseOrder = new PurchaseOrderInfo();
        Integer purchaseOrderStatusRemove = PurchaseOrderStatusEnum.PURCHASE_ORDER_STATUS_REMOVE.getCode();
        //采购单状态
        purchaseOrder.setPurchaseOrderStatus(purchaseOrderStatusRemove);
        purchaseOrder.setPurchaseOrderCode(orderStoreCode);
        int result = purchaseOrderDao.updateByPurchaseOrderStatus(purchaseOrder);
        if (result == 1) {
            LOGGER.info("采购单取消成功");
            return HttpResponse.success();
        } else {
            LOGGER.error("采购单取消失败");
            return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
    }
}