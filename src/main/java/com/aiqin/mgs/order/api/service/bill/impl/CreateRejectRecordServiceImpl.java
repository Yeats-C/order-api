package com.aiqin.mgs.order.api.service.bill.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpLogOperationTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpLogSourceTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpLogStatusTypeEnum;
import com.aiqin.mgs.order.api.component.enums.OrderSucessEnum;
import com.aiqin.mgs.order.api.dao.RejectRecordDao;
import com.aiqin.mgs.order.api.dao.RejectRecordDetailDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderDetailDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderDetailVO;
import com.aiqin.mgs.order.api.service.bill.CreateRejectRecordService;
import com.aiqin.mgs.order.api.service.bill.OperationLogService;
import com.aiqin.mgs.order.api.service.returnorder.ReturnOrderInfoService;
import com.aiqin.mgs.order.api.util.BeanCopyUtils;
import com.aiqin.mgs.order.api.util.RequestReturnUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 退供单 实现类
 */
@Service
public class CreateRejectRecordServiceImpl implements CreateRejectRecordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateRejectRecordServiceImpl.class);
    @Value("${purchase.host}")
    private String rejectHost;
    @Resource
    RejectRecordDao rejectRecordDao;
    @Resource
    ReturnOrderInfoDao returnOrderInfoDao;
    @Resource
    ReturnOrderDetailDao returnOrderDetailDao;
    @Resource
    RejectRecordDetailDao rejectRecordDetailDao;
    @Resource
    ReturnOrderInfoService returnOrderInfoService;
    @Resource
    OperationLogService operationLogService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRejectRecord(String returnOrderCode) {
        RejectRecord rejectRecord = null;
        ReturnOrderInfo returnOrderInfo = null;
        ReturnOrderDetailVO returnOrder = null;

        //退货单调用
        if (StringUtils.isNotBlank(returnOrderCode)) {
            try {
                //查询退供表里是否已经存在
                rejectRecord = rejectRecordDao.selectByRejectRecordCode(returnOrderCode);
                //查询ERP退货单，待生成爱亲退供单数据
                returnOrderInfo = returnOrderInfoDao.selectByOrderCodeAndSuccess(OrderSucessEnum.ORDER_SYNCHRO_WAIT.getCode(), returnOrderCode);
                //根据退货单号查询退货单和退货明细
                returnOrder = returnOrderInfoService.detail(returnOrderInfo.getReturnOrderCode());
                LOGGER.info("根据退货号查询退货单和退货明细-返回结果：{}",returnOrder);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (rejectRecord != null) {
                LOGGER.info("此订单号" + returnOrderCode + "已尽生成采购单");
                //修改退货单同步状态
                returnOrderInfoDao.updateOrderSuccess(OrderSucessEnum.ORDER_SYNCHRO_SUCCESS.getCode(), returnOrderCode);
                return;
            }

            if (returnOrderInfo == null || StringUtils.isBlank(returnOrderInfo.getOrderStoreCode())) {
                LOGGER.error("待生成采购单的ERP退货单为空");
                return;
            }
            instantRejectRecord(returnOrder);
        } else {

            //定时任务-》查询未同步的订单
            List<ReturnOrderInfo> returnOrderInfos = null;
            Integer orderSucess = OrderSucessEnum.ORDER_SYNCHRO_WAIT.getCode();
            try {
                //查询未同步失败的退货单
                returnOrderInfos = returnOrderInfoDao.selectByOrderSuccess(orderSucess);
            } catch (Exception e) {
                LOGGER.error("查询未同步失败的退货单失败", e);
                throw new RuntimeException();
            }

            if (returnOrderInfos != null && returnOrderInfos.size() > 0) {
                for (ReturnOrderInfo orderInfo : returnOrderInfos) {
                    RejectRecord rejectRecordTim = rejectRecordDao.selectByRejectRecordCode(orderInfo.getReturnOrderCode());
                    if (rejectRecordTim == null) {
                        //根据退货单号查询退货单和退货明细
                        returnOrder = returnOrderInfoService.detail(orderInfo.getReturnOrderCode());
                        instantRejectRecord(returnOrder);
                    } else {
                        //修改退货单同步状态
                        returnOrderInfoDao.updateOrderSuccess(OrderSucessEnum.ORDER_SYNCHRO_SUCCESS.getCode(), rejectRecordTim.getRejectRecordCode());
                        LOGGER.info("此订单号" + returnOrderCode + "已尽生成采购单");
                        continue;
                    }
                }
            }
            LOGGER.info("没有待生成采购单的ERP退货单");
            return;
        }
    }

    private void instantRejectRecord(ReturnOrderDetailVO returnOrder) {
        if (returnOrder != null && returnOrder.getReturnOrderInfo() != null && returnOrder.getDetails() != null && returnOrder.getDetails().size() > 0) {
            //查询待ERP退货单，待生成爱亲退供单数据
            returnOrderToRejectRecord(returnOrder.getReturnOrderInfo());
            //根据退货单号查询退货单和退货明细
            returnOrderToRejectRecordDetail(returnOrder);
            //根据爱亲退供单，生成耘链退货单
            createSaleOrder(returnOrder.getReturnOrderInfo().getReturnOrderCode());
            try {
                //修改退货单同步状态
                returnOrderInfoDao.updateOrderSuccess(OrderSucessEnum.ORDER_SYNCHRO_SUCCESS.getCode(), returnOrder.getReturnOrderInfo().getReturnOrderCode());
            } catch (Exception e) {
                LOGGER.error("修改退货单同步状态失败", e);
                throw new RuntimeException();
            }
            //添加操作日志
            addOperationLog(returnOrder.getReturnOrderInfo().getOrderStoreCode());
        } else {
            LOGGER.error("待生成采购单的ERP退货单或退货单明细为空");
            throw new IllegalArgumentException();
        }
    }

    /**
     * 查询待ERP退货单，待生成爱亲退供单数据
     *
     * @param returnOrderInfo
     */
    private void returnOrderToRejectRecord(ReturnOrderInfo returnOrderInfo) {
        try {
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
            rejectRecord.setValidTime(returnOrderInfo.getReviewTime());//有效期
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
            rejectRecord.setCreateTime(new Date());//创建时间
            //根据ERP退货单生成爱亲退供单
            rejectRecordDao.insertSelective(rejectRecord);
        } catch (Exception e) {
            LOGGER.error("根据ERP退货单生成爱亲退供单失败", e);
            throw new RuntimeException();
        }
    }

    /**
     * 根据退货单号查询退货单和退货明细
     *
     * @param returnOrder
     */
    private void returnOrderToRejectRecordDetail(ReturnOrderDetailVO returnOrder) {
        for (ReturnOrderDetail returnOrderDetail : returnOrder.getDetails()) {
            RejectRecordDetail rejectRecordDetail = new RejectRecordDetail();
            rejectRecordDetail.setRejectRecordDetailId(IdUtil.purchaseId());//业务id
            rejectRecordDetail.setRejectRecordCode(returnOrderDetail.getReturnOrderCode());//退供单号
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
            rejectRecordDetail.setUseStatus(returnOrderDetail.getUseStatus());//0. 启用   1.禁用
            rejectRecordDetail.setCreateById(returnOrderDetail.getCreateById());//创建人编码
            rejectRecordDetail.setCreateByName(returnOrderDetail.getCreateByName());//创建人名称
            rejectRecordDetail.setCreateTime(new Date());//创建时间
            try {
                //根据ERP退货单生成爱亲退供单详情
                rejectRecordDetailDao.insertSelective(rejectRecordDetail);
            } catch (Exception e) {
                LOGGER.error("根据ERP退货单生成爱亲退供单详情失败", e);
                throw new RuntimeException();
            }
        }
    }

    /**
     * 根据爱亲退供单，生成耘链退货单
     *
     * @param returnOrderCode
     */
    private void createSaleOrder(String returnOrderCode) {
        LOGGER.info("根据爱亲退供单，生成耘链退货单开始，returnOrderCode{}", returnOrderCode);
        try {
            //查询退供单
            ReturnOrderInfo returnOrderInfo = returnOrderInfoDao.selectByReturnOrderCode(returnOrderCode);
            //查询退供单明细
            List<ReturnOrderDetail> returnOrderDetails = returnOrderDetailDao.selectListByReturnOrderCode(returnOrderCode);
            if (returnOrderInfo != null && returnOrderDetails != null && returnOrderDetails.size() > 0) {
                ReturnOrderInfo orderInfo = new ReturnOrderInfo();
                BeanUtils.copyProperties(returnOrderInfo, orderInfo);
                List<ReturnOrderDetail> orderDetails = BeanCopyUtils.copyList(returnOrderDetails, ReturnOrderDetail.class);
                ReturnOrderReq returnOrderReq = new ReturnOrderReq();
                returnOrderReq.setReturnOrderDetailReqList(orderDetails);
                returnOrderReq.setReturnOrderInfo(orderInfo);
                String url = rejectHost + "/returnGoods/record/return";
                //String url = "http://192.168.11.119:80/returnGoods/record/return";
                LOGGER.info("根据爱亲退供单，生成耘链退货亲求地址url:"+ url +"，请求参数returnOrderReq:"+returnOrderReq);
                LOGGER.info("熙耘地址请求：{}",JSON.toJSONString(returnOrderReq));
                HttpClient httpGet = HttpClient.post(url).json(returnOrderReq);
                HttpResponse<Object> response = httpGet.action().result(new TypeReference<HttpResponse<Object>>() {
                });
                LOGGER.info("同步耘链退货-返回结果:{}", JSON.toJSON(response));
                if (!RequestReturnUtil.validateHttpResponse(response)) {
                    throw new BusinessException(response.getMessage());
                }
            }
        } catch (Exception e) {
            LOGGER.error("根据爱亲退供单，生成耘链退货单失败",e);
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * 根据爱亲退供单，生成耘链退货单，添加操作日志
     *
     * @param returnOrderCode
     */
    private void addOperationLog(String returnOrderCode) {
        try {
            Integer sourceType = ErpLogSourceTypeEnum.RETURN_SUPPLY.getCode();
            Integer operationType = ErpLogOperationTypeEnum.ADD.getCode();
            Integer useStatus = ErpLogStatusTypeEnum.USING.getCode();
            String operationContent = "根据爱亲退供单，生成耘链退货单";
            operationLogService.insert(returnOrderCode, operationType, sourceType, operationContent, null, useStatus, null);
        } catch (Exception e) {
            LOGGER.error("添加根据爱亲退供单，生成耘链退货单，操作日志失败", e);
        }
    }
}
