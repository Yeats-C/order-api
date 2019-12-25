package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.request.ProductOverViewReq;
import com.aiqin.mgs.order.api.domain.request.ProductSeachSkuReq;
import com.aiqin.mgs.order.api.domain.response.ProductBaseUnResp;
import com.aiqin.mgs.order.api.domain.response.ProductLabelStatusResp;
import com.aiqin.mgs.order.api.domain.response.conversionrate.StoreTransforRateDaily;
import com.aiqin.mgs.order.api.domain.response.conversionrate.StoreTransforRateYearMonth;
import com.aiqin.mgs.order.api.domain.response.customer.CustomreFlowDaily;
import com.aiqin.mgs.order.api.domain.response.customer.CustomreFlowYearMonth;
import com.aiqin.mgs.order.api.domain.response.sales.*;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface StoreOverviewDao {

    /**
     *  查询门店总销售额当月
     * @param storeId
     * @param month
     */
    List<StoreSalesAchievedYearMonth> storeSalesAchievedMonthly(@Param("storeId") String storeId, @Param("month") String month);

    /**
     *  查询门店总销售额当年月
     * @param storeId
     * @param year
     */
    List<StoreSalesAchievedYearMonth> storeSalesAchievedYear(@Param("storeId") String storeId, @Param("year") String year);

    /**
     *  查询门店总销售额当年月日
     * @param storeId
     * @param year
     * @param month
     */
    List<StoreSalesAchievedDaily> storeSalesAchievedYearMonthly(@Param("storeId") String storeId, @Param("year") String year, @Param("month") String month);

    /**
     *  查询门店总销售昨日的
     * @param storeId
     * @param year
     * @param month
     * @param day
     */
    List<StoreSalesAchievedDaily> storeSalesAchievedYesterday(@Param("storeId")String storeId,@Param("year") String year,@Param("month") String month,@Param("day") String day);

    /**
     *  查询门店18A销售当月
     * @param storeId
     * @param month
     */
    List<StoreSalesAchievedYearMonth> storeEighteenSalesAchievedMonthly(@Param("storeId") String storeId, @Param("month") String month);

    /**
     * 查询门店18A销售当年
     * @param storeId
     * @param year
     */
    List<StoreSalesAchievedYearMonth> storeEighteenSalesAchievedYear(@Param("storeId") String storeId, @Param("year") String year);

    /**
     * 查询门店18A销售当月品牌
     * @param storeId
     * @param year
     * @param month
     */
    List<StoreSalesEighteenAchievedBrand> storeEighteenSalesAchievedBrand(@Param("storeId") String storeId, @Param("year") String year,@Param("month") String month);

    /**
     *  查询门店客流当月数据
     * @param storeId
     * @param month
     */
    List<CustomreFlowYearMonth> storeCustomerFlowMonthly(@Param("storeId") String storeId, @Param("month") String month);

    /**
     *  查询门店客流一年客流数据
     * @param storeId
     * @param year
     */
    List<CustomreFlowYearMonth> storeCustomerFlowYear(@Param("storeId") String storeId, @Param("year") String year);

    /**
     *  查询门店一月的客流数据
     * @param storeId
     * @param year
     * @param month
     */
    List<CustomreFlowDaily> storeCustomerFlowYearMonthly(@Param("storeId") String storeId, @Param("year") String year, @Param("month") String month);

    /**
     *  查询门店转化率当月数据
     * @param storeId
     * @param month
     */
    List<StoreTransforRateYearMonth> storeTransforRateMonthly(@Param("storeId") String storeId,@Param("month") String month);

    /**
     *  查询门店转化率一年客流数据
     * @param storeId
     * @param year
     */
    List<StoreTransforRateYearMonth> storeTransforRateYear(@Param("storeId") String storeId,@Param("year") String year);

    /**
     *  查询门店一月的门店转化率数据
     * @param storeId
     * @param year
     * @param month
     */
    List<StoreTransforRateDaily> storeTransforRateYearMonthly(@Param("storeId") String storeId,@Param("year") String year,@Param("month") String month);

    /**
     *  查询门店当月销售毛利
     * @param storeId
     * @param month
     * @return
     */
    BigDecimal storeSaleMargin(@Param("storeId") String storeId,@Param("month") String month);

    /**
     *  查询门店当月18销售毛利
     * @param storeId
     * @param month
     * @return
     */
    BigDecimal storeEighteenSaleMargin(@Param("storeId") String storeId,@Param("month") String month);

    /**
     *  查询门店当年销售毛利
     * @param storeId
     * @param year
     */
    List<StoreSaleMarginResp> storeSaleMarginYear(@Param("storeId") String storeId,@Param("year") String year);

    /**
     *  查询门店当年18销售毛利
     * @param storeId
     * @param year
     */
    List<StoreSaleMarginResp> storeEighteenSaleMarginYear(@Param("storeId") String storeId,@Param("year") String year);

    /**
     *  查询门店订单概览
     * @param storeId
     * @return
     */
    List<StoreSaleOverViewResp> storeOrderOverView(@Param("storeId") String storeId);

    /**
     *  获取商品畅销/普通/滞销数量
     * @param storeId
     * @return
     */
    ProductLabelStatusResp selectProductCountByStoreId(@Param("storeId") String storeId);

    /**
     *  商品概览（畅销滞销毛利排行）
     * @param productOverViewReq
     * @return
     */
    List<ProductBaseUnResp> productBaseUnInfo(ProductOverViewReq productOverViewReq);

    /**
     *  爱掌柜商品总库商品列表畅销滞销sku
     * @param productSeachSkuReq
     * @return
     */
    List<String> storeProductSku(ProductSeachSkuReq productSeachSkuReq);
}
