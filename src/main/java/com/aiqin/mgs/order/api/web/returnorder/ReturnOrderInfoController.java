package com.aiqin.mgs.order.api.web.returnorder;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderDetailVO;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderReqVo;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderReviewApiReqVo;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderReviewReqVo;
import com.aiqin.mgs.order.api.service.returnorder.ReturnOrderInfoService;
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

    @ApiOperation("操作审核退货单")
    @PostMapping("/updateStatus")
    public HttpResponse<Boolean> updateStatus(@RequestBody ReturnOrderReviewReqVo reqVo) {
        Boolean review = returnOrderInfoService.updateReturnStatus(reqVo);
        return new HttpResponse<>(review);
    }

    /**
     * 修改退货单详情--这里逻辑是根据需求，先根据退货单id删除所有详情记录，然后重新添加
     * @param reqVo
     * @return
     */
    @ApiOperation("修改退货单详情")
    @PostMapping("/updateDetail")
    public HttpResponse<Boolean> updateDetail(@RequestBody ReturnOrderDetailVO reqVo) {
        Boolean review = returnOrderInfoService.updateOrderAfterSaleDetail(reqVo);
        return new HttpResponse<>(review);
    }

    @ApiOperation("提供给供应链--退货单状态修改")
    @PostMapping("/updateStatusApi")
    public HttpResponse<Boolean> updateReturnStatusApi(@RequestBody ReturnOrderReviewApiReqVo reqVo) {
        Boolean review = returnOrderInfoService.updateReturnStatusApi(reqVo);
        return new HttpResponse<>(review);
    }

    @ApiOperation("退货单校验--查看此订单是否已经生成一条退货单，且流程未结束。如果已存在返回true")
    @PostMapping("/check")
    public HttpResponse<Boolean> check(String orderCode) {
        Boolean review = returnOrderInfoService.check(orderCode);
        return new HttpResponse<>(review);
    }

    @ApiOperation("更新物流单信息（向爱掌柜提供接口）")
    @PostMapping("/updateLogistics")
    public HttpResponse<Boolean> updateLogistics(String returnOrderCode,String logisticsCompanyCode,String logisticsCompanyName,String logisticsNo) {
        Boolean review = returnOrderInfoService.updateLogistics(returnOrderCode,logisticsCompanyCode,logisticsCompanyName,logisticsNo);
        return new HttpResponse<>(review);
    }


}
