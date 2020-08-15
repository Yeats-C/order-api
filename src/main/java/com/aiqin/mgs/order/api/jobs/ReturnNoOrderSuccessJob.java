package com.aiqin.mgs.order.api.jobs;


import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.ReturnOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.service.order.ErpOrderQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.List;


/**
 * 预生成退货单修改退货单同步供应链定时任务
 */
@Component
public class ReturnNoOrderSuccessJob {

    private static final Logger logger = LoggerFactory.getLogger(ReturnNoOrderSuccessJob.class);

    @Autowired
    private ReturnOrderInfoDao  returnOrderInfoDao;
    @Autowired
    private ErpOrderQueryService erpOrderQueryService;


//    @Scheduled(cron = )
    public void Synchronize(){
        logger.info("预生成退货修改为退货单同步供应链开始");
        try {
            //计时器
            StopWatch stopWatch = new StopWatch();
            //计时器开始
            stopWatch.start();
            List<ReturnOrderInfo> returnOrderInfos = returnOrderInfoDao.selectReallyReturn();
            logger.info("预生成退货单-返回结果集： " + returnOrderInfos);
            for (ReturnOrderInfo returnOrderInfo : returnOrderInfos){
                ErpOrderInfo orderByOrderCode = erpOrderQueryService.getOrderByOrderCode(returnOrderInfo.getOrderStoreCode());
                Boolean aBoolean = checkSendOk(orderByOrderCode.getMainOrderCode());
                logger.info("子订单是否发货完成的返回结果： " + aBoolean);
                if (aBoolean){
                    returnOrderInfoDao.updateReturnReallyReturn(returnOrderInfo.getReturnOrderCode(),returnOrderInfo.getOrderStoreCode());
                }
            }
        }catch (Exception e){

        }

    }

    /**
     * 判断子单是否全部发货完成
     * @param mainOrderCode
     * @return true:全部发货完成 false:未全部发货
     */
    private Boolean checkSendOk(String mainOrderCode){
        logger.info("判断子单是否全部发货完成,入参mainOrderCode={}",mainOrderCode);
        ErpOrderInfo po=new ErpOrderInfo();
        po.setMainOrderCode(mainOrderCode);
        List<ErpOrderInfo> list=erpOrderQueryService.select(po);
        if(list!=null&&list.size()>0){
            logger.info("判断子单是否全部发货完成,原始订单集合为 list={}", JsonUtil.toJson(list));
            for(ErpOrderInfo eoi:list){
                Integer orderStatus = eoi.getOrderStatus();
                //判断订单状态是否是 11:发货完成或者 97:缺货终止
                if(orderStatus.equals(ErpOrderStatusEnum.ORDER_STATUS_11.getCode())
                        ||orderStatus.equals(ErpOrderStatusEnum.ORDER_STATUS_97.getCode())
                        ||orderStatus.equals(ErpOrderStatusEnum.ORDER_STATUS_12.getCode())
                        ||orderStatus.equals(ErpOrderStatusEnum.ORDER_STATUS_13.getCode())
                        ||orderStatus.equals(ErpOrderStatusEnum.ORDER_STATUS_4.getCode())){
                    return true;
                }else{
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
