package com.aiqin.mgs.order.api.web.order;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderPayCallbackRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderPayRequest;
import com.aiqin.mgs.order.api.domain.response.order.OrderPayResultResponse;
import com.aiqin.mgs.order.api.service.order.ErpOrderPayService;
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
 * 订单支付controller
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 16:39
 */
@RestController
@RequestMapping("/erpOrderPayController")
@Api("订单创建")
public class ErpOrderPayController {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderCreateController.class);

    @Resource
    private ErpOrderPayService erpOrderPayService;

    @PostMapping("/orderPay")
    @ApiOperation(value = "订单发起支付")
    public HttpResponse orderPay(@RequestBody ErpOrderPayRequest erpOrderPayRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            AuthUtil.loginCheck();
//            //发起支付
            String payId = erpOrderPayService.orderPay(erpOrderPayRequest);
//            //开始轮询
            erpOrderPayService.payPolling(payId);
        } catch (BusinessException e) {
            logger.error("订单支付异常：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("订单支付异常：{}", e);
            response = HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/orderPayResult")
    @ApiOperation(value = "订单支付结果查询")
    public HttpResponse orderPayResult(@RequestBody ErpOrderPayRequest erpOrderPayRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            OrderPayResultResponse orderPayResultResponse = erpOrderPayService.orderPayResult(erpOrderPayRequest);
            response.setData(orderPayResultResponse);
        } catch (BusinessException e) {
            logger.error("订单支付结果查询异常：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("订单支付结果查询异常：{}", e);
            response = HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/orderPayCallback")
    @ApiOperation(value = "订单支付回调")
    public HttpResponse orderPayCallback(@RequestBody ErpOrderPayCallbackRequest erpOrderPayCallbackRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            //修改支付单状态
            String payId = erpOrderPayService.orderPayCallback(erpOrderPayCallbackRequest);
            //支付之后后续逻辑
//            erpOrderPayService.endPay(payId);
        } catch (BusinessException e) {
            logger.error("订单支付回调异常：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("订单支付回调异常：{}", e);
            response = HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/orderPayRepay")
    @ApiOperation(value = "校验并同步订单支付状态")
    public HttpResponse orderPayRepay(@RequestBody ErpOrderPayRequest erpOrderPayRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            AuthUtil.loginCheck();
//            erpOrderPayService.orderPayRepay(orderStoreOrderPay);
        } catch (BusinessException e) {
            logger.error("校验并同步订单支付状态异常：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("校验并同步订单支付状态异常：{}", e);
            response = HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/getOrderSendingFee")
    @ApiOperation(value = "获取订单物流费用信息")
    public HttpResponse getOrderSendingFee(@RequestBody ErpOrderPayRequest erpOrderPayRequest) {
        HttpResponse response = HttpResponse.success();
        try {
//            OrderStoreOrderInfo order = erpOrderService.getOrderSendingFee(orderStoreOrderInfo);
//            response.setData(order);
        } catch (BusinessException e) {
            logger.error("获取订单物流费用信息异常：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("获取订单物流费用信息异常：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/orderSendingPay")
    @ApiOperation(value = "支付物流费用")
    public HttpResponse orderSendingPay(@RequestBody ErpOrderPayRequest erpOrderPayRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            AuthUtil.loginCheck();

        } catch (BusinessException e) {
            logger.error("支付物流费用异常：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("支付物流费用异常：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/orderSendingPayResult")
    @ApiOperation(value = "订单物流费用支付结果查询")
    public HttpResponse orderSendingPayResult(@RequestBody ErpOrderPayRequest erpOrderPayRequest) {
        HttpResponse response = HttpResponse.success();
        try {
//            OrderSendingPayResultResponse orderSendingPayResultResponse = erpOrderPayService.orderSendingPayResult(orderStoreOrderInfo);
//            response.setData(orderSendingPayResultResponse);
        } catch (BusinessException e) {
            logger.error("订单物流费用支付结果查询异常：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("订单物流费用支付结果查询异常：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }
}
