package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.OrderTypeEnum;
import com.aiqin.mgs.order.api.component.enums.PayStatusEnum;
import com.aiqin.mgs.order.api.component.enums.PayWayEnum;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;
import com.aiqin.mgs.order.api.domain.request.ErpOrderSaveRequest;
import com.aiqin.mgs.order.api.domain.response.ErpOrderDetailResponse;
import com.aiqin.mgs.order.api.service.ErpOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/findOrderList")
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

    @PostMapping("/getOrderDetail")
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

    @PostMapping("/saveOrder")
    @ApiOperation(value = "创建订单")
    public HttpResponse saveOrder(@RequestBody ErpOrderSaveRequest erpOrderSaveRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            ErpOrderDetailResponse erpOrderDetailResponse = erpOrderService.saveOrder(erpOrderSaveRequest);
            response.setData(erpOrderDetailResponse);
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            response = HttpResponse.failure(ResultCode.ADD_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/saveRackOrder")
    @ApiOperation(value = "创建货架订单")
    public HttpResponse saveRackOrder(@RequestBody ErpOrderSaveRequest erpOrderSaveRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            ErpOrderDetailResponse erpOrderDetailResponse = erpOrderService.saveRackOrder(erpOrderSaveRequest);
            response.setData(erpOrderDetailResponse);
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/deliveryOrder")
    @ApiOperation(value = "订单发货")
    public HttpResponse deliveryOrder(@RequestBody OrderStoreOrderInfo orderStoreOrderInfo) {
        HttpResponse response = HttpResponse.success();
        try {
            erpOrderService.deliveryOrder(orderStoreOrderInfo);
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/signOrder")
    @ApiOperation(value = "订单签收")
    public HttpResponse signOrder(@RequestBody OrderStoreOrderInfo orderStoreOrderInfo) {
        HttpResponse response = HttpResponse.success();
        try {
            erpOrderService.signOrder(orderStoreOrderInfo);
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/cancelOrder")
    @ApiOperation(value = "取消订单")
    public HttpResponse cancelOrder(@RequestBody OrderStoreOrderInfo orderStoreOrderInfo) {
        HttpResponse response = HttpResponse.success();
        try {
            erpOrderService.cancelOrder(orderStoreOrderInfo);
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/findOrderTypeList")
    @ApiOperation(value = "获取订单类型选项列表")
    public HttpResponse findOrderTypeList() {
        HttpResponse response = HttpResponse.success();
        try {
            response.setData(OrderTypeEnum.SELECT_LIST);
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/findOrderStatusList")
    @ApiOperation(value = "获取订单状态选项列表")
    public HttpResponse findOrderStatusList() {
        HttpResponse response = HttpResponse.success();
        try {
            response.setData(ErpOrderStatusEnum.SELECT_LIST);
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/findOrderPayStatusList")
    @ApiOperation(value = "获取订单支付状态选项列表")
    public HttpResponse findOrderPayStatusList() {
        HttpResponse response = HttpResponse.success();
        try {
            response.setData(PayStatusEnum.SELECT_LIST);
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/findOrderPayWayList")
    @ApiOperation(value = "获取订单支付方式选项列表")
    public HttpResponse findOrderPayWayList() {
        HttpResponse response = HttpResponse.success();
        try {
            response.setData(PayWayEnum.SELECT_LIST);
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

}
