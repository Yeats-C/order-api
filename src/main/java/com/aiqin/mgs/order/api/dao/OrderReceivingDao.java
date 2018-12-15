/*****************************************************************

* 模块名称：订单收货后台-DAO接口层
* 开发人员: hzy
* 开发时间: 2018-11-08 

* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import javax.validation.Valid;

import com.aiqin.mgs.order.api.domain.OrderDetailQuery;
import com.aiqin.mgs.order.api.domain.OrderReceivingInfo;

public interface OrderReceivingDao {

	//查询订单收货信息表byOrderid
	OrderReceivingInfo selecReceivingById(@Valid OrderDetailQuery orderDetailQuery) throws Exception;


	
}
