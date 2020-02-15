package com.aiqin.mgs.order.api.web.statistical;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.response.statistical.BusinessStatisticalResponse;
import com.aiqin.mgs.order.api.service.FrontEndSalesStatisticsService;
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
 * @author by wpp25
 * @Classname FrontEndSalesStatisticsController
 * @Description
 * @Date 2020/2/15 10:19
 */
@RestController
@RequestMapping("/order/statistical")
@Api(tags = "前台销售统计")
@Slf4j
public class FrontEndSalesStatisticsController {
    @Resource
    private FrontEndSalesStatisticsService frontEndSalesStatisticsService;

    @ApiOperation("1.前台销售统计")
    @GetMapping("/frontEndSalesStatistics")
    public HttpResponse<BusinessStatisticalResponse> frontEndSalesStatistics(@RequestParam("distributor_id") String distributorId) {
        log.info("/order/statistical/frontEndSalesStatistics [{}]", distributorId);
        // todo
        return HttpResponse.success();
    }
}

