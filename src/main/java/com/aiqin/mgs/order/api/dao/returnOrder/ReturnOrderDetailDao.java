package com.aiqin.mgs.order.api.dao.returnOrder;

import com.aiqin.mgs.order.api.domain.ReturnOrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReturnOrderDetailDao {

    int deleteByPrimaryKey(Long id);

    int insertSelective(ReturnOrderDetail record);

    ReturnOrderDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReturnOrderDetail record);

    int insertBatch(@Param("records") List<ReturnOrderDetail> records);

    int deleteByReturnOrderCode(@Param("returnOrderCode")String returnOrderCode);

}
