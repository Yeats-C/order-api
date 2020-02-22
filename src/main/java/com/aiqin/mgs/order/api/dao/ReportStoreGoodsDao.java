package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.ReportStoreGoods;
import com.aiqin.mgs.order.api.domain.ReportStoreGoodsDetail;
import com.aiqin.mgs.order.api.domain.request.ReportStoreGoodsDetailVo;
import com.aiqin.mgs.order.api.domain.request.ReportStoreGoodsVo;
import com.aiqin.mgs.order.api.domain.response.report.ReportOrderAndStoreResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReportStoreGoodsDao {

    int deleteByStoreCodeAndCountTime(ReportStoreGoodsDetailVo vo);

    int insertSelective(ReportStoreGoods record);

    ReportStoreGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReportStoreGoods record);

    List<ReportStoreGoods> selectList(ReportStoreGoodsVo vo);

    List<ReportStoreGoods> selectProductCount(String storeCode);

    List<ReportStoreGoodsDetail> selectReportStoreGoodsDetail(String storeCode);

    ReportStoreGoods selectReportStoreGoods(ReportStoreGoods record);

    int insertBatch(@Param("records") List<ReportStoreGoods> records);

    List<ReportOrderAndStoreResponse> selectOrderByStoreCodes(@Param("records") List<String> records);

    String selectStoreCodeByOrderCode(@Param("orderStoreCode") String orderStoreCode);

    int updateByStoreCodeAndTime(ReportStoreGoods record);

}