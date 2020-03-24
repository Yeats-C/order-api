package com.aiqin.mgs.order.api.service.market;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.OrderDetailInfo;
import com.aiqin.mgs.order.api.domain.activity.ActivityOrderInfo;
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

    /**
     *  查询活动数据报表实时订单情况
     * @param storeId
     * @param activityId
     * @return
     */
    HttpResponse<ActivityReportOrderResp> selectActivityReportOrder(String storeId, String activityId);

    /**
     *  查询活动数据关联订单数据
     * @param activityId
     * @param pageNo
     * @param pageSize
     * @return
     */
    HttpResponse<PageResData<ActivityOrderInfo>> selectActivityReportRelationOrder(String activityId, Integer pageNo, Integer pageSize);

    /**
     *  查询活动商品销量和销售额
     * @param activityOrderInfo
     * @return
     */
    HttpResponse<List<OrderDetailInfo>> selectActivityOrderProduct(ActivityOrderInfo activityOrderInfo);
}
