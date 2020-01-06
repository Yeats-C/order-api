package com.aiqin.mgs.order.api.service.bill.impl;

import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.mgs.order.api.component.enums.OrderSucessEnum;
import com.aiqin.mgs.order.api.dao.RejectRecordDao;
import com.aiqin.mgs.order.api.dao.RejectRecordDetailDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderDetailDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.RejectRecord;
import com.aiqin.mgs.order.api.domain.RejectRecordDetail;
import com.aiqin.mgs.order.api.domain.ReturnOrderDetail;
import com.aiqin.mgs.order.api.domain.ReturnOrderInfo;
import com.aiqin.mgs.order.api.service.bill.CreateRejectRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
@Service
public class CreateRejectRecordServiceImpl implements CreateRejectRecordService {
    @Resource
    RejectRecordDao rejectRecordDao;
    @Resource
    ReturnOrderInfoDao returnOrderInfoDao;
    @Resource
    ReturnOrderDetailDao returnOrderDetailDao;
    @Resource
    RejectRecordDetailDao rejectRecordDetailDao;

    @Override
    @Transactional
    public void addRejectRecord(String returnOrderCode) {
        //根据订单号查询为同步的订单
        Integer orderSynchroSuccess = OrderSucessEnum.ORDER_SYNCHRO_SUCCESS.getCode();
        ReturnOrderInfo returnOrderInfo = returnOrderInfoDao.selectByOrderCodeAndSuccess(orderSynchroSuccess,returnOrderCode);
        if (returnOrderInfo.getOrderSuccess() == OrderSucessEnum.ORDER_SYNCHRO_WAIT.getCode()) {
            RejectRecord rejectRecord = new RejectRecord();
            rejectRecord.setRejectRecordId(IdUtil.purchaseId());//业务id
            rejectRecord.setRejectRecordCode(returnOrderInfo.getReturnOrderCode());//退供单号
            rejectRecord.setCompanyCode(returnOrderInfo.getCompanyCode());//公司编码
            rejectRecord.setCompanyName(returnOrderInfo.getCompanyName());//公司名称
            rejectRecord.setSupplierCode(returnOrderInfo.getSupplierCode());//供应商编码
            rejectRecord.setSupplierName(returnOrderInfo.getSupplierName());//供应商名称
            rejectRecord.setTransportCenterCode(returnOrderInfo.getTransportCenterCode());//仓库编码
            rejectRecord.setTransportCenterName(returnOrderInfo.getTransportCenterName());//仓库名称
            rejectRecord.setWarehouseCode(returnOrderInfo.getWarehouseCode());//库房编码
            rejectRecord.setWarehouseName(returnOrderInfo.getWarehouseName());//库房名称
            rejectRecord.setPurchaseGroupCode(returnOrderInfo.getPurchaseGroupCode());//采购组编码
            rejectRecord.setPurchaseGroupName(returnOrderInfo.getPurchaseGroupName());//采购组名称
            rejectRecord.setSettlementMethodCode(returnOrderInfo.getPaymentCode());//结算方式编码
            rejectRecord.setSettlementMethodName(returnOrderInfo.getPaymentName());//结算方式名称
            rejectRecord.setRejectRecordStatus(returnOrderInfo.getReturnOrderStatus());//退供单状态
            rejectRecord.setTotalCount(returnOrderInfo.getProductCount());//最小单位数量
            rejectRecord.setTotalTaxAmount(returnOrderInfo.getReturnOrderAmount());//商品含税金额
            rejectRecord.setActualTotalCount(returnOrderInfo.getProductCount());//实际最小单数数量
            rejectRecord.setActualTotalTaxAmount(returnOrderInfo.getActualReturnOrderAmount());//实际商品含税金额
            //rejectRecord.setChargePerson();//负责人
            rejectRecord.setContactPerson(returnOrderInfo.getReceivePerson());//联系人
            rejectRecord.setContactMobile(returnOrderInfo.getReceiveMobile());//联系人电话
            rejectRecord.setProvinceId(returnOrderInfo.getProvinceId());//收货区域 :省编码
            rejectRecord.setProvinceName(returnOrderInfo.getProvinceName());//收货区域 :省
            rejectRecord.setCityId(returnOrderInfo.getCityId());//收货区域 :市编码
            rejectRecord.setCityName(returnOrderInfo.getCityName());//收货区域 :市
            rejectRecord.setDistrictId(returnOrderInfo.getDistrictId());//收货区域 :区/县
            rejectRecord.setDistrictName(returnOrderInfo.getDistrictName());//收货区域 :区/县
            rejectRecord.setReceiveAddress(returnOrderInfo.getReceiveAddress());//收货地址
            rejectRecord.setPreExpectTime(returnOrderInfo.getPreExpectTime());//预计发货时间
            rejectRecord.setValidTime(returnOrderInfo.getValidTime());//有效期
            rejectRecord.setOutStockTime(returnOrderInfo.getOutStockTime());//出库时间
            rejectRecord.setDeliveryTime(returnOrderInfo.getDeliveryTime());//发运时间
            rejectRecord.setFinishTime(returnOrderInfo.getFinishTime());//完成时间
            rejectRecord.setRemark(returnOrderInfo.getRemark());//备注
            rejectRecord.setUseStatus(returnOrderInfo.getUseStatus());//0. 启用   1.禁用
            rejectRecord.setSourceCode(returnOrderInfo.getReturnOrderCode());//来源单号
            rejectRecord.setSourceType(returnOrderInfo.getSourceType());//来源类型
            rejectRecord.setCreateById(returnOrderInfo.getCreateById());//创建人编码
            rejectRecord.setCreateByName(returnOrderInfo.getCreateByName());//创建人名称
            rejectRecord.setUpdateById(returnOrderInfo.getUpdateById());//修改人编码
            rejectRecord.setUpdateByName(returnOrderInfo.getUpdateByName());//修改人名称
            rejectRecord.setCreateTime(returnOrderInfo.getCreateTime());//创建时间
            rejectRecord.setUpdateTime(returnOrderInfo.getUpdateTime());//修改时间
            //生成退供单
            rejectRecordDao.insert(rejectRecord);

            List<ReturnOrderDetail> returnOrderDetails = returnOrderDetailDao.selectListByReturnOrderCode(returnOrderCode);
            for (ReturnOrderDetail returnOrderDetail : returnOrderDetails) {
                RejectRecordDetail rejectRecordDetail = new RejectRecordDetail();
                rejectRecordDetail.setRejectRecordDetailId(IdUtil.purchaseId());//业务id
                rejectRecordDetail.setRejectRecordCode(returnOrderInfo.getReturnOrderCode());//退供单号
                rejectRecordDetail.setSkuCode(returnOrderDetail.getSkuCode());//sku编号
                rejectRecordDetail.setSkuName(returnOrderDetail.getSkuName());//sku名称
                //rejectRecordDetail.setBrandCode();//品牌编码
                //rejectRecordDetail.setBrandName();//品牌名称
                //rejectRecordDetail.setCategoryCode();//品类编码
                //rejectRecordDetail.setCategoryName();//品类名称
                rejectRecordDetail.setProductSpec(returnOrderDetail.getProductSpec());//规格
                rejectRecordDetail.setColorCode(returnOrderDetail.getColorCode());//颜色编码
                rejectRecordDetail.setColorName(returnOrderDetail.getColorName());//颜色名称
                rejectRecordDetail.setModelCode(returnOrderDetail.getModelCode());//型号
                rejectRecordDetail.setProductType(returnOrderDetail.getProductType());//商品类型   0商品 1赠品 2实物返回
                rejectRecordDetail.setProductCount(returnOrderDetail.getReturnProductCount());//商品数量
                rejectRecordDetail.setUnitCode(returnOrderDetail.getUnitCode());//单位编码
                rejectRecordDetail.setUnitName(returnOrderDetail.getUnitName());//单位名称
                rejectRecordDetail.setTaxRate(returnOrderDetail.getTaxRate());//税率
                rejectRecordDetail.setLineCode(returnOrderDetail.getLineCode());//行号
                rejectRecordDetail.setFactorySkuCode(returnOrderDetail.getSkuCode());//厂商SKU编码
                rejectRecordDetail.setTaxAmount(returnOrderDetail.getProductAmount());//商品含税单价
                rejectRecordDetail.setTotalTaxAmount(returnOrderDetail.getTotalProductAmount());//商品含税总价
                //rejectRecordDetail.setTotalCount();//最小单位数量
                rejectRecordDetail.setActualTotalCount(returnOrderDetail.getActualReturnProductCount());//实际最小单数数量
                //rejectRecordDetail.setActualTotalTaxAmount();//实际含税总价
                rejectRecordDetail.setUseStatus(returnOrderInfo.getUseStatus());//0. 启用   1.禁用
                rejectRecordDetail.setCreateById(returnOrderInfo.getCreateById());//创建人编码
                rejectRecordDetail.setCreateByName(returnOrderInfo.getCreateByName());//创建人名称
                rejectRecordDetail.setUpdateById(returnOrderInfo.getUpdateById());//修改人编码
                rejectRecordDetail.setUpdateByName(returnOrderInfo.getUpdateByName());//修改人名称
                rejectRecordDetail.setCreateTime(returnOrderInfo.getCreateTime());//创建时间
                rejectRecordDetail.setUpdateTime(returnOrderInfo.getUpdateTime());//修改时间
                //生成退供单详情
                rejectRecordDetailDao.insertSelective(rejectRecordDetail);
            }
            //修改退货单同步状态
            returnOrderInfoDao.updateOrderSuccess(orderSynchroSuccess, returnOrderInfo.getReturnOrderCode());
        }
    }
}
