package com.aiqin.mgs.order.api.jobs;

import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.domain.constant.OrderConstant;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.service.order.ErpOrderPayService;
import com.aiqin.mgs.order.api.service.order.ErpOrderQueryService;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 取消超时未支付订单定时任务
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/20 16:54
 */
@Component
public class ErpOrderPayTimeOutTaskJob {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderPayTimeOutTaskJob.class);

    @Resource
    private ErpOrderQueryService erpOrderQueryService;

    @Resource
    private ErpOrderPayService erpOrderPayService;

    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void getTask() {
        //计时器
        StopWatch watch = new StopWatch();
        //计时器开始
        watch.start();
        logger.info("取消超时未支付订单定时任务=====>开始");

        ErpOrderInfo orderQuery = new ErpOrderInfo();
        orderQuery.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_1.getCode());


        List<ErpOrderInfo> orderList = erpOrderQueryService.select(orderQuery);
        if (orderList != null && orderList.size() > 0) {

            Date now = new Date();
            //这个时间点之前的订单取消
            long cancelStartTime = now.getTime() - OrderConstant.MAX_PAY_TIME_OUT_TIME;

            for (ErpOrderInfo item :
                    orderList) {
                if (item.getCreateTime().getTime() < cancelStartTime) {
                    erpOrderPayService.orderTimeoutUnpaid(item.getOrderStoreCode());
                }
            }
        }

        //计时器结束
        watch.stop();
        logger.info("取消超时未支付订单定时任务=====>结束，本次用时：{}毫秒", watch.getTime());
    }

}
