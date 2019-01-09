/*****************************************************************

* 模块名称：结算后台-DAO接口层
* 开发人员: hzy
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import javax.validation.Valid;

import com.aiqin.mgs.order.api.domain.*;

public interface SettlementDao {
	
	//查询结算信息
	SettlementInfo jkselectsettlement(@Valid OrderQuery orderQuery)throws Exception; 

	//添加新的结算数据
	void addSettlement(SettlementInfo info) throws Exception; 
	
}
