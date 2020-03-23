package com.aiqin.mgs.order.api.web.market;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.response.market.ActivityReportOrderResp;
import com.aiqin.mgs.order.api.service.market.StoreActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/activity/details")
@Api(tags = "营销活动订单相关接口")
public class StoreActivityController {

    @Resource
    private StoreActivityService storeActivityService;


    @GetMapping("/select/report/order/info")
    @ApiOperation("查询活动数据报表中每天的订单情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "store_id", value = "门店id", dataType = "String", required = true),
            @ApiImplicitParam(name = "activity_id", value = "活动id", dataType = "String", required = true),
            @ApiImplicitParam(name = "begin_time", value = "开始时间", dataType = "String", required = true),
            @ApiImplicitParam(name = "finish_time", value = "结束时间", dataType = "String", required = true)})
    public HttpResponse<List<ActivityReportOrderResp>> selectActivityReportOrderInfo(@RequestParam(name = "store_id", required = true) String storeId,
                                                                                     @RequestParam(name = "activity_id", required = true) String activityId,
                                                                                     @RequestParam(name = "begin_time", required = true) String beginTime,
                                                                                     @RequestParam(name = "finish_time", required = true) String finishTime) {
        return storeActivityService.selectActivityReportOrderInfo(storeId, activityId, beginTime, finishTime);
    }

    @GetMapping("/order/package/sale")
    @ApiOperation("查询套餐包列表销量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "package_product_id", value = "套餐包id", dataType = "String", required = true)})
    public HttpResponse selectActivityOrderPackageSale(@RequestParam(name = "package_product_id", required = true) String packageProductId) {
        return storeActivityService.selectActivityOrderPackageSale(packageProductId);
    }

    @GetMapping("/select/report/order")
    @ApiOperation("查询活动数据报表实时订单情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "store_id", value = "门店id", dataType = "String", required = false),
            @ApiImplicitParam(name = "activity_id", value = "活动id", dataType = "String", required = false)})
    public HttpResponse<ActivityReportOrderResp> selectActivityReportOrder(@RequestParam(name = "store_id", required = false) String storeId,
                                                                           @RequestParam(name = "activity_id", required = false) String activityId) {
        return storeActivityService.selectActivityReportOrder(storeId, activityId);
    }



}
