package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.request.orderList.OrderListVo3;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderDetailVO;
import com.aiqin.mgs.order.api.domain.response.RejectVoResponse;
import com.aiqin.mgs.order.api.domain.response.ReturnOrderDetailBySearchResponse;
import com.aiqin.mgs.order.api.domain.response.ReturnOrderResponse;
import com.aiqin.mgs.order.api.service.ReturnOrderDetailService;
import com.aiqin.mgs.order.api.service.returnorder.ReturnOrderInfoService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "退货单查询")
@RestController
@RequestMapping("/returnOrderInfo")
@Slf4j
public class ReturnOrderDetailSearchController {

    @Autowired
    private ReturnOrderDetailService returnOrderDetailService;

    @GetMapping("/search")
    @ApiOperation("退货单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "begin_time", value = "开始时间", type = "String"),
            @ApiImplicitParam(name = "finish_time", value = "结束时间", type = "String"),
            @ApiImplicitParam(name = "return_order_code", value = "退货单code", type = "String"),
            @ApiImplicitParam(name = "customer_name", value = "客户", type = "String"),
            @ApiImplicitParam(name = "transport_center_name", value = "仓库", type = "String"),
            @ApiImplicitParam(name = "warehouse_name", value = "库房", type = "String"),
            @ApiImplicitParam(name = "return_order_status", value = "退货单单状态" +
                    "1-待审核，2-审核通过，3-订单同步中，4-等待退货验收，5-等待退货入库，6-等待审批，11-退货完成，12-退款完成，97-退货终止，98-审核不通过，99-已取消", type = "Integer"),
            @ApiImplicitParam(name = "order_store_code", value = "订单号", type = "String"),
            @ApiImplicitParam(name = "company_code", value = "公司编码", type = "String"),
            @ApiImplicitParam(name = "page_no", value = "当前页", type = "Integer"),
            @ApiImplicitParam(name = "page_size", value = "每页条数", type = "Integer")})
    public HttpResponse<List<ReturnOrderDetailBySearchResponse>> searchReturnOrderList(@RequestParam(value = "begin_time", required = false) String beginTime,
                                                                                       @RequestParam(value = "finish_time", required = false) String finishTime,
                                                                                       @RequestParam(value = "return_order_code", required = false) String returnOrderCode,
                                                                                       @RequestParam(value = "customer_code", required = false) String customerName,
                                                                                       @RequestParam(value = "transport_center_name", required = false) String transportCenterName,
                                                                                       @RequestParam(value = "warehouse_name", required = false) String warehouseName,
                                                                                       @RequestParam(value = "return_order_status", required = false) Integer returnOrderStatus,
                                                                                       @RequestParam(value = "order_store_code", required = false) String orderStoreCode,
                                                                                       @RequestParam(value = "company_code", required = false) String companyCode,
                                                                                       @RequestParam(value = "page_no", required = false) Integer pageNo,
                                                                                       @RequestParam(value = "page_size", required = false) Integer pageSize) {
        OrderListVo3 orderListVo3 = new OrderListVo3(beginTime, finishTime, returnOrderCode, customerName, transportCenterName, warehouseName, returnOrderStatus, orderStoreCode,companyCode);
        orderListVo3.setPageNo(pageNo);
        orderListVo3.setPageSize(pageSize);
        return returnOrderDetailService.searchList(orderListVo3);
    }



    @GetMapping("/view")
    @ApiOperation("查看退货单详情")
    public HttpResponse<ReturnOrderResponse> searchReturnOrderDetailByReturnOrderCode(@ApiParam("退货单号")@RequestParam(value = "return_order_code") String returnOrderCode){
        return returnOrderDetailService.searchReturnOrderDetailByReturnOrderCode(returnOrderCode);
    }
}