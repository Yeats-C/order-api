/*****************************************************************

* 模块名称：订单售后后台-接口层
* 开发人员: hzy
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service;

import java.util.List;

import javax.validation.Valid;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.OrderAfteIListInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleQuery;
import com.aiqin.mgs.order.api.domain.OrderInfo;

@SuppressWarnings("all")
public interface OrderAfterService {

	//模糊查询售后维权列表 /条件查询退货信息
	HttpResponse selectOrderAfter(@Valid OrderAfterSaleQuery orderAfterSaleQuery);

	//添加新的订单售后数据+订单售后明细数据+修改订单表+修改订单明细表....
	HttpResponse addAfterOrder(@Valid OrderAfterSaleInfo orderAfterSaleInfo);

	//返回订单订单数据、退货数据、退货明细数据....
	HttpResponse selectDetail(@Valid String afterSaleId);

	//更改退货状态(售后明细表+售后表+订单表+订单明细表) 扩展
//	HttpResponse returus(@Valid String afterSaleId, Integer afterSaleStatus, String updateBy);
	
	//更改退货状态(售后表)
	HttpResponse returus(@Valid String afterSaleId, Integer afterSaleStatus, String updateBy);

}
