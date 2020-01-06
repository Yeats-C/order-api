package com.aiqin.mgs.order.api.service.bill.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpLogOperationTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpLogSourceTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpLogStatusTypeEnum;
import com.aiqin.mgs.order.api.component.enums.OrderSucessEnum;
import com.aiqin.mgs.order.api.dao.*;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderDetailDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderSearchVo;
import com.aiqin.mgs.order.api.service.bill.CreateRejectRecordService;
import com.aiqin.mgs.order.api.service.bill.OperationLogService;
import com.aiqin.mgs.order.api.service.bill.RejectRecordService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.mgs.order.api.util.BeanCopyUtils;
import com.aiqin.mgs.order.api.util.RequestReturnUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
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

    @Resource
    private ReturnOrderInfoDao returnOrderInfoDao;
    @Resource
    private ReturnOrderDetailDao returnOrderDetailDao;
    @Resource
    ReturnOrderDetailBatchDao returnOrderDetailBatchDao;
    @Resource
    CreateRejectRecordService createRejectRecordService;
    @Resource
    OperationLogService operationLogService;

    @Override
    @Transactional
    public HttpResponse createRejectRecord(String returnOrderCode) {
        LOGGER.info("同步推供单，rejectRecordReq{}", returnOrderCode);
        //异步执行
        rejectRecordExecutor(returnOrderCode);
        return HttpResponse.success();
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
                try {
                    //添加退货单
                    LOGGER.info("开始生成退供单&退供单明细，参数为：returnOrderCode{}", returnOrderCode);
                    Integer orderSynchroSuccess = OrderSucessEnum.ORDER_SYNCHRO_WAIT.getCode();
                    ReturnOrderInfo returnOrderInfo = returnOrderInfoDao.selectByOrderCodeAndSuccess(orderSynchroSuccess, returnOrderCode);
                    if (returnOrderInfo != null) {
                        LOGGER.info("生成退供单&退供单明细开始");
                        createRejectRecordService.addRejectRecord(returnOrderInfo);
                        LOGGER.info("生成退供单&退供单明细结束");

                        //根据爱亲退供单，生成耘链退货单
                        LOGGER.info("开始根据爱亲退供单，生成耘链退货单，参数为：returnOrderCode{}", returnOrderCode);
                        createSaleOrder(returnOrderCode);
                        LOGGER.info("根据爱亲退供单，生成耘链退货单结束");

                        //添加操作日志
                        LOGGER.info("添加根据爱亲退供单，生成耘链退货单，操作日志开始");
                        addOperationLog(returnOrderInfo.getReturnOrderCode());
                        LOGGER.info("根据爱亲退供单，生成耘链退货单，添加操作日志结束");
                    } else {
                        throw new IllegalArgumentException();
                    }
                } catch (Exception e) {
                    LOGGER.error("同步退供单失败" + e);
                    throw new RuntimeException();
                }
            }
        });
    }

    /**
     * 根据爱亲退供单，生成耘链退货单
     *
     * @param returnOrderCode
     */
    private void createSaleOrder(String returnOrderCode) {
        LOGGER.info("根据爱亲退供单，生成耘链退货单开始 returnOrderCode： " + returnOrderCode);
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
                String url = "http://192.168.10.183:80/returnGoods/record/return";
                LOGGER.info("returnOrderReq" + returnOrderReq);
                HttpClient httpGet = HttpClient.post(url).json(returnOrderReq).timeout(10000);
                HttpResponse<Object> response = httpGet.action().result(new TypeReference<HttpResponse<Object>>() {
                });
                if (!RequestReturnUtil.validateHttpResponse(response)) {
                    throw new BusinessException(response.getMessage());
                }
            }
        } catch (Exception e) {
            LOGGER.error("根据爱亲退供单，生成耘链退货单失败returnOrderCode： " + returnOrderCode);
        }
    }

    /**
     * 根据爱亲退供单，生成耘链退货单，添加操作日志
     *
     * @param returnOrderCode
     */
    private void addOperationLog(String returnOrderCode) {
        Integer operationType = ErpLogSourceTypeEnum.RETURN_SUPPLY.getCode();
        Integer sourceType = ErpLogOperationTypeEnum.ADD.getCode();
        Integer useStatus = ErpLogStatusTypeEnum.USING.getCode();
        String operationContent = "根据爱亲退供单，生成耘链退货单";
        operationLogService.insert(returnOrderCode, operationType, sourceType, operationContent, null, useStatus, null);
    }
}
