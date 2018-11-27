/*****************************************************************

* 模块名称：结算后台-DAO接口层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import java.util.List;

import javax.validation.Valid;

import com.aiqin.mgs.order.api.domain.*;

public interface SettlementDao {
	
//	SettlementInfo acountDiscount(String memberId);
//
//	SettlementInfo acountCoupon(String memberId);
//
//	SettlementInfo acountCart(String memberId);
//
//	void insertSettlement(SettlementInfo info);
//
//	void insertOrderPay(OrderPayInfo payInfo);

	List<SettlementInfo> jkselectsettlement(@Valid OrderQuery orderQuery);

	void addSettlement(SettlementInfo info) throws Exception; //添加新的结算数据
	

	
}
