package com.aiqin.mgs.order.api.service.bill.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.component.enums.ErpLogOperationTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpLogSourceTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpLogStatusTypeEnum;
import com.aiqin.mgs.order.api.dao.*;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderDetailDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.request.bill.RejectRecordReq;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderSearchVo;
import com.aiqin.mgs.order.api.service.bill.RejectRecordService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 根据退货单，生成爱亲采购单 实现类
 */
@Service
public class RejectRecordServiceImpl implements RejectRecordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RejectRecordServiceImpl.class);
    @Autowired
    RejectRecordDao rejectRecordDao;
    @Autowired
    RejectRecordDetailDao rejectRecordDetailDao;
    @Autowired
    RejectRecordDetailBatchDao rejectRecordDetailBatchDao;
    @Autowired
    OperationLogDao operationLogDao;
    @Autowired
    private ReturnOrderInfoDao returnOrderInfoDao;
    @Autowired
    private ReturnOrderDetailDao returnOrderDetailDao;
    @Autowired
    ReturnOrderDetailBatchDao returnOrderDetailBatchDao;

    @Override
    @Transactional
    public HttpResponse createRejectRecord(String returnOrderCode) {
        LOGGER.info("同步推供单，rejectRecordReq{}", returnOrderCode);
        //异步执行
        rejectRecordExecutor(returnOrderCode);
        if (StringUtils.isEmpty(returnOrderCode)) {
            return HttpResponse.success();
        }
        return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
    }

    @Override
    public HttpResponse<List<RejectRecordInfo>> selectPurchaseInfo() {
        ReturnOrderSearchVo returnOrderSearch = new ReturnOrderSearchVo();
        returnOrderInfoDao.page(returnOrderSearch);

        String returnOrderCode = "";
        returnOrderDetailDao.selectListByReturnOrderCode(returnOrderCode);

        returnOrderDetailBatchDao.select(new ReturnOrderDetailBatch());
        return null;
    }

    /**
     * 异步执行
     *
     * @param returnOrderCode
     */
    private void rejectRecordExecutor(String returnOrderCode) {
        //异步执行
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                //添加退货单
                LOGGER.info("开始同步退供单，参数为：returnOrderCode{}", returnOrderCode);
                addRejectRecord(returnOrderCode);
                LOGGER.info("同步退供单结束");

                //添加退货单详情信息
                LOGGER.info("开始添加退供单详情信息，参数为：returnOrderCode{}", returnOrderCode);
                addRejectRecordDetail(returnOrderCode);
                LOGGER.info("添加退供单信息详情结束");

                //根据爱亲退供单，生成耘链退货单
                LOGGER.info("开始根据爱亲退供单，生成耘链退货单，参数为：returnOrderCode{}", returnOrderCode);
                createSaleOrder(returnOrderCode);
                LOGGER.info("根据爱亲退供单，生成耘链退货单结束");
                ////添加操作日志
                LOGGER.info("添加根据爱亲退供单，生成耘链退货单，操作日志开始");
                //createSaleOrderLog(returnOrderCode);
                LOGGER.info("根据爱亲退供单，生成耘链退货单，添加操作日志结束");
            }
        });
    }

    /**
     * 根据爱亲退供单，生成耘链退货单
     *
     * @param returnOrderCode
     */
    private void createSaleOrder(String returnOrderCode) {
        String url = " ";
        //HttpClient httpGet = HttpClient.post(url.toString()).json().timeout(10000);
        //httpGet.action();
    }

    /**
     * 同步退供单,操作日志
     *
     * @param returnOrderCode
     */
    private void addRejectRecordLog(String returnOrderCode) {
        OperationLog operationLog = addOperationLog();
        //operationLog.setOperationCode(erpOrderInfo.getOrderStoreCode());//来源编码
        operationLog.setOperationContent("同步退供单");//日志内容
        operationLog.setRemark("");//备注

        operationLogDao.insertSelective(operationLog);
    }

    /**
     * 同步退供单详情信息,操作日志
     *
     * @param returnOrderCode
     */
    private void addRejectRecordDetailLog(String returnOrderCode) {
        OperationLog operationLog = addOperationLog();
        //operationLog.setOperationCode(erpOrderInfo.getOrderStoreCode());//来源编码
        operationLog.setOperationContent("同步退供单详情信息");//日志内容
        operationLog.setRemark("");//备注

        operationLogDao.insertSelective(operationLog);
    }

    /**
     * 根据爱亲退供单，生成耘链退货单，添加操作日志
     *
     * @param returnOrderCode
     */
    private void createSaleOrderLog(String returnOrderCode) {
        OperationLog operationLog = addOperationLog();
        //operationLog.setOperationCode(erpOrderInfo.getOrderStoreCode());//来源编码
        operationLog.setOperationContent("根据爱亲退供单，生成耘链退货单");//日志内容
        operationLog.setRemark("");//备注

        operationLogDao.insertSelective(operationLog);
    }

    /**
     * 添加退货单
     */
    private void addRejectRecord(String returnOrderCode) {
        //根据订单号查询为同步的订单
        ReturnOrderInfo returnOrderInfo = returnOrderInfoDao.selectByReturnOrderCode(returnOrderCode);
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

        rejectRecordDao.insert(rejectRecord);
    }

    /**
     * 添加退货单信息
     */
    private void addRejectRecordDetail(String returnOrderCode) {
        //根据订单号查询为同步的订单
        List<ReturnOrderDetail> returnOrderInfos = returnOrderDetailDao.selectListByReturnOrderCode(returnOrderCode);
        List<RejectRecordDetail> listRejectRecordDetail = Lists.newArrayList();
        for (ReturnOrderDetail returnOrderInfo : returnOrderInfos) {
            RejectRecordDetail rejectRecordDetail = new RejectRecordDetail();
            rejectRecordDetail.setRejectRecordDetailId(IdUtil.purchaseId());//业务id
            rejectRecordDetail.setRejectRecordCode(returnOrderInfo.getReturnOrderCode());//退供单号
            rejectRecordDetail.setSkuCode(returnOrderInfo.getSkuCode());//sku编号
            rejectRecordDetail.setSkuName(returnOrderInfo.getSkuName());//sku名称
            //rejectRecordDetail.setBrandCode();//品牌编码
            //rejectRecordDetail.setBrandName();//品牌名称
            //rejectRecordDetail.setCategoryCode();//品类编码
            //rejectRecordDetail.setCategoryName();//品类名称
            rejectRecordDetail.setProductSpec(returnOrderInfo.getProductSpec());//规格
            rejectRecordDetail.setColorCode(returnOrderInfo.getColorCode());//颜色编码
            rejectRecordDetail.setColorName(returnOrderInfo.getColorName());//颜色名称
            rejectRecordDetail.setModelCode(returnOrderInfo.getModelCode());//型号
            rejectRecordDetail.setProductType(returnOrderInfo.getProductType());//商品类型   0商品 1赠品 2实物返回
            rejectRecordDetail.setProductCount(returnOrderInfo.getReturnProductCount());//商品数量
            rejectRecordDetail.setUnitCode(returnOrderInfo.getUnitCode());//单位编码
            rejectRecordDetail.setUnitName(returnOrderInfo.getUnitName());//单位名称
            rejectRecordDetail.setTaxRate(returnOrderInfo.getTaxRate());//税率
            rejectRecordDetail.setLineCode(returnOrderInfo.getLineCode());//行号
            rejectRecordDetail.setFactorySkuCode(returnOrderInfo.getSkuCode());//厂商SKU编码
            rejectRecordDetail.setTaxAmount(returnOrderInfo.getProductAmount());//商品含税单价
            rejectRecordDetail.setTotalTaxAmount(returnOrderInfo.getTotalProductAmount());//商品含税总价
            //rejectRecordDetail.setTotalCount();//最小单位数量
            rejectRecordDetail.setActualTotalCount(returnOrderInfo.getActualReturnProductCount());//实际最小单数数量
            //rejectRecordDetail.setActualTotalTaxAmount();//实际含税总价
            rejectRecordDetail.setUseStatus(returnOrderInfo.getUseStatus());//0. 启用   1.禁用
            rejectRecordDetail.setCreateById(returnOrderInfo.getCreateById());//创建人编码
            rejectRecordDetail.setCreateByName(returnOrderInfo.getCreateByName());//创建人名称
            rejectRecordDetail.setUpdateById(returnOrderInfo.getUpdateById());//修改人编码
            rejectRecordDetail.setUpdateByName(returnOrderInfo.getUpdateByName());//修改人名称
            rejectRecordDetail.setCreateTime(returnOrderInfo.getCreateTime());//创建时间
            rejectRecordDetail.setUpdateTime(returnOrderInfo.getUpdateTime());//修改时间
            rejectRecordDetailDao.insertSelective(rejectRecordDetail);
        }
    }

    /**
     * 定时任务
     * 每半小时执行一次
     *
     * @Scheduled(cron = "0 0/30 * * * ? ")
     */
    @Scheduled(fixedRate = 1000 * 2)
    public void reportCurrentTime() {
        RejectRecordDetailBatch rejectRecordDetailBatch = new RejectRecordDetailBatch();
        rejectRecordDetailBatchDao.updateByPrimaryKeySelective(rejectRecordDetailBatch);
        //调用退供单 TODO
    }

    /**
     * 定时扫描同步失败的拖货单
     * 每半小时执行一次
     */
    @Scheduled(cron = "0 0/30 * * * ? ")
    public void TimedFailedRejectOrder() {
        RejectRecordDetailBatch rejectRecordDetailBatch = new RejectRecordDetailBatch();
        rejectRecordDetailBatchDao.updateByPrimaryKeySelective(rejectRecordDetailBatch);
        //调用退供单 TODO
    }

    /**
     * 添加操作日志
     *
     * @param
     */
    private OperationLog addOperationLog() {
        OperationLog operationLog = new OperationLog();
        ErpLogOperationTypeEnum add = ErpLogOperationTypeEnum.ADD;
        operationLog.setOperationType(add.getCode());//日志类型 0 .新增 1.修改 2.删除 3.下载
        ErpLogSourceTypeEnum purchase = ErpLogSourceTypeEnum.RETURN;
        operationLog.setSourceType(purchase.getCode());//来源类型 0.销售 1.采购 2.退货  3.退供
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
