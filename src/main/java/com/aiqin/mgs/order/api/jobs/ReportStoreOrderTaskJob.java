package com.aiqin.mgs.order.api.jobs;

import com.aiqin.mgs.order.api.component.returnenums.OrderTypeEnum;
import com.aiqin.mgs.order.api.component.returnenums.ReturnReasonEnum;
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
        vo.setReasonCode(ReturnReasonEnum.ORDER_TYPE_ZS.getCode());
        vo.setType(OrderTypeEnum.ORDER_TYPE_PS.getCode());
        reportStoreGoodsService.areaReturnSituation(vo);
        //计时器结束
        watch.stop();
        log.info("开始执行售后管理--配送质量各地区退货定时任务============>结束，本次用时：{}毫秒", watch.getTime());
    }

    /**
     * 售后管理--各地区退货统计--配送一般退货
     * 每天凌晨两点三十分执行
     */
    @Scheduled(cron = "0 30 2 * * ? ")
    public void psybReportAreaReturnSituation() {
        //计时器
        StopWatch watch = new StopWatch();
        //计时器开始
        watch.start();
        log.info("开始执行售后管理--配送一般各地区退货定时任务");
        ReportAreaReturnSituationVo vo=new ReportAreaReturnSituationVo();
        vo.setReasonCode(ReturnReasonEnum.ORDER_TYPE_PS.getCode());
        vo.setType(OrderTypeEnum.ORDER_TYPE_PS.getCode());
        reportStoreGoodsService.areaReturnSituation(vo);
        //计时器结束
        watch.stop();
        log.info("开始执行售后管理--配送质量各地区退货定时任务============>结束，本次用时：{}毫秒", watch.getTime());
    }

    /**
     * 售后管理--各地区退货统计--直送退货
     * 每天凌晨两点四十分执行
     */
    @Scheduled(cron = "0 40 2 * * ? ")
    public void zsReportAreaReturnSituation() {
        //计时器
        StopWatch watch = new StopWatch();
        //计时器开始
        watch.start();
        log.info("开始执行售后管理--直送各地区退货定时任务");
        ReportAreaReturnSituationVo vo=new ReportAreaReturnSituationVo();
        vo.setType(OrderTypeEnum.ORDER_TYPE_ZS.getCode());
        reportStoreGoodsService.areaReturnSituation(vo);
        //计时器结束
        watch.stop();
        log.info("开始执行售后管理--直送各地区退货定时任务============>结束，本次用时：{}毫秒", watch.getTime());
    }

    /**
     * 售后管理--退货商品分类统计--配送质量退货
     * 每天凌晨三点二十分执行
     */
    @Scheduled(cron = "0 20 3 * * ? ")
    public void pszlReportCategoryGoods() {
        //计时器
        StopWatch watch = new StopWatch();
        //计时器开始
        watch.start();
        log.info("开始执行售后管理--配送退货商品分类统计定时任务");
        ReportAreaReturnSituationVo vo=new ReportAreaReturnSituationVo();
        vo.setReasonCode(ReturnReasonEnum.ORDER_TYPE_ZS.getCode());
        vo.setType(OrderTypeEnum.ORDER_TYPE_PS.getCode());
        reportStoreGoodsService.reportCategoryGoods(vo);
        //计时器结束
        watch.stop();
        log.info("开始执行售后管理--配送退货商品分类统计定时任务============>结束，本次用时：{}毫秒", watch.getTime());
    }

    /**
     * 售后管理--退货商品分类统计--配送一般退货
     * 每天凌晨三点三十分执行
     */
    @Scheduled(cron = "0 30 3 * * ? ")
    public void psybReportCategoryGoods() {
        //计时器
        StopWatch watch = new StopWatch();
        //计时器开始
        watch.start();
        log.info("开始执行售后管理--配送退货商品分类统计定时任务");
        ReportAreaReturnSituationVo vo=new ReportAreaReturnSituationVo();
        vo.setReasonCode(ReturnReasonEnum.ORDER_TYPE_PS.getCode());
        vo.setType(OrderTypeEnum.ORDER_TYPE_PS.getCode());
        reportStoreGoodsService.reportCategoryGoods(vo);
        //计时器结束
        watch.stop();
        log.info("开始执行售后管理--配送退货商品分类统计定时任务============>结束，本次用时：{}毫秒", watch.getTime());
    }

}
