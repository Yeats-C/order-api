/*****************************************************************

* 模块名称：订单后台-DAO接口层
* 开发人员: hzy
* 开发时间: 2018-11-08 

* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.request.DevelRequest;
import com.aiqin.mgs.order.api.domain.request.ReorerRequest;
import com.aiqin.mgs.order.api.domain.response.OrderResponse;
import com.aiqin.mgs.order.api.domain.response.OrderbyReceiptSumResponse;
import com.aiqin.mgs.order.api.domain.response.SelectByMemberPayCountResponse;
import com.aiqin.mgs.order.api.domain.response.LastBuyResponse;
import com.aiqin.mgs.order.api.domain.response.MevBuyResponse;
import com.aiqin.mgs.order.api.domain.response.OradskuResponse;

import io.lettuce.core.dynamic.annotation.Param;

public interface OrderDao {

	
	//订单查询
    List<OrderInfo> selectOrder(OrderQuery OrderQuery) throws Exception;
    
    //唯一订单查询
    OrderInfo selecOrderById(OrderDetailQuery orderDetailQuery) throws Exception;
    
    //添加新的订单主数据
    public void addOrderInfo(@Valid OrderInfo orderInfo) throws Exception;

    //接口-分销机构维度-总销售额 返回INT
	Integer selectOrderAmt(@Param("distributorId")String distributorId,@Param("originTypeList")List<Integer> originTypeList)throws Exception;

	//接口-分销机构+当月维度-当月销售额
	Integer selectByMonthAllAmt(@Param("distributorId")String distributorId, @Param("originTypeList")List<Integer> originTypeList,@Param("yearMonth")String yearMonth)throws Exception;

    //接口-分销机构+当月维度-当月实收
	Integer selectbByMonthRetailAmt(@Param("distributorId")String distributorId, @Param("originTypeList")List<Integer> originTypeList,@Param("yearMonth")String yearMonth)throws Exception;

    //接口-分销机构+当月维度-当月支付订单数
	Integer selectByMonthAcount(@Param("distributorId")String distributorId, @Param("originTypeList")List<Integer> originTypeList,@Param("yearMonth")String yearMonth)throws Exception;

	//接口-分销机构+日维度-日销售额
	Integer selectByYesdayAllAmt(@Param("distributorId")String distributorId,@Param("originTypeList")List<Integer> originTypeList,@Param("yesterday")String yesterday)throws Exception;

    //接口-分销机构+日维度-日实收
	Integer selectbByYesdayRetailAmt(@Param("distributorId")String distributorId, @Param("originTypeList")List<Integer> originTypeList,@Param("yesterday")String yesterday)throws Exception;

    //接口-分销机构+日维度-日支付订单数
	Integer selectByYesdayAcount(@Param("distributorId")String distributorId, @Param("originTypeList")List<Integer> originTypeList,@Param("yesterday")String yesterday)throws Exception;

	//接口-订单概览-分销机构、小于当前日期9天内的实付金额、订单数量
	List<OrderResponse> selectOrderByNineDate(@Valid OrderQuery OrderQuery)throws Exception;

	//接口-订单概览-分销机构、小于当前日期9个月内的实付金额、订单数量
	List<OrderResponse> selectOrderByNineMonth(@Valid OrderQuery orderQuery)throws Exception;
	
	//接口-订单概览-分销机构、小于当前日期9周内的实付金额、订单数量
	OrderResponse selectOrderByNineWeek(@Valid OrderQuery orderQuery)throws Exception;
	
	//接口-关闭订单
	void closeOrder(@Param("orderId")String orderId, @Param("updateBy")String updateBy)throws Exception;

	//接口-更新商户备注
	void updateorderbusinessnote(@Valid OrderInfo orderInfo)throws Exception;

	//更改订单状态/支付状态/支付方式/修改员...
	void updateOrderStatus(@Valid OrderInfo orderInfo)throws Exception;

	//仅更改退货状态-订单主表
	void retustus(@Valid OrderInfo orderInfo)throws Exception;

	//修改订单主数据
	void updateOrderInfo(@Valid OrderInfo orderInfo) throws Exception;

	//仅变更订单状态
	void onlyStatus(OrderInfo orderInfo);

	//接口-收银员交班收银情况统计   获取收银员、支付类型金额
	List<OrderbyReceiptSumResponse> cashier(OrderQuery orderQuery)throws Exception;

	//接口-收银员交班收银情况统计  获取退款金额、退款订单数、销售额、销售订单数
	OrderbyReceiptSumResponse byCashierSum(OrderQuery orderQuery)throws Exception;

	//接口-通过会员查询最后一次的消费记录.
	List<LastBuyResponse> last(@Valid OrderInfo orderInfo)throws Exception;

	//将提货码插入订单表中
	void rede(OrderQuery orderQuery)throws Exception;

	//接口-可退货的订单查询
	List<OrderInfo> reorer(@Valid ReorerRequest reorerRequest)throws Exception;

	//接口-注销提货码
	void reded(@Valid OrderQuery orderQuery)throws Exception;
	
	//未付款订单30分钟后自动取消
	List<String> nevder()throws Exception;

	//提货码10分钟后失效.
	List<String> nevred()throws Exception;

	//接口-通过当前门店,等级会员list、 统计订单使用的会员数、返回7天内的统计.
	List<DevelRequest> devel(@Valid DevelRequest develRequest) throws Exception;

	//模糊查询订单列表+订单中商品sku数量分页
	List<OradskuResponse> selectskuResponse(OrderQuery orderQuery) throws Exception;

	//删除订单主数据
	void deleteOrderInfo(@Valid OrderInfo orderInfo) throws Exception;

	//订单数
	Integer selectAcountByEcshop(@Valid OrderQuery orderQuery) throws Exception;

	//订单金额
	Integer selectAmountByEcshop(OrderQuery orderQuery) throws Exception;

	//成交客户
	Integer selectMembersByEcshop(OrderQuery orderQuery) throws Exception;

	//根据状态查询订单
	Integer selectCountByStatus(OrderQuery orderQuery) throws Exception;

	//修改订单支付方式信息
	void onlyPayType(OrderQuery orderQuery)throws Exception;

	//通过订单编码查询订单ID
	String getOrderIdByCode(@Valid @Param("orderCode") String orderCode)throws Exception;

	Integer selectOrderCount(OrderQuery orderQuery)throws Exception;

	//销售目标管理-分销机构-月销售额
	Integer selectDistributorMonth(@Valid @Param("distributorCode")String distributorCode,@Valid @Param("beginTime")String beginTime,@Valid @Param("endTime")String endTime)throws Exception;

	//会员活跃情况-通过当前门店,等级会员list、 统计订单使用的会员数、日
	Integer selectByMemberPayCountDay(SelectByMemberPayCountResponse info)throws Exception;

	//会员活跃情况-通过当前门店,等级会员list、 统计订单使用的会员数、周
	Integer selectByMemberPayCountWeek(@Valid @Param("countDate")Integer countDate)throws Exception;
	
	//会员活跃情况-通过当前门店,等级会员list、 统计订单使用的会员数、月
	Integer selectByMemberPayCountMonth(SelectByMemberPayCountResponse info)throws Exception;

	

	
}
