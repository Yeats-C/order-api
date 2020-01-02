package com.aiqin.mgs.order.api.service.bill.impl;

import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderDeliverItemRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderTransportLogisticsRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderTransportRequest;
import com.google.common.collect.Lists;

import com.aiqin.mgs.order.api.component.enums.ErpLogOperationTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpLogSourceTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpLogStatusTypeEnum;
import com.aiqin.mgs.order.api.dao.order.ErpOrderLogisticsDao;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.*;
import com.aiqin.mgs.order.api.dao.order.ErpOrderInfoDao;
import com.aiqin.mgs.order.api.dao.order.ErpOrderItemDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderDeliverRequest;
import com.aiqin.mgs.order.api.service.bill.PurchaseOrderService;
import com.aiqin.mgs.order.api.service.order.ErpOrderDeliverService;
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
    @Autowired
    ErpOrderDeliverService erpOrderDeliverService;

    @Override
    public HttpResponse createPurchaseOrder(@Valid ErpOrderInfo erpOrderInfo) {
        LOGGER.info("同步采购单，erpOrderInfo{}", erpOrderInfo);
        //异步执行
        purchaseOrderExecutor(erpOrderInfo);
        if (erpOrderInfo != null) {
            //返回
            return HttpResponse.success();
        }
        return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
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
        try {
            ErpOrderTransportRequest orderTransport = new ErpOrderTransportRequest();
            ErpOrderTransportLogisticsRequest logistics = new ErpOrderTransportLogisticsRequest();
            logistics.setLogisticsCode(deliveryInfoVo.getTransportCompanyCode());
            logistics.setLogisticsCentreCode(deliveryInfoVo.getCustomerCode());
            logistics.setLogisticsCentreName(deliveryInfoVo.getCustomerName());
            logistics.setSendRepertoryCode(deliveryInfoVo.getSendRepertoryCode());
            logistics.setSendRepertoryName(deliveryInfoVo.getSendRepertoryName());
            logistics.setLogisticsFee(deliveryInfoVo.getLogisticsFee());
            orderTransport.setLogistics(logistics);//物流信息 不需要物流单的订单不需要传
            orderTransport.setTransportTime(deliveryInfoVo.getCustomerTime());//发运时间
            orderTransport.setDistributionModeCode(deliveryInfoVo.getDistributionModeCode());//配送方式编码
            orderTransport.setDistributionModeName(deliveryInfoVo.getDistributionModeName());//配送方式名称
            orderTransport.setPersonId(deliveryInfoVo.getCustomerPersonId());
            orderTransport.setPersonName(deliveryInfoVo.getCustomerName());
            orderTransport.setTransportStatus(deliveryInfoVo.getTransportStatus());
            orderTransport.setOrderCodeList(deliveryInfoVo.getTransportCode());//该物流单关联的订单，必须是同一个加盟商，同一个类型的订单
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
                //添加采购单
                LOGGER.info("开始同步采购单，参数为：erpOrderInfo{}", erpOrderInfo);
                addPurchaseOrder(erpOrderInfo);
                LOGGER.info("同步采购单完成");

                //添加采购商品信息
                LOGGER.info("开始同步采购单商品详情，erpOrderInfo{}", erpOrderInfo);
                addPurchaseOrderDetail(erpOrderInfo.getItemList());
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
     *
     * @param erpOrderInfo
     */
    private void updateOrderSuccess(ErpOrderInfo erpOrderInfo) {
        erpOrderInfoDao.updateOrderSuccess(erpOrderInfo.getOrderStoreId());
    }

    /**
     * 生成栖耘销售单
     *
     * @param erpOrderInfo
     */
    private void createSaleOrder(ErpOrderInfo erpOrderInfo) {
        String url = purchaseHost + "/order/aiqin/sale";
        HttpClient httpGet = HttpClient.post(url.toString()).json(erpOrderInfo).timeout(10000);
        httpGet.action();
    }

    /**
     * 同步采购单
     *
     * @param erpOrderInfo
     */
    private void addPurchaseOrder(ErpOrderInfo erpOrderInfo) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseOrderId(IdUtil.purchaseId());//业务id
        purchaseOrder.setPurchaseOrderCode(erpOrderInfo.getOrderStoreCode());//采购单号
        purchaseOrder.setCompanyCode(erpOrderInfo.getCompanyCode());//公司编码
        purchaseOrder.setCompanyName(erpOrderInfo.getCompanyName());//公司名称
        purchaseOrder.setSupplierCode(erpOrderInfo.getSupplierCode());//供应商编码
        purchaseOrder.setSupplierName(erpOrderInfo.getSupplierName());//供应商名称
        purchaseOrder.setTransportCenterCode(erpOrderInfo.getTransportCenterCode());//仓库编码
        purchaseOrder.setTransportCenterName(erpOrderInfo.getTransportCenterName());//仓库名称
        purchaseOrder.setWarehouseCode(erpOrderInfo.getWarehouseCode());//库房编码
        purchaseOrder.setWarehouseName(erpOrderInfo.getWarehouseName());//库房名称
        //purchaseOrder.setPurchaseGroupCode();//采购组编码
        //purchaseOrder.setPurchaseGroupName();//采购组名称
        purchaseOrder.setSettlementMethodCode(erpOrderInfo.getPaymentCode());//结算方式编码
        purchaseOrder.setSettlementMethodName(erpOrderInfo.getPaymentName());//结算方式名称
        purchaseOrder.setPurchaseOrderStatus(erpOrderInfo.getOrderStatus());//采购单状态  0.待审核 1.审核中 2.审核通过  3.备货确认 4.发货确认  5.入库开始 6.入库中 7.已入库  8.完成 9.取消 10.审核不通过
        purchaseOrder.setPurchaseMode(Integer.valueOf(erpOrderInfo.getDistributionModeCode()));//采购方式 0. 铺采直送  1.配送
        purchaseOrder.setPurchaseType(Integer.valueOf(erpOrderInfo.getOrderTypeCode()));//采购单类型   0手动，1.自动
        purchaseOrder.setTotalCount(Long.valueOf(erpOrderInfo.getItemList().size()));//最小单位数量  商品明细总数量
        purchaseOrder.setTotalTaxAmount(erpOrderInfo.getTotalProductAmount());//商品含税总金额
        purchaseOrder.setActualTotalCount(erpOrderInfo.getActualProductCount());//实际最小单数数量
        purchaseOrder.setActualTotalTaxAmount(erpOrderInfo.getActualTotalProductAmount());//实际商品含税总金额
        //purchaseOrder.setCancelReason(null);//取消原因
        //purchaseOrder.setCancelRemark(null);//取消备注
        purchaseOrder.setUseStatus(erpOrderInfo.getUseStatus());//0. 启用   1.禁用
        //purchaseOrder.setChargePerson();//负责人
        //purchaseOrder.setAccountCode();//账户编码
        //purchaseOrder.setAccountName();//账户名称
        //purchaseOrder.setContractCode();//合同编码
        //purchaseOrder.setContractName();//合同名称
        purchaseOrder.setContactPerson(erpOrderInfo.getReceivePerson());//联系人
        purchaseOrder.setContactMobile(erpOrderInfo.getReceiveMobile());//联系人电话
        //purchaseOrder.setPreArrivalTime();//预计到货时间
        //purchaseOrder.setValidTime();//有效期
        purchaseOrder.setDeliveryAddress(erpOrderInfo.getReceiveAddress());//发货地址
        purchaseOrder.setDeliveryTime(erpOrderInfo.getDeliveryTime());//发货时间
        //purchaseOrder.setInStockTime();//入库时间
        //purchaseOrder.setInStockAddress();//入库地址
        //purchaseOrder.setPrePurchaseOrder();//预采购单号
        purchaseOrder.setPaymentCode(erpOrderInfo.getPaymentCode());//付款方式编码
        purchaseOrder.setPaymentName(erpOrderInfo.getPaymentName());//付款方式名称
        purchaseOrder.setPaymentTime(erpOrderInfo.getPaymentTime());//付款期
        purchaseOrder.setPreAmount(erpOrderInfo.getOrderAmount());//预付付款金额
        purchaseOrder.setRemark(erpOrderInfo.getRemake());//备注
        purchaseOrder.setSourceCode(erpOrderInfo.getSourceCode());//来源单号
        purchaseOrder.setSourceType(erpOrderInfo.getSourceType());//来源类型
        purchaseOrder.setCreateById(erpOrderInfo.getCreateById());//创建人编码
        purchaseOrder.setCreateByName(erpOrderInfo.getCreateByName());//创建人名称
        purchaseOrder.setUpdateById(erpOrderInfo.getUpdateById());//修改人编码
        purchaseOrder.setUpdateByName(erpOrderInfo.getUpdateByName());//修改人名称
        purchaseOrder.setCreateTime(erpOrderInfo.getCreateTime());//创建时间
        purchaseOrder.setUpdateTime(erpOrderInfo.getUpdateTime());//修改时间
        purchaseOrderDao.insertSelective(purchaseOrder);
    }

    /**
     * 同步采购商品信息
     *
     * @param itemList
     */
    private void addPurchaseOrderDetail(List<ErpOrderItem> itemList) {
        for (ErpOrderItem item : itemList) {
            PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
            purchaseOrderDetail.setPurchaseOrderDetailId(IdUtil.purchaseId());//业务id
            purchaseOrderDetail.setPurchaseOrderCode(item.getOrderStoreCode());//采购单号
            purchaseOrderDetail.setSpuCode(item.getSpuCode());//spu编码
            purchaseOrderDetail.setSpuName(item.getSpuName());//spu名称
            purchaseOrderDetail.setSkuCode(item.getSkuCode());//sku编号
            purchaseOrderDetail.setSkuName(item.getSkuName());//sku名称
            //purchaseOrderDetail.setBrandCode();//品牌编码
            //purchaseOrderDetail.setBrandName();//品牌名称
            //purchaseOrderDetail.setCategoryCode();//品类编码
            //purchaseOrderDetail.setCategoryName();//品类名称
            purchaseOrderDetail.setProductSpec(item.getProductSpec());//规格
            purchaseOrderDetail.setColorCode(item.getColorCode());//颜色编码
            purchaseOrderDetail.setColorName(item.getColorName());//颜色名称
            purchaseOrderDetail.setModelCode(item.getModelCode());//型号
            purchaseOrderDetail.setUnitCode(item.getUnitCode());//单位编码
            purchaseOrderDetail.setUnitName(item.getUnitName());//单位名称
            purchaseOrderDetail.setProductType(item.getProductType());//商品类型   0商品 1赠品 2实物返回
            //purchaseOrderDetail.setPurchaseWhole();//采购件数（整数）
            //purchaseOrderDetail.setPurchaseSingle();//采购件数（零数）
            //purchaseOrderDetail.setBaseProductSpec();
            //purchaseOrderDetail.setBoxGauge();//采购包装
            purchaseOrderDetail.setTaxRate(item.getTaxRate());//税率
            purchaseOrderDetail.setLineCode(item.getLineCode());//行号
            purchaseOrderDetail.setFactorySkuCode(item.getSkuCode());//厂商SKU编码
            purchaseOrderDetail.setTaxAmount(item.getPurchaseAmount());//商品含税单价
            purchaseOrderDetail.setTotalTaxAmount(item.getTotalAcivityAmount());//商品含税总价
            purchaseOrderDetail.setTotalCount(item.getProductCount());//最小单位数量
            purchaseOrderDetail.setActualTotalTaxAmount(item.getActualTotalProductAmount());//实际含税总价
            purchaseOrderDetail.setUseStatus(item.getUseStatus());//0. 启用   1.禁用
            purchaseOrderDetail.setCreateById(item.getCreateById());//创建人编码
            purchaseOrderDetail.setCreateByName(item.getCreateByName());//创建人名称
            purchaseOrderDetail.setUpdateById(item.getUpdateById());//修改人编码
            purchaseOrderDetail.setUpdateByName(item.getUpdateByName());//修改人名称
            purchaseOrderDetail.setCreateTime(item.getCreateTime());//创建时间
            purchaseOrderDetail.setUpdateTime(item.getUpdateTime());//修改时间

            purchaseOrderDetailDao.insertSelective(purchaseOrderDetail);
        }
    }

    /**
     * 定时扫描同步失败的销售单
     * 每半小时执行一次
     */
    @Scheduled(cron = "0 0/30 * * * ? ")
    public void TimedFailedPurchaseOrder() {
        //查询同步时失败的订单
        Integer orderSuccess = 0;
        List<ErpOrderInfo> erpOrderInfo = erpOrderInfoDao.selectByOrderSucess(orderSuccess);
        //同步采购单
        for (ErpOrderInfo orderInfo : erpOrderInfo) {
            addPurchaseOrder(orderInfo);
            ErpOrderItem orderItem = new ErpOrderItem();
            orderItem.setOrderStoreCode(orderInfo.getOrderStoreId());
            //根据采购单号查询采购单明细
            List<ErpOrderItem> items = erpOrderItemDao.select(orderItem);


        }
        //修改订单同步状态
        for (ErpOrderInfo orderInfo : erpOrderInfo) {
            erpOrderInfoDao.updateOrderSuccess(orderInfo.getOrderStoreId());
        }
    }

    /**
     * 根据订单生成爱亲采购单,添加操作日志
     *
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
        return operationLog;
    }
}