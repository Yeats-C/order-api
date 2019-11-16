package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.OrderStoreCart;
import com.aiqin.mgs.order.api.service.ErpCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 购物车
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/14 18:07
 */
@RestController
@RequestMapping("/erpCartController")
@Api("购物车相关接口类")
public class ErpCartController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErpCartController.class);

    @Resource
    private ErpCartService erpCartService;

    @PostMapping("/findCartProductList")
    @ApiOperation(value = "查询购物车列表")
    public HttpResponse findCartProductList(@RequestBody OrderStoreCart orderStoreCart) {
        HttpResponse response = HttpResponse.success();
        try {
            List<OrderStoreCart> list = erpCartService.findCartProductList(orderStoreCart);
            response.setData(list);
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("异常信息：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/addCartProduct")
    @ApiOperation(value = "添加商品到购物车")
    public HttpResponse addCartProduct(@RequestBody OrderStoreCart orderStoreCart) {
        HttpResponse response = HttpResponse.success();
        try {
            erpCartService.addCartProduct(orderStoreCart);
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("异常信息：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/deleteCartProduct")
    @ApiOperation(value = "从购物车删除商品")
    public HttpResponse deleteCartProduct(@RequestBody OrderStoreCart orderStoreCart) {
        HttpResponse response = HttpResponse.success();
        try {
            erpCartService.deleteCartProduct(orderStoreCart);
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("异常信息：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

}
