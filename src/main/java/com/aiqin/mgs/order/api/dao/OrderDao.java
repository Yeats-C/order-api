/*****************************************************************

* 模块名称：订单后台-DAO接口层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-08 

* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import java.util.List;

import javax.validation.Valid;

import com.aiqin.mgs.order.api.domain.*;

import io.lettuce.core.dynamic.annotation.Param;

public interface OrderDao {

	
	//订单查询
    List<OrderInfo> selectOrder(OrderQuery OrderQuery) throws Exception;
    
    //订单查询byorderid
    OrderInfo selecOrderById(OrderDetailQuery orderDetailQuery) throws Exception;
    
    //添加新的订单主数据
    void addOrderInfo(@Valid OrderInfo orderInfo) throws Exception;

    //接口-分销机构维度-总销售额 返回INT
	int selectOrderAmt(@Param("distributorId")String distributorId, @Param("originType")String originType)throws Exception;

	//接口-分销机构+当月维度-当月销售额
	void selectByMonthAllAmt(@Param("distributorId")String distributorId, @Param("originType")String originType,@Param("year")String year, @Param("month")String month);

    //接口-分销机构+当月维度-当月实收
    void selectbByMonthRetailAmt(@Param("distributorId")String distributorId, @Param("originType")String originType,@Param("year")String year, @Param("month")String month);

    //接口-分销机构+当月维度-当月支付订单数
    void selectByMonthAcount(@Param("distributorId")String distributorId, @Param("originType")String originType,@Param("year")String year, @Param("month")String month);

}
