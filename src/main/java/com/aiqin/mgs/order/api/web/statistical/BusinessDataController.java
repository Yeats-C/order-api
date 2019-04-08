package com.aiqin.mgs.order.api.web.statistical;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.response.statistical.BusinessStatisticalResponse;
import com.aiqin.mgs.order.api.service.OrderStatisticalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Createed by sunx on 2019/4/4.<br/>
 */
@RestController
@RequestMapping("/order/statistical")
@Api("z.营业数据统计")
@Slf4j
public class BusinessDataController {

    @Resource
    private OrderStatisticalService orderStatisticalService;

    @ApiOperation("1.营业数据统计")
    @GetMapping("/business")
    public HttpResponse<BusinessStatisticalResponse> businessStatistical(@RequestParam("distributor_id") String distributorId) {
        log.info("/order/statistical/business [{}]", distributorId);
        return HttpResponse.success(orderStatisticalService.businessStatistical(distributorId));
    }
}
