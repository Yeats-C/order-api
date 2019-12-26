package com.aiqin.mgs.order.api.dao.returnOrder;

import com.aiqin.mgs.order.api.domain.ReturnOrderInfo;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderReviewReqVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ReturnOrderInfoDao {

    int deleteByPrimaryKey(Long id);

    int insertSelective(ReturnOrderInfo record);

    ReturnOrderInfo selectByPrimaryKey(Long id);

    ReturnOrderInfo selectByReturnOrderCode(String returnOrderCode);

    int updateByPrimaryKeySelective(ReturnOrderInfo record);

    List<ReturnOrderInfo> selectByOrderCodeAndStatus(@Param("orderStoreCode") String orderStoreCode, @Param("returnOrderStatus") Integer returnOrderStatus);

    Integer updateReturnStatus(ReturnOrderReviewReqVo reqVo);

    Integer updateLogistics(@Param("orderStoreCode") String orderStoreCode,@Param("logisticsCompanyCode") String logisticsCompanyCode,@Param("logisticsCompanyName") String logisticsCompanyName,@Param("logisticsCode") String logisticsCode);

    //根据订单id查询是否生成了退货单，且未完成流程。这里订单状态不为12-退款完成，97-退货终止，98-审核不通过，99-已取消
    List<ReturnOrderInfo> selectByOrderId(@Param("orderStoreCode") String orderStoreCode);

    //修改实退数量和实退金额
    Integer updateLogisticsCountAndAmount(@Param("orderStoreCode") String orderStoreCode, @Param("actualReturnOrderAmount") BigDecimal actualReturnOrderAmount, @Param("actualProductCount") Long actualProductCount);

    //修改退款状态
    Integer updateRefundStatus(String orderStoreCode);
}