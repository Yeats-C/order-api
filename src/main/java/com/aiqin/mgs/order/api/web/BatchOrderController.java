package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.OrderodrInfo;
import com.aiqin.mgs.order.api.service.OrderDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author jinghaibo
 * Date: 2020/2/15 15:13
 * Description:
 */

@RestController
@RequestMapping("/batch")
@Api("批量提交订单")
public class BatchOrderController {

    @Resource
    private OrderDetailService orderDetailService;
    /**
     * 微商城-添加/修改购物车信息
     * @param cartInfo
     * @return
     */
    @PostMapping("/batchUploadOrder")
    @ApiOperation(value = "微商城-添加/修改购物车信息0:添加1:修改....")
    public HttpResponse batchAddOrder(@Valid @RequestBody List<OrderodrInfo> cartInfo) {
        return orderDetailService.batchAddOrder(cartInfo);
    }
}
