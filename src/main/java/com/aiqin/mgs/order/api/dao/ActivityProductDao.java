package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.Activity;
import com.aiqin.mgs.order.api.domain.ActivityProduct;

import java.util.List;

/**
 * @author csf
 */
public interface ActivityProductDao {

    /**
     * 保存活动对应商品信息List
     * @param activityProductList
     * @return
     */
    Integer insertList(List<ActivityProduct> activityProductList);

    /**
     * 查询活动-商品列表
     * @param activity
     * @return
     */
    List<ActivityProduct> activityProductList(Activity activity);
}








