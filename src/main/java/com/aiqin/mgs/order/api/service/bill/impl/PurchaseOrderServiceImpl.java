package com.aiqin.mgs.order.api.service.bill.impl;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.OperationLogDao;
import com.aiqin.mgs.order.api.dao.PurchaseOrderDetailDao;
import com.aiqin.mgs.order.api.dao.PurchaseOrderDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.OperationLog;
import com.aiqin.mgs.order.api.domain.PurchaseOrderDetail;
import com.aiqin.mgs.order.api.domain.request.bill.PurchaseOrderReq;
import com.aiqin.mgs.order.api.service.bill.PurchaseOrderService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    @Override
    public HttpResponse createSaleOrder(@Valid @RequestBody PurchaseOrderReq purchaseOrderReq) {
        LOGGER.info("请求参数purchaseOrderReq为{}", purchaseOrderReq);
        //异步执行
        saleOrderExecutor(purchaseOrderReq);
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
    private void saleOrderExecutor(PurchaseOrderReq purchaseOrderReq) {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //添加采购单
                addPurchaseOrder(purchaseOrderReq);
                //添加采购商品信息
                addPurchaseOrderDetail(purchaseOrderReq);
                //添加操作日志
                addOperationLog(purchaseOrderReq);
                //调用销售单 TODO
            }
            //轮询时间控制【表示延迟1秒后每3秒执行一次】
        }, 1, 3, TimeUnit.SECONDS);
    }

    /**
     * 添加采购单
     * @param purchaseOrderReq
     */
    private void addPurchaseOrder(PurchaseOrderReq purchaseOrderReq) {
        purchaseOrderDao.insert(purchaseOrderReq.getPurchaseOrder());
    }

    /**
     * 添加采购商品信息
     * @param purchaseOrderReq
     */
    private void addPurchaseOrderDetail(PurchaseOrderReq purchaseOrderReq) {
        List<PurchaseOrderDetail> purchaseOrderDetailList =  purchaseOrderReq.getPurchaseOrderDetailList();
        purchaseOrderDetailDao.insertList(purchaseOrderDetailList);
    }

    /**
     * 添加操作日志
     * @param
     */
    private void addOperationLog(PurchaseOrderReq purchaseOrderReq) {
        AuthToken auth = AuthUtil.getCurrentAuth();
        OperationLog operationLog =new OperationLog();
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
}