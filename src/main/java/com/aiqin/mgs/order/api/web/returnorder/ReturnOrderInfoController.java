package com.aiqin.mgs.order.api.web.returnorder;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageRequestVO;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.domain.ReturnOrderInfo;
import com.aiqin.mgs.order.api.domain.request.returnorder.*;
import com.aiqin.mgs.order.api.service.order.ErpOrderQueryService;
import com.aiqin.mgs.order.api.service.returnorder.ReturnOrderInfoService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

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

    @Resource
    private ErpOrderQueryService erpOrderQueryService;

    @ApiOperation("新增退货信息")
    @PostMapping("/add")
    public HttpResponse save(@RequestBody ReturnOrderReqVo reqVo) {
        return returnOrderInfoService.save(reqVo);
    }

    @ApiOperation("后台销售退货单管理列表（搜索）")
    @PostMapping("/list")
    public HttpResponse<PageResData<ReturnOrderInfo>> list(@RequestBody ReturnOrderSearchVo searchVo) {
        //todo:放开注释
//        String companyCode = UrlInterceptor.getCurrentAuthToken().getCompanyCode();
//        searchVo.setCompanyCode(companyCode);
//        searchVo.setCompanyCode("01");
        return new HttpResponse<>(returnOrderInfoService.list(searchVo));
    }

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

    /*@ApiOperation("提供给供应链--同步是否成功（创建退供单）修改")
    @GetMapping("/updateOrderSuccessApi")
    public HttpResponse<Boolean> updateOrderSuccessApi(String returnOrderCode) {
        Boolean review = returnOrderInfoService.updateOrderSuccessApi(returnOrderCode);
        return new HttpResponse<>(review);
    }*/

    @ApiOperation("退货单校验--查看此订单是否已经生成一条退货单，且流程未结束。如果已存在返回true")
    @GetMapping("/check")
    public HttpResponse<Boolean> check(String orderCode) {
        Boolean review = returnOrderInfoService.check(orderCode);
        return new HttpResponse<>(review);
    }

    @ApiOperation("更新物流单信息（向爱掌柜提供接口）")
    @PostMapping("/updateLogistics")
    public HttpResponse<Boolean> updateLogistics(@RequestBody LogisticsVo logisticsVo) {
        Boolean review = returnOrderInfoService.updateLogistics(logisticsVo);
        return new HttpResponse<>(review);
    }

    @ApiOperation("支付中心---发起退款单回调")
    @PostMapping("/callback")
    public HttpResponse callback(@RequestBody RefundReq reqVo) {
        Boolean review = returnOrderInfoService.callback(reqVo);
        if(review){
            return  HttpResponse.success();
        }
        return HttpResponse.failure(ResultCode.PAY_ERROR);
    }

    @ApiOperation("退货单号（退货详情）")
    @GetMapping("/detail")
    public HttpResponse<ReturnOrderDetailVO> detail(@ApiParam("退货单号") @RequestParam("returnOrderCode") String returnOrderCode) {
        return new HttpResponse<>(returnOrderInfoService.detail(returnOrderCode));
    }

    @ApiOperation("erp售后管理--退货单列表")
    @PostMapping("/getlist")
    public HttpResponse<PageResData<ReturnOrderInfo>> getlist(@RequestBody PageRequestVO<AfterReturnOrderSearchVo> searchVo) {
        return new HttpResponse<>(returnOrderInfoService.getlist(searchVo));
    }

    @ApiOperation("发起退货---根据订单id和和行号计算商品金额")
    @GetMapping("/getAmount")
    @ApiImplicitParams({@ApiImplicitParam(name = "orderCode", value = "订单编码", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "lineCode", value = "行号", dataType = "Long", paramType = "query", required = true),
            @ApiImplicitParam(name = "number", value = "数量", dataType = "Long", paramType = "query", required = true)})
    public HttpResponse getAmount(String orderCode, Long lineCode, Long number) {
        return returnOrderInfoService.getAmount(orderCode,lineCode,number);
    }

    @ApiOperation("erp售后管理--退货单状态下拉选")
    @GetMapping("/getReturnStatus")
    public HttpResponse getReturnStatus() {
        return returnOrderInfoService.getReturnStatus();
    }

    @ApiOperation("退货单列表--查看附件")
    @GetMapping("/getEvidenceUrl")
    public HttpResponse getEvidenceUrl(String returnOrderDetailId) {
        return returnOrderInfoService.getEvidenceUrl(returnOrderDetailId);
    }

    @ApiOperation("根据订单编码查询主订单和详情")
    @GetMapping("/getOrderAndItemByOrderCode")
    public HttpResponse getOrderAndItemByOrderCode(String returnOrderCode) {
        return HttpResponse.success(erpOrderQueryService.getOrderAndItemByOrderCode(returnOrderCode));
    }

    @ApiOperation("支付中心--发起冲减单")
    @GetMapping("/saveWriteDownOrder")
    public HttpResponse saveWriteDownOrder(String orderCode) {
        return returnOrderInfoService.saveWriteDownOrder(orderCode);
    }

    @ApiOperation("支付中心--发起客户退货")
    @GetMapping("/saveCancelOrder")
    public HttpResponse saveCancelOrder(String orderCode) {
        return returnOrderInfoService.saveCancelOrder(orderCode);
    }



    @ApiOperation("erp售后管理--冲减单列表")
    @PostMapping("/getWriteDownOrderList")
    public HttpResponse<PageResData<ReturnOrderInfo>> getWriteDownOrderList(@RequestBody PageRequestVO<WriteDownOrderSearchVo> searchVo) {
        return new HttpResponse<>(returnOrderInfoService.getWriteDownOrderList(searchVo));
    }

    @ApiOperation("支付中心--查询订单状态,同时修改退货单和流水状态")
    @GetMapping("/searchPayOrder")
    public HttpResponse<Boolean> searchPayOrder(String orderCode) {
        return new HttpResponse<>(returnOrderInfoService.searchPayOrder(orderCode));
    }


}
