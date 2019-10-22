package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.request.StoreValueOrderPayRequest;
import com.aiqin.mgs.order.api.service.OrderService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * ━━━━━━神兽出没━━━━━━
 * 　　┏┓　　　┏┓+ +
 * 　┏┛┻━━━┛┻┓ + +
 * 　┃　　　　　　　┃
 * 　┃　　　━　　　┃ ++ + + +
 * ████━████ ┃+
 * 　┃　　　　　　　┃ +
 * 　┃　　　┻　　　┃
 * 　┃　　　　　　　┃
 * 　┗━┓　　　┏━┛
 * 　　　┃　　　┃                  神兽保佑, 永无BUG!
 * 　　　┃　　　┃
 * 　　　┃　　　┃     Code is far away from bug with the animal protecting
 * 　　　┃　 　　┗━━━┓
 * 　　　┃ 　　　　　　　┣┓
 * 　　　┃ 　　　　　　　┏┛
 * 　　　┗┓┓┏━┳┓┏┛
 * 　　　　┃┫┫　┃┫┫
 * 　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * <p>
 * <p>
 * 思维方式*热情*能力
 */
@RequestMapping("/store-value")
@RestController
@Api(tags = "储值订单支付相关")
public class StoreValueOrderController {
    private static Logger LOGGER = LoggerFactory.getLogger(StoreValueOrderController.class);

    @Resource
    private OrderService orderService;

    /**
     * 储值支付更改订单信息
     * @param storeValueOrderPayRequest
     * @return
     */
    @PutMapping("/order")
    public HttpResponse updateOrderInfo(@RequestBody StoreValueOrderPayRequest storeValueOrderPayRequest){
        LOGGER.info("储值支付更改订单信息:{}",JsonUtil.toJson(storeValueOrderPayRequest));
        return orderService.updateOrderInfo(storeValueOrderPayRequest);
    }





}
