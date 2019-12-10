package com.aiqin.mgs.order.api.web.order;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderDeliverRequest;
import com.aiqin.mgs.order.api.service.order.ErpOrderInfoService;
import com.aiqin.mgs.order.api.service.order.ErpOrderQueryService;
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
 * 订单发货controller
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 11:54
 */
@RestController
@RequestMapping("/erpOrderDeliverController")
@Api("订单发货")
public class ErpOrderDeliverController {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderDeliverController.class);

    @Resource
    private ErpOrderInfoService erpOrderInfoService;
    @Resource
    private ErpOrderQueryService erpOrderQueryService;

    @PostMapping("/orderDeliver")
    @ApiOperation(value = "订单发货")
    public HttpResponse orderDeliver(@RequestBody ErpOrderDeliverRequest erpOrderSendRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            AuthUtil.loginCheck();
            erpOrderInfoService.orderDeliver(erpOrderSendRequest);
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }
}
