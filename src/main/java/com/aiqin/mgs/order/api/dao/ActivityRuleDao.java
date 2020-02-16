package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.ActivityRule;

import java.util.List;

/**
 * @author csf
 */
public interface ActivityRuleDao {

    /**
     * 保存活动对应规则信息List
     * @param activityRuleList
     * @return
     */
    Integer insertList(List<ActivityRule> activityRuleList);

    /**
     * 通过活动id查询活动规则
     * @param activityId
     * @return
     */
    List<ActivityRule> selectByActivityId(String activityId);
}








