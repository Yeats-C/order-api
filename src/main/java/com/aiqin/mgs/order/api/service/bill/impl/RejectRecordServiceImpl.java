package com.aiqin.mgs.order.api.service.bill.impl;

import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.component.enums.OrderSucessEnum;
import com.aiqin.mgs.order.api.component.enums.RejectRecordStatusEnum;
import com.aiqin.mgs.order.api.component.returnenums.ReturnOrderStatusEnum;
import com.aiqin.mgs.order.api.dao.RejectRecordDao;
import com.aiqin.mgs.order.api.dao.RejectRecordDetailBatchDao;
import com.aiqin.mgs.order.api.dao.RejectRecordDetailDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.request.RejectRequest;
import com.aiqin.mgs.order.api.domain.request.bill.ReturnBatchDetailDLReq;
import com.aiqin.mgs.order.api.domain.request.bill.ReturnDLReq;
import com.aiqin.mgs.order.api.domain.request.bill.ReturnOrderDetailDLReq;
import com.aiqin.mgs.order.api.domain.request.bill.ReturnOrderInfoDLReq;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderDetailReviewApiReqVo;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderReviewApiReqVo;
import com.aiqin.mgs.order.api.domain.response.RejectResponse;
import com.aiqin.mgs.order.api.domain.response.RejectVoResponse;
import com.aiqin.mgs.order.api.service.bill.CreateRejectRecordService;
import com.aiqin.mgs.order.api.service.bill.RejectRecordService;
import com.aiqin.mgs.order.api.service.returnorder.ReturnOrderInfoService;
import com.aiqin.mgs.order.api.util.BeanHelper;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 爱亲退供单 实现类
 */
