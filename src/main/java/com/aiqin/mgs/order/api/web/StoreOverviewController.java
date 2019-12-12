package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.service.StoreOverviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        LOGGER.info("根据服务商品名称查询门店的服务项目信息......",storeId,text,year,month);
        return storeOverviewService.storeCustomerFlowMonthly(storeId,text,year,month);
    }

}
