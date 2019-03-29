/*****************************************************************

* 模块名称：结算后台-接口层
* 开发人员: hzy
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

	//结算数据查询
	HttpResponse jkselectsettlement(@Valid OrderQuery orderQuery); 

	//添加新的结算数据
	public void addSettlement(@Valid SettlementInfo settlementInfo, @Valid String orderId)throws Exception; 

	//添加新的支付数据
	public void addOrderPayList(@Valid List<OrderPayInfo> orderPayList, @Valid String orderId,@Valid String orderCode)throws Exception;
	
	//删除支付数据
    public void deleteOrderPayList(@Valid String orderId)throws Exception;

	//查询支付数据通过
	HttpResponse pay(@Valid String orderId);

}
