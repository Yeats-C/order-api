package com.aiqin.mgs.order.api.dao.market;

import com.aiqin.mgs.order.api.domain.response.market.ActivityReportOrderResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StoreActivityDao {


    List<ActivityReportOrderResp> selectActivityReportOrder(@Param("storeId") String storeId, @Param("activityId") String activityId, @Param("beginTime") String beginTime, @Param("finishTime") String finishTime);



    Long selectActivityOrderPackageSale(@Param("packageProductId") String packageProductId);
}
