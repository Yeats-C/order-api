package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageRequestVO;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.dao.ReportStoreGoodsDao;
import com.aiqin.mgs.order.api.domain.ReportStoreGoods;
import com.aiqin.mgs.order.api.domain.ReportStoreGoodsDetail;
import com.aiqin.mgs.order.api.domain.request.ReportStoreGoodsDetailVo;
import com.aiqin.mgs.order.api.domain.request.ReportStoreGoodsVo;
import com.aiqin.mgs.order.api.domain.response.report.ReportOrderAndStoreListResponse;
import com.aiqin.mgs.order.api.service.ReportStoreGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * description: RelportStoreGoodsController
 * date: 2020/2/19 17:03
 * author: hantao
 * version: 1.0
 */
@RestController
@Api(tags = "门店补货报表相关接口")
@RequestMapping("/relportStoreGoods")
public class RelportStoreGoodsController {

    @Autowired
    private ReportStoreGoodsService reportStoreGoodsService;

    @ApiOperation("门店补货报表列表")
    @PostMapping("/getlist")
    public HttpResponse<ReportOrderAndStoreListResponse<PageResData<ReportStoreGoods>>> getlist(@RequestBody PageRequestVO<ReportStoreGoodsVo> searchVo) {
        return new HttpResponse(reportStoreGoodsService.getList(searchVo));
    }

    @ApiOperation("门店补货报表新增")
    @PostMapping("/insert")
    public HttpResponse<Boolean> addRelatedSales(@RequestBody ReportStoreGoods relatedSales) {
        return new HttpResponse<>(reportStoreGoodsService.insert(relatedSales));
    }

    @ApiOperation("门店补货报表修改")
    @PostMapping("/update")
    public HttpResponse<Boolean> update(@RequestBody ReportStoreGoods relatedSales) {
        return new HttpResponse<>(reportStoreGoodsService.update(relatedSales));
    }

    @ApiOperation("门店补货列表商品详情")
    @PostMapping("/getCountDetailList")
    public HttpResponse<List<ReportStoreGoodsDetail>> getCountDetailList(@RequestBody ReportStoreGoodsDetailVo searchVo) {
        return new HttpResponse(reportStoreGoodsService.getCountDetailList(searchVo));
    }

    @ApiOperation("门店补货报表定时任务手动执行")
    @PostMapping("/reportTimingTask")
    public HttpResponse reportTimingTask() {
        reportStoreGoodsService.reportTimingTask();
        return HttpResponse.success();
    }

    @ApiOperation("售后管理---各地区退货情况")
    @PostMapping("/areaReturnSituation")
    public HttpResponse areaReturnSituation() {
        reportStoreGoodsService.areaReturnSituation();
        return HttpResponse.success();
    }


}
