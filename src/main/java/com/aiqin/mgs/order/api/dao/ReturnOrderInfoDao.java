package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.ReturnOrderInfo;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderReviewReqVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReturnOrderInfoDao {

    int deleteByPrimaryKey(Long id);

    int insertSelective(ReturnOrderInfo record);

    ReturnOrderInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReturnOrderInfo record);

    List<ReturnOrderInfo> selectByOrderCodeAndStatus(@Param("orderStoreCode") String orderStoreCode, @Param("returnOrderStatus") Integer returnOrderStatus);

    Integer updateReturnStatus(ReturnOrderReviewReqVo reqVo);


}