/*****************************************************************

* 模块名称：购物车后台-DAO接口层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.*;

public interface SettlementDao {
	
	//未完成 异常

	SettlementInfo acountDiscount(String memberId);

	SettlementInfo acountCoupon(String memberId);

	SettlementInfo acountCart(String memberId);

	void insertSettlement(SettlementInfo info);

	void insertOrderPay(OrderPayInfo payInfo);
	

	
}
