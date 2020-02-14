package com.aiqin.mgs.order.api.web;


import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.Activity;
import com.aiqin.mgs.order.api.domain.request.activity.ActivityRequest;
import com.aiqin.mgs.order.api.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
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
    @PostMapping("/activityList")
    @ApiOperation(value = "促销活动管理--活动列表")
    public HttpResponse<List<Activity>> activityList(Activity activity){
        return activitesService.activityList(activity);
    }

    /**
     * 通过活动id获取单个活动信息
     * @param activityId
     * @return
     */
    @PostMapping("/getActivityInformation")
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
    @ApiOperation(value = "将商品添加到购物车")
    public HttpResponse addCart(@Valid @RequestBody ActivityRequest activityRequest) {
        //将商品添加到购物车
        return activitesService.addActivity(activityRequest);
    }

}
