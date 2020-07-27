package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.service.StoreMonthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/store/month")
@Api(tags = "门店上月销量")
public class StoreMonthController {

    private static  final Logger LOGGER = LoggerFactory.getLogger(StoreMonthController.class);

    @Autowired
    private StoreMonthService storeMonthService;

    @GetMapping("/select")
    @ApiOperation("根据门店名称或者门店编码查询上月销量")
    public HttpResponse selectStore(@RequestParam(value = "store_name") String storeName){
        return storeMonthService.selectStoreMonth(storeName);
    }

    @PostMapping("/selectAll")
    @ApiOperation("根据门店编码集合查询上月销量")
    public HttpResponse selectStore(@RequestBody List<String> storeAll){
        return storeMonthService.selectStoreMonths(storeAll);
    }
}
