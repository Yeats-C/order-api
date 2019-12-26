package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.request.ProductOverViewReq;
import com.aiqin.mgs.order.api.domain.response.ProductBaseUnResp;
import com.aiqin.mgs.order.api.service.StoreOverviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ch
 * @date 2019/12/11 下午14:07
 */
@RestController
@RequestMapping("/manage/overview")
@Api(tags = "Manage9.爱掌柜首页概览Api")
@Slf4j
public class StoreOverviewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreOverviewController.class);

    @Resource
    private StoreOverviewService storeOverviewService;

    @GetMapping("/sales/achieved")
    @ApiOperation(value = "门店总销售额")
    @ApiImplicitParams({@ApiImplicitParam(name = "store_id", value = "门店id", dataType = "String"),
            @ApiImplicitParam(name = "text", value = "状态(0当月,1当年,2选择年月3,昨日)", dataType = "Integer"),
            @ApiImplicitParam(name = "year", value = "年", dataType = "String"),
            @ApiImplicitParam(name = "month", value = "月", dataType = "String"),
            @ApiImplicitParam(name = "day", value = "日", dataType = "String"),
    })
    public HttpResponse storeSalesAchieved(@RequestParam(value = "store_id") String storeId,
                                                 @RequestParam(value = "text") Integer text,
                                                 @RequestParam(value = "year", required = false) String year,
                                                 @RequestParam(value = "month", required = false) String month,
                                                 @RequestParam(value = "day", required = false) String day) {
        LOGGER.info("获取门店总销售额......",storeId,text,year,month,day);
        return storeOverviewService.storeSalesAchieved(storeId,text,year,month,day);
    }

    @GetMapping("/sales/achieved/eighteen")
    @ApiOperation(value = "门店18A销售额")
    @ApiImplicitParams({@ApiImplicitParam(name = "store_id", value = "门店id", dataType = "String"),
            @ApiImplicitParam(name = "text", value = "状态(0当月,1当年,2选择年月)", dataType = "Integer"),
            @ApiImplicitParam(name = "year", value = "年", dataType = "String"),
            @ApiImplicitParam(name = "month", value = "月", dataType = "String"),
    })
    public HttpResponse storeEighteenSalesAchieved(@RequestParam(value = "store_id") String storeId,
                                                 @RequestParam(value = "text") Integer text,
                                                 @RequestParam(value = "year", required = false) String year,
                                                 @RequestParam(value = "month", required = false) String month) {
        LOGGER.info("获取门店18A总销售额......",storeId,text,year,month);
        return storeOverviewService.storeEighteenSalesAchieved(storeId,text,year,month);
    }

    @GetMapping("/customer/flow")
    @ApiOperation(value = "客流情况")
    @ApiImplicitParams({@ApiImplicitParam(name = "store_id", value = "门店id", dataType = "String"),
            @ApiImplicitParam(name = "text", value = "状态(0当月,1当年,2选择年月)", dataType = "Integer"),
            @ApiImplicitParam(name = "year", value = "年", dataType = "String"),
            @ApiImplicitParam(name = "month", value = "月", dataType = "String"),
    })
    public HttpResponse storeCustomerFlowMonthly(@RequestParam(value = "store_id") String storeId,
                                                 @RequestParam(value = "text") Integer text,
                                                 @RequestParam(value = "year", required = false) String year,
                                                 @RequestParam(value = "month", required = false) String month) {
      //  distributorId = "ABEC8D65036E5A45DBABCBA413FA56AEA2";
        LOGGER.info("获取门店客流量......",storeId,text,year,month);
        return storeOverviewService.storeCustomerFlowMonthly(storeId,text,year,month);
    }

    @GetMapping("/store/rate")
    @ApiOperation(value = "门店转换率")
    @ApiImplicitParams({@ApiImplicitParam(name = "store_id", value = "门店id", dataType = "String"),
            @ApiImplicitParam(name = "text", value = "状态(0当月,1当年,2选择年月)", dataType = "Integer"),
            @ApiImplicitParam(name = "year", value = "年", dataType = "String"),
            @ApiImplicitParam(name = "month", value = "月", dataType = "String"),
    })
    public HttpResponse storeTransforRate(@RequestParam(value = "store_id") String storeId,
                                                 @RequestParam(value = "text") Integer text,
                                                 @RequestParam(value = "year", required = false) String year,
                                                 @RequestParam(value = "month", required = false) String month) {
        LOGGER.info("获取门店转化率数据......",storeId,text,year,month);
        return storeOverviewService.storeTransforRate(storeId,text,year,month);
    }

    @GetMapping("/store/margin")
    @ApiOperation(value = "销售毛利")
    @ApiImplicitParams({@ApiImplicitParam(name = "store_id", value = "门店id", dataType = "String"),
            @ApiImplicitParam(name = "text", value = "状态(0当月,1总毛利额,2,18A毛利额)", dataType = "Integer"),
            @ApiImplicitParam(name = "year", value = "年", dataType = "String"),
    })
    public HttpResponse storeSaleMargin(@RequestParam(value = "store_id") String storeId,
                                          @RequestParam(value = "text") Integer text,
                                          @RequestParam(value = "year", required = false) String year){
        LOGGER.info("获取门店转化率数据......",storeId,text,year);
        return storeOverviewService.storeSaleMargin(storeId,text,year);
    }

    @GetMapping("/store/order")
    @ApiOperation(value = "订单概览")
    @ApiImplicitParam(name = "store_id", value = "门店id", dataType = "String")
    public HttpResponse storeOrderOverView(@RequestParam(value = "store_id") String storeId){
        LOGGER.info("获取门店订单概览......",storeId);
        return storeOverviewService.storeOrderOverView(storeId);
    }

    @GetMapping("/store/product")
    @ApiOperation(value = "商品概览（畅销度）")
    @ApiImplicitParams({@ApiImplicitParam(name = "store_id", value = "门店id", dataType = "String"),
    })
    public HttpResponse storeProductOverView(@RequestParam(value = "store_id") String storeId){
        LOGGER.info("获取门店商品概览（畅销度）......",storeId);
        return storeOverviewService.storeProductOverView(storeId);
    }

    @PostMapping("/status/product")
    @ApiOperation(value = "商品概览（畅销滞销毛利排行）")
    public HttpResponse productBaseUnInfo(@RequestBody ProductOverViewReq productOverViewReq) {
        LOGGER.info("获取门店商品概览（畅销滞销毛利排行）......",productOverViewReq);
        return storeOverviewService.productBaseUnInfo(productOverViewReq);
    }

    /**
     *  爱掌柜商品总库商品列表畅销滞销sku
     * @param storeId
     * @param status
     * @return
     */
    @GetMapping("/store/product/sku")
    @ApiOperation(value = "商品列表（获取畅销滞销sku）")
    @ApiImplicitParams({@ApiImplicitParam(name = "store_id", value = "门店id", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态：0畅销，1滞销", dataType = "String"),
            @ApiImplicitParam(name = "page_no", value = "当前页", dataType = "String"),
            @ApiImplicitParam(name = "page_size", value = "每页条数", dataType = "String"),
    })
    public HttpResponse storeProductSku(@RequestParam(value = "store_id") String storeId,
                                        @RequestParam(value = "status") String status,
                                        @RequestParam(value = "page_no") String pageNo,
                                        @RequestParam(value = "page_size") String pageSize){
        LOGGER.info("获取门店商品列表（获取畅销滞销sku）......",storeId,status,pageNo,pageSize);
        return storeOverviewService.storeProductSku(storeId,status,pageNo,pageSize);
    }
}
