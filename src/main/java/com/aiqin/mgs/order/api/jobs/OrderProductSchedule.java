package com.aiqin.mgs.order.api.jobs;

import com.aiqin.mgs.order.api.service.OrderStatisticalService;
import com.aiqin.mgs.order.api.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Createed by sunx on 2019/4/8.<br/>
 * 爱掌柜app商品数据统计相关定时任务
 */
@Component
@Slf4j
public class OrderProductSchedule {

    @Resource
    private OrderStatisticalService orderStatisticalService;

    /**
     * 业务逻辑
     * 畅缺商品解决方案
     * 1、从订单记录获取，30天有销售数据的门店集合（不从门店去获取所有门店）
     * 2、从1获取的门店循环（开50个线程跑，商品服务在跑批的时候网络请求并发可能会比较高），获取每个门店对应订单销量排名前十的sku信息
     * 3、以2获取的数据从商品服务获取这些sku+门店低于安全库存的sku集合
     * 4、取2、3数据的交集，获取畅缺sku集合
     * 5、取4获取的sku集合，获取对应sku14天的销量数据
     * 6、组合数据写入缓存，提供查询，当天截止时间30秒左右过期
     */
    @Scheduled(cron = "0 5 0 * * ?") // 每天0:05分执行
    public void refreshSoldOutOfStockProduct() {
        log.info("畅缺商品定时任务启动");
        Date date = DateUtil.getCurrentDate();

        //获取有效销售的门店集合
        List<String> distributorIds = orderStatisticalService.existsSalesDistributorInNumDays(date, 29);
        log.info("定时任务门店集合【{}】", distributorIds);

        if (CollectionUtils.isEmpty(distributorIds)) {
            return;
        }

        for (String item : distributorIds) {
            orderStatisticalService.refreshDistributorSoldOutOfStockProduct(date, item);
        }
    }


    @Scheduled(cron = "0 5 1 * * ?") // 每天1:05分执行
    public void refreshUnsoldProduct() {
        log.info("非滞销商品sku初始化任务启动");
        Date date = DateUtil.getCurrentDate();
        //获取有效销售的门店集合
        List<String> distributorIds = orderStatisticalService.existsSalesDistributorInNumDays(date, 89);

        log.info("非滞销商品定时任务门店集合【{}】", distributorIds);
        if (CollectionUtils.isEmpty(distributorIds)) {
            return;
        }

        for (String item : distributorIds) {
            orderStatisticalService.refreshDistributorDisUnsoldProduct(date, item);
        }
    }
}
