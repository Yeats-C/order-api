package com.aiqin.mgs.order.api.web.bill;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.request.bill.PurchaseOrderReq;
import com.aiqin.mgs.order.api.service.bill.PurchaseOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/purchase")
@Api(tags = "销售相关操作接口")
public class PurchaseOrderController {

    @Resource
    private PurchaseOrderService purchaseOrderService;

    /**
     * 创建销售
     * @param purchaseOrderReq
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加销售单")
    public HttpResponse createSaleOrder(@Valid @RequestBody PurchaseOrderReq purchaseOrderReq){
        return purchaseOrderService.createSaleOrder(purchaseOrderReq);
    }

}
