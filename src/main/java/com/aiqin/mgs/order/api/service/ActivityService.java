package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.Activity;
import com.aiqin.mgs.order.api.domain.ActivityProduct;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.request.activity.ActivityRequest;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    /**
     * 促销活动管理--活动列表
     * @param activity
     * @return
     */
    HttpResponse<Map> activityList(Activity activity);

    /**
     * 通过活动id获取单个活动信息
     * @param activityId
     * @return
     */
    HttpResponse<Activity> getActivityInformation(String activityId);

    /**
     * 添加活动
     * @param activityRequest
     * @return
     */
    HttpResponse addActivity(ActivityRequest activityRequest);

    /**
     * 活动详情-促销规则-活动商品列表查询（分页），只传activityId与分页参数
     * @param activity
     * @return
     */
    HttpResponse<List<ActivityProduct>> activityProductList(Activity activity);

    /**
     * 活动详情-销售数据-活动销售列表（分页）-只传分页参数
     * @param erpOrderItem
     * @return
     */
    HttpResponse<Map> getActivityItem(ErpOrderItem erpOrderItem);

    /**
     * 活动详情-销售数据-活动销售统计
     * @return
     */
    HttpResponse<Map> getActivitySalesStatistics();

    /**
     * 通过活动id获取单个活动详情（活动+门店+商品+规则）
     * @param activityId
     * @return
     */
    HttpResponse<Activity> getActivityDetail(String activityId);

    /**
     * 编辑活动
     * @param activityRequest
     * @return
     */
    HttpResponse updateActivity(ActivityRequest activityRequest);
}
