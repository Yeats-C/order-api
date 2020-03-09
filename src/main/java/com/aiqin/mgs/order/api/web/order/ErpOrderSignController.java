package com.aiqin.mgs.order.api.web.order;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderSignRequest;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderSignResponse;
import com.aiqin.mgs.order.api.service.order.ErpOrderInfoService;
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

/**
 * 订单签收controller
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 11:54
 */
@RestController
@RequestMapping("/erpOrderSignController")
@Api(tags = "ERP订单签收")
public class ErpOrderSignController {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderSignController.class);

    @Resource
    private ErpOrderInfoService erpOrderInfoService;
    @Resource
    private ErpOrderQueryService erpOrderQueryService;

    @PostMapping("/getNeedSignOrderQuantity")
    @ApiOperation(value = "查询门店未签收订单数量")
    public HttpResponse<ErpOrderSignResponse> getNeedSignOrderQuantity(@RequestBody ErpOrderSignRequest erpOrderSignRequest) {
        logger.info("查询门店未签收订单数量：{}", erpOrderSignRequest);
        HttpResponse response = HttpResponse.success();
        try {
            int needSignOrderQuantity = erpOrderQueryService.getNeedSignOrderQuantity(erpOrderSignRequest.getStoreId());
            ErpOrderSignResponse signResponse = new ErpOrderSignResponse();
            signResponse.setNeedSignOrderQuantity(needSignOrderQuantity);
            response.setData(signResponse);
        } catch (BusinessException e) {
            logger.error("查询待签收订单详情：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("查询待签收订单详情：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/getOrderSignDetail")
    @ApiOperation(value = "查询待签收订单详情")
    public HttpResponse<ErpOrderInfo> getOrderSignDetail(@RequestBody ErpOrderSignRequest erpOrderSignRequest) {
        logger.info("查询待签收订单详情：{}", erpOrderSignRequest);
        HttpResponse response = HttpResponse.success();
        try {
            ErpOrderInfo order = erpOrderQueryService.getNeedSignOrderDetailByOrderCode(erpOrderSignRequest.getOrderCode());
            response.setData(order);
        } catch (BusinessException e) {
            logger.error("查询待签收订单详情：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("查询待签收订单详情：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/orderSign")
    @ApiOperation(value = "订单签收")
    public HttpResponse orderSign(@RequestBody ErpOrderSignRequest erpOrderSignRequest) {
        logger.info("订单签收：{}", erpOrderSignRequest);
        HttpResponse response = HttpResponse.success();
        try {
            erpOrderInfoService.orderSign(erpOrderSignRequest);
        } catch (BusinessException e) {
            logger.error("订单签收异常：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("订单签收异常：{}", e);
            response = HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
        return response;
    }
}
