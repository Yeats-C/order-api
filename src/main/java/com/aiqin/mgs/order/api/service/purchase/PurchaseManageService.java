package com.aiqin.mgs.order.api.service.purchase;


import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.PurchaseBatch;
import com.aiqin.mgs.order.api.domain.request.purchase.*;
import com.aiqin.mgs.order.api.domain.response.purchase.*;

public interface PurchaseManageService {

    HttpResponse<PageResData<PurchaseOrder>> purchaseOrderList(PurchaseApplyRequest purchaseApplyRequest);

    HttpResponse<PurchaseDetailResponse> purchaseOrderDetails(String purchaseOrderId);

    HttpResponse<PageResData<PurchaseOrderProduct>> purchaseOrderProduct(PurchaseOrderProductRequest request);

    HttpResponse insertBatch(PurchaseBatch purchaseBatch);

    HttpResponse<PageResData<PurchaseBatch>> purchaseOrderProductBatch(PurchaseOrderProductRequest request);
}
