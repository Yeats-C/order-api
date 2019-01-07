/*****************************************************************

* 模块名称：日志后台-接口层
* 开发人员: hzy
* 开发时间: 2018-12-11 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service;

import java.util.List;

import javax.validation.Valid;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailQuery;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.OrderLog;
import com.aiqin.mgs.order.api.domain.OrderPayInfo;
import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.domain.OrderRelationCouponInfo;
import com.aiqin.mgs.order.api.domain.SettlementInfo;
import com.aiqin.mgs.order.api.domain.request.OrderAndSoOnRequest;
import com.aiqin.mgs.order.api.domain.request.ReorerRequest;

@SuppressWarnings("all")
public interface OrderLogService {

    //添加日志
	public void addOrderLog(@Valid OrderLog logInfo) throws Exception;

	//查询日志
	HttpResponse orog(@Valid String orderId);

}
