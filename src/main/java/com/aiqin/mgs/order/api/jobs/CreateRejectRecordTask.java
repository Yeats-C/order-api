package com.aiqin.mgs.order.api.jobs;

import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.service.bill.CreateRejectRecordService;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CreateRejectRecordTask {
    private static final Logger logger = LoggerFactory.getLogger(CreateRejectRecordTask.class);
    @Resource
    CreateRejectRecordService createRejectRecordService;
    @Resource
    private ReturnOrderInfoDao returnOrderInfoDao;

    /**
     * 定时扫描同步失败的拖货单
     * 每2小时执行一次
     */
    @Scheduled(fixedRate = 100000 * 72)
    public void TimedFailedRejectOrder() {
        //计时器
        StopWatch watch = new StopWatch();
        //计时器开始
        watch.start();
        logger.info("根据退货单生成退供单订单定时任务=====>开始");
        createRejectRecordService.addRejectRecord(null);
        //计时器结束
        watch.stop();
        logger.info("根据退货单生成退供单订单定时任务=====>结束，本次用时：毫秒", watch.getTime());
    }
}
