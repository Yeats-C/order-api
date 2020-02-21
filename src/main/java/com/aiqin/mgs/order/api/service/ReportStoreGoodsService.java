package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.base.PageRequestVO;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.ReportStoreGoods;
import com.aiqin.mgs.order.api.domain.ReportStoreGoodsDetail;
import com.aiqin.mgs.order.api.domain.request.ReportStoreGoodsDetailVo;
import com.aiqin.mgs.order.api.domain.request.ReportStoreGoodsVo;

import java.util.List;

/**
 * description: ReportStoreGoodsService
 * date: 2020/2/19 17:02
 * author: hantao
 * version: 1.0
 */
public interface ReportStoreGoodsService {

    Boolean insert(ReportStoreGoods entity);

    Boolean update(ReportStoreGoods entity);

    PageResData<ReportStoreGoods> getList(PageRequestVO<ReportStoreGoodsVo> searchVo);

    List<ReportStoreGoodsDetail> getCountDetailList(ReportStoreGoodsDetailVo searchVo);
    //门店补货报表统计---定时任务使用
    void reportTimingTask();

}
