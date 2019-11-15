package com.aiqin.mgs.order.api.web;


import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.CartOrderInfo;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartRequest;
import com.aiqin.mgs.order.api.service.CartOrderService;
import com.aiqin.mgs.order.api.service.CartService;
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
@RequestMapping("/cartOrder")
@Api("购物车相关操作接口")
public class CartOrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);
    @Resource
    private CartOrderService cartOrderService;

    @PostMapping("/add")
    @ApiOperation(value = "将商品添加到购物车")
    public HttpResponse addNewCart(@Valid @RequestBody ShoppingCartRequest shoppingCartRequest) {
        CartOrderInfo cartOrderInfo = new CartOrderInfo();
        cartOrderInfo.setAmount(shoppingCartRequest.getAmount());
        cartOrderInfo.setColor(shoppingCartRequest.getColor());
        cartOrderInfo.setPrice(shoppingCartRequest.getPrice());
        cartOrderInfo.setLogo(shoppingCartRequest.getLogo());
        cartOrderInfo.setProductId(shoppingCartRequest.getProductId());
        cartOrderInfo.setDistributionType(shoppingCartRequest.getDistributionType());
        cartOrderInfo.setSkuId(shoppingCartRequest.getSkuId());
        cartOrderInfo.setSpuId(shoppingCartRequest.getSpuId());
        cartOrderInfo.setProductName(shoppingCartRequest.getProductName());
        cartOrderInfo.setProductSize(shoppingCartRequest.getProductSize());
        cartOrderInfo.setDistributorId(shoppingCartRequest.getDistributorId());
        LOGGER.info("将商品添加到购物车参数：{}",cartOrderInfo);
        return cartOrderService.addCartInfo(cartOrderInfo);
    }

}
