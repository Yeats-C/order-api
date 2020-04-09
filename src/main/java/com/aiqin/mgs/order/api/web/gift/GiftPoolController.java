package com.aiqin.mgs.order.api.web.gift;


import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.gift.GiftPool;
import com.aiqin.mgs.order.api.domain.request.cart.ErpCartAddRequest;
import com.aiqin.mgs.order.api.domain.response.cart.ErpOrderCartAddResponse;
import com.aiqin.mgs.order.api.service.gift.GiftPoolService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/addGift")
    @ApiOperation(value = "添加兑换赠品到购物车")
    public HttpResponse<ErpOrderCartAddResponse> addGift(@RequestBody ErpCartAddRequest erpCartAddRequest) {
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

}
