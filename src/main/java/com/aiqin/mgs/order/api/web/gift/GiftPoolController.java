package com.aiqin.mgs.order.api.web.gift;


import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.po.gift.GiftPool;
import com.aiqin.mgs.order.api.service.gift.GiftPoolService;
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

}
