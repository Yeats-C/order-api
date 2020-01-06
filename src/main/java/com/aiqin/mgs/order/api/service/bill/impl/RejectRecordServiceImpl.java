package com.aiqin.mgs.order.api.service.bill.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpLogOperationTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpLogSourceTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpLogStatusTypeEnum;
import com.aiqin.mgs.order.api.dao.*;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderDetailDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderSearchVo;
import com.aiqin.mgs.order.api.service.bill.CreateRejectRecordService;
import com.aiqin.mgs.order.api.service.bill.RejectRecordService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.mgs.order.api.util.RequestReturnUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
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
    OperationLogDao operationLogDao;
    @Resource
    private ReturnOrderInfoDao returnOrderInfoDao;
    @Resource
    private ReturnOrderDetailDao returnOrderDetailDao;
    @Resource
    ReturnOrderDetailBatchDao returnOrderDetailBatchDao;
    @Resource
    CreateRejectRecordService createRejectRecordService;

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
                    createRejectRecordService.addRejectRecord(returnOrderCode);
                    LOGGER.info("生成退供单&退供单明细结束");

                    //根据爱亲退供单，生成耘链退货单
                    LOGGER.info("开始根据爱亲退供单，生成耘链退货单，参数为：returnOrderCode{}", returnOrderCode);
                    createSaleOrder(returnOrderCode);
                    LOGGER.info("根据爱亲退供单，生成耘链退货单结束");

                    ////添加操作日志
                    LOGGER.info("添加根据爱亲退供单，生成耘链退货单，操作日志开始");
                    createSaleOrderLog(returnOrderCode);
                    LOGGER.info("根据爱亲退供单，生成耘链退货单，添加操作日志结束");
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
        //根据订单号查询为同步的订单
        ReturnOrderInfo returnOrderInfo = returnOrderInfoDao.selectByReturnOrderCode(returnOrderCode);
        List<ReturnOrderDetail> returnOrderDetails = returnOrderDetailDao.selectListByReturnOrderCode(returnOrderCode);
        ReturnOrderReq returnOrderReq = new ReturnOrderReq();
        BeanUtils.copyProperties(returnOrderInfo,returnOrderReq.getReturnOrderInfo());
        BeanUtils.copyProperties(returnOrderDetails,returnOrderReq.getReturnOrderDetails());
        String url = "";
        HttpClient httpGet = HttpClient.post(url).json(returnOrderReq).timeout(10000);
        HttpResponse<Object> response = httpGet.action().result(new TypeReference<HttpResponse<Object>>() {
        });
        if (!RequestReturnUtil.validateHttpResponse(response)) {
            throw new BusinessException(response.getMessage());
        }
    }

    /**
     * 根据爱亲退供单，生成耘链退货单，添加操作日志
     *
     * @param returnOrderCode
     */
    private void createSaleOrderLog(String returnOrderCode) {
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
        //operationLog.setOperationCode(erpOrderInfo.getOrderStoreCode());//来源编码
        operationLog.setOperationContent("根据爱亲退供单，生成耘链退货单");//日志内容
        operationLog.setRemark("");//备注

        operationLogDao.insertSelective(operationLog);
    }
}
