package com.aiqin.mgs.order.api.jobs;

import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.ReturnOrderInfo;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderQueryVo;
import com.aiqin.mgs.order.api.service.returnorder.ReturnOrderInfoService;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 取消超时未支付订单定时任务
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/20 16:54
 */
@Component
//@EnableScheduling
public class ReturnOrderTaskJob {

    private static final Logger logger = LoggerFactory.getLogger(ReturnOrderTaskJob.class);

    @Resource
    private ReturnOrderInfoService returnOrderInfoService;
    @Resource
    private ReturnOrderInfoDao returnOrderInfoDao;

    @Scheduled(cron = "0 * * * * ?")
    public void getTask() {
        //计时器
        StopWatch watch = new StopWatch();
        //计时器开始
        watch.start();
        logger.info("查询退货单退款定时任务=====>开始");
        ReturnOrderQueryVo searchVo=new ReturnOrderQueryVo();
        searchVo.setReturnOrderStatus(11);
        List<ReturnOrderInfo> returnOrderInfos = returnOrderInfoDao.selectAll(searchVo);
        for(ReturnOrderInfo roi:returnOrderInfos){
            returnOrderInfoService.searchPayOrder(roi.getReturnOrderCode());
        }
        //计时器结束
        watch.stop();
        logger.info("查询退货单退款定时任务=====>结束，本次用时：{}毫秒", watch.getTime());
    }

}
