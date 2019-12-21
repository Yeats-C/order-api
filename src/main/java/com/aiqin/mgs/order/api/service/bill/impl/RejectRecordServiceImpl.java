package com.aiqin.mgs.order.api.service.bill.impl;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.OperationLogDao;
import com.aiqin.mgs.order.api.dao.RejectRecordDao;
import com.aiqin.mgs.order.api.dao.RejectRecordDetailBatchDao;
import com.aiqin.mgs.order.api.dao.RejectRecordDetailDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.request.bill.RejectRecordReq;
import com.aiqin.mgs.order.api.service.bill.RejectRecordService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 退货单 实现类
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

    @Override
    public HttpResponse createRejectRecord(@Valid RejectRecordReq rejectRecordReq) {
        //异步执行
        rejectRecordExecutor(rejectRecordReq);
        if (StringUtils.isEmpty(rejectRecordReq)
                && StringUtils.isEmpty(rejectRecordReq.getRejectRecord())
                && StringUtils.isEmpty(rejectRecordReq.getRejectRecordDetail())) {
            return HttpResponse.success();
        }
        return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
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
                addRejectRecord(rejectRecordReq);
                //添加退货单信息
                addRejectRecordDetail(rejectRecordReq);
                //添加操作日志
                addOperationLog(rejectRecordReq);
            }
        });
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
     * 添加操作日志
     */
    private void addOperationLog(RejectRecordReq rejectRecordReq) {
        AuthToken auth = AuthUtil.getCurrentAuth();
        OperationLog operationLog = new OperationLog();
        //operationLog.setOperationCode(purchaseOrderReq);//来源编码
        //operationLog.setOperationType();//日志类型 0 .新增 1.修改 2.删除 3.下载
        //operationLog.setSourceType();//来源类型 0.销售 1.采购 2.退货  3.退供
        //operationLog.setOperationContent();//日志内容
        //operationLog.setRemark();//备注
        //operationLog.setUseStatus();//0. 启用 1.禁用

        operationLog.setCreateById(auth.getPersonId());
        operationLog.setCreateByName(auth.getPersonName());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        operationLog.setCreateTime(formatter.format(new Date()));
        operationLogDao.insert(operationLog);
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

}
