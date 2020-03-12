package com.aiqin.mgs.order.api.web.cart;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.request.cart.*;
import com.aiqin.mgs.order.api.domain.response.cart.*;
import com.aiqin.mgs.order.api.service.cart.ErpOrderCartService;
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
import javax.validation.Valid;

@RestController
@RequestMapping("/erpOrderCartController")
@Api(tags = "ERP购物车")
public class ErpOrderCartController {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderCartController.class);

    @Resource
    private ErpOrderCartService erpOrderCartService;

    @PostMapping("/queryErpCartList")
    @ApiOperation(value = "erp查询购物车列表")
    public HttpResponse<ErpCartQueryResponse> queryErpCartList(@RequestBody ErpCartQueryRequest erpCartQueryRequest) {
        HttpResponse<ErpCartQueryResponse> response = HttpResponse.success();
        try {
            AuthToken auth = AuthUtil.getCurrentAuth();
            ErpCartQueryResponse queryResponse = erpOrderCartService.queryErpCartList(erpCartQueryRequest, auth);
            response.setData(queryResponse);
        } catch (BusinessException e) {
            logger.error("查询购物车列表：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("查询购物车列表：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/queryStoreCartList")
    @ApiOperation(value = "爱掌柜查询购物车列表")
    public HttpResponse<ErpStoreCartQueryResponse> queryStoreCartList(@RequestBody ErpCartQueryRequest erpCartQueryRequest) {
        HttpResponse<ErpStoreCartQueryResponse> response = HttpResponse.success();
        try {
            AuthToken auth = AuthUtil.getCurrentAuth();
            ErpStoreCartQueryResponse queryResponse = erpOrderCartService.queryStoreCartList(erpCartQueryRequest, auth);
            response.setData(queryResponse);
        } catch (BusinessException e) {
            logger.error("查询购物车列表：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("查询购物车列表：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/addProduct")
    @ApiOperation(value = "添加商品到购物车")
    public HttpResponse<ErpOrderCartAddResponse> addProduct(@RequestBody ErpCartAddRequest erpCartAddRequest) {
        logger.info("添加商品到购物车：{}", erpCartAddRequest);
        HttpResponse<ErpOrderCartAddResponse> response = HttpResponse.success();
        try {
            AuthToken auth = AuthUtil.getCurrentAuth();
            erpOrderCartService.addProduct(erpCartAddRequest, auth);
        } catch (BusinessException e) {
            logger.error("添加商品到购物车失败：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("添加商品到购物车失败：{}", e);
            response = HttpResponse.failure(ResultCode.ADD_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/updateCartLineProduct")
    @ApiOperation(value = "修改购物车行数据")
    public HttpResponse updateCartLineProduct(@RequestBody ErpCartUpdateRequest erpCartUpdateRequest) {
        logger.info("修改购物车行数据：{}", erpCartUpdateRequest);
        HttpResponse response = HttpResponse.success();
        try {
            AuthToken auth = AuthUtil.getCurrentAuth();
            erpOrderCartService.updateCartLineProduct(erpCartUpdateRequest, auth);
        } catch (BusinessException e) {
            logger.error("修改购物车行数据失败：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("修改购物车行数据失败：{}", e);
            response = HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/changeGroupCheckStatus")
    @ApiOperation(value = "全选/全不选")
    public HttpResponse changeGroupCheckStatus(@RequestBody ErpCartChangeGroupCheckStatusRequest erpCartChangeGroupCheckStatusRequest) {
        logger.info("全选/全不选：{}", erpCartChangeGroupCheckStatusRequest);
        HttpResponse response = HttpResponse.success();
        try {
            AuthToken auth = AuthUtil.getCurrentAuth();
            erpOrderCartService.changeGroupCheckStatus(erpCartChangeGroupCheckStatusRequest, auth);
        } catch (BusinessException e) {
            logger.error("修改全选和反选状态失败：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("修改全选和反选状态失败：{}", e);
            response = HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
        return response;
    }

    @ApiOperation(value = "购物车删除单行")
    @PostMapping("/deleteCartLine")
    public HttpResponse deleteCartLine(@RequestBody ErpCartDeleteRequest erpCartDeleteRequest) {
        logger.info("删除购物车行：{}", erpCartDeleteRequest);
        HttpResponse response = HttpResponse.success();
        try {
            AuthToken auth = AuthUtil.getCurrentAuth();
            erpOrderCartService.deleteCartLine(erpCartDeleteRequest.getCartId());
        } catch (BusinessException e) {
            logger.error("删除购物车行：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("删除购物车行：{}", e);
            response = HttpResponse.failure(ResultCode.DELETE_EXCEPTION);
        }
        return response;
    }

    @ApiOperation(value = "清空购物车")
    @PostMapping("/deleteCartLine")
    public HttpResponse deleteAllCartLine(@RequestBody ErpCartQueryRequest erpCartQueryRequest) {
        logger.info("清空购物车：{}", erpCartQueryRequest);
        HttpResponse response = HttpResponse.success();
        try {
            AuthToken auth = AuthUtil.getCurrentAuth();
            erpOrderCartService.deleteAllCartLine(erpCartQueryRequest);
        } catch (BusinessException e) {
            logger.error("清空购物车失败：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("清空购物车失败：{}", e);
            response = HttpResponse.failure(ResultCode.DELETE_EXCEPTION);
        }
        return response;
    }

    @ApiOperation(value = "生成订单结算数据")
    @PostMapping("/generateCartGroupTemp")
    public HttpResponse<ErpGenerateCartGroupTempResponse> generateCartGroupTemp(@Valid @RequestBody ErpCartQueryRequest erpCartQueryRequest) {
        logger.info("生成订单结算数据：{}", erpCartQueryRequest);
        HttpResponse<ErpGenerateCartGroupTempResponse> response = HttpResponse.success();
        try {
            AuthToken auth = AuthUtil.getCurrentAuth();
            ErpGenerateCartGroupTempResponse key = erpOrderCartService.generateCartGroupTemp(erpCartQueryRequest, auth);
            response.setData(key);
        } catch (BusinessException e) {
            logger.error("生成订单结算数据失败：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("生成订单结算数据失败：{}", e);
            response = HttpResponse.failure(ResultCode.ADD_EXCEPTION);
        }
        return response;
    }

    @ApiOperation(value = "查询订单结算缓存数据")
    @PostMapping("/queryCartGroupTemp")
    public HttpResponse<ErpStoreCartQueryResponse> queryCartGroupTemp(@Valid @RequestBody ErpQueryCartGroupTempRequest erpQueryCartGroupTempRequest) {
        logger.info("查询订单结算缓存数据：{}", erpQueryCartGroupTempRequest);
        HttpResponse<ErpStoreCartQueryResponse> response = HttpResponse.success();
        try {
            AuthToken auth = AuthUtil.getCurrentAuth();
            ErpStoreCartQueryResponse queryResponse = erpOrderCartService.queryCartGroupTemp(erpQueryCartGroupTempRequest, auth);
            response.setData(queryResponse);
        } catch (BusinessException e) {
            logger.error("查询订单结算缓存数据失败：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("查询订单结算缓存数据失败：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/getCartProductTotalNum")
    @ApiOperation(value = "返回购物车中的商品总数量")
    public HttpResponse<Integer> getCartProductTotalNum(@Valid @RequestBody ErpCartQueryRequest erpCartQueryRequest) {
        HttpResponse<Integer> response = HttpResponse.success();
        try {
            AuthToken auth = AuthUtil.getCurrentAuth();
            int cartProductTotalNum = erpOrderCartService.getCartProductTotalNum(erpCartQueryRequest, auth);
            response.setData(cartProductTotalNum);
        } catch (BusinessException e) {
            logger.error("查询订单结算缓存数据失败：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("查询订单结算缓存数据失败：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/getStoreActivityAchieveDetail")
    @ApiOperation(value = "查询当前购物车活动条件满足情况")
    public HttpResponse<StoreActivityAchieveResponse> getStoreActivityAchieveDetail(@RequestBody StoreActivityAchieveRequest storeActivityAchieveRequest) {
        HttpResponse<StoreActivityAchieveResponse> httpResponse = HttpResponse.success();
        try {
            StoreActivityAchieveResponse storeActivityAchieveDetail = erpOrderCartService.getStoreActivityAchieveDetail(storeActivityAchieveRequest);
            httpResponse.setData(storeActivityAchieveDetail);
        } catch (BusinessException e) {
            logger.info("创建订单失败：{}", e);
            httpResponse = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("查询订单确认信息异常", e);
            httpResponse = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return httpResponse;
    }
}
