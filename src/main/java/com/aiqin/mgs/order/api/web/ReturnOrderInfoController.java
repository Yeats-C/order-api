package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderReqVo;
import com.aiqin.mgs.order.api.service.ReturnOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * description: ReturnOrderInfoController
 * date: 2019/12/19 17:44
 * author: hantao
 * version: 1.0
 */
@RestController
@Api(tags = "订货单退货")
@RequestMapping("/returnOrder")
public class ReturnOrderInfoController {

    @Resource
    private ReturnOrderInfoService returnOrderInfoService;

    @ApiOperation("新增退货信息")
    @PostMapping("/add")
    public HttpResponse<Boolean> save(@RequestBody ReturnOrderReqVo reqVo) {
        return new HttpResponse<>(returnOrderInfoService.save(reqVo));
    }

//    @ApiOperation("列表搜索")
//    @PostMapping("/list")
//    public HttpResponse<PageResData<OrderAfterSaleListVo>> list(@RequestBody OrderAfterSaleSearchVo searchVo) {
//        //todo:放开注释
////        String companyCode = UrlInterceptor.getCurrentAuthToken().getCompanyCode();
////        searchVo.setCompanyCode(companyCode);
//        searchVo.setCompanyCode("01");
//        return new HttpResponse<>(orderAfterSaleService.list(searchVo));
//    }




}
