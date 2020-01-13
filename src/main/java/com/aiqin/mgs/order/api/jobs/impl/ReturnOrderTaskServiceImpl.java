package com.aiqin.mgs.order.api.jobs.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.mgs.order.api.base.ConstantData;
import com.aiqin.mgs.order.api.dao.returnorder.RefundInfoDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.RefundInfo;
import com.aiqin.mgs.order.api.jobs.ReturnOrderTaskService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * description: ReturnOrderTaskServiceImpl
 * date: 2020/1/3 15:13
 * author: hantao
 * version: 1.0
 */
@Service
@Slf4j
public class ReturnOrderTaskServiceImpl implements ReturnOrderTaskService {

    @Value("${bridge.url.pay-api}")
    private String paymentHost;
    @Autowired
    private ReturnOrderInfoDao returnOrderInfoDao;
    @Resource
    private RefundInfoDao refundInfoDao;

    @Override
    @Transactional
    public boolean searchPayOrder(String orderCode) {
        String url=paymentHost+"/payment/pay/searchPayOrder?orderNo="+orderCode;
        log.info("根据退货单号查询支付状态入参，url={}",url);
        HttpClient httpClient = HttpClient.get(url);
        Map<String ,Object> result=null;
        result = httpClient.action().result(new TypeReference<Map<String ,Object>>() {});
        log.info("根据退货单号查询支付状态结果，request={}",result);
        if(result!=null&&"0".equals(result.get("code"))){
            log.info("根据退货单号查询支付状态结果---退款成功");
            //更新退货单表状态
            returnOrderInfoDao.updateRefundStatus(orderCode);
            //更新流水表状态
            Map map=(Map)result.get("data");
            RefundInfo refundInfo=new RefundInfo();
            //1-已退款
            refundInfo.setStatus(ConstantData.REFUND_STATUS);
            refundInfo.setUpdateTime(new Date());
            refundInfo.setPayNum(map.get("payNum").toString());
            refundInfo.setOrderCode(orderCode);
            refundInfoDao.updateByOrderCode(refundInfo);
            return true;
        }
        return false;
    }


}
