package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.Activity;

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
}








