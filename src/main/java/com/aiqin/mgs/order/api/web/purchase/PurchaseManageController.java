package com.aiqin.mgs.order.api.web.purchase;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.request.purchase.PurchaseApplyRequest;
import com.aiqin.mgs.order.api.domain.request.purchase.PurchaseOrderProductRequest;
import com.aiqin.mgs.order.api.domain.response.purchase.*;
import com.aiqin.mgs.order.api.service.purchase.PurchaseManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xq
 * @Date 2020/3/5
 */

@Api(tags = "采购单管理")
@RequestMapping("/manage")
@RestController
public class PurchaseManageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseManageController.class);

    @Resource
    private PurchaseManageService purchaseManageService;

    @GetMapping("/order/list")
    @ApiOperation("采购单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "begin_time", value = "创建开始时间", type = "String"),
            @ApiImplicitParam(name = "finish_time", value = "创建结束时间", type = "String"),
            @ApiImplicitParam(name = "purchase_order_code", value = "采购单号", type = "String"),
            @ApiImplicitParam(name = "supplier_code", value = "供应商编码", type = "String"),
            @ApiImplicitParam(name = "transport_center_code", value = "仓库", type = "String"),
            @ApiImplicitParam(name = "warehouse_code", value = "库房", type = "String"),
            @ApiImplicitParam(name = "purchase_order_status", value = "采购单审核状态 0.待确认1.完成 2.取消", type = "Integer"),
            @ApiImplicitParam(name = "source_code", value = "采购单来源单号", type = "String"),
            @ApiImplicitParam(name = "purchase_mode", value = "采购方式 0.直送 1.配送  2.铺采直送", type = "Integer"),
            @ApiImplicitParam(name = "page_no", value = "当前页", type = "Integer"),
            @ApiImplicitParam(name = "page_size", value = "每页条数", type = "Integer")})
    public HttpResponse<PageResData<PurchaseOrder>> applyProductList(@RequestParam(value = "begin_time", required = false) String beginTime,
                                                                      @RequestParam(value = "finish_time", required = false) String finishTime,
                                                                      @RequestParam(value = "purchase_order_code", required = false) String purchaseOrderCode,
                                                                      @RequestParam(value = "supplier_code", required = false) String supplierCode,
                                                                      @RequestParam(value = "transport_center_code", required = false) String transportCenterCode,
                                                                      @RequestParam(value = "warehouse_code", required = false) String warehouseCode,
                                                                      @RequestParam(value = "purchase_order_status", required = false) Integer purchaseOrderStatus,
                                                                      @RequestParam(value = "source_code", required = false) String sourceCode,
                                                                      @RequestParam(value = "purchase_mode", required = false) Integer purchaseMode,
                                                                      @RequestParam(value = "page_no", required = false) Integer pageNo,
                                                                      @RequestParam(value = "page_size", required = false) Integer pageSize) {
        PurchaseApplyRequest purchaseApplyRequest = new PurchaseApplyRequest(beginTime, finishTime,
                purchaseOrderCode, supplierCode, transportCenterCode, warehouseCode, purchaseOrderStatus, sourceCode, purchaseMode);
        purchaseApplyRequest.setPageSize(pageSize);
        purchaseApplyRequest.setPageNo(pageNo);
        LOGGER.info("查询采购单列表请求,入参:{}", purchaseApplyRequest.toString());
        return purchaseManageService.purchaseOrderList(purchaseApplyRequest);
    }

    @GetMapping("/order/details")
    @ApiOperation("查询采购单详情-采购信息")
    public HttpResponse<PurchaseDetailResponse> purchaseOrderDetails(@RequestParam("purchase_order_id") String purchaseOrderId) {
        return purchaseManageService.purchaseOrderDetails(purchaseOrderId);
    }

    @GetMapping("/order/product")
    @ApiOperation("查询采购单商品-商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "purchase_order_code", value = "采购单code", type = "String"),
            @ApiImplicitParam(name = "is_page", value = "是否分页 0.分页 1.不分页", type = "Integer"),
            @ApiImplicitParam(name = "page_no", value = "当前页", type = "Integer"),
            @ApiImplicitParam(name = "page_size", value = "每页条数", type = "Integer")})
    public HttpResponse<PageResData<PurchaseOrderProduct>> purchaseOrderProduct(@RequestParam("purchase_order_code") String purchaseOrderCode,
                                                                   @RequestParam(value = "is_page", required = false) Integer isPage,
                                                                   @RequestParam(value = "page_no", required = false) Integer pageNo,
                                                                   @RequestParam(value = "page_size", required = false) Integer pageSize) {
        PurchaseOrderProductRequest request = new PurchaseOrderProductRequest(purchaseOrderCode, isPage);
        request.setPageSize(pageSize);
        request.setPageNo(pageNo);
        return purchaseManageService.purchaseOrderProduct(request);
    }

}
