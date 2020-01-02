//package com.aiqin.mgs.order.api.web.bill;
//
//import com.aiqin.ground.util.protocol.http.HttpResponse;
//import com.aiqin.mgs.order.api.domain.DeliveryInfoVo;
//import com.aiqin.mgs.order.api.domain.OrderIogisticsVo;
//import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
//import com.aiqin.mgs.order.api.service.bill.PurchaseOrderService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import javax.validation.Valid;
//import java.util.List;
//
///**
// * 爱亲采购单 控制器
// */
//@RestController
//@RequestMapping("/purchase")
//@Api(tags = "爱亲采购单")
//public class PurchaseOrderController {
//
//    @Resource
//    private PurchaseOrderService purchaseOrderService;
//
//    /**
//     * 同步采购单
//     *
//     * @param erpOrderInfo
//     * @return
//     */
//    @PostMapping("/add")
//    @ApiOperation(value = "根据销售单，生成爱亲采购单")
//    public HttpResponse synchronizationPurchaseOrder(@Valid @RequestBody ErpOrderInfo erpOrderInfo) {
//        return purchaseOrderService.createPurchaseOrder(erpOrderInfo);
//    }
//
//    /**
//     * 耘链销售单回传
//     */
//    @PostMapping("info")
//    @ApiOperation(value = "耘链销售单回传")
//    public HttpResponse updatePurchaseInfo(@Valid @RequestBody OrderIogisticsVo orderIogisticsVo) {
//        return purchaseOrderService.updatePurchaseInfo(orderIogisticsVo);
//    }
//
//    /**
//     * 发运单回传
//     */
//    @PostMapping("istics")
//    @ApiOperation(value = "发运单回传")
//    public HttpResponse updateOrderStoreLogistics(@Valid @RequestBody List<DeliveryInfoVo> deliveryInfoVo) {
//        return purchaseOrderService.updateOrderStoreLogistics(deliveryInfoVo);
//    }
//}
