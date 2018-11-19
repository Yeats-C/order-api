/*****************************************************************

* 模块名称：购物车后台-接口层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service;

import java.util.List;

import javax.validation.Valid;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleInfo;
import com.aiqin.mgs.order.api.domain.OrderInfo;

@SuppressWarnings("all")
public interface OrderAfterService {

	HttpResponse selectOrderAfter(@Valid OrderAfterSaleInfo orderAfterInfo);

	HttpResponse selectOrderAfterDetail(String afterSaleId);



}
