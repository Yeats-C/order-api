package com.aiqin.mgs.order.api.web.statistical;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.dto.FrontEndSalesStatisticsByCategoryDTO;
import com.aiqin.mgs.order.api.domain.dto.FrontEndSalesStatisticsResponse;
import com.aiqin.mgs.order.api.domain.response.statistical.BusinessStatisticalResponse;
import com.aiqin.mgs.order.api.service.FrontEndSalesStatisticsDetailService;
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
    private FrontEndSalesStatisticsDetailService salesStatisticsDetailService;

    @ApiOperation(value = "1.前台销售统计", notes = "month eg : 2020-02")
    @GetMapping("/getSalesStatisticsByMonthAndStoreId")
    public HttpResponse<FrontEndSalesStatisticsByCategoryDTO> getSalesStatisticsByMonthAndStoreId(
            @RequestParam("store_id") String store_id,
            @RequestParam("month") String month) {
        log.info("/order/statistical/frontEndSalesStatistics [{}]", store_id);
        return salesStatisticsDetailService.selectStoreMonthSaleStatisticsByMonthAndStoreId(month,store_id);
    }
}

