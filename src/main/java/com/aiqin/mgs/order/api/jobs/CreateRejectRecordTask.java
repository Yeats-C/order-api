package com.aiqin.mgs.order.api.jobs;

import com.aiqin.mgs.order.api.component.enums.OrderSucessEnum;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.ReturnOrderInfo;
import com.aiqin.mgs.order.api.service.bill.CreateRejectRecordService;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CreateRejectRecordTask {
    private static final Logger logger = LoggerFactory.getLogger(CreateRejectRecordTask.class);
    @Resource
    CreateRejectRecordService createRejectRecordService;
    @Resource
    private ReturnOrderInfoDao returnOrderInfoDao;

    /**
     * 定时扫描同步失败的拖货单
     * 每半小时执行一次
     */
    @Scheduled(fixedRate = 1000 * 5)
    public void TimedFailedRejectOrder() {
        try {
            //根据订单号查询为同步的订单
            Integer orderSynchroSuccess = OrderSucessEnum.ORDER_SYNCHRO_WAIT.getCode();
            List<ReturnOrderInfo> returnOrderInfos = returnOrderInfoDao.selectByOrderSuccess(orderSynchroSuccess);
            if (returnOrderInfos != null && returnOrderInfos.size() > 0) {
                //计时器
                StopWatch watch = new StopWatch();
                //计时器开始
                watch.start();
                logger.info("根据退货单生成退供单订单定时任务=====>开始");
                for (ReturnOrderInfo returnOrderInfo : returnOrderInfos) {
                    createRejectRecordService.addRejectRecord(returnOrderInfo);
                }
                //计时器结束
                watch.stop();
                logger.info("根据退货单生成退供单订单定时任务=====>结束，本次用时：毫秒", watch.getTime() + "，本次生成：" + returnOrderInfos.size() + "单");
            }
        } catch (Exception e) {
            logger.error("根据退货单生成退供单订单定时任务=====>失败" + e);
        }

    }
}
