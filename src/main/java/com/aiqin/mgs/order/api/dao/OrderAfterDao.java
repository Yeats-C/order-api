/*****************************************************************

* 模块名称：订单售后后台-DAO接口层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-08 

* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import java.util.List;

import javax.validation.Valid;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.*;



public interface OrderAfterDao {

    
    //模糊查询售后维权列表 /条件查询退货信息 带分页
    List<OrderAfterSaleInfo> selectOrderAfter(@Valid OrderAfterSaleQuery orderAfterSaleQuery) throws Exception;

    //添加新的订单售后数据
    void addAfterOrder(@Valid OrderAfterSaleInfo orderAfterSaleInfo)throws Exception;

    //通过ID查询售后主信息
    OrderAfterSaleInfo selectOrderAfterById(@Valid OrderAfterSaleQuery orderAfterSaleQuery) throws Exception;

    //个性化接口-通过ID更改售后表退货状态/修改员 
	void returnStatus(OrderAfterSaleInfo orderAfterSaleInfo)throws Exception;
    
}
