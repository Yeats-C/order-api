package com.aiqin.mgs.order.api.jobs;

import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.PayStatusEnum;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;
import com.aiqin.mgs.order.api.service.ErpOrderPayService;
import com.aiqin.mgs.order.api.service.ErpOrderQueryService;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单支付轮询定时任务
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/20 14:22
 */
@Component
public class OrderPayRequestTaskJob {

    private static final Logger logger = LoggerFactory.getLogger(OrderPayRequestTaskJob.class);

    @Resource
    private ErpOrderQueryService erpOrderQueryService;

    @Resource
    private ErpOrderPayService erpOrderPayService;

    @Scheduled(cron = "0 0/5 * * * ? ")
    public void getTask() {
        //计时器
        StopWatch watch = new StopWatch();
        //计时器开始
        watch.start();
        logger.info("订单支付轮询定时任务=====>开始");
        OrderStoreOrderInfo orderInfo = new OrderStoreOrderInfo();
        orderInfo.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_1.getCode());
        orderInfo.setPayStatus(PayStatusEnum.PAYING.getCode());
        List<OrderStoreOrderInfo> list = erpOrderQueryService.selectOrderBySelective(orderInfo);
        if (list != null && list.size() > 0) {
            logger.info("订单支付轮询定时任务=====>本次共{}个支付中的订单", list.size());
            for (OrderStoreOrderInfo item :
                    list) {
                try {
                    erpOrderPayService.orderPayPolling(item);
                } catch (Exception e) {
                    logger.error("定时任务轮询支付状态异常：订单号：{}，异常信息：{}", item.getOrderCode(), e);
                }
            }
        } else {
            logger.info("订单支付轮询定时任务=====>本次无支付中的订单");
        }
        //计时器结束
        watch.stop();
        logger.info("订单支付轮询定时任务=====>结束，本次用时：{}毫秒", watch.getTime());
    }

}
