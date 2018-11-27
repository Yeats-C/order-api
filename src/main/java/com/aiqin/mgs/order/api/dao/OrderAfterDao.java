/*****************************************************************

* 模块名称：订单后台-DAO接口层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-08 

* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import java.util.List;

import javax.validation.Valid;

import com.aiqin.mgs.order.api.domain.*;

public interface OrderAfterDao {

	//模糊查询售后维权列表 /条件查询退货信息
	List<OrderAfterSaleInfo> selectOrderAfter(@Valid OrderAfterSaleQuery orderAfterSaleQuery) throws Exception;

	//添加新的订单售后数据
	void addAfterOrder(@Valid OrderAfterSaleInfo orderAfterSaleInfo)throws Exception;

    //ID查询售后主信息
    OrderAfterSaleInfo selectOrderAfterById(@Valid OrderAfterSaleQuery orderAfterSaleQuery) throws Exception;

	
}
