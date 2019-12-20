package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.component.SequenceService;
import com.aiqin.mgs.order.api.dao.ReturnOrderDetailDao;
import com.aiqin.mgs.order.api.dao.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.ReturnOrderDetail;
import com.aiqin.mgs.order.api.domain.ReturnOrderInfo;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderReqVo;
import com.aiqin.mgs.order.api.domain.response.returnorder.ReturnOrderListVo;
import com.aiqin.mgs.order.api.service.ReturnOrderInfoService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * description: ReturnOrderInfoServiceImpl
 * date: 2019/12/19 19:16
 * author: hantao
 * version: 1.0
 */
@Service
@Slf4j
public class ReturnOrderInfoServiceImpl implements ReturnOrderInfoService {

    @Autowired
    private ReturnOrderInfoDao returnOrderInfoDao;
    @Autowired
    private ReturnOrderDetailDao returnOrderDetailDao;
    @Autowired
    private SequenceService sequenceService;

    @Override
    @Transactional
    public Boolean save(ReturnOrderReqVo reqVo) {
        //查询订单是否存在未处理售后单
        List<ReturnOrderInfo> returnOrderInfo = returnOrderInfoDao.selectByOrderCodeAndStatus(reqVo.getOrderStoreCode(), 1);
        Assert.isTrue(CollectionUtils.isEmpty(returnOrderInfo), "该订单还有未审核售后单，请稍后提交");
        ReturnOrderInfo record = new ReturnOrderInfo();
        Date now = new Date();
        BeanUtils.copyProperties(reqVo, record);
        String returnOrderId = IdUtil.uuid();
        String afterSaleCode = sequenceService.generateOrderAfterSaleCode(reqVo.getCompanyCode(), reqVo.getReturnOrderType());
        record.setReturnOrderId(returnOrderId);
        record.setReturnOrderCode(afterSaleCode);
        record.setCreateTime(now);
        switch (reqVo.getReturnOrderType()) {
            case 2:
                record.setReturnOrderStatus(1);
                break;
            case 1:
            case 0:
                record.setReturnOrderStatus(12);
                record.setActualProductCount(record.getProductCount());
                record.setActualReturnOrderAmount(record.getReturnOrderAmount());
//                refund(record, reqVo.getDiscountAmountInfos(), reqVo.getCreateBy());
                break;
            default:
                record.setReturnOrderStatus(1);
        }
//        if (reqVo.getReturnDiscountAmount() > 0) {//退回优惠额度
//            record.setDiscountAmountInfoStr(JSON.toJSONString(reqVo.getDiscountAmountInfos()));
//        }
        returnOrderInfoDao.insertSelective(record);
        List<ReturnOrderDetail> details = reqVo.getDetails().stream().map(detailVo -> {
            ReturnOrderDetail detail = new ReturnOrderDetail();
            BeanUtils.copyProperties(detailVo, detail);
            detail.setCreateTime(now);
            detail.setReturnOrderDetailId(IdUtil.uuid());
            detail.setReturnOrderCode(returnOrderId);
            detail.setCreateById(reqVo.getCreateById());
            detail.setCreateByName(reqVo.getCreateByName());
            return detail;
        }).collect(Collectors.toList());
        returnOrderDetailDao.insertBatch(details);
        return true;
    }

//    @Override
//    public PageResData<ReturnOrderListVo> list(OrderAfterSaleSearchVo searchVo) {
//        return null;
//    }

}
