package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.CouponRule;
import com.aiqin.mgs.order.api.service.CouponRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * description: 优惠券规则设置相关接口
 * date: 2020/3/11 15:30
 * author: hantao
 * version: 1.0
 */
@RestController
@RequestMapping("/couponRule")
@Api(tags = "优惠券规则设置相关接口")
public class CouponRuleController {

    @Autowired
    private CouponRuleService couponRuleService;

    /**
     * 基础设置--活惠券规则设置列表
     * @return
     */
    @GetMapping("/getList")
    @ApiOperation(value = "基础设置--活惠券规则设置列表")
    public HttpResponse<List<CouponRule>> getList(){
        return HttpResponse.success(couponRuleService.getList());
    }

    @GetMapping("/getCouponRule")
    @ApiOperation(value = "基础设置--根据活惠券类型查看规则及商品属性")
    @ApiImplicitParams({@ApiImplicitParam(name = "coupon_type", value = "优惠券类型 0-物流券 1-服纺券 2-A品券", dataType = "int", paramType = "query", required = true)})
    public HttpResponse<CouponRule> getCouponRule(Integer coupon_type){
        return HttpResponse.success(couponRuleService.getCouponRule(coupon_type));
    }

    @GetMapping("/insert")
    @ApiOperation(value = "基础设置--活惠券规则设置")
    public HttpResponse<Boolean> insert(@RequestBody CouponRule couponRule){
        return HttpResponse.success(couponRuleService.insert(couponRule));
    }

    @GetMapping("/update")
    @ApiOperation(value = "基础设置--活惠券规则修改")
    public HttpResponse<Boolean> update(@RequestBody CouponRule couponRule){
        return HttpResponse.success(couponRuleService.update(couponRule));
    }


}
