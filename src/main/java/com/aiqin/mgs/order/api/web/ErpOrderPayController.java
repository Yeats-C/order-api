package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderPay;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderSending;
import com.aiqin.mgs.order.api.domain.response.order.OrderPayResultResponse;
import com.aiqin.mgs.order.api.domain.response.order.OrderSendingPayResultResponse;
import com.aiqin.mgs.order.api.service.ErpOrderPayService;
import com.aiqin.mgs.order.api.service.ErpOrderService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 支付相关接口类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/4 15:08
 */
@RestController
@RequestMapping("/erpOrderPayController")
@Api("订单支付相关接口类")
public class ErpOrderPayController {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderPayController.class);

    @Resource
    private ErpOrderPayService erpOrderPayService;

    @Resource
    private ErpOrderService erpOrderService;

    @PostMapping("/orderPay")
    @ApiOperation(value = "订单发起支付")
    public HttpResponse orderPay(@RequestBody OrderStoreOrderPay orderStoreOrderPay) {
        HttpResponse response = HttpResponse.success();
        try {
            AuthUtil.loginCheck();

            //发起支付
            erpOrderPayService.orderPay(orderStoreOrderPay);
            //开始轮询
            erpOrderPayService.orderPayPolling(orderStoreOrderPay);
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
    public HttpResponse orderPayResult(@RequestBody OrderStoreOrderPay orderStoreOrderPay) {
        HttpResponse response = HttpResponse.success();
        try {
            OrderPayResultResponse orderPayResultResponse = erpOrderPayService.orderPayResult(orderStoreOrderPay);
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
    public HttpResponse orderPayCallback(@RequestBody OrderStoreOrderPay orderStoreOrderPay) {
        HttpResponse response = HttpResponse.success();
        try {
            erpOrderPayService.orderPayCallback(orderStoreOrderPay);
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
    public HttpResponse orderPayRepay(@RequestBody OrderStoreOrderPay orderStoreOrderPay) {
        HttpResponse response = HttpResponse.success();
        try {
            AuthUtil.loginCheck();
            erpOrderPayService.orderPayRepay(orderStoreOrderPay);
        } catch (BusinessException e) {
            logger.error("异常信息：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("异常信息：{}", e);
            response = HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/getOrderSendingFee")
    @ApiOperation(value = "获取订单物流费用信息")
    public HttpResponse getOrderSendingFee(@RequestBody OrderStoreOrderInfo orderStoreOrderInfo) {
        HttpResponse response = HttpResponse.success();
        try {
            OrderStoreOrderInfo order = erpOrderService.getOrderSendingFee(orderStoreOrderInfo);
            response.setData(order);
        } catch (BusinessException e) {
            logger.error("异常信息：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("异常信息：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/orderSendingPay")
    @ApiOperation(value = "支付物流费用")
    public HttpResponse orderSendingPay(@RequestBody OrderStoreOrderSending orderStoreOrderSending) {
        HttpResponse response = HttpResponse.success();
        try {
            AuthUtil.loginCheck();

        } catch (BusinessException e) {
            logger.error("异常信息：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("异常信息：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/orderSendingPayResult")
    @ApiOperation(value = "订单物流费用支付结果查询")
    public HttpResponse orderSendingPayResult(@RequestBody OrderStoreOrderInfo orderStoreOrderInfo) {
        HttpResponse response = HttpResponse.success();
        try {
            OrderSendingPayResultResponse orderSendingPayResultResponse = erpOrderPayService.orderSendingPayResult(orderStoreOrderInfo);
            response.setData(orderSendingPayResultResponse);
        } catch (BusinessException e) {
            logger.error("异常信息：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("异常信息：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }


}
