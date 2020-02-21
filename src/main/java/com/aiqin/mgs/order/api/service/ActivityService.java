package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.Activity;
import com.aiqin.mgs.order.api.domain.ActivityProduct;
import com.aiqin.mgs.order.api.domain.ActivitySales;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.request.activity.ActivityParameterRequest;
import com.aiqin.mgs.order.api.domain.request.activity.ActivityRequest;
import com.aiqin.mgs.order.api.domain.request.activity.SpuProductReqVO;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartRequest;

import javax.servlet.http.HttpServletResponse;
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
    HttpResponse<ActivitySales> getActivitySalesStatistics(String activityId);

    /**
     * 通过活动id获取单个活动详情（活动+门店+商品+规则）
     * @param activityId
     * @return
     */
    HttpResponse<ActivityRequest> getActivityDetail(String activityId);

    /**
     * 编辑活动
     * @param activityRequest
     * @return
     */
    HttpResponse updateActivity(ActivityRequest activityRequest);

    /**
     * 编辑活动生效状态
     * @param activity
     * @return
     */
    HttpResponse updateStatus(Activity activity);

    /**
     * 通过门店id爱掌柜的促销活动列表（所有生效活动）
     * @param storeId
     * @return
     */
    HttpResponse<List<ActivityRequest>> effectiveActivityList(String storeId);

    /**
     * 返回购物车中的sku商品的数量
     * @param shoppingCartRequest
     * @return
     */
    HttpResponse<Integer> getSkuNum(ShoppingCartRequest shoppingCartRequest);

    /**
     * 校验商品活动是否过期
     * @param  activityParameterRequest

     * @return
     */
    Boolean checkProcuct(ActivityParameterRequest activityParameterRequest);

    /**
     * 活动商品品牌列表接口
     * @param productBrandName
     * @return
     */
    HttpResponse<List<ActivityProduct>> productBrandList(String productBrandName);

    /**
     * 活动商品品类接口
     * @return
     */
    HttpResponse<List<ActivityProduct>> productCategoryList();

    /**
     *导出--活动详情-销售数据-活动销售列表
     * @param  erpOrderItem
     * @param response
     * @return
     */
    HttpResponse excelActivityItem(ErpOrderItem erpOrderItem, HttpServletResponse response);

    /**
     *活动列表-对比分析柱状图
     * @param activityIdList
     * @return
     */
    HttpResponse<List<ActivitySales>> comparisonActivitySalesStatistics(List<String> activityIdList);

    /**
     * 导出-活动列表-对比分析柱状图
     * @param activityIdList
     * @param response
     * @return
     */
    HttpResponse excelActivitySalesStatistics(List<String> activityIdList, HttpServletResponse response);

    /**
     * 活动商品筛选分页
     * @param spuProductReqVO
     * @return
     */
    HttpResponse skuPage(SpuProductReqVO spuProductReqVO);

    /**
     * 通过条件查询一个商品有多少个进行中的活动
     *
     * @return
     */
    List<Activity> activityList(ActivityParameterRequest activityParameterRequest);
}
