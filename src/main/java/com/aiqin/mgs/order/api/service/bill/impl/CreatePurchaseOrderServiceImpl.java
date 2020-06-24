package com.aiqin.mgs.order.api.service.bill.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.dao.PurchaseBatchDao;
import com.aiqin.mgs.order.api.dao.PurchaseOrderDao;
import com.aiqin.mgs.order.api.dao.PurchaseOrderDetailDao;
import com.aiqin.mgs.order.api.dao.order.ErpOrderInfoDao;
import com.aiqin.mgs.order.api.domain.PurchaseBatch;
import com.aiqin.mgs.order.api.domain.PurchaseOrderDetail;
import com.aiqin.mgs.order.api.domain.PurchaseOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderFee;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.service.bill.CreatePurchaseOrderService;
import com.aiqin.mgs.order.api.service.bill.OperationLogService;
import com.aiqin.mgs.order.api.service.order.ErpOrderFeeService;
import com.aiqin.mgs.order.api.util.RequestReturnUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 采购单 实现类
 */
@Service
public class CreatePurchaseOrderServiceImpl implements CreatePurchaseOrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePurchaseOrderServiceImpl.class);

    @Value("${purchase.host}")
    private String purchaseHost;
    @Resource
    PurchaseOrderDao purchaseOrderDao;
    @Resource
    PurchaseOrderDetailDao purchaseOrderDetailDao;
    @Resource
    PurchaseBatchDao purchaseBatchDao;
    @Resource
    ErpOrderInfoDao erpOrderInfoDao;
    @Resource
    OperationLogService operationLogService;
    @Resource
    private ErpOrderFeeService erpOrderFeeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrderAndDetail(List<ErpOrderInfo> erpOrderInfos) {
        LOGGER.info("根据ERP订单生成爱亲采购单&采购单明细&修改订单同步状态，参数为：erpOrderInfo{}", JsonUtil.toJson(erpOrderInfos));
        PurchaseOrderInfo purchaseOrder = null;
        for(ErpOrderInfo erpOrderInfo:erpOrderInfos){
            try {
                //根据订单好查询，是否否已经生成采购单
                purchaseOrder = purchaseOrderDao.selectByOrderStoreCode(erpOrderInfo.getOrderStoreCode());
            } catch (Exception e) {
                LOGGER.error("根据订单号查询采购单异常", e);
                throw new RuntimeException();
            }

            if (purchaseOrder != null) {
                //修改ERP订单同步状态
                erpOrderInfoDao.updateOrderSuccess(OrderSucessEnum.ORDER_SYNCHRO_SUCCESS.getCode(), erpOrderInfo.getOrderStoreCode());
                LOGGER.info("此订单号" + purchaseOrder.getPurchaseOrderCode() + "已经生成采购单，无需再次生成");
                return;
            }

            try {
                //根据ERP订单生成爱亲采购单
                erpOrderInfoTopurchaseOrder(erpOrderInfo);
                //根据ERP订单明细生成爱亲采购单明细
                itemListToPurchaseOrderDetail(erpOrderInfo.getItemList());
                //根据ERP订单明细生成爱亲采购单批次明细
                itemListToPurchaseBatch(erpOrderInfo.getItemList());

                //修改ERP订单同步状态
                erpOrderInfoDao.updateOrderSuccess(OrderSucessEnum.ORDER_SYNCHRO_SUCCESS.getCode(), erpOrderInfo.getOrderStoreCode());

                //根据ERP订单生成爱亲采购单,采购单明细,修改订单同步状态&根据爱亲采购单，生成耘链销售单，添加操作日志
                addOperationLog(erpOrderInfo);
            } catch (Exception e) {
                LOGGER.error("根据ERP订单生成爱亲采购单异常" + e);
                throw new RuntimeException();
            }
        }

        //根据爱亲采购单，生成耘链销售单开始
        createSaleOrder(erpOrderInfos);

        LOGGER.info("根据ERP订单生成爱亲采购单&采购单明细&生成耘链销售单&修改订单同步状态,成功");
    }


    /**
     * 订单转采购单
     *
     * @param erpOrderInfo
     * @return
     */
    private void erpOrderInfoTopurchaseOrder(ErpOrderInfo erpOrderInfo) {
        PurchaseOrderInfo purchaseOrder = new PurchaseOrderInfo();
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
        //purchaseOrder.setPurchaseMode(Integer.valueOf(erpOrderInfo.getDistributionModeCode()));//采购方式 0. 铺采直送  1.配送
        purchaseOrder.setPurchaseType(Integer.valueOf(erpOrderInfo.getOrderTypeCode()));//采购单类型   0手动，1.自动
        //purchaseOrder.setTotalCount(Long.valueOf(erpOrderInfo.getItemList().size()));//最小单位数量  商品明细总数量
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
        //purchaseOrder.setPaymentTime(erpOrderInfo.getPaymentTime());//付款期
        purchaseOrder.setPreAmount(erpOrderInfo.getOrderAmount());//预付付款金额
        purchaseOrder.setRemark(erpOrderInfo.getRemake());//备注
        purchaseOrder.setSourceCode(erpOrderInfo.getSourceCode());//来源单号
        purchaseOrder.setSourceType(erpOrderInfo.getSourceType());//来源类型
        purchaseOrder.setCreateById(erpOrderInfo.getCreateById());//创建人编码
        purchaseOrder.setCreateByName(erpOrderInfo.getCreateByName());//创建人名称
        purchaseOrder.setCreateTime(new Date());//创建时间
        try {
            purchaseOrderDao.insertSelective(purchaseOrder);
        } catch (Exception e) {
            LOGGER.error("根据ERP订单生成爱亲采购单异常", e);
            throw new RuntimeException();
        }
    }

    /**
     * 订单明细转采购单单明细
     *
     * @param itemList
     */
    private void itemListToPurchaseOrderDetail(List<ErpOrderItem> itemList) {
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
            purchaseOrderDetail.setCreateTime(new Date());//创建时间
            try {
                //根据ERP订单生成爱亲采购单明细
                purchaseOrderDetailDao.insertSelective(purchaseOrderDetail);
            } catch (Exception e) {
                LOGGER.error("根据ERP订单生成爱亲采购单明细异常", e);
                throw new RuntimeException();
            }
        }
    }

    /**
     * 订单明细转采购单单批次明细
     *
     * @param itemList
     */
    private void itemListToPurchaseBatch(List<ErpOrderItem> itemList) {
        for (ErpOrderItem item : itemList) {
            PurchaseBatch purchaseBatch = new PurchaseBatch();
            purchaseBatch.setPurchaseOderCode(item.getOrderStoreCode());//采购单号
            purchaseBatch.setBatchCode(item.getBatchCode());//批次号
            purchaseBatch.setBatchInfoCode(item.getBatchInfoCode());//批次编号
            purchaseBatch.setSkuCode(item.getSkuCode());//sku编号
            purchaseBatch.setSkuName(item.getSkuName());//sku名称
            purchaseBatch.setSupplierCode(item.getSupplierCode());//供应商编码
            purchaseBatch.setSupplierName(item.getSupplierName());//供应商名称
            purchaseBatch.setProductDate(item.getBatchDate());//生产日期
            purchaseBatch.setTotalCount(item.getProductCount());//最小单位数量
            purchaseBatch.setActualTotalCount(item.getActualProductCount());//实际最小单位数量
            purchaseBatch.setLineCode(item.getLineCode());//行号
            purchaseBatch.setCreateById(item.getCreateById());//创建人编码
            purchaseBatch.setCreateByName(item.getCreateByName());//创建人名称
            purchaseBatch.setCreateTime(new Date());//创建时间
            purchaseBatch.setUpdateById(item.getCreateById());//创建人编码
            purchaseBatch.setUpdateByName(item.getCreateByName());//创建人名称
            purchaseBatch.setUpdateTime(new Date());//创建时间

         //   purchaseBatch.setBatchRemark();//批次备注
         //   purchaseBatch.setBeOverdueDate();//过期日期
         //   purchaseBatch.setLineCode();//库位号
            try {
                //根据ERP订单生成爱亲采购单批次明细
                purchaseBatchDao.insertSelective(purchaseBatch);
            } catch (Exception e) {
                LOGGER.error("根据ERP订单生成爱亲采购单明细异常", e);
                throw new RuntimeException();
            }
        }
    }

    /**
     * 生成栖耘销售单.
     *
     * @param erpOrderInfo
     */
    private void createSaleOrder(List<ErpOrderInfo> erpOrderInfo) {
        LOGGER.info("根据爱亲采购单，生成耘链销售单开始，参数为：erpOrderInfo{}",  JsonUtil.toJson(erpOrderInfo));
        try {
            for(ErpOrderInfo orderInfo:erpOrderInfo){
                //查询主订单费用信息
                ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByOrderId(orderInfo.getOrderStoreId());

                //耘链销售单需求字段
                if(ErpOrderCategoryEnum.ORDER_TYPE_51.getCode().equals(orderInfo.getOrderCategoryCode())){
                    //批发业务
                    //业务形式
                    orderInfo.setBusinessForm(1);
                }else{
                    //门店业务
                    //业务形式
                    orderInfo.setBusinessForm(0);
                }

                //平台(0:爱亲(新系统) 1:DL)
                orderInfo.setPlatformType("0");
                //订单产品类型 0.B2B 1.B2C
                orderInfo.setOrderProductType("0");
                //合伙人编码
                orderInfo.setPartnerCode(orderInfo.getCopartnerAreaId());
                //合伙人名称
                orderInfo.setPartnerName(orderInfo.getCopartnerAreaName());
                //收货人手机号
                orderInfo.setConsigneePhone(orderInfo.getReceiveMobile());
                //A品券优惠金额
                orderInfo.setTopCouponMoney(orderFee.getTopCouponMoney());
                //服纺券优惠金额
                orderInfo.setSuitCouponMoney(orderFee.getSuitCouponMoney());
                //渠道订单金额
                orderInfo.setChannelOrderAmount(orderInfo.getTotalProductAmount());
                //商品渠道总金额
                orderInfo.setProductChannelTotalAmount(orderInfo.getTotalProductAmount());


                String url = purchaseHost + "/order/aiqin/sale";
                HttpClient httpGet = HttpClient.post(url).json(orderInfo).timeout(10000);
                LOGGER.info("根据爱亲采购单，生成耘链销售单开始url:" + url + " httpGet:" + httpGet);
                HttpResponse<Object> response = httpGet.action().result(new TypeReference<HttpResponse<Object>>() {
                });
                if (!RequestReturnUtil.validateHttpResponse(response)) {
                    throw new BusinessException(response.getMessage());
                }
            }
        } catch (Exception e) {
            LOGGER.error("根据爱亲采购单，生成耘链销售单失败returnOrderCode： " + erpOrderInfo);
            throw new RuntimeException();
        }
    }

    /**
     * 根据ERP订单生成爱亲采购单，添加操作日志.
     *
     * @param erpOrderInfo
     */
    private void addOperationLog(ErpOrderInfo erpOrderInfo) {
        try {
            String operationCode = erpOrderInfo.getOrderStoreCode();
            Integer sourceType = ErpLogSourceTypeEnum.PURCHASE.getCode();
            Integer operationType = ErpLogOperationTypeEnum.ADD.getCode();
            Integer useStatus = ErpLogStatusTypeEnum.USING.getCode();
            String operationContent = "根据ERP订单生成爱亲采购单,采购单明细,修改订单同步状态&根据爱亲采购单，生成耘链销售单";
            //添加操作日志
            operationLogService.insert(operationCode, operationType, sourceType, operationContent, null, useStatus, null);
        } catch (Exception e) {
            LOGGER.error("根据ERP订单生成爱亲采购单，添加操作日志失败");
            throw new RuntimeException();
        }
    }
}
