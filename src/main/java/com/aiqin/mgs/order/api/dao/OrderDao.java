/*****************************************************************

* 模块名称：订单后台-DAO接口层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-08 

* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import java.util.List;

import javax.validation.Valid;

import com.aiqin.mgs.order.api.domain.*;

public interface OrderDao {

	
	//订单查询
    List<OrderInfo> selectOrder(OrderInfo orderInfo);
    //订单明细查询
  	List<OrderDetailInfo> selectOrderSKUId(String string);
		
	//插入订单表
	void insertOrder(OrderInfo orderInfo);
	//插入订单明细表
	void insertOrderDetail(OrderDetailInfo orderDetailInfo);
	//插入订单日志表
	void inserOrderLog(SettlementInfo info);
    
	
	
	//导出
	//根据门店查询订单列表
	
	//查询订单详细列表-带结算、退货信息......
	void selectorderDetail(@Valid OrderInfo orderInfo);
	
	
	
	

	
}
