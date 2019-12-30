package com.aiqin.mgs.order.api.web.bill;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.dao.OperationLogDao;
import com.aiqin.mgs.order.api.dao.PurchaseOrderDao;
import com.aiqin.mgs.order.api.dao.PurchaseOrderDetailDao;
import com.aiqin.mgs.order.api.domain.PurchaseInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.service.bill.PurchaseOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/purchase")
@Api(tags = "同步采购单")
public class PurchaseOrderController {

    @Resource
    private PurchaseOrderService purchaseOrderService;

    /**
     * 同步采购单
     *
     * @param erpOrderInfo
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "根据销售单，生成爱亲采购单")
    public HttpResponse synchronizationPurchaseOrder(@Valid @RequestBody ErpOrderInfo erpOrderInfo) {
        return purchaseOrderService.createPurchaseOrder(erpOrderInfo);
    }

    /**
     * 栖耘销售单回传
     */
    @PostMapping("info")
    @ApiOperation(value = "耘链销售单回传")
    public HttpResponse<PurchaseInfo> selectPurchaseInfo() {
        return purchaseOrderService.selectPurchaseInfo();
    }
}
