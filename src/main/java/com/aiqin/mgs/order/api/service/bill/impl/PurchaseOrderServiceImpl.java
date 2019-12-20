package com.aiqin.mgs.order.api.service.bill.impl;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.OperationLogDao;
import com.aiqin.mgs.order.api.dao.PurchaseOrderDao;
import com.aiqin.mgs.order.api.dao.PurchaseOrderDetailBatchDao;
import com.aiqin.mgs.order.api.dao.PurchaseOrderDetailDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.OperationLog;
import com.aiqin.mgs.order.api.domain.PurchaseOrderDetail;
import com.aiqin.mgs.order.api.domain.PurchaseOrderDetailBatch;
import com.aiqin.mgs.order.api.domain.request.bill.PurchaseOrderReq;
import com.aiqin.mgs.order.api.service.bill.PurchaseOrderService;
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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 销售 实现类
 */
@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);

    @Autowired
    PurchaseOrderDao purchaseOrderDao;
    @Autowired
    PurchaseOrderDetailDao purchaseOrderDetailDao;
    @Autowired
    OperationLogDao operationLogDao;
    @Autowired
    PurchaseOrderDetailBatchDao purchaseOrderDetailBatchDao;

    @Override
    public HttpResponse createSaleOrder(@Valid PurchaseOrderReq purchaseOrderReq) {
        LOGGER.info("请求参数purchaseOrderReq为{}", purchaseOrderReq);
        //异步执行
        purchaseOrderExecutor(purchaseOrderReq);
        if (StringUtils.isEmpty(purchaseOrderReq)
                //关联子订单
                && !StringUtils.isEmpty(purchaseOrderReq.getPurchaseOrder())
                //订单商品明细行
                && !StringUtils.isEmpty(purchaseOrderReq.getPurchaseOrderDetailList())) {
            //返回
            return HttpResponse.success();
        }
        return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
    }

    /**
     * 异步执行
     *
     * @param purchaseOrderReq
     */
    private void purchaseOrderExecutor(PurchaseOrderReq purchaseOrderReq) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                //添加采购单
                addPurchaseOrder(purchaseOrderReq);
                //添加采购商品信息
                addPurchaseOrderDetail(purchaseOrderReq);
                //添加操作日志
                addOperationLog(purchaseOrderReq);

            }
        });
    }

    /**
     * 添加采购单
     *
     * @param purchaseOrderReq
     */
    private void addPurchaseOrder(PurchaseOrderReq purchaseOrderReq) {
        purchaseOrderDao.insert(purchaseOrderReq.getPurchaseOrder());
    }

    /**
     * 添加采购商品信息
     *
     * @param purchaseOrderReq
     */
    private void addPurchaseOrderDetail(PurchaseOrderReq purchaseOrderReq) {
        List<PurchaseOrderDetail> purchaseOrderDetailList = purchaseOrderReq.getPurchaseOrderDetailList();
        purchaseOrderDetailDao.insertList(purchaseOrderDetailList);
    }

    /**
     * 添加操作日志
     *
     * @param
     */
    private void addOperationLog(PurchaseOrderReq purchaseOrderReq) {
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
        PurchaseOrderDetailBatch purchaseOrderDetailBatch = new PurchaseOrderDetailBatch();
        purchaseOrderDetailBatchDao.updateByPrimaryKeySelective(purchaseOrderDetailBatch);
        //调用销售单 TODO
    }
}