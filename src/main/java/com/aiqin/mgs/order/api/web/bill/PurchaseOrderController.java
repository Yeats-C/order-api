package com.aiqin.mgs.order.api.web.bill;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.DeliveryInfoVo;
import com.aiqin.mgs.order.api.domain.OrderIogisticsVo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.service.bill.PurchaseOrderService;
import com.aiqin.mgs.order.api.service.order.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 爱亲采购单 控制器
 */
@RestController
@RequestMapping("/purchase")
@Api(tags = "爱亲采购单")
public class PurchaseOrderController {

    @Resource
    private PurchaseOrderService purchaseOrderService;
    @Resource
    private ErpOrderPayService erpOrderPayService;
    @Resource
    private ErpOrderPayNoTransactionalService erpOrderPayNoTransactionalService;
    /**
     * 同步采购单
     *
     * @param erpOrderInfo
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "根据销售单，生成爱亲采购单")
    public HttpResponse synchronizationPurchaseOrder(@Valid @RequestBody ErpOrderInfo erpOrderInfo) {
        List<ErpOrderInfo> list = new ArrayList<>();
        list.add(erpOrderInfo);
        return purchaseOrderService.createPurchaseOrder(list);
    }

    /**
     * 耘链销售单回传
     */
    @PostMapping("/sale/info")
    @ApiOperation(value = "耘链销售单回传")
    public HttpResponse updatePurchaseInfo(@Valid @RequestBody OrderIogisticsVo orderIogisticsVo) {
        try {
            return purchaseOrderService.updatePurchaseInfo(orderIogisticsVo);
        } catch (BusinessException e) {
            return HttpResponse.failure(MessageId.create(null,-1,e.getMessage()));
        } catch (Exception e) {
            return HttpResponse.failure(MessageId.create(null,-1,e.getMessage()));
        }
    }

    /**
     * 发运单回传
     */
    @PostMapping("/delivery/info")
    @ApiOperation(value = "发运单回传")
    public HttpResponse updateOrderStoreLogistics(@Valid @RequestBody DeliveryInfoVo deliveryInfoVo) {
        return purchaseOrderService.updateOrderStoreLogistics(deliveryInfoVo);
    }

    /**
     * 取消订单
     */
    @PostMapping("/cancel/info")
    @ApiOperation(value = "取消订单")
    public HttpResponse updateCancelOrderinfo(String orderStoreCode) {
        return purchaseOrderService.updateCancelOrderinfo(orderStoreCode);
    }

    @GetMapping("/测试【触发订单 生成采购单】test1")
    public HttpResponse test1(@RequestParam  String orderStoreCode) {
        HttpResponse response = HttpResponse.success();
        try {

            AuthToken auth = new AuthToken();
            auth.setPersonId("123456");
            auth.setPersonName("1234567890");
            erpOrderPayNoTransactionalService.orderPaySuccessMethodGroup(orderStoreCode, auth);

        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }
}
