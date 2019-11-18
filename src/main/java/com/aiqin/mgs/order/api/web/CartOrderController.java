package com.aiqin.mgs.order.api.web;


import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.request.cart.DeleteCartProductRequest;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartRequest;
import com.aiqin.mgs.order.api.service.CartOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/cartOrder")
@Api("购物车相关操作接口")
public class CartOrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);
    @Resource
    private CartOrderService cartOrderService;



    /**
     * 添加商品到购物车
     *
     * @param shoppingCartRequest
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "将商品添加到购物车")
    public HttpResponse addCart(@Valid @RequestBody ShoppingCartRequest shoppingCartRequest) {
        //将商品添加到购物车
        return cartOrderService.addCart(shoppingCartRequest);
    }

    /**
     * 返回购物车中的商品的总数量
     * @param shoppingCartRequest
     * @return
     */
    @PostMapping("/getTotal")
    @ApiOperation(value = "返回购物车中的商品总数量")
    public HttpResponse getTotal(@Valid @RequestBody ShoppingCartRequest shoppingCartRequest) {
        return cartOrderService.getTotal(shoppingCartRequest.getStoreId());
    }


    /**
     * 购物车展示列表
     * @param
     * @return
     */
    @GetMapping("/cartDisplay")
    @ApiOperation(value = "购物车展示列表")
    public HttpResponse selectCartByStoreId(@Valid @RequestParam(name = "storeId", required = true) String storeId, Integer productType) {
        LOGGER.info("购物车展示列表参数：{},{}", storeId,productType);
        return cartOrderService.selectCartByStoreId(storeId,productType);
    }

    /**
     * 清空购物车、删除单个商品、删除勾选商品
     * @param deleteCartProductRequest
     * @return
     */
    @PostMapping("/deleteCart")
    @ApiOperation(value = "清空购物车、删除单个商品、删除勾选商品")
    public HttpResponse deleteCart(@Valid @RequestBody DeleteCartProductRequest deleteCartProductRequest){
        return cartOrderService.deleteCartInfo(deleteCartProductRequest.getStoreId(),deleteCartProductRequest.getSkuId(),deleteCartProductRequest.getLineCheckStatus());
    }


}
