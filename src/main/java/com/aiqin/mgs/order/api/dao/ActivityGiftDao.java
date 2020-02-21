package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.ActivityGift;

import java.util.List;

/**
 * @author csf
 */
public interface ActivityGiftDao {

    /**
     * 保存活动对应规则信息List
     * @param activityGiftList
     * @return
     */
    Integer insertList(List<ActivityGift> activityGiftList);

    /**
     * 通过活动id查询活动规则
     * @param ruleId
     * @return
     */
    List<ActivityGift> selectByRuleId(String ruleId);


}








