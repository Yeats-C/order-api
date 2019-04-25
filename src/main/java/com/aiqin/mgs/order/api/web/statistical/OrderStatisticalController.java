package com.aiqin.mgs.order.api.web.statistical;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.response.statistical.Last10DaysOrderStatistical;
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
@RequestMapping("/order/statistical")
@Api(tags = "z.订单数据统计")
@Slf4j
public class OrderStatisticalController {

    @Resource
    private OrderStatisticalService orderStatisticalService;

    @GetMapping("/product/sold/init")
    @ApiOperation("t.初始化指定门店的畅缺商品信息")
    public void initSoldOutOfStock(String distributorId) {
        orderStatisticalService.refreshDistributorSoldOutOfStockProduct(DateUtil.getCurrentDate(), distributorId);
    }

    @GetMapping("/product/unsold/init")
    @ApiOperation("t.初始化指定门店的非滞销sku集合")
    public void initUnsold(String distributorId) {
        orderStatisticalService.refreshDistributorDisUnsoldProduct(DateUtil.getCurrentDate(), distributorId);
    }

    @GetMapping("/product/sold/stock/info")
    @ApiOperation("1.畅缺商品信息")
    public HttpResponse<List<SoldOutOfStockProduct>> getSoldOutOfStock(@RequestParam("distributor_id") String distributorId) {
        log.info("/order/statistical/product/sold/stock/info [{}]", distributorId);
        return HttpResponse.success(orderStatisticalService.getSoldOutOfStockProduct(distributorId));
    }

    @GetMapping("/product/unsold")
    @ApiOperation("2.不满足滞销商品的sku集合")
    public HttpResponse<List<String>> getDisUnsold(@RequestParam("distributor_id") String distributorId) {
        log.info("/order/statistical/product/unsold [{}]", distributorId);
        return HttpResponse.success(orderStatisticalService.getDisUnsoldProduct(distributorId));
    }

    @GetMapping("/finish/ten/day")
    @ApiOperation("3.门店近10天订单完成情况统计")
    public HttpResponse<List<Last10DaysOrderStatistical>> getLast10DaysOrderFinishStatistical(@RequestParam("distributor_id") String distributorId) {
        log.info("/order/statistical/finish/ten/day [{}]", distributorId);
        return HttpResponse.success(orderStatisticalService.getLast10DaysOrderStatisticals(distributorId));
    }
}
