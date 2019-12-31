package com.aiqin.mgs.order.api.service.bill.impl;
import com.aiqin.mgs.order.api.component.enums.ErpLogOperationTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpLogSourceTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpLogStatusTypeEnum;
import com.aiqin.mgs.order.api.dao.order.ErpOrderLogisticsDao;
import com.aiqin.mgs.order.api.domain.PurchaseBanchInfo;
import java.math.BigDecimal;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.*;
import com.aiqin.mgs.order.api.dao.order.ErpOrderInfoDao;
import com.aiqin.mgs.order.api.dao.order.ErpOrderItemDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics;
import com.aiqin.mgs.order.api.service.bill.PurchaseOrderService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    @Autowired
    PurchaseOrderDao purchaseOrderDao;
    @Autowired
    PurchaseOrderDetailDao purchaseOrderDetailDao;
    @Autowired
    OperationLogDao operationLogDao;
    @Autowired
    PurchaseOrderDetailBatchDao purchaseOrderDetailBatchDao;
    @Autowired
    ErpOrderInfoDao erpOrderInfoDao;
    @Autowired
    ErpOrderItemDao erpOrderItemDao;
    @Autowired
    OrderStoreDetailBatchDao orderStoreDetailBatchDao;
    @Autowired
    ErpOrderLogisticsDao erpOrderLogisticsDao;

    @Override
    public HttpResponse createPurchaseOrder(@Valid ErpOrderInfo erpOrderInfo) {
        LOGGER.info("同步采购单，erpOrderInfo{}", erpOrderInfo);
        //异步执行
        purchaseOrderExecutor(erpOrderInfo);
        if(erpOrderInfo != null){
            //返回
            return HttpResponse.success();
        }
        return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
    }

    @Override
    @Transactional
    public HttpResponse updatePurchaseInfo(List<PurchaseInfoVo> purchaseInfo) {
        LOGGER.info("耘链销售单回传更新开始 参数purchaseInfo{}"+purchaseInfo);
        try {
        for (PurchaseInfoVo order : purchaseInfo) {
            //更新订单

            PurchaseOrder purchaseOrder = new PurchaseOrder();
            purchaseOrder.setPurchaseOrderCode(order.getOrderCode());
            purchaseOrder.setActualTotalCount(order.getActualTotalCount());
            purchaseOrder.setDeliveryTime(order.getDeliveryTime());
            purchaseOrder.setContactPerson(order.getDeliveryPersionId());
            //更新采购单
            purchaseOrderDao.updateByPrimaryKeySelective(purchaseOrder);

            //更新订单明细

            //更新采购单明细
            for(PurchaseDetailInfo orderDetail : order.getPurchaseDetailInfo()){
                PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
                purchaseOrderDetail.setLineCode(orderDetail.getLineCode());
                purchaseOrderDetail.setSkuCode(orderDetail.getSkuCode());
                purchaseOrderDetail.setSkuName(orderDetail.getSkuName());
                purchaseOrderDetail.setTotalCount(orderDetail.getActualToalCount());
                purchaseOrderDetailDao.updateByPrimaryKeySelective(purchaseOrderDetail);
            }

            //添加销售明细批次
            if (order.getPurchaseBanchInfo() != null && order.getPurchaseBanchInfo().size() > 0) {
                for (PurchaseBanchInfo orderDetailBanch : order.getPurchaseBanchInfo()) {
                    OrderStoreDetailBatch orderStoreDetailBatch = new OrderStoreDetailBatch();
                    orderStoreDetailBatch.setLineCode(orderDetailBanch.getLineCode());
                    orderStoreDetailBatch.setBatchCode(orderDetailBanch.getBatchCode());
                    orderStoreDetailBatch.setSkuCode(orderDetailBanch.getSkuCode());
                    orderStoreDetailBatch.setSkuName(orderDetailBanch.getSkuName());
                    orderStoreDetailBatch.setProductDate(orderDetailBanch.getProductDate());
                    //orderDetailBanch.getActualToalCount()
                    orderStoreDetailBatchDao.updateByPrimaryKeySelective(orderStoreDetailBatch);
                }
            }
            LOGGER.info("耘链销售单回传,销售单单号为DeliveryPersionId："+order.getDeliveryPersionId()+"的更新成功");
        }
        return HttpResponse.success();
        } catch (Exception e) {
            LOGGER.error("耘链销售单回传更新失败 {}", e);
            return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
    }

    @Override
    public HttpResponse updateOrderStoreLogistics(List<DeliveryInfoVo> deliveryInfoVo) {
        ErpOrderLogistics erpOrderLogistics = new ErpOrderLogistics();
        erpOrderLogisticsDao.select(erpOrderLogistics);
        return null;
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
                //添加采购单
                LOGGER.info("开始同步采购单，参数为：erpOrderInfo{}", erpOrderInfo);
                addPurchaseOrder(erpOrderInfo);
                LOGGER.info("同步采购单完成");

                //添加采购商品信息
                LOGGER.info("开始同步采购单商品详情，erpOrderInfo{}", erpOrderInfo);
                addPurchaseOrderDetail(erpOrderInfo);
                LOGGER.info("同步采购单商品详结束");

                //修改订单同步状态
                updateOrderSuccess(erpOrderInfo);

                //根据爱亲采购单，生成耘链销售单
                LOGGER.info("开始根据爱亲采购单，生成耘链销售单，参数为：erpOrderInfo{}", erpOrderInfo);
                createSaleOrder(erpOrderInfo);
                LOGGER.info("根据爱亲采购单，生成耘链销售单结束");

                //添加操作日志
                LOGGER.info("根据订单生产爱亲采购单，添加操作日志开始···");
                addPurchaseOrderLog(erpOrderInfo);
                LOGGER.info("根据订单生产爱亲采购单，添加操作日志结束···");
            }
        });
    }

    /**
     * 修改订单同步状态
     * @param erpOrderInfo
     */
    private void updateOrderSuccess(ErpOrderInfo erpOrderInfo) {
        erpOrderInfoDao.updateOrderSuccess(erpOrderInfo.getOrderStoreId());
    }

    /**
     * 生成栖耘销售单
     * @param erpOrderInfo
     */
    private void createSaleOrder(ErpOrderInfo erpOrderInfo) {
        String url =purchaseHost+"/order/aiqin/sale";
        HttpClient httpGet = HttpClient.post(url.toString()).json(erpOrderInfo).timeout(10000);
        httpGet.action();
    }

    /**
     * 同步采购单
     * @param erpOrderInfo
     */
    private void addPurchaseOrder(ErpOrderInfo erpOrderInfo) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseOrderId(erpOrderInfo.getOrderStoreId());
        purchaseOrder.setPurchaseOrderCode(erpOrderInfo.getOrderStoreCode());
        purchaseOrder.setCompanyCode(erpOrderInfo.getCompanyCode());
        purchaseOrder.setCompanyName(erpOrderInfo.getCompanyName());
        purchaseOrder.setSupplierCode(erpOrderInfo.getSupplierCode());
        purchaseOrder.setSupplierName(erpOrderInfo.getSupplierName());
        purchaseOrder.setTransportCenterCode(erpOrderInfo.getTransportCenterCode());
        purchaseOrder.setTransportCenterName(erpOrderInfo.getTransportCenterName());
        purchaseOrder.setWarehouseCode(erpOrderInfo.getWarehouseCode());
        purchaseOrder.setWarehouseName(erpOrderInfo.getWarehouseName());
        purchaseOrder.setPurchaseGroupCode("");
        purchaseOrder.setPurchaseGroupName("");
        purchaseOrder.setSettlementMethodCode(erpOrderInfo.getPaymentCode());
        purchaseOrder.setSettlementMethodName(erpOrderInfo.getPaymentName());
        purchaseOrder.setPurchaseOrderStatus(erpOrderInfo.getOrderStatus());
        purchaseOrder.setPurchaseMode(0);
        purchaseOrder.setPurchaseType(0);
        purchaseOrder.setTotalCount(0L);
        purchaseOrder.setTotalTaxAmount(erpOrderInfo.getTotalProductAmount());
        purchaseOrder.setActualTotalCount(1L);
        purchaseOrder.setActualTotalTaxAmount(new BigDecimal("0"));
        purchaseOrder.setCancelReason("");
        purchaseOrder.setCancelRemark(erpOrderInfo.getRemake());
        purchaseOrder.setUseStatus(erpOrderInfo.getUseStatus());
        purchaseOrder.setChargePerson("");
        purchaseOrder.setAccountCode("");
        purchaseOrder.setAccountName("");
        purchaseOrder.setContractCode("");
        purchaseOrder.setContractName("");
        purchaseOrder.setContactPerson(erpOrderInfo.getReceivePerson());
        purchaseOrder.setContactMobile("");
        purchaseOrder.setPreArrivalTime(new Date());
        purchaseOrder.setValidTime(new Date());
        purchaseOrder.setDeliveryAddress(erpOrderInfo.getReceiveAddress());
        purchaseOrder.setDeliveryTime(erpOrderInfo.getDeliveryTime());
        purchaseOrder.setInStockTime(new Date());
        purchaseOrder.setInStockAddress(erpOrderInfo.getReceiveAddress());
        purchaseOrder.setPrePurchaseOrder(erpOrderInfo.getMainOrderCode());
        purchaseOrder.setPaymentCode(erpOrderInfo.getPaymentCode());
        purchaseOrder.setPaymentName(erpOrderInfo.getPaymentName());
        purchaseOrder.setPaymentTime(erpOrderInfo.getPaymentTime());
        purchaseOrder.setPreAmount(erpOrderInfo.getOrderAmount());
        purchaseOrder.setRemark(erpOrderInfo.getRemake());
        purchaseOrder.setSourceCode(erpOrderInfo.getOrderStoreCode());
        purchaseOrder.setSourceType(erpOrderInfo.getSourceType());
        purchaseOrder.setCreateById(erpOrderInfo.getCreateById());
        purchaseOrder.setCreateByName(erpOrderInfo.getCreateByName());
        purchaseOrder.setUpdateById(erpOrderInfo.getUpdateById());
        purchaseOrder.setUpdateByName(erpOrderInfo.getUpdateByName());
        purchaseOrder.setCreateTime(erpOrderInfo.getCreateTime());
        purchaseOrder.setUpdateTime(erpOrderInfo.getUpdateTime());

        purchaseOrderDao.insertSelective(purchaseOrder);
    }

    /**
     * 同步采购商品信息
     * @param erpOrderInfo
     */
    private void addPurchaseOrderDetail(ErpOrderInfo erpOrderInfo) {
        List<ErpOrderItem> itemList = erpOrderInfo.getItemList();
        List<PurchaseOrderDetail> purchaseOrderDetailList= new ArrayList<>();
        for (ErpOrderItem item : itemList){
            PurchaseOrderDetail purchaseOrderDetail =  new PurchaseOrderDetail();
            purchaseOrderDetail.setPurchaseOrderDetailId(item.getOrderInfoDetailId());
            purchaseOrderDetail.setPurchaseOrderCode(item.getOrderStoreCode());
            purchaseOrderDetail.setSpuCode(item.getSpuCode());
            purchaseOrderDetail.setSpuName(item.getSpuName());
            purchaseOrderDetail.setSkuCode(item.getSkuCode());
            purchaseOrderDetail.setSkuName(item.getSkuName());
            purchaseOrderDetail.setBrandCode(item.getActivityCode());
            purchaseOrderDetail.setBrandName(item.getColorName());
            purchaseOrderDetail.setCategoryCode("");
            purchaseOrderDetail.setCategoryName("");
            purchaseOrderDetail.setProductSpec("");
            purchaseOrderDetail.setColorCode("");
            purchaseOrderDetail.setColorName("");
            purchaseOrderDetail.setModelCode("");
            purchaseOrderDetail.setUnitCode("");
            purchaseOrderDetail.setUnitName("");
            purchaseOrderDetail.setProductType(item.getProductType());
            purchaseOrderDetail.setPurchaseWhole(0L);
            purchaseOrderDetail.setPurchaseSingle(0L);
            purchaseOrderDetail.setBaseProductSpec(0L);
            purchaseOrderDetail.setBoxGauge("");
            purchaseOrderDetail.setTaxRate(new BigDecimal("0"));
            purchaseOrderDetail.setLineCode(0L);
            purchaseOrderDetail.setFactorySkuCode("");
            purchaseOrderDetail.setTaxAmount(item.getPurchaseAmount());
            purchaseOrderDetail.setTotalTaxAmount(item.getTotalAcivityAmount());
            purchaseOrderDetail.setTotalCount(item.getProductCount());
            purchaseOrderDetail.setActualTotalTaxAmount(item.getActualTotalProductAmount());
            purchaseOrderDetail.setUseStatus(item.getUseStatus());
            purchaseOrderDetail.setCreateById(item.getCreateById());
            purchaseOrderDetail.setCreateByName(item.getCreateByName());
            purchaseOrderDetail.setUpdateById(item.getUpdateById());
            purchaseOrderDetail.setUpdateByName(item.getUpdateByName());
            purchaseOrderDetail.setCreateTime(item.getCreateTime());
            purchaseOrderDetail.setUpdateTime(item.getUpdateTime());
            purchaseOrderDetailList.add(purchaseOrderDetail);

            purchaseOrderDetailDao.insertSelective(purchaseOrderDetail);
        }
    }

    /**
     * 定时扫描同步失败的销售单
     * 每半小时执行一次
     */
    @Scheduled(cron = "0 0/30 * * * ? ")
    public void TimedFailedPurchaseOrder() {
        PurchaseOrderDetailBatch purchaseOrderDetailBatch = new PurchaseOrderDetailBatch();
        purchaseOrderDetailBatchDao.updateByPrimaryKeySelective(purchaseOrderDetailBatch);
        //调用销售单 TODO
    }

    /**
     * 根据订单生成爱亲采购单,添加操作日志
     * @param erpOrderInfo
     */
    private void addPurchaseOrderLog(ErpOrderInfo erpOrderInfo) {
        OperationLog operationLog = insertSelective();
        operationLog.setOperationCode(erpOrderInfo.getOrderStoreCode());//来源编码
        operationLog.setOperationContent("根据订单生成爱亲采购单");//日志内容
        operationLog.setRemark("");//备注

        operationLogDao.insertSelective(operationLog);
    }

    private OperationLog insertSelective() {
        OperationLog operationLog = new OperationLog();
        ErpLogSourceTypeEnum purchase =  ErpLogSourceTypeEnum.PURCHASE;
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
        return operationLog;
    }
}