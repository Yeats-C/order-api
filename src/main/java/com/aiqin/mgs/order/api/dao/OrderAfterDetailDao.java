/*****************************************************************

* 模块名称：订单售后后台-DAO接口层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-08 

* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import java.util.List;

import com.aiqin.mgs.order.api.domain.*;

import io.lettuce.core.dynamic.annotation.Param;

public interface OrderAfterDetailDao {

	//通过ID查询退货明细数据-带分页
    List<OrderAfterSaleDetailInfo> selectOrderAfterDetail(OrderAfterSaleQuery orderAfterSaleQuery) throws Exception;
    
    //通过ID查询退货明细数据-不带分页
    List<OrderAfterSaleDetailInfo> selectDetailbyId(OrderAfterSaleQuery orderAfterSaleQuery) throws Exception;

    void addAfterOrderDetail(OrderAfterSaleDetailInfo info) throws Exception;

}
