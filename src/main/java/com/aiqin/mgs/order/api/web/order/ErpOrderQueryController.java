package com.aiqin.mgs.order.api.web.order;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.component.enums.ErpPayWayEnum;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.service.order.ErpOrderQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/erpOrderQueryController")
@Api(tags = "ERP订单查询")
public class ErpOrderQueryController {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderQueryController.class);

    @Resource
    private ErpOrderQueryService erpOrderQueryService;

    @PostMapping("/findOrderList")
    @ApiOperation(value = "查询销售单订单列表")
    public HttpResponse findOrderList(@RequestBody ErpOrderInfo erpOrderInfo) {
        HttpResponse response = HttpResponse.success();
        try {
            PageResData<ErpOrderInfo> orderList = erpOrderQueryService.findOrderList(erpOrderInfo);
            response.setData(orderList);
        } catch (BusinessException e) {
            logger.error("查询销售单订单列表异常：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("查询销售单订单列表异常：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/getOrderDetail")
    @ApiOperation(value = "查询订单详情")
    public HttpResponse getOrderDetail(@RequestBody ErpOrderInfo erpOrderInfo) {
        HttpResponse response = HttpResponse.success();
        try {
            ErpOrderInfo orderDetailByOrderCode = erpOrderQueryService.getOrderDetailByOrderCode(erpOrderInfo.getOrderCode());
            response.setData(orderDetailByOrderCode);
        } catch (BusinessException e) {
            logger.error("查询订单详情异常：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("查询订单详情异常：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @GetMapping("/findOrderTypeList")
    @ApiOperation(value = "获取订单类型选项列表")
    public HttpResponse findOrderTypeList() {
        HttpResponse response = HttpResponse.success();
        try {
            response.setData(ErpOrderTypeEnum.SELECT_LIST);
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @GetMapping("/findOrderStatusList")
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

    @GetMapping("/findOrderPayStatusList")
    @ApiOperation(value = "获取订单支付状态选项列表")
    public HttpResponse findOrderPayStatusList() {
        HttpResponse response = HttpResponse.success();
        try {
            response.setData(ErpPayStatusEnum.SELECT_LIST);
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @GetMapping("/findOrderPayWayList")
    @ApiOperation(value = "获取订单支付方式选项列表")
    public HttpResponse findOrderPayWayList() {
        HttpResponse response = HttpResponse.success();
        try {
            response.setData(ErpPayWayEnum.SELECT_LIST);
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }
}
