package com.aiqin.mgs.order.api.service.bill.impl;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderDeliverItemRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderTransportLogisticsRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderTransportRequest;
import com.aiqin.mgs.order.api.service.bill.CreatePurchaseOrderService;
import com.aiqin.mgs.order.api.util.RequestReturnUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;

import com.aiqin.mgs.order.api.component.enums.ErpLogOperationTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpLogSourceTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpLogStatusTypeEnum;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.*;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderDeliverRequest;
import com.aiqin.mgs.order.api.service.bill.PurchaseOrderService;
import com.aiqin.mgs.order.api.service.order.ErpOrderDeliverService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 爱亲采购单 实现类
 */
@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);

    @Value("${purchase.host}")
    private String purchaseHost;
    @Resource
    PurchaseOrderDao purchaseOrderDao;
    @Resource
    PurchaseOrderDetailDao purchaseOrderDetailDao;
    @Resource
    OperationLogDao operationLogDao;
    @Resource
    OrderStoreDetailBatchDao orderStoreDetailBatchDao;
    @Resource
    ErpOrderDeliverService erpOrderDeliverService;
    @Resource
    CreatePurchaseOrderService createPurchaseOrderService;

    @Override
    public HttpResponse createPurchaseOrder(@Valid ErpOrderInfo erpOrderInfo) {
        LOGGER.info("同步采购单，erpOrderInfo{}", erpOrderInfo);
        //异步执行
        purchaseOrderExecutor(erpOrderInfo);
        return HttpResponse.success();
    }

    @Override
    @Transactional
    public HttpResponse updatePurchaseInfo(OrderIogisticsVo purchaseInfo) {
        LOGGER.info("耘链销售单回传更新开始 参数purchaseInfo{}" + purchaseInfo);
        try {
            //更新订单&订单明细
            ErpOrderDeliverRequest erpOrderDeliverRequest = new ErpOrderDeliverRequest();
            erpOrderDeliverRequest.setOrderCode(purchaseInfo.getOrderStoreCode());
            erpOrderDeliverRequest.setDeliveryTime(purchaseInfo.getDeliveryTime());
            erpOrderDeliverRequest.setPersonId(purchaseInfo.getPersonId());
            erpOrderDeliverRequest.setPersonName(purchaseInfo.getPersonName());
            List<ErpOrderDeliverItemRequest> orderDeliverItemList = Lists.newArrayList();
            for (OrderStoreDetail orderStoreDetail : purchaseInfo.getOrderStoreDetail()) {
                ErpOrderDeliverItemRequest orderDeliverItem = new ErpOrderDeliverItemRequest();
                orderDeliverItem.setLineCode(orderStoreDetail.getLineCode());
                orderDeliverItem.setActualProductCount(orderStoreDetail.getActualProductCount());
                orderDeliverItemList.add(orderDeliverItem);
            }
            erpOrderDeliverRequest.setItemList(orderDeliverItemList);
            erpOrderDeliverService.orderDeliver(erpOrderDeliverRequest);

            //更新采购单
            PurchaseOrder purchaseOrder = new PurchaseOrder();
            purchaseOrder.setActualTotalCount(purchaseInfo.getActualTotalCount());//
            purchaseOrder.setDeliveryTime(purchaseInfo.getDeliveryTime());//
            purchaseOrder.setContactPerson(purchaseInfo.getDeliveryPersonId());//
            purchaseOrderDao.updateByPrimaryKeySelective(purchaseOrder);

            //更新采购单明细
            for (OrderStoreDetail orderStoreDetail : purchaseInfo.getOrderStoreDetail()) {
                PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
                purchaseOrderDetail.setLineCode(orderStoreDetail.getLineCode());//行号
                purchaseOrderDetail.setSkuCode(orderStoreDetail.getSkuCode());//SKU编码
                purchaseOrderDetail.setSkuName(orderStoreDetail.getSkuName());//SKU名称
                purchaseOrderDetail.setTotalCount(orderStoreDetail.getActualProductCount());//实发数量
                purchaseOrderDetailDao.updateByPrimaryKeySelective(purchaseOrderDetail);
            }

            //添加订单批次明细
            for (OrderBatchStoreDetail batchStoreDetail : purchaseInfo.getOrderBatchStoreDetail()) {
                OrderStoreDetailBatch orderStoreDetailBatch = new OrderStoreDetailBatch();
                orderStoreDetailBatch.setSkuCode(batchStoreDetail.getSkuCode());//SKU编码
                orderStoreDetailBatch.setSkuName(batchStoreDetail.getSkuName());//SKU名称
                orderStoreDetailBatch.setBatchCode(batchStoreDetail.getBatchCode());//批次编号
                orderStoreDetailBatch.setProductDate(batchStoreDetail.getProductDate());//生产日期
                orderStoreDetailBatch.setActualProductCount(purchaseInfo.getActualTotalCount());//实际销售数量
                orderStoreDetailBatch.setLineCode(batchStoreDetail.getLineCode());//行号
                orderStoreDetailBatchDao.insert(orderStoreDetailBatch);
            }
            return HttpResponse.success();
        } catch (Exception e) {
            LOGGER.error("耘链销售单回传更新失败 {}", e);
            return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
    }

    @Override
    public HttpResponse updateOrderStoreLogistics(DeliveryInfoVo deliveryInfoVo) {
        LOGGER.info("发运单回传开始，deliveryInfoVo {}" + deliveryInfoVo);
        try {
            ErpOrderTransportRequest orderTransport = new ErpOrderTransportRequest();
            ErpOrderTransportLogisticsRequest logistics = new ErpOrderTransportLogisticsRequest();
            logistics.setLogisticsCode(deliveryInfoVo.getTransportCompanyCode());
            logistics.setLogisticsCentreCode(deliveryInfoVo.getCustomerCode());
            logistics.setLogisticsCentreName(deliveryInfoVo.getCustomerName());
            logistics.setSendRepertoryCode(deliveryInfoVo.getTransportCenterCode());
            logistics.setSendRepertoryName(deliveryInfoVo.getTransportCenterName());
            orderTransport.setLogistics(logistics);//物流信息 不需要物流单的订单不需要传
            orderTransport.setTransportTime(deliveryInfoVo.getTransportDate());//发运时间
            //orderTransport.setDistributionModeCode(deliveryInfoVo.getDistributionModeCode());//配送方式编码
            //orderTransport.setDistributionModeName(deliveryInfoVo.getDistributionModeName());//配送方式名称
            orderTransport.setPersonId(deliveryInfoVo.getCustomerCode());
            orderTransport.setPersonName(deliveryInfoVo.getCustomerName());
            orderTransport.setTransportStatus(deliveryInfoVo.getTransportStatus());
            //orderTransport.setOrderCodeList(deliveryInfoVo.getTransportCode());//该物流单关联的订单，必须是同一个加盟商，同一个类型的订单
            erpOrderDeliverService.orderTransport(orderTransport);
            return HttpResponse.success();
        } catch (Exception e) {
            LOGGER.error("发运单回传失败 {}", e);
            return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
    }

    /**
     * 异步执行
     *
     * @param erpOrderInfo
     */
    private void purchaseOrderExecutor(ErpOrderInfo erpOrderInfo) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //生成采购单¥&采购单明细&修改订单同步状态
                    LOGGER.info("开始生成采购单¥&采购单明细&修改订单同步状态，参数为：erpOrderInfo{}", erpOrderInfo);
                    createPurchaseOrderService.addOrderAndDetail(erpOrderInfo);
                    LOGGER.info("生成采购单¥&采购单明细&修改订单同步状态,结束");

                    //根据爱亲采购单，生成耘链销售单
                    LOGGER.info("开始根据爱亲采购单，生成耘链销售单，参数为：erpOrderInfo{}", erpOrderInfo);
                    createSaleOrder(erpOrderInfo);
                    LOGGER.info("根据爱亲采购单，生成耘链销售单结束");

                    //添加操作日志
                    LOGGER.info("根据订单生产爱亲采购单，添加操作日志开始···");
                    addPurchaseOrderLog(erpOrderInfo);
                    LOGGER.info("根据订单生产爱亲采购单，添加操作日志结束···");
                } catch (Exception e) {
                    LOGGER.error("同步采购单失败" + e);
                    throw new RuntimeException();
                }
            }
        });
    }

    /**
     * 生成栖耘销售单
     *
     * @param erpOrderInfo
     */
    private void createSaleOrder(ErpOrderInfo erpOrderInfo) {
        String url = purchaseHost + "/order/aiqin/sale";
        HttpClient httpGet = HttpClient.post(url).json(erpOrderInfo).timeout(10000);
        HttpResponse<Object> response = httpGet.action().result(new TypeReference<HttpResponse<Object>>() {
        });
        if (!RequestReturnUtil.validateHttpResponse(response)) {
            throw new BusinessException(response.getMessage());
        }
    }

    /**
     * 根据订单生成爱亲采购单,添加操作日志
     *
     * @param erpOrderInfo
     */
    private void addPurchaseOrderLog(ErpOrderInfo erpOrderInfo) {
        OperationLog operationLog = new OperationLog();
        ErpLogSourceTypeEnum purchase = ErpLogSourceTypeEnum.PURCHASE;
        operationLog.setOperationType(purchase.getCode());//日志类型 0 .新增 1.修改 2.删除 3.下载
        ErpLogOperationTypeEnum add = ErpLogOperationTypeEnum.ADD;
        operationLog.setSourceType(add.getCode());//来源类型 0.销售 1.采购 2.退货  3.退供
        ErpLogStatusTypeEnum using = ErpLogStatusTypeEnum.USING;
        operationLog.setUseStatus(String.valueOf(using.getCode()));//0. 启用 1.禁用
        AuthToken auth = AuthUtil.getCurrentAuth();
        operationLog.setCreateById(auth.getPersonId());
        operationLog.setCreateByName(auth.getPersonName());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        operationLog.setCreateTime(formatter.format(new Date()));
        operationLog.setOperationCode(erpOrderInfo.getOrderStoreCode());//来源编码
        operationLog.setOperationContent("根据ERP订单和订单明细生成爱亲采购单和采购单明细");//日志内容
        operationLog.setRemark("");//备注
        operationLogDao.insertSelective(operationLog);
    }
}