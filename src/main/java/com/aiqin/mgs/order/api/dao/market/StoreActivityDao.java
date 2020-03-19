package com.aiqin.mgs.order.api.dao.market;

import com.aiqin.mgs.order.api.domain.response.market.ActivityReportOrderResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StoreActivityDao {


    List<ActivityReportOrderResp> selectActivityReportOrderInfo(@Param("storeId") String storeId, @Param("activityId") String activityId, @Param("beginTime") String beginTime, @Param("finishTime") String finishTime);
}