@Service
public class RejectRecordServiceImpl implements RejectRecordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RejectRecordServiceImpl.class);

    @Resource
    ReturnOrderInfoDao returnOrderInfoDao;
    @Resource
    RejectRecordDao rejectRecordDao;
    @Resource
    RejectRecordDetailDao rejectRecordDetailDao;
    @Resource
    RejectRecordDetailBatchDao rejectRecordDetailBatchDao;
    @Resource
    CreateRejectRecordService createRejectRecordService;
    @Resource
    ReturnOrderInfoService returnOrderInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HttpResponse createRejectRecord(String returnOrderCode) {
        LOGGER.info("根据ERP退货单生成爱亲退供单，开始，returnOrderCode{}", returnOrderCode);
        if (StringUtils.isNotBlank(returnOrderCode)) {
            //异步执行
            rejectRecordExecutor(returnOrderCode);
            return HttpResponse.success();
        }
        return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
    }

    //耘链退货单回传
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean selectPurchaseInfo(ReturnDLReq returnDLReq) {
        try {
            LOGGER.info("耘链退货单回传参数： " + JsonUtil.toJson(returnDLReq));
            if (returnDLReq.getReturnOrderInfoDLReq() != null) {
                RejectRecord rejectRecord = new RejectRecord();
                ReturnOrderInfoDLReq returnOrderInfo = returnDLReq.getReturnOrderInfoDLReq();
                rejectRecord.setRejectRecordCode(returnOrderInfo.getReturnOrderCode());//退货单号
                rejectRecord.setActualTotalCount(returnOrderInfo.getActualProductCount());//实退商品数量
                rejectRecord.setChargePerson(returnOrderInfo.getReturnById());//退货人id
                rejectRecord.setUpdateTime(new Date());//修改时间
                 LOGGER.info("退供单：{}",rejectRecord);
                //修改退货单
                rejectRecordDao.updateByReturnOrderCode(rejectRecord);

            if (returnDLReq.getReturnOrderDetailDLReqList() != null && returnDLReq.getReturnOrderDetailDLReqList().size() > 0) {
                List<ReturnOrderDetailDLReq> returnOrderDetails = returnDLReq.getReturnOrderDetailDLReqList();
                List<ReturnOrderDetailReviewApiReqVo> detailReviewApis = new ArrayList<>();
                for (ReturnOrderDetailDLReq returnOrderDetail : returnOrderDetails) {
                    RejectRecordDetail rejectRecordDetail = new RejectRecordDetail();
                    rejectRecordDetail.setProductCount(returnOrderDetail.getActualReturnProductCount());
                    rejectRecordDetail.setLineCode(returnOrderDetail.getLineCode());
                    rejectRecordDetail.setSkuCode(returnOrderDetail.getSkuCode());
                    rejectRecordDetail.setSkuName(returnOrderDetail.getSkuName());
                    rejectRecordDetail.setUpdateTime(new Date());
                    LOGGER.info("退工单明细：{}",rejectRecordDetail);
                    //修改退货单明细
                    rejectRecordDetailDao.updateByPrimaryKey(rejectRecordDetail);

                    ReturnOrderDetailReviewApiReqVo returnOrderDetailReviewApi = new ReturnOrderDetailReviewApiReqVo();
                    returnOrderDetailReviewApi.setLineCode(returnOrderDetail.getLineCode().intValue());
                    returnOrderDetailReviewApi.setActualReturnProductCount(returnOrderDetail.getActualReturnProductCount());
                    detailReviewApis.add(returnOrderDetailReviewApi);
                }
                ReturnOrderReviewApiReqVo returnOrderReviewApi = new ReturnOrderReviewApiReqVo();
                returnOrderReviewApi.setReturnOrderCode(returnDLReq.getReturnOrderInfoDLReq().getReturnOrderCode());//退货单编码
                returnOrderReviewApi.setOperator(returnDLReq.getReturnOrderInfoDLReq().getReturnById());//操作人
                returnOrderReviewApi.setOperateStatus(ReturnOrderStatusEnum.RETURN_ORDER_STATUS_CHECK.getKey());//退货单状态(供应链使用):4-等待退货验收，5-等待退货入库 11-退货完成
                returnOrderReviewApi.setDetails(detailReviewApis);
                //ERP 退货单状态修改
                ReturnOrderInfo returnOrderInfo1 = returnOrderInfoDao.selectByReturnOrderCode(returnOrderReviewApi.getReturnOrderCode());
                LOGGER.info("service 层退货单状态修改********************,returnOrderInfo1={}",returnOrderInfo1);
                if(!returnOrderInfo1.getRefundStatus().equals(1)){
                    LOGGER.info("进行退款的入参：{}",returnOrderReviewApi);
                    LOGGER.info("供应链入库完成--回调退货单*********");
                    returnOrderInfoService.updateReturnStatusApi(returnOrderReviewApi);
                }

            }

            if (returnDLReq.getReturnBatchDetailDLReqList() != null) {
                List<ReturnBatchDetailDLReq> returnBatchDetails = returnDLReq.getReturnBatchDetailDLReqList();
                for (ReturnBatchDetailDLReq returnBatchDetail : returnBatchDetails) {
                    RejectRecordDetailBatch rejectRecordDetailBatch = new RejectRecordDetailBatch();
                    rejectRecordDetailBatch.setRejectRecordCode(returnDLReq.getReturnOrderInfoDLReq().getReturnOrderCode());
                    rejectRecordDetailBatch.setRejectRecordDetailBatchId(IdUtil.purchaseId());
                    rejectRecordDetailBatch.setSkuCode(returnBatchDetail.getSkuCode());
                    rejectRecordDetailBatch.setSkuName(returnBatchDetail.getSkuName());
                    rejectRecordDetailBatch.setLineCode(returnBatchDetail.getLineCode());
                    rejectRecordDetailBatch.setActualTotalCount(Long.valueOf(returnBatchDetail.getBatchNum()));
                    rejectRecordDetailBatch.setCreateTime(new Date());
                    //添加退货单明细批次
                    rejectRecordDetailBatchDao.insertSelective(rejectRecordDetailBatch);
                }
            }
            } else {
                LOGGER.error("耘链退货单回传参数为空 returnDLReq:"+returnDLReq);
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            LOGGER.error("耘链退货单回传失败"+e);
            throw new RuntimeException();
        }
        LOGGER.info("耘链退货单回传成功");
        return true;
    }

    //取消退货单
    @Override
    public Boolean removeRejectRecordStatus(String rejectRecordCode) {
        if (StringUtils.isNotBlank(rejectRecordCode)) {
            try {
                RejectRecord rejectRecord = new RejectRecord();
                rejectRecord.setRejectRecordCode(rejectRecordCode);
                rejectRecord.setRejectRecordStatus(RejectRecordStatusEnum.PURCHASE_ORDER_STATUS_REMOVE.getCode());
                rejectRecordDao.updateByReturnOrderCode(rejectRecord);
                LOGGER.error("取消退货单成功，取消单号：" + rejectRecordCode);
            } catch (Exception e) {
                LOGGER.error("取消退货单失败，，取消失败单号：" + rejectRecordCode);
                throw new RuntimeException();
            }
        }
        return true;
    }

    /**
     * 根据条件查询退供单
     *
     * @param rejectRequest
     * @return
     */
    @Override
    public HttpResponse<List<RejectResponse>> selectByRejectRequest(RejectRequest rejectRequest) {
        /*if (rejectRequest == null){
            //return ResultCode.PARAMETER_EXCEPTION;
            return HttpResponse.failure(ResultCode.PARAMETER_EXCEPTION);
        }*/
        PageHelper.startPage(rejectRequest.getPageNo(), rejectRequest.getPageSize());
        List<RejectRecord> rejectRecords = rejectRecordDao.selectByRequest(rejectRequest);
        //Assert.notNull(rejectRecords, "退供单不存在");

        if (CollectionUtils.isEmpty(rejectRecords)) {
            return HttpResponse.failure(ResultCode.NO_FOUND_REJECT_ERROR);
        }

        List<RejectRecordVo> rejectRecordVos = BeanHelper.copyWithCollection(rejectRecords, RejectRecordVo.class);

        List<RejectResponse> responses = BeanHelper.copyWithCollection(rejectRecordVos, RejectResponse.class);

        return HttpResponse.success(responses);
    }

    /**
     * 根据退供单号查询退供单详情
     *
     * @param rejectRecordCode
     * @return
     */
    @Override
    public HttpResponse<RejectVoResponse> searchRejectDetailByRejectCode(String rejectRecordCode) {
        //判断参数是否合法
        if (org.apache.commons.lang.StringUtils.isBlank(rejectRecordCode)) {
            //返回无效参数
            return HttpResponse.failure(ResultCode.PARAMETER_EXCEPTION);
        }
        RejectRecord rejectRecord = rejectRecordDao.selectByRejectRecordCode(rejectRecordCode);
        //Assert.notNull(rejectRecord, "退供单不存在");
        //LOGGER.info();
        if (rejectRecord == null) {
            //返回找不到该退供单
            return HttpResponse.failure(ResultCode.NOT_FOUND_REJECT_RECORD_DATA);
        }
        RejectRecordVo rejectRecordVo = BeanHelper.copyProperties(rejectRecord, RejectRecordVo.class);
        List<RejectRecordDetail> rejectRecordDetails = rejectRecordDetailDao.selectByRejectRecordCode(rejectRecordCode);
        List<RejectRecordDetailBatch> rejectRecordDetailBatches = rejectRecordDetailBatchDao.selectByRejectRecordCode(rejectRecordCode);

        RejectVoResponse resp = new RejectVoResponse();
        resp.setDetails(rejectRecordDetails);
        resp.setRejectRecordVo(rejectRecordVo);
        resp.setRejectRecordDetailBatches(rejectRecordDetailBatches);
        return HttpResponse.success(resp);
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
                try {
                    int count = 0;
                    Integer orderSynchroSuccess = OrderSucessEnum.ORDER_SYNCHRO_WAIT.getCode();
                    LOGGER.info("待生成退供单以及退货编码：{},{}",orderSynchroSuccess,returnOrderCode);
                    //查询待ERP退货单，待生成爱亲退供单数据
                    ReturnOrderInfo returnOrderInfo = returnOrderInfoDao.selectByOrderCodeAndSuccess(orderSynchroSuccess, returnOrderCode);
                    //时间紧张，暂时这样，有时间再去优化
                    while(returnOrderInfo == null){
                       if (count == 5){
                           break;
                       }
                       returnOrderInfo = returnOrderInfoDao.selectByOrderCodeAndSuccess(orderSynchroSuccess, returnOrderCode);
                       count++;
                       Thread.currentThread().sleep(500);
                       LOGGER.info("循环第： " + count + "次查询");
                    }
                    LOGGER.info("查询ERP退货单---返回实体:{}",returnOrderInfo);
                    if (returnOrderInfo != null) {
                        createRejectRecordService.addRejectRecord(returnOrderCode);
                        LOGGER.info("根据ERP退货单生成爱亲退供单&爱亲退供单明细&修改ERP订单同步状态结束");
                    } else {
                        LOGGER.error("查询待ERP退货单为空");
                        throw new IllegalArgumentException();
                    }
                } catch (Exception e) {
                    LOGGER.error("同步退供单失败" + e);
                    throw new RuntimeException();
                }
            }
        });
    }
}
