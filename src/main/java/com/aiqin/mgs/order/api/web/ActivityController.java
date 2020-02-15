package com.aiqin.mgs.order.api.web;


import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.Activity;
import com.aiqin.mgs.order.api.domain.ActivityProduct;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.request.activity.ActivityRequest;
import com.aiqin.mgs.order.api.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    public HttpResponse<List<Activity>> activityList(Activity activity){
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
    public HttpResponse<List<Activity>> getActivityItem(ErpOrderItem erpOrderItem){
        return activitesService.getActivityItem(erpOrderItem);
    }

}
