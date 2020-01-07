package com.aiqin.mgs.order.api.service.bill.impl;

import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.mgs.order.api.component.enums.OrderSucessEnum;
import com.aiqin.mgs.order.api.dao.PurchaseOrderDao;
import com.aiqin.mgs.order.api.dao.PurchaseOrderDetailDao;
import com.aiqin.mgs.order.api.dao.order.ErpOrderInfoDao;
import com.aiqin.mgs.order.api.domain.PurchaseOrder;
import com.aiqin.mgs.order.api.domain.PurchaseOrderDetail;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.service.bill.CreatePurchaseOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 采购单 实现类
 */
@Service
public class CreatePurchaseOrderServiceImpl implements CreatePurchaseOrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePurchaseOrderServiceImpl.class);
    @Resource
    PurchaseOrderDao purchaseOrderDao;
    @Resource
    PurchaseOrderDetailDao purchaseOrderDetailDao;
    @Resource
    ErpOrderInfoDao erpOrderInfoDao;

    @Override
    @Transactional
    public void addOrderAndDetail(ErpOrderInfo erpOrderInfo) {
        try {
            if (erpOrderInfo != null) {
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
                //purchaseOrder.setPurchaseMode(Integer.valueOf(erpOrderInfo.getDistributionModeCode()));//采购方式 0. 铺采直送  1.配送
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
                //purchaseOrder.setPaymentTime(erpOrderInfo.getPaymentTime());//付款期
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
                //根据ERP订单生成爱亲采购单
                purchaseOrderDao.insertSelective(purchaseOrder);

                for (ErpOrderItem item : erpOrderInfo.getItemList()) {
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
                    //根据ERP订单生成爱亲采购单明细
                    purchaseOrderDetailDao.insertSelective(purchaseOrderDetail);
                }
                //修改订单同步状态
                erpOrderInfoDao.updateOrderSuccess(OrderSucessEnum.ORDER_SYNCHRO_SUCCESS.getCode(), erpOrderInfo.getOrderStoreCode());
            } else {
                LOGGER.error("订单为空 erpOrderInfo {}"+erpOrderInfo);
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            LOGGER.error("根据ERP订单生成爱亲采购单异常" + e);
            throw new RuntimeException();
        }
    }
}
