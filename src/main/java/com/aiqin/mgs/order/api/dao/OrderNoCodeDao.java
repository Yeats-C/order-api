/*****************************************************************

* 模块名称：服务订单后台-DAO接口层
* 开发人员: hzy
* 开发时间: 2019-02-15 

* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.request.DevelRequest;
import com.aiqin.mgs.order.api.domain.request.MemberByDistributorRequest;
import com.aiqin.mgs.order.api.domain.request.OrderNoCodeRequest;
import com.aiqin.mgs.order.api.domain.request.ReorerRequest;
import com.aiqin.mgs.order.api.domain.response.OrderResponse;
import com.aiqin.mgs.order.api.domain.response.OrderbyReceiptSumResponse;
import com.aiqin.mgs.order.api.domain.response.SelectByMemberPayCountResponse;
import com.aiqin.mgs.order.api.domain.response.OrderNoCodeResponse.SelectSaleViewResonse;
import com.aiqin.mgs.order.api.domain.response.LastBuyResponse;
import com.aiqin.mgs.order.api.domain.response.MevBuyResponse;
import com.aiqin.mgs.order.api.domain.response.OradskuResponse;

import io.lettuce.core.dynamic.annotation.Param;

public interface OrderNoCodeDao {

	//总销售金额
	Integer getIncomePrice(@Valid @Param("distributorId")String distributorId,@Param("beginDate")Date beginDate,@Param("endDate")Date endDate);
	//总退次金额
	Integer getReturnPrice(@Valid @Param("distributorId")String distributorId,@Param("beginDate")Date beginDate,@Param("endDate")Date endDate);
	//总销量
	Integer getIncomeCount(@Valid @Param("distributorId")String distributorId,@Param("beginDate")Date beginDate,@Param("endDate")Date endDate);
	//总退货量
	Integer getReturnCount(@Valid @Param("distributorId")String distributorId,@Param("beginDate")Date beginDate,@Param("endDate")Date endDate);
	//当月客流量
	Integer getPassengerFlow(@Valid @Param("distributorId")String distributorId,@Param("beginDate")Date beginDate,@Param("endDate")Date endDate);
	//昨日订单的当天退货金额
	Integer getYesterdayReturnPrice(@Valid @Param("distributorId")String distributorId,@Param("beginDate")Date beginDate,@Param("endDate")Date endDate);
	//昨日订单的当天退货量
	Integer getYesterdayReturnCount(@Valid @Param("distributorId")String distributorId,@Param("beginDate")Date beginDate,@Param("endDate")Date endDate);
	
	//订单所涉及到的商品类别
    List<SelectSaleViewResonse> getTypeId(@Valid @Param("distributorId")String distributorId,@Param("beginDate")Date beginDate,@Param("endDate")Date endDate);
	//订单的销售额
	List<SelectSaleViewResonse> getIncomePriceGroupByTypeId(@Valid @Param("distributorId")String distributorId,@Param("beginDate")Date beginDate,@Param("endDate")Date endDate);
	//订单的退货金额
	List<SelectSaleViewResonse> getReturnPriceGroupByTypeId(@Valid @Param("distributorId")String distributorId,@Param("beginDate")Date beginDate,@Param("endDate")Date endDate);
	//订单销量
	List<SelectSaleViewResonse> getIncomeCountGroupByTypeId(@Valid @Param("distributorId")String distributorId,@Param("beginDate")Date beginDate,@Param("endDate")Date endDate);
	//订单的退货量
	List<SelectSaleViewResonse> getReturnCountGroupByTypeId(@Valid @Param("distributorId")String distributorId,@Param("beginDate")Date beginDate,@Param("endDate")Date endDate);
	//客流量
	Integer getPassengerFlowGroupByTypeId(@Valid @Param("distributorId")String distributorId,@Param("beginDate")Date beginDate,@Param("endDate")Date endDate,@Param("typeId")String typeId);

//	//购买订单列表
//	List<OrderNoCodeInfo> selectNoCodeOrderList(@Valid OrderNoCodeRequest orderNoCodeBuyRequest) throws Exception;
//	//订单列表总数据条数
//	Integer selectNoCodeOrderListCount(@Valid OrderNoCodeRequest orderNoCodeBuyRequest)throws Exception;
//	//退货订单列表
//	List<OrderNoCodeInfo> selectNoCodeReturnList(@Valid OrderNoCodeRequest orderNoCodeBuyRequest)throws Exception;
//	//退货订单列表总数据条数
//	Integer selectNoCodeReturnListCount(@Valid OrderNoCodeRequest orderNoCodeBuyRequest)throws Exception;
}
