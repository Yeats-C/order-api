package com.aiqin.mgs.order.api.jobs;

import com.aiqin.mgs.order.api.domain.request.ReportAreaReturnSituationVo;
import com.aiqin.mgs.order.api.service.ReportStoreGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.StopWatch;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * description: 门店补货报表、售后管理统计定时任务执行类
 * date: 2020/2/21 9:18
 * author: hantao
 * version: 1.0
 */
@Slf4j
@Component
public class ReportStoreOrderTaskJob {

    @Resource
    private ReportStoreGoodsService reportStoreGoodsService;

    /**
     * 门店补货报表定时任务执行方法
     * 每天凌晨两点十分执行
     */
    @Scheduled(cron = "0 10 2 * * ? ")
    public void getTask() {
        //计时器
        StopWatch watch = new StopWatch();
        //计时器开始
        watch.start();
        log.info("开始执行门店补货报表定时任务");
        reportStoreGoodsService.reportTimingTask();
        //计时器结束
        watch.stop();
        log.info("执行门店补货报表定时任务============>结束，本次用时：{}毫秒", watch.getTime());
    }

    /**
     * 售后管理--各地区退货统计--配送质量退货
     * 每天凌晨两点二十分执行
     */
    @Scheduled(cron = "0 20 2 * * ? ")
    public void pszlReportAreaReturnSituation() {
        //计时器
        StopWatch watch = new StopWatch();
        //计时器开始
        watch.start();
        log.info("开始执行售后管理--配送质量各地区退货定时任务");
        ReportAreaReturnSituationVo vo=new ReportAreaReturnSituationVo();
        vo.setReasonCode("14");
        vo.setType(2);
        reportStoreGoodsService.areaReturnSituation(vo);
        //计时器结束
        watch.stop();
        log.info("开始执行售后管理--配送质量各地区退货定时任务============>结束，本次用时：{}毫秒", watch.getTime());
    }

}
