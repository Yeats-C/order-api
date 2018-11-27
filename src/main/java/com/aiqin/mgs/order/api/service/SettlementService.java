/*****************************************************************

* 模块名称：结算后台-接口层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.FrozenInfo;
import com.aiqin.mgs.order.api.domain.OrderPayInfo;
import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.domain.SettlementInfo;

@SuppressWarnings("all")
public interface SettlementService {

	
//	HttpResponse selectSettlement(List<CartInfo> cartInfo);    //結算頁面列表
//
//	HttpResponse settlement(SettlementInfo info,List<CartInfo> cartInfoList, OrderQuery conditionInfo); //去结账
//
//	HttpResponse ajaxsettlement(@Valid SettlementInfo settlementInfo);  //结算页面动态刷新元素

	HttpResponse jkselectsettlement(@Valid OrderQuery orderQuery); //结算数据查询

	HttpResponse addSettlement(@Valid SettlementInfo settlementInfo, @Valid String orderId); //添加新的结算数据

	HttpResponse addOrderPayList(@Valid List<OrderPayInfo> orderPayList, @Valid String orderId);//添加新的支付数据

}
