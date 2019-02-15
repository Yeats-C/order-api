/*****************************************************************

* 模块名称：订单后台-接口层
* 开发人员: hzy
* 开发时间: 2018-11-05 

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
import com.aiqin.mgs.order.api.domain.request.ReorerRequest;

@SuppressWarnings("all")
public interface OrderService {

	
	HttpResponse selectOrder(@Valid OrderQuery OrderQuery);
	
	//导出订单列表
	HttpResponse exorder(@Valid OrderQuery OrderQuery);

	HttpResponse addOrderLog(@Valid OrderLog logInfo);

	OrderInfo addOrderInfo(@Valid OrderInfo orderInfo) throws Exception;
	
	//添加新的订单优惠券关系表数据
	void addOrderCoupon(@Valid List<OrderRelationCouponInfo> orderCouponList, @Valid String orderId)throws Exception;

	//接口-分销机构维度-总销售额  废弃
//	HttpResponse selectOrderAmt(String distributorId, String originType);

	//接口-分销机构+当月维度-当月销售额、当月实收、当月支付订单数
	HttpResponse selectorderbymonth(@Valid String distributorId,@Valid List<Integer> originTypeList);

	//接口-订单概览-分销机构、小于当前日期9天内的实付金额、订单数量
	HttpResponse selectOrderByNineDate(@Valid String distributorId,@Valid List<Integer> originTypeList);

	//接口-订单概览-分销机构、小于当前日期9周内的实付金额、订单数量
	HttpResponse selectOrderByNineWeek(@Valid String distributorId,@Valid List<Integer> originTypeList);

	//接口-订单概览-分销机构、小于当前日期9个月内的实付金额、订单数量
	HttpResponse selectOrderByNineMonth(@Valid String distributorId,@Valid List<Integer> originTypeList);
	
	//添加新的订单主数据以及其他订单关联数据
	HttpResponse addOrderList(@Valid OrderAndSoOnRequest orderAndSoOnRequest);
	
	//添加订单主数据+添加订单明细数据+返回订单编号
	HttpResponse addOrdta(@Valid OrderAndSoOnRequest orderAndSoOnRequest);
	
	//添加结算数据+添加支付数据+添加优惠关系数据+修改订单主数据+修改订单明细数据
    HttpResponse addPamo(@Valid OrderAndSoOnRequest orderAndSoOnRequest);

	//接口-关闭订单
	HttpResponse closeorder(@Valid String orderId, String updateBy);

	//接口-更新商户备注
	HttpResponse updateorderbusinessnote(@Valid String orderId, String updateBy, String businessNote);

	//更改订单状态/支付状态/修改员...
	HttpResponse updateOrderStatus(@Valid String orderId, Integer orderStatus, Integer payStatus,
			String updateBy);

	//仅更改退货状态-订单主表
	void retustus(@Valid String orderId, Integer returnStatus, String updateBy)throws Exception;

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

	//删除订单主数据+删除订单明细数据
	HttpResponse deordta(@Valid String orderId);

	//微商城-销售总览
	HttpResponse wssev(@Valid String distributorId);

	//微商城-事务总览
	HttpResponse wsswv(@Valid String distributorId);

	//已存在订单更新支付状态、重新生成支付数据(更改订单表、删除新增支付表)
	HttpResponse repast(@Valid String orderId, @Valid String payType, @Valid List<OrderPayInfo> orderPayList);

	//销售目标管理-分销机构-月销售额
	HttpResponse selectDistributorMonth(@Valid DistributorMonthRequest detailCouponRequest);

	//会员活跃情况-通过当前门店,等级会员list、 统计订单使用的会员数、日周月.
	HttpResponse selectByMemberPayCount(@Valid String distributorId, @Valid Integer dateType);

    //判断会员是否在当前门店时候有过消费记录
	HttpResponse selectMemberByDistributor(@Valid MemberByDistributorRequest memberByDistributorRequest);
}
