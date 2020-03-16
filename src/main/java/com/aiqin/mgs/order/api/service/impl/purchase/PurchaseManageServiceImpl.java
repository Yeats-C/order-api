package com.aiqin.mgs.order.api.service.impl.purchase;

import com.aiqin.ground.util.protocol.http.HttpResponse;

import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.OperationLogDao;
import com.aiqin.mgs.order.api.dao.PurchaseOrderDao;
import com.aiqin.mgs.order.api.dao.PurchaseOrderDetailDao;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.request.purchase.PurchaseApplyRequest;
import com.aiqin.mgs.order.api.domain.request.purchase.PurchaseOrderProductRequest;
import com.aiqin.mgs.order.api.domain.response.OrderOperationLog;
import com.aiqin.mgs.order.api.domain.response.purchase.*;
import com.aiqin.mgs.order.api.service.purchase.PurchaseManageService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;


@Service
public class PurchaseManageServiceImpl implements PurchaseManageService {
    private static final BigDecimal big = BigDecimal.valueOf(0);

    @Resource
    private PurchaseOrderDao purchaseOrderDao;
    @Resource
    private PurchaseOrderDetailDao purchaseOrderDetailDao;
    @Resource
    private OperationLogDao operationLogDao;

    @Override
    public HttpResponse<List<PurchaseOrder>> purchaseOrderList(PurchaseApplyRequest purchaseApplyRequest){
        PageResData pageResData = new PageResData();
        List<PurchaseOrderResponse> list = purchaseOrderDao.purchaseOrderList(purchaseApplyRequest);
        Integer count = purchaseOrderDao.purchaseOrderCount(purchaseApplyRequest);
        pageResData.setDataList(list);
        pageResData.setTotalCount(count);
        return HttpResponse.success(pageResData);
    }

