package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;

public interface StoreOverviewService {

    /**
     *  爱掌柜首页概览客流情况
     *
     * @param storeId
     * @param text
     *@param year
     * @param month  @return
     */
    HttpResponse storeCustomerFlowMonthly(String storeId, Integer text, String year, String month);
}
