package com.aiqin.mgs.order.api.web.purchase;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.PurchaseOrderDetail;
import com.aiqin.mgs.order.api.domain.request.purchase.PurchaseApplyRequest;
import com.aiqin.mgs.order.api.domain.request.purchase.PurchaseOrderProductRequest;
import com.aiqin.mgs.order.api.domain.response.purchase.*;
import com.aiqin.mgs.order.api.service.purchase.PurchaseManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

    @Resource
    private PurchaseManageService purchaseManageService;

    @GetMapping("/order/list")
    @ApiOperation("采购单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "create_begin_time", value = "创建开始时间", type = "String"),
            @ApiImplicitParam(name = "create_finish_time", value = "创建结束时间", type = "String"),
            @ApiImplicitParam(name = "update_begin_time", value = "操作开始时间", type = "String"),
            @ApiImplicitParam(name = "update_finish_time", value = "操作结束时间", type = "String"),
            @ApiImplicitParam(name = "purchase_group_code", value = "采购组编码", type = "String"),
            @ApiImplicitParam(name = "purchase_order_code", value = "采购单号", type = "String"),
            @ApiImplicitParam(name = "supplier_code", value = "供应商编码", type = "String"),
            @ApiImplicitParam(name = "supplier_name", value = "供应商名称", type = "String"),
            @ApiImplicitParam(name = "transport_center_code", value = "仓库", type = "String"),
            @ApiImplicitParam(name = "warehouse_code", value = "库房", type = "String"),
            @ApiImplicitParam(name = "purchase_order_status", value = "采购单审核状态 " +
                    "3.备货确认 4.发货确认  5.入库开始 6.入库中 7.已入库  8.完成 9.取消", type = "Integer"),
            @ApiImplicitParam(name = "purchase_source", value = "采购单来源 0.采购申请 1.订单 ", type = "Integer"),
            @ApiImplicitParam(name = "purchase_apply_code", value = "采购单来源单号", type = "String"),
            // @ApiImplicitParam(name = "company_code", value = "公司编码", type = "String"),
            @ApiImplicitParam(name = "page_no", value = "当前页", type = "Integer"),
            @ApiImplicitParam(name = "page_size", value = "每页条数", type = "Integer")})
    public HttpResponse<List<PurchaseOrder>> applyProductList(@RequestParam(value = "create_begin_time", required = false) String createBeginTime,
                                                              @RequestParam(value = "create_finish_time", required = false) String createFinishTime,
                                                              @RequestParam(value = "update_begin_time", required = false) String updateBeginTime,
                                                              @RequestParam(value = "update_finish_time", required = false) String updateFinishTime,
                                                              @RequestParam(value = "purchase_group_code", required = false) String purchaseGroupCode,
                                                              @RequestParam(value = "purchase_order_code", required = false) String purchaseOrderCode,
                                                              @RequestParam(value = "supplier_code", required = false) String supplierCode,
                                                              @RequestParam(value = "supplier_name", required = false) String supplierName,
                                                              @RequestParam(value = "transport_center_code", required = false) String transportCenterCode,
                                                              @RequestParam(value = "warehouse_code", required = false) String warehouseCode,
                                                              @RequestParam(value = "purchase_order_status", required = false) Integer purchaseOrderStatus,
                                                              @RequestParam(value = "purchase_source", required = false) Integer purchaseSource,
                                                              @RequestParam(value = "purchase_apply_code", required = false) String purchaseApplyCode,
                                                              @RequestParam(value = "page_no", required = false) Integer pageNo,
                                                              @RequestParam(value = "page_size", required = false) Integer pageSize) {
        PurchaseApplyRequest purchaseApplyRequest = new PurchaseApplyRequest(purchaseGroupCode, createBeginTime, createFinishTime, updateBeginTime, updateFinishTime,
                purchaseOrderCode, supplierCode, supplierName, transportCenterCode, warehouseCode, purchaseOrderStatus, purchaseSource, purchaseApplyCode,null);
        purchaseApplyRequest.setPageSize(pageSize);
        purchaseApplyRequest.setPageNo(pageNo);
        return purchaseManageService.purchaseOrderList(purchaseApplyRequest);
    }

    @GetMapping("/order/details")
    @ApiOperation("查询采购单详情-采购信息")
    public HttpResponse<PurchaseOrder> purchaseOrderDetails(@RequestParam("purchase_order_id") String purchaseOrderId) {
        return purchaseManageService.purchaseOrderDetails(purchaseOrderId);
    }

    @GetMapping("/order/product")
    @ApiOperation("查询采购单商品-商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "purchase_order_code", value = "采购单code", type = "String"),
            @ApiImplicitParam(name = "is_page", value = "是否分页 0.分页 1.不分页", type = "Integer"),
            @ApiImplicitParam(name = "page_no", value = "当前页", type = "Integer"),
            @ApiImplicitParam(name = "page_size", value = "每页条数", type = "Integer")})
    public HttpResponse<PurchaseOrderProduct> purchaseOrderProduct(@RequestParam("purchase_order_code") String purchaseOrderCode,
                                                                   @RequestParam(value = "is_page", required = false) Integer isPage,
                                                                   @RequestParam(value = "page_no", required = false) Integer pageNo,
                                                                   @RequestParam(value = "page_size", required = false) Integer pageSize) {
        PurchaseOrderProductRequest request = new PurchaseOrderProductRequest(purchaseOrderCode, isPage);
        request.setPageSize(pageSize);
        request.setPageNo(pageNo);
        return purchaseManageService.purchaseOrderProduct(request);
    }

    // @GetMapping("/order/log")
    // @ApiOperation("查询采购单-操作日志")
    // public HttpResponse<List<OrderOperationLog>> purchaseOrderLog(@RequestParam("operation_id") String operationId) {
    //     return purchaseManageService.purchaseOrderLog(operationId);
    // }

    @GetMapping("/order/amount")
    @ApiOperation("查询采购单-采购数量金额/实际数量金额")
    public HttpResponse<PurchaseApplyProductInfoResponse> purchaseOrderAmount(@RequestParam("purchase_order_code") String purchaseOrderCode) {
        return purchaseManageService.purchaseOrderAmount(purchaseOrderCode);
    }
}
