package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageRequestVO;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.dao.ReportStoreGoodsDao;
import com.aiqin.mgs.order.api.domain.ReportStoreGoods;
import com.aiqin.mgs.order.api.domain.ReportStoreGoodsDetail;
import com.aiqin.mgs.order.api.domain.request.ReportStoreGoodsDetailVo;
import com.aiqin.mgs.order.api.domain.request.ReportStoreGoodsVo;
import com.aiqin.mgs.order.api.service.ReportStoreGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private ReportStoreGoodsDao reportStoreGoodsDao;

    @ApiOperation("门店补货报表列表")
    @PostMapping("/getlist")
    public HttpResponse<PageResData<ReportStoreGoods>> getlist(@RequestBody PageRequestVO<ReportStoreGoodsVo> searchVo) {
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

    @ApiOperation("门店补货报表定时任务测试")
    @PostMapping("/test")
    public HttpResponse test() {
        reportStoreGoodsService.dealWith();
//        List<ReportStoreGoods> records=new ArrayList<>();
//        ReportStoreGoods ss=new ReportStoreGoods();
//        ss.setStoreCode("60000028");
//        ss.setChainRatio(new BigDecimal(100));
//        ss.setTongRatio(new BigDecimal(100));
//        ss.setCountTime("2020-02");
//        ss.setCreateTime(new Date());
//        records.add(ss);
//        reportStoreGoodsDao.insertBatch(records);
        return HttpResponse.success();
    }

}
