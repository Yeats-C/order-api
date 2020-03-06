package com.aiqin.mgs.order.api.service.purchase;


import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.OperationLog;
import com.aiqin.mgs.order.api.domain.PurchaseOrderInfo;
import com.aiqin.mgs.order.api.domain.request.purchase.*;
import com.aiqin.mgs.order.api.domain.response.OrderOperationLog;
import com.aiqin.mgs.order.api.domain.response.purchase.*;

import java.util.List;

public interface PurchaseManageService {

    HttpResponse<List<PurchaseOrder>> purchaseOrderList(PurchaseApplyRequest purchaseApplyRequest);

    HttpResponse<PurchaseOrder> purchaseOrderDetails(String purchaseOrderId);

    HttpResponse<PurchaseOrderProduct> purchaseOrderProduct(PurchaseOrderProductRequest request);

    HttpResponse<List<OrderOperationLog>> purchaseOrderLog(String operationId);

    HttpResponse<PurchaseApplyProductInfoResponse> purchaseOrderAmount(String purchaseOrderId);
}