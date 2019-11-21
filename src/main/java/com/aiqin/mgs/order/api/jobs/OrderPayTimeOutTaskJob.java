package com.aiqin.mgs.order.api.jobs;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.PayStatusEnum;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;
import com.aiqin.mgs.order.api.domain.constant.OrderConstant;
import com.aiqin.mgs.order.api.service.ErpOrderPayService;
import com.aiqin.mgs.order.api.service.ErpOrderQueryService;
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
public class OrderPayTimeOutTaskJob {

    private static final Logger logger = LoggerFactory.getLogger(OrderPayRequestTaskJob.class);

    @Resource
    private ErpOrderQueryService erpOrderQueryService;

    @Resource
    private ErpOrderPayService erpOrderPayService;

//    @Scheduled(cron = "0 0/10 * * * ? ")
    public void getTask() {
        //计时器
        StopWatch watch = new StopWatch();
        //计时器开始
        watch.start();
        logger.info("取消超时未支付订单定时任务=====>开始");
        OrderStoreOrderInfo orderInfo = new OrderStoreOrderInfo();
        orderInfo.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_1.getCode());
        orderInfo.setPayStatus(PayStatusEnum.UNPAID.getCode());
        List<OrderStoreOrderInfo> list = erpOrderQueryService.selectOrderBySelective(orderInfo);
        Date now = new Date();
        if (list != null && list.size() > 0) {
            for (OrderStoreOrderInfo item :
                    list) {
                Date createTime = item.getCreateTime();
                if (now.getTime() - createTime.getTime() > OrderConstant.MAX_PAY_TIME_OUT_TIME) {
                    try {
                        erpOrderPayService.orderTimeoutUnpaid(item);
                    } catch (BusinessException e) {
                        logger.info("取消超时未支付订单定时任务=====>失败：订单号：{}，异常信息：{}", item.getOrderCode(), e.getMessage());
                    } catch (Exception e) {
                        logger.info("取消超时未支付订单定时任务=====>失败：订单号：{}，异常信息：{}", item.getOrderCode(), e);
                    }
                }
            }
        }
        //计时器结束
        watch.stop();
        logger.info("取消超时未支付订单定时任务=====>结束，本次用时：{}毫秒", watch.getTime());
    }

}
