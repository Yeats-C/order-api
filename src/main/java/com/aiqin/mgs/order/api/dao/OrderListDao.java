package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderList;
import com.aiqin.mgs.order.api.domain.request.orderList.*;
import com.aiqin.mgs.order.api.domain.response.orderlistre.FirstOrderTimeRespVo;
import com.aiqin.mgs.order.api.domain.response.orderlistre.OrderStockReVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderListDao {


    int deleteByPrimaryKey(Long id);

    int insert(OrderList record);

    int insertSelective(OrderList record);



    OrderList selectByPrimaryKey(Long id);



    int updateByPrimaryKeySelective(OrderList record);

    int updateByPrimaryKey(OrderList record);

    List<OrderList> searchOrderList(OrderListVo param);

    int searchOrderListCount(OrderListVo param);

    OrderListDetailsVo searchOrderByCode(String code);

    Boolean insertOrderListDetailsVo(OrderListDetailsVo param);

    List<OrderList> searchOrderReceptionList(OrderListVo2 param);

    int searchOrderReceptionListCount(OrderListVo2 param);

    Boolean updateByCode(@Param("code") String code, @Param("status") Integer status, @Param("paymentStatus") Integer paymentStatus);

    List<OrderStockReVo> getStockValue(@Param("vo")OrderStockVo vo);

    List<OrderList> searchFZ(String code);

    List<SupplyOrderInfoReqVO> searchOrderByCodeOrOriginal(String code);

    List<OrderListFather> searchOrderReceptionListFather(OrderListVo2 param);

    int searchOrderReceptionListFatherCount(OrderListVo2 param);

    List<OrderListFather> searchOrderListFather(OrderListVo param);

    int searchOrderListFatherCount(OrderListVo param);

    Boolean updateStatusByCode(@Param("code") String code, @Param("status") Integer status);

    Boolean updateOrderPaymentStatus(@Param("code") String code,@Param("paymentStatus") Integer paymentStatus);

    List<OrderListDetailsVo> searchOrderByCodeFather(String code);

    List<FirstOrderTimeRespVo> selectFirstOrderTime(List<String> storeIds);
}