package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;

public interface StoreOverviewService {


    /**
     *  爱掌柜首页概览总销售额
     * @param storeId
     * @param text
     * @param year
     * @param month
     * @param day
     * @return
     */
    HttpResponse storeSalesAchieved(String storeId, Integer text, String year, String month, String day);

    /**
     *  爱掌柜首页概览18A销售额
     * @param storeId
     * @param text
     * @param year
     * @param month
     * @return
     */
    HttpResponse storeEighteenSalesAchieved(String storeId, Integer text, String year, String month);

    /**
     *  爱掌柜首页概览客流情况
     *
     * @param storeId
     * @param text
     *@param year
     * @param month  @return
     */
    HttpResponse storeCustomerFlowMonthly(String storeId, Integer text, String year, String month);

    /**
     *  爱掌柜首页概览门店转化率
     *
     * @param storeId
     * @param text
     *@param year
     * @param month  @return
     */
    HttpResponse storeTransforRate(String storeId, Integer text, String year, String month);

    /**
     *  爱掌柜首页概览门店销售毛利
     * @param storeId
     * @param text
     * @param year
     * @return
     */
    HttpResponse storeSaleMargin(String storeId, Integer text, String year);

    /**
     *  爱掌柜首页概览订单概览
     * @param storeId
     * @return
     */
    HttpResponse storeOrderOverView(String storeId);

    /**
     *  爱掌柜首页概览商品概览
     * @param storeId
     * @return
     */
    HttpResponse storeProductOverView(String storeId);
}
