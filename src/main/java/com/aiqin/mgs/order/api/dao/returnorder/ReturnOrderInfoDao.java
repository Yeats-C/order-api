package com.aiqin.mgs.order.api.dao.returnorder;

import com.aiqin.mgs.order.api.domain.ReturnOrderInfo;
import com.aiqin.mgs.order.api.domain.request.returnorder.AfterReturnOrderSearchVo;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderReviewReqVo;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderSearchVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ReturnOrderInfoDao {

    int deleteByPrimaryKey(Long id);

    int insertSelective(ReturnOrderInfo record);

    ReturnOrderInfo selectByPrimaryKey(Long id);

    ReturnOrderInfo selectByReturnOrderCode(String returnOrderCode);

    int updateByPrimaryKeySelective(ReturnOrderInfo record);

    List<ReturnOrderInfo> selectByOrderCodeAndStatus(@Param("orderStoreCode") String orderStoreCode, @Param("returnOrderStatus") Integer returnOrderStatus);

    Integer updateReturnStatus(ReturnOrderReviewReqVo reqVo);

    Integer updateLogistics(@Param("returnOrderCode") String returnOrderCode,@Param("logisticsCompanyCode") String logisticsCompanyCode,@Param("logisticsCompanyName") String logisticsCompanyName,@Param("logisticsCode") String logisticsCode);

    //根据订单id查询是否生成了退货单，且未完成流程。这里订单状态不为12-退款完成，97-退货终止，98-审核不通过，99-已取消
    List<ReturnOrderInfo> selectByOrderId(@Param("orderStoreCode") String orderStoreCode);

    //修改实退数量和实退金额
    Integer updateLogisticsCountAndAmount(@Param("returnOrderCode") String returnOrderCode, @Param("actualReturnOrderAmount") BigDecimal actualReturnOrderAmount, @Param("actualProductCount") Long actualProductCount);

    //修改退款状态
    Integer updateRefundStatus(String orderStoreCode);

    //后台销售退货单管理列表（搜索）
    List<ReturnOrderInfo> page(ReturnOrderSearchVo searchVo);

    //后台销售退货单管理列表条数（搜索）
    Integer pageCount(ReturnOrderSearchVo searchVo);

    //通过条件查询退货单
    ReturnOrderInfo selectByParameter(Map queryData);

    //售后管理--退货单列表
    List<ReturnOrderInfo> selectAll(AfterReturnOrderSearchVo searchVo);

    String selectOrderId(@Param("returnOrderCode") String returnOrderCode);

}