package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityDao {

    /**
     * 促销活动管理--活动列表
     * @param activity
     * @return
     */
    List<Activity> activityList(Activity activity);

    /**
     * 通过活动id获取单个活动信息
     * @param activity
     * @return
     */
    Activity getActivityInformation(Activity activity);

    /**
     * 保存活动主体
     * @param activity
     * @return
     */
    int insertActivity(Activity activity);

    /**
     * 编辑活动主体
     * @param activity
     * @return
     */
    int updateActivity(Activity activity);

    /**
     * 查询总条数
     * @param activity
     * @return
     */
    Integer totalCount(Activity activity);

    /**
     * 通过门店id爱掌柜的促销活动列表（所有生效活动）
     * @param storeId
     * @return
     */
    List<Activity> effectiveActivityList(String storeId);

    /**
     * 校验商品活动是否过期
     * @param activityId
     * @param storeId
     * @param productId
     * @return
     */
    List<Activity> checkProcuct(@Param("activityId") String activityId,@Param("storeId") String storeId,@Param("productId") String productId,@Param("productBrandCode") String productBrandCode,@Param("productCategoryCode") List<String> productCategoryCode);
}








