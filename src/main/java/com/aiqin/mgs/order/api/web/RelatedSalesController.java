package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageRequestVO;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.RelatedSales;
import com.aiqin.mgs.order.api.domain.request.RelatedSalesVo;
import com.aiqin.mgs.order.api.service.RelatedSalesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return new HttpResponse(relatedSalesService.getList(searchVo));
    }

    @ApiOperation("关联销售新增")
    @PostMapping("/insert")
    public HttpResponse<Boolean> addRelatedSales(@RequestBody RelatedSales relatedSales) {
        return new HttpResponse<>(relatedSalesService.insert(relatedSales));
    }

    @ApiOperation("关联销售修改")
    @PostMapping("/update")
    public HttpResponse<Boolean> update(@RequestBody RelatedSales relatedSales) {
        return new HttpResponse<>(relatedSalesService.update(relatedSales));
    }

    @ApiOperation("关联销售修改--生效/失效")
    @GetMapping("/updateStatus")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "关联销售id", dataType = "long", paramType = "query", required = true),
            @ApiImplicitParam(name = "status", value = "生效状态 0:生效 1:失效", dataType = "int", paramType = "query", required = true)})
    public HttpResponse<Boolean> updateStatus(Long id,Integer status) {
        return new HttpResponse<>(relatedSalesService.updateStatus(id,status));
    }

    @ApiOperation("根据品类编码，查询sku信息")
    @GetMapping("/getBySalseCategoryId")
    @ApiImplicitParams({@ApiImplicitParam(name = "salse_category_id", value = "销售品类ID", dataType = "String", paramType = "query", required = true)})
    public HttpResponse<RelatedSales> selectBySalseCategoryId(String salse_category_id) {
        return new HttpResponse(relatedSalesService.selectBySalseCategoryId(salse_category_id));
    }

    @ApiOperation("根据一二三四类品类编码，查询sku信息--提供给pos")
    @GetMapping("/getByCategoryLevel")
    @ApiImplicitParams({@ApiImplicitParam(name = "categoryLevel", value = "销售品类id", dataType = "String", paramType = "query", required = true)})
    public HttpResponse<RelatedSales> getByCategoryLevel(String categoryLevel) {
        return new HttpResponse(relatedSalesService.getByCategoryLevel(categoryLevel));
    }

}
