/*****************************************************************

* 模块名称：挂单解挂后台-接口层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service;

import java.util.List;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.FrozenInfo;

@SuppressWarnings("all")
public interface FrozenService {

	
    HttpResponse addFrozenInfo(List<FrozenInfo> frozenInfolist);  //挂单入库

    HttpResponse selectDetailByFrozenId(String frozenId);   //查询挂单明细

	HttpResponse deleteByFrozenId(String frozenId);  //解卦
	
	HttpResponse selectSumByFrozenId(String saleById,String distributorId);  //查询挂单汇总

}
