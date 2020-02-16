package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.ActivityStore;

import java.util.List;

/**
 * @author csf
 */
public interface ActivityStoreDao {

    /**
     * 保存活动对应门店信息List
     * @param activityStoreList
     * @return
     */
    Integer insertList(List<ActivityStore> activityStoreList);

    /**
     * 通过activityId查询活动-门店列表
     * @param activityId
     * @return
     */
    List<ActivityStore> selectByActivityId(String activityId);

    /**
     * 删除活动-门店信息
     * @param activityId
     * @return
     */
    Integer deleteStoreByActivityId(String activityId);
}








