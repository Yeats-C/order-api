package com.aiqin.mgs.order.api.web.order;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayWayEnum;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.OrderGiveApproval;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderPayRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderSaveRequest;
import com.aiqin.mgs.order.api.service.order.ErpOrderCreateService;
import com.aiqin.mgs.order.api.service.order.ErpOrderPayNoTransactionalService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.platform.flows.client.domain.vo.FormCallBackVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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
    private ErpOrderPayNoTransactionalService erpOrderPayNoTransactionalService;

    @PostMapping("/erpSaveOrder")
    @ApiOperation(value = "erp从购物车创建订单")
    public HttpResponse<ErpOrderInfo> erpSaveOrder(@RequestBody ErpOrderSaveRequest erpOrderSaveRequest) {
        logger.info("erp从购物车创建订单：{}", erpOrderSaveRequest);
        HttpResponse response = HttpResponse.success();
        try {
            AuthUtil.loginCheck();
            AuthToken auth = AuthUtil.getCurrentAuth();
            ErpOrderInfo erpOrderInfo = erpOrderCreateService.erpSaveOrder(erpOrderSaveRequest, auth);
            response.setData(erpOrderInfo);

            ErpOrderPayRequest payRequest = new ErpOrderPayRequest();
            payRequest.setOrderCode(erpOrderInfo.getOrderStoreCode());
            payRequest.setPayWay(ErpPayWayEnum.PAY_1.getCode());
            erpOrderPayNoTransactionalService.orderPayStartMethodGroup(payRequest, auth, true);

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
        logger.info("爱掌柜从购物车创建订单：{}", erpOrderSaveRequest);
        HttpResponse response = HttpResponse.success();
        try {
            AuthUtil.loginCheck();
            AuthToken auth = AuthUtil.getCurrentAuth();
            ErpOrderInfo erpOrderInfo = erpOrderCreateService.storeSaveOrder(erpOrderSaveRequest, auth);
            response.setData(erpOrderInfo);

            ErpOrderPayRequest payRequest = new ErpOrderPayRequest();
            payRequest.setOrderCode(erpOrderInfo.getOrderStoreCode());
            payRequest.setPayWay(ErpPayWayEnum.PAY_1.getCode());
            erpOrderPayNoTransactionalService.orderPayStartMethodGroup(payRequest, auth, true);

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
        logger.info("erp创建货架订单：{}", erpOrderSaveRequest);
        HttpResponse response = HttpResponse.success();
        try {
            AuthUtil.loginCheck();
            AuthToken auth = AuthUtil.getCurrentAuth();
            ErpOrderInfo erpOrderInfo = erpOrderCreateService.saveRackOrder(erpOrderSaveRequest, auth);
            response.setData(erpOrderInfo);

            ErpOrderPayRequest payRequest = new ErpOrderPayRequest();
            payRequest.setOrderCode(erpOrderInfo.getOrderStoreCode());
            payRequest.setPayWay(ErpPayWayEnum.PAY_1.getCode());
            erpOrderPayNoTransactionalService.orderPayStartMethodGroup(payRequest, auth, true);

        } catch (BusinessException e) {
            logger.info("创建货架订单失败：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.info("创建货架订单失败：{}", e);
            response = HttpResponse.failure(ResultCode.ADD_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/callback")
    @ApiOperation("订单金额超出市值赠送金额--审批流回调地址")
    public String getByBrandName(@RequestBody FormCallBackVo formCallBackVo) {
        return erpOrderCreateService.callback(formCallBackVo);
    }

    @ApiOperation("订单金额超出市值赠送金额--根据formNo查询审批详情")
    @GetMapping("/formDetail/{form_no}")
    public HttpResponse<OrderGiveApproval> getDetailByformNo(@PathVariable(value = "form_no") String formNo) {
        return HttpResponse.success(erpOrderCreateService.getDetailByformNo(formNo));
    }

    @PostMapping("/saveWholesaleOrder")
    @ApiOperation(value = "创建批发订单")
    public HttpResponse<ErpOrderInfo> saveWholesaleOrder(@RequestBody ErpOrderSaveRequest erpOrderSaveRequest) {
        logger.info("erp创建批发订单：{}", erpOrderSaveRequest);
        HttpResponse response = HttpResponse.success();
        try {
            AuthUtil.loginCheck();
            AuthToken auth = AuthUtil.getCurrentAuth();
            ErpOrderInfo erpOrderInfo = erpOrderCreateService.saveWholesaleOrder(erpOrderSaveRequest, auth);
            response.setData(erpOrderInfo);

            ErpOrderPayRequest payRequest = new ErpOrderPayRequest();
            payRequest.setOrderCode(erpOrderInfo.getOrderStoreCode());
            payRequest.setPayWay(ErpPayWayEnum.PAY_1.getCode());
            erpOrderPayNoTransactionalService.orderPayStartMethodGroup(payRequest, auth, true);

        } catch (BusinessException e) {
            logger.info("创建批发订单失败：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.info("创建批发订单失败：{}", e);
            response = HttpResponse.failure(ResultCode.ADD_EXCEPTION);
        }
        return response;
    }

}
