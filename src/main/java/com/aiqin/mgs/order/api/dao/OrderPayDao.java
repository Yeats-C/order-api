/*****************************************************************

* 模块名称：订单支付后台-DAO接口层
* 开发人员: hzy
* 开发时间: 2018-11-08 

* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import java.util.List;

import javax.validation.Valid;

import com.aiqin.mgs.order.api.domain.OrderPayInfo;

import io.lettuce.core.dynamic.annotation.Param;

public interface OrderPayDao {

    //添加新的支付数据
	void orderPayList(@Valid OrderPayInfo info)throws Exception;
	
	//查询支付数据通过Order_id
	List<OrderPayInfo> pay(@Valid OrderPayInfo info)throws Exception;
	
	//更新支付状态
	void usts(@Valid OrderPayInfo info)throws Exception;

	//已存在订单更新支付状态、重新生成支付数据(更改订单表、删除新增支付表)
	void deleteOrderPayList(@Param("orderId")String orderId)throws Exception;
	
	//查询支付订单号
	List<String> orderIDListPay(@Valid OrderPayInfo info) throws Exception;

}
