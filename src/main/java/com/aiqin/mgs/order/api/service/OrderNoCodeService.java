/*****************************************************************

* 模块名称：服务订单后台-接口层
* 开发人员: hzy
* 开发时间: 2019-02-15 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

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
import com.aiqin.mgs.order.api.domain.request.DetailCouponRequest;
import com.aiqin.mgs.order.api.domain.request.DistributorMonthRequest;
import com.aiqin.mgs.order.api.domain.request.MemberByDistributorRequest;
import com.aiqin.mgs.order.api.domain.request.OrderAndSoOnRequest;
import com.aiqin.mgs.order.api.domain.request.OrderNoCodeRequest;
import com.aiqin.mgs.order.api.domain.request.ReorerRequest;

@SuppressWarnings("all")
public interface OrderNoCodeService {

	//订单概览统计
	HttpResponse selectSumByStoreId(@Valid String distributorId);

	//服务销售概况
	HttpResponse selectSaleView(@Valid String distributorId, @Valid String beginDate, @Valid String endDate);

//	//订单列表
//	HttpResponse<List<OrderInfo>> selectNoCodeList(@Valid OrderNoCodeRequest orderNoCodeBuyRequest);

	//编号查询订单.
	HttpResponse selectorderByCode(@Valid String orderCode);

	}
