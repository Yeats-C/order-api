package com.aiqin.mgs.order.api.dao.returnorder;

import com.aiqin.mgs.order.api.domain.ReturnOrderDetail;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderDetailReviewApiReqVo;
import org.apache.ibatis.annotations.Param;

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

}