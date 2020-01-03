package com.aiqin.mgs.order.api.web.order;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayWayEnum;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderPayRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderSaveRequest;
import com.aiqin.mgs.order.api.service.order.ErpOrderCreateService;
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
 * 订单创建controller
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 11:49
 */
@RestController
@RequestMapping("/erpOrderCreateController")
@Api(tags = "ERP订单创建")
public class ErpOrderCreateController {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderCreateController.class);

    @Resource
    private ErpOrderCreateService erpOrderCreateService;

    @Resource
    private ErpOrderPayService erpOrderPayService;

    @PostMapping("/erpSaveOrder")
    @ApiOperation(value = "erp从购物车创建订单")
    public HttpResponse<ErpOrderInfo> erpSaveOrder(@RequestBody ErpOrderSaveRequest erpOrderSaveRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            AuthUtil.loginCheck();
            AuthToken auth = AuthUtil.getCurrentAuth();
            ErpOrderInfo erpOrderInfo = erpOrderCreateService.erpSaveOrder(erpOrderSaveRequest, auth);
            response.setData(erpOrderInfo);

            ErpOrderPayRequest payRequest = new ErpOrderPayRequest();
            payRequest.setOrderCode(erpOrderInfo.getOrderStoreCode());
            payRequest.setPayWay(ErpPayWayEnum.PAY_1.getCode());
            erpOrderPayService.orderPayStartMethodGroup(payRequest, auth, true);

        } catch (BusinessException e) {
            logger.info("创建订单失败：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.info("创建订单失败：{}", e);
            response = HttpResponse.failure(ResultCode.ADD_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/storeSaveOrder")
    @ApiOperation(value = "爱掌柜从购物车创建订单")
    public HttpResponse<ErpOrderInfo> storeSaveOrder(@RequestBody ErpOrderSaveRequest erpOrderSaveRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            AuthUtil.loginCheck();
            AuthToken auth = AuthUtil.getCurrentAuth();
            ErpOrderInfo erpOrderInfo = erpOrderCreateService.storeSaveOrder(erpOrderSaveRequest, auth);
            response.setData(erpOrderInfo);

            ErpOrderPayRequest payRequest = new ErpOrderPayRequest();
            payRequest.setOrderCode(erpOrderInfo.getOrderStoreCode());
            payRequest.setPayWay(ErpPayWayEnum.PAY_1.getCode());
            erpOrderPayService.orderPayStartMethodGroup(payRequest, auth, true);

        } catch (BusinessException e) {
            logger.info("创建订单失败：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.info("创建订单失败：{}", e);
            response = HttpResponse.failure(ResultCode.ADD_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/saveRackOrder")
    @ApiOperation(value = "创建货架订单")
    public HttpResponse<ErpOrderInfo> saveRackOrder(@RequestBody ErpOrderSaveRequest erpOrderSaveRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            AuthUtil.loginCheck();
            AuthToken auth = AuthUtil.getCurrentAuth();
            ErpOrderInfo erpOrderInfo = erpOrderCreateService.saveRackOrder(erpOrderSaveRequest, auth);
            response.setData(erpOrderInfo);

            ErpOrderPayRequest payRequest = new ErpOrderPayRequest();
            payRequest.setOrderCode(erpOrderInfo.getOrderStoreCode());
            payRequest.setPayWay(ErpPayWayEnum.PAY_1.getCode());
            erpOrderPayService.orderPayStartMethodGroup(payRequest, auth, true);

        } catch (BusinessException e) {
            logger.info("创建货架订单失败：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.info("创建货架订单失败：{}", e);
            response = HttpResponse.failure(ResultCode.ADD_EXCEPTION);
        }
        return response;
    }
}
