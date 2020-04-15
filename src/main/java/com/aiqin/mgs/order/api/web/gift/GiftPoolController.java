package com.aiqin.mgs.order.api.web.gift;


import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.gift.GiftCartQueryResponse;
import com.aiqin.mgs.order.api.domain.po.gift.GiftPool;
import com.aiqin.mgs.order.api.domain.request.cart.ErpCartAddRequest;
import com.aiqin.mgs.order.api.domain.request.cart.ErpCartDeleteRequest;
import com.aiqin.mgs.order.api.domain.request.cart.ErpCartQueryRequest;
import com.aiqin.mgs.order.api.domain.response.cart.ErpCartQueryResponse;
import com.aiqin.mgs.order.api.domain.response.cart.ErpOrderCartAddResponse;
import com.aiqin.mgs.order.api.service.gift.GiftPoolService;
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

@RestController
@RequestMapping("/giftPool")
@Api(tags = "兑换赠品池相关接口")
public class GiftPoolController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GiftPoolController.class);
    @Resource
    private GiftPoolService giftPoolService;

    /**
     * 添加兑换赠品池赠品
     *
     * @param
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加兑换赠品池赠品")
    public HttpResponse add(@RequestBody GiftPool giftPool) {
        return giftPoolService.add(giftPool);
    }

    /**
     * 查询赠品池列表
     * @param
     * @return
     */
    @PostMapping("/getGiftPoolList")
    @ApiOperation(value = "查询赠品池列表")
    public HttpResponse<PageResData<GiftPool>> getGiftPoolList(@RequestBody GiftPool giftPool){
        return giftPoolService.getGiftPoolList(giftPool);
    }

    @PostMapping("/addGiftToCart")
    @ApiOperation(value = "添加兑换赠品到购物车")
    public HttpResponse<ErpOrderCartAddResponse> addGiftToCart(@RequestBody ErpCartAddRequest erpCartAddRequest) {
        LOGGER.info("添加商品到购物车：{}", erpCartAddRequest);
        HttpResponse<ErpOrderCartAddResponse> response = HttpResponse.success();
        try {
            AuthToken auth = AuthUtil.getCurrentAuth();
            giftPoolService.addProduct(erpCartAddRequest, auth);
        } catch (BusinessException e) {
            LOGGER.error("添加商品到购物车失败：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("添加商品到购物车失败：{}", e);
            response = HttpResponse.failure(ResultCode.ADD_EXCEPTION);
        }
        return response;
    }

    /**
     * 修改兑换赠品池赠品状态
     *
     * @param
     * @return
     */
    @PostMapping("/updateUseStatus")
    @ApiOperation(value = "修改兑换赠品池赠品状态")
    public HttpResponse updateUseStatus(@RequestBody GiftPool giftPool) {
        return giftPoolService.updateUseStatus(giftPool);
    }

    /**
     * 爱掌柜通过门店id及筛选项查询赠品池列表
     * @param
     * @return
     */
    @PostMapping("/getGiftPoolListByStoreId")
    @ApiOperation(value = "爱掌柜通过门店id及筛选项查询赠品池列表")
    public HttpResponse<GiftCartQueryResponse> getGiftPoolListByStoreId(@RequestBody GiftPool giftPool){
        return giftPoolService.getGiftPoolListByStoreId(giftPool);
    }

    @PostMapping("/queryGiftCartList")
    @ApiOperation(value = "爱掌柜查询赠品购物车列表")
    public HttpResponse<ErpCartQueryResponse> queryGiftCartList(@RequestBody ErpCartQueryRequest erpCartQueryRequest) {
        HttpResponse<ErpCartQueryResponse> response = HttpResponse.success();
        try {
            AuthToken auth = AuthUtil.getCurrentAuth();
            ErpCartQueryResponse queryResponse = giftPoolService.queryGiftCartList(erpCartQueryRequest, auth);
            response.setData(queryResponse);
        } catch (BusinessException e) {
            LOGGER.error("查询购物车列表：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("查询购物车列表：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @ApiOperation(value = "购物车删除单行")
    @PostMapping("/deleteCartLine")
    public HttpResponse deleteCartLine(@RequestBody ErpCartDeleteRequest erpCartDeleteRequest) {
        LOGGER.info("删除购物车行：{}", erpCartDeleteRequest);
        HttpResponse response = HttpResponse.success();
        try {
            AuthToken auth = AuthUtil.getCurrentAuth();
            giftPoolService.deleteCartLine(erpCartDeleteRequest.getCartId());
        } catch (BusinessException e) {
            LOGGER.error("删除购物车行：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("删除购物车行：{}", e);
            response = HttpResponse.failure(ResultCode.DELETE_EXCEPTION);
        }
        return response;
    }

    @ApiOperation(value = "清空兑换赠品池购物车")
    @PostMapping("/deleteAllCartLine")
    public HttpResponse deleteAllCartLine(@RequestBody ErpCartQueryRequest erpCartQueryRequest) {
        LOGGER.info("清空购物车：{}", erpCartQueryRequest);
        HttpResponse response = HttpResponse.success();
        try {
            giftPoolService.deleteAllCartLine(erpCartQueryRequest);
        } catch (BusinessException e) {
            LOGGER.error("清空购物车失败：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("清空购物车失败：{}", e);
            response = HttpResponse.failure(ResultCode.DELETE_EXCEPTION);
        }
        return response;
    }

}
