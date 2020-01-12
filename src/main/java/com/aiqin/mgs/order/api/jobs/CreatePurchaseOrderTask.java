package com.aiqin.mgs.order.api.jobs;

import com.aiqin.mgs.order.api.component.enums.OrderSucessEnum;
import com.aiqin.mgs.order.api.dao.order.ErpOrderInfoDao;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.service.bill.CreatePurchaseOrderService;
import com.aiqin.mgs.order.api.service.order.ErpOrderQueryService;
import com.google.common.collect.Lists;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreatePurchaseOrderTask {
    private static final Logger logger = LoggerFactory.getLogger(CreatePurchaseOrderTask.class);
    @Autowired
    ErpOrderInfoDao erpOrderInfoDao;
    @Autowired
    CreatePurchaseOrderService createPurchaseOrderService;
    @Autowired
    ErpOrderQueryService erpOrderQueryService;

    /**
     * 定时扫描生成采购三失败的定单
     * 每5秒执行一次 @Scheduled(fixedRate = 1000 * 5)
     */
    //在固定时间执行 @Scheduled(cron = "0 */1 *  * * * ")
    @Scheduled(fixedRate = 1000 * 5)
    public void TimedFailedPurchaseOrder() {
        List<ErpOrderInfo> erpOrderInfos = null;
        List<ErpOrderInfo> orderInfos = Lists.newArrayList();
        try {
            //查询同步失败的订单
            erpOrderInfos = erpOrderInfoDao.selectByOrderSucess(OrderSucessEnum.ORDER_SYNCHRO_WAIT.getCode());
        } catch (Exception e) {
            logger.error("查询同步失败的订单异常", e);
            throw new RuntimeException();
        }

        if (erpOrderInfos != null && erpOrderInfos.size() > 0) {
            for (ErpOrderInfo erpOrderInfo : erpOrderInfos) {
                try {
                    //查询同步失败的订单明细
                    ErpOrderInfo orderInfo = erpOrderQueryService.getOrderAndItemByOrderCode(erpOrderInfo.getOrderStoreCode());
                    orderInfos.add(orderInfo);
                } catch (Exception e) {
                    logger.error("查询同步失败的订单明细异常", e);
                    throw new RuntimeException();
                }
            }
        }

        if (orderInfos != null && orderInfos.size() > 0) {
            for (ErpOrderInfo orderInfo : orderInfos) {
                if (orderInfo.getItemList() != null && orderInfo.getItemList().size() > 0) {
                    StopWatch watch = new StopWatch();//计时器
                    watch.start();//计时器开始
                    logger.info("根据订单生成采购单定时任务=====>开始");
                    try {
                        //根据ERP订单生成爱亲采购单,采购单明细,修改订单同步状态&根据爱亲采购单，生成耘链销售单,添加操作日志
                        createPurchaseOrderService.addOrderAndDetail(orderInfo);
                    } catch (Exception e) {
                        logger.error("根据订单生成采购单定时任务=====>失败" + e);
                    }
                    watch.stop();//计时器结束
                    logger.info("根据订单生成采购单定时任务=====>结束，本次用时：{}毫秒", watch.getTime() + "，生成：" + erpOrderInfos.size() + "单");
                }
            }
        }
    }
}
