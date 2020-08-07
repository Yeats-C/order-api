package com.aiqin.mgs.order.api.dao.returnorder;

import com.aiqin.mgs.order.api.domain.ReturnOrderDetail;
import com.aiqin.mgs.order.api.domain.request.ReportAreaReturnSituationVo;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderDetailReviewApiReqVo;
import com.aiqin.mgs.order.api.domain.response.ReturnOrderDetailList;
import com.aiqin.mgs.order.api.domain.response.returngoods.ReturnOrderInfoItemBatchRespVO;
import com.aiqin.mgs.order.api.domain.response.returngoods.ReturnOrderInfoItemRespVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ReturnOrderDetailDao {

    int deleteByPrimaryKey(Long id);

    int insertSelective(ReturnOrderDetail record);

    ReturnOrderDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReturnOrderDetail record);

    int insertBatch(@Param("records") List<ReturnOrderDetail> records);

    int insertWriteDownOrderBatch(@Param("records") List<ReturnOrderDetail> records);

    int deleteByReturnOrderCode(@Param("returnOrderCode") String returnOrderCode);

    List<ReturnOrderDetail> selectListByReturnOrderCode(String returnOrderCode);

    int updateActualCountBatch(List<ReturnOrderDetailReviewApiReqVo> list);

    int updateActualAmountBatch(List<ReturnOrderDetail> list);

    String selectUrlsByReturnOrderDetailId(String returnOrderDetailId);

    BigDecimal countAmountByCategoryCode(ReportAreaReturnSituationVo vo);

    List<ReturnOrderInfoItemRespVO> selectReturnOrderList(@Param("returnOrderCode") String returnOrderCode);

    ReturnOrderDetail selectReturnOrder(String skuCode);

    List<ReturnOrderInfoItemBatchRespVO> selectReturnOrderBatchList(@Param("returnOrderCode") String returnOrderCode);

    List<ReturnOrderDetailList> selectOrderDetailAll(String returnOrderCode);
}