package com.aiqin.mgs.order.api.web;


import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.CartOrderInfo;
import com.aiqin.mgs.order.api.domain.constant.Global;
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
import java.util.List;

@RestController
@RequestMapping("/cartOrder")
@Api(tags = "购物车相关接口")
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
     * 购物车展示列表,如果勾选了商品，就要连数量一起更新
     * @param
     * @return
     */
    @GetMapping("/cartDisplay")
    @ApiOperation(value = "购物车展示列表,附带勾选功能")
    public HttpResponse selectCartByStoreId(@Valid @RequestParam(name = "storeId", required = true) String storeId,
                                            @Valid @RequestParam(name = "productType", required = true)Integer productType,
                                            @Valid @RequestParam(name = "skuId", required = false)String skuId,
                                            @Valid @RequestParam(name = "lineCheckStatus", required = false)Integer lineCheckStatus,
                                            @Valid @RequestParam(name = "number", required = false)Integer number) {
        LOGGER.info("购物车展示列表参数：{},{},{},{},{}", storeId,productType,skuId,lineCheckStatus,number);
        return cartOrderService.selectCartByStoreId(storeId,productType,skuId,lineCheckStatus,number);
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

    /**
     * 显示勾选商品列表
     * @param cartOrderInfo
     * @return
     */
    @PostMapping("/displayCartLineCheckProduct")
    @ApiOperation(value = "显示勾选商品列表")
    public HttpResponse<List<CartOrderInfo>> displayCartLineCheckProduct(CartOrderInfo cartOrderInfo){
        return cartOrderService.displayCartLineCheckProduct(cartOrderInfo);
    }

}
