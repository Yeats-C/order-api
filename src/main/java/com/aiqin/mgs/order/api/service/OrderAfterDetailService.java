/*****************************************************************

* 模块名称：订单售后明细后台-接口层
* 开发人员: hzy
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service;

import java.util.List;

import javax.validation.Valid;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleQuery;
import com.aiqin.mgs.order.api.domain.OrderInfo;

@SuppressWarnings("all")
public interface OrderAfterDetailService {

	HttpResponse addAfterOrderDetail(@Valid List<OrderAfterSaleDetailInfo> afterDetailList, String afterSaleId);

	HttpResponse OrderAfteIList(@Valid OrderAfterSaleQuery orderAfterSaleQuery);




}
