/*****************************************************************

* 模块名称：订单明细后台-DAO接口层
* 开发人员: hzy
* 开发时间: 2018-11-08 

* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import java.util.List;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Param;

import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.request.OrderIdAndAmountRequest;
import com.aiqin.mgs.order.api.domain.request.ReorerRequest;
import com.aiqin.mgs.order.api.domain.response.OrderDetailByMemberResponse;
import com.aiqin.mgs.order.api.domain.response.OrderProductsResponse;
import com.aiqin.mgs.order.api.domain.response.ProdisorResponse;
import com.aiqin.mgs.order.api.domain.response.SkuSaleResponse;

public interface OrderDetailDao {

	//查询订单明细-订单明细编码
	OrderDetailInfo setail(@Valid OrderDetailInfo orderDetailInfo) throws Exception;
		
	//模糊查询订单明细列表......
	List<OrderDetailInfo> selectorderDetail(@Valid OrderDetailQuery orderDetailQuery)throws Exception;
		
	//查询订单明细部分汇总-（支持活动ID汇总、）
	OrderDetailInfo selectorderdetailsum(@Valid OrderDetailQuery orderDetailQuery)throws Exception;

	//接口--商品概览门店sku月销量、月销售额
	OrderDetailInfo productOverviewByMonth(@Param("distributorId")String distributorId,@Param("year")String year,@Param("month")String month) throws Exception;

	//接口--商品概览产品销量、销售额-前5名
	List<OrderDetailInfo> productOverviewByOrderTop(@Param("distributor_id")String distributor_id,@Param("year")String year,@Param("month")String month) throws Exception;

    //接口--商品概览产品销量、销售额-后5名
	List<OrderDetailInfo> productOverviewByOrderLast(@Param("distributor_id")String distributor_id,@Param("year")String year,@Param("month")String month) throws Exception;

	//查询订单明细Byorderid
	List<OrderDetailInfo> selectDetailById(@Valid OrderDetailQuery orderDetailQuery) throws Exception;

	//插入订单明细表
	void addDetailList(@Valid OrderDetailInfo orderDetailInfo) throws Exception;

//	//查询会员下的所有订单ID下的商品集合...
//	List<OrderProductsResponse> selectproductbyorders(@Valid List<String> orderidslList, @Param("memberId")String memberId)throws Exception;

	//修改订单明细退货数据
	void returnStatus(@Valid OrderDetailInfo info)throws Exception;

	//接口--会员管理-会员消费记录
	List<OrderDetailByMemberResponse> byMemberOrder(@Valid OrderDetailQuery orderDetailQuery)throws Exception;

	//接口-统计商品在各个渠道的订单数.
	List<ProdisorResponse> prodisor(@Valid OrderDetailQuery orderDetailQuery)throws Exception;

	//订单中商品sku数量
	Integer getSkuSum(OrderDetailQuery query)throws Exception;
	
	//删除订单明细数据
	void deleteOrderDetailInfo(OrderInfo orderInfo) throws Exception;

	//sku销量统计
	Integer getSkuAmount(@Param("skuCode")String skuCode,@Param("nextDate")String nextDate) throws Exception;

	//sku金额统计
	Integer getSkuPrice(@Param("skuCode")String skuCode,@Param("nextDate")String nextDate) throws Exception;

	List<SkuSaleResponse> selectSkuSale(@Valid List<String> orderList)throws Exception;

	//查询购买数量
	List<OrderIdAndAmountRequest> buyAmount(@Valid ReorerRequest reorerRequest)throws Exception;

}
