package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.domain.pay.PayReq;
import com.aiqin.mgs.order.api.service.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author jinghaibo
 * Date: 2019/11/12 13:49
 * Description:
 */
/**
 * 支付
 */
@Api(tags = "支付")
@RestController
@RequestMapping("/payment/pay")
@Slf4j
public class PayController {

    @Resource
    private PayService payService;
    @PostMapping("/getCodeUrl")
    @ApiOperation(value = "支付")
    public HttpResponse getCodeUrl(@RequestBody PayReq req) {
        return payService.doPay(req);
    }
}
