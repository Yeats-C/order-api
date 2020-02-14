package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageRequestVO;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.RelatedSales;
import com.aiqin.mgs.order.api.domain.request.RelatedSalesVo;
import com.aiqin.mgs.order.api.service.RelatedSalesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: RelatedSalesController
 * date: 2020/2/14 14:03
 * author: hantao
 * version: 1.0
 */
@RestController
@Api(tags = "关联销售相关接口")
@RequestMapping("/relatedSales")
public class RelatedSalesController {

    @Autowired
    private RelatedSalesService relatedSalesService;

    @ApiOperation("关联销售列表")
    @PostMapping("/getlist")
    public HttpResponse<PageResData<RelatedSales>> getlist(@RequestBody PageRequestVO<RelatedSalesVo> searchVo) {
        return new HttpResponse<>(relatedSalesService.getList(searchVo));
    }

    @ApiOperation("关联销售新增")
    @PostMapping("/insert")
    public HttpResponse addRelatedSales(@RequestBody RelatedSales relatedSales) {
        return relatedSalesService.insert(relatedSales);
    }

}
