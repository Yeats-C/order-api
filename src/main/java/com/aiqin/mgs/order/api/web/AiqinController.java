package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.exception.GroundRuntimeException;
import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.domain.request.order.QueryOrderListReqVO;
import com.aiqin.mgs.order.api.domain.response.order.QueryOrderInfoRespVO;
import com.aiqin.mgs.order.api.domain.response.order.QueryOrderListRespVO;
import com.aiqin.mgs.order.api.service.OrderService;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * @Description
 * @Author xieq
 * @Date 2020/3/4
 */
@RestController
@Slf4j
@Api(tags = "订单api")
@RequestMapping("/order")
public class AiqinController {
    @Autowired
    private OrderService orderService;

    @ApiOperation("订单列表")
    @PostMapping("/list")
    public HttpResponse<QueryOrderListRespVO> list(@RequestBody @Valid QueryOrderListReqVO reqVO){

        log.info("OrderController---list---param：[{}]", JSONObject.toJSONString(reqVO));
        try {
            return HttpResponse.success(orderService.list(reqVO));
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return HttpResponse.failure(ResultCode.SYSTEM_ERROR);
        }
    }

    @ApiOperation("订单详情")
    @GetMapping("/view")
    public HttpResponse<QueryOrderInfoRespVO> view(@RequestParam String orderCode){
        log.info("OrderController---view---param：[{}]", orderCode);
        try {
            return HttpResponse.success(orderService.view(orderCode));
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return HttpResponse.failure(ResultCode.SYSTEM_ERROR);
        }
    }
}
