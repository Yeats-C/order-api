package com.aiqin.mgs.order.api.web;


import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.Activities;
import com.aiqin.mgs.order.api.domain.CartOrderInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/activities")
@Api(tags = "促销活动相关接口")
public class ActivitiesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiesController.class);

    /**
     * 促销活动管理--活动列表
     * @param activities
     * @return
     */
    @PostMapping("/ActivityList")
    @ApiOperation(value = "显示勾选商品列表")
    public HttpResponse<List<CartOrderInfo>> displayCartLineCheckProduct(Activities activities){
        return null;
    }

}
