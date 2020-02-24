package com.aiqin.mgs.order.api.jobs;

import com.aiqin.mgs.order.api.service.FirstReportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 首单报表定时任务
 */
@Slf4j
@Component
public class FirstReportTaskJob {

    @Autowired
    private FirstReportService firstReportService;

    /**
     * 首单月报定时任务
     */
    @Scheduled(cron = "0 10 3 * * ? ") //每天凌晨三点十分执行
    public void getTasks(){
        //计时器
        StopWatch watch = new StopWatch();
        //计时器开始
        watch.start();
        log.info("开始执行首单月报报表定时任务");
        firstReportService.reportTimedTask();
        //计时器结束
        watch.stop();
        log.info("执行首单月报报表定时任务============>结束，本次用时：{}毫秒", watch.getTime());
    }


    /**
     * 首单年报定时任务
     */
    @Scheduled(cron = "0 10 1 * * ? ")//每天凌晨一点十分执行
    public void getTasksYear(){
        //计时器
        StopWatch watch = new StopWatch();
        //计时器开始
        watch.start();
        log.info("开始执行首单年报报表定时任务");
        firstReportService.reportTimedTaskYear();
        //计时器结束
        watch.stop();
        log.info("执行首单年报报表定时任务============>结束，本次用时：{}毫秒", watch.getTime());
    }

}
