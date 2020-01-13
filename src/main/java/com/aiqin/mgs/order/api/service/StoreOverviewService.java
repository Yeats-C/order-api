package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.request.ProductOverViewReq;

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

    /**
     *  爱掌柜商品总库商品概览畅销滞销毛利排行
     * @param productOverViewReq
     * @return
     */
    HttpResponse productBaseUnInfo(ProductOverViewReq productOverViewReq);

    /**
     *  爱掌柜商品总库商品列表畅销滞销sku
     * @param storeId
     * @param status
     * @return
     */
    HttpResponse storeProductSku(String storeId, String status, String pageNo, String pageSize);

    /**
     *  通过门店id和sku获取对应畅销度
     * @param productOverViewReq
     * @return
     */
    HttpResponse productStoreSaleoutDgree(ProductOverViewReq productOverViewReq);
}
