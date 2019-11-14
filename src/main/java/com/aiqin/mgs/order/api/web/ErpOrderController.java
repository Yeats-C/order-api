package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;
import com.aiqin.mgs.order.api.domain.response.ErpOrderDetailResponse;
import com.aiqin.mgs.order.api.service.ErpOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/erpOrderController")
@Api("erp订单")
public class ErpOrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErpOrderController.class);

    @Resource
    private ErpOrderService erpOrderService;

    @GetMapping("/findOrderList")
    @ApiOperation(value = "查询销售单订单列表")
    public HttpResponse findOrderList(@RequestBody OrderStoreOrderInfo orderStoreOrderInfo) {
        HttpResponse response = HttpResponse.success();
        try {
            PageResData<OrderStoreOrderInfo> list = erpOrderService.findOrderList(orderStoreOrderInfo);
            response.setData(list);
        } catch (Exception e) {
            LOGGER.error("异常信息：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @GetMapping("/getOrderDetail")
    @ApiOperation(value = "查询erp订单详情")
    public HttpResponse getOrderDetail(@RequestBody OrderStoreOrderInfo orderStoreOrderInfo) {
        HttpResponse response = HttpResponse.success();
        try {
            ErpOrderDetailResponse orderDetail = erpOrderService.getOrderDetail(orderStoreOrderInfo);
            response.setData(orderDetail);
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @GetMapping("/saveOrder")
    @ApiOperation(value = "保存erp订单")
    public HttpResponse saveOrder(@RequestBody OrderStoreOrderInfo orderStoreOrderInfo) {
        HttpResponse response = HttpResponse.success();
        try {
            erpOrderService.saveOrder(orderStoreOrderInfo);
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            response = HttpResponse.failure(ResultCode.ADD_EXCEPTION);
        }
        return response;
    }

    @GetMapping("/changeOrderStatus")
    @ApiOperation(value = "修改订单状态")
    public HttpResponse changeOrderStatus(@RequestBody OrderStoreOrderInfo orderStoreOrderInfo) {
        HttpResponse response = HttpResponse.success();
        try {

        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

}
