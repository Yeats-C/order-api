/*****************************************************************

* 模块名称：订单优惠券关系表后台-DAO接口层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-08 

* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderRelationCouponInfo;

public interface OrderCouponDao {

	//插入订单优惠券关系表
	void addOrderCoupon(OrderRelationCouponInfo info) throws Exception;
}
