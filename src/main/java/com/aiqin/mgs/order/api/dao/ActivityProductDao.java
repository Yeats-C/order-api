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

    /**
     * 通过activityId删除活动-商品信息（逻辑删除）
     * @param activityId
     * @return
     */
    Integer deleteProductByActivityId(String activityId);

    /**
     * 活动商品品牌列表接口
     * @param productBrandName
     * @return
     */
    List<ActivityProduct> productBrandList(ActivityProduct productBrandName);

    /**
     * 活动商品品类接口
     * @return
     */
    List<ActivityProduct> productCategoryList();
}








