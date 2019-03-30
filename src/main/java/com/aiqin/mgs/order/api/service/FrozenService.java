/*****************************************************************

* 模块名称：挂单解挂后台-接口层
* 开发人员: hzy
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service;

import java.util.List;

import javax.validation.Valid;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.FrozenInfo;

@SuppressWarnings("all")
public interface FrozenService {

	// 挂单入库
	HttpResponse addFrozenInfo(List<FrozenInfo> frozenInfolist);

	// 查询挂单明细
	HttpResponse selectDetailByFrozenId(String frozenId);

	// 解卦
	HttpResponse deleteByFrozenId(String frozenId);

	// 查询挂单汇总
	HttpResponse selectSumByFrozenId(String createBy, String distributorId);

	HttpResponse selectDetail(String createBy, String distributorId);

	//查询挂单数量
	HttpResponse selectSumByParam(@Valid String createBy, String distributorId);
}
