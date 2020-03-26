package com.aiqin.mgs.order.api.jobs;

import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.ReturnOrderInfo;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderQueryVo;
import com.aiqin.mgs.order.api.domain.statistical.FrontEndSalesStatistics;
import com.aiqin.mgs.order.api.domain.statistical.FrontEndSalesStatisticsDetail;
import com.aiqin.mgs.order.api.service.FrontEndSalesStatisticsDetailService;
import com.aiqin.mgs.order.api.service.FrontEndSalesStatisticsService;
import com.aiqin.mgs.order.api.service.OrderStatisticalService;
import com.aiqin.mgs.order.api.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author by wpp25
 * @Classname FrontEndSaleStatisticsJob
 * @Description 前台销售统计
 * @Date 2020/2/15 11:12
 */
@Component
public class FrontEndSaleStatisticsJob {

    private static final Logger logger = LoggerFactory.getLogger(ReturnOrderTaskJob.class);

    @Resource
    private FrontEndSalesStatisticsDetailService service;

    /**
     *  每天凌晨执行一次
     *     @Scheduled(cron = "0 0 23 * * ? ") // 每晚11点执行一次
     *     @Scheduled(cron = "0 0 0,23 * * ?") // 每晚11点和凌晨各执行一次
     */
//    @Scheduled(cron = "*/5****?") 每隔5s执行一次
    @Scheduled(cron = "0 0 0 * * ?")
    public void getTask() {
        logger.info("定时任务 处理前台销售统计数据");
        //计时器
        StopWatch watch = new StopWatch();
         //计时器开始
         watch.start();
        List<FrontEndSalesStatisticsDetail> selectYesterdaySalesStatistics = service.selectYesterdaySalesStatistics();
        if (selectYesterdaySalesStatistics != null && selectYesterdaySalesStatistics.size() >0) {
            selectYesterdaySalesStatistics.stream().forEach(s->{
                s.setSaleStatisticsDetailId(IdUtil.uuid());
                s.setSaleStatisticsId(IdUtil.uuid());
            });
        }
        service.insertSalesStatisticsDetailList(selectYesterdaySalesStatistics);

         //计时器结束
        watch.stop();
        logger.info("处理前台销售统计数据=====>结束，本次用时：{}毫秒", watch.getTime());
    }
}
