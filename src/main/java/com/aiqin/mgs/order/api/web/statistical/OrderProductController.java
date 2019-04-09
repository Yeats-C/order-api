package com.aiqin.mgs.order.api.web.statistical;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.statistical.SoldOutOfStockProduct;
import com.aiqin.mgs.order.api.service.OrderStatisticalService;
import com.aiqin.mgs.order.api.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Createed by sunx on 2019/4/9.<br/>
 */
@RestController
@RequestMapping("/order/product/statistical")
@Api(tags = "z.商品数据统计")
@Slf4j
public class OrderProductController {

    @Resource
    private OrderStatisticalService orderStatisticalService;


    @GetMapping("/test")
    @ApiOperation("测试数据初始化")
    public void initSoldOutOfStock(String distributorId) {
        orderStatisticalService.refreshDistributorSoldOutOfStockProduct(DateUtil.getCurrentDate(), distributorId);
    }

    @GetMapping("/sold/stock/info")
    @ApiOperation("1.畅缺商品信息")
    public HttpResponse<List<SoldOutOfStockProduct>> getSoldOutOfStock(@RequestParam("distributor_id") String distributorId) {
        log.info("/order/product/statistical/sold/stock/info [{}]", distributorId);
        return HttpResponse.success(orderStatisticalService.getSoldOutOfStockProduct(distributorId));
    }
}
