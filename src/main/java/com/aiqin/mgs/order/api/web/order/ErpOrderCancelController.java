package com.aiqin.mgs.order.api.web.order;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderCancelRequest;
import com.aiqin.mgs.order.api.service.order.*;
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
 * @date 2019/12/9 11:53
 */
@RestController
@RequestMapping("/erpOrderCancelController")
@Api(tags = "ERP订单取消")
public class ErpOrderCancelController {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderCancelController.class);

    @Resource
    private ErpOrderCancelNoTransactionalService erpOrderCancelNoTransactionalService;

    @PostMapping("/cancelOrderWithoutStock")
    @ApiOperation(value = "缺货终止交易")
    public HttpResponse cancelOrderWithoutStock(@RequestBody ErpOrderCancelRequest erpOrderCancelRequest) {
        logger.info("发起缺货终止交易：{}", erpOrderCancelRequest);
        HttpResponse response = HttpResponse.success();
        try {
            AuthUtil.loginCheck();
            AuthToken auth = AuthUtil.getCurrentAuth();
            erpOrderCancelNoTransactionalService.cancelOrderWithoutStock(erpOrderCancelRequest, auth);
        } catch (BusinessException e) {
            logger.error("异常信息：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("异常信息：{}", e);
            response = HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/applyCancelOrder")
    @ApiOperation(value = "申请取消订单")
    public HttpResponse applyCancelOrder(@RequestBody ErpOrderCancelRequest erpOrderCancelRequest) {
        logger.info("发起申请取消订单：{}", erpOrderCancelRequest);
        HttpResponse response = HttpResponse.success();
        try {
            AuthUtil.loginCheck();
            AuthToken auth = AuthUtil.getCurrentAuth();
            erpOrderCancelNoTransactionalService.applyCancelOrder(erpOrderCancelRequest, auth);
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
