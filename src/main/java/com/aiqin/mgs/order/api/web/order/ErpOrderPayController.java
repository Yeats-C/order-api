package com.aiqin.mgs.order.api.web.order;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderPay;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderPayCallbackRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderPayRequest;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderLogisticsPayResultResponse;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderLogisticsPrintQueryResponse;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderPayResultResponse;
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
@Api(tags = "ERP订单支付")
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
            erpOrderPayService.orderPay(erpOrderPayRequest);
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
            ErpOrderPayResultResponse erpOrderPayResultResponse = erpOrderPayService.orderPayResult(erpOrderPayRequest);
            response.setData(erpOrderPayResultResponse);
        } catch (BusinessException e) {
            logger.error("订单支付结果查询异常：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("订单支付结果查询异常：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/orderPayCallback")
    @ApiOperation(value = "订单支付回调")
    public HttpResponse orderPayCallback(@RequestBody ErpOrderPayCallbackRequest erpOrderPayCallbackRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            erpOrderPayService.orderPayCallback(erpOrderPayCallbackRequest);
        } catch (BusinessException e) {
            logger.error("订单支付回调异常：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("订单支付回调异常：{}", e);
            response = HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/getOrderPayRepayInfo")
    @ApiOperation(value = "查询确认收款信息")
    public HttpResponse getOrderPayRepayInfo(@RequestBody ErpOrderPayRequest erpOrderPayRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            ErpOrderPay payRepayInfo = erpOrderPayService.getOrderPayRepayInfo(erpOrderPayRequest);
            response.setData(payRepayInfo);
        } catch (BusinessException e) {
            logger.error("校验并同步订单支付状态异常：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("校验并同步订单支付状态异常：{}", e);
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
            erpOrderPayService.orderPayRepay(erpOrderPayRequest);
        } catch (BusinessException e) {
            logger.error("校验并同步订单支付状态异常：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("校验并同步订单支付状态异常：{}", e);
            response = HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/orderLogisticsPay")
    @ApiOperation(value = "支付物流费用")
    public HttpResponse orderLogisticsPay(@RequestBody ErpOrderPayRequest erpOrderPayRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            AuthUtil.loginCheck();
            //发起支付
            erpOrderPayService.orderLogisticsPay(erpOrderPayRequest);
        } catch (BusinessException e) {
            logger.error("支付物流费用异常：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("支付物流费用异常：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/orderLogisticsPayResult")
    @ApiOperation(value = "订单物流费用支付结果轮询")
    public HttpResponse orderLogisticsPayResult(@RequestBody ErpOrderPayRequest erpOrderPayRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            ErpOrderLogisticsPayResultResponse resultResponse = erpOrderPayService.orderLogisticsPayResult(erpOrderPayRequest);
            response.setData(resultResponse);
        } catch (BusinessException e) {
            logger.error("订单物流费用支付结果查询异常：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("订单物流费用支付结果查询异常：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/orderLogisticsPrintQuery")
    @ApiOperation(value = "订单物流费用支付凭证")
    public HttpResponse orderLogisticsPrintQuery(@RequestBody ErpOrderPayRequest erpOrderPayRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            ErpOrderLogisticsPrintQueryResponse queryResponse = erpOrderPayService.orderLogisticsPrintQuery(erpOrderPayRequest);
            response.setData(queryResponse);
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