    @Override
    public HttpResponse<PurchaseOrder> purchaseOrderDetails(String purchaseOrderId){
        if(StringUtils.isBlank(purchaseOrderId)){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
        PurchaseOrder detail = purchaseOrderDao.purchaseOrder(purchaseOrderId);
        return HttpResponse.success(detail);
    }

    @Override
    public HttpResponse<PurchaseOrderProduct> purchaseOrderProduct(PurchaseOrderProductRequest request){
        PageResData pageResData = new PageResData();
        if(StringUtils.isBlank(request.getPurchaseOrderId())){
            return HttpResponse.success(pageResData);
        }
        List<PurchaseOrderProduct> list = purchaseOrderDetailDao.purchaseOrderList(request);
        Integer count = purchaseOrderDetailDao.purchaseOrderCount(request);
        pageResData.setDataList(list);
        pageResData.setTotalCount(count);
        return HttpResponse.success(pageResData);
    }

    @Override
    public HttpResponse<List<OrderOperationLog>> purchaseOrderLog(String operationId){
        if(StringUtils.isBlank(operationId)){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
        List<OrderOperationLog> list = operationLogDao.list(operationId);
        return HttpResponse.success(list);
    }

    @Override
    public HttpResponse<PurchaseApplyProductInfoResponse> purchaseOrderAmount(String purchaseOrderId){
        if(StringUtils.isBlank(purchaseOrderId)){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
        // 计算采购单的数量与金额
        PurchaseApplyProductInfoResponse amountResponse = new PurchaseApplyProductInfoResponse();
        Integer productPieceSum = 0, matterPieceSum = 0, giftPieceSum = 0;
        Integer productSingleSum= 0, matterSingleSum = 0, giftSingleSum = 0;
        Integer actualProductPieceSum = 0, actualMatterPieceSum = 0, actualGiftPieceSum = 0;
        BigDecimal productTaxSum = big, matterTaxSum = big, giftTaxSum = big;
        Integer singleSum = 0, priceSum = 0, actualSingleSum = 0, actualPriceSum = 0;
        Integer actualProductSingleSum= 0, actualMatterSingleSum = 0, actualGiftSingleSum = 0;
        BigDecimal actualProductTaxSum = big, actualMatterTaxSum = big, actualGiftTaxSum = big;
        PurchaseOrderProductRequest request = new PurchaseOrderProductRequest();
        request.setPurchaseOrderId(purchaseOrderId);
        request.setIsPage(1);
        List<PurchaseOrderProduct> orderProducts = purchaseOrderDetailDao.purchaseOrderList(request);
        if(CollectionUtils.isNotEmpty(orderProducts)){
            for(PurchaseOrderProduct order:orderProducts){
                // 商品采购件数量
                Integer purchaseWhole = order.getPurchaseWhole() == null ? 0 : order.getPurchaseWhole();
                Integer purchaseSingle = order.getPurchaseSingle() == null ? 0 : order.getPurchaseSingle();
                // 包装数量
                Integer packNumber = order.getBaseProductContent() == null ? 0 : order.getBaseProductContent();
                BigDecimal amount = order.getProductAmount() == null ? big : order.getProductAmount();
                Integer singleCount = purchaseWhole * packNumber + purchaseSingle;
                singleSum += singleCount;
                priceSum += purchaseWhole;
                // 实际
                Integer actualSingleCount = order.getActualSingleCount() == null ? 0: order.getActualSingleCount();
                Integer actualWhole = 0;
                if (packNumber != 0 ) {
                    actualWhole = actualSingleCount / packNumber;
                }
                actualPriceSum += actualWhole;
                actualSingleSum += actualSingleCount;
                if(order.getProductType().equals(Global.PRODUCT_TYPE_0)){
                    productPieceSum += purchaseWhole;
                    productSingleSum += singleCount;
                    productTaxSum = amount.multiply(BigDecimal.valueOf(singleCount)).setScale(4, BigDecimal.ROUND_HALF_UP).add(productTaxSum);
                    actualProductPieceSum += actualWhole;
                    actualProductSingleSum += actualSingleCount;
                    actualProductTaxSum = amount.multiply(BigDecimal.valueOf(actualSingleCount)).setScale(4, BigDecimal.ROUND_HALF_UP).add(actualProductTaxSum);
                }else if(order.getProductType().equals(Global.PRODUCT_TYPE_2)){
                    matterPieceSum += purchaseWhole;
                    matterSingleSum += singleCount;
                    matterTaxSum = amount.multiply(BigDecimal.valueOf(singleCount)).setScale(4, BigDecimal.ROUND_HALF_UP).add(matterTaxSum);
                    actualMatterPieceSum += actualWhole;
                    actualMatterSingleSum += actualSingleCount;
                    actualMatterTaxSum = amount.multiply(BigDecimal.valueOf(actualSingleCount)).setScale(4, BigDecimal.ROUND_HALF_UP).add(actualMatterTaxSum);
                }else if(order.getProductType().equals(Global.PRODUCT_TYPE_1)){
                    giftPieceSum += purchaseWhole;
                    giftSingleSum += singleCount;
                    giftTaxSum = amount.multiply(BigDecimal.valueOf(singleCount)).setScale(4, BigDecimal.ROUND_HALF_UP).add(giftTaxSum);
                    actualGiftPieceSum += actualWhole;
                    actualGiftSingleSum += actualSingleCount;
                    actualGiftTaxSum = amount.multiply(BigDecimal.valueOf(actualSingleCount)).setScale(4, BigDecimal.ROUND_HALF_UP).add(actualGiftTaxSum);
                }
            }
            // 采购
            amountResponse.setProductPieceSum(productPieceSum);
            amountResponse.setProductSingleSum(productSingleSum);
            amountResponse.setProductTaxSum(productTaxSum);
            amountResponse.setMatterPieceSum(matterPieceSum);
            amountResponse.setMatterSingleSum(matterSingleSum);
            amountResponse.setMatterTaxSum(matterTaxSum);
            amountResponse.setGiftPieceSum(giftPieceSum);
            amountResponse.setGiftSingleSum(giftSingleSum);
            amountResponse.setGiftTaxSum(giftTaxSum);
            amountResponse.setSingleSum(singleSum);
            amountResponse.setPieceSum(priceSum);

            // 实际
            amountResponse.setActualProductPieceSum(actualProductPieceSum);
            amountResponse.setActualProductSingleSum(actualProductSingleSum);
            amountResponse.setActualProductTaxSum(actualProductTaxSum);
            amountResponse.setActualMatterPieceSum(actualMatterPieceSum);
            amountResponse.setActualMatterSingleSum(actualMatterSingleSum);
            amountResponse.setActualMatterTaxSum(actualMatterTaxSum);
            amountResponse.setActualGiftPieceSum(actualGiftPieceSum);
            amountResponse.setActualGiftSingleSum(actualGiftSingleSum);
            amountResponse.setActualGiftTaxSum(actualGiftTaxSum);
            amountResponse.setActualPieceSum(actualPriceSum);
            amountResponse.setActualSingleSum(actualSingleSum);
        }
        return HttpResponse.success(amountResponse);
    }
}
