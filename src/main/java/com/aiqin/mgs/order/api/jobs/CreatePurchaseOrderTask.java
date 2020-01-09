package com.aiqin.mgs.order.api.jobs;

import com.aiqin.mgs.order.api.component.enums.OrderSucessEnum;
import com.aiqin.mgs.order.api.dao.order.ErpOrderInfoDao;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.service.bill.CreatePurchaseOrderService;
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

    /**
     * 定时扫描生成采购三失败的定单
     * 每5秒执行一次 @Scheduled(fixedRate = 1000 * 5)
     */
    //在固定时间执行 @Scheduled(cron = "0 */1 *  * * * ")
    //@Scheduled(fixedRate = 1000 * 5)
    public void TimedFailedPurchaseOrder() {
        try {
            //查询同步时失败的订单
            Integer orderSuccess = OrderSucessEnum.ORDER_SYNCHRO_WAIT.getCode();
            List<ErpOrderInfo> erpOrderInfos = erpOrderInfoDao.selectByOrderSucess(orderSuccess);
            if (erpOrderInfos != null && erpOrderInfos.size() > 0) {
                try {
                    //计时器
                    StopWatch watch = new StopWatch();
                    //计时器开始
                    watch.start();
                    logger.info("根据订单生成采购单定时任务=====>开始");
                    for (ErpOrderInfo orderInfo : erpOrderInfos) {
                        createPurchaseOrderService.addOrderAndDetail(orderInfo);
                    }
                    //计时器结束
                    watch.stop();
                    logger.info("根据订单生成采购单定时任务=====>结束，本次用时：{}毫秒", watch.getTime() + "，生成：" + erpOrderInfos.size() + "单");
                } catch (Exception e) {
                    logger.error("根据订单生成采购单定时任务=====>失败" + e);
                }
            }
        } catch (Exception e) {
            logger.error("根据订单生成采购单定时任务=====>失败" + e);
            throw new RuntimeException();
        }
    }
}
