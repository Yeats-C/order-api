package com.aiqin.mgs.order.api.jobs;

import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.service.OrderListService;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 描述:订单tob定时任务
 *
 * @author zhujunchao
 * @create 2019-03-28 17:00
 */
@Component
@Transactional
public class OrderCancellation {

    private static final Logger log = LoggerFactory.getLogger(OrderCancellation.class);
    @Resource
    private OrderListService orderListService;

    //超时分钟数
    @Value("${overtime}")
    private  Integer overtime;
    //订单创建超过30分钟未支付的订单改为99, "交易取消"
    @Scheduled(cron = "0 0/1 * * * ? ")
//    @Scheduled(cron = "0/1 * * * * ? ")
    public void cancellation() {
        //查询所有超过30分钟未支付的订单
        List<String> codeString = orderListService.selectOrderCancellation(overtime, new Date());
        if (codeString != null && codeString.size() > 0) {
            Boolean br = orderListService.updateOrderCancellation(codeString, 99);
        }
        return;
    }

    /**
     * 发放物流减免费用定时任务
     */
    @Scheduled(cron = "0 0 2 * * ? ") //每天凌晨两点执行
    public void getTasks(){
        //计时器
        StopWatch watch = new StopWatch();
        log.info("开始执行发放物流减免费用定时任务");
        //计时器开始
        watch.start();
        List<ErpOrderInfo> list=orderListService.getLogisticsSentList();
        if(null!=list){
            for (ErpOrderInfo info:list){
                Boolean check= orderListService.refund(info);
            }
        }

        //计时器结束
        watch.stop();
        log.info("执行发放物流减免费用定时任务============>结束，本次用时：{}毫秒", watch.getTime());
    }
}
