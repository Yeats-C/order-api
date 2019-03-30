package com.aiqin.mgs.order.api.jobs;

import com.aiqin.mgs.order.api.service.OrderListService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
