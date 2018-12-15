/*****************************************************************

* 模块名称：订单后台-接口层
* 开发人员: hzy
* 开发时间: 2018-11-05 

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
public interface OrderService {

	
	HttpResponse selectOrder(@Valid OrderQuery OrderQuery);

	HttpResponse addOrderLog(@Valid OrderLog logInfo);

	String addOrderInfo(@Valid OrderInfo orderInfo) throws Exception;
	
	//添加新的订单优惠券关系表数据
	void addOrderCoupon(@Valid List<OrderRelationCouponInfo> orderCouponList, @Valid String orderId)throws Exception;

	//接口-分销机构维度-总销售额
	HttpResponse selectOrderAmt(String distributorId, String originType);

	//接口-分销机构+当月维度-当月销售额、当月实收、当月支付订单数
	HttpResponse selectorderbymonth(@Valid String distributorId, String originType);

	//接口-订单概览-分销机构、小于当前日期9天内的实付金额、订单数量
	HttpResponse selectOrderByNineDate(@Valid String distributorId, String originType);

	//接口-订单概览-分销机构、小于当前日期9周内的实付金额、订单数量
	HttpResponse selectOrderByNineWeek(@Valid String distributorId, String originType);

	//接口-订单概览-分销机构、小于当前日期9个月内的实付金额、订单数量
	HttpResponse selectOrderByNineMonth(@Valid String distributorId, String originType);
	
	//添加新的订单主数据以及其他订单关联数据
	HttpResponse addOrderList(@Valid OrderAndSoOnRequest orderAndSoOnRequest);

	//接口-关闭订单
	HttpResponse closeorder(@Valid String orderId, String updateBy);

	//接口-更新商户备注
	HttpResponse updateorderbusinessnote(@Valid String orderId, String updateBy, String businessNote);

	//更改订单状态/支付状态/修改员...
	HttpResponse updateOrderStatus(@Valid String orderId, Integer orderStatus, Integer payStatus,
			String updateBy);

	//仅更改退货状态-订单主表
	HttpResponse retustus(@Valid String orderId, Integer returnStatus, String updateBy);

	//仅变更订单状态
	HttpResponse onlyStatus(@Valid String orderId, Integer orderStatus, String updateBy);

	//接口-收银员交班收银情况统计
	HttpResponse cashier(@Valid String cashierId, String beginTime, String endTime);

	//接口-通过会员查询最后一次的消费记录.
	HttpResponse last(@Valid String memberId);

	//接口-生成提货码
	HttpResponse rede(@Valid String orderId);
	
	//接口-注销提货码
	HttpResponse reded(@Valid String orderId);

	//接口-可退货的订单查询
	HttpResponse reorer(@Valid ReorerRequest reorerRequest);

	//未付款订单30分钟后自动取消
	List<String> nevder();

	//提货码10分钟后失效.
	List<String> nevred();

	//接口-通过当前门店,等级会员list、 统计订单使用的会员数、返回7天内的统计.
	HttpResponse devel(@Valid String distributor_id, @Valid List<String> detailList);

	//模糊查询订单列表+订单中商品sku数量
	HttpResponse oradsku(@Valid OrderQuery orderQuery);

	//查询订单日志数据
	HttpResponse orog(@Valid String orderId);
}
