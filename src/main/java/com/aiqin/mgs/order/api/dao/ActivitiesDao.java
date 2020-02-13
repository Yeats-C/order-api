package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.Activities;

import java.util.List;

public interface ActivitiesDao {

    /**
     * 促销活动管理--活动列表
     * @param activities
     * @return
     */
    List<Activities> activityList(Activities activities);

    /**
     * 通过活动id获取单个活动信息
     * @param activities
     * @return
     */
    Activities getActivityInformation(Activities activities);
}








