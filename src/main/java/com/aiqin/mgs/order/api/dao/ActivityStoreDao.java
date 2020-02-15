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
}








