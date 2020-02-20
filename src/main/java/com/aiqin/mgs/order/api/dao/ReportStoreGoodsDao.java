package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.ReportStoreGoods;
import com.aiqin.mgs.order.api.domain.ReportStoreGoodsDetail;
import com.aiqin.mgs.order.api.domain.request.ReportStoreGoodsVo;
import com.aiqin.mgs.order.api.domain.response.report.ReportStoreGoodsCountResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReportStoreGoodsDao {

    int deleteByPrimaryKey(Long id);

    int insertSelective(ReportStoreGoods record);

    ReportStoreGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReportStoreGoods record);

    List<ReportStoreGoods> selectList(ReportStoreGoodsVo vo);

//    List<ReportStoreGoodsCountResponse> selectProductCount(String storeCode);
    List<ReportStoreGoods> selectProductCount(String storeCode);

    List<ReportStoreGoodsDetail> selectReportStoreGoodsDetail(String storeCode);

    ReportStoreGoods selectReportStoreGoods(ReportStoreGoods record);

    int insertBatch(@Param("records") List<ReportStoreGoods> records);

}