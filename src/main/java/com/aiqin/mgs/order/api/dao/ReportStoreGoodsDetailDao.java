package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.ReportStoreGoodsDetail;
import com.aiqin.mgs.order.api.domain.request.ReportStoreGoodsDetailVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ReportStoreGoodsDetailDao {

    int deleteByPrimaryKey(Long id);

    int insert(ReportStoreGoodsDetail record);

    int insertSelective(ReportStoreGoodsDetail record);

    ReportStoreGoodsDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReportStoreGoodsDetail record);

    int updateByPrimaryKey(ReportStoreGoodsDetail record);

    List<ReportStoreGoodsDetail> selectList(ReportStoreGoodsDetailVo vo);

    int insertBatch(@Param("records") List<ReportStoreGoodsDetail> records);

    BigDecimal sumAmountByBrandId(ReportStoreGoodsDetailVo vo);

}