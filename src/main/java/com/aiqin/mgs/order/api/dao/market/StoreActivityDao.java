package com.aiqin.mgs.order.api.dao.market;

import com.aiqin.mgs.order.api.domain.OrderDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.activity.ActivityOrderInfo;
import com.aiqin.mgs.order.api.domain.response.market.ActivityReportOrderResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StoreActivityDao {


    List<ActivityReportOrderResp> selectActivityReportOrder(@Param("storeId") String storeId, @Param("activityId") String activityId, @Param("beginTime") String beginTime, @Param("finishTime") String finishTime);

    Long selectActivityOrderPackageSale(@Param("packageProductId") String packageProductId);

    ActivityReportOrderResp selectActivityOrder(String storeId, String activityId);

    List<OrderInfo> selectActivityOrderCode(@Param("storeId") String storeId, @Param("activityId") String activityId);

    List<String> selectActivitySkuCode(@Param("orderCodes") String orderCodes, @Param("activityId") String activityId);

    List<ActivityOrderInfo> selectActivityReportRelationOrder(OrderDetailInfo orderDetailInfo);

    Integer selectActivityReportRelationOrderCount(OrderDetailInfo orderDetailInfo);
}
