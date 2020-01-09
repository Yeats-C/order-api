package com.aiqin.mgs.order.api.jobs;

import com.aiqin.mgs.order.api.component.enums.ErpOrderScourSheetStatusEnum;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.service.order.ErpOrderQueryService;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 生成冲减单定时任务
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/9 20:12
 */
@Component
public class ErpScourSheetTaskJob {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderPayTimeOutTaskJob.class);

    @Resource
    private ErpOrderQueryService erpOrderQueryService;

    @Scheduled(cron = "0 0 0/2 * * ? ")
    public void getTask() {
        //计时器
        StopWatch watch = new StopWatch();
        //计时器开始
        watch.start();
        logger.info("生成冲减单定时任务=====>开始");

        ErpOrderInfo orderQuery = new ErpOrderInfo();
        orderQuery.setScourSheetStatus(ErpOrderScourSheetStatusEnum.WAIT.getCode());
        List<ErpOrderInfo> orderList = erpOrderQueryService.select(orderQuery);
        if (orderList != null && orderList.size() > 0) {
            for (ErpOrderInfo item :
                    orderList) {
                //TODO 调用接口生成冲减单

            }
        }

        //计时器结束
        watch.stop();
        logger.info("生成冲减单定时任务=====>结束，本次用时：{}毫秒", watch.getTime());
    }

}
