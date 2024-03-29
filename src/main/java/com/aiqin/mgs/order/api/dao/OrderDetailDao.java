/*****************************************************************

 * 模块名称：订单明细后台-DAO接口层
 * 开发人员: hzy
 * 开发时间: 2018-11-08

 * ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailQuery;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.request.OrderIdAndAmountRequest;
import com.aiqin.mgs.order.api.domain.request.ReorerRequest;
import com.aiqin.mgs.order.api.domain.response.OrderDetailByMemberResponse;
import com.aiqin.mgs.order.api.domain.response.ProdisorResponse;
import com.aiqin.mgs.order.api.domain.response.ProductRespVO;
import com.aiqin.mgs.order.api.domain.response.SkuSaleResponse;
import org.apache.ibatis.annotations.Param;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

public interface OrderDetailDao {

    //查询订单明细-订单明细编码
    OrderDetailInfo setail(@Valid OrderDetailInfo orderDetailInfo) throws Exception;

    //模糊查询订单明细列表......
    List<OrderDetailInfo> selectorderDetail(@Valid OrderDetailQuery orderDetailQuery) throws Exception;

    //查询订单明细部分汇总-（支持活动ID汇总、）
    OrderDetailInfo selectorderdetailsum(@Valid OrderDetailQuery orderDetailQuery) throws Exception;

//	//接口--商品概览门店sku月销量、月销售额
//	OrderDetailInfo productOverviewByMonth(@Param("distributorId")String distributorId,@Param("dayBegin")Date dayBegin,@Param("dayEnd")Date dayEnd) throws Exception;

    //接口--商品概览产品销量、销售额-前5名
    List<OrderDetailInfo> productOverviewByOrderTop(@Param("distributorId") String distributorId, @Param("dayBegin") Date dayBegin, @Param("dayEnd") Date dayEnd) throws Exception;

    //接口--商品概览产品销量、销售额-后5名
    List<OrderDetailInfo> productOverviewByOrderLast(@Param("distributorId") String distributorId, @Param("dayBegin") Date dayBegin, @Param("dayEnd") Date dayEnd) throws Exception;

    //查询订单明细Byorderid
    List<OrderDetailInfo> selectDetailById(@Valid OrderDetailQuery orderDetailQuery) throws Exception;
    List<OrderDetailInfo> selectDetailPreById(@Valid OrderDetailQuery orderDetailQuery) throws Exception;

	//接口--商品概览产品销量、销售额-前5名
	List<OrderDetailInfo> productFrontTop10(@Param("distributorId")String distributorId,@Param("dayBegin")Date dayBegin,@Param("dayEnd")Date dayEnd,@Param("categoryId") String categoryId) throws Exception;

    //接口--商品概览产品销量、销售额-后5名
	List<OrderDetailInfo> productAfterTop10(@Param("distributorId")String distributorId,@Param("dayBegin")Date dayBegin,@Param("dayEnd")Date dayEnd,@Param("categoryId") String categoryId) throws Exception;

    //插入订单明细表
    void addDetailList(@Valid OrderDetailInfo orderDetailInfo) throws Exception;

    //插入订单明细表
    void addDetailPreList(@Valid OrderDetailInfo orderDetailInfo) throws Exception;
//	//查询会员下的所有订单ID下的商品集合...
//	List<OrderProductsResponse> selectproductbyorders(@Valid List<String> orderidslList, @Param("memberId")String memberId)throws Exception;

    //修改订单明细退货数据
    void returnStatus(@Valid OrderDetailInfo info) throws Exception;

    //接口--会员管理-会员消费记录
    List<OrderDetailByMemberResponse> byMemberOrder(@Valid OrderDetailQuery orderDetailQuery) throws Exception;

    //接口-统计商品在各个渠道的订单数.4
    List<ProdisorResponse> prodisor(@Valid OrderDetailQuery orderDetailQuery) throws Exception;

    //订单中商品sku数量
    Integer getSkuSum(OrderDetailQuery query) throws Exception;

    //删除订单明细数据
    void deleteOrderDetailInfo(OrderInfo orderInfo) throws Exception;
    //删除订单明细数据
    void deleteOrderDetailInfoPre(OrderInfo orderInfo) throws Exception;

    //sku销量统计
    Integer getSkuAmount(@Param("skuCode") String skuCode, @Param("nextDate") String nextDate) throws Exception;

    //sku金额统计
    Integer getSkuPrice(@Param("skuCode") String skuCode, @Param("nextDate") String nextDate) throws Exception;

    List<SkuSaleResponse> selectSkuSale(@Valid List<String> orderList) throws Exception;

    //查询购买数量
    List<OrderIdAndAmountRequest> buyAmount(@Valid ReorerRequest reorerRequest) throws Exception;

    //更改详情中实际金额
    void updateOrderDetail(OrderDetailInfo orderDetailInfo);

    //顾客可能还想购买
    List<String> wantBuy(@Valid @Param("sukList") List<String> sukList) throws Exception;

    OrderDetailInfo findOrderDetailById(@Param("orderDetailId") String orderDetailId);
   //统计数量
    Integer byMemberOrderCount(OrderDetailQuery orderDetailQuery);

    List<OrderDetailInfo> selectDetailByIdList(@Param("list") List<String> orderIdList);

    List<OrderDetailInfo> selectDetailByStoreId(@Param("storeId") String storeId);


    int updateOrderDetailById(OrderDetailInfo orderDetailInfo);
}
