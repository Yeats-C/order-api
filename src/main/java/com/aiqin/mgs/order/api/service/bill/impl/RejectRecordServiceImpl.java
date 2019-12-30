package com.aiqin.mgs.order.api.service.bill.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.component.enums.ErpLogOperationTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpLogSourceTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpLogStatusTypeEnum;
import com.aiqin.mgs.order.api.dao.OperationLogDao;
import com.aiqin.mgs.order.api.dao.RejectRecordDao;
import com.aiqin.mgs.order.api.dao.RejectRecordDetailBatchDao;
import com.aiqin.mgs.order.api.dao.RejectRecordDetailDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderDetailDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.request.bill.RejectRecordReq;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderSearchVo;
import com.aiqin.mgs.order.api.service.bill.RejectRecordService;
import com.aiqin.mgs.order.api.util.AuthUtil;
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


    @Override
    @Transactional
    public HttpResponse createRejectRecord(@Valid RejectRecordReq rejectRecordReq) {
        LOGGER.info("同步推供单，rejectRecordReq{}", rejectRecordReq);
        //异步执行
        rejectRecordExecutor(rejectRecordReq);
        if (StringUtils.isEmpty(rejectRecordReq)
                && StringUtils.isEmpty(rejectRecordReq.getRejectRecord())
                && StringUtils.isEmpty(rejectRecordReq.getRejectRecordDetail())) {
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

        return null;
    }

    /**
     * 异步执行
     *
     * @param rejectRecordReq
     */
    private void rejectRecordExecutor(RejectRecordReq rejectRecordReq) {
        //异步执行
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                //添加退货单
                LOGGER.info("开始同步退供单，参数为：rejectRecordReq{}", rejectRecordReq);
                addRejectRecord(rejectRecordReq);
                LOGGER.info("同步退供单结束");
                //添加操作日志
                LOGGER.info("开始同步退供单,操作日志");
                addRejectRecordLog(rejectRecordReq);
                LOGGER.info("同步退供单,操作日志结束");


                //添加退货单详情信息
                LOGGER.info("开始添加退供单详情信息，参数为：rejectRecordReq{}", rejectRecordReq);
                addRejectRecordDetail(rejectRecordReq);
                LOGGER.info("添加退供单信息详情结束");
                //添加操作日志
                LOGGER.info("添加同步退供单详情信息,操作日志开始");
                addRejectRecordDetailLog(rejectRecordReq);
                LOGGER.info("添加同步退供单,操作日志结束");

                //根据爱亲退供单，生成耘链退货单
                LOGGER.info("开始根据爱亲退供单，生成耘链退货单，参数为：rejectRecordReq{}", rejectRecordReq);
                createSaleOrder(rejectRecordReq);
                LOGGER.info("根据爱亲退供单，生成耘链退货单结束");
                ////添加操作日志
                LOGGER.info("添加根据爱亲退供单，生成耘链退货单，操作日志开始");
                createSaleOrderLog(rejectRecordReq);
                LOGGER.info("根据爱亲退供单，生成耘链退货单，添加操作日志结束");
            }
        });
    }

    /**
     * 根据爱亲退供单，生成耘链退货单
     * @param rejectRecordReq
     */
    private void createSaleOrder(RejectRecordReq rejectRecordReq) {
        String url = " ";
        HttpClient httpGet = HttpClient.post(url.toString()).json(rejectRecordReq).timeout(10000);
        httpGet.action();
    }

    /**
     * 同步退供单,操作日志
     * @param rejectRecordReq
     */
    private void addRejectRecordLog(RejectRecordReq rejectRecordReq) {
        OperationLog operationLog = addOperationLog();
        //operationLog.setOperationCode(erpOrderInfo.getOrderStoreCode());//来源编码
        operationLog.setOperationContent("同步退供单");//日志内容
        operationLog.setRemark("");//备注

        operationLogDao.insertSelective(operationLog);
    }

    /**
     * 同步退供单详情信息,操作日志
     * @param rejectRecordReq
     */
    private void addRejectRecordDetailLog(RejectRecordReq rejectRecordReq) {
        OperationLog operationLog = addOperationLog();
        //operationLog.setOperationCode(erpOrderInfo.getOrderStoreCode());//来源编码
        operationLog.setOperationContent("同步退供单详情信息");//日志内容
        operationLog.setRemark("");//备注

        operationLogDao.insertSelective(operationLog);
    }

    /**
     * 根据爱亲退供单，生成耘链退货单，添加操作日志
     * @param rejectRecordReq
     */
    private void createSaleOrderLog(RejectRecordReq rejectRecordReq) {
        OperationLog operationLog = addOperationLog();
        //operationLog.setOperationCode(erpOrderInfo.getOrderStoreCode());//来源编码
        operationLog.setOperationContent("根据爱亲退供单，生成耘链退货单");//日志内容
        operationLog.setRemark("");//备注

        operationLogDao.insertSelective(operationLog);
    }

    /**
     * 添加退货单
     */
    private void addRejectRecord(RejectRecordReq rejectRecordReq) {
        rejectRecordDao.insert(rejectRecordReq.getRejectRecord());
    }

    /**
     * 添加退货单信息
     */
    private void addRejectRecordDetail(RejectRecordReq rejectRecordReq) {
        rejectRecordDetailDao.insert(rejectRecordReq.getRejectRecordDetail());
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
        ErpLogSourceTypeEnum purchase =  ErpLogSourceTypeEnum.RETURN;
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
