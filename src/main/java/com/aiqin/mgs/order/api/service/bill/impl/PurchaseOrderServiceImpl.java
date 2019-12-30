package com.aiqin.mgs.order.api.service.bill.impl;
import java.math.BigDecimal;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.OperationLogDao;
import com.aiqin.mgs.order.api.dao.PurchaseOrderDao;
import com.aiqin.mgs.order.api.dao.PurchaseOrderDetailBatchDao;
import com.aiqin.mgs.order.api.dao.PurchaseOrderDetailDao;
import com.aiqin.mgs.order.api.dao.order.ErpOrderInfoDao;
import com.aiqin.mgs.order.api.dao.order.ErpOrderItemDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
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
 * 销售 实现类
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
    public HttpResponse<PurchaseInfo> selectPurchaseInfo() {
        ErpOrderInfo erpOrderInfo = new ErpOrderInfo();
        //订单查询
        erpOrderInfoDao.select(erpOrderInfo);
        ErpOrderItem erpOrderItem = new ErpOrderItem();
        //订单明细查询
        erpOrderItemDao.select(erpOrderItem);
        //订单批次明细查询（仓卡）

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
            @Transactional
            public void run() {
                //添加采购单
                LOGGER.info("开始同步采购单，参数为：erpOrderInfo{}", erpOrderInfo);
                addPurchaseOrder(erpOrderInfo);
                LOGGER.info("同步采购单完成");
                //添加操作日志
                LOGGER.info("添加操作日志开始");

                //添加采购商品信息
                LOGGER.info("开始同步采购单商品详情，erpOrderInfo{}", erpOrderInfo);
                addPurchaseOrderDetail(erpOrderInfo);
                LOGGER.info("同步采购单商品详结束");
                //添加操作日志
                LOGGER.info("添加操作日志开始");

                //根据爱亲采购单，生成耘链销售单
                LOGGER.info("开始根据爱亲采购单，生成耘链销售单，参数为：erpOrderInfo{}", erpOrderInfo);
                createSaleOrder(erpOrderInfo);
                LOGGER.info("根据爱亲采购单，生成耘链销售单结束");
                //添加操作日志
                LOGGER.info("添加操作日志开始");
            }
        });
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
     * 添加操作日志
     *
     * @param erpOrderInfo
     */
    private void addOperationLog(ErpOrderInfo erpOrderInfo) {
        AuthToken auth = AuthUtil.getCurrentAuth();
        OperationLog operationLog = new OperationLog();
        operationLog.setOperationCode(erpOrderInfo.getOrderStoreCode());//来源编码
        operationLog.setOperationType(null);//日志类型 0 .新增 1.修改 2.删除 3.下载
        operationLog.setSourceType(1);//来源类型 0.销售 1.采购 2.退货  3.退供
        //operationLog.setOperationContent();//日志内容
        //operationLog.setRemark();//备注
        //operationLog.setUseStatus();//0. 启用 1.禁用

        operationLog.setCreateById(auth.getPersonId());
        operationLog.setCreateByName(auth.getPersonName());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        operationLog.setCreateTime(formatter.format(new Date()));
        operationLogDao.insertSelective(operationLog);
    }

    /**
     * 销售定时任务扫描失败订单
     * 每半小时执行一次
     */
    @Scheduled(cron = "0 0/30 * * * ? ")
    public void reportCurrentTime() {
        PurchaseOrderDetailBatch purchaseOrderDetailBatch = new PurchaseOrderDetailBatch();
        purchaseOrderDetailBatchDao.updateByPrimaryKeySelective(purchaseOrderDetailBatch);
        //调用销售单 TODO
    }
}