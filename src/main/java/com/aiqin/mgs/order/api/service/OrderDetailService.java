/*****************************************************************

* 模块名称：订单明细后台-接口层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailQuery;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.domain.request.ProdisorRequest;
import com.aiqin.mgs.order.api.domain.response.SkuSaleResponse;

@SuppressWarnings("all")
public interface OrderDetailService {

	HttpResponse selectorderDetail(@Valid OrderDetailQuery orderDetailQuery);

	//查询订单明细部分汇总-（支持活动ID汇总、）
	HttpResponse selectorderdetailsum(@Valid OrderDetailQuery orderDetailQuery);

	//商品概览菜单-月销量、月销售额(产品确认：统计维度为订单)
	HttpResponse productOverviewByMonth(@Valid String distributorId,String year, String month);

	//接口--商品概览产品销量、销售额-前5名
	HttpResponse productOverviewByOrderTop(@Valid String distributor_id,String year, String month);
	
    //接口--商品概览产品销量、销售额-后5名
	HttpResponse productOverviewByOrderLast(@Valid String distributor_id,String year, String month);

	//接口--会员管理-会员消费记录
	HttpResponse byMemberOrder(OrderDetailQuery orderDetailQuery); 

	//查询BYorderid-返回订单明细数据、订单数据、收货信息
	HttpResponse selectorderany(@Valid String orderId);

	//添加新的订单明细数据
	public List<OrderDetailInfo> addDetailList(@Valid List<OrderDetailInfo> detailList, @Valid String orderId,@Valid String orderCode) throws Exception; 

//	//查询会员下的所有订单ID下的商品集合...
//	HttpResponse selectproductbyorders(@Valid List<String> orderidslList, @Valid String memberId); 

	//查询会员下的全部订单 返回订单主数据+订单详细列表
	HttpResponse selectorderdbumemberid(@Valid String memberId, @Valid Integer orderStatus, @Valid String pageSize, @Valid String pageNo);  

    //修改订单明细退货数据
	void returnStatus(@Valid String orderDetailId,Integer returnStatus,Integer returnAmount, String updateBy)throws Exception;

	//接口-统计商品在各个渠道的订单数.
	HttpResponse prodisor(@Valid ProdisorRequest info);

	//订单中商品sku数量
	Integer getSkuSum(@Valid String orderId);

	//sku销量统计
	HttpResponse skuSum(@Valid List<String> sukList);

	//批量添加sku销量
	HttpResponse saveBatch(@Valid List<String> sukList);

	//查询BYordercode-返回订单明细数据、订单数据、收货信息、结算数据
	HttpResponse selectorderSelde(@Valid String orderCode);
	
	//查询SKU+销量
	List<SkuSaleResponse> selectSkuSale(@Valid List<String> orderList);

	//顾客可能还想购买
	HttpResponse wantBuy(@Valid List<String> sukList);


}
