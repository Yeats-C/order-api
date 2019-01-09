/*****************************************************************

* 模块名称：订单日志后台-DAO接口层
* 开发人员: hzy
* 开发时间: 2018-11-08 

* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import java.util.List;

import javax.validation.Valid;

import com.aiqin.mgs.order.api.domain.*;

import io.lettuce.core.dynamic.annotation.Param;

public interface OrderLogDao {

	//插入订单日志表
	void addOrderLog(@Valid OrderLog logInfo) throws Exception;

	//查询订单日志表
	List<OrderLog> orog(@Param("orderId")String orderId)throws Exception;
}
