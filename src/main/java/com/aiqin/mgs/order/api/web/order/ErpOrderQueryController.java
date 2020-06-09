package com.aiqin.mgs.order.api.web.order;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderQueryRequest;
import com.aiqin.mgs.order.api.service.order.ErpOrderQueryService;
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
@RequestMapping("/erpOrderQueryController")
@Api(tags = "ERP订单查询")
public class ErpOrderQueryController {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderQueryController.class);

    @Resource
    private ErpOrderQueryService erpOrderQueryService;

    @PostMapping("/findErpOrderList")
    @ApiOperation(value = "ERP查询销售单订单列表")
    public HttpResponse<PageResData<ErpOrderInfo>> findErpOrderList(@RequestBody ErpOrderQueryRequest erpOrderQueryRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            PageResData<ErpOrderInfo> orderList = erpOrderQueryService.findErpOrderList(erpOrderQueryRequest);
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

    @PostMapping("/findStoreOrderList")
    @ApiOperation(value = "爱掌柜查询销售单订单列表")
    public HttpResponse<PageResData<ErpOrderInfo>> findStoreOrderList(@RequestBody ErpOrderQueryRequest erpOrderQueryRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            PageResData<ErpOrderInfo> orderList = erpOrderQueryService.findStoreOrderList(erpOrderQueryRequest);
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

    @PostMapping("/findErpRackOrderList")
    @ApiOperation(value = "查询货架订单列表")
    public HttpResponse<PageResData<ErpOrderInfo>> findErpRackOrderList(@RequestBody ErpOrderQueryRequest erpOrderQueryRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            PageResData<ErpOrderInfo> orderList = erpOrderQueryService.findErpRackOrderList(erpOrderQueryRequest);
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
    public HttpResponse<ErpOrderInfo> getOrderDetail(@RequestBody ErpOrderQueryRequest erpOrderQueryRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            ErpOrderInfo orderDetailByOrderCode = erpOrderQueryService.getOrderDetailByOrderCode(erpOrderQueryRequest.getOrderStoreCode());
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

    @PostMapping("/approvalFindStoreOrderList")
    @ApiOperation(value = "赠送市值审批--查询销售单订单列表")
    public HttpResponse<PageResData<ErpOrderInfo>> approvalFindStoreOrderList(@RequestBody ErpOrderQueryRequest erpOrderQueryRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            PageResData<ErpOrderInfo> orderList = erpOrderQueryService.findStoreOrderList(erpOrderQueryRequest);
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

    @PostMapping("/findErpWholesaleOrderList")
    @ApiOperation(value = "ERP查询批发订单列表")
    public HttpResponse<PageResData<ErpOrderInfo>> findErpWholesaleOrderList(@RequestBody ErpOrderQueryRequest erpOrderQueryRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            PageResData<ErpOrderInfo> orderList = erpOrderQueryService.findErpWholesaleOrderList(erpOrderQueryRequest);
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
}
