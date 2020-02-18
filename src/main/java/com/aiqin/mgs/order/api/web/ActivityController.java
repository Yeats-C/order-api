package com.aiqin.mgs.order.api.web;


import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.Activity;
import com.aiqin.mgs.order.api.domain.ActivityProduct;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.request.activity.ActivityRequest;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartRequest;
import com.aiqin.mgs.order.api.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activity")
@Api(tags = "促销活动相关接口")
public class ActivityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityController.class);
    @Resource
    private ActivityService activitesService;

    /**
     * 促销活动管理--活动列表
     * @param activity
     * @return
     */
    @GetMapping("/activityList")
    @ApiOperation(value = "促销活动管理--活动列表")
    public HttpResponse<Map> activityList(Activity activity){
        return activitesService.activityList(activity);
    }

    /**
     * 通过活动id获取单个活动信息
     * @param activityId
     * @return
     */
    @GetMapping("/getActivityInformation")
    @ApiOperation(value = "通过活动id获取单个活动信息")
    public HttpResponse<Activity> getActivityInformation(String activityId){
        return activitesService.getActivityInformation(activityId);
    }

    /**
     * 添加活动
     *
     * @param
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加活动")
    public HttpResponse add(@RequestBody ActivityRequest activityRequest) {
        //将商品添加到购物车
        return activitesService.addActivity(activityRequest);
    }

    /**
     * 活动详情-促销规则-活动商品列表查询（分页），只传activityId与分页参数
     * @param activity
     * @return
     */
    @GetMapping("/activityProductList")
    @ApiOperation(value = "活动详情-促销规则-活动商品列表查询（分页），只传activityId与分页参数")
    public HttpResponse<List<ActivityProduct>> activityProductList(Activity activity){
        return activitesService.activityProductList(activity);
    }

    /**
     * 活动详情-销售数据-活动销售列表（分页）-只传分页参数
     * @param erpOrderItem
     * @return
     */
    @GetMapping("/getActivityItem")
    @ApiOperation(value = "活动详情-销售数据-活动销售列表（分页）-只传分页参数")
    public HttpResponse<Map> getActivityItem(ErpOrderItem erpOrderItem){
        return activitesService.getActivityItem(erpOrderItem);
    }

    /**
     * 活动详情-销售数据-活动销售统计
     * @param
     * @return
     */
    @GetMapping("/getActivitySalesStatistics")
    @ApiOperation(value = "活动详情-销售数据-活动销售统计")
    public HttpResponse<Map> getActivitySalesStatistics(){
        return activitesService.getActivitySalesStatistics();
    }

    /**
     * 通过活动id获取单个活动详情（活动+门店+商品+规则）
     * @param activityId
     * @return
     */
    @GetMapping("/getActivityDetail")
    @ApiOperation(value = "通过活动id获取单个活动详情（活动+门店+商品+规则）")
    public HttpResponse<ActivityRequest> getActivityDetail(String activityId){
        return activitesService.getActivityDetail(activityId);
    }

    /**
     * 编辑活动
     *
     * @param
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "编辑活动")
    public HttpResponse update(@RequestBody ActivityRequest activityRequest) {
        //将商品添加到购物车
        return activitesService.updateActivity(activityRequest);
    }

    /**
     * 通过门店id爱掌柜的促销活动列表（所有生效活动）
     * @param storeId
     * @return
     */
    @GetMapping("/effectiveActivityList")
    @ApiOperation(value = "通过门店id爱掌柜的促销活动列表（所有生效活动）")
    public HttpResponse<List<ActivityRequest>> effectiveActivityList(String storeId){
        return activitesService.effectiveActivityList(storeId);
    }

    /**
     * 爱掌柜-活动商品列表查询（分页），只传activityId与分页参数（未完，商品需处理。每个商品得确定有什么活动，这个最好是在service层写成公用方法）
     * @param activity
     * @return
     */
    @GetMapping("/productList")
    @ApiOperation(value = "活动详情-促销规则-活动商品列表查询（分页），只传activityId与分页参数")
    public HttpResponse<List<ActivityProduct>> productList(Activity activity){
        return activitesService.activityProductList(activity);
    }

    /**
     * 返回购物车中的sku商品的数量
     * @param shoppingCartRequest
     * @return
     */
    @GetMapping("/getSkuNum")
    @ApiOperation(value = "返回购物车中的sku商品的数量")
    public HttpResponse<Integer> getSkuNum(@Valid ShoppingCartRequest shoppingCartRequest) {
        return activitesService.getSkuNum(shoppingCartRequest);
    }

    /**
     * 校验商品活动是否过期
     * @param activityId
     * @param storeId
     * @param productId
     * @return
     */
    @GetMapping("/checkProcuct")
    @ApiOperation(value = "校验商品活动是否过期")
    Boolean checkProcuct(String activityId,String storeId,String productId){
        return activitesService.checkProcuct(activityId,storeId,productId);
    }

    /**
     * 活动商品品牌列表接口
     * @param productBrandName
     * @return
     */
    @GetMapping("/product/brand/list")
    @ApiOperation(value = "活动商品品牌列表接口")
    public HttpResponse<ActivityProduct> productBrandList(String productBrandName) {
        return activitesService.productBrandList(productBrandName);
    }
}
