package com.aiqin.mgs.order.api.service.market;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.response.market.ActivityReportOrderResp;

import java.util.List;

public interface StoreActivityService {

    /**
     *  查询活动数据报表中每天的订单情况
     * @param storeId
     * @param activityId
     * @param beginTime
     * @param finishTime
     * @return
     */
    HttpResponse<List<ActivityReportOrderResp>> selectActivityReportOrderInfo(String storeId, String activityId, String beginTime, String finishTime);

    /**
     *  查询套餐包列表销量
     * @param packageProductId
     * @return
     */
    HttpResponse selectActivityOrderPackageSale(String packageProductId);
}
