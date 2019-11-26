package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;
import com.aiqin.mgs.order.api.domain.response.OrderPayResultResponse;
import com.aiqin.mgs.order.api.service.ErpOrderPayService;
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
@RequestMapping("/erpOrderPayController")
@Api("erp订单支付")
public class ErpOrderPayController {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderPayController.class);

    @Resource
    private ErpOrderPayService erpOrderPayService;

    @PostMapping("/orderPay")
    @ApiOperation(value = "订单发起支付")
    public HttpResponse orderPay(@RequestBody OrderStoreOrderInfo orderStoreOrderInfo) {
        HttpResponse response = HttpResponse.success();
        try {
            //发起支付
            erpOrderPayService.orderPay(orderStoreOrderInfo);
            //开始轮询
            erpOrderPayService.orderPayPolling(orderStoreOrderInfo);
        } catch (BusinessException e) {
            logger.error("异常信息：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("异常信息：{}", e);
            response = HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/orderPayResult")
    @ApiOperation(value = "订单支付结果查询")
    public HttpResponse orderPayResult(@RequestBody OrderStoreOrderInfo orderStoreOrderInfo) {
        HttpResponse response = HttpResponse.success();
        try {
            OrderPayResultResponse orderPayResultResponse = erpOrderPayService.orderPayResult(orderStoreOrderInfo);
            response.setData(orderPayResultResponse);
        } catch (BusinessException e) {
            logger.error("异常信息：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("异常信息：{}", e);
            response = HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/orderPayCallback")
    @ApiOperation(value = "订单支付回调接口")
    public HttpResponse orderPayCallback(@RequestBody OrderStoreOrderInfo orderStoreOrderInfo) {
        HttpResponse response = HttpResponse.success();
        try {
            erpOrderPayService.orderPayCallback(orderStoreOrderInfo);
        } catch (BusinessException e) {
            logger.error("异常信息：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("异常信息：{}", e);
            response = HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/orderPayRepay")
    @ApiOperation(value = "校验并同步订单支付状态")
    public HttpResponse orderPayRepay(@RequestBody OrderStoreOrderInfo orderStoreOrderInfo) {
        HttpResponse response = HttpResponse.success();
        try {
            erpOrderPayService.orderPayRepay(orderStoreOrderInfo);
        } catch (BusinessException e) {
            logger.error("异常信息：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("异常信息：{}", e);
            response = HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
        return response;
    }

}
