package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.base.PageRequestVO;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.ReportAreaReturnSituation;
import com.aiqin.mgs.order.api.domain.ReportStoreGoods;
import com.aiqin.mgs.order.api.domain.ReportStoreGoodsDetail;
import com.aiqin.mgs.order.api.domain.request.ReportAreaReturnSituationVo;
import com.aiqin.mgs.order.api.domain.request.ReportStoreGoodsDetailVo;
import com.aiqin.mgs.order.api.domain.request.ReportStoreGoodsVo;
import com.aiqin.mgs.order.api.domain.response.report.ReportOrderAndStoreListResponse;

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

    ReportOrderAndStoreListResponse<PageResData<ReportStoreGoods>> getList(PageRequestVO<ReportStoreGoodsVo> searchVo);

    List<ReportStoreGoodsDetail> getCountDetailList(ReportStoreGoodsDetailVo searchVo);
    //门店补货报表统计---定时任务使用
    void reportTimingTask();
    //售后管理---各地区退货情况
    void areaReturnSituation(ReportAreaReturnSituationVo vo);
    //各地区退货排行榜TOP10
    List<ReportAreaReturnSituation> topProvinceAmount(Integer type);

}
