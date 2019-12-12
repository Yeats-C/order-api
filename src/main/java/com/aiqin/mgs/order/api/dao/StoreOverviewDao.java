package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.response.conversionrate.StoreTransforRateDaily;
import com.aiqin.mgs.order.api.domain.response.conversionrate.StoreTransforRateYearMonth;
import com.aiqin.mgs.order.api.domain.response.customer.CustomreFlowDaily;
import com.aiqin.mgs.order.api.domain.response.customer.CustomreFlowYearMonth;
import com.aiqin.mgs.order.api.domain.response.sales.StoreSalesAchievedDaily;
import com.aiqin.mgs.order.api.domain.response.sales.StoreSalesAchievedYearMonth;
import com.aiqin.mgs.order.api.domain.response.sales.StoreSalesEighteenAchievedBrand;
import org.apache.ibatis.annotations.Param;

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



}
