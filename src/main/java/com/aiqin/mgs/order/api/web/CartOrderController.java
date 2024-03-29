package com.aiqin.mgs.order.api.web;


import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.CartOrderInfo;
import com.aiqin.mgs.order.api.domain.request.cart.DeleteCartProductRequest;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartRequest;
import com.aiqin.mgs.order.api.domain.request.cart.StoreActivityAchieveRequest;
import com.aiqin.mgs.order.api.domain.response.cart.CartResponse;
import com.aiqin.mgs.order.api.domain.response.cart.StoreActivityAchieveResponse;
import com.aiqin.mgs.order.api.domain.response.cart.StoreCartProductResponse;
import com.aiqin.mgs.order.api.service.CartOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(CartOrderController.class);
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
    public HttpResponse addCart(@Valid @RequestBody ShoppingCartRequest shoppingCartRequest, AuthToken authToken) {
        //将商品添加到购物车
        return cartOrderService.addCart(shoppingCartRequest,authToken);
    }

    /**
     * 返回购物车中的商品的总数量
     * @param shoppingCartRequest
     * @return
     */
    @PostMapping("/getTotal")
    @ApiOperation(value = "返回购物车中的商品总数量")
    public HttpResponse<Integer> getTotal(@Valid @RequestBody ShoppingCartRequest shoppingCartRequest) {
        return cartOrderService.getTotal(shoppingCartRequest.getStoreId());
    }


    /**
     * 购物车展示列表,如果勾选了商品，就要连数量一起更新
     * @param
     * @return
     */
    @GetMapping("/cartDisplay")
    @ApiOperation(value = "购物车展示列表,附带勾选功能，仅erp使用")
    @ApiImplicitParams({@ApiImplicitParam(name = "store_id", value = "门店id", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "product_type", value = "商品类型 1:直送 2:配送 3:货架", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(name = "sku_code", value = "sku编码", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(name = "line_check_status", value = "勾选标记,0:未勾选,1:勾选单个商品,2:全部勾选,3:全部取消", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(name = "number", value = "商品数量", dataType = "Integer", paramType = "query", required = false)})
    public HttpResponse<CartResponse> selectCartByStoreId(String store_id, Integer product_type, String sku_code, Integer line_check_status, Integer number) {
        LOGGER.info("购物车展示列表参数：{},{},{},{},{}", store_id,product_type,sku_code,line_check_status,number);
        return cartOrderService.selectCartByStoreId(store_id,product_type,sku_code,line_check_status,number);
    }

    /**
     * 购物车展示列表,如果勾选了商品，就要连数量一起更新
     * 仅爱掌柜调用
     * @param
     * @return
     */
    @GetMapping("/queryCartByStoreId")
    @ApiOperation(value = "购物车展示列表,附带勾选功能，仅爱掌柜调用")
    @ApiImplicitParams({@ApiImplicitParam(name = "store_id", value = "门店id", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "product_type", value = "商品类型 1:直送 2:配送 3:货架", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(name = "sku_code", value = "sku编码", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(name = "line_check_status", value = "勾选标记,0:未勾选,1:勾选单个商品,2:全部勾选,3:全部取消", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(name = "number", value = "商品数量", dataType = "Integer", paramType = "query", required = false),
            @ApiImplicitParam(name = "activity_id", value = "活动id", dataType = "String", paramType = "query", required = false)
    })
    public HttpResponse<StoreCartProductResponse> queryCartByStoreId(String store_id, Integer product_type, String sku_code, Integer line_check_status, Integer number, String activity_id) {
        LOGGER.info("购物车展示列表参数：{},{},{},{},{}", store_id,product_type,sku_code,line_check_status,number,activity_id);
        return cartOrderService.queryCartByStoreId(store_id,product_type,sku_code,line_check_status,number,activity_id);
    }

    /**
     * 清空购物车、删除单个商品、删除勾选商品
     * @param deleteCartProductRequest
     * @return
     */
    @PostMapping("/deleteCart")
    @ApiOperation(value = "清空购物车、删除单个商品、删除勾选商品")
    public HttpResponse deleteCart(@Valid @RequestBody DeleteCartProductRequest deleteCartProductRequest){
        return cartOrderService.deleteCartInfo(deleteCartProductRequest.getStoreId(),deleteCartProductRequest.getSkuId(),deleteCartProductRequest.getLineCheckStatus(),deleteCartProductRequest.getProductType());
    }

    /**
     * 显示勾选商品列表
     * @param cartOrderInfo
     * @return
     */
    @PostMapping("/displayCartLineCheckProduct")
    @ApiOperation(value = "显示勾选商品列表")
    public HttpResponse<StoreCartProductResponse> displayCartLineCheckProduct(CartOrderInfo cartOrderInfo){
        return cartOrderService.displayCartLineCheckProduct(cartOrderInfo);
    }

    @PostMapping("/getStoreActivityAchieveDetail")
    @ApiOperation(value = "查询当前购物车活动条件满足情况")
    public HttpResponse<StoreActivityAchieveResponse> getStoreActivityAchieveDetail(@RequestBody StoreActivityAchieveRequest storeActivityAchieveRequest) {
        HttpResponse<StoreActivityAchieveResponse> httpResponse = HttpResponse.success();
        try {
            StoreActivityAchieveResponse storeActivityAchieveDetail = cartOrderService.getStoreActivityAchieveDetail(storeActivityAchieveRequest);
            httpResponse.setData(storeActivityAchieveDetail);
        } catch (BusinessException e) {
            LOGGER.info("创建订单失败：{}", e);
            httpResponse = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("查询订单确认信息异常", e);
            httpResponse = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return httpResponse;
    }
}
