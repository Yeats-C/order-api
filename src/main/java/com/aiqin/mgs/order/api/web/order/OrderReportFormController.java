package com.aiqin.mgs.order.api.web.order;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.BasePage;
import com.aiqin.mgs.order.api.domain.request.CostAndSalesReq;
import com.aiqin.mgs.order.api.domain.response.CostAndSalesResp;
import com.aiqin.mgs.order.api.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author jinghaibo
 * Date: 2020/9/1 20:31
 * Description:
 */
@RestController
@RequestMapping("/report/form")
@Api(tags = "订单报表")
@Slf4j
public class OrderReportFormController {

    @Resource
    private OrderService orderService;

    @PostMapping("/costAndSales")
    @ApiOperation(value = "销售及成本报表")
    public HttpResponse<BasePage<CostAndSalesResp>> costAndSales(@RequestBody CostAndSalesReq costAndSalesReq) {
        log.info("销售及成本报表{}", costAndSalesReq);
        try {
            HttpResponse httpResponse = orderService.costAndSales(costAndSalesReq);
            return httpResponse;
        } catch (Exception e) {
            log.info("错误信息{}", e.getMessage());
            return HttpResponse.failure(MessageId.create(Project.PAYMENT_API, -1, e.getMessage()));
        }
    }

}
